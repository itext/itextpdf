/* in_action/chapter04/FoxDogSpaceCharRatio.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogSpaceCharRatio {

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy
	 * dog'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogSpaceCharRatio");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is added using Paragraph objects");
		System.out.println("   and the char/space ratio is changed.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_space_char_ratio.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A8.rotate());
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_space_char_ratio.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			String text = "Quick brown fox jumps over the lazy dog.";
			Paragraph paragraph = new Paragraph(text);
			paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(paragraph);
			document.newPage();
			writer.setSpaceCharRatio(PdfWriter.NO_SPACE_CHAR_RATIO);
			document.add(paragraph);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}