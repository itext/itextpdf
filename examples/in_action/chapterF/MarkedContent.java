/* in_action/chapterF/MarkedContent.java */

package in_action.chapterF;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfStructureElement;
import com.lowagie.text.pdf.PdfStructureTreeRoot;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class MarkedContent {

	/**
	 * Generates a PDF file showing the different canvases in iText.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Appendix F: example MarkedContent");
		System.out.println("-> Creates a Tagged PDF file.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: marked_content.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapterF/marked_content.pdf"));
			writer.setTagged();
			// step 3: we open the document
			document.open();
			// step 4:
			PdfStructureTreeRoot root = writer.getStructureTreeRoot();
			// we call the root "Everything"
			PdfStructureElement eTop = new PdfStructureElement(root,
					new PdfName("Everything"));
			// "Everything" is not a standard structure and must be mapped to a
			// standard one like "Sect"
			root.mapRole(new PdfName("Everything"), new PdfName("Sect"));
			// "P" is a standard structure, no need to map
			PdfStructureElement e1 = new PdfStructureElement(eTop, PdfName.P);
			PdfStructureElement e2 = new PdfStructureElement(eTop, PdfName.P);
			PdfStructureElement e3 = new PdfStructureElement(eTop, PdfName.P);
			// we grab the direct content and create a font
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, false);
			cb.setLeading(16);
			cb.setFontAndSize(bf, 12);

			// the paragraph is contained in a single sequence
			cb.beginMarkedContentSequence(e1);
			cb.beginText();
			cb.setTextMatrix(50, 790);
			for (int k = 0; k < text1.length; ++k) {
				cb.newlineShowText(text1[k]);
			}
			cb.endText();
			cb.endMarkedContentSequence();

			// the paragraph is contained in several sequences but logically is
			// a single sequence
			cb.beginText();
			cb.setTextMatrix(50, 700);
			for (int k = 0; k < 2; ++k) {
				cb.beginMarkedContentSequence(e2);
				cb.newlineShowText(text2[k]);
				cb.endMarkedContentSequence();
			}
			cb.endText();
			document.newPage();
			cb.setLeading(16);
			cb.setFontAndSize(bf, 12);
			cb.beginText();
			cb.setTextMatrix(50, 804);
			for (int k = 2; k < text2.length; ++k) {
				cb.beginMarkedContentSequence(e2);
				cb.newlineShowText(text2[k]);
				cb.endMarkedContentSequence();
			}
			cb.endText();

			// text replacement - the word "good" will be replaced by "bad" when
			// extracting text
			cb.beginMarkedContentSequence(e3);
			cb.beginText();
			cb.setTextMatrix(50, 400);
			cb.showText("It was the ");
			PdfDictionary dic = new PdfDictionary();
			dic.put(new PdfName("ActualText"), new PdfString("best"));
			cb.beginMarkedContentSequence(new PdfName("Span"), dic, true);
			cb.showText("worst");
			cb.endMarkedContentSequence();
			cb.showText(" of times.");
			cb.endText();
			cb.endMarkedContentSequence();
		} catch (Exception de) {
			de.printStackTrace();
		}
		document.close();
	}

	/** a string array with text. */
	public static String[] text1 = {
			"It was the best of times, it was the worst of times, ",
			"it was the age of wisdom, it was the age of foolishness, ",
			"it was the epoch of belief, it was the epoch of incredulity, ",
			"it was the season of Light, it was the season of Darkness, ",
			"it was the spring of hope, it was the winter of despair." };

	/** a string array with text. */
	public static String[] text2 = {
			"We had everything before us, we had nothing before us, ",
			"we were all going direct to Heaven, we were all going direct ",
			"the other way\u2014in short, the period was so far like the present ",
			"period, that some of its noisiest authorities insisted on its ",
			"being received, for good or for evil, in the superlative degree ",
			"of comparison only." };
}