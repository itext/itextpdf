/* in_action/chapter06/PdfPTableCellEvents.java */

package in_action.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class PdfPTableCellEvents {

	class RoundRectangle implements PdfPCellEvent {
		/**
		 * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell,
		 *      com.lowagie.text.Rectangle,
		 *      com.lowagie.text.pdf.PdfContentByte[])
		 */
		public void cellLayout(PdfPCell cell, Rectangle rect,
				PdfContentByte[] canvas) {
			PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
			cb.setColorStroke(new GrayColor(0.8f));
			cb.roundRectangle(rect.getLeft() + 4, rect.getBottom(), rect.getWidth() - 8,
					rect.getHeight() - 4, 4);
			cb.stroke();
		}
	}

	class Ellipse implements PdfPCellEvent {
		/**
		 * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell,
		 *      com.lowagie.text.Rectangle,
		 *      com.lowagie.text.pdf.PdfContentByte[])
		 */
		public void cellLayout(PdfPCell cell, Rectangle rect,
				PdfContentByte[] canvas) {
			PdfContentByte cb = canvas[PdfPTable.BACKGROUNDCANVAS];
			cb.setRGBColorFill(0xFF, 0x00, 0x00);
			cb.ellipse(rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop());
			cb.fill();
			cb.resetRGBColorFill();
		}
	}

	class Strike implements PdfPCellEvent {
		/**
		 * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell,
		 *      com.lowagie.text.Rectangle,
		 *      com.lowagie.text.pdf.PdfContentByte[])
		 */
		public void cellLayout(PdfPCell cell, Rectangle rect,
				PdfContentByte[] canvas) {
			PdfContentByte cb = canvas[PdfPTable.TEXTCANVAS];
			cb.setRGBColorStroke(0x00, 0x00, 0xFF);
			cb.moveTo(rect.getLeft(), rect.getBottom());
			cb.lineTo(rect.getRight(), rect.getTop());
			cb.stroke();
			cb.resetRGBColorStroke();
		}
	}

	/**
	 * Generates a PDF file with a table and special cells (using cell events).
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example PdfPTableCellEvents");
		System.out.println("-> Creates a PDF file with a PdfPTable with cell events.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: pdfptable_cell_events.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter10/pdfptable_cell_events.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfPTableCellEvents example = new PdfPTableCellEvents();
			RoundRectangle border = example.new RoundRectangle();
			Ellipse ellipse = example.new Ellipse();
			Strike strike = example.new Strike();

			PdfPTable table = new PdfPTable(6);
			PdfPCell cell;
			for (int i = 1; i <= 30; i++) {
				cell = new PdfPCell(new Phrase("day " + i));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setPadding(4);
				cell.setCellEvent(border);
				if (i % 3 == 0)
					cell.setCellEvent(strike);
				if (i % 4 == 0)
					cell.setCellEvent(ellipse);
				table.addCell(cell);
			}
			document.add(table);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}