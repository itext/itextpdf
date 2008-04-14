/* in_action/chapter06/PdfPTableColors.java */

package in_action.chapter06;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
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

public class PdfPTableColors {

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 6: example PdfPTableColors");
		System.out.println("-> Creates a PDF file with a PdfPTable.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: pdfptable_colors.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4.rotate());
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter06/pdfptable_colors.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);
			PdfPCell cell;
			cell = new PdfPCell(new Paragraph("test colors:"));
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("red / no borders"));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setBackgroundColor(Color.red);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("green / magenta bottom border"));
			cell.setBorder(Rectangle.BOTTOM);
			cell.setBorderColorBottom(Color.magenta);
			cell.setBorderWidthBottom(10f);
			cell.setBackgroundColor(Color.green);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph(
					"blue / cyan top border + padding"));
			cell.setBorder(Rectangle.TOP);
			cell.setUseBorderPadding(true);
			cell.setBorderWidthTop(5f);
			cell.setBorderColorTop(Color.cyan);
			cell.setBackgroundColor(Color.blue);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("test GrayFill:"));
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("0.25"));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setGrayFill(0.25f);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("0.5"));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setGrayFill(0.5f);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("0.75"));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setGrayFill(0.75f);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("test bordercolors:"));
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("different borders"));
			cell.setBorderWidthLeft(6f);
			cell.setBorderWidthBottom(5f);
			cell.setBorderWidthRight(4f);
			cell.setBorderWidthTop(2f);
			cell.setBorderColorLeft(Color.red);
			cell.setBorderColorBottom(Color.orange);
			cell.setBorderColorRight(Color.yellow);
			cell.setBorderColorTop(Color.green);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("with correct padding"));
			cell.setUseBorderPadding(true);
			cell.setBorderWidthLeft(6f);
			cell.setBorderWidthBottom(5f);
			cell.setBorderWidthRight(4f);
			cell.setBorderWidthTop(2f);
			cell.setBorderColorLeft(Color.red);
			cell.setBorderColorBottom(Color.orange);
			cell.setBorderColorRight(Color.yellow);
			cell.setBorderColorTop(Color.green);
			table.addCell(cell);
			cell = new PdfPCell(new Paragraph("orange border"));
			cell.setBorderWidth(6f);
			cell.setBorderColor(Color.orange);
			table.addCell(cell);
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