/* in_action/chapter10/FoobarCityStreets.java */
package in_action.chapter11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;

import org.xml.sax.InputSource;

import in_action.chapter10.FoobarSvgHandler;

import com.lowagie.text.Document;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoobarCityStreets {

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
		System.out.println("   and an SVG with street names.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> external resource needed:");
		System.out.println("   foobarcity.svg (chapter 10)");
		System.out.println("   and streets.svg");
		System.out.println("-> file generated: foobar_streets.pdf");

		try {
			Document document = new Document(PageSize.A4);
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter11/foobar_streets.pdf"));
			document.open();
			FoobarSvgHandler handler = new FoobarSvgHandler(writer,
					new InputSource(new FileInputStream(
							"resources/in_action/chapter10/foobarcity.svg")));
			PdfTemplate template = handler.getTemplate();
			FoobarSvgTextHandler text = new FoobarSvgTextHandler(
					new InputSource(new FileInputStream(
							"resources/in_action/chapter11/streets.svg")));
			Map streets = text.getStreets();
			FoobarSvgTextHandler.Street street;
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			template.beginText();
			for (Iterator i = streets.keySet().iterator(); i.hasNext();) {
				street = (FoobarSvgTextHandler.Street) streets.get(i.next());
				template.setFontAndSize(bf, street.fontsize);
				template.showTextAligned(PdfTemplate.ALIGN_LEFT, street.name,
						street.x, street.y, street.alpha);
			}
			template.endText();
			Image image = Image.getInstance(template);
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