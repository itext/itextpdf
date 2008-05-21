/* in_action/chapter12/OptionalContentActionExample.java */

package in_action.chapter12;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
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

public class OptionalContentActionExample {

	/**
	 * A simple example with optional content.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: OptionalContentActionExample");
		System.out.println("-> Creates a PDF with optional content.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: action.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter12/action.pdf"));
			writer.setPdfVersion(PdfWriter.VERSION_1_5);
			// step 3: we open the document
			document.open();
			// step 4:
			PdfLayer a1 = new PdfLayer("answer 1", writer);
			PdfLayer a2 = new PdfLayer("answer 2", writer);
			PdfLayer a3 = new PdfLayer("answer 3", writer);
			a1.setOn(false);
			a2.setOn(false);
			a3.setOn(false);

			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent();
			cb.beginText();
			cb.setTextMatrix(50, 790);
			cb.setLeading(24);
			cb.setFontAndSize(bf, 18);
			cb.newlineShowText("Q1: Who is quick?");
			cb.beginLayer(a1);
			cb.setRGBColorFill(0xFF, 0x00, 0x00);
			cb.newlineShowText("A1: the fox");
			cb.resetRGBColorFill();
			cb.endLayer();
			cb.newlineShowText("Q2: Who is lazy?");
			cb.beginLayer(a2);
			cb.setRGBColorFill(0xFF, 0x00, 0x00);
			cb.newlineShowText("A2: the dog");
			cb.resetRGBColorFill();
			cb.endLayer();
			cb.newlineShowText("Q3: Who jumps over the lazy dog?");
			cb.beginLayer(a3);
			cb.setRGBColorFill(0xFF, 0x00, 0x00);
			cb.newlineShowText("A3: the quick fox");
			cb.resetRGBColorFill();
			cb.endLayer();
			cb.endText();

			ArrayList stateOn = new ArrayList();
			stateOn.add("ON");
			stateOn.add(a1);
			stateOn.add(a2);
			stateOn.add(a3);
			PdfAction actionOn = PdfAction.setOCGstate(stateOn, true);
			ArrayList stateOff = new ArrayList();
			stateOff.add("OFF");
			stateOff.add(a1);
			stateOff.add(a2);
			stateOff.add(a3);
			PdfAction actionOff = PdfAction.setOCGstate(stateOff, true);
			ArrayList stateToggle = new ArrayList();
			stateToggle.add("Toggle");
			stateToggle.add(a1);
			stateToggle.add(a2);
			stateToggle.add(a3);
			PdfAction actionToggle = PdfAction.setOCGstate(stateToggle, true);
			Phrase p = new Phrase("Change the state of the answers:");
			Chunk on = new Chunk(" on ").setAction(actionOn);
			p.add(on);
			Chunk off = new Chunk("/ off ").setAction(actionOff);
			p.add(off);
			Chunk toggle = new Chunk("/ toggle").setAction(actionToggle);
			p.add(toggle);
			document.add(p);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
