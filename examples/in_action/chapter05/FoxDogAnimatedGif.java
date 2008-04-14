/* in_action/chapter05/FoxDogAnimatedGif.java */

package in_action.chapter05;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.codec.GifImage;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogAnimatedGif {

	/**
	 * Generates a PDF file with Images.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 5: example FoxDogAnimatedGif");
		System.out.println("-> Creates a PDF file with images");
		System.out.println("   of a brown fox and a lazy dog.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: animated_fox_dog.gif");
		System.out.println("-> resulting PDF: fox_dog_gif.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter05/fox_dog_gif.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			document.add(new Paragraph(
					"This is the animated gif added with Image.getInstance:"));
			document.add(Image.getInstance("resources/in_action/chapter05/animated_fox_dog.gif"));
			GifImage img = new GifImage("resources/in_action/chapter05/animated_fox_dog.gif");
			int frames = img.getFrameCount();
			document.add(new Paragraph("There are " + frames
					+ " frames in the animated gif file."));
			for (int i = 0; i < frames;) {
				++i;
				document.add(img.getImage(i));
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
