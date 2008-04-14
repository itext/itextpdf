/* in_action/chapter04/FoxDogGeneric2.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogGeneric2 extends PdfPageEventHelper {
	
	/** the font for the speakers */
	public static final Font SPEAKER = new Font(Font.HELVETICA, 12, Font.BOLD);
	
	/** keeps the number of lines every body is saying. */
	protected HashMap lines = new HashMap();
	
	/**
	 * Keeps count of the number of times somebody says something.
	 * 
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onGenericTag(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document, com.lowagie.text.Rectangle, java.lang.String)
	 */
	public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
		Integer count = (Integer) lines.get(text);
		if (count == null) {
			lines.put(text, new Integer(1));
		}
		else {
			lines.put(text, new Integer(count.intValue() + 1));
		}
    }
	
	/**
	 * @return Returns the lines.
	 */
	public HashMap getLines() {
		return lines;
	}

	/**
	 * Generates a PDF file with a 'screenplay' of a fox jumping over a dog.
	 * @param args no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogGeneric2");
		System.out.println("-> Creates a PDF file with the a 'screenplay';");
		System.out.println("   the lines per speaker are counted automatically.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_generic2.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
					// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_generic2.pdf"));
			FoxDogGeneric2 tracker = new FoxDogGeneric2();
			writer.setPageEvent(tracker);
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			document.add(getLine("Fox", "Hello lazy dog."));
			document.add(getLine("Dog", "Hello quick brown fox."));
			document.add(getLine("Fox", "I want to jump over you."));
			document.add(getLine("Dog", "No problem. Go ahead!"));
			document.add(getLine("Narrator", "And the fox jumps over the dog."));
			document.add(getLine("Fox", "Thank you very much, lazy dog."));
			document.add(getLine("Dog", "You're welcome."));
			document.add(getLine("Fox", "See you."));
			document.add(getLine("Dog", "Bye!"));
			document.add(getLine("Narrator", "The fox leaves the dog."));
			document.newPage();
			HashMap lines = tracker.getLines();
			for (Iterator i = lines.keySet().iterator(); i.hasNext(); ) {
				String speaker = (String)i.next();
				Integer count = (Integer)lines.get(speaker);
				document.add(new Paragraph(speaker + ": " + count.intValue() + " lines."));
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	/**
	 * @param speaker
	 * @param line
	 * @return a paragraph
	 */
	private static Paragraph getLine(String speaker, String line) {
		Paragraph p = new Paragraph(18);
		Chunk s = new Chunk(speaker + ": ", SPEAKER);
		s.setGenericTag(speaker);
		p.add(s);
		p.add(line);
		return p;
	}
}