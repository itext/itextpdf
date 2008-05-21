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
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfTemplate;

public class Newspaper05 extends Newspaper {
	
	public static final String RESULT = RESULTPATH + "newspaper05.pdf";
	
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
			Phrase p = new Phrase(MESSAGE);
			putText(canvas, p, LLX1, LLY1, W1, H1);
			putText(canvas, p, LLX2, LLY2, W2, H2);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void putText(PdfContentByte canvas, Phrase p, float llx, float lly, float w, float h) throws DocumentException {
		PdfTemplate template = canvas.createTemplate(h, w);
		ColumnText column = new ColumnText(template);
		column.setSimpleColumn(0, 0, h, w);
		column.setAlignment(Element.ALIGN_CENTER);
		column.setText(p);
		column.go(true);
		float offset = w - (column.getYLine() / 2f);
		column.setText(p);
		column.setYLine(offset);
		column.go();
		canvas.addTemplate(template, 0, 1, -1, 0, llx + w, lly);
	}
}
