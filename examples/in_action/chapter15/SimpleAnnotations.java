/* in_action/chapter15/SimpleAnnotations.java */

package in_action.chapter15;

import java.io.FileOutputStream;
import java.net.URL;

import com.lowagie.text.Annotation;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SimpleAnnotations {

	/**
	 * Creates a PDF file with annotations.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example SimpleAnnotations");
		System.out.println("-> Creates a PDF file with annoations;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: foxdog.mpg");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   simple_annotations1.pdf and simple_annotations2.pdf");
		// step 1:
		Document document1 = new Document(PageSize.A4, 10, 10, 10, 10);
		Document document2 = new Document(PageSize.A4, 10, 10, 10, 10);
		try {

			// step 2:
			PdfWriter writer1 = PdfWriter.getInstance(document1,
					new FileOutputStream("results/in_action/chapter15/simple_annotations1.pdf"));
			PdfWriter writer2 = PdfWriter.getInstance(document2,
					new FileOutputStream("results/in_action/chapter15/simple_annotations2.pdf"));
			// step 3:
			writer2.setPdfVersion(PdfWriter.VERSION_1_5);
			document1.open();
			document2.open();
			// step 4:
			document1.add(new Paragraph(
					"Each square on this page represents an annotation."));
			// document1
			PdfContentByte cb1 = writer1.getDirectContent();
			Annotation a1 = new Annotation(
					"authors",
					"Maybe it's because I wanted to be an author myself that I wrote iText.",
					250f, 700f, 350f, 800f);
			Annotation a2 = new Annotation(250f, 550f, 350f, 650f, new URL(
					"http://www.lowagie.com/iText/"));
			Annotation a3 = new Annotation(250f, 400f, 350f, 500f,
					"http://www.lowagie.com/iText");
			Annotation a4 = new Annotation(250f, 250f, 350f, 350f,
					PdfAction.LASTPAGE);
			// draw rectangles to show where the annotations were added
			cb1.rectangle(250, 700, 100, 100);
			document1.add(a1);
			cb1.rectangle(250, 550, 100, 100);
			document1.add(a2);
			cb1.rectangle(250, 400, 100, 100);
			document1.add(a3);
			cb1.rectangle(250, 250, 100, 100);
			document1.add(a4);
			cb1.stroke();
			// more content
			document1.newPage();
			for (int i = 0; i < 5; i++) {
				document1.add(new Paragraph("blahblahblah"));
			}
			document1.add(new Annotation("blahblah",
					"Adding an annotation without specifying coordinates"));
			for (int i = 0; i < 3; i++) {
				document1.add(new Paragraph("blahblahblah"));
			}
			document1.newPage();
			document1
					.add(new Chunk("marked chunk").setLocalDestination("mark"));

			// document2
			document2.add(new Paragraph(
					"Each square on this page represents an annotation."));
			PdfContentByte cb2 = writer2.getDirectContent();
			Annotation a5 = new Annotation(100f, 700f, 200f, 800f,
					"resources/in_action/chapter15/foxdog.mpg", "video/mpeg", true);
			Annotation a6 = new Annotation(100f, 550f, 200f, 650f,
					"results/in_action/chapter15/simple_annotations1.pdf", "mark");
			Annotation a7 = new Annotation(100f, 400f, 200f, 500f,
					"results/in_action/chapter15/simple_annotations1.pdf", 2);
			Annotation a8 = new Annotation(100f, 250f, 200f, 350f,
					"C://windows/notepad.exe", null, null, null);
			// draw rectangles to show where the annotations were added
			cb2.rectangle(100, 700, 100, 100);
			document2.add(a5);
			cb2.rectangle(100, 550, 100, 100);
			document2.add(a6);
			cb2.rectangle(100, 400, 100, 100);
			document2.add(a7);
			cb2.rectangle(100, 250, 100, 100);
			document2.add(a8);
			cb2.stroke();
		} catch (Exception de) {
			de.printStackTrace();
		}

		// step 5: we close the document
		document1.close();
		document2.close();
	}
}