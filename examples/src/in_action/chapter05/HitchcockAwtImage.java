/* in_action/chapter05/HitchcockAwtImage.java */

package in_action.chapter05;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HitchcockAwtImage {

	/**
	 * Generates a PDF file with Images.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example HitchcockAwtImage");
		System.out.println("-> Creates a PDF file with images.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> images needed: hitchcock.gif");
		System.out.println("-> resulting PDF: hitchcock_awt_image.pdf");
		// step 1: creation of a document-object
		Rectangle r = new Rectangle(PageSize.A4);
		r.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
		Document document = new Document(r);
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter05/hitchcock_awt_image.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(
					"resources/in_action/chapter05/hitchcock.gif");
			document.add(new Paragraph("Hitchcock in Red."));
			com.lowagie.text.Image img1 = com.lowagie.text.Image.getInstance(
					awtImage, null);
			document.add(img1);
			document.add(new Paragraph("Hitchcock in Black and White."));
			com.lowagie.text.Image img2 = com.lowagie.text.Image.getInstance(
					awtImage, null, true);
			document.add(img2);
			document.newPage();
			document.add(new Paragraph("Hitchcock in Red and Yellow."));
			com.lowagie.text.Image img3 = com.lowagie.text.Image.getInstance(
					awtImage, new Color(0xFF, 0xFF, 0x00));
			document.add(img3);
			document.add(new Paragraph("Hitchcock in Black and White."));
			com.lowagie.text.Image img4 = com.lowagie.text.Image.getInstance(
					awtImage, new Color(0xFF, 0xFF, 0x00), true);
			document.add(img4);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
