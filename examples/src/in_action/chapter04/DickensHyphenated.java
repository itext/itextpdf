/* in_action/chapter04/DickensHyphenated.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.HyphenationAuto;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class DickensHyphenated {

	/**
	 * Generates a PDF file with hyphenated text.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example DickensHyphenated");
		System.out.println("-> Creates a PDF file with a page");
		System.out.println("   from Charles Dickens.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("   jars needed: itext-hyph-xml.jar");
		System.out.println("-> resulting PDF: dickens.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A6);
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/dickens.pdf"));
			// step 3: we open the document
			document.open();
			document.setMarginMirroring(true);
			// step 4: we add a paragraph to the document
			String text = "It was the best of times, it was the worst of times, "
					+ "it was the age of wisdom, it was the age of foolishness, "
					+ "it was the epoch of belief, it was the epoch of incredulity, "
					+ "it was the season of Light, it was the season of Darkness, "
					+ "it was the spring of hope, it was the winter of despair, "
					+ "we had everything before us, we had nothing before us, "
					+ "we were all going direct to Heaven, we were all going direct "
					+ "the other way\u2014in short, the period was so far like the present "
					+ "period, that some of its noisiest authorities insisted on its "
					+ "being received, for good or for evil, in the superlative degree "
					+ "of comparison only.";
			Chunk ck = new Chunk(text);
			HyphenationAuto auto = new HyphenationAuto("en", "GB", 2, 2);
			ck.setHyphenation(auto);
			Paragraph p = new Paragraph(ck);
			p.setAlignment(Paragraph.ALIGN_JUSTIFIED);
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