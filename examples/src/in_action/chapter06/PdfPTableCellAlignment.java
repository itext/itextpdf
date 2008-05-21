/* in_action/chapter06/PdfPTableCellAlignment.java */

package in_action.chapter06;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
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

public class PdfPTableCellAlignment {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example PdfPTableCellAlignment");
		System.out.println("-> Creates a PDF file with a PdfPTable.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: pdfptable_cell_alignment.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/pdfptable_cell_alignment.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document

			PdfPTable table = new PdfPTable(2);
			PdfPCell cell;
			Paragraph p = new Paragraph(
					"Quick brown fox jumps over the lazy dog. Quick brown fox jumps over the lazy dog.");
			table.addCell("default alignment");
			cell = new PdfPCell(p);
			table.addCell(cell);
			table.addCell("centered alignment");
			cell = new PdfPCell(p);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			table.addCell("right alignment");
			cell = new PdfPCell(p);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			table.addCell("justified alignment");
			cell = new PdfPCell(p);
			cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table.addCell(cell);
			table.addCell("paragraph alignment");
			Paragraph p1 = new Paragraph("Quick brown fox");
			Paragraph p2 = new Paragraph("jumps over");
			p2.setAlignment(Element.ALIGN_CENTER);
			Paragraph p3 = new Paragraph("the lazy dog.");
			p3.setAlignment(Element.ALIGN_RIGHT);
			cell = new PdfPCell();
			cell.addElement(p1);
			cell.addElement(p2);
			cell.addElement(p3);
			table.addCell(cell);
			table.addCell("extra indentation (cell)");
			cell = new PdfPCell(p);
			cell.setIndent(20);
			table.addCell(cell);
			table.addCell("extra indentation (paragraph)");
			p.setFirstLineIndent(10);
			cell = new PdfPCell();
			cell.addElement(p);
			table.addCell(cell);
			table
					.addCell("blah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\n");
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			table.addCell("bottom");
			table
					.addCell("blah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\n");
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell("middle");
			table
					.addCell("blah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\n");
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			table.addCell("top");
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