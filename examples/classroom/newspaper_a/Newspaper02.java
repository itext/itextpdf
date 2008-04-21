package classroom.newspaper_a;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class Newspaper02 extends Newspaper {

	public static final String RESULT = RESULTPATH + "newspaper02.pdf";
	
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			Document document = new Document(reader.getPageSizeWithRotation(1));
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
			document.open();
			PdfContentByte content = writer.getDirectContent();
			content.rectangle(document.left(), document.bottom(), document.right(), document.top());
			content.rectangle(LLX1, LLY1, W1, H1);
			content.rectangle(LLX2, LLY2, W2, H2);
			content.eoClip();
			content.newPath();
			PdfImportedPage page = writer.getImportedPage(reader, 1);
			content.addTemplate(page, 0, 0);
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
