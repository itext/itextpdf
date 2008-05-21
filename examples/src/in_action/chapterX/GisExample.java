package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class GisExample implements PdfPCellEvent {

	public static void main(String[] args) {
		Document document = new Document(PageSize.A4.rotate());
		try {
			PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapterX/gis.pdf"));
			document.open();
			
			Image large = Image.getInstance("resources/in_action/chapterX/foobar.png");
			Image small = Image.getInstance("resources/in_action/chapterX/foobar_thumb.png");
			
			float[] widths = { large.getWidth(), small.getWidth() * 2 };
			PdfPTable table = new PdfPTable(widths);
			PdfPCell cell = new PdfPCell(large, true);
			cell.setBorderWidth(3);
			cell.setBorderColor(new GrayColor(0.7f));
			cell.setUseBorderPadding(true);
			table.addCell(cell);
			
			PdfPTable innertable = new PdfPTable(1);
			cell = new PdfPCell(small, true);
			cell.setBorder(PdfPCell.BOTTOM);
			cell.setBorderWidth(3);
			cell.setBorderColor(new GrayColor(0.7f));
			cell.setUseBorderPadding(true);
			innertable.addCell(cell);
			
			PdfPTable legendtable = new PdfPTable(1);
			legendtable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			legendtable.addCell("Legend");
			legendtable.addCell("test1");
			legendtable.addCell("test2");
			legendtable.addCell("test3");
			legendtable.addCell("test4");
			legendtable.addCell("test5");
			legendtable.addCell("test6");
			legendtable.addCell("test7");
			cell = new PdfPCell(legendtable);
			cell.setBorder(PdfPCell.BOTTOM);
			cell.setBorderWidth(3);
			cell.setBorderColor(new GrayColor(0.7f));
			cell.setUseBorderPadding(true);
			innertable.addCell(cell);
			
			cell = new PdfPCell();
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setBackgroundColor(new GrayColor(0.7f));
			innertable.addCell(cell);
			
			cell = new PdfPCell(innertable);
			cell.setBorderWidth(3);
			cell.setBorderColor(new GrayColor(0.7f));
			cell.setUseBorderPadding(true);
			cell.setCellEvent(new GisExample());
			table.addCell(cell);
			document.add(table);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.close();
	}

	public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] cb) {
		PdfPTable titleTable = new PdfPTable(2);
		titleTable.getDefaultCell().setBackgroundColor(GrayColor.WHITE);
		titleTable.setTotalWidth(rect.getWidth());
		titleTable.setLockedWidth(true);
		cell = new PdfPCell(new Phrase("title"));
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setBackgroundColor(GrayColor.WHITE);
		titleTable.addCell(cell);
		titleTable.addCell("test1");
		titleTable.addCell("test2");
		cell = new PdfPCell(titleTable);
		cell.setPadding(2);
		titleTable.calculateHeightsFast();
		float height = titleTable.getTotalHeight();
		titleTable.writeSelectedRows(0, -1, rect.getLeft(), rect.getBottom() + height, cb[PdfPTable.TEXTCANVAS]);
	}
}
