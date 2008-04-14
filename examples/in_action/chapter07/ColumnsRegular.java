/* in_action/chapter07/ColumnsRegular.java */

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

public class ColumnsRegular {

	/**
	 * Generates a PDF file with several phrases.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 7: example ColumnsRegular");
		System.out.println("-> Creates a PDF file with blocks of text.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource: caesar.txt");
		System.out.println("-> file generated: columns_regular.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter07/columns_regular.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfContentByte cb = writer.getDirectContent();
			StringBuffer sb = new StringBuffer(1024);
			BufferedReader reader = new BufferedReader(new FileReader(
					"resources/in_action/chapter07/caesar.txt"));
			int c;
			while ((c = reader.read()) > -1) {
				sb.append((char) c);
			}
			reader.close();
			ColumnText ct = new ColumnText(cb);
			ct.setAlignment(Element.ALIGN_JUSTIFIED);
			ct.setText(new Phrase(sb.toString()));
			float[] left = { 36, (PageSize.A4.getWidth() / 2) + 18 };
			float[] right = { (PageSize.A4.getWidth() / 2) - 18,
					PageSize.A4.getWidth() - 36 };
			int status = ColumnText.START_COLUMN;
			int column = 0;
			while (ColumnText.hasMoreText(status)) {
				ct.setSimpleColumn(left[column], 36, right[column], PageSize.A4.getHeight() - 36);
				status = ct.go();
				column++;
				if (column > 1) {
					column = 0;
					document.newPage();
				}
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