/* in_action/chapter13/VPPageLayout.java */

package in_action.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class VPPageLayout {

	/**
	 * Generates PDF files with the text 'Hello World' with multiple pages. When
	 * opened, different preferences are used.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example VPPageLayout");
		System.out.println("-> Creates a PDF file with multiple pages");
		System.out.println("   but different layout preferences.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   single_page.pdf");
		System.out.println("   one_column.pdf");
		System.out.println("   two_column_left.pdf");
		System.out.println("   two_column_right.pdf");
		System.out.println("   two_page_left.pdf");
		System.out.println("   two_page_right.pdf");
		// we create a PDF file
		Document document = new Document();
		try {
			PdfWriter writer1 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/single_page.pdf"));
			writer1.setViewerPreferences(PdfWriter.PageLayoutSinglePage);
			PdfWriter writer2 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/one_column.pdf"));
			writer2.setViewerPreferences(PdfWriter.PageLayoutOneColumn);
			PdfWriter writer3 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/two_column_left.pdf"));
			writer3.setViewerPreferences(PdfWriter.PageLayoutTwoColumnLeft);
			PdfWriter writer4 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/two_column_right.pdf"));
			writer4.setViewerPreferences(PdfWriter.PageLayoutTwoColumnRight);
			PdfWriter writer5 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/two_page_left.pdf"));
			writer5.setPdfVersion(PdfWriter.VERSION_1_5);
			writer5.setViewerPreferences(PdfWriter.PageLayoutTwoPageLeft);
			PdfWriter writer6 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/two_page_right.pdf"));
			writer6.setPdfVersion(PdfWriter.VERSION_1_5);
			writer6.setViewerPreferences(PdfWriter.PageLayoutTwoPageRight);
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
			document.add(new Paragraph("2. to the World:"));
			document.add(hello);
			document.add(new Paragraph("3. to the Sun:"));
			document.add(hello);
			document.add(new Paragraph("4. to the Moon:"));
			document.add(hello);
			document.add(new Paragraph("5. to the Stars:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("6. To the People:"));
			document.add(hello);
			document.add(new Paragraph("7. to mothers and fathers:"));
			document.add(hello);
			document.add(new Paragraph("8. to brothers and sisters:"));
			document.add(hello);
			document.add(new Paragraph("9. to wives and husbands:"));
			document.add(hello);
			document.add(new Paragraph("10. to sons and daughters:"));
			document.add(hello);
			document.add(new Paragraph("11. to complete strangers:"));
			document.add(hello);
			document.newPage();
			document.add(new Paragraph("12. To the Animals:"));
			document.add(hello);
			document.add(new Paragraph("13. o cats and dogs:"));
			document.add(hello);
			document.add(new Paragraph("14. to birds and bees:"));
			document.add(hello);
			document.add(new Paragraph("15. to farm animals and wild animals:"));
			document.add(hello);
			document.add(new Paragraph("16. to bugs and beatles:"));
			document.add(hello);
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