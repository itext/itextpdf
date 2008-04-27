package classroom.intro;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class HelloWorld03 {
	
	public static final String RESULT = "results/classroom/intro/hello03.pdf";
	
	public static void main(String[] args) {
		
		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
			writer.setFullCompression();
			// step 3
			document.open();
			// step 4
			document.add(new Paragraph("Hello World"));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5
		document.close();
	}
}
