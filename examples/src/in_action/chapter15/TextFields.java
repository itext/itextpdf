/* in_action/chapter15/TextFields.java */
package in_action.chapter15;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
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

public class TextFields {

	/**
	 * Demonstrates the different types of buttons.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example TextFields");
		System.out.println("-> Creates a PDF file with widget annotations of type field;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   textfields.pdf");

		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/textfields.pdf"));
			// step 3:
			document.open();
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			BaseFont helv = BaseFont.createFont(BaseFont.COURIER,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			String text = "Some start text";
			PdfFormField field = PdfFormField.createTextField(writer, false,
					false, 0);
			field.setWidget(new Rectangle(40, 780, 360, 810),
					PdfAnnotation.HIGHLIGHT_INVERT);
			field.setFlags(PdfAnnotation.FLAGS_PRINT);
			field.setFieldName("some_text");
			field.setValueAsString(text);
			field.setDefaultValueAsString(text);
			field.setMKBorderColor(Color.RED);
			field.setMKBackgroundColor(Color.YELLOW);
			field.setBorderStyle(new PdfBorderDictionary(2,
					PdfBorderDictionary.STYLE_SOLID));
			field.setPage();
			PdfAppearance tp = cb.createAppearance(320, 30);
			PdfAppearance da = (PdfAppearance) tp.getDuplicate();
			da.setFontAndSize(helv, 12);
			field.setDefaultAppearanceString(da);
			tp.saveState();
			tp.setColorStroke(Color.RED);
			tp.setLineWidth(2);
			tp.setColorFill(Color.YELLOW);
			tp.rectangle(1, 1, 318, 28);
			tp.fillStroke();
			tp.restoreState();
			tp.beginVariableText();
			tp.saveState();
			tp.rectangle(2, 2, 318, 28);
			tp.clip();
			tp.newPath();
			tp.beginText();
			tp.setFontAndSize(helv, 12);
			tp.setTextMatrix(4, 11);
			tp.showText(text);
			tp.endText();
			tp.restoreState();
			tp.endVariableText();
			field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, tp);
			writer.addAnnotation(field);

			TextField tf1 = new TextField(writer, new Rectangle(40, 720, 360,
					750), "fox");
			tf1.setBackgroundColor(Color.YELLOW);
			tf1.setBorderColor(Color.RED);
			tf1.setBorderWidth(2);
			tf1.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
			tf1.setText("Quick brown fox jumps over the lazy dog");
			tf1.setAlignment(Element.ALIGN_CENTER);
			tf1.setOptions(TextField.REQUIRED);
			writer.addAnnotation(tf1.getTextField());

			TextField tf2 = new TextField(writer, new Rectangle(400, 720, 520,
					810), "dog");
			tf2.setBackgroundColor(Color.YELLOW);
			tf2.setBorderColor(Color.RED);
			tf2.setBorderWidth(2);
			tf2.setBorderStyle(PdfBorderDictionary.STYLE_DASHED);
			tf2.setText("Quick brown fox jumps over the lazy dog");
			tf2.setAlignment(Element.ALIGN_RIGHT);
			tf2.setOptions(TextField.MULTILINE | TextField.REQUIRED);
			tf2.setRotation(90);
			writer.addAnnotation(tf2.getTextField());

			TextField tf3 = new TextField(writer, new Rectangle(40, 690, 120,
					710), "secret");
			tf3.setBackgroundColor(Color.RED);
			tf3.setBorderColor(Color.BLUE);
			tf3.setBorderWidth(1);
			tf3.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
			tf3.setText("secret");
			tf3.setOptions(TextField.PASSWORD);
			writer.addAnnotation(tf3.getTextField());

			cb.rectangle(142, 692.5f, 12, 15);
			cb.rectangle(157, 692.5f, 12, 15);
			cb.rectangle(172, 692.5f, 12, 15);
			cb.rectangle(187, 692.5f, 12, 15);
			cb.stroke();
			TextField tf4 = new TextField(writer, new Rectangle(140, 690, 200,
					710), "comb");
			tf4.setMaxCharacterLength(4);
			tf4.setOptions(TextField.COMB);
			tf4.setText("COMB");
			writer.addAnnotation(tf4.getTextField());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// step 5: we close the document
		document.close();

	}
}
