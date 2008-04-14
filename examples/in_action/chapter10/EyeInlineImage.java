/* in_action/chapter10/EyeInlineImage.java */

package in_action.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class EyeInlineImage {

	/**
	 * Generates a PDF file with the iText logo.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example EyeInlineImage");
		System.out.println("-> Creates a PDF file with an image that");
		System.out.println("   is added inline.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: iTextLogo.gif");
		System.out.println("-> file generated: eye_inline_image.pdf");
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
					new FileOutputStream("results/in_action/chapter10/eye_inline_image.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			Image eye = Image.getInstance("resources/in_action/chapter10/iTextLogo.gif");
			eye.setAbsolutePosition(36, 780);
			cb.addImage(eye, true);
			cb.addImage(eye, 271, -50, -30, 550, 100, 100, true);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
