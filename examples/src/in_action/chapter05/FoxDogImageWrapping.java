/* in_action/chapter05/FoxDogImageWrapping.java */

package in_action.chapter05;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogImageWrapping {

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy
	 * dog'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 5: example FoxDogImageWrapping");
		System.out.println("-> Creates a PDF file with images");
		System.out.println("   of a brown fox and a lazy dog.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resource needed: foxdog.jpg and foxdog.gif");
		System.out.println("-> resulting PDF: fox_dog_image_wrapping.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter05/fox_dog_image_wrapping.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Phrase p = new Phrase("Quick brown fox jumps over the lazy dog. ");
			Image img1 = Image.getInstance("resources/in_action/chapter05/foxdog.jpg");
			img1.setAlignment(Image.RIGHT | Image.TEXTWRAP);
			document.add(img1);
			for (int i = 0; i < 20; i++)
				document.add(p);
			Image img2 = Image.getInstance("resources/in_action/chapter05/foxdog.gif");
			img2.setAlignment(Image.MIDDLE | Image.UNDERLYING);
			document.add(img2);
			for (int i = 0; i < 30; i++)
				document.add(p);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
