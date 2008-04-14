/* in_action/chapter11/TemplateClip.java */

package in_action.chapter11;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class TemplateClip {

	/**
	 * Generates a PDF file showing how to clip an image.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example TemplateClip");
		System.out.println("-> Creates a PDF file with paths that are constructed");
		System.out.println("   and painted.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource needed: foxdog.jpg (chapter 5)");
		System.out.println("-> file generated: template_clip.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/template_clip.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			Image img = Image
					.getInstance("resources/in_action/chapter05/foxdog.jpg");
			float w = img.getScaledWidth();
			float h = img.getScaledHeight();
			float gap = 15;
			PdfContentByte cb = writer.getDirectContent();
			PdfTemplate t1 = cb.createTemplate(w / 2, h / 2);
			t1.addImage(img, w, 0, 0, h, 0, -h / 2);
			cb.addTemplate(t1, 36, PageSize.A4.getHeight() - 36 - h / 2);
			PdfTemplate t2 = cb.createTemplate(w / 2, h / 2);
			t2.addImage(img, w, 0, 0, h, -w / 2, -h / 2);
			cb.addTemplate(t2, 36 + w / 2 + gap, PageSize.A4.getHeight() - 36 - h
					/ 2);
			PdfTemplate t3 = cb.createTemplate(w / 2, h / 2);
			t3.addImage(img, w, 0, 0, h, 0, 0);
			cb.addTemplate(t3, 36, PageSize.A4.getHeight() - 36 - h - gap);
			PdfTemplate t4 = cb.createTemplate(w / 2, h / 2);
			t4.addImage(img, w, 0, 0, h, -w / 2, 0);
			cb.addTemplate(t4, 36 + w / 2 + gap, PageSize.A4.getHeight() - 36 - h
					- gap);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}