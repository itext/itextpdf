/* in_action/chapter02/HelloWorldOpen.java */

package in_action.chapter02;

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

public class HelloWorldOpen {

	/**
	 * Generates a PDF file with the text 'Hello World'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		Document.compress = false;
		System.out.println("Chapter 2: example HelloWorldOpen");
		System.out.println("-> Creates a PDF file with the text 'Hello World';");
		System.out.println("   PageSize and margins are defined after creating the Document");
		System.out.println("   and BEFORE document.open()");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldOpen.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter02/HelloWorldOpen.pdf"));
			// step 3: we open the document
			document.setPageSize(PageSize.LETTER.rotate());
			document.setMargins(36, 72, 108, 144);
			document.open();
			// step 4: we add a paragraph to the document
			document.add(new Paragraph("Hello World"));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}