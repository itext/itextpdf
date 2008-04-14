/* in_action/chapter03/HelloWorldMaximum.java */

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

public class HelloWorldMaximum {

	/**
	 * Generates a PDF file with the text 'Hello World' on a page that has the
	 * maximum size in Adobe Reader 7
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 3: example HelloWorldMaximum");
		System.out.println("-> Creates a PDF file with the text 'Hello World';");
		System.out.println("   The maximum pagesize is used.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldMaximum.pdf");
		// step 1: creation of a document-object
		Document document = new Document(new Rectangle(14400, 14400));
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter03/HelloWorldMaximum.pdf"));
			writer.setPdfVersion(PdfWriter.VERSION_1_6);
			writer.setUserunit(75000f);
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