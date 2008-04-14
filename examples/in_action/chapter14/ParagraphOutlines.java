/* in_action/chapter14/ParagraphOutlines.java */

package in_action.chapter14;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ParagraphOutlines extends PdfPageEventHelper {

	/** Keeps the number of the current paragraph. */
	private int n = 0;

	/**
	 * Adds an outline for every new Paragraph
	 * 
	 * @param writer
	 * @param document
	 * @param position
	 */
	public void onParagraph(PdfWriter writer, Document document, float position) {
		n++;
		PdfContentByte cb = writer.getDirectContent();
		PdfDestination destination = new PdfDestination(PdfDestination.FITH,
				position);
		new PdfOutline(cb.getRootOutline(), destination,
				"paragraph " + n);
	}

	/**
	 * Generates a file with a header and a footer.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 14: Paragraph outlines");
		System.out.println("-> Creates a PDF file with paragraph events.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resource needed: caesar.txt (chapter 7)");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   paragraph_outlines.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A6);
		try {
			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter14/paragraph_outlines.pdf"));
			writer.setBoxSize("art", new Rectangle(document.left(), document
					.bottom(), document.right(), document.top()));
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines
					| PdfWriter.PageLayoutSinglePage);
			writer.setPageEvent(new ParagraphOutlines());
			// step 3:
			document.open();
			// step 4: we grab the ContentByte and do some stuff with it
			BufferedReader reader = new BufferedReader(new FileReader(
					"resources/in_action/chapter07/caesar.txt"));
			String line;
			Paragraph p;
			while ((line = reader.readLine()) != null) {
				p = new Paragraph(line);
				p.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(p);
			}
			reader.close();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}