/* in_action/chapter10/LineCharacteristics.java */

package in_action.chapter10;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class LineCharacteristics {

	/**
	 * Generates a PDF file showing the different line characteristics.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: LineCharacteristics");
		System.out.println("-> Creates a PDF file with linear paths that are constructed");
		System.out.println("   and painted in a different Text State.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: line_characteristics.pdf");
		// step 1: creation of a document-object
		Document.compress = false;
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter10/line_characteristics.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			cb.saveState();
			for (int i = 25; i > 0; i--) {
				cb.setLineWidth((float) i / 10);
				cb.moveTo(40, 806 - (5 * i));
				cb.lineTo(320, 806 - (5 * i));
				cb.stroke();
			}
			cb.restoreState();
			cb.moveTo(72, 650);
			cb.lineTo(72, 600);
			cb.moveTo(144, 650);
			cb.lineTo(144, 600);
			cb.stroke();
			cb.saveState();
			cb.setLineWidth(8);
			cb.setLineCap(PdfContentByte.LINE_CAP_BUTT);
			cb.moveTo(72, 640);
			cb.lineTo(144, 640);
			cb.stroke();
			cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
			cb.moveTo(72, 625);
			cb.lineTo(144, 625);
			cb.stroke();
			cb.setLineCap(PdfContentByte.LINE_CAP_PROJECTING_SQUARE);
			cb.moveTo(72, 610);
			cb.lineTo(144, 610);
			cb.stroke();
			cb.restoreState();
			cb.saveState();
			cb.setLineWidth(8);
			cb.setLineJoin(PdfContentByte.LINE_JOIN_MITER);
			cb.moveTo(200, 610);
			cb.lineTo(215, 640);
			cb.lineTo(230, 610);
			cb.stroke();
			cb.setLineJoin(PdfContentByte.LINE_JOIN_ROUND);
			cb.moveTo(240, 610);
			cb.lineTo(255, 640);
			cb.lineTo(270, 610);
			cb.stroke();
			cb.setLineJoin(PdfContentByte.LINE_JOIN_BEVEL);
			cb.moveTo(280, 610);
			cb.lineTo(295, 640);
			cb.lineTo(310, 610);
			cb.stroke();
			cb.restoreState();

			cb.saveState();
			cb.setLineWidth(8);
			cb.setLineJoin(PdfContentByte.LINE_JOIN_MITER);
			cb.setMiterLimit(2);
			cb.moveTo(75, 560);
			cb.lineTo(95, 590);
			cb.lineTo(115, 560);
			cb.stroke();
			cb.moveTo(116, 560);
			cb.lineTo(135, 590);
			cb.lineTo(154, 560);
			cb.stroke();
			cb.moveTo(157, 560);
			cb.lineTo(175, 590);
			cb.lineTo(193, 560);
			cb.stroke();
			cb.moveTo(198, 560);
			cb.lineTo(215, 590);
			cb.lineTo(232, 560);
			cb.stroke();
			cb.moveTo(239, 560);
			cb.lineTo(255, 590);
			cb.lineTo(271, 560);
			cb.stroke();
			cb.moveTo(280, 560);
			cb.lineTo(295, 590);
			cb.lineTo(310, 560);
			cb.stroke();
			cb.restoreState();
			cb.saveState();
			cb.setLineWidth(8);
			cb.setLineJoin(PdfContentByte.LINE_JOIN_MITER);
			cb.setMiterLimit(2.1f);
			cb.moveTo(75, 500);
			cb.lineTo(95, 530);
			cb.lineTo(115, 500);
			cb.stroke();
			cb.moveTo(116, 500);
			cb.lineTo(135, 530);
			cb.lineTo(154, 500);
			cb.stroke();
			cb.moveTo(157, 500);
			cb.lineTo(175, 530);
			cb.lineTo(193, 500);
			cb.stroke();
			cb.moveTo(198, 500);
			cb.lineTo(215, 530);
			cb.lineTo(232, 500);
			cb.stroke();
			cb.moveTo(239, 500);
			cb.lineTo(255, 530);
			cb.lineTo(271, 500);
			cb.stroke();
			cb.moveTo(280, 500);
			cb.lineTo(295, 530);
			cb.lineTo(310, 500);
			cb.stroke();
			cb.restoreState();

			cb.saveState();
			cb.setLineWidth(3);
			cb.moveTo(40, 480);
			cb.lineTo(320, 480);
			cb.stroke();
			cb.setLineDash(6, 0);
			cb.moveTo(40, 470);
			cb.lineTo(320, 470);
			cb.stroke();
			cb.setLineDash(6, 3);
			cb.moveTo(40, 460);
			cb.lineTo(320, 460);
			cb.stroke();
			cb.setLineDash(15, 10, 5);
			cb.moveTo(40, 450);
			cb.lineTo(320, 450);
			cb.stroke();
			float[] dash1 = { 10, 5, 5, 5, 20 };
			cb.setLineDash(dash1, 5);
			cb.moveTo(40, 440);
			cb.lineTo(320, 440);
			cb.stroke();
			float[] dash2 = { 9, 6, 0, 6 };
			cb.setLineCap(PdfContentByte.LINE_CAP_ROUND);
			cb.setLineDash(dash2, 0);
			cb.moveTo(40, 430);
			cb.lineTo(320, 430);
			cb.stroke();
			cb.restoreState();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
