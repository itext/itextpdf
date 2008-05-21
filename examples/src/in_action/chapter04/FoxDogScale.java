/* in_action/chapter04/FoxDogScale.java */

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

public class FoxDogScale {

	/**
	 * Generates a PDF file. Chunks are measured and scaled.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogScale");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is measured and scaled.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_scaling.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_scaling.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Chunk c = new Chunk("quick brown fox jumps over the lazy dog");
			float w = c.getWidthPoint();
			Paragraph p = new Paragraph("The width of the chunk: '");
			p.add(c);
			p.add("' is ");
			p.add(String.valueOf(w));
			p.add(" points or ");
			p.add(String.valueOf(w / 72f));
			p.add(" inches or ");
			p.add(String.valueOf(w / 72f * 2.54f));
			p.add(" cm.");
			document.add(p);
			document.add(c);
			document.add(Chunk.NEWLINE);
			c.setHorizontalScaling(0.5f);
			document.add(c);
			document.add(c);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}