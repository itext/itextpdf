/* in_action/chapter06/PdfPTableVerticalCells.java */

package in_action.chapter06;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
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

public class PdfPTableVerticalCells {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example PdfPTableVerticalCells");
		System.out.println("-> Creates a PDF file with a PdfPTable.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: pdfptable_vertical.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/pdfptable_vertical_cells.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document

			// we create a PdfTemplate
			float[] widths = { 1, 4 };
			PdfPTable table = new PdfPTable(widths);
			table.setWidthPercentage(30);
			PdfPCell cell = new PdfPCell(new Paragraph("fox"));
			cell.setPadding(4);
			cell.setBackgroundColor(Color.YELLOW);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setRotation(90);
			table.addCell(cell);
			table
					.addCell("The fox is a red/brown animal that is very quick and that jumps over dogs.");
			cell = new PdfPCell(new Paragraph("dog"));
			cell.setPadding(4);
			cell.setBackgroundColor(Color.YELLOW);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setRotation(90);
			table.addCell(cell);
			table
					.addCell("The dog in the sentence 'quick brown fox jumps over the lazy dog' is a rather lazy animal.");
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