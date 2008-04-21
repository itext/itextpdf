package classroom.newspaper_b;

import java.io.FileOutputStream;
import java.io.IOException;

import classroom.newspaper_a.Newspaper;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PushbuttonField;

public class Newspaper09 extends Newspaper {
	
	public static final String RESULT = RESULTPATH + "newspaper09.pdf";
	
	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader(NEWSPAPER);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
			

			PdfAnnotation annotation1 = PdfAnnotation.createSquareCircle(
					stamper.getWriter(), new Rectangle(LLX1, LLY1, URX1, URY1),
					MESSAGE, true);
			annotation1.put(PdfName.T, new PdfString("Advertisement 1"));
			annotation1.put(PdfName.C, new PdfArray(new float[]{ 1, 0, 0 }));
			stamper.addAnnotation(annotation1, 1);
			
			PdfAnnotation annotation2 =
				PdfAnnotation.createText(
						stamper.getWriter(), new Rectangle(LLX2, LLY2, URX2, URY2),
						"Advertisement 2", MESSAGE, false, null);
			annotation2.put(PdfName.NM, new PdfString("ad2"));
			// the text must be read only, and the annotation set to NOVIEW
			annotation2.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_READONLY | PdfAnnotation.FLAGS_NOVIEW));
			
			// we create a popup annotation that will define where the rectangle will appear
			PdfAnnotation popup = PdfAnnotation.createPopup(
					stamper.getWriter(), new Rectangle(LLX2 + 50, LLY2 + 120, URX2 - 80, URY2 - 120),
					null, false);
			
			// we add a reference to the text annotation to the popup annotation
			popup.put(PdfName.PARENT, annotation2.getIndirectReference());
			// we add a reference to the popup annotation to the text annotation
			annotation2.put(PdfName.POPUP, popup.getIndirectReference());
			
			// we add both annotations to the writer
			stamper.addAnnotation(annotation2, 1);
			stamper.addAnnotation(popup, 1);
 
			// the text annotation can't be viewed (it's invisible)
			// we create a widget annotation named mywidget (it's a button field)
			PushbuttonField field = new PushbuttonField(
					stamper.getWriter(), new Rectangle(LLX2, LLY2, URX2, URY2),
					"button");
			PdfAnnotation widget = field.getField();
			PdfDictionary dict = new PdfDictionary();
			// we write some javascript that makes the popup of the text annotation visible/invisible on mouse enter/exit
			String js1 = "var t = this.getAnnot(this.pageNum, 'ad2'); t.popupOpen = true; var w = this.getField('button'); w.setFocus();";
			PdfAction enter = PdfAction.javaScript(js1, stamper.getWriter());
			dict.put(PdfName.E, enter);
			String js2 = "var t = this.getAnnot(this.pageNum, 'ad2'); t.popupOpen = false;";
			PdfAction exit = PdfAction.javaScript(js2, stamper.getWriter());
			dict.put(PdfName.X, exit);
			// we add the javascript as additional action
			widget.put(PdfName.AA, dict);
			// we add the button field
			stamper.addAnnotation(widget, 1);
			
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
