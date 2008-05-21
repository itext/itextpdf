/* in_action/chapter10/FoobarSvgHandler */
package in_action.chapter10;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import com.lowagie.text.html.WebColors;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 * 
 * WARNING: iText doesn't really do SVG parsing. You should use Batik in
 * combination with iText's PdfGraphics2D if you want full blown SVG parsing.
 * This class only demonstrates how you could write an SVG parser if you would
 * want to.
 */
public class FoobarSvgHandler extends DefaultHandler {
	/** Inner class that holds an int and two floats */
	protected class iXY {
		/** position of the next character in a data string */
		public int i;

		/** x coordinate */
		public float x;

		/** y coordinate */
		public float y;

		/**
		 * @param value
		 * @param pos
		 */
		public iXY(String value, int pos) {
			i = pos;
			char digit;
			// skip extra non-numerical characters
			while (i < value.length()) {
				digit = value.charAt(i);
				if (digit >= '0' && digit <= '9')
					break;
				if (digit == '.')
					break;
				i++;
			}
			pos = i;
			// retrieve the x value
			while (i < value.length()) {
				digit = value.charAt(i);
				if (digit != '.' && (digit < '0' || digit > '9')) {
					x = getFloat(value.substring(pos));
					break;
				}
				i++;
			}
			// skip extra non-numerical characters
			while (i < value.length()) {
				digit = value.charAt(i);
				if (digit >= '0' && digit <= '9')
					break;
				if (digit == '.')
					break;
				i++;
			}
			pos = i;
			// retrieve the y value
			while (i < value.length()) {
				digit = value.charAt(i);
				if (digit != '.' && (digit < '0' || digit > '9')) {
					y = getFloat(value.substring(pos));
					break;
				}
				i++;
				if (i == value.length()) {
					y = getFloat(value.substring(pos));
				}
			}
		}
	}

	/** The PdfWriter that is writing the PDF. */
	protected PdfWriter writer;

	/** The PdfContentByte we are going to draw to. */
	protected PdfTemplate template = null;

	/** Content between the tags. */
	protected StringBuffer buf = new StringBuffer();

	/** PageSize and Viewbox parameters. */
	protected float[] coordinates = new float[6];

	/**
	 * Creates a FoobarSvgHandler that will create a PdfTemplate for the writer,
	 * containing the image that is provided through InputSource.
	 * 
	 * @param writer
	 *            the PdfWriter responsible for the PDF
	 * @param is
	 *            the inputstream with the SVG
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public FoobarSvgHandler(PdfWriter writer, InputSource is)
			throws FileNotFoundException, SAXException, IOException,
			ParserConfigurationException, FactoryConfigurationError {
		this.writer = writer;
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(is, this);
	}

	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("polyline".equals(qName)) {
			drawPolyline(attributes);
		} else if ("path".equals(qName)) {
			drawPath(attributes);
		} else if ("svg".equals(qName)) {
			calcSize(attributes);
		}
	}

	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		buf = new StringBuffer();
	}

	/**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buf.append(ch, start, length);
	}

	/**
	 * Draws a polyline.
	 * 
	 * @param attributes
	 *            the attributes of the polyline-tag
	 */
	private void drawPolyline(Attributes attributes) {
		template.saveState();
		setFill(attributes);
		setStroke(attributes);
		computePoints(attributes);
		template.stroke();
		template.restoreState();
	}

	/**
	 * Draws a path.
	 * 
	 * @param attributes
	 *            the attributes of the path-tag
	 */
	private void drawPath(Attributes attributes) {
		template.saveState();
		setFill(attributes);
		setStroke(attributes);
		computeData(attributes);
		template.stroke();
		template.restoreState();
	}

	/**
	 * Sets the fill color.
	 * 
	 * @param attributes
	 *            attributes that can contain a fill attribute
	 */
	private void setFill(Attributes attributes) {
		String name = attributes.getValue("fill");
		if (name == null || "none".equals(name))
			return;
		Color c = WebColors.getRGBColor(name);
		if (c != null) {
			template.setColorFill(c);
		}
	}

	/**
	 * Sets the stroke color.
	 * 
	 * @param attributes
	 *            attributes that can contain a fill attribute
	 */
	private void setStroke(Attributes attributes) {
		String name = attributes.getValue("stroke");
		if (name == null || "none".equals(name))
			return;
		Color c = WebColors.getRGBColor(name);
		if (c != null) {
			template.setColorStroke(c);
		}
		name = attributes.getValue("stroke-width");
		if (name == null)
			return;
		float w = getPixels(name);
		template.setLineWidth(w);
	}

