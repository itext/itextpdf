/* in_action/chapter08/Type3Characters.java */

package in_action.chapter08;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.Type3Font;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Type3Characters {

	/**
	 * Generates a PDF file with a user defined Type3 Font.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 8: example Type3Characters");
		System.out.println("-> Creates a PDF file with Type3 font.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: type3.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter08/type3.pdf"));
			// step 3: we open the document
			document.open();
			// step 4
			Type3Font t3 = new Type3Font(writer, new char[] { ' ', '1', '2',
					'3', '4', '5' }, false);
			PdfContentByte g;
			g = t3.defineGlyph(' ', 300, 0, 0, 600, 1200);
			g = t3.defineGlyph('1', 600, 0, 0, 600, 1200);
			g.moveTo(250, 1200);
			g.lineTo(350, 100);
			g.lineTo(400, 1100);
			g.closePathFillStroke();
			g = t3.defineGlyph('2', 800, 0, 0, 800, 1200);
			g.moveTo(250, 1200);
			g.lineTo(350, 100);
			g.lineTo(400, 1100);
			g.closePathFillStroke();
			g.moveTo(450, 1200);
			g.lineTo(550, 100);
			g.lineTo(600, 1100);
			g.closePathFillStroke();
			g = t3.defineGlyph('3', 1000, 0, 0, 500, 600);
			g.moveTo(250, 1200);
			g.lineTo(350, 100);
			g.lineTo(400, 1100);
			g.closePathFillStroke();
			g.moveTo(450, 1200);
			g.lineTo(550, 100);
			g.lineTo(600, 1100);
			g.closePathFillStroke();
			g.moveTo(650, 1200);
			g.lineTo(750, 100);
			g.lineTo(800, 1100);
			g.closePathFillStroke();
			g = t3.defineGlyph('4', 1200, 0, 0, 600, 600);
			g.moveTo(250, 1200);
			g.lineTo(350, 100);
			g.lineTo(400, 1100);
			g.closePathFillStroke();
			g.moveTo(450, 1200);
			g.lineTo(550, 100);
			g.lineTo(600, 1100);
			g.closePathFillStroke();
			g.moveTo(650, 1200);
			g.lineTo(750, 100);
			g.lineTo(800, 1100);
			g.closePathFillStroke();
			g.moveTo(850, 1200);
			g.lineTo(950, 100);
			g.lineTo(1000, 1100);
			g.closePathFillStroke();
			g.closePathFillStroke();
			g = t3.defineGlyph('5', 1200, 0, 0, 1200, 1200);
			g.moveTo(250, 1200);
			g.lineTo(350, 100);
			g.lineTo(400, 1100);
			g.closePathFillStroke();
			g.moveTo(450, 1200);
			g.lineTo(550, 100);
			g.lineTo(600, 1100);
			g.closePathFillStroke();
			g.moveTo(650, 1200);
			g.lineTo(750, 100);
			g.lineTo(800, 1100);
			g.closePathFillStroke();
			g.moveTo(850, 1200);
			g.lineTo(950, 100);
			g.lineTo(1000, 1100);
			g.closePathFillStroke();
			g.moveTo(1200, 600);
			g.lineTo(0, 650);
			g.lineTo(100, 550);
			g.closePathFillStroke();
			Font font = new Font(t3, 24);
			document.add(new Paragraph("1 2 3 4 5\n5 5 5 5 5 5 5 1", font));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
