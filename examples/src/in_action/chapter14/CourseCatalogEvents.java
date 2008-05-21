/* in_action/chapter14/CourseCatalogWatermarks.java */

package in_action.chapter14;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import in_action.chapter13.CourseCatalogBookmarked;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfPageLabels;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class CourseCatalogEvents extends PdfPageEventHelper {
	/** header */
	protected String header = "";

	/** The font that will be used. */
	protected BaseFont helv;

	/** The Image used as watermark. */
	protected Image image;

	/** The Graphics State for the watermark. */
	protected PdfGState gstate;

	/**
	 * @param header
	 *            The header to set.
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onOpenDocument(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		try {
			helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
					BaseFont.NOT_EMBEDDED);
			image = Image
					.getInstance("resources/in_action/chapter05/shield_tuf.gif");
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
		gstate = new PdfGState();
		gstate.setFillOpacity(0.1f);
		gstate.setStrokeOpacity(0.3f);
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onEndPage(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte directcontent = writer.getDirectContent();
		directcontent.saveState();
		String text = "Page " + writer.getPageNumber();
		directcontent.beginText();
		directcontent.setFontAndSize(helv, 11);
		directcontent.showTextAligned(Element.ALIGN_RIGHT, header, document
				.right(), 810, 0);
		directcontent.showTextAligned(Element.ALIGN_CENTER, text, (document
				.right() + document.left()) / 2, 28, 0);
		directcontent.endText();
		directcontent.restoreState();
		PdfContentByte contentunder = writer.getDirectContentUnder();
		contentunder.saveState();
		contentunder.setGState(gstate);
		try {
			contentunder.addImage(image, image.getWidth(), 0, 0, image.getHeight(),
					100, 200);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		contentunder.restoreState();

	}

	/**
	 * Generates a fancy flyer in PDF for the new faculty of the Technological
	 * University of Foobar.
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(final String[] args) {
		System.out.println("Chapter 14: Course Catalog with watermarks");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   course_catalog_events.pdf");
		try {
			Document document = new Document();
			OutputStream outPDF = new FileOutputStream(
					"results/in_action/chapter14/course_catalog_events.pdf");
			PdfWriter writer = PdfWriter.getInstance(document, outPDF);
			writer.setViewerPreferences(PdfWriter.PageLayoutSinglePage
					| PdfWriter.PageModeUseOutlines);
			CourseCatalogEvents event = new CourseCatalogEvents();
			writer.setPageEvent(event);
			document.open();

			PdfOutline outline = writer.getRootOutline();
			String[] courses = { "8001", "8002", "8003", "8010", "8011",
					"8020", "8021", "8022", "8030", "8031", "8032", "8033",
					"8040", "8041", "8042", "8043", "8051", "8052" };
			CourseCatalogBookmarked cc;
			PdfPageLabels labels = new PdfPageLabels();
			for (int i = 0; i < courses.length; i++) {
				cc = new CourseCatalogBookmarked(courses[i]);
				cc.flushToDocument(document);
				int pagenumber = writer.getPageNumber();
				event.setHeader(courses[i]);
				new PdfOutline(outline, new PdfDestination(PdfDestination.FIT),
						cc.getTitle());
				try {
					labels.addPageLabel(pagenumber, PdfPageLabels.EMPTY,
							courses[i]);
					writer.setThumbnail(Image
							.getInstance("resources/in_action/chapter07/"
									+ courses[i] + ".jpg"));
				} catch (FileNotFoundException fnfe) {
					// the thumbnail will not be replaced by an image if the
					// image is not found
				}
				document.newPage();
			}
			writer.setPageLabels(labels);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}