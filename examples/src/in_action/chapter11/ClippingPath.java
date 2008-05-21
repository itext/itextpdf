/* in_action/chapter11/ClippingPath.java */

package in_action.chapter11;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ClippingPath {

	/**
	 * Generates a PDF file showing the different canvases in iText.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example ClippingPath");
		System.out.println("-> Creates a PDF file with paths that are constructed");
		System.out.println("   and used as clipping path.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource needed: foxdog.jpg (chapter 5)");
		System.out.println("-> file generated: template_clip.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/clipping_path.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			Image img = Image
					.getInstance("resources/in_action/chapter05/foxdog.jpg");
			float w = img.getScaledWidth();
			float h = img.getScaledHeight();
			PdfContentByte cb = writer.getDirectContent();
			cb.saveState();
			cb.circle(260, 700, 70);
			cb.clip();
			cb.newPath();
			cb.addImage(img, w, 0, 0, h, 36, 620);
			cb.restoreState();

			PdfTemplate tp1 = cb.createTemplate(w, h);
			img.setAbsolutePosition(0, 0);
			tp1.roundRectangle(0, 0, w, h, 10);
			tp1.clip();
			tp1.newPath();
			tp1.addImage(img);
			cb.addTemplate(tp1, 36, 420);

			PdfTemplate tp2 = cb.createTemplate(90, 90);
			tp2.moveTo(10, 0);
			tp2.lineTo(80, 60);
			tp2.lineTo(0, 60);
			tp2.lineTo(70, 0);
			tp2.lineTo(40, 90);
			tp2.closePath();
			tp2.clip();
			tp2.newPath();
			img.setAbsolutePosition(-40, -100);
			tp2.addImage(img);
			cb.addTemplate(tp2, 36, 710);

			PdfTemplate tp3 = cb.createTemplate(90, 90);
			tp3.moveTo(10, 0);
			tp3.lineTo(80, 60);
			tp3.lineTo(0, 60);
			tp3.lineTo(70, 0);
			tp3.lineTo(40, 90);
			tp3.closePath();
			tp3.eoClip();
			tp3.newPath();
			tp3.addImage(img);
			cb.addTemplate(tp3, 36, 620);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}