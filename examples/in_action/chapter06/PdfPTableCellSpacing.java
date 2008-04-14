/* in_action/chapter06/PdfPTableCellSpacing.java */

package in_action.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
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

public class PdfPTableCellSpacing {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example PdfPTableCellSpacing");
		System.out.println("-> Creates a PDF file with a PdfPTable.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: pdfptable_cellspacing.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/pdfptable_cellspacing.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			PdfPCell cell = new PdfPCell(
					new Paragraph(
							"Quick brown fox jumps over the lazy dog. Quick brown fox jumps over the lazy dog."));
			table.addCell("default leading / spacing");
			table.addCell(cell);
			table.addCell("absolute leading: 20");
			cell.setLeading(20f, 0f);
			table.addCell(cell);
			table.addCell("absolute leading: 3; relative leading: 1.2");
			cell.setLeading(3f, 1.2f);
			table.addCell(cell);
			table.addCell("absolute leading: 0; relative leading: 1.2");
			cell.setLeading(0f, 1.2f);
			table.addCell(cell);
			table.addCell("no leading at all");
			cell.setLeading(0f, 0f);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph(
					"Quick brown fox jumps over the lazy dog."));
			table.addCell("padding 10");
			cell.setPadding(10);
			table.addCell(cell);
			table.addCell("padding 0");
			cell.setPadding(0);
			table.addCell(cell);
			table.addCell("different padding for left, right, top and bottom");
			cell.setPaddingLeft(20);
			cell.setPaddingRight(50);
			cell.setPaddingTop(0);
			cell.setPaddingBottom(5);
			table.addCell(cell);
			Phrase p = new Phrase("Quick brown fox jumps over the lazy dog");
			table.getDefaultCell().setPadding(2);
			table.getDefaultCell().setUseAscender(false);
			table.getDefaultCell().setUseDescender(false);
			table.addCell("padding 2; no ascender, no descender");
			table.addCell(p);
			table.getDefaultCell().setUseAscender(true);
			table.getDefaultCell().setUseDescender(false);
			table.addCell("padding 2; ascender, no descender");
			table.addCell(p);
			table.getDefaultCell().setUseAscender(false);
			table.getDefaultCell().setUseDescender(true);
			table.addCell("padding 2; descender, no ascender");
			table.addCell(p);
			table.getDefaultCell().setUseAscender(true);
			table.getDefaultCell().setUseDescender(true);
			table.addCell("padding 2; ascender and descender");
			cell.setPadding(2);
			cell.setUseAscender(true);
			cell.setUseDescender(true);
			table.addCell(p);
			document.add(table);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}