/* in_action/chapter05/HitchcockAwt.java */

package in_action.chapter05;

import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HitchcockAwt {

	/**
	 * Generates PDF files with Images.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 5: example HitchcockAwt");
		System.out.println("-> Creates a PDF file with images.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resource needed: hitchcock.png");
		System.out.println("-> resulting PDFs:");
		System.out.println("   hitchcock.pdf");
		System.out.println("   hitchcockAwt.pdf");
		System.out.println("   hitchcock100.pdf");
		System.out.println("   hitchcock20.pdf");
		System.out.println("   hitchcock10.pdf");
		com.lowagie.text.Image img = null;
		Document document = new Document(new Rectangle(200, 280));
		try {
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter05/hitchcock.pdf"));
			document.open();
			img = com.lowagie.text.Image
					.getInstance("resources/in_action/chapter05/hitchcock.png");
			img.setAbsolutePosition(15, 15);
			document.add(img);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();

		// adding the image as an Image
		java.awt.Image awtImage = Toolkit.getDefaultToolkit().createImage(
				"resources/in_action/chapter05/hitchcock.png");
		document = new Document(new Rectangle(200, 280));
		try {
			PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter05/hitchcockAwt.pdf"));
			document.open();
			img = com.lowagie.text.Image.getInstance(awtImage, null);
			img.setAbsolutePosition(15, 15);
			document.add(img);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		// converting the image to JPEG 100%
		document = new Document(new Rectangle(200, 280));
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter05/hitchcock100.pdf"));
			document.open();
			img = Image.getInstance(writer, awtImage, 1);
			img.setAbsolutePosition(15, 15);
			document.add(img);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		// converting the image to JPEG 50%
		document = new Document(new Rectangle(200, 280));
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter05/hitchcock20.pdf"));
			document.open();
			img = Image.getInstance(writer, awtImage, 0.2f);
			img.setAbsolutePosition(15, 15);
			document.add(img);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		// converting the image to JPEG 10%
		document = new Document(new Rectangle(200, 280));
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter05/hitchcock10.pdf"));
			document.open();
			img = Image.getInstance(writer, awtImage, 0.1f);
			img.setAbsolutePosition(15, 15);
			document.add(img);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}

}
