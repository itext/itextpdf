/* in_action/chapter04/FoxDogChapter2.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.ChapterAutoNumber;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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

public class FoxDogChapter2 {

	/**
	 * Generates a PDF file with autonumbered chapters and an open bookmark tab
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogChapter2");
		System.out.println("-> Creates a PDF file with autonumbered chapters");
		System.out.println("   and an open bookmark tab.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_chapter2.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_chapter2.pdf"));
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Phrase text = new Phrase(
					"Quick brown fox jumps over the lazy dog. ");
			ChapterAutoNumber chapter1 = new ChapterAutoNumber(
					"This is a sample sentence:");
			chapter1.setBookmarkTitle("The fox");
			chapter1.setBookmarkOpen(false);
			Section section1 = chapter1.addSection("Quick");
			section1.add(text);
			section1.add(text);
			section1.add(text);
			Section section2 = chapter1.addSection("Fox");
			section2.add(text);
			section2.add(text);
			section2.add(text);
			document.add(chapter1);
			ChapterAutoNumber chapter2 = new ChapterAutoNumber("Jumps");
			Section section = chapter2.addSection("Over");
			section.add(text);
			section.add(text);
			section.add(text);
			Section subsection1 = section.addSection("Lazy");
			subsection1.setIndentationLeft(30);
			subsection1.add(text);
			subsection1.add(text);
			subsection1.add(text);
			subsection1.add(text);
			subsection1.add(text);
			Section subsection2 = section.addSection("Dog");
			subsection2.setIndentationRight(30);
			subsection2.add(text);
			subsection2.add(text);
			subsection2.add(text);
			subsection2.add(text);
			subsection2.add(text);
			Section subsection3 = section.addSection("Did you see it?");
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