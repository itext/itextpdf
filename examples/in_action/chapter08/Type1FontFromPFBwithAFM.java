/* in_action/chapter08/Type1FontFromPFBwithAFM.java */

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

public class Type1FontFromPFBwithAFM {

	/**
	 * Generates a PDF file with the standard Type 1 Fonts.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example Type1FontFromPFBwithAFM");
		System.out.println("-> Creates a PDF file with Type1 font.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resources needed: cmr10.afm and cmr10.pfb");
		System.out.println("                           putr08.afm and putr08.pfb");
		System.out.println("-> file generated: type1_font_pfb_with_afm.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter08/type1_font_pfb_with_afm.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			BaseFont bf = BaseFont.createFont("resources/in_action/chapter08/putr8a.afm", "",
					BaseFont.EMBEDDED);
			Font font = new Font(bf, 12);
			document
					.add(new Paragraph(
							"0123456789\nabcdefghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXZ",
							font));
			bf = BaseFont.createFont("resources/in_action/chapter08/cmr10.afm", "",
					BaseFont.EMBEDDED);
			font = new Font(bf, 12);
			document
					.add(new Paragraph(
							"0123456789\nabcdefghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXZ",
							font));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
