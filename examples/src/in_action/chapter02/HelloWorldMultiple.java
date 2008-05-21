/* in_action/chapter02/HelloWorldMultiple.java */

package in_action.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldMultiple {

	/**
	 * Generates a PDF, RTF and HTML file with the text 'Hello World'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 2: example HelloWorldMultiple");
		System.out.println("-> Creates a PDF, RTF and HTML file with the text 'Hello World';");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldMultiple.pdf");
		System.out.println("   HelloWorldMultiple.rtf");
		System.out.println("   HelloWorldMultiple.htm");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a PDF writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter02/HelloWorldMultiple.pdf"));
			// we create an RTF writer
			RtfWriter2.getInstance(
			// that also listens to the document
					document,
					// and directs an RTF-stream to a file
					new FileOutputStream("results/in_action/chapter02/HelloWorldMultiple.rtf"));
			// we create an HTML writer
			HtmlWriter.getInstance(
			// that also listens to the document
					document,
					// and directs an RTF-stream to a file
					new FileOutputStream("results/in_action/chapter02/HelloWorldMultiple.htm"));
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