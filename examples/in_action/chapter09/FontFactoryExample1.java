/* in_action/chapter09/FontFactoryExample1.java */

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

public class FontFactoryExample1 {

	/**
	 * Generates a PDF file with fonts from the fontfactory.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example FontFactoryExample1");
		System.out.println("-> Creates a PDF file with fonts from the fontfactory.");
		System.out.println("-> resources needed: cmr10.afm, cmr10.pfb (chapter 8)");
		System.out.println("                     arial.ttf, gara.ttf, garabd.ttf, garait.ttf");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: font_factory1.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/font_factory1.pdf"));

			// step 3: we open the document
			document.open();
			// step 4:
			Font[] fonts = new Font[10];
			fonts[0] = FontFactory.getFont("Times-Roman");
			fonts[1] = FontFactory.getFont("Courier", 10);
			fonts[2] = FontFactory.getFont("Courier", 10, Font.BOLD);
			fonts[3] = FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD,
					new CMYKColor(255, 0, 0, 64));
			fonts[4] = FontFactory.getFont("c:/windows/fonts/arial.ttf",
					BaseFont.CP1252, BaseFont.EMBEDDED);
			FontFactory.register("resources/in_action/chapter08/cmr10.afm");
			fonts[5] = FontFactory.getFont("CMR10", BaseFont.CP1252,
					BaseFont.EMBEDDED);
			fonts[5].getBaseFont().setPostscriptFontName("Computer Modern");
			FontFactory.register("c:/windows/fonts/gara.ttf", "Manning");
			FontFactory.register("c:/windows/fonts/garabd.ttf", "Manning-bold");
			FontFactory.register("c:/windows/fonts/garait.ttf",
					"Manning-italic");
			fonts[6] = FontFactory.getFont("Manning", BaseFont.CP1252,
					BaseFont.EMBEDDED);
			fonts[7] = FontFactory.getFont("Manning-bold", BaseFont.CP1252,
					BaseFont.EMBEDDED, 10);
			fonts[8] = FontFactory.getFont("Manning", BaseFont.CP1252,
					BaseFont.EMBEDDED, 10, Font.ITALIC);
			fonts[9] = FontFactory.getFont("garamond vet", BaseFont.CP1252,
					BaseFont.EMBEDDED, 10, Font.UNDEFINED, new CMYKColor(0,
							255, 0, 64));
			System.out.println("Registered fonts");
			for (Iterator i = FontFactory.getRegisteredFonts().iterator(); i
					.hasNext();) {
				System.out.println((String) i.next());
			}
			System.out.println("Registered font families");
			for (Iterator i = FontFactory.getRegisteredFamilies().iterator(); i
					.hasNext();) {
				System.out.println((String) i.next());
			}
			// add the content
			for (int i = 0; i < 10; i++) {
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
