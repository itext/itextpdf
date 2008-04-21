package classroom.newspaper_a;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Annotation;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class Newspaper07 extends Newspaper {
	
	public static final String RESULT = RESULTPATH + "newspaper07.pdf";
	public static final String IMG1 = RESOURCESPATH + "manning.gif";
	public static final String IMG2 = RESOURCESPATH + "iia.jpg";
	
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			PdfContentByte canvas = stamper.getOverContent(1);
			canvas.saveState();
			canvas.setRGBColorFill(0xFF, 0xFF, 0xFF);
			canvas.rectangle(LLX1, LLY1, W1, H1);
			canvas.rectangle(LLX2, LLY2, W2, H2);
			canvas.fill();
			canvas.restoreState();
			putImage(canvas, Image.getInstance(IMG1), "http://www.manning.com/affiliate/idevaffiliate.php?id=223_0_3_14", LLX1, LLY1, W1, H1);
			putImage(canvas, Image.getInstance(IMG2), "http://www.1t3xt.com/docs/book.php", LLX2, LLY2, W2, H2);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void putImage(PdfContentByte canvas, Image img, String url, float llx, float lly, float w, float h) throws DocumentException {
		img.scaleToFit(w, h);
		float offsetX = (w - img.getScaledWidth()) / 2f;
		float offsetY = (h - img.getScaledHeight()) / 2f;
		img.setAbsolutePosition(llx + offsetX, lly + offsetY);
		img.setAnnotation(new Annotation(0, 0, 0, 0, url));
		canvas.addImage(img);
	}
}
