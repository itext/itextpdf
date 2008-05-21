/* in_action/chapter02/HelloWorldMargins.java */

package in_action.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldMargins {

	/**
	 * Generates a PDF file with the text 'Hello World'. Custom values are used
	 * for the margins.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 2: example HelloWorldMargins");
		System.out.println("-> Creates a PDF file with the text 'Hello World';");
		System.out.println("   Custom values are used for the margins.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldMargins.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A5, 36, 72, 108, 180);
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter02/HelloWorldMargins.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			document.add(new Paragraph(
							"The left margin of this document is 36pt (0.5 inch); the right margin 72pt (1 inch); the top margin 108pt (1.5 inch); the bottom margin 180pt (2.5 inch)."));
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
			for (int i = 0; i < 20; i++) {
				paragraph.add("Hello World, Hello Sun, Hello Moon, Hello Stars, Hello Sea, Hello Land, Hello People. ");
			}
			document.add(paragraph);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
