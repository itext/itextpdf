/* in_action/chapter07/ColumnControl.java */

package in_action.chapter07;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ColumnControl {

	/**
	 * Generates a PDF file with several phrases.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 7: example ColumnControl");
		System.out.println("-> Creates a PDF file with a block of Text.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource: caesar.txt");
		System.out.println("-> file generated: column_control.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter07/column_control.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfContentByte cb = writer.getDirectContent();
			BufferedReader reader = new BufferedReader(new FileReader(
					"resources/in_action/chapter07/caesar.txt"));
			ColumnText ct = new ColumnText(cb);
			float pos;
			String line;
			Phrase p;
			int status = ColumnText.START_COLUMN;
			ct.setSimpleColumn(36, 36, PageSize.A4.getWidth() - 36, PageSize.A4.getHeight() - 36, 18, Element.ALIGN_JUSTIFIED);
			while ((line = reader.readLine()) != null) {
				p = new Phrase(line);
				ct.addText(p);
				pos = ct.getYLine();
				status = ct.go(true);
				System.out.println("Lines written:" + ct.getLinesWritten()
						+ " Y-position: " + pos + " - " + ct.getYLine());
				if (!ColumnText.hasMoreText(status)) {
					ct.addText(p);
					ct.setYLine(pos);
					ct.go(false);
				} else {
					document.newPage();
					ct.setText(p);
					ct.setYLine(PageSize.A4.getHeight() - 36);
					ct.go();
				}
			}
			reader.close();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}