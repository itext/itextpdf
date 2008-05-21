package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class TablesInColumns {
	public static void main(String[] args) {
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapterX/tables_in_columns.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfPTable outertable = new PdfPTable(1);
			outertable.addCell("This is the header of table A");
			outertable.addCell("This is the footer of table A");
			outertable.setHeaderRows(2);
			outertable.setFooterRows(1);
			outertable.setSplitLate(false);
			outertable.addCell("Table A");
			PdfPTable innertable = new PdfPTable(3);
			PdfPCell cell = new PdfPCell(new Phrase("Table B: header"));
			cell.setColspan(3);
			innertable.addCell(cell);
			cell = new PdfPCell(new Phrase("Table B: footer"));
			cell.setColspan(3);
			innertable.addCell(cell);
			innertable.setHeaderRows(2);
			innertable.setFooterRows(1);
			for (int i = 0; i < 400; i++) {
				innertable.addCell("cell " + i + ".0");
				innertable.addCell("cell " + i + ".1");
				innertable.addCell("cell " + i + ".3");
			}
			outertable.addCell(innertable);
			ColumnText ct = new ColumnText(writer.getDirectContent());
			ct.addElement(outertable);
			ct.setSimpleColumn(36, 36, PageSize.A4.getWidth() - 36, PageSize.A4
					.getHeight() - 36, 18, Element.ALIGN_JUSTIFIED);
			int status = ColumnText.NO_MORE_COLUMN;
			while ((status & ColumnText.NO_MORE_TEXT) == 0) {
				status = ct.go();
				ct.setYLine(PageSize.A4.getHeight() - 36);
				document.newPage();
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
