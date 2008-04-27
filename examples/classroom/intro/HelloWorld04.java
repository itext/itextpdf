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
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class HelloWorld04 {
	
	public static final String RESULT = "results/classroom/intro/hello04.pdf";
	
	public static void main(String[] args) {
		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
			writer.setEncryption("hello".getBytes(), "world".getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
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
