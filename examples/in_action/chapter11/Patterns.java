/* in_action/chapter11/Patterns.java */

package in_action.chapter11;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PatternColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPatternPainter;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Patterns {

	/**
	 * Generates a PDF file demonstrating the use of patterns.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example Patterns");
		System.out.println("-> Creates a PDF file demonstrating tiling patterns.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource needed: iTextLogo.gif (chapter 10)");
		System.out.println("-> file generated: patterns.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/patterns.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();

			PdfPatternPainter square = cb.createPattern(15, 15);
			square.setColorFill(new Color(0xFF, 0xFF, 0x00));
			square.setColorStroke(new Color(0xFF, 0x00, 0x00));
			square.rectangle(5, 5, 5, 5);
			square.fillStroke();
			PdfPatternPainter ellipse = cb.createPattern(15, 10, 20, 25);
			ellipse.setColorFill(new Color(0xFF, 0xFF, 0x00));
			ellipse.setColorStroke(new Color(0xFF, 0x00, 0x00));
			ellipse.ellipse(2f, 2f, 13f, 8f);
			ellipse.fillStroke();
			PdfPatternPainter circle = cb.createPattern(15, 15, 10, 20,
					Color.blue);
			circle.circle(7.5f, 7.5f, 2.5f);
			circle.fill();
			PdfPatternPainter line = cb.createPattern(5, 10, null);
			line.setLineWidth(1);
			line.moveTo(3, -1);
			line.lineTo(3, 11);
			line.stroke();
			Image img = Image
					.getInstance("resources/in_action/chapter10/iTextLogo.gif");
			PdfPatternPainter img_pattern = cb.createPattern(img.getScaledWidth(),
					img.getScaledHeight(), img.getScaledWidth(), img.getScaledHeight());
			img_pattern.addImage(img, img.getScaledWidth(), 0f, 0f, img
					.getScaledHeight(), 0f, 0f);
			img_pattern.setPatternMatrix(1f, 0f, 0f, 1f, 60f, 60f);

			PatternColor squares = new PatternColor(square);
			PatternColor ellipses = new PatternColor(ellipse);
			PatternColor circles = new PatternColor(circle);
			PatternColor lines = new PatternColor(line);

			cb.setColorFill(squares);
			cb.rectangle(36, 716, 72, 72);
			cb.fillStroke();
			cb.setColorFill(ellipses);
			cb.rectangle(144, 716, 72, 72);
			cb.fillStroke();
			cb.setColorFill(circles);
			cb.rectangle(252, 716, 72, 72);
			cb.fillStroke();
			cb.setColorFill(lines);
			cb.rectangle(360, 716, 72, 72);
			cb.fillStroke();
			cb.setPatternFill(circle, Color.red);
			cb.rectangle(470, 716, 72, 72);
			cb.fillStroke();

			cb.setPatternFill(line, Color.red);
			cb.rectangle(36, 608, 72, 72);
			cb.fillStroke();
			cb.setPatternFill(line, Color.green);
			cb.rectangle(144, 608, 72, 72);
			cb.fillStroke();
			cb.setPatternFill(line, Color.blue);
			cb.rectangle(252, 608, 72, 72);
			cb.fillStroke();
			cb.setPatternFill(line, Color.yellow);
			cb.rectangle(360, 608, 72, 72);
			cb.fillStroke();
			cb.setPatternFill(line, Color.black);
			cb.rectangle(470, 608, 72, 72);
			cb.fillStroke();

			cb.setPatternFill(img_pattern);
			cb.ellipse(36, 520, 360, 590);
			cb.fillStroke();

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}