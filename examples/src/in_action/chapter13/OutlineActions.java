/* in_action/chapter13/OutlineActions.java */

package in_action.chapter13;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfLayer;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;
import com.lowagie.text.pdf.SimpleNamedDestination;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class OutlineActions {

	/**
	 * Generates a file with actions in the outline tree.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example Outline Actions");
		System.out.println("-> Creates a PDF file with some actions");
		System.out.println("   that can be triggered using the outline tree.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   outline_actions.pdf, outline_actions1.xml and outline_actions2.xml");
		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter13/outline_actions.pdf"));
			// step 3:
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			document.open();
			// step 4: we grab the ContentByte and do some stuff with it
			document.add(new Chunk("Questions and Answers")
					.setLocalDestination("Title"));

			PdfLayer answers = new PdfLayer("answers", writer);
			answers.setOn(false);
			answers.setOnPanel(false);

			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent();
			cb.beginText();
			cb.setTextMatrix(50, 790);
			cb.setLeading(24);
			cb.setFontAndSize(bf, 18);
			cb.newlineShowText("Q1: Who is quick?");
			cb.beginLayer(answers);
			cb.setRGBColorFill(0xFF, 0x00, 0x00);
			cb.newlineShowText("A1: the fox");
			cb.resetRGBColorFill();
			cb.endLayer();
			cb.newlineShowText("Q2: Who is lazy?");
			cb.beginLayer(answers);
			cb.setRGBColorFill(0xFF, 0x00, 0x00);
			cb.newlineShowText("A2: the dog");
			cb.resetRGBColorFill();
			cb.endLayer();
			cb.newlineShowText("Q3: Who jumps over the lazy dog?");
			cb.beginLayer(answers);
			cb.setRGBColorFill(0xFF, 0x00, 0x00);
			cb.newlineShowText("A3: the quick fox");
			cb.resetRGBColorFill();
			cb.endLayer();
			cb.endText();

			PdfOutline root = cb.getRootOutline();
			new PdfOutline(root, PdfAction.gotoLocalPage(
					"Title", false), "Go to the top of the page");
			ArrayList stateToggle = new ArrayList();
			stateToggle.add("Toggle");
			stateToggle.add(answers);
			PdfAction actionToggle = PdfAction.setOCGstate(stateToggle, true);
			PdfOutline toggle = new PdfOutline(root, actionToggle,
					"Toggle the state of the answers");
			toggle.setColor(new Color(0x00, 0x80, 0x80));
			toggle.setStyle(Font.BOLD);
			PdfOutline links = new PdfOutline(root, new PdfAction(),
					"Useful links");
			links.setOpen(false);
			new PdfOutline(links,
					new PdfAction("http://www.lowagie.com/iText"),
					"Bruno's iText site");
			new PdfOutline(links, new PdfAction(
					"http://itextpdf.sourceforge.net/"), "Paulo's iText site");
			new PdfOutline(links, new PdfAction(
					"http://sourceforge.net/projects/itext/"),
					"iText @ SourceForge");
			PdfAction chained = PdfAction.javaScript(
					"app.alert('Bin-jip at IMDB');\r", writer);
			chained.next(new PdfAction("http://www.imdb.com/title/tt0423866/"));
			new PdfOutline(root, chained, "\ube48\uc9d1");
			document.newPage();
			document.add(new Paragraph("This was quite an easy quiz."));
			PdfAction dest = PdfAction.gotoLocalPage(2, new PdfDestination(
					PdfDestination.FITB), writer);
			PdfOutline what = new PdfOutline(root, dest, "What's on page 2?");
			what.setStyle(Font.ITALIC);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		document.close();
		try {
			PdfReader reader = new PdfReader("results/in_action/chapter13/outline_actions.pdf");
			List list = SimpleBookmark.getBookmark(reader);
			SimpleBookmark.exportToXML(list, new FileOutputStream(
					"results/in_action/chapter13/outline_actions1.xml"), "ISO8859-1", true);
			HashMap map = SimpleNamedDestination.getNamedDestination(reader,
					false);
			SimpleNamedDestination.exportToXML(map, new FileOutputStream(
					"results/in_action/chapter13/outline_actions2.xml"), "ISO8859-1", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}