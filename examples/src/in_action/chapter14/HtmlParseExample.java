/* in_action/chapter14/HtmlParseExample.java */

package in_action.chapter14;

import java.io.FileOutputStream;

import com.lowagie.text.*;
import com.lowagie.text.html.HtmlParser;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HtmlParseExample {

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
		System.out.println("   html1.pdf");

		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter14/html1.pdf"));
			HtmlParser.parse(document, "resources/in_action/chapter14/example.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}