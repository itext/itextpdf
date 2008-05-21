/* in_action/chapter10/PdfPTableFloatingBoxesjava */

package in_action.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class PdfPTableFloatingBoxes implements PdfPCellEvent, PdfPTableEvent {

	/**
	 * @see com.lowagie.text.pdf.PdfPTableEvent#tableLayout(com.lowagie.text.pdf.PdfPTable,
	 *      float[][], float[], int, int, com.lowagie.text.pdf.PdfContentByte[])
	 */
	public void tableLayout(PdfPTable table, float[][] width, float[] height,
			int headerRows, int rowStart, PdfContentByte[] canvas) {
		float widths[] = width[0];
		float x1 = widths[0];
		float x2 = widths[widths.length - 1];
		float y1 = height[0];
		float y2 = height[height.length - 1];
		PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
		cb.setRGBColorStroke(0x00, 0x00, 0xFF);
		cb.rectangle(x1, y1, x2 - x1, y2 - y1);
		cb.stroke();
		cb.resetRGBColorStroke();
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell,
	 *      com.lowagie.text.Rectangle, com.lowagie.text.pdf.PdfContentByte[])
	 */
	public void cellLayout(PdfPCell cell, Rectangle position,
			PdfContentByte[] canvases) {
		float x1 = position.getLeft() + 2;
		float x2 = position.getRight() - 2;
		float y1 = position.getTop() - 2;
		float y2 = position.getBottom() + 2;
		PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
		canvas.setRGBColorStroke(0xFF, 0x00, 0x00);
		canvas.rectangle(x1, y1, x2 - x1, y2 - y1);
		canvas.stroke();
		canvas.resetRGBColorStroke();
	}

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: example PdfPTableFloatingBoxes");
		System.out.println("-> Creates a PDF file with a PdfPTable with");
		System.out.println("   table and cell events.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: pdfptable_floating_boxes.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter10/pdfptable_floating_boxes.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfPTable table = new PdfPTable(2);
			PdfPTableFloatingBoxes event = new PdfPTableFloatingBoxes();
			table.setTableEvent(event);
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			table.getDefaultCell().setCellEvent(event);
			table.getDefaultCell().setPadding(5f);
			table.addCell("value");
			table.addCell("name");
			table.addCell(new Paragraph("fox"));
			table.addCell(new Paragraph("wolve"));
			table.addCell(new Paragraph("dog"));
			table.addCell(new Paragraph("cat"));
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