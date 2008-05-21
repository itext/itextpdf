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
import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class Newspaper12 {

	public static final String NEWSPAPER = Newspaper11.RESULT;
	public static final String RESULT = Newspaper11.RESULTPATH + "newspaper12.pdf";
	public static final String IMG1 = Newspaper11.RESOURCESPATH + "manning.gif";
	public static final String IMG2 = Newspaper11.RESOURCESPATH + "iia.jpg";
	
	public static void main(String[] args) {
		Newspaper11.main(args);
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			AcroFields fields = stamper.getAcroFields();
			fields.setField("field1", "Advertissement 1");
			float[] positions1 = fields.getFieldPositions("field1");
			putImage(stamper.getOverContent((int)positions1[0]), IMG1, positions1);
			fields.setField("field2", "Advertissement 2");
			float[] positions2 = fields.getFieldPositions("field2");
			putImage(stamper.getOverContent((int)positions2[0]), IMG2, positions2);
			stamper.setFormFlattening(true);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void putImage(PdfContentByte canvas, String path, float[] positions) throws DocumentException, IOException {
		float w = positions[3] - positions[1];
		float h = positions[4] - positions[2];
		Image img = Image.getInstance(path);
		img.scaleToFit(w, h);
		float offsetX = (w - img.getScaledWidth()) / 2f;
		float offsetY = (h - img.getScaledHeight()) / 2f;
		img.setAbsolutePosition(positions[1] + offsetX, positions[2] + offsetY);
		canvas.addImage(img);
		canvas.rectangle(positions[1], positions[2], w, h);
		canvas.stroke();
	}
}
