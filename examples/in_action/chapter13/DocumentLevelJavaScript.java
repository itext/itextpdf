/* in_action/chapter13/DocumentLevelJavaScript.java */

package in_action.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class DocumentLevelJavaScript {

	/**
	 * Generates a file with document level JavaScript.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example Document Level JavaScript");
		System.out.println("-> Creates a PDF with a JavaScript method.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   document_level_javascript.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/document_level_javascript.pdf"));
			// step 3:
			document.open();
			// step 4: we grab the ContentByte and do some stuff with it
			writer.addJavaScript(
					"function saySomething(s) {app.alert('JS says: ' + s)}",
					false);
			writer
					.setAdditionalAction(
							PdfWriter.DOCUMENT_CLOSE,
							PdfAction
									.javaScript(
											"saySomething('Thank you for reading this document.');\r",
											writer));
			document.add(new Paragraph(
					"PDF document with a JavaScript function."));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}