/* in_action/chapter10/DirectContent.java */

package in_action.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class DirectContent {

	/**
	 * Generates a PDF file showing the different canvases in iText.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example DirectContent");
		System.out.println("-> Creates a PDF file showing the different canvases in iText.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: direct_content.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter10/direct_content.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte over = writer.getDirectContent();
			PdfContentByte under = writer.getDirectContentUnder();
			drawLayer(over, 70, 750, 150, 100);
			drawLayer(under, 70, 730, 150, 100);
			Paragraph p = new Paragraph("quick brown fox ");
			Chunk c = new Chunk("jumps");
			c.setBackground(new GrayColor(0.5f));
			p.add(c);
			p.add(" over the lazy dog");
			for (int i = 0; i < 10; i++) {
				document.add(p);
			}
			drawLayer(over, 70, 670, 150, 100);
			drawLayer(under, 70, 650, 150, 100);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	public static void drawLayer(PdfContentByte cb, float llx, float lly,
			float w, float h) {
		cb.saveState();
		cb.setColorFill(new GrayColor(0.9f));
		cb.setColorStroke(new GrayColor(0.2f));
		cb.moveTo(llx, lly);
		cb.lineTo(llx + w / 4, lly + h / 3);
		cb.lineTo(llx + 7 * w / 8, lly + h / 3);
		cb.lineTo(llx + (2 * w) / 3, lly);
		cb.closePathFillStroke();
		cb.restoreState();
	}
}
