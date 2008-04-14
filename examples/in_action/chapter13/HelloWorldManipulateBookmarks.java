/* in_action/chapter13/HelloWorldManipulateBookmarks.java */

package in_action.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldManipulateBookmarks {

	/**
	 * Generates a PDF file, then adds an extra page, stamps it, adds an entry
	 * to the bookmarks.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example Manipulate Bookmarks");
		System.out.println("-> Creates a PDF file, then stamps it;");
		System.out.println("   an extra page and an extra bookmark are added.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldRead.pdf");
		System.out.println("   HelloWorldManipulateBookmarks.pdf");
		// we create a PDF file
		createPdf("results/in_action/chapter13/HelloWorldRead.pdf");
		// now we are going to inspect it
		try {
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			PdfReader reader = new PdfReader("results/in_action/chapter13/HelloWorldRead.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter13/HelloWorldManipulateBookmarks.pdf"));
			stamper.insertPage(1, PageSize.A4);
			PdfContentByte cb = stamper.getOverContent(1);
			cb.beginText();
			cb.setFontAndSize(bf, 18);
			cb.setTextMatrix(36, 770);
			cb.showText("Inserted Title Page");
			cb.endText();
			stamper.addAnnotation(PdfAnnotation.createText(stamper.getWriter(),
					new Rectangle(30f, 750f, 80f, 800f), "inserted page",
					"This page is the title page.", true, null), 1);
			List list = SimpleBookmark.getBookmark(reader);
			HashMap map = new HashMap();
			map.put("Title", "Title Page");
			ArrayList kids = new ArrayList();
			HashMap kid1 = new HashMap();
			kid1.put("Title", "top");
			kid1.put("Action", "GoTo");
			kid1.put("Page", "1 FitH 806");
			kids.add(kid1);
			HashMap kid2 = new HashMap();
			kid2.put("Title", "bottom");
			kid2.put("Action", "GoTo");
			kid2.put("Page", "1 FitH 36");
			kids.add(kid2);
			map.put("Kids", kids);
			list.add(0, map);
			SimpleBookmark.exportToXML(list, new FileOutputStream(
					"results/in_action/chapter13/manipulated_bookmarks.xml"), "ISO8859-1", true);
			stamper.setOutlines(list);
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
			section = people.addSection("to suns and daughters:");
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