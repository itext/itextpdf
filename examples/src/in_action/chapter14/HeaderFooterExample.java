/* in_action/chapter14/HeaderFooterExample.java */

package in_action.chapter14;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HeaderFooterExample extends PdfPageEventHelper {

	/** The Phrase that will be added as the header of the document. */
	protected Phrase header;

	/** The PdfPTable that will be added as the footer of the document. */
	protected PdfPTable footer;

	/**
	 * Constructs an Event that adds a Header and a Footer.
	 */
	public HeaderFooterExample() {
		header = new Phrase("This is the header of the document.");
		footer = new PdfPTable(4);
		footer.setTotalWidth(300);
		footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		footer.addCell(new Phrase(new Chunk("First Page")
				.setAction(new PdfAction(PdfAction.FIRSTPAGE))));
		footer.addCell(new Phrase(new Chunk("Prev Page")
				.setAction(new PdfAction(PdfAction.PREVPAGE))));
		footer.addCell(new Phrase(new Chunk("Next Page")
				.setAction(new PdfAction(PdfAction.NEXTPAGE))));
		footer.addCell(new Phrase(new Chunk("Last Page")
				.setAction(new PdfAction(PdfAction.LASTPAGE))));
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onEndPage(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		if (document.getPageNumber() > 1) {
			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header,
					(document.right() - document.left()) / 2
							+ document.leftMargin(), document.top() + 10, 0);
		}
		footer.writeSelectedRows(0, -1,
				(document.right() - document.left() - 300) / 2
						+ document.leftMargin(), document.bottom() - 10, cb);
	}

	/**
	 * Generates a file with a header and a footer.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 14: Header Footer Example");
		System.out.println("-> Creates a PDF file with a header and a footer.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   header_footer.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter14/header_footer.pdf"));
			writer.setViewerPreferences(PdfWriter.PageLayoutTwoColumnLeft);
			writer.setPageEvent(new HeaderFooterExample());
			document.setMargins(36, 36, 54, 72);
			// step 3:
			document.open();
			// step 4: we grab the ContentByte and do some stuff with it
			for (int k = 1; k <= 300; ++k) {
				document.add(new Phrase(
						"Quick brown fox jumps over the lazy dog. "));
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}