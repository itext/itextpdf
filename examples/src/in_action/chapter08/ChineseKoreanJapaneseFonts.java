/* in_action/chapter08/ChineseKoreanJapaneseFonts.java */

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

public class ChineseKoreanJapaneseFonts {

	/**
	 * Generates a PDF file with CJK fonts.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example ChineseKoreanJapaneseFonts");
		System.out.println("-> Creates a PDF file with a CJK font.");
		System.out.println("-> jars needed: iText.jar and iTextAsian.jar");
		System.out.println("-> file generated: cjk.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter08/cjk.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			BaseFont bf;
			Font font;
			bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
					BaseFont.NOT_EMBEDDED);
			font = new Font(bf, 12);
			System.out.println(bf.getClass().getName());
			document.add(new Paragraph(
					"Movie title: House of The Flying Daggers (China)", font));
			document.add(new Paragraph("directed by Zhang Yimou", font));
			document.add(new Paragraph("Font: " + bf.getPostscriptFontName(),
					font));
			document.add(new Paragraph("\u5341\u950a\u57cb\u4f0f", font));

			bf = BaseFont.createFont("KozMinPro-Regular", "UniJIS-UCS2-H",
					BaseFont.EMBEDDED);
			font = new Font(bf, 12);
			document.add(new Paragraph("Movie title: Nobody Knows (Japan)",
					font));
			document.add(new Paragraph("directed by Hirokazu Koreeda", font));
			document.add(new Paragraph("Font: " + bf.getPostscriptFontName(),
					font));
			document.add(new Paragraph("\u8ab0\u3082\u77e5\u3089\u306a\u3044",
					font));

			bf = BaseFont.createFont("HYGoThic-Medium", "UniKS-UCS2-H",
					BaseFont.NOT_EMBEDDED);
			font = new Font(bf, 12);
			document.add(new Paragraph(
					"Movie title: '3-Iron' aka 'Bin-jip' (South-Korea)", font));
			document.add(new Paragraph("directed by Kim Ki-Duk", font));
			document.add(new Paragraph("Font: " + bf.getPostscriptFontName(),
					font));
			document.add(new Paragraph("\ube48\uc9d1", font));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
