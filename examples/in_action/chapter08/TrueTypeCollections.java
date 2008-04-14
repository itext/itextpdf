/* in_action/chapter08/TrueTypeCollections.java */

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

public class TrueTypeCollections {

	/**
	 * Generates a PDF file with a TrueType Font from a TrueType Collection.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example TrueTypeCollections");
		System.out.println("-> Creates a PDF file with a TTC font.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: c:/windows/fonts/msgothic.ttc");
		System.out.println("-> file generated: ttc.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter08/ttc.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			BaseFont bf;
			Font font;
			bf = BaseFont.createFont("c:/windows/fonts/msgothic.ttc,0",
					BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			font = new Font(bf, 12);
			System.out.println(bf.getClass().getName());
			document.add(new Paragraph("Rash\u00f4mon", font));
			document.add(new Paragraph("Directed by Akira Kurosawa", font));
			document.add(new Paragraph("\u7f85\u751f\u9580", font));
			String[] names = BaseFont
					.enumerateTTCNames("c:/windows/fonts/msgothic.ttc");
			for (int i = 0; i < names.length; i++) {
				document
						.add(new Paragraph("font " + i + ": " + names[i], font));
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
