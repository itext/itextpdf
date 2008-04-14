/* in_action/chapter13/LaunchAction.java */

package in_action.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
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

public class LaunchAction {

	/**
	 * Generates a file with a launch action.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example Launch Action");
		System.out.println("-> Creates a PDF file with a launch action.");
		System.out.println("-> jars needed: iText.jar");
		System.out
				.println("-> resources needed: C:/windows/notepad.exe and test.txt");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   launch_action.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/launch_action.pdf"));
			// step 3:
			document.open();
			// step 4: we grab the ContentByte and do some stuff with it
			Paragraph p = new Paragraph(new Chunk(
					"Click to open test.txt in Notepad.")
					.setAction(new PdfAction("c:/windows/notepad.exe",
							"test.txt", "open", "resources/in_action/chapter13/")));
			document.add(p);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}