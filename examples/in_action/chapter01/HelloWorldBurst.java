/* in_action/chapter01/HelloWorldBurst.java */

package in_action.chapter01;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie.
 * It is part of the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class HelloWorldBurst {

	/**
	 * Generates a PDF file, then cuts it into single pages.
	 * @param args no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 1: example HelloWorldBurst");
		System.out.println("-> Creates a PDF file, then 'bursts' it");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("->              iText-toolbox.jar");
		System.out.println("-> files generated in /results subdirectory:");	
		System.out.println("   HelloWorldRead.pdf (will be used as input)");
		System.out.println("   HelloWorldRead_1.pdf (first page of HelloWorld.pdf)");
		System.out.println("   HelloWorldRead_2.pdf (second page of HelloWorld.pdf)");
		System.out.println("   HelloWorldRead_3.pdf (third page of HelloWorld.pdf)");	
		// we create a PDF file
		createPdf("results/in_action/chapter01/HelloWorldRead.pdf");
		String[] arg = {"results/in_action/chapter01/HelloWorldRead.pdf"};
		com.lowagie.toolbox.plugins.Burst.main(arg);
	}
	
	/**
	 * Generates a PDF file with bookmarks.
	 * @param filename the filename of the PDF file.
	 */
	private static void createPdf(String filename) {
		// we create a document with multiple pages and bookmarks
		Document document = new Document();
		try {
			PdfWriter.getInstance(document,	new FileOutputStream(filename));
			document.open();
			Paragraph hello = new Paragraph("(English:) hello, " +
					"(Esperanto:) he, alo, saluton, (Latin:) heu, ave, " +
					"(French:) all\u00f4, (Italian:) ciao, (German:) hallo, he, heda, holla, " +
					"(Portuguese:) al\u00f4, ol\u00e1, hei, psiu, bom d\u00eda, (Dutch:) hallo, dag, " +
					"(Spanish:) ola, eh, (Catalan:) au, bah, eh, ep, " +
					"(Swedish:) hej, hejsan(Danish:) hallo, dav, davs, goddag, hej, " +
					"(Norwegian:) hei; morn, (Papiamento:) halo; hallo; k\u00ed tal, " +
					"(Faeroese:) hall\u00f3, hoyr, (Turkish:) alo, merhaba, (Albanian:) tungjatjeta");
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