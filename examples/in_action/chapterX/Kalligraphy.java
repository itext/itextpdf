package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class Kalligraphy {
	public static void main(String[] args) {
		// step 1
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			PdfWriter writer = PdfWriter.getInstance(
					document,
					new FileOutputStream("results/in_action/chapterX/kalligraphy.pdf"));
			writer.setViewerPreferences(PdfWriter.PrintScalingNone);
			// step 3
			document.open();
			// step 4
			PdfContentByte cb = writer.getDirectContent();
			float lines[] = { mm2pt(7), mm2pt(3), mm2pt(7) };
			float width = PageSize.A4.getWidth();
			float bottom = mm2pt(30);
			float y = PageSize.A4.getHeight() - mm2pt(21);
			cb.setRGBColorStroke(0xD0, 0xD0, 0xD0);
			cb.moveTo(0, y);
			cb.lineTo(width, y);
			while (y > bottom) {
				for (int i = 0; i < lines.length; i++) {
					y -= lines[i];
					cb.moveTo(0, y);
					cb.lineTo(width, y);
				}
			}
			cb.stroke();
			cb.setRGBColorStroke(0xFF, 0x00, 0x00);
			cb.moveTo(mm2pt(30), 0);
			cb.lineTo(mm2pt(30), PageSize.A4.getHeight());
			cb.stroke();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5
		document.close();
	}
	
	private static float mm2pt(float f) {
		return f * 72f / 25.4f;
	}
}
