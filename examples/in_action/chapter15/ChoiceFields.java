/* in_action/chapter15/ChoiceFields.java */
package in_action.chapter15;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.pdf.PdfBorderDictionary;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ChoiceFields {

	/**
	 * Demonstrates the different types of buttons.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example Choice Fields");
		System.out.println("-> Creates a PDF file with widget annotations of type field;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   choicefields.pdf");

		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/choicefields.pdf"));
			// step 3:
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();

			String options[] = { "English", "French", "Dutch", "German" };

			PdfFormField combo = PdfFormField.createCombo(writer, true,
					options, 0);
			combo.setWidget(new Rectangle(40, 780, 120, 800),
					PdfAnnotation.HIGHLIGHT_INVERT);
			combo.setFieldName("languageCombo");
			combo.setValueAsString("English");
			writer.addAnnotation(combo);

			PdfFormField field = PdfFormField.createList(writer, options, 0);
			PdfAppearance app = cb.createAppearance(80, 60);
			app.rectangle(1, 1, 78, 58);
			app.stroke();
			field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, app);
			field.setWidget(new Rectangle(140, 740, 220, 800),
					PdfAnnotation.HIGHLIGHT_OUTLINE);
			field.setFieldName("languageList");
			field.setValueAsString("English");
			writer.addAnnotation(field);

			TextField tf1 = new TextField(writer, new Rectangle(240, 740, 290,
					800), "comboLanguage");
			tf1.setBackgroundColor(Color.YELLOW);
			tf1.setBorderColor(Color.BLUE);
			tf1.setBorderWidth(2);
			tf1.setFontSize(10);
			tf1.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
			tf1.setVisibility(TextField.VISIBLE_BUT_DOES_NOT_PRINT);
			tf1.setChoices(new String[] { "English", "French" });
			tf1.setChoiceExports(new String[] { "EN", "FR" });
			tf1.setRotation(90);
			writer.addAnnotation(tf1.getComboField());

			TextField tf2 = new TextField(writer, new Rectangle(300, 740, 400,
					800), "listLetters");
			tf2.setBackgroundColor(Color.YELLOW);
			tf2.setBorderColor(Color.RED);
			tf2.setBorderWidth(2);
			tf2.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
			tf2.setFontSize(10);
			tf2.setChoices(new String[] { "a", "b", "c", "d", "e", "f", "g",
					"h" });
			tf2.setChoiceSelection(4);
			writer.addAnnotation(tf2.getListField());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// step 5: we close the document
		document.close();

	}
}
