/* in_action/chapter08/CompactFontFormatExample.java */

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

public class CompactFontFormatExample {

	/**
	 * Generates a PDF file with a compact font format font.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example CompactFontFormatExample");
		System.out.println("-> Creates a PDF file with a compact font format font.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: esl_gothic_shavian.otf");
		System.out.println("-> file generated: cff.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter08/cff.pdf"));

			// step 3: we open the document
			document.open();
			// step 4:
			BaseFont bf = BaseFont.createFont(
					"resources/in_action/chapter08/esl_gothic_shavian.otf", "Cp1252",
					BaseFont.EMBEDDED);
			System.out.println(bf.getClass().getName());
			Font font = new Font(bf, 12);
			document
					.add(new Paragraph(
							"All human beings are born free and equal in dignity and rights. "
									+ "They are endowed with reason and conscience and should act towards one another in a spirit of brotherhood."));
			document
					.add(new Paragraph(
							"Yl hVman bIiNz R bPn frI n ikwal in dignitI n rFts. "
									+ "Hej R endQd wiH rIzn n konSans n Sud Akt tawPds wan anaHr in a spirit ov braHarhUd.",
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
