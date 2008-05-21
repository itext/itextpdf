/* in_action/chapter02/HelloWorldEncrypted.java */

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

public class HelloWorldEncrypted {

	/**
	 * Generates an encrypted PDF file with the text 'Hello World'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 3: example HelloWorldEncrypted");
		System.out.println("-> Creates a PDF file with the text 'Hello World';");
		System.out.println("   You will need to enter the password 'Hello' to open it,");
		System.out.println("   and the password 'World' to change it.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("->              bcmail-jdk14-135.jar");
		System.out.println("->              bcprov-jdk14-135.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldEncrypted.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter03/HelloWorldEncrypted.pdf"));
			writer.setEncryption("Hello".getBytes(), "World".getBytes(),
					PdfWriter.AllowCopy | PdfWriter.AllowPrinting, PdfWriter.STANDARD_ENCRYPTION_128);
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