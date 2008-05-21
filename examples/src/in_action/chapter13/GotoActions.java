/* in_action/chapter13/GotoActions.java */

package in_action.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleNamedDestination;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class GotoActions {

	/**
	 * Generates a file with goto actions.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example Goto Actions");
		System.out.println("-> Creates a PDF file with goto actions.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   goto.pdf, remote.pdf and remote.xml");
		// step 1: creation of a document-object
		Document document = new Document();
		Document remote = new Document();
		try {
			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/goto.pdf"));
			PdfWriter.getInstance(remote, new FileOutputStream("results/in_action/chapter13/remote.pdf"));
			// step 3:
			document.open();
			remote.open();
			// step 4: we add some content
			PdfAction action = PdfAction.gotoLocalPage(2, new PdfDestination(
					PdfDestination.XYZ, -1, 10000, 0), writer);
			writer.setOpenAction(action);
			document.add(new Paragraph("Page 1"));
			document.newPage();
			document.add(new Paragraph("Page 2"));
			document.add(new Chunk("go to page 1").setAction(PdfAction
					.gotoLocalPage(1, new PdfDestination(PdfDestination.FITH,
							500), writer)));
			document.add(Chunk.NEWLINE);
			document.add(new Chunk("go to another document")
					.setAction(PdfAction.gotoRemotePage("remote.pdf", "test",
							false, true)));
			remote.add(new Paragraph("Some remote document"));
			remote.newPage();
			Paragraph p = new Paragraph("This paragraph contains a ");
			p.add(new Chunk("local destination").setLocalDestination("test"));
			remote.add(p);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		remote.close();

		try {
			PdfReader reader = new PdfReader("results/in_action/chapter13/remote.pdf");
			HashMap map = SimpleNamedDestination.getNamedDestination(reader,
					false);
			SimpleNamedDestination.exportToXML(map, new FileOutputStream(
					"results/in_action/chapter13/remote.xml"), "ISO8859-1", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}