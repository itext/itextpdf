/* in_action/chapter04/FoxDogColor.java */

package in_action.chapter04;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogColor {

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy
	 * dog'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogColor");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is added using colored Chunk objects.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_color.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_color.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Font font = new Font(Font.COURIER, 10, Font.BOLD);
			Phrase p;
			font.setColor(new Color(0xFF, 0xFF, 0xFF));
			Chunk fox = new Chunk("quick brown fox", font);
			fox.setBackground(new Color(0xa5, 0x2a, 0x2a));
			p = new Phrase();
			p.add(fox);
			p.add(" jumps over ");
			Chunk dog = new Chunk("the lazy dog", new Font(Font.TIMES_ROMAN,
					14, Font.ITALIC));
			dog.setBackground(new Color(0xFF, 0x00, 0x00), 10, -30, 20, -10);
			p.add(dog);
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