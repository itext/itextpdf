/* in_action/chapter04/FoxDogList1.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogList1 {

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy
	 * dog'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogList1");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is added using List objects.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_list1.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_list1.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Phrase phrase = new Phrase("Quick brown fox jumps over");
			document.add(phrase);
			List list1 = new List(List.ORDERED, 20);
			list1.add(new ListItem("the lazy dog"));
			list1.add(new ListItem("the lazy cat"));
			list1.add(new ListItem("the fence"));
			document.add(list1);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			List list2 = new List(List.UNORDERED, 10);
			list2.add("the lazy dog");
			list2.add("the lazy cat");
			list2.add("the fence");
			document.add(list2);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			List list3 = new List(List.ORDERED, List.ALPHABETICAL, 20);
			list3.add(new ListItem("the lazy dog"));
			list3.add(new ListItem("the lazy cat"));
			list3.add(new ListItem("the fence"));
			document.add(list3);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			List list4 = new List(List.UNORDERED, 30);
			list4.setListSymbol("----->");
			list4.setIndentationLeft(10);
			list4.add("the lazy dog");
			list4.add("the lazy cat");
			list4.add("the fence");
			document.add(list4);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			List list5 = new List(List.ORDERED, 20);
			list5.setFirst(11);
			list5.add(new ListItem("the lazy dog"));
			list5.add(new ListItem("the lazy cat"));
			list5.add(new ListItem("the fence"));
			document.add(list5);
			document.add(Chunk.NEWLINE);
			List list = new List(List.UNORDERED, 10);
			list.setListSymbol(new Chunk('*'));
			list.add("Quick brown fox jumps over");
			list.add(list1);
			list.add("Quick brown fox jumps over");
			list.add(list3);
			list.add("Quick brown fox jumps over");
			list.add(list5);
			document.add(list);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}