	/**
	 * Computes the data of the <code>d</code> attribute.
	 * 
	 * @param attributes
	 */
	private void computeData(Attributes attributes) {
		String value = attributes.getValue("d");
		if (value == null)
			return;
		int digit;
		iXY ixy;
		for (int i = 0; i < value.length(); i++) {
			digit = (int) value.charAt(i);
			switch (digit) {
			case 'M':
				ixy = new iXY(value, i);
				template.moveTo(ixy.x, coordinates[5] - ixy.y);
				i = ixy.i;
				break;
			case 'm':
				break;
			case 'L':
				ixy = new iXY(value, i);
				template.lineTo(ixy.x, coordinates[5] - ixy.y);
				i = ixy.i;
				break;
			case 'l':
				break;
			case 'z':
			case 'Z':
				template.closePathFillStroke();
				break;
			}
		}
	}

	/**
	 * Computes the data of the <code>d</code> attribute.
	 * 
	 * @param attributes
	 */
	private void computePoints(Attributes attributes) {
		String value = attributes.getValue("points");
		if (value == null)
			return;
		int i = 0;
		iXY ixy = new iXY(value, i);
		template.moveTo(ixy.x, coordinates[5] - ixy.y);
		i = ixy.i;
		while (i < value.length()) {
			ixy = new iXY(value, i);
			template.lineTo(ixy.x, coordinates[5] - ixy.y);
			i = ixy.i;
		}
	}

	/**
	 * Sets the pages size of the document.
	 * 
	 * @param attributes
	 *            the attributes of the svg-tag.
	 */
	private void calcSize(Attributes attributes) {
		String width, height;
		width = attributes.getValue("width");
		coordinates[0] = getPixels(width);
		height = attributes.getValue("height");
		coordinates[1] = getPixels(height);
		coordinates[2] = 0;
		coordinates[3] = 0;
		coordinates[4] = coordinates[0];
		coordinates[5] = coordinates[1];
		String viewBox = attributes.getValue("viewBox");
		if (viewBox != null) {
			StringTokenizer tokenizer = new StringTokenizer(viewBox);
			if (tokenizer.hasMoreElements()) {
				coordinates[2] = getFloat(tokenizer.nextToken());
			}
			if (tokenizer.hasMoreElements()) {
				coordinates[3] = getFloat(tokenizer.nextToken());
			}
			if (tokenizer.hasMoreElements()) {
				coordinates[4] = getFloat(tokenizer.nextToken());
			}
			if (tokenizer.hasMoreElements()) {
				coordinates[5] = getFloat(tokenizer.nextToken());
			}
		}
		PdfContentByte content = writer.getDirectContent();
		template = content.createTemplate(coordinates[4], coordinates[5]);
	}

	/**
	 * Parses a string that represents a float.
	 * 
	 * @param value
	 *            the string
	 * @return the float defined by the String
	 */
	private float getFloat(String value) {
		char digit;
		float total = 0;
		int decimals = 0;
		boolean decimal = false;
		for (int i = 0; i < value.length(); i++) {
			digit = value.charAt(i);
			if (digit == '.') {
				decimal = true;
				continue;
			}
			if (digit < '0' || digit > '9') {
				break;
			}
			total = (total * 10) + (digit - '0');
			if (decimal)
				decimals++;
		}
		total = total / (float) Math.pow(10, decimals);
		return total;
	}

	/**
	 * Parses a string that represents a length.
	 * 
	 * @param length
	 *            the string
	 * @return the length defined by the String in pixels.
	 */
	private float getPixels(String length) {
		float total = getFloat(length);
		if (length.endsWith("pt"))
			return total / 1.25f;
		if (length.endsWith("pc"))
			return total / 15f;
		if (length.endsWith("pt"))
			return total / 1.25f;
		if (length.endsWith("mm"))
			return total / 3.543307f;
		if (length.endsWith("cm"))
			return total / 35.443307f;
		if (length.endsWith("in"))
			return total / 90f;
		return total;
	}

	/**
	 * Returns the PdfTemplate as a com.lowagie.text.Image.
	 * 
	 * @return a com.lowagie.text.Image
	 * @throws BadElementException
	 */
	public Image getImage() throws BadElementException {
		return Image.getInstance(template);
	}

	/**
	 * Returns the PdfTemplate.
	 * 
	 * @return a com.lowagie.text.pdf.PdfTemplate
	 */
	public PdfTemplate getTemplate() {
		return template;
	}
}
