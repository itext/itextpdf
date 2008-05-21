package in_action.chapterX;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
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

public class CellImageBackground {

	public static void main(String[] args) {
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapterX/cell_image_background.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			Image img = Image.getInstance("resources/in_action/chapterX/fflogo.jpg");
			
			PdfPTable table = new PdfPTable(1);
			
			Phrase p = new Phrase();
			p.add(new Chunk(img, 0, - img.getHeight() / 2));
			p.add(" some more text.");
			PdfPCell cell = new PdfPCell(p);
			cell.setFixedHeight(img.getHeight() + 6);
			cell.setPadding(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(Color.BLUE);
			table.addCell(cell);
			
			PdfPTable innertable = new PdfPTable(3);
			cell = new PdfPCell(Image.getInstance("resources/in_action/chapterX/file.gif"), true);
			cell.setBackgroundColor(Color.YELLOW);
			cell.setBorder(PdfPCell.NO_BORDER);
			innertable.addCell(cell);
			cell = new PdfPCell(Image.getInstance("resources/in_action/chapterX/dir.gif"), false);
			cell.setBackgroundColor(Color.ORANGE);
			cell.setBorder(PdfPCell.NO_BORDER);
			innertable.addCell(cell);
			cell = new PdfPCell(new Phrase("This is a test"));
			cell.setBackgroundColor(Color.RED);
			cell.setBorder(PdfPCell.NO_BORDER);
			innertable.addCell(cell);
			
			cell = new PdfPCell(innertable);
			cell.setBackgroundColor(Color.GREEN);
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
