/* in_action/chapter12/SunTutorialExampleWithText.java */

package in_action.chapter12;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SunTutorialExampleWithText extends SunTutorialExample {

	/**
	 * Example code from SUN's Java tutorial translated to PDF.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: example from SUN's Java tutorial");
		System.out.println("-> Converts a shape and some text created with Graphics2D to PDF");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource needed: gara.ttf");
		System.out.println("-> file generated: sun_tutorial_with_text.pdf");
		SunTutorialExampleWithText example = new SunTutorialExampleWithText();
		example.createPdf();
	}

	/** Creates the PDF. */
	public void createPdf() {
		// step 1: creation of a document-object
		Document document = new Document(new Rectangle(w, h));
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter12/sun_tutorial_with_text.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			// we create a template and a Graphics2D object that corresponds
			// with it
			PdfContentByte cb = writer.getDirectContent();
			PdfTemplate tp = cb.createTemplate(w, h);
			DefaultFontMapper mapper = new DefaultFontMapper();
			mapper.insertDirectory("c:/windows/fonts");
			String name;
			Map map = mapper.getMapper();
			for (Iterator i = map.keySet().iterator(); i.hasNext();) {
				name = (String) i.next();
				System.out
						.println(name
								+ ": "
								+ ((DefaultFontMapper.BaseFontParameters) map
										.get(name)).fontName);
			}
			Graphics2D g2 = tp.createGraphics(w, h, mapper);
			paint(g2);
			g2.setColor(Color.black);
			java.awt.Font thisFont = new java.awt.Font("Garamond",
					java.awt.Font.PLAIN, 18);
			g2.setFont(thisFont);
			String pear = "Pear";
			FontMetrics metrics = g2.getFontMetrics();
			int width = metrics.stringWidth(pear);
			g2.drawString(pear, (w - width) / 2, 20);
			g2.dispose();
			cb.addTemplate(tp, 0, 0);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
