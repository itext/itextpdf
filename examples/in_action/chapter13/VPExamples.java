/* in_action/chapter13/VPExamples.java */

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

public class VPExamples {

	/**
	 * Generates a PDF file with the text 'Hello World' with multiple pages.
	 * When opened, different preferences are used.
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
		System.out.println("   hide_menu_center_window.pdf");
		System.out.println("   no_ui_fit_window.pdf");
		System.out.println("   display_title_two_page_left.pdf");
		System.out.println("   no_toolbar_use_thumbs.pdf");
		// we create a PDF file
		Document document = new Document();
		try {
			PdfWriter writer1 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/hide_menu_center_window.pdf"));
			writer1.setViewerPreferences(PdfWriter.HideMenubar
					| PdfWriter.CenterWindow);
			PdfWriter writer2 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/no_ui_fit_window.pdf"));
			writer2.setViewerPreferences(PdfWriter.HideWindowUI
					| PdfWriter.FitWindow);
			PdfWriter writer3 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/display_title_two_page_left.pdf"));
			writer3.setPdfVersion(PdfWriter.VERSION_1_5);
			writer3.setViewerPreferences(PdfWriter.DisplayDocTitle
					| PdfWriter.PageLayoutTwoPageLeft);
			PdfWriter writer4 = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/no_toolbar_use_thumbs.pdf"));
			writer4.setViewerPreferences(PdfWriter.HideToolbar
					| PdfWriter.PageModeUseThumbs);
			document.addTitle("Hello World in different languages");
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
			document
					.add(new Paragraph("15. to farm animals and wild animals:"));
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