/* in_action/chapter11/DeviceColor.java */

package in_action.chapter11;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class DeviceColor {

	/**
	 * Generates a PDF file in which the device colorspace is demonstrated.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 11: example DeviceColor");
		System.out.println("-> Creates a PDF file with device colors");
		System.out.println("   and painted.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: device_color.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter11/device_color.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			cb.setColorFill(new GrayColor(0));
			cb.rectangle(36, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new GrayColor(0.125f));
			cb.rectangle(90, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new GrayColor(0.25f));
			cb.rectangle(144, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new GrayColor(0.375f));
			cb.rectangle(198, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new GrayColor(0.5f));
			cb.rectangle(252, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new GrayColor(0.625f));
			cb.rectangle(306, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new GrayColor(191));
			cb.rectangle(360, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new GrayColor(223));
			cb.rectangle(416, 770, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new GrayColor(255));
			cb.rectangle(470, 770, 36, 36);
			cb.fillStroke();

			cb.setGrayFill(0);
			cb.rectangle(36, 716, 36, 36);
			cb.fillStroke();
			cb.setGrayFill(0.125f);
			cb.rectangle(90, 716, 36, 36);
			cb.fillStroke();
			cb.setGrayFill(0.25f);
			cb.rectangle(144, 716, 36, 36);
			cb.fillStroke();
			cb.setGrayFill(0.375f);
			cb.rectangle(198, 716, 36, 36);
			cb.fillStroke();
			cb.setGrayFill(0.5f);
			cb.rectangle(252, 716, 36, 36);
			cb.fillStroke();
			cb.setGrayFill(0.625f);
			cb.rectangle(306, 716, 36, 36);
			cb.fillStroke();
			cb.setGrayFill(0.75f);
			cb.rectangle(360, 716, 36, 36);
			cb.fillStroke();
			cb.setGrayFill(0.875f);
			cb.rectangle(416, 716, 36, 36);
			cb.fillStroke();
			cb.setGrayFill(1);
			cb.rectangle(470, 716, 36, 36);
			cb.fillStroke();

			cb.setColorFill(new Color(0x00, 0x00, 0x00));
			cb.rectangle(36, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new Color(0x00, 0x00, 0xFF));
			cb.rectangle(90, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new Color(0x00, 0xFF, 0x00));
			cb.rectangle(144, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new Color(0x00, 0xFF, 0xFF));
			cb.rectangle(198, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new Color(1f, 0f, 0f));
			cb.rectangle(252, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new Color(1f, 0, 1f));
			cb.rectangle(306, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new Color(1f, 1f, 0));
			cb.rectangle(360, 662, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new Color(1f, 1f, 1f));
			cb.rectangle(416, 662, 36, 36);
			cb.fillStroke();

			cb.setRGBColorFill(0x00, 0x00, 0x00);
			cb.rectangle(36, 608, 36, 36);
			cb.fillStroke();
			cb.setRGBColorFill(0x00, 0x00, 0xFF);
			cb.rectangle(90, 608, 36, 36);
			cb.fillStroke();
			cb.setRGBColorFill(0x00, 0xFF, 0x00);
			cb.rectangle(144, 608, 36, 36);
			cb.fillStroke();
			cb.setRGBColorFill(0x00, 0xFF, 0xFF);
			cb.rectangle(198, 608, 36, 36);
			cb.fillStroke();
			cb.setRGBColorFillF(1f, 0f, 0f);
			cb.rectangle(252, 608, 36, 36);
			cb.fillStroke();
			cb.setRGBColorFillF(1f, 0f, 1f);
			cb.rectangle(306, 608, 36, 36);
			cb.fillStroke();
			cb.setRGBColorFillF(1f, 1f, 0f);
			cb.rectangle(360, 608, 36, 36);
			cb.fillStroke();
			cb.setRGBColorFillF(1f, 1f, 1f);
			cb.rectangle(416, 608, 36, 36);
			cb.fillStroke();

			cb.setColorFill(new CMYKColor(0x00, 0x00, 0x00, 0x00));
			cb.rectangle(36, 554, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new CMYKColor(0x00, 0x00, 0xFF, 0x00));
			cb.rectangle(90, 554, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new CMYKColor(0x00, 0x00, 0xFF, 0x0F));
			cb.rectangle(144, 554, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new CMYKColor(0x00, 0xFF, 0x00, 0x00));
			cb.rectangle(198, 554, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new CMYKColor(0f, 1f, 0f, 0.5f));
			cb.rectangle(252, 554, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new CMYKColor(1f, 0f, 0f, 0f));
			cb.rectangle(306, 554, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new CMYKColor(1f, 0f, 0f, 0.5f));
			cb.rectangle(360, 554, 36, 36);
			cb.fillStroke();
			cb.setColorFill(new CMYKColor(0f, 0f, 0f, 1f));
			cb.rectangle(416, 554, 36, 36);
			cb.fillStroke();

			cb.setCMYKColorFill(0x00, 0x00, 0x00, 0x00);
			cb.rectangle(36, 500, 36, 36);
			cb.fillStroke();
			cb.setCMYKColorFill(0x00, 0xFF, 0xFF, 0x00);
			cb.rectangle(90, 500, 36, 36);
			cb.fillStroke();
			cb.setCMYKColorFill(0x00, 0xFF, 0xFF, 0x0F);
			cb.rectangle(144, 500, 36, 36);
			cb.fillStroke();
			cb.setCMYKColorFill(0xFF, 0xFF, 0x00, 0x00);
			cb.rectangle(198, 500, 36, 36);
			cb.fillStroke();
			cb.setCMYKColorFillF(1f, 1f, 0f, 0.5f);
			cb.rectangle(252, 500, 36, 36);
			cb.fillStroke();
			cb.setCMYKColorFillF(1f, 0f, 1f, 0f);
			cb.rectangle(306, 500, 36, 36);
			cb.fillStroke();
			cb.setCMYKColorFillF(1f, 0f, 1f, 0.5f);
			cb.rectangle(360, 500, 36, 36);
			cb.fillStroke();
			cb.setCMYKColorFillF(0f, 0f, 0f, 1f);
			cb.rectangle(416, 500, 36, 36);
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