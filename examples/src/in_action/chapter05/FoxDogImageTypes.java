/* in_action/chapter05/FoxDogImageTypes.java */

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

public class FoxDogImageTypes {

	/**
	 * Generates a PDF file with Images.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogImageTypes");
		System.out.println("-> Creates a PDF file with images");
		System.out.println("   of a brown fox and a lazy dog.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: foxdog.jpg, foxdog.gif, foxdog.png, foxdog.tiff, foxdog.wmf, foxdog.bmp and tiger.eps");
		System.out.println("-> resulting PDF: fox_dog_imagetypes.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter05/fox_dog_imagetypes.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			document.add(new Paragraph("foxdog.jpg"));
			Image img1 = Image.getInstance("resources/in_action/chapter05/foxdog.jpg");
			System.out.println(img1.getClass().getName());
			document.add(img1);
			document.add(new Paragraph("foxdog.gif"));
			Image img2 = Image.getInstance("resources/in_action/chapter05/foxdog.gif");
			System.out.println(img2.getClass().getName());
			document.add(img2);
			document.add(new Paragraph("foxdog.png"));
			Image img3 = Image.getInstance("resources/in_action/chapter05/foxdog.png");
			System.out.println(img3.getClass().getName());
			document.add(img3);
			document.newPage();
			document.add(new Paragraph("foxdog.tiff"));
			Image img4 = Image.getInstance("resources/in_action/chapter05/foxdog.tiff");
			System.out.println(img4.getClass().getName());
			document.add(img4);
			document.add(new Paragraph("foxdog.wmf"));
			Image img5 = Image.getInstance("resources/in_action/chapter05/foxdog.wmf");
			System.out.println(img5.getClass().getName());
			document.add(img5);
			document.add(new Paragraph("foxdog.bmp"));
			Image img6 = Image.getInstance("resources/in_action/chapter05/foxdog.bmp");
			System.out.println(img6.getClass().getName());
			document.add(img6);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
