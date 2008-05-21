/* in_action/chapter06/MyFirstTable.java */

package in_action.chapter06;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Table;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class MyFirstTable {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example MyFirstTable");
		System.out.println("-> Creates a PDF, RTF and HTML file with a Table.");
		System.out.println("-> jars needed: iText.jar");
		System.out
				.println("-> resulting PDF: my_first_table.pdf, my_first_table.rtf and my_first_table.htm");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/my_first_table.pdf"));
			RtfWriter2.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/my_first_table.rtf"));
			HtmlWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/my_first_table.htm"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			Table table = new Table(3);
			table.setBorderWidth(1);
			table.setBorderColor(new Color(0, 0, 255));
			table.setPadding(5);
			table.setSpacing(5);
			Cell cell = new Cell("header");
			cell.setHeader(true);
			cell.setColspan(3);
			table.addCell(cell);
			cell = new Cell("example cell with colspan 1 and rowspan 2");
			cell.setRowspan(2);
			cell.setBorderColor(new Color(255, 0, 0));
			table.addCell(cell);
			table.addCell("1.1");
			table.addCell("2.1");
			table.addCell("1.2");
			table.addCell("2.2");
			table.addCell("cell test1");
			cell = new Cell("big cell");
			cell.setRowspan(2);
			cell.setColspan(2);
			cell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
			table.addCell(cell);
			table.addCell("cell test2");
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