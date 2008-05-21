/* in_action/chapter09/Ligatures2.java */

package in_action.chapter09;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Ligatures2 {

	/**
	 * Generates a PDF file with a TrueType Fonts.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example Ligatures2");
		System.out.println("-> Creates a PDF file with arabic text.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: arialuni.ttf");
		System.out.println("-> file generated: ligatures2.pdf");

		// step 1
		String movieTitle = "\u0644\u0648\u0631\u0627\u0646\u0633 \u0627\u0644\u0639\u0631\u0628";
		String movieTitleWithExtraSpaces = "\u0644 \u0648 \u0631 \u0627 \u0646 \u0633   \u0627 \u0644 \u0639 \u0631 \u0628";
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/ligatures2.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			BaseFont bf;
			Font font;
			bf = BaseFont.createFont("c:/windows/fonts/arialuni.ttf",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			font = new Font(bf, 20);
			document.add(new Paragraph("Movie title: Lawrence of Arabia (UK)"));
			document.add(new Paragraph("directed by David Lean"));
			document.add(new Paragraph("Wrong: " + movieTitle, font));
			MultiColumnText mct = new MultiColumnText();
			mct.addSimpleColumn(36, PageSize.A4.getWidth() - 36);
			mct.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			mct.addElement(new Paragraph("Wrong: " + movieTitleWithExtraSpaces,
					font));
			document.add(mct);
			mct = new MultiColumnText();
			mct.addSimpleColumn(36, PageSize.A4.getWidth() - 36);
			mct.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			mct.addElement(new Paragraph(movieTitle, font));
			document.add(mct);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
