/* in_action/chapter13/HelloWorldCopyBookmarks.java */

package in_action.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldCopyBookmarks {

	/**
	 * Generates three 'Hello World' files with bookmarks, then copies them into
	 * one file, combining the bookmarks.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example HelloWorldCopyBookmarks");
		System.out.println("-> Creates some PDF files, then copies them to one;");
		System.out.println("   in the new PDF file, the bookmarks are combined.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorld1.pdf");
		System.out.println("   HelloWorld2.pdf");
		System.out.println("   HelloWorld3.pdf");
		System.out.println("   HelloWorldCopyBookmarks.pdf");
		// we create a PDF file
		createPdfs();
		try {
			// we create a PdfReader object
			ArrayList bookmarks = new ArrayList();
			PdfReader reader = new PdfReader("results/in_action/chapter13/HelloWorld1.pdf");
			// step 1
			Document document = new Document(reader.getPageSizeWithRotation(1));
			// step 2
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(
					"results/in_action/chapter13/HelloWorldCopyBookmarks.pdf"));
			// step 3
			document.open();
			// step 4
			copy.addPage(copy.getImportedPage(reader, 1));
			bookmarks.addAll(SimpleBookmark.getBookmark(reader));
			reader = new PdfReader("results/in_action/chapter13/HelloWorld2.pdf");
			copy.addPage(copy.getImportedPage(reader, 1));
			List tmp = SimpleBookmark.getBookmark(reader);
			SimpleBookmark.shiftPageNumbers(tmp, 1, null);
			bookmarks.addAll(tmp);
			reader = new PdfReader("results/in_action/chapter13/HelloWorld3.pdf");
			copy.addPage(copy.getImportedPage(reader, 1));
			tmp = SimpleBookmark.getBookmark(reader);
			SimpleBookmark.shiftPageNumbers(tmp, 2, null);
			bookmarks.addAll(tmp);
			copy.setOutlines(bookmarks);
			// step 5
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates three PDF file with bookmarks.
	 */
	private static void createPdfs() {
		// we create a document with multiple pages and bookmarks
		Document document;
		Paragraph hello = new Paragraph("(English:) hello, " +
				"(Esperanto:) he, alo, saluton, (Latin:) heu, ave, " +
				"(French:) all\u00f4, (Italian:) ciao, (German:) hallo, he, heda, holla, " +
				"(Portuguese:) al\u00f4, ol\u00e1, hei, psiu, bom d\u00eda, (Dutch:) hallo, dag, " +
				"(Spanish:) ola, eh, (Catalan:) au, bah, eh, ep, " +
				"(Swedish:) hej, hejsan(Danish:) hallo, dav, davs, goddag, hej, " +
				"(Norwegian:) hei; morn, (Papiamento:) halo; hallo; k\u00ed tal, " +
				"(Faeroese:) hall\u00f3, hoyr, (Turkish:) alo, merhaba, (Albanian:) tungjatjeta");
		document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter13/HelloWorld1.pdf"));
			document.open();
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
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter13/HelloWorld2.pdf"));
			document.open();
			Chapter people = new Chapter("To the People:", 2);
			Section section = people.addSection("to mothers and fathers:");
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
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter13/HelloWorld3.pdf"));
			document.open();
			Chapter animals = new Chapter("To the Animals:", 3);
			Section section = animals.addSection("to cats and dogs:");
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