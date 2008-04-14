/* in_action/chapter04/FoxDogSplit.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.SplitCharacter;
import com.lowagie.text.pdf.PdfChunk;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogSplit implements SplitCharacter {

	/**
	 * @see com.lowagie.text.SplitCharacter#isSplitCharacter(int, int, int,
	 *      char[], com.lowagie.text.pdf.PdfChunk[])
	 */
	public boolean isSplitCharacter(int start, int current, int end, char[] cc,
			PdfChunk[] ck) {
		char c;
		if (ck == null)
			c = cc[current];
		else
			c = ck[Math.min(current, ck.length - 1)]
					.getUnicodeEquivalent(cc[current]);
		return (c == '/' || c == ' ');
	}

	/**
	 * Generates a PDF file that has a Chunk with a custom split character.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogSplit");
		System.out.println("-> Creates a PDF file with text");
		System.out.println("   that has a custom split character.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_split.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_split.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Chunk urlChunk;
			Paragraph p;
			Font font = new Font(Font.HELVETICA, 18);
			String text = "This is the link that explains the sentence 'Quick brown fox jumps over the lazy dog: ";
			String url = "http://en.wikipedia.org/wiki/The_quick_brown_fox_jumps_over_the_lazy_dog";

			document.add(new Paragraph("Default split behavior."));
			p = new Paragraph(24, new Chunk(text, font));
			urlChunk = new Chunk(url, font);
			p.add(urlChunk);
			document.add(p);

			document.add(Chunk.NEWLINE);

			document.add(new Paragraph(
					"Space and forward slash are split characters."));
			p = new Paragraph(24, new Chunk(text, font));
			urlChunk = new Chunk(url, font);
			urlChunk.setSplitCharacter(new FoxDogSplit());
			p.add(urlChunk);
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