/* in_action/chapter11/TextMethods.java */

package in_action.chapter11;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class TextMethods {

	/**
	 * Generates a PDF file showing the different canvases in iText.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example TextMethods");
		System.out.println("-> Creates a PDF file with text at absolute positions.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: text_methods.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/text_methods.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			cb.setLineWidth(0f);
			cb.moveTo(250, 450);
			cb.lineTo(250, 750);
			cb.moveTo(50, 650);
			cb.lineTo(400, 650);
			cb.moveTo(50, 600);
			cb.lineTo(400, 600);
			cb.moveTo(50, 550);
			cb.lineTo(400, 550);
			cb.stroke();
			String text = "AWAY again ";
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);

			cb.beginText();
			cb.setFontAndSize(bf, 12);

			cb.setTextMatrix(50, 700);
			cb.showText(text);

			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text + " Center",
					250, 650, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, text + " Right",
					250, 600, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text + " Left", 250,
					550, 0);
			cb.showTextAlignedKerned(PdfContentByte.ALIGN_LEFT, text + " Left",
					250, 532, 0);

			cb.setTextMatrix(0, 1, -1, 0, 100, 200);
			cb.showText("Text at position 100,200, rotated 90 degrees.");

			for (int i = 0; i < 360; i += 30) {
				cb
						.showTextAligned(PdfContentByte.ALIGN_LEFT, text, 200,
								300, i);
			}
			cb.endText();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}