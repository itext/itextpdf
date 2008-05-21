/* in_action/chapter06/FoobarStudyProgram.java */

package in_action.chapter06;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.SimpleCell;
import com.lowagie.text.SimpleTable;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoobarStudyProgram extends DefaultHandler {

	/** The number of columns in the table. */
	public static final int NUMCOLUMNS = 12;

	/** The relative widths of the columns. */
	public static final float[] COLUMNWIDTHS = { 5, 7, 35, 4, 4, 10, 15, 4, 4,
			4, 4, 4 };

	/** A possible status. */
	public static final int BEFORE = 0;

	/** A possible status. */
	public static final int GROUP = 1;

	/** A possible status. */
	public static final int UNIT = 2;

	/** A possible status. */
	public static final int COURSE = 3;

	/** possible style. */
	public static final int EMPTY = 0;

	/** possible style. */
	public static final int TITLE = 1;

	/** possible style. */
	public static final int OPTION = 2;

	/** possible style. */
	public static final int GROUPTITLE = 3;

	/** possible style. */
	public static final int UNITTITLE = 4;

	/** possible style. */
	public static final int HEADER = 5;

	/** possible style. */
	public static final int NUMBER = 6;

	/** possible style. */
	public static final int BOLDNUMBER = 7;

	/** possible style. */
	public static final int STRING = 8;

	/** The table that will hold the Study Program. */
	protected SimpleTable table;

	/** The table that holds the current row. */
	protected SimpleCell currentRow;

	/** The StringBuffer that holds what's inside the tags. */
	protected StringBuffer buffer;

	/** Indicates if the parser is inside a group, unit or course tag. */
	protected int status = BEFORE;

	/** Counter for the units. */
	protected int units = 0;

	/** Indicates if the unit number was shown. */
	protected boolean unit = false;

	/** Indicates if the value has to be counted. */
	protected boolean count = false;

	/** Keeps the sum of the D column (study hours). */
	protected int totalD = 0;

	/** Keeps the sum of the E column (credits). */
	protected int totalE = 0;

	/**
	 * Creates a new FoobarHtmlHandler.
	 * 
	 * @param html
	 * @throws DocumentException
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws SAXException
	 * @throws IOException
	 */
	public FoobarStudyProgram(String html) throws DocumentException,
			ParserConfigurationException, FactoryConfigurationError,
			FileNotFoundException, SAXException, IOException {
		table = new SimpleTable();
		table.setWidthpercentage(100f);
		currentRow = new SimpleCell(SimpleCell.ROW);
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(new InputSource(new FileInputStream(html)), this);
	}

	/**
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(new String(ch, start, length).trim());
		buffer.append(" ");
	}

	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		try {
			if ("group".equals(qName)) {
				if (status == BEFORE) {
					addHeader();
				}
				status = GROUP;
			} else if ("unit".equals(qName)) {
				status = UNIT;
				units++;
				currentRow.add(getCell(String.valueOf(units), NUMBER));
				unit = true;
			} else if ("course".equals(qName)) {
				status = COURSE;
			} else if ("d".equals(qName) || "e".equals(qName)) {
				count = "true".equals(attributes.getValue("count"));
			}
			buffer = new StringBuffer();
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
			if ("studyprogram".equals(qName)) {
				addFooter();
				return;
			}
			switch (status) {
			case BEFORE:
				if ("faculty".equals(qName) || "program".equals(qName)) {
					currentRow.add(getCell(buffer.toString().trim(), TITLE));
					table.add(currentRow);
					currentRow = new SimpleCell(SimpleCell.ROW);
				} else if ("option".equals(qName)) {
					currentRow.add(getCell("Option: "
							+ buffer.toString().trim(), OPTION));
					table.add(currentRow);
					currentRow = new SimpleCell(SimpleCell.ROW);
				}
				break;
			case GROUP:
				if ("title".equals(qName)) {
					currentRow.add(getCell("", EMPTY));
					currentRow
							.add(getCell(buffer.toString().trim(), GROUPTITLE));
					table.add(currentRow);
					currentRow = new SimpleCell(SimpleCell.ROW);
					break;
				}
				break;
			case UNIT:
				if ("title".equals(qName)) {
					currentRow
							.add(getCell(buffer.toString().trim(), UNITTITLE));
					break;
				}
				if ("d".equals(qName)) {
					String d = buffer.toString().trim();
					if (count) {
						totalD += Integer.parseInt(d);
						currentRow.add(getCell(d, BOLDNUMBER));
					} else {
						currentRow.add(getCell(d, NUMBER));
					}
					break;
				}
				if ("e".equals(qName)) {
					String e = buffer.toString().trim();
					if (count) {
						totalE += Integer.parseInt(e);
						currentRow.add(getCell(e, BOLDNUMBER));
					} else {
						currentRow.add(getCell(e, NUMBER));
					}
					table.add(currentRow);
					currentRow = new SimpleCell(SimpleCell.ROW);
					unit = false;
					break;
				}
				break;
			case COURSE:
				if ("coursenumber".equals(qName)) {
					if (unit) {
						unit = false;
					} else {
						currentRow.add(getCell("", EMPTY));
					}
					currentRow.add(getCell(buffer.toString().trim(), NUMBER));
					break;
				}
				if ("title".equals(qName) || "teacher".equals(qName)) {
					currentRow.add(getCell(buffer.toString().trim(), STRING));
					break;
				}
				if ("semester".equals(qName) || "pt".equals(qName)
						|| "department".equals(qName) || "a".equals(qName)
						|| "b".equals(qName) || "c".equals(qName)) {
					currentRow.add(getCell(buffer.toString().trim(), NUMBER));
					break;
				}
				if ("d".equals(qName)) {
					String d = buffer.toString().trim();
					if (count) {
						totalD += Integer.parseInt(d);
						currentRow.add(getCell(d, BOLDNUMBER));
					} else {
						currentRow.add(getCell(d, NUMBER));
					}
					break;
				}
				if ("e".equals(qName)) {
					String e = buffer.toString().trim();
					if (count) {
						totalE += Integer.parseInt(e);
						currentRow.add(getCell(e, BOLDNUMBER));
					} else {
						currentRow.add(getCell(e, NUMBER));
					}
					table.add(currentRow);
					currentRow = new SimpleCell(SimpleCell.ROW);
					break;
				}
				break;
			}
			buffer = new StringBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Returns the table.
	 */
	public SimpleTable getTable() {
		return table;
	}

	/** Adds a header to the table */
	private void addHeader() {
		SimpleCell headerRow = new SimpleCell(SimpleCell.ROW);
		headerRow.add(getCell("Unit", HEADER, 4));
		headerRow.add(getCell("Code", HEADER, 6));
		headerRow.add(getCell("Course", HEADER, 38));
		headerRow.add(getCell("Sem.", HEADER, 5));
		headerRow.add(getCell("P-T", HEADER, 5));
		headerRow.add(getCell("Dept.", HEADER, 7));
		headerRow.add(getCell("Lecturer in Charge", HEADER, 15));
		headerRow.add(getCell("A", HEADER, 4));
		headerRow.add(getCell("B", HEADER, 4));
		headerRow.add(getCell("C", HEADER, 4));
		headerRow.add(getCell("D", HEADER, 4));
		headerRow.add(getCell("E", HEADER, 4));
		table.add(headerRow);
	}

	/** Adds a header to the table */
	private void addFooter() {
		SimpleCell headerRow = new SimpleCell(SimpleCell.ROW);
		headerRow.add(getCell("", EMPTY));
		headerRow.add(getCell("", UNITTITLE));
		headerRow.add(getCell(String.valueOf(totalD), BOLDNUMBER));
		headerRow.add(getCell(String.valueOf(totalE), BOLDNUMBER));
		table.add(headerRow);
	}

	/**
	 * Returns a cell in a certain style.
	 * 
	 * @param s
	 * @param style
	 * @return a cell with the content defined by s and the style by the style
	 *         parameter.
	 */
	private SimpleCell getCell(String s, int style) {
		switch (style) {
		case HEADER:
			throw new UnsupportedOperationException(
					"You can't use this method if you want to get a HeaderCell.");
		default:
			return getCell(s, style, -1);
		}
	}

	/**
	 * Returns a cell in a certain style.
	 * 
	 * @param s
	 * @param style
	 * @param width
	 * @return a cell with the content defined by s.
	 */
	private SimpleCell getCell(String s, int style, float width) {
		SimpleCell cell = new SimpleCell(SimpleCell.CELL);
		Paragraph p;
		switch (style) {
		case EMPTY:
			cell.setBorder(SimpleCell.BOX);
			break;
		case TITLE:
			p = new Paragraph(s, FontFactory.getFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 14));
			p.setAlignment(Element.ALIGN_CENTER);
			cell.add(p);
			cell.setColspan(NUMCOLUMNS);
			cell.setBorder(SimpleCell.NO_BORDER);
			break;
		case OPTION:
			p = new Paragraph(s, FontFactory.getFont(BaseFont.HELVETICA_BOLD,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 12));
			p.setAlignment(Element.ALIGN_CENTER);
			cell.add(p);
			cell.setColspan(NUMCOLUMNS);
			cell.setBorder(SimpleCell.NO_BORDER);
			break;
		case GROUPTITLE:
			p = new Paragraph(s, FontFactory.getFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 12));
			p.setAlignment(Element.ALIGN_LEFT);
			cell.add(p);
			cell.setColspan(NUMCOLUMNS - 1);
			cell.setPadding_left(5);
			cell.setBorder(SimpleCell.BOX);
			break;
		case UNITTITLE:
			p = new Paragraph(s, FontFactory.getFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 12));
			p.setAlignment(Element.ALIGN_LEFT);
			cell.add(p);
			cell.setColspan(NUMCOLUMNS - 3);
			cell.setBorder(SimpleCell.BOX);
			cell.setPadding_left(5);
			break;
		case HEADER:
			p = new Paragraph(s, FontFactory.getFont(BaseFont.HELVETICA_BOLD,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 11));
			p.setAlignment(Element.ALIGN_CENTER);
			cell.add(p);
			cell.setWidthpercentage(width);
			cell.setBorder(SimpleCell.BOX);
			break;
		case NUMBER:
			p = new Paragraph(s, FontFactory.getFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 11));
			p.setAlignment(Element.ALIGN_CENTER);
			cell.add(p);
			cell.setBorder(SimpleCell.BOX);
			break;
		case BOLDNUMBER:
			p = new Paragraph(s, FontFactory.getFont(BaseFont.HELVETICA_BOLD,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 11));
			p.setAlignment(Element.ALIGN_CENTER);
			cell.add(p);
			cell.setBorder(SimpleCell.BOX);
			break;
		case STRING:
			p = new Paragraph(s, FontFactory.getFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 11));
			p.setAlignment(Element.ALIGN_LEFT);
			cell.add(p);
			cell.setBorder(SimpleCell.BOX);
			cell.setPadding_left(5);
			break;
		}
		cell.setBorderWidth(0.3f);
		cell.setPadding_bottom(5);
		return cell;
	}

	/**
	 * Generates a PDF with a table showing a study program of a department of the
	 * Technological University of Foobar.
	 *
	 * @param args
	 *            no arguments needed
	 */
	public static void main(final String[] args) {
		System.out.println("Chapter 6: example FoobarStudyProgram");
		System.out.println("-> Creates a PDF file with a Study Program Table.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resource needed: studyprogram.xml");
		System.out.println("-> resulting PDF: studyprogram.pdf and studyprogram.htm");
		try {
			Document document = new Document(PageSize.A4.rotate());
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter06/studyprogram.pdf"));
			HtmlWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter06/studyprogram.htm"));
			document.open();
			Paragraph p = new Paragraph("Academic Year 2006-2007\n\n");
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			document
					.add(new FoobarStudyProgram("resources/in_action/chapter06/studyprogram.xml")
							.getTable());
			p = new Paragraph(
					"Sem.: 1 = first semester, 2 = second semester, Y = annual course");
			p.setAlignment(Element.ALIGN_RIGHT);
			document.add(p);
			p = new Paragraph(
					"P-T = courses can be taken on a part-time basis, 1 = first part, 2 = second part");
			p.setAlignment(Element.ALIGN_RIGHT);
			document.add(p);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}