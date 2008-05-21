/* in_action/chapter06/PdfPTableAbsolutePositions.java */

package in_action.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class PdfPTableAbsolutePositions {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example PdfPTableAbsolutePositions");
		System.out.println("-> Creates a PDF file with a nested PdfPTable");
		System.out.println("   added to the document with writeSelectedRows.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: pdfptable_writeselectedrows.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/pdfptable_writeselectedrows.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfContentByte cb = writer.getDirectContent();
			PdfPTable table = new PdfPTable(2);
			float[] rows = { 50f, 250f };
			table.setTotalWidth(rows);
			for (int k = 0; k < 200; ++k) {
				table.addCell("row " + k);
				table.addCell("blah blah blah " + k);
			}
			document.add(new Paragraph("row 0 - 50"));
			table.writeSelectedRows(0, 50, 150, 820, cb);
			document.newPage();
			document.add(new Paragraph("row 50 - 100"));
			table.writeSelectedRows(50, 100, 150, 820, cb);
			document.newPage();
			document.add(new Paragraph(
					"row 100 - 150 DOESN'T FIT ON THE PAGE!!!"));
			table.writeSelectedRows(100, 150, 150, 200, cb);
			document.newPage();
			document.add(new Paragraph("row 150 - 200"));
			table.writeSelectedRows(150, -1, 150, 820, cb);
			System.out.println("Total table height: " + table.getTotalHeight());
			float rowheight = 0;
			for (int i = 0; i < 50; i++) {
				rowheight += table.getRowHeight(i);
			}
			System.out.println("Height of the first 50 rows: " + rowheight);
			System.out.print("Heights of the individual rows:");
			PdfPRow row;
			for (Iterator i = table.getRows().iterator(); i.hasNext();) {
				row = (PdfPRow) i.next();
				System.out.print(" ");
				System.out.print(row.getMaxHeights());
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}