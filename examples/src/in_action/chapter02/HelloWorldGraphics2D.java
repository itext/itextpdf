/* in_action/chapter02/HelloWorldGraphics2D.java */

package in_action.chapter02;

import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldGraphics2D {

	/**
	 * Generates a PDF file with the text 'Hello World'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 2: example HelloWorldGraphics2D");
		System.out.println("-> Creates a PDF file with the text 'Hello World';");
		System.out.println("   the text is added at an absolute position using");
		System.out.println("   the java.awt.Graphics2D object.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldGraphics2D.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter02/HelloWorldGraphics2D.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			PdfContentByte cb = writer.getDirectContent();
			Graphics2D graphics2D = cb.createGraphics(PageSize.A4.getWidth(),
					PageSize.A4.getHeight());
			graphics2D.drawString("Hello World", 36, 54);
			graphics2D.dispose();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}