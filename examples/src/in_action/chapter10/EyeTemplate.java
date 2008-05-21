/* in_action/chapter10/EyeTemplate.java */

package in_action.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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

public class EyeTemplate {

	/**
	 * Generates a PDF file with the iText logo.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example EyeTemplate");
		System.out.println("-> Creates a PDF file with the iText eye that are constructed");
		System.out.println("   and painted on a PdfTemplate.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: eye_template.pdf");
		// step 1: creation of a document-object
		Document.compress = false;
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter10/eye_template.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			PdfTemplate template = cb.createTemplate(150, 150);
			template.setLineWidth(12f);
			template.arc(40f - (float) Math.sqrt(12800), 110f + (float) Math
					.sqrt(12800), 200f - (float) Math.sqrt(12800), -50f
					+ (float) Math.sqrt(12800), 281.25f, 33.75f);
			template.arc(40f, 110f, 200f, -50f, 90f, 45f);
			template.stroke();
			template.setLineCap(PdfContentByte.LINE_JOIN_ROUND);
			template.arc(80f, 30f, 160f, 110f, 90f, 180f);
			template.arc(115f, 65f, 125f, 75f, 0f, 360f);
			template.stroke();
			cb.addTemplate(template, 0f, 0f);
			cb.addTemplate(template, 1f, 0f, 0f, -1f, 0f, PageSize.A4.getHeight());
			cb.addTemplate(template, 100, 400);
			cb.addTemplate(template, 0, -2, 2, 0, 100, 400);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
