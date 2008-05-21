/* in_action/chapter09/SayPeace.java */

package in_action.chapter09;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SayPeace extends DefaultHandler {
	/** The font that is used for the peace message. */
	public Font f;

	/** The document to which we are going to add our message. */
	protected Document document;

	/** The StringBuffer that holds the characters. */
	protected StringBuffer buf = new StringBuffer();

	/** The columns that contains the message. */
	protected MultiColumnText column = null;

	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("message".equals(qName)) {
			buf = new StringBuffer();
			column = new MultiColumnText();
			column.addSimpleColumn(36, PageSize.A4.getWidth() - 36);
			if ("RTL".equals(attributes.getValue("direction"))) {
				column.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			}
		}
	}

	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		try {
			if ("big".equals(qName)) {
				Chunk bold = new Chunk(strip(buf), f);
				bold.setTextRenderMode(
						PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 0.5f,
						new Color(0x00, 0x00, 0x00));
				Paragraph p = new Paragraph(bold);
				p.setAlignment(Element.ALIGN_LEFT);
				column.addElement(p);
			}
			if ("message".equals(qName)) {
				Paragraph p = new Paragraph(strip(buf), f);
				p.setAlignment(Element.ALIGN_LEFT);
				column.addElement(p);
				document.add(column);
				column = null;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
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
	 * Replaces all the newline characters by a space.
	 * 
	 * @param buf
	 *            the original StringBuffer
	 * @return a String without newlines
	 */
	protected String strip(StringBuffer buf) {
		int pos;
		while ((pos = buf.indexOf("\n")) != -1)
			buf.replace(pos, pos + 1, " ");
		while (buf.charAt(0) == ' ')
			buf.deleteCharAt(0);
		return buf.toString();
	}

	/**
	 * Creates the handler to read the peace message.
	 * 
	 * @param document
	 * @param is
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public SayPeace(Document document, InputSource is)
			throws ParserConfigurationException, SAXException,
			FactoryConfigurationError, DocumentException, IOException {
		this.document = document;
		f = new Font(BaseFont.createFont("c:/windows/fonts/arialuni.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(is, this);
	}

	/**
	 * Generates a PDF with a Peace message in English, Arabic and Hebrew.
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: SayPeace");
		System.out.println("-> Creates a PDF file with a peace message.");
		System.out.println("-> resources needed: arialuni.ttf");
		System.out.println("                     the file say_peace.xml");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: say_peace.pdf");
		// step 1:
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/say_peace.pdf"));
			// step 3:
			document.open();
			// step 4:
			new SayPeace(document, new InputSource(new FileInputStream(
					"resources/in_action/chapter09/say_peace.xml")));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		// step 5: we close the document
		document.close();
	}
}