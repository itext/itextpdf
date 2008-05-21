/* in_action/chapter10/EyeLogo.java */

package in_action.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class EyeLogo {

	/**
	 * Generates a PDF file with the iText logo.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example EyeLogo");
		System.out.println("-> Creates a PDF file to which literal PDF syntax is added.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: eye_logo.pdf");
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
					new FileOutputStream("results/in_action/chapter10/eye_logo.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			String eye = "12 w\n22.47 64.67 m\n37.99 67.76 52.24 75.38 63.43 86.57 c\n120 110 m\n98.78 110 78.43 101.57 63.43 86.57 c\nS\n1 J\n120 110 m\n97.91 110 80 92.09 80 70 c\n80 47.91 97.91 30 120 30 c\n125 70 m\n125 72.76 122.76 75 120 75 c\n117.24 75 115 72.76 115 70 c\n115 67.24 117.24 65 120 65 c\n122.76 65 125 67.24 125 70 c\nS\n";
			cb.setLiteral(eye);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
