/* in_action/chapterF/HelloWorldPdfX.java */

package in_action.chapterF;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldPdfX {

	/**
	 * Generates a PDF file with the text 'Hello World' that is PDF/X-1a 2002
	 * conformant.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Appendix F: example HelloWorldPdfX");
		System.out.println("-> Creates a PDF file with the text 'Hello World';");
		System.out.println("   the PDF is conformant with PDF/X-1a 2002.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resource needed: c:/windows/fonts/arial.ttf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapterF/HelloWorldPdfX.pdf"));
			writer.setPDFXConformance(PdfWriter.PDFX1A2001);
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Font font = FontFactory.getFont("c:/windows/fonts/arial.ttf",
					BaseFont.CP1252, BaseFont.EMBEDDED, Font.UNDEFINED,
					Font.UNDEFINED, new CMYKColor(255, 255, 0, 0));
			document.add(new Paragraph("Hello World", font));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}