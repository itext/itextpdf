package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class NonBreakingSpace {
	public static void main(String[] args) {
		String string = "Look at this paragraph with a lot of different products and at least one product called Kautschuk\u00a0Plant, where the plant itself can be planted, on the other hand, there are some more words with absolutely no sense.";
		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(
					document,
					new FileOutputStream("results/in_action/chapterX/nbsp.pdf"));
			// step 3
			document.open();
			// step 4
			document.add(new Paragraph(string));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5
		document.close();
	}
}
