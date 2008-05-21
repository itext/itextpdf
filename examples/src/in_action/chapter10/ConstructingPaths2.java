/* in_action/chapter10/ConstructingPaths2.java */

package in_action.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ConstructingPaths2 {

	/**
	 * Generates a PDF file with paths that are constructed and painted.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example ConstructingPaths2");
		System.out.println("-> Creates a PDF file with paths that are constructed");
		System.out.println("   and painted.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: paths2.pdf");
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
					new FileOutputStream("results/in_action/chapter10/paths2.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			cb.setColorStroke(new GrayColor(0.2f));
			cb.setColorFill(new GrayColor(0.9f));
			constructStar(cb, 30, 720);
			constructCircle(cb, 70, 650, 40, true);
			constructCircle(cb, 70, 650, 20, true);
			cb.fill();
			constructStar(cb, 120, 720);
			constructCircle(cb, 160, 650, 40, true);
			constructCircle(cb, 160, 650, 20, true);
			cb.eoFill();
			constructStar(cb, 250, 650);
			cb.newPath();
			constructCircle(cb, 250, 650, 40, true);
			constructCircle(cb, 250, 650, 20, true);
			constructStar(cb, 300, 720);
			constructCircle(cb, 340, 650, 40, true);
			constructCircle(cb, 340, 650, 20, false);
			cb.fillStroke();
			constructStar(cb, 390, 720);
			constructCircle(cb, 430, 650, 40, true);
			constructCircle(cb, 430, 650, 20, true);
			cb.eoFillStroke();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5: we close the document
		document.close();
	}

	public static void constructStar(PdfContentByte cb, float x, float y) {
		cb.moveTo(x + 10, y);
		cb.lineTo(x + 80, y + 60);
		cb.lineTo(x, y + 60);
		cb.lineTo(x + 70, y);
		cb.lineTo(x + 40, y + 90);
		cb.closePath();
	}

	public static void constructCircle(PdfContentByte cb, float x, float y,
			float r, boolean clockwise) {
		float b = 0.5523f;
		if (clockwise) {
			cb.moveTo(x + r, y);
			cb.curveTo(x + r, y - r * b, x + r * b, y - r, x, y - r);
			cb.curveTo(x - r * b, y - r, x - r, y - r * b, x - r, y);
			cb.curveTo(x - r, y + r * b, x - r * b, y + r, x, y + r);
			cb.curveTo(x + r * b, y + r, x + r, y + r * b, x + r, y);
		} else {
			cb.moveTo(x + r, y);
			cb.curveTo(x + r, y + r * b, x + r * b, y + r, x, y + r);
			cb.curveTo(x - r * b, y + r, x - r, y + r * b, x - r, y);
			cb.curveTo(x - r, y - r * b, x - r * b, y - r, x, y - r);
			cb.curveTo(x + r * b, y - r, x + r, y - r * b, x + r, y);
		}
	}
}
