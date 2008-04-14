/* in_action/chapter15/AnnotatedImages.java */

package in_action.chapter15;

import java.io.FileOutputStream;

import com.lowagie.text.Annotation;
import com.lowagie.text.Document;
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

public class AnnotatedImages {

	/**
	 * Creates a PDF file with an annotated image.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example AnnotatedImages");
		System.out.println("-> Creates a PDF with annotated images;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: foxdog.jpg (chapter 5) and iTextLogo.gif (chapter 10)");
		System.out.println("-> file generated in /results subdirectory:");
		System.out.println("   annotated_images.pdf");
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			// step 2:
			// we create a writer that listens to the document
			PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/annotated_images.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add some content
			Image gif = Image
					.getInstance("resources/in_action/chapter10/iTextLogo.gif");
			gif.setAnnotation(new Annotation(0, 0, 0, 0,
					"http://www.lowagie.com/iText"));
			gif.setAbsolutePosition(30f, 750f);
			document.add(gif);
			Image jpeg = Image
					.getInstance("resources/in_action/chapter05/foxdog.jpg");
			jpeg.setAnnotation(new Annotation("picture",
					"quick brown fox jumps over the lazy dog", 0, 0, 0, 0));
			jpeg.setAbsolutePosition(120f, 550f);
			document.add(jpeg);
		} catch (Exception de) {
			de.printStackTrace();
		}

		// step 5: we close the document
		document.close();
	}
}