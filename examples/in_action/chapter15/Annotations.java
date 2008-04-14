/* in_action/chapter15/Annotations.java */

package in_action.chapter15;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfBoolean;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Annotations {

	/**
	 * Creates a PDF file with annotations.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example Annotations");
		System.out.println("-> Creates a PDF file with annotations;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: foxdog.jpg (chapter 5) and foxdog.mpg");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   annotations.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/annotations.pdf"));
			// step 3:
			writer.setPdfVersion(PdfWriter.VERSION_1_5);
			document.open();
			// step 4:
			document.add(new Chunk("top of the page")
					.setLocalDestination("top"));
			PdfContentByte cb = writer.getDirectContent();
			// page 1
			PdfAnnotation annotation = new PdfAnnotation(writer, new Rectangle(
					100, 750, 150, 800));
			writer.addAnnotation(annotation);
			annotation.put(PdfName.SUBTYPE, PdfName.TEXT);
			annotation.put(PdfName.OPEN, PdfBoolean.PDFTRUE);
			annotation.put(PdfName.T, new PdfString("custom"));
			annotation.put(PdfName.CONTENTS, new PdfString(
					"This is a custom built text annotation."));
			cb.rectangle(100, 750, 50, 50);
			cb.stroke();

			PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(writer,
					"resources/in_action/chapter15/foxdog.mpg", "foxdog.mpg", null);
			writer.addAnnotation(PdfAnnotation.createScreen(writer,
					new Rectangle(200f, 700f, 300f, 800f), "Fox and Dog", fs,
					"video/mpeg", true));
			PdfAnnotation a = new PdfAnnotation(writer, 200f, 550f, 300f, 650f,
					PdfAction.javaScript("app.alert('Hello');\r", writer));
			writer.addAnnotation(a);
			writer.addAnnotation(PdfAnnotation.createFileAttachment(writer,
					new Rectangle(100f, 650f, 150f, 700f), "This is some text",
					"some text".getBytes(), null, "some.txt"));
			writer.addAnnotation(PdfAnnotation.createText(writer,
					new Rectangle(200f, 400f, 300f, 500f), "Help",
					"This Help annotation was made with 'createText'", false,
					"Help"));
			writer.addAnnotation(PdfAnnotation.createText(writer,
					new Rectangle(200f, 250f, 300f, 350f), "Help",
					"This Comment annotation was made with 'createText'", true,
					"Comment"));
			cb.rectangle(200, 700, 100, 100);
			cb.rectangle(200, 550, 100, 100);
			cb.rectangle(200, 400, 100, 100);
			cb.rectangle(200, 250, 100, 100);
			cb.stroke();
			document.newPage();
			// page 2
			writer.addAnnotation(PdfAnnotation.createLink(writer,
					new Rectangle(200f, 700f, 300f, 800f),
					PdfAnnotation.HIGHLIGHT_INVERT, PdfAction.javaScript(
							"app.alert('Hello');\r", writer)));
			writer.addAnnotation(PdfAnnotation.createLink(writer,
					new Rectangle(200f, 550f, 300f, 650f),
					PdfAnnotation.HIGHLIGHT_OUTLINE, "top"));
			writer.addAnnotation(PdfAnnotation.createLink(writer,
					new Rectangle(400f, 700f, 500f, 800f),
					PdfAnnotation.HIGHLIGHT_PUSH, 1, new PdfDestination(
							PdfDestination.FIT)));
			writer.addAnnotation(PdfAnnotation.createPopup(writer,
					new Rectangle(400f, 550f, 500f, 650f),
					"Hello, I'm a popup!", true));

			PdfAnnotation shape1 = PdfAnnotation
					.createSquareCircle(
							writer,
							new Rectangle(200f, 400f, 300f, 500f),
							"This Comment annotation was made with 'createSquareCircle'",
							false);
			float[] red = { 1, 0, 0 };
			shape1.put(new PdfName("IC"), new PdfArray(red));
			writer.addAnnotation(shape1);
			PdfAnnotation shape2 = PdfAnnotation.createLine(writer,
					new Rectangle(200f, 250f, 300f, 350f), "this is a line",
					200, 250, 300, 350);
			shape2.setColor(Color.BLUE);
			PdfArray lineEndingStyles = new PdfArray();
			lineEndingStyles.add(new PdfName("Diamond"));
			lineEndingStyles.add(new PdfName("OpenArrow"));
			shape2.put(new PdfName("LE"), lineEndingStyles);
			shape2.put(PdfName.BS, new PdfBorderDictionary(5,
					PdfBorderDictionary.STYLE_SOLID));
			writer.addAnnotation(shape2);
			PdfContentByte pcb = new PdfContentByte(writer);
			pcb.setColorFill(new Color(0xFF, 0x00, 0x00));
			PdfAnnotation freeText = PdfAnnotation.createFreeText(writer,
					new Rectangle(400f, 400f, 500f, 500f),
					"This is some free text, blah blah blah", pcb);
			writer.addAnnotation(freeText);
			PdfAnnotation attachment = PdfAnnotation.createFileAttachment(
					writer, new Rectangle(400f, 250f, 500f, 350f),
					"Image of the fox and the dog", null,
					"resources/in_action/chapter05/foxdog.jpg", "foxdog.jpg");
			attachment.put(PdfName.NAME, new PdfString("Paperclip"));
			writer.addAnnotation(attachment);

		} catch (Exception de) {
			de.printStackTrace();
		}

		// step 5: we close the document
		document.close();
	}
}