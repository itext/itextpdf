/* in_action/chapter04/FoxDogGeneric3.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.events.IndexEvents;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie based on a
 * code contribution by Michael Niedermair.
 * This example is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogGeneric3 extends PdfPageEventHelper {

	/**
	 * Index
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(final String[] args) {
		System.out.println("Chapter 4: example FoxDogGeneric3");
		System.out.println("-> Creates a PDF file with some text");
		System.out.println("   of which some words are indexed automatically.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_generic3.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer that listens to the document
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter04/fox_dog_generic3.pdf"));
			IndexEvents index = new IndexEvents();
			writer.setPageEvent(index);
			// step 3: we open the document
			document.open();
			// step 4:
			create_0(document, index);
			create_1(document, index);
			document.newPage();
			create_1(document, index);
			document.newPage();
			create_2(document, index);
			document.newPage();
			create_3(document, index);
			document.newPage();
			create_1(document, index);
			document.newPage();
			// we add the index
			document.add(new Paragraph("Index:"));
			List list = index.getSortedEntries();
			for (int i = 0, n = list.size(); i < n; i++) {
				IndexEvents.Entry entry = (IndexEvents.Entry) list.get(i);
				Paragraph in = new Paragraph();
				in.add(new Chunk(entry.getIn1()));
				if (entry.getIn2().length() > 0) {
					in.add(new Chunk("; " + entry.getIn2()));
				}
				if (entry.getIn3().length() > 0) {
					in.add(new Chunk(" (" + entry.getIn3() + ")"));
				}
				List pages = entry.getPagenumbers();
				List tags = entry.getTags();
				in.add(": ");
				for (int p = 0, x = pages.size(); p < x; p++) {
					Chunk pagenr = new Chunk(" p" + pages.get(p));
					pagenr.setLocalGoto((String) tags.get(p));
					in.add(pagenr);
				}
				document.add(in);
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	/**
	 * Create ...
	 * 
	 * @param document
	 * @param index
	 * @throws DocumentException
	 */
	private static void create_0(Document document, IndexEvents index)
			throws DocumentException {
		Paragraph p = new Paragraph("Quick brown fox ");
		p.add(index.create("jumps", "Jump"));
		p.add(" over the lazy dog.");
		document.add(p);
	}

	/**
	 * Create ...
	 * 
	 * @param document
	 * @param index
	 * @throws DocumentException
	 */
	private static void create_1(Document document, IndexEvents index)
			throws DocumentException {
		Paragraph p = new Paragraph();
		p.add(index.create("Quick brown fox", "Fox", "quick, brown"));
		p.add(new Chunk(" jumps over "));
		p.add(index.create("the lazy dog.", "Dog", "lazy"));
		p.add(index.create(" ", "Jumping"));
		document.add(p);
	}

	/**
	 * Create ...
	 * 
	 * @param document
	 * @param index
	 * @throws DocumentException
	 */
	private static void create_2(Document document, IndexEvents index)
			throws DocumentException {
		Paragraph p = new Paragraph();
		p.add(new Chunk("The fox is "));
		p.add(index.create("brown", "Color", "brown"));
		p.add(index.create(" ", "Brown", "color", "see Color; brown"));
		p.add(Chunk.NEWLINE);
		document.add(p);
	}

	/**
	 * Create ...
	 * 
	 * @param document
	 * @param index
	 * @throws DocumentException
	 */
	private static void create_3(Document document, IndexEvents index)
			throws DocumentException {
		Paragraph p = new Paragraph();
		p.add(new Chunk("The dog is "));
		p.add(index.create("yellow", "Color", "yellow"));
		p.add(index.create(" ", "Yellow", "color", "see Color; yellow"));
		p.add(Chunk.NEWLINE);
		document.add(p);
	}
}