/* in_action/chapter02/HelloWorldStamper2.java */

package in_action.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldStamper2 {

	/**
	 * Generates a PDF file, then stamps it.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 2: example HelloWorldStamper2");
		System.out.println("-> Creates a PDF file, then stamps extra content on it:");
		System.out.println("   a pagenumber, a watermark, some graphics;");
		System.out.println("   the rotation of the original page is not taken into account.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> extra resource needed: watermark.jpg");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldRead.pdf");
		System.out.println("   HelloWorldStamper2.pdf");
		// we create a PDF file
		createPdf("results/in_action/chapter02/HelloWorldRead.pdf");
		// now we are going to inspect it
		try {
			// we create a PdfReader object
			PdfReader reader = new PdfReader("results/in_action/chapter02/HelloWorldRead.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter02/HelloWorldStamper2.pdf"));
			// we create an Image we'll use as a Watermark
			Image img = Image.getInstance("resources/in_action/chapter02/watermark.jpg");
			img.setAbsolutePosition(200, 400);
			// we create a Font for the text to add
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			// these are the canvases we are going to use
			PdfContentByte under, over;
			int total = reader.getNumberOfPages() + 1;
			for (int i = 1; i < total; i++) {
				stamper.setRotateContents(false);
				under = stamper.getUnderContent(i);
				under.addImage(img);
				// text over the existing page
				over = stamper.getOverContent(i);
				over.beginText();
				over.setFontAndSize(bf, 18);
				over.setTextMatrix(30, 30);
				over.showText("page " + i);
				over.endText();
				over.setRGBColorStroke(0xFF, 0x00, 0x00);
				over.setLineWidth(5f);
				over.ellipse(250, 450, 350, 550);
				over.stroke();
			}
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a PDF file with bookmarks.
	 * 
	 * @param filename
	 *            the filename of the PDF file.
	 */
	private static void createPdf(String filename) {
		// we create a document with multiple pages and bookmarks
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			Paragraph hello = new Paragraph(
					"(English:) hello, "
							+ "(Esperanto:) he, alo, saluton, (Latin:) heu, ave, "
							+ "(French:) allô, (Italian:) ciao, (German:) hallo, he, heda, holla, "
							+ "(Portuguese:) alô, olá, hei, psiu, bom día, (Dutch:) hallo, dag, "
							+ "(Spanish:) ola, eh, (Catalan:) au, bah, eh, ep, "
							+ "(Swedish:) hej, hejsan(Danish:) hallo, dav, davs, goddag, hej, "
							+ "(Norwegian:) hei; morn, (Papiamento:) halo; hallo; kí tal, "
							+ "(Faeroese:) halló, hoyr, (Turkish:) alo, merhaba, (Albanian:) tungjatjeta");
			Chapter universe = new Chapter("To the Universe:", 1);
			Section section;
			section = universe.addSection("to the World:");
			section.add(hello);
			section = universe.addSection("to the Sun:");
			section.add(hello);
			section = universe.addSection("to the Moon:");
			section.add(hello);
			section = universe.addSection("to the Stars:");
			section.add(hello);
			document.add(universe);
			Chapter people = new Chapter("To the People:", 2);
			section = people.addSection("to mothers and fathers:");
			section.add(hello);
			section = people.addSection("to brothers and sisters:");
			section.add(hello);
			section = people.addSection("to wives and husbands:");
			section.add(hello);
			section = people.addSection("to sons and daughters:");
			section.add(hello);
			section = people.addSection("to complete strangers:");
			section.add(hello);
			document.add(people);
			document.setPageSize(PageSize.A4.rotate());
			Chapter animals = new Chapter("To the Animals:", 3);
			section = animals.addSection("to cats and dogs:");
			section.add(hello);
			section = animals.addSection("to birds and bees:");
			section.add(hello);
			section = animals.addSection("to farm animals and wild animals:");
			section.add(hello);
			section = animals.addSection("to bugs and beatles:");
			section.add(hello);
			section = animals.addSection("to fish and shellfish:");
			section.add(hello);
			document.add(animals);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}