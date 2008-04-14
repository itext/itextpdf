/* in_action/chapter13/VPPageModeAndLayout.java */

package in_action.chapter13;

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
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class VPPageModeAndLayout {

	/**
	 * Generates PDF files with the text 'Hello World' with multiple pages. When
	 * opened, different preferences are used.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example VPPageModeAndLayout");
		System.out.println("-> Creates a PDF file with multiple pages");
		System.out.println("   and a certain page mode and layout.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   page_mode_and_layout.pdf");
		System.out.println("   full_screen.pdf");
		// we create a PDF file
		Document document = new Document();
		try {
			PdfWriter writer1 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/page_mode_and_layout.pdf"));
			writer1.setViewerPreferences(PdfWriter.PageModeUseOutlines
					| PdfWriter.PageLayoutTwoColumnRight);
			document.open();
			PdfWriter writer2 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/full_screen.pdf"));
			writer2.setViewerPreferences(PdfWriter.PageModeFullScreen
					| PdfWriter.NonFullScreenPageModeUseOutlines);
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