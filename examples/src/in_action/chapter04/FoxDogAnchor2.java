/* in_action/chapter04/FoxDogAnchor2.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Anchor;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogAnchor2 {

	/**
	 * Generates a PDF file with internal links using Anchor.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogAnchor2");
		System.out.println("-> Creates a PDF file with internal links");
		System.out.println("   using Anchor.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_anchors_internal.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_anchors_internal.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Font font = new Font();
			font.setStyle(Font.UNDERLINE);
			font.setColor(new GrayColor(0.3f));
			Paragraph paragraph = new Paragraph("Quick brown ");
			Anchor foxReference = new Anchor("fox", font);
			foxReference.setReference("#fox");
			paragraph.add(foxReference);
			paragraph.add(" jumps over the lazy ");
			Anchor dogReference = new Anchor("dog", font);
			dogReference.setReference("#dog");
			paragraph.add(dogReference);
			paragraph.add(".");
			document.add(paragraph);
			document.newPage();
			Anchor foxName = new Anchor("This is the FOX.");
			foxName.setName("fox");
			document.add(foxName);
			for (int i = 0; i < 10; i++)
				document.add(Chunk.NEWLINE);
			Anchor dogName = new Anchor("This is the DOG.");
			dogName.setName("dog");
			document.add(dogName);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}