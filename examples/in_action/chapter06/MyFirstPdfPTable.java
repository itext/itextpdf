/* in_action/chapter06/MyFirstPdfPTable.java */

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

public class MyFirstPdfPTable {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example MyFirstPdfPTable");
		System.out.println("-> Creates a PDF file with a PdfPTable.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: my_first_pdfptable.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/my_first_pdfptable.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfPTable table = new PdfPTable(3);
			PdfPCell cell = new PdfPCell(new Paragraph("header with colspan 3"));
			cell.setColspan(3);
			table.addCell(cell);
			table.addCell("1.1");
			table.addCell("2.1");
			table.addCell("3.1");
			table.addCell("1.2");
			table.addCell("2.2");
			table.addCell("3.2");
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