/* in_action/chapter02/HelloWorldSelectPages.java */

package in_action.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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

public class HelloWorldSelectPages {

	/**
	 * Generates a PDF file with the text 'Hello World' with multiple pages.
	 * Then copies a selection of pages to new PDF files.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 2: example HelloWorldSelectPages");
		System.out.println("-> Creates a PDF file with multiple pages;");
		System.out.println("   then copies a selection of pages to new PDF files.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldMultiplePages.pdf");
		System.out.println("   HelloWorldSelectPagesOdd.pdf");
		System.out.println("   HelloWorldSelectPagesEven.pdf");
		System.out.println("   HelloWorldSelectPages12379.pdf");
		// we create a PDF file
		createPdf("results/in_action/chapter02/HelloWorldMultiplePages.pdf");
		// now we are going to inspect it
		try {
			// we create a PdfReader object
			PdfReader reader = new PdfReader("results/in_action/chapter02/HelloWorldMultiplePages.pdf");
			reader.selectPages("o");
			int pages = reader.getNumberOfPages();
			Document document = new Document();
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(
					"results/in_action/chapter02/HelloWorldSelectPagesOdd.pdf"));
			document.open();
			for (int i = 0; i < pages;) {
				++i;
				copy.addPage(copy.getImportedPage(reader, i));
			}
			document.close();
			reader = new PdfReader("results/in_action/chapter02/HelloWorldMultiplePages.pdf");
			reader.selectPages("e");
			pages = reader.getNumberOfPages();
			document = new Document();
			copy = new PdfCopy(document, new FileOutputStream(
					"results/in_action/chapter02/HelloWorldSelectPagesEven.pdf"));
			document.open();
			for (int i = 0; i < pages;) {
				++i;
				copy.addPage(copy.getImportedPage(reader, i));
			}
			document.close();
			reader = new PdfReader("results/in_action/chapter02/HelloWorldMultiplePages.pdf");
			reader.selectPages("1-3, 7-9, !8");
			pages = reader.getNumberOfPages();
			document = new Document();
			copy = new PdfCopy(document, new FileOutputStream(
					"results/in_action/chapter02/HelloWorldSelectPages12379.pdf"));
			document.open();
			for (int i = 0; i < pages;) {
				++i;
				copy.addPage(copy.getImportedPage(reader, i));
			}
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a PDF file with multiple pages.
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
			Paragraph hello = new Paragraph("(English:) hello, " +
					"(Esperanto:) he, alo, saluton, (Latin:) heu, ave, " +
					"(French:) all\u00f4, (Italian:) ciao, (German:) hallo, he, heda, holla, " +
					"(Portuguese:) al\u00f4, ol\u00e1, hei, psiu, bom d\u00eda, (Dutch:) hallo, dag, " +
					"(Spanish:) ola, eh, (Catalan:) au, bah, eh, ep, " +
					"(Swedish:) hej, hejsan(Danish:) hallo, dav, davs, goddag, hej, " +
					"(Norwegian:) hei; morn, (Papiamento:) halo; hallo; k\u00ed tal, " +
					"(Faeroese:) hall\u00f3, hoyr, (Turkish:) alo, merhaba, (Albanian:) tungjatjeta");
			document.add(new Paragraph("1. To the Universe:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("2. to the World:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("3. to the Sun:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("4. to the Moon:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("5. to the Stars:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("6. To the People:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("7. to mothers and fathers:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("8. to brothers and sisters:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("9. to wives and husbands:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("10. to sons and daughters:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("11. to complete strangers:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("12. To the Animals:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("13. o cats and dogs:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("14. to birds and bees:"));
			document.add(hello);
			document.newPage();
			document
					.add(new Paragraph("15. to farm animals and wild animals:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("16. to bugs and beatles:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("17. to fish and shellfish:"));
			document.add(hello);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
	}
}