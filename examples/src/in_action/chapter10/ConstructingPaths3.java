/* in_action/chapter10/ConstructingPaths3.java */

package in_action.chapter10;

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

public class ConstructingPaths3 {

	/**
	 * Generates a PDF file with paths that are constructed and painted.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example ConstructingPaths3");
		System.out.println("-> Creates a PDF file with paths that are constructed");
		System.out.println("   and painted.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: paths3.pdf");
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
					new FileOutputStream("results/in_action/chapter10/paths3.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			float x0, y0, x1, y1, x2, y2, x3, y3;
			x0 = 30;
			y0 = 720;
			x1 = 40;
			y1 = 790;
			x2 = 100;
			y2 = 810;
			x3 = 120;
			y3 = 750;
			cb.moveTo(x0, y0);
			cb.lineTo(x1, y1);
			cb.moveTo(x2, y2);
			cb.lineTo(x3, y3);
			cb.moveTo(x0, y0);
			cb.curveTo(x1, y1, x2, y2, x3, y3);
			x0 = 180;
			y0 = 720;
			x2 = 250;
			y2 = 810;
			x3 = 270;
			y3 = 750;
			cb.moveTo(x2, y2);
			cb.lineTo(x3, y3);
			cb.moveTo(x0, y0);
			cb.curveTo(x2, y2, x3, y3);
			x0 = 330;
			y0 = 720;
			x1 = 340;
			y1 = 790;
			x3 = 420;
			y3 = 750;
			cb.moveTo(x0, y0);
			cb.lineTo(x1, y1);
			cb.moveTo(x0, y0);
			cb.curveTo(x1, y1, x3, y3);
			cb.stroke();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5: we close the document
		document.close();
	}
}
