/* in_action/chapter11/TextOperators.java */

package in_action.chapter11;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfTextArray;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class TextOperators {

	/**
	 * Generates a PDF file showing the different text operators.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example TextOperators");
		System.out.println("-> Creates a PDF file demonstrating PDF's Text State.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: text_operators.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/text_operators.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			String text = "AWAY again";
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			cb.beginText();
			cb.moveText(36, 806);
			cb.setFontAndSize(bf, 24);
			cb.moveTextWithLeading(0, -36);
			cb.showText(text);
			cb.newlineText();
			PdfTextArray array = new PdfTextArray("A");
			array.add(120);
			array.add("W");
			array.add(120);
			array.add("A");
			array.add(95);
			array.add("Y again");
			cb.showText(array);
			cb.setWordSpacing(50);
			cb.newlineShowText(text);
			cb.setCharacterSpacing(20);
			cb.newlineShowText(text);
			cb.setWordSpacing(0);
			cb.setCharacterSpacing(0);
			cb.setLeading(56);
			cb.newlineShowText("Changing the leading: " + text);
			cb.setLeading(36);
			cb.setHorizontalScaling(50);
			cb.newlineShowText(text);
			cb.setHorizontalScaling(100);
			cb.newlineShowText(text);
			cb.setTextRise(15);
			cb.setFontAndSize(bf, 12);
			cb.setColorFill(Color.red);
			cb.showText("2");
			cb.endText();

			PdfTemplate tp1 = cb.createTemplate(160, 36);
			tp1.beginText();
			tp1.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
			tp1.setFontAndSize(bf, 24);
			tp1.moveText(6, -6);
			tp1.showText(text);
			tp1.endText();
			cb.addTemplate(tp1, 36, 240);

			PdfTemplate tp2 = cb.createTemplate(200, 36);
			tp2.beginText();
			tp2.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_STROKE);
			tp2.setFontAndSize(bf, 24);
			tp2.moveText(6, -6);
			tp2.showText(text);
			tp2.endText();
			cb.addTemplate(tp2, 36, 200);

			PdfTemplate tp3 = cb.createTemplate(200, 36);
			tp3.beginText();
			tp3
					.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
			tp3.setFontAndSize(bf, 24);
			tp3.moveText(6, -6);
			tp3.showText(text);
			tp3.endText();
			cb.addTemplate(tp3, 36, 160);

			PdfTemplate tp4 = cb.createTemplate(200, 36);
			tp4.beginText();
			tp4.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);
			tp4.setFontAndSize(bf, 24);
			tp4.moveText(6, -6);
			tp4.showText(text);
			tp4.endText();
			cb.addTemplate(tp4, 36, 120);

			PdfTemplate tp5 = cb.createTemplate(200, 36);
			tp5.beginText();
			tp5.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_CLIP);
			tp5.setFontAndSize(bf, 24);
			tp5.moveText(6, -6);
			tp5.showText(text);
			tp5.endText();
			tp5.setLineWidth(2);
			for (int i = 0; i < 6; i++) {
				tp5.moveTo(0, i * 6 + 3);
				tp5.lineTo(200, i * 6 + 3);
			}
			tp5.stroke();
			cb.addTemplate(tp5, 210, 240);

			PdfTemplate tp6 = cb.createTemplate(200, 36);
			tp6.beginText();
			tp6
					.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_STROKE_CLIP);
			tp6.setFontAndSize(bf, 24);
			tp6.moveText(6, -6);
			tp6.showText(text);
			tp6.endText();
			tp6.setLineWidth(2);
			for (int i = 0; i < 6; i++) {
				tp6.moveTo(0, i * 6 + 3);
				tp6.lineTo(200, i * 6 + 3);
			}
			tp6.stroke();
			cb.addTemplate(tp6, 210, 200);

			PdfTemplate tp7 = cb.createTemplate(200, 36);
			tp7.beginText();
			tp7
					.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE_CLIP);
			tp7.setFontAndSize(bf, 24);
			tp7.moveText(6, -6);
			tp7.showText(text);
			tp7.endText();
			tp7.setLineWidth(2);
			for (int i = 0; i < 6; i++) {
				tp7.moveTo(0, i * 6 + 3);
				tp7.lineTo(200, i * 6 + 3);
			}
			tp7.stroke();
			cb.addTemplate(tp7, 210, 160);

			PdfTemplate tp8 = cb.createTemplate(200, 36);
			tp8.beginText();
			tp8.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_CLIP);
			tp8.setFontAndSize(bf, 24);
			tp8.moveText(6, -6);
			tp8.showText(text);
			tp8.endText();
			tp8.setLineWidth(2);
			for (int i = 0; i < 6; i++) {
				tp8.moveTo(0, i * 6 + 3);
				tp8.lineTo(200, i * 6 + 3);
			}
			tp8.stroke();
			cb.addTemplate(tp8, 210, 120);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}