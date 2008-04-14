/* in_action/chapter07/ColumnsIrregular.java */

package in_action.chapter07;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ColumnsIrregular {

	/**
	 * Generates a PDF file with several phrases.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 7: example ColumnsIrregular");
		System.out.println("-> Creates a PDF file with blocks of text.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resources: caesar.txt and caesar.jpg");
		System.out.println("-> file generated: columns_irregular.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter07/columns_irregular.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfContentByte cb = writer.getDirectContent();
			Image caesar = Image.getInstance("resources/in_action/chapter07/caesar.jpg");
			cb.addImage(caesar, 100, 0, 0, 100, 260, 595);
			PdfTemplate t = cb.createTemplate(600, 800);
			t.setGrayFill(0.75f);
			t.moveTo(310, 112);
			t.lineTo(280, 60);
			t.lineTo(340, 60);
			t.closePath();
			t.moveTo(310, 790);
			t.lineTo(310, 710);
			t.moveTo(310, 580);
			t.lineTo(310, 122);
			t.fillStroke();
			cb.addTemplate(t, 0, 0);

			StringBuffer sb = new StringBuffer(1024);
			BufferedReader reader = new BufferedReader(new FileReader(
					"resources/in_action/chapter07/caesar.txt"));
			int c;
			while ((c = reader.read()) > -1) {
				sb.append((char) c);
			}
			reader.close();
			ColumnText ct = new ColumnText(cb);
			ct.setText(new Phrase(sb.toString()));
			ct.setAlignment(Element.ALIGN_JUSTIFIED);
			float[][] left = {
					{ 70, 790, 70, 60 },
					{ 320, 790, 320, 700, 380, 700, 380, 590, 320, 590, 320,
							106, 350, 60 } };
			float[][] right = {
					{ 300, 790, 300, 700, 240, 700, 240, 590, 300, 590, 300,
							106, 270, 60 }, { 550, 790, 550, 60 } };
			int status = ColumnText.START_COLUMN;
			int column = 0;
			while (ColumnText.hasMoreText(status)) {
				if (column > 1) {
					column = 0;
					document.newPage();
					cb.addTemplate(t, 0, 0);
					cb.addImage(caesar, 100, 0, 0, 100, 260, 595);
				}
				ct.setColumns(left[column], right[column]);
				ct.setYLine(790);
				status = ct.go();
				column++;
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