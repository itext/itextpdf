/* in_action/chapter13/NamedActions.java */

package in_action.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class NamedActions {

	/**
	 * Generates a file with named actions.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example Named Actions");
		System.out.println("-> Creates a PDF file with named actions.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   named_actions.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/named_actions.pdf"));
			// step 3:
			document.open();
			// step 4: we grab the ContentByte and do some stuff with it
			PdfPTable table = new PdfPTable(4);
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(new Phrase(new Chunk("First Page")
					.setAction(new PdfAction(PdfAction.FIRSTPAGE))));
			table.addCell(new Phrase(new Chunk("Prev Page")
					.setAction(new PdfAction(PdfAction.PREVPAGE))));
			table.addCell(new Phrase(new Chunk("Next Page")
					.setAction(new PdfAction(PdfAction.NEXTPAGE))));
			table.addCell(new Phrase(new Chunk("Last Page")
					.setAction(new PdfAction(PdfAction.LASTPAGE))));
			Paragraph p = new Paragraph(new Chunk("Click to print")
					.setAction(new PdfAction(PdfAction.PRINTDIALOG)));
			for (int k = 1; k <= 10; ++k) {
				document.add(new Paragraph("This is page " + k));
				document.add(Chunk.NEWLINE);
				document.add(table);
				document.add(p);
				document.newPage();
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}