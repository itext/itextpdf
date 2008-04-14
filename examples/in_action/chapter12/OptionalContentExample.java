/* in_action/chapter12/OptionalContentExample.java */

package in_action.chapter12;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfLayer;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class OptionalContentExample {

	/**
	 * A more complex example with optional content.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: Optional Content Example");
		System.out.println("-> Creates a PDF with optional content.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: optional_content.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter12/optional_content.pdf"));
			writer.setPdfVersion(PdfWriter.VERSION_1_5);
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();

			PdfLayer nested = new PdfLayer("Nested Layers", writer);
			PdfLayer nested_1 = new PdfLayer("Nested Layer 1", writer);
			PdfLayer nested_2 = new PdfLayer("Nested Layer 2", writer);
			nested.addChild(nested_1);
			nested.addChild(nested_2);
			cb.beginLayer(nested);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"nested layers"), 50, 775, 0);
			cb.endLayer();
			cb.beginLayer(nested_1);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"nested layer 1"), 100, 800, 0);
			cb.endLayer();
			cb.beginLayer(nested_2);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"nested layer 2"), 100, 750, 0);
			cb.endLayer();

			PdfLayer group = PdfLayer.createTitle("Grouped layers", writer);
			PdfLayer layer1 = new PdfLayer("Group: layer 1", writer);
			PdfLayer layer2 = new PdfLayer("Group: layer 2", writer);
			group.addChild(layer1);
			group.addChild(layer2);
			cb.beginLayer(layer1);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"layer 1 in the group"), 50, 700, 0);
			cb.endLayer();
			cb.beginLayer(layer2);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"layer 2 in the group"), 50, 675, 0);
			cb.endLayer();

			PdfLayer radiogroup = PdfLayer.createTitle("Radio Group", writer);
			PdfLayer radio1 = new PdfLayer("Radiogroup: layer 1", writer);
			radio1.setOn(true);
			PdfLayer radio2 = new PdfLayer("Radiogroup: layer 2", writer);
			radio2.setOn(false);
			PdfLayer radio3 = new PdfLayer("Radiogroup: layer 3", writer);
			radio3.setOn(false);
			radiogroup.addChild(radio1);
			radiogroup.addChild(radio2);
			radiogroup.addChild(radio3);
			ArrayList options = new ArrayList();
			options.add(radio1);
			options.add(radio2);
			options.add(radio3);
			writer.addOCGRadioGroup(options);
			cb.beginLayer(radio1);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"option 1"), 50, 600, 0);
			cb.endLayer();
			cb.beginLayer(radio2);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"option 2"), 50, 575, 0);
			cb.endLayer();
			cb.beginLayer(radio3);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"option 3"), 50, 550, 0);
			cb.endLayer();

			PdfLayer not_printed = new PdfLayer("not printed", writer);
			not_printed.setOnPanel(false);
			not_printed.setPrint("Print", false);
			cb.beginLayer(not_printed);
			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(
					"PRINT THIS PAGE"), 300, 700, 90);
			cb.endLayer();

			PdfLayer zoom = new PdfLayer("Zoom 0.75-1.25", writer);
			zoom.setOnPanel(false);
			zoom.setZoom(0.75f, 1.25f);
			cb.beginLayer(zoom);
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(
					"Only visible if the zoomfactor is between 75 and 125%"),
					30, 530, 90);
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
