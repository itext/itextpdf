/* in_action/chapter15/TextAnnotations.java */

package in_action.chapter15;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class TextAnnotations {

	/**
	 * Creates a PDF file with annotations.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example Text Annotations");
		System.out.println("-> Creates a PDF file with annotations;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   text_annotations.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/text_annotations.pdf"));
			// step 3:
			document.open();
			// step 4:
			writer.addAnnotation(PdfAnnotation.createText(writer,
					new Rectangle(50, 780, 70, 800), "Comment",
					"This Comment annotation was made with 'createText'",
					false, "Comment"));
			writer.addAnnotation(PdfAnnotation.createText(writer,
					new Rectangle(100, 780, 120, 800), "Help",
					"This Help annotation was made with 'createText'", true,
					"Help"));
			writer.addAnnotation(PdfAnnotation.createText(writer,
					new Rectangle(150, 780, 170, 800), "Insert",
					"This Insert annotation was made with 'createText'", false,
					"Insert"));
			writer.addAnnotation(PdfAnnotation.createText(writer,
					new Rectangle(200, 780, 220, 800), "Key",
					"This Key annotation was made with 'createText'", true,
					"Key"));
			writer.addAnnotation(PdfAnnotation.createText(writer,
					new Rectangle(250, 780, 270, 800), "NewParagraph",
					"This NewParagraph annotation was made with 'createText'",
					false, "NewParagraph"));
			writer.addAnnotation(PdfAnnotation.createText(writer,
					new Rectangle(300, 780, 320, 800), "Note",
					"This Note annotation was made with 'createText'", true,
					"Note"));
			writer.addAnnotation(PdfAnnotation.createText(writer,
					new Rectangle(350, 780, 370, 800), "Paragraph",
					"This Paragraph annotation was made with 'createText'",
					false, "Paragraph"));
		} catch (Exception de) {
			de.printStackTrace();
		}

		// step 5: we close the document
		document.close();
	}
}