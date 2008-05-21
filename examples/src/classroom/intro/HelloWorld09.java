/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.intro;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class HelloWorld09 {

	public static final String RESULT1 = "results/classroom/intro/hello09_helper.pdf";
	public static final String RESULT2 = "results/classroom/intro/hello09.pdf";
	
	public static void main(String[] args) {
		Document.compress = false;
		BaseFont bf = null;
		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT1));
			// step 3
			document.open();
			// step 4
			PdfContentByte cb = writer.getDirectContent();
			bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			cb.beginText();
            cb.setFontAndSize(bf, 12);
            cb.moveText(88.66f, 788);
            cb.showText("ld");
            cb.moveText(-22f, 0); 
            cb.showText("Wor");
            cb.endText();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5
		document.close();
		
		try {
			PdfReader reader = new PdfReader(RESULT1);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT2));
			PdfContentByte cb1 = stamper.getUnderContent(1);
			cb1.beginText();
            cb1.setFontAndSize(bf, 12);
			cb1.setTextMatrix(51.33f, 788);
            cb1.showText("llo");
            cb1.endText();
			PdfContentByte cb2 = stamper.getOverContent(1);
            PdfTemplate tmp = cb2.createTemplate(250, 25);
            tmp.beginText();
            tmp.setFontAndSize(bf, 12);
            tmp.moveText(0, 7);
            tmp.showText("He");
            tmp.endText();
            cb2.addTemplate(tmp, 36, 781);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
}
