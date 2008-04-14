package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PushbuttonField;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class TooltipExample3 extends PdfPageEventHelper {
	public static void main(String[] args) {
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
					// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapterX/tooltip3.pdf"));
			// step 3: we open the document
			document.open();
			writer.setPageEvent(new TooltipExample3());
			// step 4: we add a paragraph to the document
			Paragraph p = new Paragraph("Hello World ");
			Chunk c = new Chunk("tooltip");
			c.setGenericTag("This is my tooltip.");
			p.add(c);
			document.add(p);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5: we close the document
		document.close();
	}

	/**
	 * (non-Javadoc)
	 * @see com.lowagie.text.pdf.PdfPageEventHelper#onGenericTag(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document, com.lowagie.text.Rectangle, java.lang.String)
	 */
	public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
		try {
			
			// we create a text annotation with name mytooltip and color red
			PdfAnnotation annotation =
				PdfAnnotation.createText(writer, rect, "tooltip", text, false, null);
			annotation.put(PdfName.NM, new PdfString("mytooltip"));
			float[] red = { 1, 0, 0 };
			annotation.put(PdfName.C, new PdfArray(red));
			// the text must be read only, and the annotation set to NOVIEW
			annotation.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_READONLY | PdfAnnotation.FLAGS_NOVIEW));
			// we create a popup annotation that will define where the rectangle will appear
			PdfAnnotation popup = PdfAnnotation.createPopup(writer, new Rectangle(rect.getLeft(), rect.getBottom() - 80, rect.getRight() + 100, rect.getBottom()), null, false);
			// we add a reference to the text annotation to the popup annotation
			popup.put(PdfName.PARENT, annotation.getIndirectReference());
			// we add a reference to the popup annotation to the text annotation
			annotation.put(PdfName.POPUP, popup.getIndirectReference());
			// we add both annotations to the writer
			writer.addAnnotation(annotation);
			writer.addAnnotation(popup);
			
			// the text annotation can't be viewed (it's invisible)
			// we create a widget annotation named mywidget (it's a button field)
			PushbuttonField field = new PushbuttonField(writer, rect, "mywidget");
			PdfAnnotation widget = field.getField();
			PdfDictionary dict = new PdfDictionary();
			// we write some javascript that makes the popup of the text annotation visible/invisible on mouse enter/exit
			String js1 = "var t = this.getAnnot(this.pageNum, 'mytooltip'); t.popupOpen = true; var w = this.getField('mywidget'); w.setFocus();";
			PdfAction enter = PdfAction.javaScript(js1, writer);
			dict.put(PdfName.E, enter);
			String js2 = "var t = this.getAnnot(this.pageNum, 'mytooltip'); t.popupOpen = false;";
			PdfAction exit = PdfAction.javaScript(js2, writer);
			dict.put(PdfName.X, exit);
			// we add the javascript as additional action
			widget.put(PdfName.AA, dict);
			// we add the button field
			writer.addAnnotation(widget);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
