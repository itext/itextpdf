/* in_action/chapter14/ChapterEvents.java */

package in_action.chapter14;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chapter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.toolbox.plugins.Concat;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ChapterEvents extends PdfPageEventHelper {

	/** The document with the ToC. */
	protected Document toc;

	/**
	 * Constructs a chapter events object.
	 */
	public ChapterEvents() {
		toc = new Document();
		try {
			PdfWriter.getInstance(toc, new FileOutputStream("results/in_action/chapter14/toc.pdf"));
			System.out.println("opening the ToC");
			toc.open();
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onChapter(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document, float, com.lowagie.text.Paragraph)
	 */
	public void onChapter(PdfWriter writer, Document document, float position,
			Paragraph title) {
		System.out.println(title.getContent());
		try {
			toc.add(new Paragraph(title.getContent() + " page "
					+ document.getPageNumber()));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onChapterEnd(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document, float)
	 */
	public void onChapterEnd(PdfWriter writer, Document document, float position) {
		try {
			toc.add(Chunk.NEWLINE);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onSection(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document, float, int, com.lowagie.text.Paragraph)
	 */
	public void onSection(PdfWriter writer, Document document, float position,
			int depth, Paragraph title) {
		try {
			switch (depth) {
			case 2:
				toc.add(new Paragraph(title.getContent(), new Font(Font.HELVETICA,
						10)));
				break;
			default:
				toc.add(new Paragraph(title.getContent(), new Font(Font.HELVETICA,
						8)));
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPageEvent#onCloseDocument(com.lowagie.text.pdf.PdfWriter,
	 *      com.lowagie.text.Document)
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		System.out.println("Closing the ToC");
		toc.close();
	}

	/**
	 * Generates a PDF file, then reads it to retrieve the bookmarks.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 14: example ChapterEvents");
		System.out.println("-> Creates a PDF file with chapters/sections");
		System.out.println("   and a ToC.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("->              iText-toolbox.jar");
		System.out.println("-> file generated in /results subdirectory:");
		System.out.println("   toc.pdf, chapter_events.pdf and toc_chapters.pdf");
		// we create a PDF file
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter14/chapter_events.pdf"));
			writer.setPageEvent(new ChapterEvents());
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
			Section planets = universe.addSection("to the Planets:");
			planets.add(hello);
			section = planets.addSection("to Venus:");
			section.add(hello);
			section = planets.addSection("to Mercury:");
			section.add(hello);
			section = planets.addSection("to Mars:");
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

		String[] arguments = { "results/in_action/chapter14/toc.pdf",
				"results/in_action/chapter14/chapter_events.pdf",
				"results/in_action/chapter14/toc_chapters.pdf" };
		Concat.main(arguments);
	}
}