/* in_action/chapter05/FoxDogImageScaling2.java */

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

public class FoxDogImageScaling2 {

	/**
	 * Generates a PDF file with Images.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogImageScaling2");
		System.out.println("-> Creates a PDF file with images");
		System.out.println("   of a brown fox and a lazy dog.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: foxdog.tiff");
		System.out.println("-> resulting PDF: fox_dog_image_scaling2.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter05/fox_dog_image_scaling2.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Image tiff = Image.getInstance("resources/in_action/chapter05/foxdog.tiff");
			document.add(tiff);
			// document.newPage();
			document.add(new Paragraph("Original width: " + tiff.getWidth()
					+ "; original height: " + tiff.getHeight()));
			document.add(new Paragraph("DPI X: " + tiff.getDpiX() + "; DPI Y: "
					+ tiff.getDpiY()));
			tiff.scalePercent(72f / tiff.getDpiX() * 100);
			document.add(new Paragraph("Show the image with 360 Dpi (scaled "
					+ (7200f / tiff.getDpiX()) + "%):"));
			document.add(tiff);
			document.add(new Paragraph("Scaled width: " + tiff.getScaledWidth()
					+ "; scaled height: " + tiff.getScaledHeight()));
			tiff.scaleToFit(200, 200);
			document.add(tiff);
			document.add(new Paragraph("Scaled width: " + tiff.getScaledWidth()
					+ "; scaled height: " + tiff.getScaledHeight()));
			document.add(new Paragraph("DPI X: "
					+ (72f * tiff.getWidth() / tiff.getScaledWidth()) + "; DPI Y: "
					+ (72f * tiff.getHeight() / tiff.getScaledHeight())));

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
