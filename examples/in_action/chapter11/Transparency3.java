/* in_action/chapter11/Transparency3.java */

package in_action.chapter11;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
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

public class Transparency3 {

	/**
	 * Generates a PDF file demonstrating soft masks.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example Transparency3");
		System.out
				.println("-> Creates a PDF file with a transparent image (soft mask).");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource needed: foxdog.jpg (chapter 5)");
		System.out.println("-> file generated: transparency3.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/transparency3.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			Paragraph text = new Paragraph(
					"Quick brown fox jumps over the lazy dog.", new Font(
							Font.HELVETICA, 18));
			for (int i = 0; i < 10; i++)
				document.add(text);
			Image img = Image
					.getInstance("resources/in_action/chapter05/foxdog.jpg");
			img.setAbsolutePosition(50, 550);
			byte gradient[] = new byte[256];
			for (int k = 0; k < 256; ++k)
				gradient[k] = (byte) k;
			Image smask = Image.getInstance(256, 1, 1, 8, gradient);
			smask.makeMask();
			img.setImageMask(smask);
			writer.getDirectContent().addImage(img);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	/**
	 * Prints a square and fills half of it with a gray rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param cb
	 * @throws Exception
	 */
	public static void pictureBackdrop(float x, float y, PdfContentByte cb) {
		cb.setColorStroke(Color.black);
		cb.setColorFill(Color.gray);
		cb.rectangle(x, y, 100, 200);
		cb.fill();
		cb.setLineWidth(2);
		cb.rectangle(x, y, 200, 200);
		cb.stroke();
	}

	/**
	 * Prints 3 circles in different colors that intersect with eachother.
	 * 
	 * @param x
	 * @param y
	 * @param cb
	 * @throws Exception
	 */
	public static void pictureCircles(float x, float y, PdfContentByte cb) {
		cb.setColorFill(Color.red);
		cb.circle(x + 70, y + 70, 50);
		cb.fill();
		cb.setColorFill(Color.yellow);
		cb.circle(x + 100, y + 130, 50);
		cb.fill();
		cb.setColorFill(Color.blue);
		cb.circle(x + 130, y + 70, 50);
		cb.fill();
	}
}