/* in_action/chapter09/FontSelectionExample.java */

package in_action.chapter09;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FontSelectionExample {

	/**
	 * Generates a PDF file with the standard Type 1 Fonts.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example FontSelectionExample");
		System.out.println("-> Creates a PDF file with mixed fonts.");
		System.out.println("-> jars needed: iText.jar and iTextAsian.jar");
		System.out.println("-> file generated: font_selection.pdf");

		// step 1
		Document document = new Document(PageSize.A4);
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/font_selection.pdf"));

			// step 3: we open the document
			document.open();
			// step 4:
			String text = "These are the protagonists in 'Hero', a movie by Zhang Yimou:\n"
					+ "\u7121\u540d (Nameless), \u6b98\u528d (Broken Sword), "
					+ "\u98db\u96ea (Flying Snow), \u5982\u6708 (Moon), "
					+ "\u79e6\u738b (the King), and \u9577\u7a7a (Sky).";
			FontSelector selector = new FontSelector();
			selector.addFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 12));
			selector.addFont(FontFactory.getFont("MSung-Light",
					"UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED));
			Phrase ph = selector.process(text);
			document.add(new Paragraph(ph));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

}
