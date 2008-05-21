/* in_action/chapter09/Monospace.java */

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

public class Monospace {

	/**
	 * Generates a PDF file with monospace fonts.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example Monospace");
		System.out.println("-> Creates a PDF file with monospace fonts.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: arial.ttf and cour.ttf");
		System.out.println("-> file generated: monospace.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/monospace.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			BaseFont bf1, bf2, bf3;
			Font font1, font2, font3;
			bf1 = BaseFont.createFont("c:/windows/fonts/arial.ttf",
					BaseFont.CP1252, BaseFont.EMBEDDED);
			font1 = new Font(bf1, 12);
			document.add(new Paragraph(
					"Movie title: The Memory of a Killer (Belgium)", font1));
			document.add(new Paragraph("directed by Erik Van Looy", font1));
			document.add(new Paragraph("De Zaak Alzheimer", font1));
			bf2 = BaseFont.createFont("c:/windows/fonts/cour.ttf",
					BaseFont.CP1252, BaseFont.EMBEDDED);
			font2 = new Font(bf2, 12);
			document.add(new Paragraph("De Zaak Alzheimer", font2));
			bf3 = BaseFont.createFont("c:/windows/fonts/arialbd.ttf",
					BaseFont.CP1252, BaseFont.EMBEDDED);
			font3 = new Font(bf3, 12);
			int widths[] = bf3.getWidths();
			for (int k = 0; k < widths.length; ++k) {
				if (widths[k] != 0)
					widths[k] = 600;
			}
			bf3.setForceWidthsOutput(true);
			document.add(new Paragraph("De Zaak Alzheimer", font3));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
