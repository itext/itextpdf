package classroom.intro;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class HelloWorld08 {
	
	public static final String RESULT = "results/classroom/intro/hello08.pdf";
	
	public static void main(String[] args) {
		Document.compress = false;
		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
			// step 3
			document.open();
			// step 4
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			cb.beginText();
            cb.setFontAndSize(bf, 12);
            cb.moveText(88.66f, 788);
            cb.showText("ld");
            cb.moveText(-22f, 0); 
            cb.showText("Wor");
            cb.moveText(-15.33f, 0); 
            cb.showText("llo");
            cb.endText();
            PdfTemplate tmp = cb.createTemplate(250, 25);
            tmp.beginText();
            tmp.setFontAndSize(bf, 12);
            tmp.moveText(0, 7);
            tmp.showText("He");
            tmp.endText();
            cb.addTemplate(tmp, 36, 781);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5
		document.close();
	}
}
