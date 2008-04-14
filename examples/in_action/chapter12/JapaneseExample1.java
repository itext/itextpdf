/* in_action/chapter12/JapaneseExample1.java */

package in_action.chapter12;

import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AsianFontMapper;
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

public class JapaneseExample1 {

	/**
	 * PdfGraphics2D example with Japanese characters.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: JapaneseExample1");
		System.out.println("-> Creates a PDF with a word in Japanese.");
		System.out.println("-> jars needed: iText.jar and iTextAsian.jar");
		System.out.println("-> file generated: japanese1.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter12/japanese1.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			String text = "\u5e73\u548C";
			// we create a template and a Graphics2D object that corresponds
			// with it
			PdfContentByte cb = writer.getDirectContent();
			PdfTemplate tp = cb.createTemplate(100, 50);
			AsianFontMapper mapper = new AsianFontMapper(
					AsianFontMapper.JapaneseFont_Min,
					AsianFontMapper.JapaneseEncoding_H);
			Graphics2D g2 = tp.createGraphics(100, 50, mapper);
			java.awt.Font font = new java.awt.Font("Arial Unicode MS",
					java.awt.Font.PLAIN, 12);
			g2.setFont(font);
			g2.drawString(text, 0, 40);
			g2.dispose();
			cb.addTemplate(tp, 36, 780);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
