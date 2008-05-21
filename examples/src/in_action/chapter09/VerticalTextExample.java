/* in_action/chapter09/VerticalTextExample.java */

package in_action.chapter09;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.VerticalText;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class VerticalTextExample {

	/**
	 * Generates a PDF file with vertical text.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example VerticalTextExample");
		System.out.println("-> Creates a PDF file with vertical text.");
		System.out.println("-> jars needed: iText.jar, iTextAsian.jar");
		System.out.println("-> file generated: vertical_text.pdf");

		String movie = "\u4e03\u4eba\u306e\u4f8d";
		String quote_p1 = "You embarrass me. You're overestimating me. Listen, I'm not a man with any special skill, but I've had plenty of experience in battles; losing battles, all of them.";
		String quote_p2 = "In short, that's all I am. Drop such an idea for your own good.";
		// step 1
		Document document = new Document(PageSize.A4);
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter09/vertical_text.pdf"));

			// step 3: we open the document
			document.open();
			// step 4
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf;
			Font font;
			VerticalText vt;
			bf = BaseFont.createFont("KozMinPro-Regular", "UniJIS-UCS2-V",
					BaseFont.NOT_EMBEDDED);
			font = new Font(bf, 20);
			vt = new VerticalText(cb);
			vt.setVerticalLayout(PageSize.A4.getWidth() * 0.75f, PageSize.A4
					.getHeight() - 36, PageSize.A4.getHeight() - 72, 8, 30);
			vt.addText(new Chunk(movie, font));
			vt.go();
			vt.addText(new Phrase(quote_p1, font));
			vt.go();
			vt.setAlignment(Element.ALIGN_RIGHT);
			vt.addText(new Phrase(quote_p2, font));
			vt.go();
			bf = BaseFont.createFont("KozMinPro-Regular", "Identity-V",
					BaseFont.NOT_EMBEDDED);
			font = new Font(bf, 20);
			vt = new VerticalText(cb);
			vt.setVerticalLayout(PageSize.A4.getWidth() * 0.25f, PageSize.A4
					.getHeight() - 36, PageSize.A4.getHeight() - 72, 8, 30);
			vt.addText(new Phrase(convertCIDs(quote_p1), font));
			vt.go();
			vt.setAlignment(Element.ALIGN_RIGHT);
			vt.addText(new Phrase(convertCIDs(quote_p2), font));
			vt.go();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	public static String convertCIDs(String text) {
		char cid[] = text.toCharArray();
		for (int k = 0; k < cid.length; ++k) {
			char c = cid[k];
			if (c == '\n')
				cid[k] = '\uff00';
			else
				cid[k] = (char) (c - ' ' + 8720);
		}
		return new String(cid);
	}
}
