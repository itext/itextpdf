/* in_action/chapter02/HelloWorldAbsolute.java */

package in_action.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
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

public class HelloWorldAbsolute {

	/**
	 * Generates a PDF file with the text 'Hello World'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 2: example HelloWorldAbsolute");
		System.out.println("-> Creates a PDF file with the text 'Hello World';");
		System.out.println("   the text is added at an absolute position and");
		System.out.println("   the stream with the content of a page is not compressed.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldAbsolute.pdf");
		// step 1: creation of a document-object
		Document.compress = false;
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter02/HelloWorldAbsolute.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			cb.saveState(); // q
			cb.beginText(); // BT
			cb.moveText(36, 806); // 36 806 Td
			cb.moveText(0, -18); // 0 -18 Td
			cb.setFontAndSize(bf, 12); // /F1 12 Tf
			cb.showText("Hello World"); // (Hello World)Tj
			cb.endText(); // ET
			cb.restoreState(); // Q
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}