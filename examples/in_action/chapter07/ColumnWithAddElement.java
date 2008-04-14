/* in_action/chapter07/ColumnWithAddElement.java */

package in_action.chapter07;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ColumnWithAddElement {
	public static final Font FONT9 = FontFactory.getFont(
			FontFactory.TIMES_ROMAN, 9);

	public static final Font FONT11 = FontFactory.getFont(
			FontFactory.TIMES_ROMAN, 11);

	public static final Font FONT11B = FontFactory.getFont(
			FontFactory.TIMES_ROMAN, 11, Font.BOLD);

	public static final Font FONT14B = FontFactory.getFont(
			FontFactory.TIMES_ROMAN, 14, Font.BOLD);

	public static final Font FONT14BC = FontFactory.getFont(
			FontFactory.TIMES_ROMAN, 14, Font.BOLD, new Color(255, 0, 0));

	public static final Font FONT24B = FontFactory.getFont(
			FontFactory.TIMES_ROMAN, 24, Font.BOLD);

	/**
	 * Generates a PDF file with columns containing composite content.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 7: example ColumnWithAddElement");
		System.out.println("-> Creates a PDF file with columns containing composite content.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource: resources/8001.jpg");
		System.out.println("-> file generated: column_with_add_element.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter07/column_with_add_element.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			float gutter = 20;
			int numColumns = 2;
			float fullWidth = document.right() - document.left();
			float columnWidth = (fullWidth - (numColumns - 1) * gutter)
					/ numColumns;
			float allColumns[] = new float[numColumns]; // left
			for (int k = 0; k < numColumns; ++k) {
				allColumns[k] = document.left() + (columnWidth + gutter) * k;
			}

			PdfContentByte cb = writer.getDirectContent();
			ColumnText ct = new ColumnText(cb);
			ct.setLeading(0, 1.5f);
			ct.setSimpleColumn(document.left(), 0, document.right(), document.top());
			Phrase fullTitle = new Phrase("POJOs in Action", FONT24B);
			ct.addText(fullTitle);
			ct.go();
			Phrase subTitle = new Phrase(
					"Developing Enterprise Applications with Lightweight Frameworks",
					FONT14B);
			ct.addText(subTitle);
			ct.go();
			float currentY = ct.getYLine();
			currentY -= 4;
			cb.setLineWidth(1);
			cb.moveTo(document.left(), currentY);
			cb.lineTo(document.right(), currentY);
			cb.stroke();
			ct.setYLine(currentY);
			ct.addText(new Chunk("Chris Richardson", FONT14B));
			ct.go();
			currentY = ct.getYLine();
			currentY -= 15;
			float topColumn = currentY;
			for (int k = 1; k < numColumns; ++k) {
				float x = allColumns[k] - gutter / 2;
				cb.moveTo(x, topColumn);
				cb.lineTo(x, document.bottom());
			}
			cb.stroke();
			int currentColumn = 0;
			ct.setSimpleColumn(allColumns[currentColumn], document.bottom(),
					allColumns[currentColumn] + columnWidth, currentY);
			Image img = Image.getInstance("resources/in_action/chapter07/8001.jpg");
			ct.addElement(img);
			ct.addElement(newParagraph("Key Data:", FONT14BC, 5));
			PdfPTable ptable = new PdfPTable(2);
			float[] widths = { 1, 2 };
			ptable.setWidths(widths);
			ptable.getDefaultCell().setPaddingLeft(4);
			ptable.getDefaultCell().setPaddingTop(0);
			ptable.getDefaultCell().setPaddingBottom(4);
			ptable.addCell(new Phrase("Publisher:", FONT9));
			ptable.addCell(new Phrase("Manning Publications Co.", FONT9));
			ptable.addCell(new Phrase("ISBN:", FONT9));
			ptable.addCell(new Phrase("1932394583", FONT9));
			ptable.addCell(new Phrase("Price:", FONT9));
			ptable.addCell(new Phrase("$44.95", FONT9));
			ptable.addCell(new Phrase("Page Count:", FONT9));
			ptable.addCell(new Phrase("450", FONT9));
			ptable.addCell(new Phrase("Pub Date:", FONT9));
			ptable.addCell(new Phrase("2005", FONT9));
			ptable.setSpacingBefore(5);
			ptable.setWidthPercentage(100);
			ct.addElement(ptable);
			ct.addElement(newParagraph("Description", FONT14BC, 15));
			ct.addElement(newParagraph(
							"In the past, developers built enterprise Java applications using EJB technologies that are excessively complex and difficult to use. Often EJB introduced more problems than it solved. There is a major trend in the industry towards using simpler and easier technologies such as Hibernate, Spring, JDO, iBATIS and others, all of which allow the developer to work directly with the simpler Plain Old Java Objects or POJOs. Now EJB version 3 solves the problems that gave EJB 2 a black eye--it too works with POJOs.",
							FONT11, 5));
			Paragraph p = new Paragraph();
			p.setSpacingBefore(5);
			p.setAlignment(Element.ALIGN_JUSTIFIED);
			Chunk anchor = new Chunk("POJOs in Action", FONT11B);
			anchor.setAnchor("http://www.manning.com/books/crichardson");
			p.add(anchor);
			p.add(new Phrase(
							" describes the new, easier ways to develop enterprise Java applications. It describes how to make key design decisions when developing business logic using POJOs, including how to organize and encapsulate the business logic, access the database, manage transactions, and handle database concurrency. This book is a new-generation Java applications guide: it enables readers to successfully build lightweight applications that are easier to develop, test, and maintain.",
							FONT11));
			ct.addElement(p);
			ct.addElement(newParagraph("Inside the Book", FONT14BC, 15));
			List list = new List(List.UNORDERED, 15);
			ListItem li;
			li = new ListItem("How to develop apps in the post EJB 2 world",
					FONT11);
			list.add(li);
			li = new ListItem(
					"How to leverage the strengths and work around the weaknesses of: JDO, Hibernate, and EJB 3",
					FONT11);
			list.add(li);
			li = new ListItem("How to benefit by using aspects", FONT11);
			list.add(li);
			li = new ListItem(
					"How to do test-driven development with lightweight frameworks",
					FONT11);
			list.add(li);
			li = new ListItem("How to accelerate the edit-compile-debug cycle",
					FONT11);
			list.add(li);
			ct.addElement(list);
			ct.addElement(newParagraph("About the Author...", FONT14BC, 15));
			ct.addElement(newParagraph(
							"Chris Richardson is a developer, architect and mentor with over 20 years of experience. He runs a consulting company that jumpstarts new development projects and helps teams that are frustrated with enterprise Java become more productive and successful. Chris has been a technical leader at a variety of companies including Insignia Solutions and BEA Systems. Chris holds a MA & BA in Computer Science from the University of Cambridge in England. He lives in Oakland, CA.",
							FONT11, 15));
			while (true) {
				int status = ct.go();
				if (!ColumnText.hasMoreText(status))
					break;
				// we run out of column. Let's go to another one
				++currentColumn;
				if (currentColumn >= allColumns.length)
					break;
				ct.setSimpleColumn(allColumns[currentColumn],
						document.bottom(), allColumns[currentColumn]
								+ columnWidth, topColumn);
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	private static Paragraph newParagraph(String s, Font f, float spacingBefore) {
		Paragraph p = new Paragraph(s, f);
		p.setAlignment(Element.ALIGN_JUSTIFIED);
		p.setSpacingBefore(spacingBefore);
		return p;
	}
}