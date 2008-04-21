package classroom.newspaper_a;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class Newspaper04 extends Newspaper {
	
	public static final String RESULT = RESULTPATH + "newspaper04.pdf";
	
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			PdfContentByte canvas = stamper.getOverContent(1);
			canvas.saveState();
			canvas.setRGBColorFill(0xFF, 0xFF, 0xFF);
			canvas.rectangle(LLX1, LLY1, W1, H1);
			canvas.rectangle(LLX2, LLY2, W2, H2);
			canvas.fillStroke();
			canvas.restoreState();
			Phrase p = new Phrase(MESSAGE);
			putText(canvas, p, LLX1, LLY1, URX1, URY1);
			putText(canvas, p, LLX2, LLY2, URX2, URY2);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void putText(PdfContentByte canvas, Phrase phrase, float llx, float lly, float urx, float ury) throws DocumentException {
		ColumnText column = new ColumnText(canvas);
		column.setAlignment(Element.ALIGN_CENTER);
		column.setSimpleColumn(llx, lly, urx, ury);
		column.setText(phrase);
		column.go(true);
		float offset = (column.getYLine() - lly) / 2f;
		column.setText(phrase);
		column.setYLine(ury - offset);
		column.go();
	}
}
