/* in_action/chapter13/ExplicitDestinations.java */

package in_action.chapter13;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ExplicitDestinations {

	/**
	 * Generates a file with some explicit destinations that can be reached
	 * through the outline tree.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example Explicit Destinations");
		System.out.println("-> Creates a PDF file with some explicit destinations");
		System.out.println("   that can be reached through the outline tree.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   explicit_destinations.pdf");
		System.out.println("   explicit_destinations.xml");
		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/explicit_destinations.pdf"));
			// step 3:
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			document.open();
			// step 4: we grab the ContentByte and do some stuff with it
			PdfContentByte cb = writer.getDirectContent();

			// we create a PdfTemplate
			PdfTemplate template = cb.createTemplate(25, 25);

			// we add some crosses to visualize the destinations
			template.moveTo(13, 0);
			template.lineTo(13, 25);
			template.moveTo(0, 13);
			template.lineTo(50, 13);
			template.stroke();

			// we add the template on different positions
			cb.addTemplate(template, 287, 787);
			cb.addTemplate(template, 187, 487);
			cb.addTemplate(template, 487, 287);
			cb.addTemplate(template, 87, 87);

			// we define the destinations
			PdfDestination d1 = new PdfDestination(PdfDestination.XYZ, 300,
					800, 0);
			PdfDestination d2 = new PdfDestination(PdfDestination.FITH, 500);
			PdfDestination d3 = new PdfDestination(PdfDestination.FITR, 200,
					300, 400, 500);
			PdfDestination d4 = new PdfDestination(PdfDestination.FITBV, 100);
			PdfDestination d5 = new PdfDestination(PdfDestination.FIT);

			// we define the outlines
			PdfOutline root = cb.getRootOutline();
			PdfOutline out1 = new PdfOutline(root, d1, "root", true);
			PdfOutline out2 = new PdfOutline(out1, d2, "sub 1", false);
			new PdfOutline(out1, d3, "sub 2");
			new PdfOutline(out2, d4, "sub 2.1");
			new PdfOutline(out2, d5, "sub 2.2");
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		try {
			PdfReader reader = new PdfReader("results/in_action/chapter13/explicit_destinations.pdf");
			List list = SimpleBookmark.getBookmark(reader);
			SimpleBookmark.exportToXML(list, new FileOutputStream(
					"results/in_action/chapter13/explicit_destinations.xml"), "ISO8859-1", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}