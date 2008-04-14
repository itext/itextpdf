/* in_action/chapter14/WatermarkExample.java */

package in_action.chapter14;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class WatermarkExample extends PdfPageEventHelper {

	/** The PdfTemplate that contains the total number of pages. */
	protected PdfTemplate total;

	/** The font that will be used. */
	protected BaseFont helv;

	/** The Graphics State for the watermark. */
	protected PdfGState gstate;

	/** The color of the text watermark. */
	protected Color color;

	/** The Image used as watermark. */
	protected Image image;

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onOpenDocument(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(100, 100);
		total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
		try {
			helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
					BaseFont.NOT_EMBEDDED);
			image = Image
					.getInstance("resources/in_action/chapter10/iTextLogo.gif");
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
		gstate = new PdfGState();
		gstate.setFillOpacity(0.3f);
		gstate.setStrokeOpacity(0.3f);
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onStartPage(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onStartPage(PdfWriter writer, Document document) {
		if (writer.getPageNumber() % 2 == 1) {
			color = Color.blue;
		} else {
			color = Color.red;
		}
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onEndPage(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte directcontent = writer.getDirectContent();
		directcontent.saveState();
		String text = "Page " + writer.getPageNumber() + " of ";
		float textBase = document.bottom() - 20;
		float textSize = helv.getWidthPoint(text, 12);
		directcontent.beginText();
		directcontent.setFontAndSize(helv, 12);
		if ((writer.getPageNumber() % 2) == 1) {
			directcontent.setTextMatrix(document.left(), textBase);
			directcontent.showText(text);
			directcontent.endText();
			directcontent.addTemplate(total, document.left() + textSize,
					textBase);
		}
		// for even numbers, show the footer at the right
		else {
			float adjust = helv.getWidthPoint("0", 12);
			directcontent.setTextMatrix(document.right() - textSize - adjust,
					textBase);
			directcontent.showText(text);
			directcontent.endText();
			directcontent.addTemplate(total, document.right() - adjust,
					textBase);
		}
		directcontent.restoreState();
		try {
			PdfContentByte contentunder = writer.getDirectContentUnder();
			contentunder.saveState();
			contentunder.setGState(gstate);
			contentunder.addImage(image, image.getWidth() * 4, 0, 0, image
					.getHeight() * 4, 120, 650);
			contentunder.setColorFill(color);
			contentunder.beginText();
			contentunder.setFontAndSize(helv, 48);
			contentunder.showTextAligned(Element.ALIGN_CENTER,
					"My Watermark Under " + writer.getPageNumber(), document
							.getPageSize().getWidth() / 2, document.getPageSize()
							.getHeight() / 2, 45);
			contentunder.endText();
			contentunder.restoreState();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onCloseDocument(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		total.beginText();
		total.setFontAndSize(helv, 12);
		total.setTextMatrix(0, 0);
		total.showText("" + (writer.getPageNumber() - 1));
		total.endText();
	}

	/**
	 * Generates a file with a header and a footer.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 14: Page X of Y Example");
		System.out.println("-> Creates a PDF file with page numbers.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resource needed: iTextLogo.gif (chapter 10)");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   watermarks.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter14/watermarks.pdf"));
			writer.setViewerPreferences(PdfWriter.PageLayoutTwoColumnLeft);
			writer.setPageEvent(new WatermarkExample());
			document.setMargins(36, 36, 36, 54);
			// step 3:
			document.open();
			Paragraph p = new Paragraph();
			// step 4: we grab the ContentByte and do some stuff with it
			for (int k = 1; k <= 30; ++k) {
				p.add(new Phrase("Quick brown fox jumps over the lazy dog. "));
			}
			p.setAlignment(Element.ALIGN_JUSTIFIED);
			for (int k = 1; k <= 12; ++k) {
				document.add(p);
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}