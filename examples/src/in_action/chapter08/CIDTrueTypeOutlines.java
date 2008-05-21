/* in_action/chapter08/CIDTrueTypeOutlines.java */
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

public class CIDTrueTypeOutlines {

	/**
	 * Generates a PDF file with an OpenType ttf.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example CIDTrueTypeOutlines");
		System.out.println("-> Creates a PDF file with a OpenType ttf.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resources needed: esl_gothic_unicode.ttf");
		System.out.println("-> file generated: cid_ttoutlines.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter08/cid_ttoutlines.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			BaseFont bf = BaseFont.createFont(
					"resources/in_action/chapter08/esl_gothic_unicode.ttf", BaseFont.IDENTITY_H,
					BaseFont.NOT_EMBEDDED);
			Font font = new Font(bf, 12);
			System.out.println(bf.getClass().getName());
			document
					.add(new Paragraph(
							"All human beings are born free and equal in dignity and rights. "
									+ "They are endowed with reason and conscience and should act towards one another in a spirit of brotherhood.",
							font));
			document
					.add(new Paragraph(
							"\ue727\ue714 \ue713\ue72f\ue715\ue719\ue71f \ue70a\ue720\ue716\ue709\ue70f \ue728 "
									+ "\ue70a\ue729\ue71f \ue703\ue71e\ue720 \ue71f \ue716\ue702\ue712\ue71a\ue714 \ue716\ue71f "
									+ "\ue70b\ue716\ue70c\ue71f\ue716\ue701\ue720 \ue71f \ue71e\ue722\ue701\ue705. "
									+ "\ue70e\ue717\ue708 \ue728 \ue717\ue71f\ue70b\ue71c\ue70b \ue712\ue716\ue70e \ue71e\ue720\ue70f\ue71f "
									+ "\ue71f \ue702\ue71a\ue71f\ue706\ue719\ue71f\ue705 \ue71f \ue706\ue723\ue70b "
									+ "\ue718\ue702\ue701 \ue701\ue719\ue712\ue729\ue70b\ue70f \ue712\ue719\ue71f "
									+ "\ue719\ue71f\ue719\ue70e\ue71e \ue716\ue71f \ue419 \ue719 \ue705\ue700\ue716\ue71e\ue716\ue701 "
									+ "\ue71a\ue70d \ue70a\ue71e\ue719\ue70e\ue719\ue71e\ue713\ue71b\ue70b.",
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
