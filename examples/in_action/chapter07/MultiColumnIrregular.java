/* in_action/chapter07/MultiColumnIrregular.java */

package in_action.chapter07;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class MultiColumnIrregular {

	/**
	 * Generates a PDF file with several phrases.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 7: example MultiColumnIrregular");
		System.out.println("-> Creates a PDF file with irregular columns.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource: caesar.txt");
		System.out.println("-> file generated: multicolumn_irregular.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter07/multicolumn_irregular.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			BufferedReader reader = new BufferedReader(new FileReader(
					"resources/in_action/chapter07/caesar.txt"));

			MultiColumnText mct = new MultiColumnText(document.top()
					- document.bottom());
			mct.setAlignment(Element.ALIGN_JUSTIFIED);
			float diamondHeight = 400;
			float diamondWidth = 400;
			float gutter = 10;
			float bodyHeight = document.top() - document.bottom();
			float colMaxWidth = (document.right() - document.left() - (gutter * 2)) / 2f;
			float diamondTop = document.top()
					- ((bodyHeight - diamondHeight) / 2f);
			float diamondInset = colMaxWidth - (diamondWidth / 2f);
			float centerX = (document.right() - document.left()) / 2
					+ document.left();
			// setup column 1
			float[] left = { document.left(), document.top(), document.left(),
					document.bottom() };
			float[] right = { document.left() + colMaxWidth, document.top(),
					document.left() + colMaxWidth, diamondTop,
					document.left() + diamondInset,
					diamondTop - diamondHeight / 2,
					document.left() + colMaxWidth, diamondTop - diamondHeight,
					document.left() + colMaxWidth, document.bottom() };
			mct.addColumn(left, right);
			// setup column 2
			left = new float[] { document.right() - colMaxWidth,
					document.top(), document.right() - colMaxWidth, diamondTop,
					document.right() - diamondInset,
					diamondTop - diamondHeight / 2,
					document.right() - colMaxWidth, diamondTop - diamondHeight,
					document.right() - colMaxWidth, document.bottom() };
			right = new float[] { document.right(), document.top(),
					document.right(), document.bottom() };
			mct.addColumn(left, right);

			String line;
			while ((line = reader.readLine()) != null) {
				mct.addElement(new Phrase(line + "\n"));
			}
			reader.close();

			PdfContentByte cb = writer.getDirectContent();
			do {
				cb.saveState();
				cb.setLineWidth(5);
				cb.setColorStroke(Color.GRAY);
				cb.moveTo(centerX, document.top());
				cb.lineTo(centerX, document.bottom());
				cb.stroke();
				cb.moveTo(centerX, diamondTop);
				cb.lineTo(centerX - (diamondWidth / 2), diamondTop
						- (diamondHeight / 2));
				cb.lineTo(centerX, diamondTop - diamondHeight);
				cb.lineTo(centerX + (diamondWidth / 2), diamondTop
						- (diamondHeight / 2));
				cb.lineTo(centerX, diamondTop);
				cb.setColorFill(Color.GRAY);
				cb.fill();
				cb.restoreState();
				document.add(mct);
				mct.nextColumn();
			} while (mct.isOverflow());
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}