/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.newspaper_a;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class Newspaper03 extends Newspaper {
	
	public static final String RESULT = RESULTPATH + "newspaper03.pdf";
	
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			PdfContentByte canvas = stamper.getOverContent(1);
			canvas.saveState();
			canvas.setRGBColorFill(0xFF, 0xFF, 0xFF);
			canvas.rectangle(LLX1, LLY1, W1, H1);
			canvas.rectangle(LLX2, LLY2, W2, H2);
			canvas.fillStroke();
			canvas.restoreState();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			putText(canvas, MESSAGE, bf, LLX1, LLY1, URX1, URY1);
			putText(canvas, MESSAGE, bf, LLX2, LLY2, URX2, URY2);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void putText(PdfContentByte canvas, String p, BaseFont bf, float llx, float lly, float urx, float ury) throws DocumentException {
		float x = (llx + urx) / 2f + 5;
		float y = (lly + ury) / 2f - 5;
		float w = urx - llx;
		float h = ury - lly;
		float angle = (float) (180 * Math.atan(h / w) / Math.PI);
		canvas.beginText();
		canvas.setFontAndSize(bf, 10);
		canvas.showTextAligned(Element.ALIGN_CENTER, p, x, y, angle);
		canvas.endText();
	}
}
