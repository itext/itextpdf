/* in_action/chapter04/FoxDogGeneric1.java */

package in_action.chapter04;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogGeneric1 extends PdfPageEventHelper {
	
	/**
	 * Draws an ellipse if the text was ellipse, a box if the text was box.
	 * 
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onGenericTag(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document, com.lowagie.text.Rectangle, java.lang.String)
	 */
	public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
		if ("ellipse".equals(text)) {
			PdfContentByte cb = writer.getDirectContent();
			cb.setRGBColorStroke(0xFF, 0x00, 0x00);
			cb.ellipse(rect.getLeft(), rect.getBottom() - 5f, rect.getRight(), rect.getTop());
			cb.stroke();
			cb.resetRGBColorStroke();
		}
		else if ("box".equals(text)) {
			PdfContentByte cb = writer.getDirectContentUnder();
			rect.setBackgroundColor(new Color(0xa5, 0x2a, 0x2a));
			cb.rectangle(rect);
		}
    }

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy dog'.
	 * @param args no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogGeneric1");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   some chunks are tagged.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_generic1.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
					// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_generic1.pdf"));
			writer.setPageEvent(new FoxDogGeneric1());
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Paragraph p = new Paragraph();
			Chunk fox = new Chunk("Quick brown fox");
			fox.setGenericTag("box");
			p.add(fox);
			p.add(" jumps over ");
			Chunk dog = new Chunk("the lazy dog.");
			dog.setGenericTag("ellipse");
			p.add(dog);
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