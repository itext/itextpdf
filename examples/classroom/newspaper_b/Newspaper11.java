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
import java.util.Set;

import classroom.newspaper_a.Newspaper;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.TextField;

public class Newspaper11 extends Newspaper {

	public static final String RESULT = RESULTPATH + "newspaper11.pdf";
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			PdfContentByte canvas = stamper.getOverContent(1);
			canvas.setRGBColorFill(0xFF, 0xFF, 0xFF);
			canvas.rectangle(LLX1, LLY1, W1, H1);
			canvas.rectangle(LLX2, LLY2, W2, H2);
			canvas.fill();
			addTextField(stamper, new Rectangle(LLX1, LLY1, URX1, URY1), "field1", 1);
			addTextField(stamper, new Rectangle(LLX2, LLY2, URX2, URY2), "field2", 1);
			stamper.close();
			
			reader = new PdfReader(RESULT);
			AcroFields fields = reader.getAcroFields();
			Set<String> fieldnames = fields.getFields().keySet();
			for (String fieldname : fieldnames) {
				System.out.print(fieldname);
				System.out.print(": page ");
				float[] positions = fields.getFieldPositions(fieldname);
				System.out.print(positions[0]);
				System.out.print(" [ ");
				System.out.print(positions[1]);
				System.out.print(", ");
				System.out.print(positions[2]);
				System.out.print(", ");
				System.out.print(positions[3]);
				System.out.print(", ");
				System.out.print(positions[4]);
				System.out.println("]");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void addTextField(PdfStamper stamper, Rectangle rect, String name, int pagenumber) throws IOException, DocumentException {
		TextField field = new TextField(stamper.getWriter(), rect, name);
		field.setAlignment(Element.ALIGN_CENTER);
		field.setOptions(TextField.MULTILINE);
		field.setText(MESSAGE);
		stamper.addAnnotation(field.getTextField(), pagenumber);
	}
}
