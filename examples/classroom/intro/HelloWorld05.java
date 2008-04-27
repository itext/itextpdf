package classroom.intro;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class HelloWorld05 {
	
	public static final String RESULT = "results/classroom/intro/hello05.pdf";
	
	public static void main(String[] args) {
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
			cb.saveState(); // q
			cb.beginText(); // BT
			cb.moveText(36, 806); // 36 806 Td
			cb.moveText(0, -18); // 0 -18 Td
			cb.setFontAndSize(bf, 12); // /F1 12 Tf
			cb.showText("Hello World"); // (Hello World)Tj
			cb.endText(); // ET
			cb.restoreState(); // Q
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5
		document.close();
	}
}
