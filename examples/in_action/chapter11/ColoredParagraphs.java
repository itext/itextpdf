/* in_action/chapter11/ColoredParagraphs.java */

package in_action.chapter11;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PatternColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPatternPainter;
import com.lowagie.text.pdf.PdfShading;
import com.lowagie.text.pdf.PdfShadingPattern;
import com.lowagie.text.pdf.PdfSpotColor;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.ShadingColor;
import com.lowagie.text.pdf.SpotColor;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ColoredParagraphs {

	/**
	 * Generates a PDF file using different color spaces for text.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example ColoredParagraphs");
		System.out
				.println("-> Creates a PDF file with paragraph that are written in special colors.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource needed: foxdog.jpg (chapter 5)");
		System.out.println("-> file generated: colored_paragraphs.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/colored_paragraphs.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();

			PdfSpotColor psc_cmyk = new PdfSpotColor("iTextSpotColorCMYK",
					0.25f, new CMYKColor(0.3f, .9f, .3f, .1f));
			SpotColor sc_cmyk = new SpotColor(psc_cmyk);

			Image img = Image
					.getInstance("resources/in_action/chapter05/foxdog.jpg");
			PdfPatternPainter img_pattern = cb.createPattern(img.getScaledWidth(),
					img.getScaledHeight(), img.getScaledWidth(), img.getScaledHeight());
			img_pattern.addImage(img, img.getScaledWidth(), 0f, 0f, img
					.getScaledHeight(), 0f, 0f);
			img_pattern.setPatternMatrix(1f, 0f, 0f, 1f, 60f, 60f);
			PatternColor img_color = new PatternColor(img_pattern);

			PdfShading axial = PdfShading.simpleAxial(writer, 36, 716, 396,
					788, Color.orange, Color.blue);
			PdfShadingPattern axialPattern = new PdfShadingPattern(axial);
			ShadingColor axialColor = new ShadingColor(axialPattern);

			document.add(new Paragraph(
					"This is a paragraph painted using a SpotColor", new Font(
							Font.HELVETICA, 24, Font.BOLD, sc_cmyk)));
			document.add(new Paragraph(
					"This is a paragraph painted using an image pattern",
					new Font(Font.HELVETICA, 24, Font.BOLD, img_color)));
			document.add(new Paragraph(
					"This is a paragraph painted using a shading pattern",
					new Font(Font.HELVETICA, 24, Font.BOLD, axialColor)));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}