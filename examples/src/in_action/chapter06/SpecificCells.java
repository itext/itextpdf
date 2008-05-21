/* in_action/chapter06/SpecificCells.java */

package in_action.chapter06;

import java.awt.Point;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SpecificCells {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example SpecificCells");
		System.out.println("-> Creates a PDF with a Table.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: specific_cells.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/specific_cells.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			Table table = new Table(2, 2);
			table.setAlignment(Element.ALIGN_LEFT);
			table.setAutoFillEmptyCells(true);
			table.addCell("0.0");
			table.addCell("0.1");
			table.addCell("1.0");
			table.addCell("1.1");
			table.addColumns(2);
			float[] f = { 2f, 1f, 1f, 1f };
			table.setWidths(f);
			table.addCell("2.2", new Point(2, 2));
			table.addCell("3.3", new Point(3, 3));
			table.addCell("2.1", new Point(2, 1));
			table.addCell("1.3", new Point(1, 3));
			table.addCell("5.3", new Point(5, 3));
			table.addCell("5.0", new Point(5, 0));
			table.deleteColumn(2);
			document.add(table);
			document.add(new Paragraph("converted to PdfPTable:"));
			table.setConvert2pdfptable(true);
			document.add(table);
			document.add(new Paragraph("positioned PdfPTable:"));
			PdfPTable pTable = table.createPdfPTable();
			pTable.setTotalWidth(400);
			PdfContentByte cb = writer.getDirectContent();
			pTable.writeSelectedRows(0, -1, 36, 550, cb);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}