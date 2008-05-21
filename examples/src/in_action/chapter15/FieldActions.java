/* in_action/chapter15/FieldsActions.java */
package in_action.chapter15;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FieldActions {

	/**
	 * Demonstrates the different types of buttons.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example Field Actions");
		System.out.println("-> Creates a PDF file with fields that have additional actions;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   field_actions.pdf");

		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/field_actions.pdf"));
			// step 3:
			document.open();
			// step 4:
			TextField textfield = new TextField(writer, new Rectangle(140, 790,
					200, 810), "uppercase");
			textfield.setMaxCharacterLength(4);
			textfield.setOptions(TextField.COMB);
			textfield.setBorderWidth(1);
			textfield.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
			PdfFormField field = textfield.getTextField();
			field.setAdditionalActions(new PdfName("Fo"), PdfAction.javaScript(
					"app.alert('COMB got the focus');", writer));
			field.setAdditionalActions(new PdfName("Bl"), PdfAction.javaScript(
					"app.alert('COMB lost the focus');", writer));
			field.setAdditionalActions(new PdfName("K"), PdfAction.javaScript(
					"event.change = event.change.toUpperCase();", writer));
			writer.addAnnotation(field);

			TextField date = new TextField(writer, new Rectangle(140, 760, 250,
					780), "date");
			date.setBackgroundColor(Color.YELLOW);
			date.setBorderColor(Color.BLUE);
			date.setBorderWidth(2);
			date.setFontSize(10);
			date.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
			date.setOptions(TextField.EDIT);
			date.setChoices(new String[] { "Christmas", "New Year" });
			date.setChoiceExports(new String[] { "12-25-2006", "01-01-2007" });
			field = date.getComboField();
			field.setAdditionalActions(PdfName.K, PdfAction.javaScript(
					"AFDate_KeystrokeEx( 'dd-mm-yyyy' )", writer));
			writer.addAnnotation(field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// step 5: we close the document
		document.close();

	}
}
