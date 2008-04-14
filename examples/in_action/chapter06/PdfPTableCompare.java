/* in_action/chapter06/PdfPTableCompare.java */

package in_action.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class PdfPTableCompare {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example PdfPTableCompare");
		System.out.println("-> Creates a PDF file with two PdfPTables");
		System.out.println("   once added with document.add,");
		System.out.println("   once with writeSelectedRows.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: pdfptable_compared.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/pdfptable_compared.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfPTable table = new PdfPTable(3);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell("the quick brown fox");
			table.addCell("jumps over");
			table.addCell("the lazy dog");
			table.addCell("the lazy dog");
			table.addCell("jumps over");
			table.addCell("the quick brown fox");

			document.add(new Paragraph(
					"The table below is added with document.add():"));
			document.add(Chunk.NEWLINE);
			document.add(table);
			document.add(new Paragraph(
					"The table below is added with writeSelectedRows() at position (x = 50; y ="
							+ PageSize.A4.getHeight() * 0.75f + "):"));
			table.writeSelectedRows(0, -1, 50, PageSize.A4.getHeight() * 0.75f,
					writer.getDirectContent());
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}