/* in_action/chapter14/ParsingHtmlSnippets.java */

package in_action.chapter14;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
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

public class ParsingHtmlSnippets {

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
		System.out.println("-> resource needed: list.html");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   html3.pdf");

		Document document = new Document();
		try {
			StyleSheet styles = new StyleSheet();
			styles.loadTagStyle("ol", "leading", "16,0");
			PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter14/html3.pdf"));
			document.open();
			ArrayList objects;
			objects = HTMLWorker.parseToList(new FileReader(
					"resources/in_action/chapter14/list.html"), styles);
			for (int k = 0; k < objects.size(); ++k)
				document.add((Element) objects.get(k));
			FontFactory.register("c:\\windows\\fonts\\gara.ttf");
			styles.loadTagStyle("li", "face", "garamond");
			styles.loadTagStyle("span", "size", "8px");
			objects = HTMLWorker.parseToList(new FileReader(
					"resources/in_action/chapter14/list.html"), styles);
			for (int k = 0; k < objects.size(); ++k)
				document.add((Element) objects.get(k));
			styles.loadStyle("sf", "color", "blue");
			styles.loadStyle("sf", "b", "");
			styles.loadStyle("classic", "color", "red");
			styles.loadStyle("classic", "i", "");
			objects = HTMLWorker.parseToList(new FileReader(
					"resources/in_action/chapter14/list.html"), styles);
			for (int k = 0; k < objects.size(); ++k)
				document.add((Element) objects.get(k));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		document.close();
	}
}