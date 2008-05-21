/* in_action/chapter12/OptionalXObjectExample.java */

package in_action.chapter12;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfLayer;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class OptionalXObjectExample {

	/**
	 * A simple example with optional content.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: OptionalXObjectExample");
		System.out.println("-> Creates a PDF with optional content.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource: iTextLogo.gif (chapter 10)");
		System.out.println("-> file generated: xobjects.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter12/xobjects.pdf"));
			writer.setPdfVersion(PdfWriter.VERSION_1_5);
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();

			PdfLayer logo = new PdfLayer("iText logo", writer);
			PdfLayer eye = new PdfLayer("iText eye", writer);
			PdfLayer field = new PdfLayer("form field", writer);
			Image image = Image
					.getInstance("resources/in_action/chapter10/iTextLogo.gif");
			image.setAbsolutePosition(36, 780);
			image.setLayer(logo);
			document.add(image);

			PdfTemplate template = cb.createTemplate(150, 150);
			template.setLineWidth(12f);
			template.arc(40f - (float) Math.sqrt(12800), 110f + (float) Math
					.sqrt(12800), 200f - (float) Math.sqrt(12800), -50f
					+ (float) Math.sqrt(12800), 281.25f, 33.75f);
			template.arc(40f, 110f, 200f, -50f, 90f, 45f);
			template.stroke();
			template.setLineCap(PdfContentByte.LINE_JOIN_ROUND);
			template.arc(80f, 30f, 160f, 110f, 90f, 180f);
			template.arc(115f, 65f, 125f, 75f, 0f, 360f);
			template.stroke();
			template.setLayer(eye);
			cb.addTemplate(template, 36, 630);

			TextField ff = new TextField(writer, new Rectangle(36, 600, 150,
					620), "field1");
			ff.setBorderColor(Color.blue);
			ff.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);
			ff.setBorderWidth(TextField.BORDER_WIDTH_THIN);
			ff.setText("iText in Action");
			PdfFormField form = ff.getTextField();
			form.setLayer(field);
			writer.addAnnotation(form);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
