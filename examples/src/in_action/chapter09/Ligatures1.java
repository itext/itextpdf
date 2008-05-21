/* in_action/chapter09/Ligatures1.java */

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

public class Ligatures1 {

	/**
	 * Generates a PDF file with a ligaturize method.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example Ligatures1");
		System.out.println("-> Creates a PDF file with a ligaturize method.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: arial.ttf");
		System.out.println("-> file generated: ligatures1.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/ligatures1.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			BaseFont bf;
			Font font;
			bf = BaseFont.createFont("c:/windows/fonts/arial.ttf",
					BaseFont.CP1252, BaseFont.EMBEDDED);
			font = new Font(bf, 12);
			document.add(new Paragraph(
					"Movie title: Love at First Hiccough (Denmark)", font));
			document
					.add(new Paragraph("directed by Tomas Villum Jensen", font));
			document.add(new Paragraph("K\u00e6rlighed ved f\u00f8rste hik", font));
			document.add(new Paragraph(
					ligaturize("Kaerlighed ved f/orste hik"), font));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	private static String ligaturize(String s) {
		int pos;
		while ((pos = s.indexOf("ae")) > -1) {
			s = s.substring(0, pos) + '\u00e6' + s.substring(pos + 2);
		}
		while ((pos = s.indexOf("/o")) > -1) {
			s = s.substring(0, pos) + '\u00f8' + s.substring(pos + 2);
		}
		return s;
	}
}
