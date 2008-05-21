/* in_action/chapter13/CourseCatalogBookmarked.java */

package in_action.chapter13;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.SimpleCell;
import com.lowagie.text.SimpleTable;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfPageLabels;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class CourseCatalogBookmarked extends DefaultHandler {

	/** This is a Stack that holds objects. */
	protected Stack objectStack;

	/** The plain font. */
	protected Font font = new Font(Font.UNDEFINED, 9);

	/** The title of the course */
	protected String title;

	/**
	 * Creates a new FoobarHtmlHandler.
	 * 
	 * @param course
	 * 
	 * @throws DocumentException
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws SAXException
	 * @throws IOException
	 */
	public CourseCatalogBookmarked(String course) throws DocumentException,
			ParserConfigurationException, FactoryConfigurationError,
			FileNotFoundException, SAXException, IOException {
		objectStack = new Stack();
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(new InputSource(new FileInputStream(
				"resources/in_action/chapter07/" + course + ".xml")), this);
	}

	/**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch, start, length).trim();
		if (s.length() > 0)
			objectStack.add(new Phrase(s + " ", font));
	}

	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		try {
			if ("title".equals(qName)) {
				font.setSize(14);
			} else if ("specs".equals(qName)) {
				SimpleTable table = new SimpleTable();
				table.setWidthpercentage(100);
				table.setBorderWidth(0.5f);
				table.setCellpadding(3);
				objectStack.push(table);
			} else if ("programs".equals(qName) || "lecturers".equals(qName)) {
				objectStack.push(new List(false, 5));
			} else if ("tagline".equals(qName)) {
				objectStack.push(new Paragraph("Tagline:", new Font(
						Font.UNDEFINED, 12, Font.BOLD)));
			} else if ("description".equals(qName)) {
				objectStack.push(new Paragraph("Description:", new Font(
						Font.UNDEFINED, 12, Font.BOLD)));
			} else if ("contents".equals(qName)) {
				objectStack.push(new Paragraph("Contents:", new Font(
						Font.UNDEFINED, 12, Font.BOLD)));
				objectStack.push(new List(false, 5));
			} else if ("subtopics".equals(qName)) {
				objectStack.push(new List(false, 8));
			} else if ("img".equals(qName)) {
				objectStack.push(Image.getInstance("resources/in_action/chapter07/"
						+ attributes.getValue("src")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		try {
			if ("title".equals(qName)) {
				Phrase p = (Phrase) objectStack.pop();
				title = p.getContent();
				objectStack.push(new Paragraph(p));
				font = new Font(Font.UNDEFINED, 9);
				objectStack.push(new Paragraph("\n", font));
			} else if ("coursenumber".equals(qName)) {
				addRow("Course Number:");
			} else if ("program".equals(qName) || "topic".equals(qName)) {
				addListItem();
			} else if ("tagline".equals(qName) || "description".equals(qName)) {
				objectStack.push(getParagraph());
			} else if ("programs".equals(qName)) {
				addRow("Lectured in:");
			} else if ("a".equals(qName)) {
				addRow("Theory", "A");
			} else if ("b".equals(qName)) {
				addRow("Exercises", "B");
			} else if ("c".equals(qName)) {
				addRow("Training and Projects:", "C");
			} else if ("d".equals(qName)) {
				addRow("Study Time:", "D");
			} else if ("e".equals(qName)) {
				addRow("Credits:", "E");
			} else if ("department".equals(qName)) {
				addRow("Department:");
			} else if ("language".equals(qName)) {
				addRow("Language of Instruction:");
			} else if ("lecturer".equals(qName)) {
				addListItem();
			} else if ("lecturers".equals(qName)) {
				addRow("Lecturers:");
			} else if ("specs".equals(qName)
					|| "newline".equalsIgnoreCase(qName)) {
				objectStack.push(Chunk.NEWLINE);
			} else if ("subtopics".equals(qName)) {
				List l = (List) objectStack.pop();
				ListItem li = new ListItem();
				li.add(getParagraph());
				List list = (List) objectStack.pop();
				list.add(li);
				list.add(l);
				objectStack.push(list);
			} else if ("book".equals(qName)) {
				addTable();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addTable() throws BadElementException {
		SimpleTable table = new SimpleTable();
		table.setWidthpercentage(60);
		table.setCellpadding(3);
		table.setCellspacing(5);
		table.setBorderWidth(1);
		SimpleCell cell = new SimpleCell(SimpleCell.CELL);
		cell.addElement(getParagraph());
		cell.setBorderWidth(0.5f);
		cell.setWidthpercentage(75);
		SimpleCell row = new SimpleCell(SimpleCell.ROW);
		SimpleCell img = new SimpleCell(SimpleCell.CELL);
		img.setWidthpercentage(25);
		img.add((Image) objectStack.pop());
		img.setBorderWidth(0.5f);
		row.addElement(img);
		row.addElement(cell);
		table.add(row);
		objectStack.push(Chunk.NEWLINE);
		objectStack.push(table);
	}

	private Paragraph getParagraph() {
		ArrayList list = new ArrayList();
		while (objectStack.peek() instanceof Chunk
				|| objectStack.peek() instanceof Phrase) {
			list.add(objectStack.pop());
		}
		Collections.reverse(list);
		Paragraph p = new Paragraph();
		p.setAlignment(Element.ALIGN_JUSTIFIED);
		for (Iterator i = list.iterator(); i.hasNext();) {
			p.add(i.next());
		}
		return p;
	}

	private void addRow(String key) throws BadElementException {
		Element e = (Element) objectStack.pop();
		SimpleTable table = (SimpleTable) objectStack.pop();
		SimpleCell row = new SimpleCell(SimpleCell.ROW);
		row.setBorder(SimpleCell.BOTTOM);
		row.setBorderWidth(0.5f);
		SimpleCell cell;
		cell = new SimpleCell(SimpleCell.CELL);
		row.setBorder(SimpleCell.RIGHT);
		row.setBorderWidth(0.5f);
		cell
				.addElement(new Phrase(key, new Font(Font.UNDEFINED, 9,
						Font.BOLD)));
		row.add(cell);
		cell = new SimpleCell(SimpleCell.CELL);
		cell.addElement(e);
		cell.setColspan(2);
		row.add(cell);
		table.add(row);
		objectStack.push(table);
	}

	private void addRow(String key1, String key2) throws BadElementException {
		Element e = (Element) objectStack.pop();
		SimpleTable table;
		if (e instanceof SimpleTable) {
			table = (SimpleTable) e;
			e = new Phrase();
		} else {
			table = (SimpleTable) objectStack.pop();
		}
		SimpleCell row = new SimpleCell(SimpleCell.ROW);
		row.setBorder(SimpleCell.BOTTOM);
		row.setBorderWidth(0.5f);
		SimpleCell cell;
		cell = new SimpleCell(SimpleCell.CELL);
		row.setBorder(SimpleCell.RIGHT);
		row.setBorderWidth(0.5f);
		cell
				.addElement(new Phrase(key1, new Font(Font.UNDEFINED, 9,
						Font.BOLD)));
		cell.setWidthpercentage(30);
		row.add(cell);
		cell = new SimpleCell(SimpleCell.CELL);
		row.setBorder(SimpleCell.RIGHT);
		row.setBorderWidth(0.5f);
		cell
				.addElement(new Phrase(key2, new Font(Font.UNDEFINED, 9,
						Font.BOLD)));
		cell.setWidthpercentage(10);
		row.add(cell);
		cell = new SimpleCell(SimpleCell.CELL);
		cell.addElement(e);
		cell.setWidthpercentage(60);
		row.add(cell);
		table.add(row);
		objectStack.push(table);
	}

	private void addListItem() {
		if (objectStack.peek() instanceof Phrase) {
			Phrase p = (Phrase) objectStack.pop();
			List l = (List) objectStack.pop();
			ListItem li = new ListItem(p);
			l.add(li);
			objectStack.push(l);
		}
	}

	/**
	 * @param doc
	 * @throws DocumentException
	 */
	public void flushToDocument(Document doc) throws DocumentException {
		for (Iterator i = objectStack.iterator(); i.hasNext();) {
			Element e = (Element) i.next();
			doc.add(e);
		}
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Generates a fancy flyer in PDF for the new faculty of the Technological
	 * University of Foobar.
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(final String[] args) {

		System.out.println("Chapter 13: Course Catalog with bookmarks");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   course_catalog_bookmarks.pdf");
		try {
			Document document = new Document();
			OutputStream outPDF = new FileOutputStream(
					"results/in_action/chapter13/course_catalog_bookmarks.pdf");
			PdfWriter writer = PdfWriter.getInstance(document, outPDF);
			writer.setViewerPreferences(PdfWriter.PageLayoutSinglePage
					| PdfWriter.PageModeUseOutlines);
			OutputStream outHTML = new FileOutputStream(
					"results/in_action/chapter13/course_catalog_bookmarks.html");
			HtmlWriter.getInstance(document, outHTML);
			document.open();

			PdfOutline outline = writer.getRootOutline();
			String[] courses = { "8001", "8002", "8003", "8010", "8011",
					"8020", "8021", "8022", "8030", "8031", "8032", "8033",
					"8040", "8041", "8042", "8043", "8051", "8052" };
			CourseCatalogBookmarked cc;
			PdfPageLabels labels = new PdfPageLabels();
			for (int i = 0; i < courses.length; i++) {
				cc = new CourseCatalogBookmarked(courses[i]);
				cc.flushToDocument(document);
				int pagenumber = writer.getPageNumber();
				new PdfOutline(outline, new PdfDestination(PdfDestination.FIT),
						cc.getTitle());
				try {
					labels.addPageLabel(pagenumber, PdfPageLabels.EMPTY,
							courses[i]);
					writer.setThumbnail(Image
							.getInstance("resources/in_action/chapter07/"
									+ courses[i] + ".jpg"));
				} catch (FileNotFoundException fnfe) {
					// the thumbnail will not be replaced by an image if the
					// image is not found
				}
				document.newPage();
			}
			writer.setPageLabels(labels);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			PdfReader reader = new PdfReader("results/in_action/chapter13/course_catalog_bookmarks.pdf");
			java.util.List list = SimpleBookmark.getBookmark(reader);
			SimpleBookmark.exportToXML(list, new FileOutputStream(
					"results/in_action/chapter13/course_catalog_bookmarks.xml"), "ISO8859-1", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param arg0
	 * @throws SAXException
	 * @see org.xml.sax.DTDHandler#unparsedEntityDecl(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public void skippedEntity(String arg0) throws SAXException {
		System.out.println(arg0);
	}
}