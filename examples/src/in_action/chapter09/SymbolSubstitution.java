/* in_action/chapter09/SymbolSubstitution.java */

package in_action.chapter09;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SymbolSubstitution {

	/**
	 * Generates a PDF file with fonts from the fontfactory.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 9: example SymbolSubstitution");
		System.out.println("-> Creates a PDF file that uses a special Phrase constructor.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: symbol_substitution.pdf");

		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(
					"results/in_action/chapter09/symbol_substitution.pdf"));

			// step 3: we open the document
			document.open();
			// step 4:
			String text = "What is the " + (char) 945 + "-coefficient of the "
					+ (char) 946 + "-factor in the " + (char) 947
					+ "-equation?";
			document.add(Phrase.getInstance(text));
			document.add(Chunk.NEWLINE);
			for (int i = 913; i < 970; i++) {
				document.add(Phrase.getInstance(String.valueOf(i) + ": "
						+ (char) i + " "));
			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

}
