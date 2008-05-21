/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.newspaper_b;

import java.io.FileOutputStream;
import java.io.IOException;

import classroom.newspaper_a.Newspaper;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class Newspaper08 extends Newspaper {
	
	public static final String RESULT = RESULTPATH + "newspaper08.pdf";
	
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			
			PdfAnnotation annotation1 = PdfAnnotation.createText(
					stamper.getWriter(), new Rectangle(LLX1, LLY1, URX1, URY1),
					"Advertisement 1", MESSAGE, false, "Insert");
			PdfAppearance ap = stamper.getOverContent(1).createAppearance(W1, H1);
			ap.setRGBColorStroke(0xFF, 0x00, 0x00);
			ap.setLineWidth(3);
			ap.moveTo(0, 0);
			ap.lineTo(W1, H1);
			ap.moveTo(W1, 0);
			ap.lineTo(0, H1);
			ap.stroke();
			annotation1.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, ap);
			stamper.addAnnotation(annotation1, 1);
			
			PdfAnnotation annotation2 = PdfAnnotation.createText(
					stamper.getWriter(), new Rectangle(LLX2, LLY2, URX2, URY2),
					"Advertisement 2", MESSAGE, true, "Insert");
			annotation2.put(PdfName.C, new PdfArray(new float[]{ 0, 0, 1 }));
			stamper.addAnnotation(annotation2, 1);
			
			
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
