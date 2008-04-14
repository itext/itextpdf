/* in_action/chapter04/FoxDogGoto2.java */

package in_action.chapter04;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogGoto2 {

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy
	 * dog'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogGoto2");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is added using Chunks with links.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox.pdf and dog.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writerA = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter04/fox.pdf"));
			PdfWriter writerB = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter04/dog.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			// a paragraph with a link to an external url
			Font font = FontFactory.getFont(FontFactory.HELVETICA, 12,
					Font.UNDERLINE, new Color(0, 0, 255));
			Paragraph p1 = new Paragraph("The quick brown fox wants to");
			Chunk chunk = new Chunk(" jump over ", font);
			chunk.setRemoteGoto("dog.pdf", "jump");
			p1.add(chunk);
			p1.add(" the lazy dog.");

			// some paragraph
			Paragraph p2 = new Paragraph("blah, blah, blah");

			// two paragraphs with a local destination
			Paragraph p3 = new Paragraph("The quick brown fox has jumped over ");
			p3
					.add(new Chunk("the lazy dog.", font)
							.setLocalDestination("jump"));

			// a special remote goto
			Paragraph p4 = new Paragraph("you can also jump to a ");
			p4.add(new Chunk("specific page on another document", font)
					.setRemoteGoto("fox.pdf", 3));

			// we add all the content
			writerB.pause();
			document.add(p1);
			writerB.resume();
			document.add(p2);
			document.add(p2);
			document.add(p2);
			document.add(p2);
			document.add(p2);
			document.add(p2);
			document.add(p2);
			writerA.pause();
			document.add(p3);
			document.add(p4);
			writerA.resume();
			writerB.pause();
			document.newPage();
			document.add(new Paragraph("page 2"));
			document.newPage();
			document.add(new Paragraph("page 3"));
			writerB.resume();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}