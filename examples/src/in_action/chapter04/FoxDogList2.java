/* in_action/chapter04/FoxDogList2.java */

package in_action.chapter04;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.GreekList;
import com.lowagie.text.ListItem;
import com.lowagie.text.Phrase;
import com.lowagie.text.RomanList;
import com.lowagie.text.ZapfDingbatsList;
import com.lowagie.text.ZapfDingbatsNumberList;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogList2 {

	/**
	 * Generates a PDF file with the text 'Quick brown fox jumps over the lazy
	 * dog'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogList2");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is added using List objects.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_list2.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_list2.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Phrase phrase = new Phrase("Quick brown fox jumps over");
			document.add(phrase);
			RomanList romanlist;
			romanlist = new RomanList(20);
			romanlist.setLowercase(true);
			romanlist.add(new ListItem("the lazy dog"));
			romanlist.add(new ListItem("the lazy cat"));
			document.add(romanlist);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			romanlist = new RomanList(20);
			romanlist.setLowercase(false);
			romanlist.add(new ListItem("the lazy dog"));
			romanlist.add(new ListItem("the lazy cat"));
			document.add(romanlist);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			GreekList greeklist;
			greeklist = new GreekList(20);
			greeklist.setLowercase(true);
			greeklist.add(new ListItem("the lazy dog"));
			greeklist.add(new ListItem("the lazy cat"));
			document.add(greeklist);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			greeklist = new GreekList(20);
			greeklist.setLowercase(false);
			greeklist.add(new ListItem("the lazy dog"));
			greeklist.add(new ListItem("the lazy cat"));
			document.add(greeklist);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			ZapfDingbatsList zapfdingbatslist;
			zapfdingbatslist = new ZapfDingbatsList(42, 15);
			zapfdingbatslist.add(new ListItem("the lazy dog"));
			zapfdingbatslist.add(new ListItem("the lazy cat"));
			document.add(zapfdingbatslist);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			ZapfDingbatsNumberList zapfdingbatsnumberlist;
			zapfdingbatsnumberlist = new ZapfDingbatsNumberList(0, 15);
			zapfdingbatsnumberlist.add(new ListItem("the lazy dog"));
			zapfdingbatsnumberlist.add(new ListItem("the lazy cat"));
			document.add(zapfdingbatsnumberlist);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			zapfdingbatsnumberlist = new ZapfDingbatsNumberList(1, 15);
			zapfdingbatsnumberlist.add(new ListItem("the lazy dog"));
			zapfdingbatsnumberlist.add(new ListItem("the lazy cat"));
			document.add(zapfdingbatsnumberlist);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			zapfdingbatsnumberlist = new ZapfDingbatsNumberList(2, 15);
			zapfdingbatsnumberlist.add(new ListItem("the lazy dog"));
			zapfdingbatsnumberlist.add(new ListItem("the lazy cat"));
			document.add(zapfdingbatsnumberlist);
			document.add(Chunk.NEWLINE);
			document.add(phrase);
			zapfdingbatsnumberlist = new ZapfDingbatsNumberList(3, 15);
			zapfdingbatsnumberlist.add(new ListItem("the lazy dog"));
			zapfdingbatsnumberlist.add(new ListItem("the lazy cat"));
			document.add(zapfdingbatsnumberlist);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}