package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import in_action.chapter02.HelloWorldForm;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PushbuttonField;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class HelloWorldAddButton {

	/**
	 * Adds a submit button to an existing form.
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		// we create a PDF file
		HelloWorldForm.createPdf("results/in_action/chapterX/HelloWorldForm.pdf");
		// now we are going to add a button
		try {
			PdfReader reader = new PdfReader("results/in_action/chapterX/HelloWorldForm.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapterX/HelloWorldFormWithButton.pdf"));
			PdfWriter writer = stamper.getWriter();
			PushbuttonField button = new PushbuttonField(writer,
					new Rectangle(150, 760, 200, 790), "submit");
			button.setText("Submit");
			button.setOptions(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
			PdfFormField submit = button.getField();
			submit.setAction(PdfAction.createSubmitForm(
					"http://www.1t3xt.info:8080/itext-in-action/form.jsp",
					null, PdfAction.SUBMIT_HTML_FORMAT | PdfAction.SUBMIT_COORDINATES));
			stamper.addAnnotation(submit, 1);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
