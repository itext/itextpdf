/* in_action/chapter09/RightToLeftExample.java */

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

public class RightToLeftExample {

	/**
	 * Generates a PDF file with text written from right to left.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example RightToLeftExample");
		System.out.println("-> Creates a PDF file with text written from right to left.");
		System.out.println("-> resources needed: arial.ttf");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: right_to_left.pdf");

		// step 1
		Document document = new Document(PageSize.A4);
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/right_to_left.pdf"));

			// step 3: we open the document
			document.open();
			// step 4:
			BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf",
					BaseFont.IDENTITY_H, true);
			Font font = new Font(bf, 14);
			document.add(new Paragraph("Movie title: Nina's Tragedies"));
			document.add(new Paragraph("directed by Savi Gabizon"));
			MultiColumnText mct = new MultiColumnText();
			mct.addSimpleColumn(36, PageSize.A4.getWidth() - 36);
			mct.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			mct
					.addElement(new Paragraph(
							"\u05d4\u05d0\u05e1\u05d5\u05e0\u05d5\u05ea \u05e9\u05dc \u05e0\u05d9\u05e0\u05d4",
							font));
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
