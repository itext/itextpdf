/* in_action/chapter04/FoxDogGoto3.java */

package in_action.chapter04;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogGoto3 {

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy
	 * dog'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogGoto3");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is added using Chunks with links.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_goto3.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_goto3.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			// a paragraph with a local goto
			Paragraph p1 = new Paragraph("The Quick brown fox wants to");
			p1.add(new Chunk(" jump over ", FontFactory.getFont(
					FontFactory.HELVETICA, 12, Font.NORMAL,
					new Color(0, 0, 255))).setLocalGoto("jump"));
			p1.add("the lazy dog.");

			// some paragraph
			Paragraph p2 = new Paragraph("blah, blah, blah");

			// a paragraph with a local destination
			Paragraph p3 = new Paragraph("The fox");
			p3.add(new Chunk(" has jumped ", FontFactory.getFont(
					FontFactory.HELVETICA, 12, Font.NORMAL,
					new Color(0, 255, 0))).setLocalDestination("jump"));
			p3.add("over the lazy dog.");

			// we add the content
			document.add(p1);
			document.add(p2);
			document.add(p2);
			document.add(p2);
			document.add(p2);
			document.add(p2);
			document.add(p2);
			document.add(p2);
			document.add(p3);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}