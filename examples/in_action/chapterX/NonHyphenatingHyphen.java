package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.SplitCharacter;
import com.lowagie.text.pdf.PdfChunk;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class NonHyphenatingHyphen implements SplitCharacter {
	public static void main(String[] args) {
		String string = "Look at this paragraph with a lot of different products and at least one product called Kautschuk-Plant, where the plant itself can be planted, on the other hand, there are some more words with absolutely no sense.";
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapterX/hyphen_not_hyphenated.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			document.add(new Paragraph(string));
			Chunk c = new Chunk(string);
			c.setSplitCharacter(new NonHyphenatingHyphen());
			document.add(new Paragraph(c));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	public boolean isSplitCharacter(int start, int current, int end, char[] cc,
			PdfChunk[] ck) {
		char c;
		if (ck == null)
			c = cc[current];
		else
			c = (char)ck[Math.min(current, ck.length - 1)]
					.getUnicodeEquivalent(cc[current]);
		return (c == ' ');
	}
}
