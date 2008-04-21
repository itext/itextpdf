package classroom.newspaper_b;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import classroom.newspaper_a.Newspaper;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PushbuttonField;

public class Newspaper10 extends Newspaper {
	public static final String RESULT = RESULTPATH + "newspaper10.pdf";
	public static final String IMG = RESOURCESPATH + "iia.jpg";
	
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			PushbuttonField button;
			Rectangle rect;
			
			rect = new Rectangle(100, 980, 700, 1000);
			button = new PushbuttonField(
					stamper.getWriter(), rect, "click");
			button.setBackgroundColor(Color.ORANGE);
			button.setText("Click here to close window");
			button.setLayout(PushbuttonField.LAYOUT_LABEL_ONLY);
			button.setAlignment(Element.ALIGN_RIGHT);
			PdfFormField menubar = button.getField();
			String js = "var f1 = getField('click'); f1.display = display.hidden;"
				+ "var f2 = getField('advertisement'); f2.display = display.hidden;";
			menubar.setAction(PdfAction.javaScript(js, stamper.getWriter()));
			stamper.addAnnotation(menubar, 1);
			
			rect = new Rectangle(100, 500, 700, 980);
			button = new PushbuttonField(
					stamper.getWriter(), rect, "advertisement");
			button.setBackgroundColor(Color.WHITE);
			button.setBorderColor(Color.ORANGE);
			button.setImage(Image.getInstance(IMG));
			button.setText("Buy the book iText in Action");
			button.setLayout(PushbuttonField.LAYOUT_LABEL_TOP_ICON_BOTTOM);
			PdfFormField advertisement = button.getField();
			advertisement.setAction(
					new PdfAction("http://www.1t3xt.com/docs/book.php"));
			stamper.addAnnotation(advertisement, 1);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
