/* in_action/chapter03/HelloWorldUncompressed.java */

package in_action.chapter03;

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

public class HelloWorldUncompressed {

	/**
	 * Generates a PDF file with the text 'Hello World'
	 * @param args no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 3: example HelloWorldUncompressed");
		System.out.println("-> Creates a PDF file with the text 'Hello World';");
		System.out.println("   The stream with the content of a page is not compressed.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldUncompressed.pdf");		
		// step 1: creation of a document-object
		Document.compress = false;
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
					// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter03/HelloWorldUncompressed.pdf"));
			writer.setPdfVersion('1');
			// step 3: we open the document
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