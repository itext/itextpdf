/* in_action/chapter11/ShadingPatterns.java */

package in_action.chapter11;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfShading;
import com.lowagie.text.pdf.PdfShadingPattern;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.ShadingColor;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ShadingPatterns {

	/**
	 * Generates a PDF file demonstrating the use of shading patterns.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example ShadingPatterns");
		System.out.println("-> Creates a PDF file demonstrating shading");
		System.out.println("   and shading patterns.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: shading_patterns.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/shading_patterns.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			PdfShading axial = PdfShading.simpleAxial(writer, 36, 716, 396,
					788, Color.orange, Color.blue);
			cb.paintShading(axial);
			PdfShading radial = PdfShading.simpleRadial(writer, 200, 500, 50,
					300, 500, 100, new Color(255, 247, 148), new Color(247,
							138, 107), false, false);
			cb.paintShading(radial);
			document.newPage();

			PdfShadingPattern axialPattern = new PdfShadingPattern(axial);
			cb.setShadingFill(axialPattern);
			cb.rectangle(36, 716, 72, 72);
			cb.rectangle(144, 716, 72, 72);
			cb.rectangle(252, 716, 72, 72);
			cb.rectangle(360, 716, 72, 72);
			cb.fillStroke();

			ShadingColor axialColor = new ShadingColor(axialPattern);
			cb.setColorFill(axialColor);
			cb.rectangle(36, 608, 72, 72);
			cb.rectangle(144, 608, 72, 72);
			cb.rectangle(252, 608, 72, 72);
			cb.rectangle(360, 608, 72, 72);
			cb.fillStroke();
			PdfShadingPattern radialPattern = new PdfShadingPattern(radial);
			ShadingColor radialColor = new ShadingColor(radialPattern);
			cb.setColorFill(radialColor);
			cb.rectangle(36, 500, 72, 72);
			cb.rectangle(144, 500, 72, 72);
			cb.rectangle(252, 500, 72, 72);
			cb.rectangle(360, 500, 72, 72);
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