/* in_action/chapter04/FoxDogChapter1.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogChapter1 {

	/**
	 * Generates a PDF file with chapters and sections.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogChapter1");
		System.out.println("-> Creates a PDF file with chapters and sections.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_chapter1.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_chapter1.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Phrase text = new Phrase(
					"Quick brown fox jumps over the lazy dog. ");
			Font font = new Font(Font.HELVETICA, 14, Font.BOLD);
			Chapter chapter1 = new Chapter(new Paragraph("This is the title.",
					font), 1);
			chapter1.add(text);
			Section section1 = chapter1.addSection("Quick", 0);
			section1.add(text);
			section1.add(text);
			section1.add(text);
			Section section2 = chapter1.addSection("Fox", 0);
			section2.add(text);
			section2.add(text);
			section2.add(text);
			document.add(chapter1);
			Chapter chapter2 = new Chapter("Jumps", 2);
			Section section = chapter2.addSection("Over", 1);
			section.add(text);
			section.add(text);
			section.add(text);
			Section subsection1 = section.addSection("Lazy", 2);
			subsection1.setIndentationLeft(30);
			subsection1.add(text);
			subsection1.add(text);
			subsection1.add(text);
			subsection1.add(text);
			subsection1.add(text);
			Section subsection2 = section.addSection("Dog", 2);
			subsection2.setIndentationRight(30);
			subsection2.add(text);
			subsection2.add(text);
			subsection2.add(text);
			subsection2.add(text);
			subsection2.add(text);
			Section subsection3 = section.addSection("Did you see it?", 2);
			subsection3.setIndentation(50);
			subsection3.add(text);
			subsection3.add(text);
			subsection3.add(text);
			subsection3.add(text);
			subsection3.add(text);
			document.add(chapter2);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}