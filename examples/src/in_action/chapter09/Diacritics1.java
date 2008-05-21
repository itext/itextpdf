/* in_action/chapter09/Diacritics1.java */

package in_action.chapter09;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
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

public class Diacritics1 {

	/**
	 * Generates a PDF file demonstrating a problem with diacritics.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example Diacritics1");
		System.out.println("-> Creates a PDF file demonstrating a problem with diacritics.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: tears.jpg, angsa.ttf and arialuni.ttf");
		System.out.println("-> file generated: diacritics1.pdf");

		// step 1
		String movieTitle = "\u0e1f\u0e49\u0e32\u0e17\u0e30\u0e25\u0e32\u0e22\u0e42\u0e08\u0e23";
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/diacritics1.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			BaseFont bf;
			Font font;
			bf = BaseFont.createFont("c:/windows/fonts/angsa.ttf",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			font = new Font(bf, 20);
			Image img = Image.getInstance("resources/in_action/chapter09/tears.jpg");
			img.scalePercent(50);
			img.setBorderWidth(18f);
			img.setBorder(Image.BOX);
			img.setBorderColor(new Color(0xFF, 0xFF, 0xFF));
			img.setAlignment(Element.ALIGN_LEFT | Image.TEXTWRAP);
			document.add(img);
			document.add(new Paragraph(
					"Movie title: Tears of the Black Tiger (Thailand)"));
			document.add(new Paragraph("directed by Wisit Sasanatieng"));
			document.add(new Paragraph("Font: " + bf.getPostscriptFontName()));
			document.add(new Paragraph(movieTitle, font));
			bf = BaseFont.createFont("c:/windows/fonts/arialuni.ttf",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			font = new Font(bf, 12);
			document.add(new Paragraph("Font: " + bf.getPostscriptFontName()));
			document.add(new Paragraph(movieTitle, font));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
