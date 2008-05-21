/* in_action/chapter08/FontMetrics.java */

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

public class FontMetrics {

	/**
	 * Generates a PDF file with the standard Type 1 Fonts.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example FontMetrics");
		System.out.println("-> Creates a PDF file with text in font Helvetica.");
		System.out.println("   Different metrics are measured.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: font_metrics.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter08/font_metrics.pdf"));

			// step 3: we open the document
			document.open();
			// step 4:
			Font font = new Font(Font.HELVETICA, 12);
			BaseFont bf = font.getCalculatedBaseFont(false);

			String numbers = "0123456789";
			String letters = "abcdefghijklmnopqrstuvwxyz";
			document.add(new Paragraph(numbers, font));
			document.add(new Paragraph("width: " + bf.getWidth(numbers) + " ("
					+ bf.getWidthPoint(numbers, 12) + "pt)", font));
			document.add(new Paragraph("ascent: "
					+ bf.getAscent(numbers)
					+ "; descent: "
					+ bf.getDescent(numbers)
					+ "; height: "
					+ (bf.getAscentPoint(numbers, 12)
							- bf.getDescentPoint(numbers, 12) + "pt"), font));
			document.add(new Paragraph(letters, font));
			document.add(new Paragraph("width: " + bf.getWidth(letters) + " ("
					+ bf.getWidthPoint(letters, 12) + "pt)", font));
			document.add(new Paragraph("ascent: "
					+ bf.getAscent(letters)
					+ "; descent: "
					+ bf.getDescent(letters)
					+ "; height: "
					+ (bf.getAscentPoint(letters, 12) - bf.getDescentPoint(
							letters, 12)) + "pt", font));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}