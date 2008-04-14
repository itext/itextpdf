/** chapter09/Peace.java */
package in_action.chapter09;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Peace extends DefaultHandler {

	/** Holds he fonts that can be used for the peace message. */
	public FontSelector fs;

	/** The columns that contains the message. */
	protected PdfPTable table;

	/** The language. */
	protected String language;

	/** The countries. */
	protected String countries;

	/** Indicates when the text should be written from right to left. */
	protected boolean rtl;

	/** The StringBuffer that holds the characters. */
	protected StringBuffer buf = new StringBuffer();

	/**
	 * Creates the handler for the pace.xml file.
	 */
	public Peace() {
		fs = new FontSelector();
		fs.addFont(FontFactory.getFont("c:/windows/fonts/arialuni.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		fs.addFont(FontFactory.getFont("resources/in_action/chapter09/abserif4_5.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		fs.addFont(FontFactory.getFont("resources/in_action/chapter09/damase.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		fs.addFont(FontFactory.getFont("resources/in_action/chapter09/fsex2p00_public.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
		table = new PdfPTable(3);
		table.getDefaultCell().setPadding(3);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);
	}

	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if ("pace".equals(qName)) {
			buf = new StringBuffer();
			language = attributes.getValue("language");
			countries = attributes.getValue("countries");
			if ("RTL".equals(attributes.getValue("direction"))) {
				rtl = true;
			} else {
				rtl = false;
			}
		}
	}

	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("pace".equals(qName)) {
			PdfPCell cell = new PdfPCell();
			cell.addElement(fs.process(buf.toString()));
			cell.setPadding(3);
			cell.setUseAscender(true);
			cell.setUseDescender(true);
			if (rtl) {
				cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			}
			table.addCell(language);
			table.addCell(cell);
			table.addCell(countries);
		}
	}

	/**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buf.append(ch, start, length);
	}

	/**
	 * Returns the table.
	 * 
	 * @return the PdfPTable (get it after using the parser)
	 */
	public PdfPTable getTable() {
		return table;
	}

	/**
	 * This example reads a text file written in UTF-8. Each line is a pipe
	 * delimited array containing the name of a language, the word 'peace'
	 * written in that language
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args) {

		System.out.println("Chapter 9: example peace in all languages");
		System.out.println("-> Creates a PDF file with a table containing");
		System.out.println("   the word peace in different languages.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: c:/windows/fonts/arialuni.ttf");
		System.out.println("->                   abserif4_5.ttf, damase.ttf and");
		System.out.println("                     fsex2p00_public.ttf");
		System.out.println("->                   the file peace.xml");

		// step 1
		Document doc = new Document(PageSize.A4.rotate());
		try {
			// step 2 (creating the writer)
			PdfWriter.getInstance(doc, new FileOutputStream("results/in_action/chapter09/peace.pdf"));
			// step 3
			doc.open();
			// step 4
			Peace p = new Peace();
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(new InputSource(new FileInputStream(
					"resources/in_action/chapter09/peace.xml")), p);
			doc.add(p.getTable());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// step 5
		doc.close();
	}
}