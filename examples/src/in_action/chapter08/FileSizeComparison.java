/* in_action/chapter08/FileSizeComparison.java */

package in_action.chapter08;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FileSizeComparison {

	/**
	 * Compares files with and without font embedding.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example FileSizeComparison");
		System.out.println("-> Creates a PDF file with a font that is not embedded");
		System.out.println("   and compared it with files that have the font embedded.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: c:/windows/fonts/comic.ttf");
		System.out.println("-> file generated: font__not_embedded.pdf");
		System.out.println("   file generated: font_embedded1.pdf");
		System.out.println("   file generated: font_embedded2.pdf");

		// step 1
		Document document1 = new Document();
		Document document2 = new Document();
		Document document3 = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document1, new FileOutputStream(
					"results/in_action/chapter08/font_not_embedded.pdf"));
			PdfWriter.getInstance(document2, new FileOutputStream(
					"results/in_action/chapter08/font_embedded1.pdf"));
			PdfWriter.getInstance(document3, new FileOutputStream(
					"results/in_action/chapter08/font_embedded2.pdf"));

			// step 3: we open the document
			document1.open();
			document2.open();
			document3.open();
			// step 4:
			BaseFont bf_not_embedded = BaseFont.createFont(
					"c:/windows/fonts/comic.ttf", BaseFont.CP1252,
					BaseFont.NOT_EMBEDDED);
			BaseFont bf_embedded = BaseFont.createFont(
					"c:/windows/fonts/comic.ttf", BaseFont.CP1252,
					BaseFont.EMBEDDED);
			Font font_not_embedded = new Font(bf_not_embedded, 12);
			Font font_embedded = new Font(bf_embedded, 12);

			document1.add(new Paragraph(
					"quick brown fox jumps over the lazy dog",
					font_not_embedded));
			document2.add(new Paragraph(
					"quick brown fox jumps over the lazy dog", font_embedded));
			document3.add(new Paragraph(
					"ooooo ooooo ooo ooooo oooo ooo oooo ooo", font_embedded));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document1.close();
		document2.close();
		document3.close();
		RandomAccessFileOrArray file;
		try {
			file = new RandomAccessFileOrArray("results/in_action/chapter08/font_not_embedded.pdf");
			System.out.println("Size font_not_embedded.pdf: " + file.length());
			file = new RandomAccessFileOrArray("results/in_action/chapter08/font_embedded1.pdf");
			System.out.println("Size font_embedded1.pdf: " + file.length());
			file = new RandomAccessFileOrArray("results/in_action/chapter08/font_embedded2.pdf");
			System.out.println("Size font_embedded2.pdf: " + file.length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}