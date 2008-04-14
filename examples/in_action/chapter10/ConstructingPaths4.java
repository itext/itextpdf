/* in_action/chapter10/ConstructingPaths4.java */

package in_action.chapter10;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
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

public class ConstructingPaths4 {

	/**
	 * Generates a PDF file with paths that are constructed and painted.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example ConstructingPaths4");
		System.out.println("-> Creates a PDF file with paths that are constructed");
		System.out.println("   and painted.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: paths4.pdf");
		// step 1: creation of a document-object
		Document.compress = false;
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter10/paths4.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			cb.setColorStroke(new GrayColor(0.2f));
			cb.setColorFill(new GrayColor(0.9f));
			cb.circle(70, 770, 40);
			cb.ellipse(120, 730, 240, 810);
			cb.arc(250, 730, 370, 810, 45, 270);
			cb.roundRectangle(30, 620, 80, 100, 20);
			cb.fillStroke();
			Rectangle rect;
			rect = new Rectangle(120, 620, 240, 720);
			rect.setBorder(Rectangle.BOX);
			rect.setBorderWidth(5);
			rect.setBorderColor(new GrayColor(0.2f));
			rect.setBackgroundColor(new GrayColor(0.9f));
			cb.rectangle(rect);
			rect = new Rectangle(250, 620, 370, 720);
			rect.setBorder(Rectangle.BOX);
			rect.setBorderWidthTop(15);
			rect.setBorderWidthBottom(1);
			rect.setBorderWidthLeft(5);
			rect.setBorderWidthRight(10);
			rect.setBorderColorTop(new GrayColor(0.2f));
			rect.setBorderColorBottom(new Color(0xFF, 0x00, 0x00));
			rect.setBorderColorLeft(new Color(0xFF, 0xFF, 0x00));
			rect.setBorderColorRight(new Color(0x00, 0x00, 0xFF));
			rect.setBackgroundColor(new GrayColor(0.9f));
			cb.rectangle(rect);
			cb.variableRectangle(rect);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5: we close the document
		document.close();
	}
}
