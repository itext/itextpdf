/* in_action/chapter14/PageBoundaries.java */

package in_action.chapter14;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class PageBoundaries extends PdfPageEventHelper {
	/** the font used in the page event. */
	BaseFont bf;

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onOpenDocument(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onOpenDocument(PdfWriter arg0, Document arg1) {
		try {
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
					BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			bf = null;
		}
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onEndPage(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		cb.saveState();
		Rectangle pageSize = writer.getPageSize();
		Rectangle trim = writer.getBoxSize("trim");
		Rectangle art = writer.getBoxSize("art");
		Rectangle bleed = writer.getBoxSize("bleed");
		cb.rectangle(trim.getLeft(), trim.getBottom(), trim.getWidth(), trim.getHeight());
		cb.rectangle(art.getLeft(), art.getBottom(), art.getWidth(), art.getHeight());
		cb.stroke();
		cb.setLineWidth(3);
		cb.moveTo(pageSize.getWidth() / 2, bleed.getBottom());
		cb.lineTo(pageSize.getWidth() / 2, 0);
		cb.moveTo(pageSize.getWidth() / 2, bleed.getTop());
		cb.lineTo(pageSize.getWidth() / 2, pageSize.getHeight());
		cb.moveTo(0, pageSize.getHeight() / 2);
		cb.lineTo(bleed.getLeft(), pageSize.getHeight() / 2);
		cb.moveTo(pageSize.getWidth(), pageSize.getHeight() / 2);
		cb.lineTo(bleed.getRight(), pageSize.getHeight() / 2);
		cb.stroke();
		cb.setLineWidth(1);
		cb.setLineDash(6, 0);
		cb.rectangle(bleed.getLeft(), bleed.getBottom(), bleed.getWidth(), bleed.getHeight());
		cb.stroke();
		cb.restoreState();
		float x = trim.getLeft() + trim.getWidth() / 2;
		float y = art.getTop() + 16;
		cb.beginText();
		cb.setFontAndSize(bf, 36);
		cb.showTextAligned(Element.ALIGN_CENTER, "Fox and Dog News", x, y, 0);
		cb.endText();
	}

	/**
	 * Generates a PDF file that explains the concept of page boundaries.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 14: Page Boundaries");
		System.out.println("-> Creates a PDF file that explains the concept of page boundaries.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   page_boundaries.pdf");
		// step 1: creation of a document-object
		Document document = new Document(new Rectangle(432, 792));
		try {
			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter14/page_boundaries.pdf"));
			writer.setViewerPreferences(PdfWriter.PageLayoutTwoPageLeft);
			writer.setPageEvent(new PageBoundaries());
			writer.setCropBoxSize(new Rectangle(5, 5, 427, 787));
			writer.setBoxSize("bleed", new Rectangle(30, 30, 402, 762));
			writer.setBoxSize("trim", new Rectangle(36, 36, 396, 756));
			writer.setBoxSize("art", new Rectangle(72, 72, 360, 684));
			// step 3:
			document.open();
			// step 4: we grab the ContentByte and do some stuff with it
			PdfContentByte cb = writer.getDirectContent();
			ColumnText ct = new ColumnText(cb);
			Paragraph p = new Paragraph();
			for (int i = 0; i < 10; i++) {
				p.add(new Phrase("Quick brown fox jumps over the lazy dog. "));
			}
			for (int i = 0; i < 10; i++) {
				ct.addElement(p);
			}
			int status = ColumnText.NO_MORE_COLUMN;
			while (ColumnText.hasMoreText(status)) {
				ct.setSimpleColumn(72, 72, 360, 684);
				status = ct.go();
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