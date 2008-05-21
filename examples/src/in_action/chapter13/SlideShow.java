/* in_action/chapter13/SlideShow.java */

package in_action.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfTransition;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SlideShow {

	/**
	 * Generates a slideshow in PDF.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example SlideShow");
		System.out.println("-> Generates a slideshow in PDF.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: some photos in the /resources directory");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   slide_show.pdf");
		// step 1: creation of a document-object
		Document document = new Document(new Rectangle(144, 115));
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter13/slide_show.pdf"));
			writer.setPdfVersion(PdfWriter.VERSION_1_5);
			writer.setViewerPreferences(PdfWriter.PageModeFullScreen);
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document

			Image img0 = Image
					.getInstance("resources/in_action/chapter13/fox dog 0.gif");
			img0.setAbsolutePosition(0, 0);
			writer.setTransition(new PdfTransition(PdfTransition.OUTBOX, 3));
			document.add(img0);
			document.newPage();

			Image img1 = Image
					.getInstance("resources/in_action/chapter13/fox dog 1.gif");
			img1.setAbsolutePosition(0, 0);
			writer.setTransition(new PdfTransition(PdfTransition.INBOX, 2));
			writer.setDuration(1);
			document.add(img1);
			document.newPage();

			Image img2 = Image
					.getInstance("resources/in_action/chapter13/fox dog 2.gif");
			img2.setAbsolutePosition(0, 0);
			writer.setDuration(3);
			writer.setTransition(new PdfTransition(PdfTransition.DGLITTER, 2));
			document.add(img2);
			document.newPage();

			Image img3 = Image
					.getInstance("resources/in_action/chapter13/fox dog 3.gif");
			img3.setAbsolutePosition(0, 0);
			writer.setDuration(1);
			writer.setTransition(new PdfTransition(PdfTransition.TBWIPE));
			document.add(img3);
			document.newPage();

			Image img4 = Image
					.getInstance("resources/in_action/chapter13/fox dog 4.gif");
			img4.setAbsolutePosition(0, 0);
			writer.setDuration(1);
			writer.setTransition(new PdfTransition(PdfTransition.SPLITVOUT));
			document.add(img4);
			document.newPage();

			Image img5 = Image
					.getInstance("resources/in_action/chapter13/fox dog 5.gif");
			img5.setAbsolutePosition(0, 0);
			writer.setDuration(1);
			writer.setTransition(new PdfTransition(PdfTransition.LRGLITTER, 3));
			document.add(img5);
			document.newPage();

			Image img6 = Image
					.getInstance("resources/in_action/chapter13/fox dog 6.gif");
			img6.setAbsolutePosition(0, 0);
			writer.setDuration(2);
			writer.setTransition(new PdfTransition());
			document.add(img6);
			document.newPage();

			Image img7 = Image
					.getInstance("resources/in_action/chapter13/fox dog 7.gif");
			img7.setAbsolutePosition(0, 0);
			writer.setDuration(1);
			writer.setTransition(new PdfTransition(PdfTransition.DISSOLVE, 2));
			document.add(img7);
			document.newPage();

			Image img8 = Image
					.getInstance("resources/in_action/chapter13/fox dog 8.gif");
			img8.setAbsolutePosition(0, 0);
			writer.setDuration(1);
			document.add(img8);
			document.newPage();

			Image img9 = Image
					.getInstance("resources/in_action/chapter13/fox dog 9.gif");
			img9.setAbsolutePosition(0, 0);
			writer.setDuration(1);
			document.add(img9);
			document.newPage();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}