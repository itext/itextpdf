/* in_action/chapter11/Transparency1.java */

package in_action.chapter11;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfTransparencyGroup;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Transparency1 {

	/**
	 * Generates a PDF file demonstrating the use of transparency.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example Transparency1");
		System.out.println("-> Creates a PDF file demonstrating transparency.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: transparency1.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/transparency1.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			float gap = (document.getPageSize().getWidth() - 400) / 3;

			pictureBackdrop(gap, 500, cb);
			pictureBackdrop(200 + 2 * gap, 500, cb);
			pictureBackdrop(gap, 500 - 200 - gap, cb);
			pictureBackdrop(200 + 2 * gap, 500 - 200 - gap, cb);

			pictureCircles(gap, 500, cb);
			cb.saveState();
			PdfGState gs1 = new PdfGState();
			gs1.setFillOpacity(0.5f);
			cb.setGState(gs1);
			pictureCircles(200 + 2 * gap, 500, cb);
			cb.restoreState();

			PdfTemplate tp = cb.createTemplate(200, 200);
			cb.saveState();
			pictureCircles(0, 0, tp);
			PdfTransparencyGroup group = new PdfTransparencyGroup();
			tp.setGroup(group);
			cb.setGState(gs1);
			cb.addTemplate(tp, gap, 500 - 200 - gap);
			cb.restoreState();

			tp = cb.createTemplate(200, 200);
			cb.saveState();
			PdfGState gs2 = new PdfGState();
			gs2.setFillOpacity(0.5f);
			gs2.setBlendMode(PdfGState.BM_SOFTLIGHT);
			tp.setGState(gs2);
			pictureCircles(0, 0, tp);
			tp.setGroup(group);
			cb.addTemplate(tp, 200 + 2 * gap, 500 - 200 - gap);
			cb.restoreState();

			cb.resetRGBColorFill();
			ColumnText ct = new ColumnText(cb);
			Phrase ph = new Phrase("Ungrouped objects\nObject opacity = 1.0");
			ct.setSimpleColumn(ph, gap, 0, gap + 200, 500, 18,
					Element.ALIGN_CENTER);
			ct.go();

			ph = new Phrase("Ungrouped objects\nObject opacity = 0.5");
			ct.setSimpleColumn(ph, 200 + 2 * gap, 0, 200 + 2 * gap + 200, 500,
					18, Element.ALIGN_CENTER);
			ct.go();

			ph = new Phrase(
					"Transparency group\nObject opacity = 1.0\nGroup opacity = 0.5\nBlend mode = Normal");
			ct.setSimpleColumn(ph, gap, 0, gap + 200, 500 - 200 - gap, 18,
					Element.ALIGN_CENTER);
			ct.go();

			ph = new Phrase(
					"Transparency group\nObject opacity = 0.5\nGroup opacity = 1.0\nBlend mode = SoftLight");
			ct.setSimpleColumn(ph, 200 + 2 * gap, 0, 200 + 2 * gap + 200,
					500 - 200 - gap, 18, Element.ALIGN_CENTER);
			ct.go();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	/**
	 * Prints a square and fills half of it with a gray rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param cb
	 * @throws Exception
	 */
	public static void pictureBackdrop(float x, float y, PdfContentByte cb) {
		cb.setColorStroke(Color.black);
		cb.setColorFill(Color.gray);
		cb.rectangle(x, y, 100, 200);
		cb.fill();
		cb.setLineWidth(2);
		cb.rectangle(x, y, 200, 200);
		cb.stroke();
	}

	/**
	 * Prints 3 circles in different colors that intersect with eachother.
	 * 
	 * @param x
	 * @param y
	 * @param cb
	 * @throws Exception
	 */
	public static void pictureCircles(float x, float y, PdfContentByte cb) {
		cb.setColorFill(Color.red);
		cb.circle(x + 70, y + 70, 50);
		cb.fill();
		cb.setColorFill(Color.yellow);
		cb.circle(x + 100, y + 130, 50);
		cb.fill();
		cb.setColorFill(Color.blue);
		cb.circle(x + 130, y + 70, 50);
		cb.fill();
	}
}