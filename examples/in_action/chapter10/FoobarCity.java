/* in_action/chapter10/FoobarCity.java */
package in_action.chapter10;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.xml.sax.InputSource;

import com.lowagie.text.Document;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoobarCity {

	/** The document to which the SVG is written. */
	protected Document document = new Document();

	/** The writer that generates the PDF. */
	protected PdfWriter writer;

	/**
	 * Generates a PDF showing the Map of a City.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 10: City of Foobar");
		System.out.println("-> Creates a Map based on an SVG image.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> external resource needed: foobarcity.svg");
		System.out.println("-> resulting PDF: foobarcity.pdf");

		try {
			Document.compress = false;
			Document document = new Document(PageSize.A4);
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter10/foobarcity.pdf"));
			document.open();
			FoobarSvgHandler handler = new FoobarSvgHandler(writer,
					new InputSource(new FileInputStream(
							"resources/in_action/chapter10/foobarcity.svg")));
			Image image = handler.getImage();
			image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
			image.setAbsolutePosition(0, PageSize.A4.getHeight()
					- image.getScaledHeight());
			document.add(image);
			document.close();
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}
}