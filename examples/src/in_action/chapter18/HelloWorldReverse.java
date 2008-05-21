/* in_action/chapter18/HelloWorldReverse.java */

package in_action.chapter18;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PRTokeniser;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldReverse {

	/**
	 * Generates a PDF file with the text 'Hello World'
	 * @param args no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 18: example HelloWorldReverse");
		System.out.println("-> Creates a PDF file with the text 'Hello World';");
		System.out.println("   the text is added at absolute positions in reverse order;");
		System.out.println("   the stream is inspected afterwards.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldReverse.pdf");
		Document document = new Document(PageSize.A6);
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream("results/in_action/chapter18/HelloWorldReverse.pdf"));
			document.open();
			// we add the text to the direct content, but not in the right order
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.beginText();
            cb.setFontAndSize(bf, 12);
            cb.moveText(88.66f, 367); 
            cb.showText("ld");
            cb.moveText(-22f, 0); 
            cb.showText("Wor");
            cb.moveText(-15.33f, 0); 
            cb.showText("llo");
            cb.moveText(-15.33f, 0); 
            cb.showText("He");
            cb.endText();
            PdfTemplate tmp = cb.createTemplate(250, 25);
            tmp.beginText();
            tmp.setFontAndSize(bf, 12);
            tmp.moveText(0, 7);
            tmp.showText("Hello People");
            tmp.endText();
            cb.addTemplate(tmp, 36, 343);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		
		// now we are going to read the file and extract the content stream
		try {
			// creating a reader object that will read the file we created earlier
			PdfReader reader = new PdfReader("results/in_action/chapter18/HelloWorldReverse.pdf");
			// we can inspect the syntax of the imported page
			PdfDictionary page = reader.getPageN(1);
			PRIndirectReference objectReference = (PRIndirectReference) page.get(PdfName.CONTENTS);
			System.out.println("=== inspecting the stream of page 1 in object " +
					objectReference.getNumber() + " ===");
			PRStream stream = (PRStream) PdfReader.getPdfObject(objectReference);
			byte[] streamBytes = PdfReader.getStreamBytes(stream);
			System.out.println(new String(streamBytes));
			System.out.println("=== extracting the strings from the stream ===");
			PRTokeniser tokenizer = new PRTokeniser(streamBytes);
			while (tokenizer.nextToken()) {
				if (tokenizer.getTokenType() == PRTokeniser.TK_STRING) {
					System.out.println(tokenizer.getStringValue());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
