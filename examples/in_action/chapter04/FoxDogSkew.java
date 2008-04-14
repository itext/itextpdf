/* in_action/chapter04/FoxDogSkew.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogSkew {

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy
	 * dog'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogSkew");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is skewed.");
		System.out.println("   that are being skewed.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_skew.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_skew.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Paragraph p;
			Chunk chunk;
			p = new Paragraph();
			chunk = new Chunk("Quick brown fox");
			chunk.setSkew(15f, -30f);
			p.add(chunk);
			chunk = new Chunk(" jumps over ");
			chunk.setSkew(15f, 15f);
			p.add(chunk);
			chunk = new Chunk("the lazy dog.");
			chunk.setSkew(-30f, 15f);
			p.add(chunk);
			document.add(p);
			p = new Paragraph();
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			p = new Paragraph();
			chunk = new Chunk("Quick brown fox");
			chunk.setSkew(45f, 0f);
			p.add(chunk);
			chunk = new Chunk(" jumps over ");
			p.add(chunk);
			chunk = new Chunk("the lazy dog.");
			chunk.setSkew(-45f, 0f);
			p.add(chunk);
			document.add(p);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			p = new Paragraph();
			chunk = new Chunk("Quick brown fox");
			chunk.setSkew(0f, 25f);
			p.add(chunk);
			chunk = new Chunk(" jumps over ");
			p.add(chunk);
			chunk = new Chunk("the lazy dog.");
			chunk.setSkew(0f, -25f);
			p.add(chunk);
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