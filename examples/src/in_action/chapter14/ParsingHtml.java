/* in_action/chapter14/ParsingHtml.java */

package in_action.chapter14;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ParsingHtml {

	/**
	 * Generates a PDF file with the text 'Hello World'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 14: HTML parse example");
		System.out.println("-> Parses an HTML file into PDF.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resource needed: example.html");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   html2.pdf");

		Document document = new Document();
		StyleSheet st = new StyleSheet();
		st.loadTagStyle("body", "leading", "16,0");
		try {
			PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter14/html2.pdf"));
			document.open();
			ArrayList p = HTMLWorker.parseToList(new FileReader(
					"resources/in_action/chapter14/example.html"), st);
			for (int k = 0; k < p.size(); ++k)
				document.add((Element) p.get(k));
		} catch (Exception e) {
			e.printStackTrace();
		}
		document.close();
	}
}