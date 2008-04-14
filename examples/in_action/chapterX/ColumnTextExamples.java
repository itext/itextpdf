package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class ColumnTextExamples {

	public static void main(String[] args) {
		// step 1
		Document document = new Document(PageSize.A4);
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(
					document,
					new FileOutputStream("results/in_action/chapterX/columntext.pdf"));
			// step 3
			document.open();
			// step 4
			PdfContentByte cb = writer.getDirectContent();
			cb.setRGBColorStroke(0xC0, 0xC0, 0xC0);
			cb.moveTo(40, 36);
			cb.lineTo(40, PageSize.A4.getHeight() - 36);
			cb.moveTo(120, 36);
			cb.lineTo(120, PageSize.A4.getHeight() - 36);
			cb.moveTo(160, 36);
			cb.lineTo(160, PageSize.A4.getHeight() - 36);
			cb.moveTo(240, 36);
			cb.lineTo(240, PageSize.A4.getHeight() - 36);
			cb.moveTo(120, 36);
			cb.lineTo(120, PageSize.A4.getHeight() - 36);
			cb.moveTo(280, 36);
			cb.lineTo(280, PageSize.A4.getHeight() - 36);
			cb.moveTo(360, 36);
			cb.lineTo(360, PageSize.A4.getHeight() - 36);
			cb.moveTo(400, 36);
			cb.lineTo(400, PageSize.A4.getHeight() - 36);
			cb.moveTo(480, 36);
			cb.lineTo(480, PageSize.A4.getHeight() - 36);
			cb.stroke();
			
			ColumnText ct = new ColumnText(cb);
			
			// text mode: chunks and phrases only
			
			ct.addText(new Phrase("Quick brown fox jumps over the lazy dog"));
			ct.setSimpleColumn(40, 36, 120, PageSize.A4.getHeight() - 36, 18, Element.ALIGN_JUSTIFIED);
			ct.go();

			ct.addText(new Phrase("Quick brown fox jumps over the lazy dog"));
			ct.setSimpleColumn(160, 36, 240, PageSize.A4.getHeight() - 36, 18, Element.ALIGN_CENTER);
			ct.go();
			
			ct.addText(new Phrase("Quick brown fox jumps over the lazy dog"));
			ct.setSimpleColumn(280, 36, 360, PageSize.A4.getHeight() - 36, 18, Element.ALIGN_LEFT);
			ct.go();

			ct.addText(new Phrase("Quick brown fox jumps over the lazy dog"));
			ct.setSimpleColumn(400, 36, 480, PageSize.A4.getHeight() - 36, 18, Element.ALIGN_RIGHT);
			ct.go();
			
			// composite mode: any object
			
			Paragraph p = new Paragraph("Justified: Quick brown fox jumps over the lazy dog");
			p.setAlignment(Element.ALIGN_JUSTIFIED);
			ct.addElement(p);
			p = new Paragraph("Centered: Quick brown fox jumps over the lazy dog");
			p.setAlignment(Element.ALIGN_CENTER);
			ct.addElement(p);
			p = new Paragraph("Left: Quick brown fox jumps over the lazy dog");
			p.setAlignment(Element.ALIGN_LEFT);
			ct.addElement(p);
			p = new Paragraph("Right: Quick brown fox jumps over the lazy dog");
			p.setAlignment(Element.ALIGN_RIGHT);
			ct.addElement(p);
			ct.setSimpleColumn(40, 36, 120, PageSize.A4.getHeight() - 144);
			ct.go();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5
		document.close();
	}
}
