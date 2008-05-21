package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class TooltipExample2 extends PdfPageEventHelper {
	public static void main(String[] args) {
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
					// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapterX/tooltip2.pdf"));
			// step 3: we open the document
			document.open();
			writer.setPageEvent(new TooltipExample2());
			// step 4: we add a paragraph to the document
			Paragraph p = new Paragraph("Hello World ");
			Chunk c = new Chunk("tooltip");
			c.setGenericTag("This is my tooltip.");
			p.add(c);
			document.add(p);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onGenericTag(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document, com.lowagie.text.Rectangle, java.lang.String)
	 */
	public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
		PdfAnnotation annotation =
			PdfAnnotation.createText(writer, rect, "tooltip", text, false, null);
		PdfAppearance ap = writer.getDirectContent().createAppearance(rect.getWidth(), rect.getHeight());
		annotation.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, ap);
		float[] red = { 1, 0, 0 };
		annotation.put(PdfName.C, new PdfArray(red));
		writer.addAnnotation(annotation);
	}
}
