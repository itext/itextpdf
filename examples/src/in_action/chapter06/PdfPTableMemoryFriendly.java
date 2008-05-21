/* in_action/chapter06/PdfPTableMemoryFriendly.java */

package in_action.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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

public class PdfPTableMemoryFriendly {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example PdfPTableMemoryFriendly");
		System.out.println("-> Creates a PDF file with a large, memory friendly PdfPTable.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: pdfptable_large.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/pdfptable_large.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setHeaderRows(1);
			PdfPCell h1 = new PdfPCell(new Paragraph("Header 1"));
			h1.setGrayFill(0.7f);
			table.addCell(h1);
			PdfPCell h2 = new PdfPCell(new Paragraph("Header 2"));
			h2.setGrayFill(0.7f);
			table.addCell(h2);
			PdfPCell cell;
			for (int row = 1; row <= 2000; row++) {
				if (row % 50 == 50 - 1) {
					document.add(table);
					table.deleteBodyRows();
					table.setSkipFirstHeader(true);
				}
				cell = new PdfPCell(new Paragraph(String.valueOf(row)));
				table.addCell(cell);
				cell = new PdfPCell(new Paragraph(
						"Quick brown fox jumps over the lazy dog."));
				table.addCell(cell);
			}
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