/* in_action/chapter10/ConstructingPaths1.java */

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

public class ConstructingPaths1 {

	/**
	 * Generates a PDF file with paths that are constructed and painted.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example ConstructingPaths1");
		System.out.println("-> Creates a PDF file with paths that are constructed");
		System.out.println("   and painted.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: paths1.pdf");
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
					new FileOutputStream("results/in_action/chapter10/paths1.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			cb.setColorStroke(new GrayColor(0.2f));
			cb.setColorFill(new GrayColor(0.9f));
			cb.moveTo(30, 700);
			cb.lineTo(130, 700);
			cb.lineTo(130, 800);
			cb.lineTo(30, 800);
			cb.stroke();
			cb.moveTo(140, 700);
			cb.lineTo(240, 700);
			cb.lineTo(240, 800);
			cb.lineTo(140, 800);
			cb.closePathStroke();
			cb.moveTo(250, 700);
			cb.lineTo(350, 700);
			cb.lineTo(350, 800);
			cb.lineTo(250, 800);
			cb.fill();
			cb.moveTo(360, 700);
			cb.lineTo(460, 700);
			cb.lineTo(460, 800);
			cb.lineTo(360, 800);
			cb.fillStroke();
			cb.moveTo(470, 700);
			cb.lineTo(570, 700);
			cb.lineTo(570, 800);
			cb.lineTo(470, 800);
			cb.closePathFillStroke();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
