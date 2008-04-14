/* in_action/chapter11/FoobarSvgTextHandler */
package in_action.chapter11;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
public class FoobarSvgTextHandler extends DefaultHandler {
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

	/** Inner class that holds a text, its starting coordinate and an angle */
	protected class Street {
		/** the street name */
		public String name;

		/** the x coordinate */
		public float x;

		/** the y coordinate */
		public float y;

		/** the angle */
		public float alpha;

		/** the fontsize */
		public int fontsize;

		/**
		 * @param dx
		 * @param dy
		 */
		public void calcAlfa(float dx, float dy) {
			alpha = (float) (Math.atan((double) ((dy - y) / (dx - x))) * 180 / Math.PI);
		}
	}

	/** Content between the tags. */
	protected StringBuffer buf = new StringBuffer();

	/** Current font-size */
	protected int fontsize;

	/** Current id */
	protected String link;

	/** Hashmap with the street names. */
	protected HashMap map = new HashMap();

	/** PageSize and Viewbox parameters. */
	protected float[] coordinates = new float[6];

	/**
	 * @param is
	 *            the inputstream with the SVG
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public FoobarSvgTextHandler(InputSource is) throws FileNotFoundException,
			SAXException, IOException, ParserConfigurationException,
			FactoryConfigurationError {
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(is, this);
	}

	/**
	 * Returns a map with Streets.
	 * 
	 * @return a map with Streets and their coordinates.
	 */
	public HashMap getStreets() {
		return map;
	}

	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("text".equals(qName)) {
			storeFontSize(attributes);
		} else if ("textPath".equals(qName)) {
			storeId(attributes);
		} else if ("path".equals(qName)) {
			registerPath(attributes);
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
		if ("textPath".equals(qName)) {
			Street street = (Street) map.get(link);
			street.name = buf.toString();
			street.fontsize = fontsize;
		}
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
	 * Draws a path.
	 * 
	 * @param attributes
	 *            the attributes of the path-tag
	 */
	private void registerPath(Attributes attributes) {
		String id = attributes.getValue("id");
		String value = attributes.getValue("d");
		Street street = new Street();
		if (value == null)
			return;
		int digit;
		iXY ixy;
		for (int i = 0; i < value.length(); i++) {
			digit = (int) value.charAt(i);
			switch (digit) {
			case 'M':
				ixy = new iXY(value, i);
				street.x = ixy.x;
				street.y = coordinates[5] - ixy.y;
				i = ixy.i;
				break;
			case 'L':
				ixy = new iXY(value, i);
				street.calcAlfa(ixy.x, coordinates[5] - ixy.y);
				i = ixy.i;
				break;
			}
		}
		map.put("#" + id, street);
	}

	/**
	 * @param attributes
	 *            attributes of the text-tag
	 */
	private void storeFontSize(Attributes attributes) {
		fontsize = Integer.parseInt(attributes.getValue("font-size"));
	}

	/**
	 * @param attributes
	 *            the attributes of the textPath-tag
	 */
	private void storeId(Attributes attributes) {
		link = attributes.getValue("xlink:href");
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
}
