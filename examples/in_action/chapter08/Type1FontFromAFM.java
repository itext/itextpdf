/* in_action/chapter08/Type1FontFromAFM.java */

package in_action.chapter08;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Type1FontFromAFM {

	/**
	 * Generates a PDF file with the standard Type 1 Fonts.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example Type1FontFromAFM");
		System.out.println("-> Creates a PDF file with Type1 font.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resources needed: cmr10.afm, putr8a.afm");
		System.out.println("   com/lowagie/text/pdf/fonts/Times-Roman.afm");
		System.out.println("-> file generated: type1_font_afm.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter08/type1_font_afm.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			BaseFont bf1 = BaseFont.createFont(
					"/com/lowagie/text/pdf/fonts/Times-Roman.afm", "",
					BaseFont.NOT_EMBEDDED);
			Font font1 = new Font(bf1, 12);
			document
					.add(new Paragraph(
							"0123456789\nabcdefghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXZ",
							font1));
			BaseFont bf2 = BaseFont.createFont("resources/in_action/chapter08/putr8a.afm", "",
					BaseFont.NOT_EMBEDDED);
			Font font2 = new Font(bf2, 12);
			document
					.add(new Paragraph(
							"0123456789\nabcdefghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXZ",
							font2));
			BaseFont bf3 = BaseFont.createFont("resources/in_action/chapter08/cmr10.afm", "",
					BaseFont.NOT_EMBEDDED);
			Font font3 = new Font(bf3, 12);
			document
					.add(new Paragraph(
							"0123456789\nabcdefghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXZ",
							font3));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
