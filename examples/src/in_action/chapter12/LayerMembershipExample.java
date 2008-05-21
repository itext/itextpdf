/* in_action/chapter12/LayerMembershipExample.java */

package in_action.chapter12;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfLayer;
import com.lowagie.text.pdf.PdfLayerMembership;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class LayerMembershipExample {

	/**
	 * A more complex example with optional content.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: Layer Membership Example");
		System.out.println("-> Creates a PDF with optional content.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: layer_membership.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter12/layer_membership.pdf"));
			writer.setPdfVersion(PdfWriter.VERSION_1_5);
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();

			PdfLayer dog = new PdfLayer("layer 1", writer);
			PdfLayer tiger = new PdfLayer("layer 2", writer);
			PdfLayer lion = new PdfLayer("layer 3", writer);
			PdfLayerMembership cat = new PdfLayerMembership(writer);
			cat.addMember(tiger);
			cat.addMember(lion);
			PdfLayerMembership no_cat = new PdfLayerMembership(writer);
			no_cat.addMember(tiger);
			no_cat.addMember(lion);
			no_cat.setVisibilityPolicy(PdfLayerMembership.ALLOFF);
			cb.beginLayer(dog);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
					new Phrase("dog"), 50, 775, 0);
			cb.endLayer();
			cb.beginLayer(tiger);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"tiger"), 50, 750, 0);
			cb.endLayer();
			cb.beginLayer(lion);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"lion"), 50, 725, 0);
			cb.endLayer();
			cb.beginLayer(cat);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
					new Phrase("cat"), 50, 700, 0);
			cb.endLayer();
			cb.beginLayer(no_cat);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"no cat"), 50, 700, 0);
			cb.endLayer();

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
