/* in_action/chapter18/HelloWorldStream.java */

package in_action.chapter18;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PRTokeniser;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldStream {

	/**
	 * Generates a PDF file with the text 'Hello World' and then reads it to
	 * look what it's like internally.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 18: example HelloWorldStream");
		System.out.println("-> Creates a PDF file and then inspects the content stream.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldStream.pdf and HelloWorldStreamHacked.pdf");

		// we create a PDF file
		createPdf("results/in_action/chapter18/HelloWorldStream.pdf");

		// now we are going to read the file and extract the content stream
		try {
			// creating a reader object that will read the file we created
			// earlier
			PdfReader reader = new PdfReader("results/in_action/chapter18/HelloWorldStream.pdf");
			// we can inspect the syntax of the imported page
			PdfDictionary page = reader.getPageN(1);
			PRIndirectReference objectReference = (PRIndirectReference) page
					.get(PdfName.CONTENTS);
			System.out.println("=== inspecting the stream of page 1 in object "
					+ objectReference.getNumber() + " ===");
			PRStream stream = (PRStream) PdfReader
					.getPdfObject(objectReference);
			byte[] streamBytes = PdfReader.getStreamBytes(stream);
			String contentStream = new String(streamBytes);
			System.out.println(contentStream);
			// we can retrieve the String sections of the content stream
			System.out
					.println("=== extracting the strings from the stream ===");
			PRTokeniser tokenizer = new PRTokeniser(streamBytes);
			while (tokenizer.nextToken()) {
				if (tokenizer.getTokenType() == PRTokeniser.TK_STRING) {
					System.out.println(tokenizer.getStringValue());
				}
			}
			// we do some dirty hacking, replacing Hello World by something else
			StringBuffer buf = new StringBuffer();
			int pos = contentStream.indexOf("Hello World") + 11;
			buf.append(contentStream.substring(0, pos));
			buf.append(", Hello Sun, Hello Moon, Hello Stars, Hello Universe");
			buf.append(contentStream.substring(pos));
			String hackedContentStream = buf.toString();
			// we create a new PDF file that contains the hacked stream
			Document document = new Document(PageSize.A6);
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter18/HelloWorldStreamHacked.pdf"));
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			cb.setLiteral(hackedContentStream);
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a PDF file.
	 * 
	 * @param filename
	 *            the filename of the PDF that should be created
	 */
	private static void createPdf(String filename) {
		Document document = new Document(PageSize.A6);
		try {
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			document.add(new Paragraph("Hello World"));
			document.add(new Paragraph("Hello People"));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}