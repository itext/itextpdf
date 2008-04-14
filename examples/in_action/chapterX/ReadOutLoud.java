package in_action.chapterX;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
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

public class ReadOutLoud {

	/**
	 * Generates a PDF file showing the different canvases in iText.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapterX/read_out_loud.pdf"));
			writer.setTagged();
			// step 3: we open the document
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
			PdfStructureTreeRoot root = writer.getStructureTreeRoot();
			PdfStructureElement div = new PdfStructureElement(root, new PdfName("Div"));
			PdfDictionary dict;
			
			cb.beginMarkedContentSequence(div);
			
			cb.beginText();
			cb.moveText(36, 788);
			cb.setFontAndSize(bf, 12);
			cb.setLeading(18);
			cb.showText("These are some famous movies by Stanley Kubrick: ");
			dict = new PdfDictionary();
			dict.put(PdfName.E, new PdfString("Doctor"));
			cb.beginMarkedContentSequence(new PdfName("Span"), dict, true);
			cb.newlineShowText("Dr.");
			cb.endMarkedContentSequence();
			cb.showText(" Strangelove or: How I Learned to Stop Worrying and Love the Bomb.");
			dict = new PdfDictionary();
			dict.put(PdfName.E, new PdfString("Eyes Wide Shut."));
			cb.beginMarkedContentSequence(new PdfName("Span"), dict, true);
			cb.newlineShowText("EWS");
			cb.endMarkedContentSequence();
			cb.endText();
			dict = new PdfDictionary();
			dict.put(PdfName.LANGUAGE, new PdfString("en-us"));
			dict.put(new PdfName("Alt"), new PdfString("2001: A Space Odyssey."));
			cb.beginMarkedContentSequence(new PdfName("Span"), dict, true);
			Image img = Image.getInstance("resources/in_action/chapterX/kubrick07.jpg");
			img.setAbsolutePosition(36, 734 - img.getScaledHeight());
			cb.addImage(img);
			cb.endMarkedContentSequence();

			cb.endMarkedContentSequence();
		} catch (Exception de) {
			de.printStackTrace();
		}
		document.close();
	}
}