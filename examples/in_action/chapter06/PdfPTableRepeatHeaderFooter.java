/* in_action/chapter06/PdfPTableRepeatHeaderFooter.java */

package in_action.chapter06;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class PdfPTableRepeatHeaderFooter {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example PdfPTableRepeatHeaderFooter");
		System.out.println("-> Creates a PDF file with a large PdfPTable.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: pdfptable_repeated.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4.rotate());
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/pdfptable_repeated2.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfPTable datatable = new PdfPTable(10);
			int headerwidths[] = { 10, 24, 12, 12, 7, 7, 7, 7, 7, 7 };
			datatable.setWidths(headerwidths);
			datatable.setWidthPercentage(100);
			datatable.getDefaultCell().setPadding(5);

			// The header starts with a cell that spans 10 columns
			PdfPCell cell = new PdfPCell(new Phrase(
					"Administration - System Users Report", FontFactory
							.getFont(FontFactory.HELVETICA, 24, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorderWidth(2);
			cell.setColspan(10);
			cell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
			cell.setUseDescender(true);
			datatable.addCell(cell);
			// We need 4 cells with rowspan 2
			datatable.getDefaultCell().setBorderWidth(2);
			datatable.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_CENTER);
			datatable.addCell("User Id");
			datatable.addCell("Name\nAddress");
			datatable.addCell("Company");
			datatable.addCell("Department");
			// we use a nested table to fake this
			PdfPTable permissions = new PdfPTable(6);
			permissions.getDefaultCell().setBorderWidth(2);
			permissions.getDefaultCell().setHorizontalAlignment(
					Element.ALIGN_CENTER);
			permissions.getDefaultCell().setColspan(6);
			permissions.addCell("Permissions");
			permissions.getDefaultCell().setColspan(1);
			permissions.addCell("Admin");
			permissions.addCell("Data");
			permissions.addCell("Expl");
			permissions.addCell("Prod");
			permissions.addCell("Proj");
			permissions.addCell("Online");
			PdfPCell permission = new PdfPCell(permissions);
			permission.setColspan(6);
			datatable.addCell(permission);
			// footer
			PdfPCell empty = new PdfPCell();
			empty.setColspan(4);
			datatable.addCell(empty);
			datatable.addCell("Admin");
			datatable.addCell("Data");
			datatable.addCell("Expl");
			datatable.addCell("Prod");
			datatable.addCell("Proj");
			datatable.addCell("Online");
			// this is the end of the table header
			// as far as PdfPTable is concerned there are 2 rows in the header
			datatable.setHeaderRows(3);
			datatable.setFooterRows(1);

			// we add the data to the table
			datatable.getDefaultCell().setBorderWidth(1);
			for (int i = 1; i < 30; i++) {
				datatable.getDefaultCell().setHorizontalAlignment(
						Element.ALIGN_LEFT);
				datatable.addCell("myUserId");
				datatable
						.addCell("Somebody with a very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very, very long long name");
				datatable.addCell("No Name Company");
				datatable.addCell("D" + i);
				datatable.getDefaultCell().setHorizontalAlignment(
						Element.ALIGN_CENTER);
				for (int j = 0; j < 6; j++)
					datatable.addCell(Math.random() > .5 ? "Yes" : "No");
			}
			datatable.setSplitLate(false);
			document.add(datatable);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}