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

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PushbuttonField;

public class Newspaper14 {

	public static final String NEWSPAPER = Newspaper13.RESULT;
	public static final String RESULT = Newspaper13.RESULTPATH + "newspaper14.pdf";
	public static final String PATH1 = "resources/classroom/1t3xt/1t3xt.pdf";
	public static final String PATH2 = "resources/classroom/1t3xt/hero.pdf";
	
	public static void main(String[] args) {
		Newspaper13.main(args);
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			changeField(stamper, "button1", PATH1);
			changeField(stamper, "button2", PATH2);
			stamper.setFormFlattening(true);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void changeField(PdfStamper stamper, String button, String path) throws IOException, DocumentException {
		AcroFields fields = stamper.getAcroFields();
		PushbuttonField bt = fields.getNewPushbuttonFromField(button);
	    bt.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
	    bt.setProportionalIcon(true);
	    PdfReader reader = new PdfReader(path);
		bt.setTemplate(stamper.getImportedPage(reader, 1));
		fields.replacePushbuttonField(button, bt.getField());
	}
}
