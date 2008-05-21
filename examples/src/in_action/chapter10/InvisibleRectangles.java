/* in_action/chapter10/InvisibleRectangles.java */

package in_action.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class InvisibleRectangles {

	/**
	 * Generates a PDF file that is supposed to contain rectangles,
	 * but due to a deliberate mistake, the rectangles aren't visible.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example InvisibleRectangles");
		System.out.println("-> Creates a PDF file with paths that are constructed");
		System.out.println("   but not painted.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: invisible_rectangles.pdf");
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
					new FileOutputStream("results/in_action/chapter10/invisible_rectangles.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			document
					.add(new Paragraph(
							"Two paths for identical rectangles are constructed, but we forgot to paint them."));
			PdfContentByte cb = writer.getDirectContent();
			cb.moveTo(30, 700);
			cb.lineTo(490, 700);
			cb.lineTo(490, 800);
			cb.lineTo(30, 800);
			cb.closePath();
			cb.rectangle(30, 700, 460, 100);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
