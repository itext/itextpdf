/* in_action/chapter09/Diacritics2.java */

package in_action.chapter09;

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

public class Diacritics2 {

	/**
	 * Generates a PDF file with a possible solution for the diacritics problem.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example Diacritics2");
		System.out.println("-> Creates a PDF file with a possible solution for the diacritics problem.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: arial.ttf and cour.ttf");
		System.out.println("-> file generated: diacritics2.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/diacritics2.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			BaseFont bf;
			Font font;
			bf = BaseFont.createFont("c:/windows/fonts/arial.ttf",
					BaseFont.CP1252, BaseFont.EMBEDDED);
			font = new Font(bf, 12);
			document.add(new Paragraph(
					"Movie title: In Bed With Santa (Sweden)", font));
			document.add(new Paragraph("directed by Kjell Sundvall", font));
			document.add(new Paragraph("Tomten är far till alla barnen", font));
			System.out.println(bf.getPostscriptFontName());
			System.out.println("Width in arial.ttf: " + bf.getWidth('¨'));
			bf.setCharAdvance('¨', -100);
			document
					.add(new Paragraph("Tomten ¨ar far till alla barnen", font));
			bf = BaseFont.createFont("c:/windows/fonts/cour.ttf",
					BaseFont.CP1252, BaseFont.EMBEDDED);
			System.out.println(bf.getPostscriptFontName());
			System.out.println("Width in cour.ttf: " + bf.getWidth('¨'));
			bf.setCharAdvance('¨', 0);
			font = new Font(bf, 12);
			document
					.add(new Paragraph("Tomten ¨ar far till alla barnen", font));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
