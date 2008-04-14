/* in_action/chapter11/SeparationColor.java */

package in_action.chapter11;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfSpotColor;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SpotColor;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SeparationColor {

	/**
	 * Generates a PDF file demonstating the use of the separation colorspace.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example SeparationColor");
		System.out.println("-> Creates a PDF file with spotcolors.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: separation_color.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/separation_color.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			PdfSpotColor psc_g = new PdfSpotColor("iTextSpotColorGray", 0.5f,
					new GrayColor(0.9f));
			PdfSpotColor psc_rgb = new PdfSpotColor("iTextSpotColorRGB", 0.9f,
					new Color(0x64, 0x95, 0xed));
			PdfSpotColor psc_cmyk = new PdfSpotColor("iTextSpotColorCMYK",
					0.25f, new CMYKColor(0.3f, .9f, .3f, .1f));

			SpotColor sc_g = new SpotColor(psc_g);
			SpotColor sc_rgb1 = new SpotColor(psc_rgb, 0.1f);
			SpotColor sc_rgb2 = new SpotColor(psc_rgb, 0.2f);
			SpotColor sc_rgb3 = new SpotColor(psc_rgb, 0.3f);
			SpotColor sc_rgb4 = new SpotColor(psc_rgb, 0.4f);
			SpotColor sc_rgb5 = new SpotColor(psc_rgb, 0.5f);
			SpotColor sc_rgb6 = new SpotColor(psc_rgb, 0.6f);
			SpotColor sc_rgb7 = new SpotColor(psc_rgb, 0.7f);
			SpotColor sc_rgb8 = new SpotColor(psc_rgb, 0.8f);
			SpotColor sc_rgb9 = new SpotColor(psc_rgb, 0.9f);
			SpotColor sc_cmyk = new SpotColor(psc_cmyk);

			cb.setColorFill(sc_g);
			cb.rectangle(36, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_g, psc_g.getTint());
			cb.rectangle(90, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_g, 0.2f);
			cb.rectangle(144, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_g, 0.5f);
			cb.rectangle(198, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_g, 1);
			cb.rectangle(252, 770, 36, 36);
			cb.fillStroke();

			cb.setColorFill(sc_rgb1);
			cb.rectangle(36, 716, 36, 36);
			cb.fillStroke();
			cb.setColorFill(sc_rgb2);
			cb.rectangle(90, 716, 36, 36);
			cb.fillStroke();
			cb.setColorFill(sc_rgb3);
			cb.rectangle(144, 716, 36, 36);
			cb.fillStroke();
			cb.setColorFill(sc_rgb4);
			cb.rectangle(198, 716, 36, 36);
			cb.fillStroke();
			cb.setColorFill(sc_rgb5);
			cb.rectangle(252, 716, 36, 36);
			cb.fillStroke();
			cb.setColorFill(sc_rgb6);
			cb.rectangle(306, 716, 36, 36);
			cb.fillStroke();
			cb.setColorFill(sc_rgb7);
			cb.rectangle(360, 716, 36, 36);
			cb.fillStroke();
			cb.setColorFill(sc_rgb8);
			cb.rectangle(416, 716, 36, 36);
			cb.fillStroke();
			cb.setColorFill(sc_rgb9);
			cb.rectangle(470, 716, 36, 36);
			cb.fillStroke();

			cb.setColorFill(psc_rgb, 0.1f);
			cb.rectangle(36, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_rgb, 0.2f);
			cb.rectangle(90, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_rgb, 0.3f);
			cb.rectangle(144, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_rgb, 0.4f);
			cb.rectangle(198, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_rgb, 0.5f);
			cb.rectangle(252, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_rgb, 0.6f);
			cb.rectangle(306, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_rgb, 0.7f);
			cb.rectangle(360, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_rgb, 0.8f);
			cb.rectangle(416, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_rgb, 0.9f);
			cb.rectangle(470, 662, 36, 36);
			cb.fillStroke();

			cb.setColorFill(sc_cmyk);
			cb.rectangle(36, 608, 36, 36);
			cb.fillStroke();
			cb.setColorFill(psc_cmyk, psc_cmyk.getTint());
			cb.rectangle(90, 608, 36, 36);
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