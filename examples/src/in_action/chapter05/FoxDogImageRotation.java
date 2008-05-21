/* in_action/chapter05/FoxDogImageRotation.java */

package in_action.chapter05;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogImageRotation {

	/**
	 * Generates a PDF file with Images.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogImageRotation");
		System.out.println("-> Creates a PDF file with images");
		System.out.println("   of a brown fox and a lazy dog.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: foxdog.jpg");
		System.out.println("-> resulting PDF: fox_dog_image_rotation.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter05/fox_dog_image_rotation.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Image jpg1 = Image.getInstance("resources/in_action/chapter05/foxdog.jpg");
			jpg1.scalePercent(80);
			jpg1.setRotation((float) Math.PI / 6);
			document.add(new Paragraph("rotate 30 degrees"));
			document.add(jpg1);
			document.add(new Paragraph("Original width: " + jpg1.getWidth()
					+ "; original height: " + jpg1.getHeight()));
			document.add(new Paragraph("Plain width: " + jpg1.getPlainWidth()
					+ "; plain height: " + jpg1.getPlainHeight()));
			document.add(new Paragraph("Scaled width: " + jpg1.getScaledWidth()
					+ "; scaled height: " + jpg1.getScaledHeight()));
			document.newPage();
			Image jpg2 = Image.getInstance("resources/in_action/chapter05/foxdog.jpg");
			jpg2.setRotationDegrees(45);
			document.add(new Paragraph("rotate 45 degrees"));
			document.add(jpg2);
			jpg2.setRotation((float) Math.PI / 2);
			document.add(new Paragraph("rotate pi/2 radians"));
			document.add(jpg2);
			document.newPage();
			jpg2.setRotationDegrees(135);
			document.add(new Paragraph("rotate 135 degrees"));
			document.add(jpg2);
			jpg2.setRotation((float) Math.PI);
			document.add(new Paragraph("rotate pi radians"));
			document.add(jpg2);
			document.newPage();
			jpg2.setRotation((float) (2.0 * Math.PI));
			document.add(new Paragraph("rotate 2 x pi radians"));
			document.add(jpg2);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
