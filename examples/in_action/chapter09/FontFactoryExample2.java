/* in_action/chapter09/FontFactoryExample2.java */

package in_action.chapter09;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FontFactoryExample2 {

	/**
	 * Generates a PDF file with fonts from the fontfactory.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example FontFactoryExample2");
		System.out.println("-> Creates a PDF file with fonts from the fontfactory.");
		System.out.println("-> resources needed: cmr10.afm, esl_gothic_unicode.ttf,");
		System.out.println("                     putr8a.afm and putr8a.pfb (chapter 8)");
		System.out.println("                     angsa.ttf, gara.ttf, garabd.ttf");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: font_factory2.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/font_factory2.pdf"));

			// step 3: we open the document
			document.open();
			// step 4:
			Font[] fonts = new Font[8];
			FontFactory.registerDirectory("resources/in_action/chapter08");
			System.out.println("Registered fonts");
			for (Iterator i = FontFactory.getRegisteredFonts().iterator(); i
					.hasNext();) {
				System.out.println((String) i.next());
			}
			fonts[0] = FontFactory.getFont("utopia-regular");
			fonts[1] = FontFactory.getFont("cmr10", 10);
			fonts[2] = FontFactory.getFont("utopia-regular", 10, Font.BOLD);
			fonts[3] = FontFactory.getFont("esl gothic unicode", 10,
					Font.UNDEFINED, new CMYKColor(255, 0, 0, 64));
			fonts[4] = FontFactory.getFont("utopia-regular", BaseFont.CP1252,
					BaseFont.EMBEDDED);
			FontFactory.registerDirectories();
			System.out.println("Registered font families");
			for (Iterator i = FontFactory.getRegisteredFamilies().iterator(); i
					.hasNext();) {
				System.out.println((String) i.next());
			}
			fonts[5] = FontFactory.getFont("angsana new", BaseFont.CP1252,
					BaseFont.EMBEDDED, 14);
			fonts[6] = FontFactory.getFont("garamond", BaseFont.CP1252,
					BaseFont.EMBEDDED, 10, Font.ITALIC);
			fonts[7] = FontFactory.getFont("garamond bold", BaseFont.CP1252,
					BaseFont.EMBEDDED, 10, Font.UNDEFINED, new CMYKColor(0,
							255, 0, 64));
			// add the content
			for (int i = 0; i < 8; i++) {
				document.add(new Paragraph(
						"Quick brown fox jumps over the lazy dog", fonts[i]));
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
