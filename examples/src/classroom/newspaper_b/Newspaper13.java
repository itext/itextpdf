/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.newspaper_b;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import classroom.newspaper_a.Newspaper;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PushbuttonField;

public class Newspaper13 extends Newspaper {

	public static final String RESULT = RESULTPATH + "newspaper13.pdf";
	public static final String PATH1 = "resources/classroom/1t3xt/logo.gif";
	public static final String PATH2 = "resources/classroom/1t3xt/logo.pdf";
	
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			addButton(stamper, new Rectangle(LLX1, LLY1, URX1, URY1), PATH1, "button1", 1);
			addButton(stamper, new Rectangle(LLX2, LLY2, URX2, URY2), PATH2, "button2", 1);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void addButton(PdfStamper stamper, Rectangle rect, String path, String name, int pagenumber) throws IOException, DocumentException {
		PushbuttonField field = new PushbuttonField(stamper.getWriter(), rect, name);
		if (path.endsWith(".pdf")) {
			PdfReader reader = new PdfReader(path);
			field.setTemplate(stamper.getImportedPage(reader, 1));
		}
		else {
			field.setImage(Image.getInstance(path));
		}
		field.setBackgroundColor(new Color(0xFF, 0xFF, 0xFF));
		field.setBorderColor(new Color(0xC0, 0xC0, 0xC0));
		field.setBorderWidth(0.5f);
		field.setScaleIcon(PushbuttonField.SCALE_ICON_ALWAYS);
		field.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
		stamper.addAnnotation(field.getField(), pagenumber);
	}
}
