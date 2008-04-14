/* in_action/chapter08/StandardType1Fonts.java */

package in_action.chapter08;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class StandardType1Fonts {

	/**
	 * Generates a PDF file with the standard Type 1 Fonts.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example StandardType1Fonts");
		System.out.println("-> Creates a PDF file with 16 standard type 1 fonts.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: standard_type1_fonts.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter08/standard_type1_fonts.pdf"));

			// step 3: we open the document
			document.open();
			// step 4:

			// the 14 standard fonts in PDF: do not use this Font constructor!
			// this is for demonstration purposes only, use FontFactory!
			Font[] fonts = new Font[14];
			fonts[0] = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.NORMAL);
			fonts[1] = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.ITALIC);
			fonts[2] = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.BOLD);
			fonts[3] = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.BOLD
					| Font.ITALIC);
			fonts[4] = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.NORMAL);
			fonts[5] = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.ITALIC);
			fonts[6] = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD);
			fonts[7] = new Font(Font.HELVETICA, Font.DEFAULTSIZE,
					Font.BOLDITALIC);
			fonts[8] = new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL);
			fonts[9] = new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.ITALIC);
			fonts[10] = new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD);
			fonts[11] = new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE,
					Font.BOLDITALIC);
			fonts[12] = new Font(Font.SYMBOL, Font.DEFAULTSIZE);
			fonts[13] = new Font(Font.ZAPFDINGBATS, Font.DEFAULTSIZE,
					Font.UNDEFINED, new Color(0xFF, 0x00, 0x00));
			// add the content
			for (int i = 0; i < 14; i++) {
				document.add(new Paragraph(
						"quick brown fox jumps over the lazy dog", fonts[i]));
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

}
