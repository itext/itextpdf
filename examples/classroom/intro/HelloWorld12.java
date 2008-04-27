package classroom.intro;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

public class HelloWorld12 {
	
	public static final String RESULT = "results/classroom/intro/hello12.pdf";
	public static final String IMAGE = "resources/classroom/hello.png";
	
	public static void main(String[] args) {
		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(RESULT));
			// step 3
			document.open();
			// step 4
			Image image = Image.getInstance(IMAGE);
			image.scaleToFit(595, 842);
			System.out.println("w x h: " + image.getScaledWidth() + " x " + image.getScaledHeight());
			image.setAbsolutePosition(0, 0);
			document.add(image);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5
		document.close();
	}
}