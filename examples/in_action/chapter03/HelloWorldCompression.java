/* in_action/chapter03/HelloWorldCompression.java */

package in_action.chapter03;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldCompression {

	/**
	 * Generates a PDF file, then reads it to generate a copy that is fully
	 * compressed and one that isn't compressed.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 3: example HelloWorldCompression");
		System.out.println("-> Creates a PDF file, then reads it;");
		System.out.println("   a fully compressed and an uncompressed copy are generated.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldCompressed.pdf");
		System.out.println("   HelloWorldFullCompression.pdf");
		System.out.println("   HelloWorldDecompressed.pdf");
		// we create a PDF file
		createPdf("results/in_action/chapter03/HelloWorldCompressed.pdf");
		// now we are going to inspect it
		try {
			// we create a PdfReader object
			PdfReader reader;
			PdfStamper stamper;
			// we set full compression
			reader = new PdfReader("results/in_action/chapter03/HelloWorldCompressed.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter03/HelloWorldFullCompression.pdf"), PdfWriter.VERSION_1_5);
			stamper.setFullCompression();
			stamper.close();
			// we remove compression
			reader = new PdfReader("results/in_action/chapter03/HelloWorldCompressed.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter03/HelloWorldDecompressed.pdf"), '1');
			Document.compress = false;
			int total = reader.getNumberOfPages() + 1;
			for (int i = 1; i < total; i++) {
				reader.setPageContent(i, reader.getPageContent(i));
			}
			stamper.close();
			// we compare the file sizes:
			showFileSize("results/in_action/chapter03/HelloWorldCompressed.pdf");
			showFileSize("results/in_action/chapter03/HelloWorldFullCompression.pdf");
			showFileSize("results/in_action/chapter03/HelloWorldDecompressed.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the filesize.
	 * 
	 * @param filename
	 *            the file of which we want to know the size
	 * @throws IOException
	 */
	private static void showFileSize(String filename) throws IOException {
		PdfReader reader = new PdfReader(filename);
		System.out.print("Size ");
		System.out.print(filename);
		System.out.print(": ");
		System.out.println(reader.getFileLength());
	}

	/**
	 * Generates a PDF.
	 * 
	 * @param filename
	 *            the filename of the PDF file.
	 */
	private static void createPdf(String filename) {
		// we create a document with multiple pages and bookmarks
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			Paragraph hello = new Paragraph(
					"(English:) hello, "
							+ "(Esperanto:) he, alo, saluton, (Latin:) heu, ave, "
							+ "(French:) allô, (Italian:) ciao, (German:) hallo, he, heda, holla, "
							+ "(Portuguese:) alô, olá, hei, psiu, bom día, (Dutch:) hallo, dag, "
							+ "(Spanish:) ola, eh, (Catalan:) au, bah, eh, ep, "
							+ "(Swedish:) hej, hejsan(Danish:) hallo, dav, davs, goddag, hej, "
							+ "(Norwegian:) hei; morn, (Papiamento:) halo; hallo; kí tal, "
							+ "(Faeroese:) halló, hoyr, (Turkish:) alo, merhaba, (Albanian:) tungjatjeta");
			document.add(new Paragraph("Hello World"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("Hello People"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("Hello Sun"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("Hello Moon"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("Hello Clouds"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("Hello Stars"));
			document.add(hello);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}