/* in_action/chapter04/FoxDogUnderline.java */

package in_action.chapter04;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogUnderline {

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy
	 * dog' that is underlined in different ways.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogUnderline");
		System.out.println("-> Creates a PDF file with underlined Chunks.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_underline.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_underline.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Chunk space = new Chunk(' ');
			Chunk foxLineUnder = new Chunk("Quick brown fox");
			foxLineUnder.setUnderline(0.2f, -2f);
			Chunk jumpsStrikeThrough = new Chunk("jumps over");
			jumpsStrikeThrough.setUnderline(0.5f, 3f);
			Chunk dogLineAbove = new Chunk("the lazy dog.");
			dogLineAbove.setUnderline(0.2f, 14f);
			Paragraph p = new Paragraph(foxLineUnder);
			p.add(space);
			p.add(jumpsStrikeThrough);
			p.add(space);
			p.add(dogLineAbove);
			document.add(p);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			Chunk c;
			c = new Chunk("Quick brown fox jumps over the lazy dog.");
			c.setUnderline(new Color(0x00, 0x00, 0xFF), 0.0f, 0.2f, 15.0f,
					0.0f, PdfContentByte.LINE_CAP_BUTT);
			c.setUnderline(new Color(0x00, 0xFF, 0x00), 5.0f, 0.0f, 0.0f,
					-0.5f, PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
			c.setUnderline(new Color(0xFF, 0x00, 0x00), 0.0f, 0.3f, 0.0f, 0.4f,
					PdfContentByte.LINE_CAP_ROUND);
			document.add(c);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			c = new Chunk("Quick brown fox jumps over the lazy dog.", new Font(
					Font.HELVETICA, 24));
			c.setUnderline(new Color(0x00, 0x00, 0xFF), 0.0f, 0.2f, 15.0f,
					0.0f, PdfContentByte.LINE_CAP_BUTT);
			c.setUnderline(new Color(0x00, 0xFF, 0x00), 5.0f, 0.0f, 0.0f,
					-0.5f, PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
			c.setUnderline(new Color(0xFF, 0x00, 0x00), 0.0f, 0.3f, 0.0f, 0.4f,
					PdfContentByte.LINE_CAP_ROUND);
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