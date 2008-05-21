/* in_action/chapter11/Transparency2.java */

package in_action.chapter11;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfShading;
import com.lowagie.text.pdf.PdfShadingPattern;
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

public class Transparency2 {

	/**
	 * Generates a PDF file demonstrating the use of transparency.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example Transparency2");
		System.out.println("-> Creates a PDF file demonstrating transparency.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: transparency2.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/transparency2.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			float gap = (document.getPageSize().getWidth() - 400) / 3;

			pictureBackdrop(gap, 500, cb, writer);
			pictureBackdrop(200 + 2 * gap, 500, cb, writer);
			pictureBackdrop(gap, 500 - 200 - gap, cb, writer);
			pictureBackdrop(200 + 2 * gap, 500 - 200 - gap, cb, writer);
			PdfTemplate tp;
			PdfTransparencyGroup group;

			tp = cb.createTemplate(200, 200);
			pictureCircles(0, 0, tp);
			group = new PdfTransparencyGroup();
			group.setIsolated(true);
			group.setKnockout(true);
			tp.setGroup(group);
			cb.addTemplate(tp, gap, 500);

			tp = cb.createTemplate(200, 200);
			pictureCircles(0, 0, tp);
			group = new PdfTransparencyGroup();
			group.setIsolated(true);
			group.setKnockout(false);
			tp.setGroup(group);
			cb.addTemplate(tp, 200 + 2 * gap, 500);

			tp = cb.createTemplate(200, 200);
			pictureCircles(0, 0, tp);
			group = new PdfTransparencyGroup();
			group.setIsolated(false);
			group.setKnockout(true);
			tp.setGroup(group);
			cb.addTemplate(tp, gap, 500 - 200 - gap);

			tp = cb.createTemplate(200, 200);
			pictureCircles(0, 0, tp);
			group = new PdfTransparencyGroup();
			group.setIsolated(false);
			group.setKnockout(false);
			tp.setGroup(group);
			cb.addTemplate(tp, 200 + 2 * gap, 500 - 200 - gap);
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
	public static void pictureBackdrop(float x, float y, PdfContentByte cb,
			PdfWriter writer) {
		PdfShading axial = PdfShading.simpleAxial(writer, x, y, x + 200, y,
				Color.yellow, Color.red);
		PdfShadingPattern axialPattern = new PdfShadingPattern(axial);
		cb.setShadingFill(axialPattern);
		cb.setColorStroke(Color.black);
		cb.setLineWidth(2);
		cb.rectangle(x, y, 200, 200);
		cb.fillStroke();
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
		PdfGState gs = new PdfGState();
		gs.setBlendMode(PdfGState.BM_MULTIPLY);
		gs.setFillOpacity(1f);
		cb.setGState(gs);
		cb.setColorFill(new CMYKColor(0f, 0f, 0f, 0.15f));
		cb.circle(x + 75, y + 75, 70);
		cb.fill();
		cb.circle(x + 75, y + 125, 70);
		cb.fill();
		cb.circle(x + 125, y + 75, 70);
		cb.fill();
		cb.circle(x + 125, y + 125, 70);
		cb.fill();
	}
}