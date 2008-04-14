/* in_action/chapter08/TrueTypeFontEncoding.java */

package in_action.chapter08;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
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

public class TrueTypeFontEncoding {

	/**
	 * Generates a PDF file with a TrueType font using different encodings.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example TrueTypeFontEncoding");
		System.out.println("-> Creates a PDF file with Type1 font.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource needed: c:/windows/fonts/arialbd.ttf");
		System.out.println("-> file generated: ttf_encoding.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter08/ttf_encoding.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			BaseFont bf;
			Font font;

			bf = BaseFont.createFont("c:/windows/fonts/arialbd.ttf", "Cp1252",
					BaseFont.EMBEDDED);
			System.out.println(bf.getClass().getName());
			document.add(new Paragraph("Font: " + bf.getPostscriptFontName()));
			document.add(Chunk.NEWLINE);
			document.add(new Paragraph(
					"Movie title: A Very long Engagement (France)"));
			document.add(new Paragraph("directed by Jean-Pierre Jeunet"));
			document.add(new Paragraph("Encoding: " + bf.getEncoding()));
			font = new Font(bf, 12);
			document
					.add(new Paragraph("Un long dimanche de fiançailles", font));
			document.add(Chunk.NEWLINE);

			document.add(new Paragraph(
					"Movie title: No Man's Land (Bosnia-Herzegovina)"));
			document.add(new Paragraph("Directed by Danis Tanovic"));
			bf = BaseFont.createFont("c:/windows/fonts/arialbd.ttf", "Cp1250",
					BaseFont.EMBEDDED);
			document.add(new Paragraph("Encoding: " + bf.getEncoding()));
			font = new Font(bf, 12);
			byte[] noMansLand = { 'N', 'i', 'k', 'o', 'g', 'a', 'r',
					(byte) 0x9A, 'n', 'j', 'a', ' ', 'z', 'e', 'm', 'l', 'j',
					'a' };
			document.add(new Paragraph(new String(noMansLand), font));
			document.add(Chunk.NEWLINE);

			document.add(new Paragraph("Movie title: You I Love (Russia)"));
			bf = BaseFont.createFont("c:/windows/fonts/arialbd.ttf", "Cp1251",
					BaseFont.EMBEDDED);
			document.add(new Paragraph(
					"directed by Olga Stolpovskaja and Dmitry Troitsky"));
			document.add(new Paragraph("Encoding: " + bf.getEncoding()));
			font = new Font(bf, 12);
			char[] youILove = { 1071, ' ', 1083, 1102, 1073, 1083, 1102, ' ',
					1090, 1077, 1073, 1103 };
			document.add(new Paragraph(new String(youILove), font));
			document.add(Chunk.NEWLINE);

			document.add(new Paragraph("Movie title: Brides (Greece)"));
			document.add(new Paragraph("directed by Pantelis Voulgaris"));
			bf = BaseFont.createFont("c:/windows/fonts/arialbd.ttf", "Cp1253",
					BaseFont.EMBEDDED);
			document.add(new Paragraph("Encoding: " + bf.getEncoding()));
			font = new Font(bf, 12);
			byte[] brides = { -51, -3, -10, -27, -14 };
			document.add(new Paragraph(new String(brides, "Cp1253"), font));

			document.newPage();
			document.add(new Paragraph("Available code pages"));
			String[] encoding = bf.getCodePagesSupported();
			for (int i = 0; i < encoding.length; i++) {
				document.add(new Paragraph("encoding[" + i + "] = "
						+ encoding[i]));
			}
			document.newPage();
			document.add(new Paragraph("Full font names:"));
			String[][] name = bf.getFullFontName();
			for (int i = 0; i < name.length; i++) {
				document.add(new Paragraph(name[i][3] + " (" + name[i][0]
						+ "; " + name[i][1] + "; " + name[i][2] + ")"));
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