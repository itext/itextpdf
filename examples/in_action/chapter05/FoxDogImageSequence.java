/* in_action/chapter05/FoxDogImageSequence.java */

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

public class FoxDogImageSequence {

	/**
	 * Generates a PDF file with Images.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogImageSequence");
		System.out.println("-> Creates a PDF file with images");
		System.out.println("   of a brown fox and a lazy dog.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: foxdog.jpg and dog.gif");
		System.out.println("-> resulting PDF: fox_dog_imageNotInSequence.pdf");
		System.out.println("   and fox_dog_imageInSequence.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter05/fox_dog_imageNotInSequence.pdf"));
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter05/fox_dog_imageInSequence.pdf"));
			writer.setStrictImageSequence(true);
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Image jpg = Image.getInstance("resources/in_action/chapter05/foxdog.jpg");
			Image gif = Image.getInstance("resources/in_action/chapter05/dog.gif");
			document.add(new Paragraph("image 1"));
			document.add(jpg);
			document.add(new Paragraph("image 2"));
			document.add(gif);
			document.add(new Paragraph("image 3"));
			document.add(jpg);
			document.add(new Paragraph("image 4"));
			document.add(gif);
			document.add(new Paragraph("image 5"));
			document.add(jpg);
			document.add(new Paragraph("image 6"));
			document.add(gif);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
