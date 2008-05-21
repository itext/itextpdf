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
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class Newspaper01 extends Newspaper {

	public static final String RESULT = RESULTPATH + "newspaper01.pdf";
	
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			PdfContentByte canvas = stamper.getOverContent(1);
			canvas.setRGBColorFill(0xC0, 0xC0, 0xC0);
			canvas.setRGBColorStroke(0xFF, 0x00, 0x00);
			canvas.rectangle(LLX1, LLY1, W1, H1);
			canvas.rectangle(LLX2, LLY2, W2, H2);
			canvas.fillStroke();
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
