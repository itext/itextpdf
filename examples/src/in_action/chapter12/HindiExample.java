/* in_action/chapter12/HindiExample.java */

package in_action.chapter12;

import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HindiExample {

	/**
	 * Java does ligatures of Indic languages.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: HindiExample");
		System.out.println("-> Creates a PDF with a word in Hindi.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource needed: arialuni.ttf");
		System.out.println("-> file generated: hindi.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter12/hindi.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			String text = "\u0936\u093e\u0902\u0924\u093f";
			BaseFont bf = BaseFont.createFont("c:/windows/fonts/arialuni.ttf",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			document.add(new Paragraph("Pure iText: " + text,
					new com.lowagie.text.Font(bf, 12)));
			PdfContentByte cb = writer.getDirectContent();
			PdfTemplate tp = cb.createTemplate(100, 50);
			Graphics2D g2 = tp.createGraphicsShapes(100, 50);
			java.awt.Font font = new java.awt.Font("Arial Unicode MS",
					java.awt.Font.PLAIN, 12);
			g2.setFont(font);
			g2.drawString("Graphics2D: " + text, 0, 40);
			g2.dispose();
			cb.addTemplate(tp, 36, 750);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
