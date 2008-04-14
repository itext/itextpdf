/* in_action/chapterF/HelloWorldReadMetadata.java */

package in_action.chapterF;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Iterator;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldReadMetadata {

	/**
	 * Generates a PDF file, then reads it.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Appendix F: example HelloWorldReadMetadata");
		System.out.println("-> Creates a PDF file, then reads it;");
		System.out.println("   the metadata, some document info and");
		System.out.println("   some page sizes are retrieved.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldToRead.pdf");
		// we create a PDF file
		createPdf("results/in_action/chapterF/HelloWorldToRead.pdf");
		// now we are going to inspect it
		try {
			// we create a PdfReader object
			PdfReader reader = new PdfReader("results/in_action/chapterF/HelloWorldToRead.pdf");
			// Some metadata
			System.out.println("=== Metadata ===");
			Map info = reader.getInfo();
			String key;
			String value;
			for (Iterator i = info.keySet().iterator(); i.hasNext();) {
				key = (String) i.next();
				value = (String) info.get(key);
				System.out.println(key + ": " + value);
			}
			if (reader.getMetadata() == null) {
				System.out.println("No XML Metadata.");
			} else {
				System.out.println("XML Metadata: "
						+ new String(reader.getMetadata()));
			}
		} catch (IOException e) {
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
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(filename));
			document.addTitle("Hello World example");
			document.addSubject("This example shows how to add metadata");
			document.addKeywords("Metadata, iText, step 3");
			document.addCreator("My program using iText");
			document.addAuthor("Bruno Lowagie");
			writer.createXmpMetadata();
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