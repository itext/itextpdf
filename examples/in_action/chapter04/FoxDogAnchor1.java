/* in_action/chapter04/FoxDogAnchor1.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Anchor;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogAnchor1 {

	/**
	 * Generates a PDF file with the clickable text 'Quick brown fox jumps over
	 * the lazy dog'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogAnchor1");
		System.out.println("-> Creates a PDF file with the clickable text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is added using Anchor objects.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_anchors_external.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_anchors_external.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Font font = new Font();
			font.setStyle(Font.UNDERLINE);
			font.setColor(new GrayColor(0.3f));
			Anchor anchor = new Anchor(
					"Quick brown fox jumps over the lazy dog.", font);
			anchor
					.setReference("http://en.wikipedia.org/wiki/The_quick_brown_fox_jumps_over_the_lazy_dog");
			document.add(anchor);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}