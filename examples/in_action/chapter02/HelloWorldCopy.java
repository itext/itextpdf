/* in_action/chapter02/HelloWorldCopy.java */

package in_action.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Anchor;
import com.lowagie.text.Annotation;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldCopy {

	/**
	 * Generates three PDF files with plain content, annotations, a link; then
	 * copies the three files into one.
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 2: example HelloWorldCopy");
		System.out.println("-> Creates some PDF files, then copies them into one;");
		System.out.println("   some of the files contain an annotation or a link.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   Hello1.pdf");
		System.out.println("   Hello2.pdf");
		System.out.println("   Hello3.pdf");
		System.out.println("   HelloWorldPdfCopy123.pdf");
		// we create a PDF file
		createPdfs();
		try {
			// we create a PdfReader object
			PdfReader reader = new PdfReader("results/in_action/chapter02/Hello1.pdf");
			// step 1
			Document document = new Document(reader.getPageSizeWithRotation(1));
			// step 2
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(
					"results/in_action/chapter02/HelloWorldPdfCopy123.pdf"));
			// step 3
			document.open();
			// step 4
			System.out.println("Tampered? " + reader.isTampered());
			copy.addPage(copy.getImportedPage(reader, 1));
			reader = new PdfReader("results/in_action/chapter02/Hello2.pdf");
			copy.addPage(copy.getImportedPage(reader, 1));
			reader = new PdfReader("results/in_action/chapter02/Hello3.pdf");
			copy.addPage(copy.getImportedPage(reader, 1));
			System.out.println("Tampered? " + reader.isTampered());
			// step 5
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates three PDF files.
	 */
	public static void createPdfs() {
		// we create a document with multiple pages and bookmarks
		Document document;
		document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter02/Hello1.pdf"));
			document.open();
			document.add(new Paragraph("Hello World"));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter02/Hello2.pdf"));
			document.open();
			document.add(new Annotation("remark",
					"This page only contains an annotation.", 100f, 700f, 200f,
					800f));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter02/Hello3.pdf"));
			document.open();
			Anchor link = new Anchor("Go to the official iText site");
			link.setReference("http://www.lowagie.com/iText/");
			document.add(link);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}