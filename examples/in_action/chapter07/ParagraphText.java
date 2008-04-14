/* in_action/chapter07/ParagraphText.java */

package in_action.chapter07;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
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

public class ParagraphText {

	/**
	 * Generates a PDF file with several paragraphs.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 7: example ParagraphText");
		System.out.println("-> Creates a PDF file with several paragraphs.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource: caesar.txt");
		System.out.println("-> file generated: caesar.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter07/caesar.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			BufferedReader reader = new BufferedReader(new FileReader(
					"resources/in_action/chapter07/caesar.txt"));
			String line;
			Paragraph p;
			while ((line = reader.readLine()) != null) {
				p = new Paragraph("    " + line);
				p.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(p);
			}
			reader.close();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}