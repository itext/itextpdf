/* in_action/chapter14/EmptyPages.java */

package in_action.chapter14;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class EmptyPages {

	/**
	 * Generates a PDF file with the text 'Hello World'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 14: example empty pages");
		System.out.println("-> Creates a PDF file with empty pages and");
		System.out.println("   a PDF without empty pages.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorld_one_page.pdf and HelloWorld_empty_pages.pdf");
		createPdf("results/in_action/chapter14/HelloWorld_empty_pages.pdf", false);
		createPdf("results/in_action/chapter14/HelloWorld_one_page.pdf", true);
	}

	public static void createPdf(String filename, boolean ignore_empty) {
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream(filename));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			writer.setPageEmpty(ignore_empty);
			document.newPage();
			writer.setPageEmpty(ignore_empty);
			document.newPage();
			document.add(new Paragraph("Hello World"));
			document.newPage();
			writer.setPageEmpty(ignore_empty);
			document.newPage();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}