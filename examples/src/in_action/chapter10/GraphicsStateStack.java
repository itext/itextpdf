/* in_action/chapter10/GraphicsStateStack.java */

package in_action.chapter10;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class GraphicsStateStack {

	/**
	 * Generates a PDF file explaining the Graphics State stack.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example GraphicsStateStack");
		System.out.println("-> Creates a PDF file with paths that are constructed");
		System.out.println("   and painted in nested graphic elements.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: graphics_state_stack.pdf");
		// step 1: creation of a document-object
		Document.compress = false;
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter10/graphics_state_stack.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			cb.circle(260.0f, 500.0f, 250.0f);
			cb.fill();
			cb.saveState();
			cb.setColorFill(Color.yellow);
			cb.circle(260.0f, 500.0f, 200.0f);
			cb.fill();
			cb.saveState();
			cb.setColorFill(Color.red);
			cb.circle(260.0f, 500.0f, 150.0f);
			cb.fill();
			cb.restoreState();
			cb.circle(260.0f, 500.0f, 100.0f);
			cb.fill();
			cb.restoreState();
			cb.circle(260.0f, 500.0f, 50.0f);
			cb.fill();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
