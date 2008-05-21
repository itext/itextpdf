/* in_action/chapter16/UnsignedSignatureField.java */

package in_action.chapter16;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfAcroForm;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class UnsignedSignatureField {

	/**
	 * Creates a signed PDF file.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 16: example Unsigned Signature Field");
		System.out.println("-> Creates a PDF with an unsigned signature field;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   unsigned_signature_field.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter16/unsigned_signature_field.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			document
					.add(new Paragraph("This is a personal message from Laura."));
			PdfAcroForm acroForm = writer.getAcroForm();
			acroForm.addSignature("foobarsig", 73, 705, 149, 759);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}