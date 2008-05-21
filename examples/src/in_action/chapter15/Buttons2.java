/* in_action/chapter15/Buttons2.java */
package in_action.chapter15;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PushbuttonField;
import com.lowagie.text.pdf.RadioCheckField;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Buttons2 {

	/**
	 * Demonstrates the different types of buttons.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example Buttons2");
		System.out.println("-> Creates a PDF file with widget annotations of type button;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   buttons2.pdf");

		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/buttons2.pdf"));
			// step 3:
			document.open();
			writer.addJavaScript("function showButtonState() {\n"
					+ "app.alert('Checkboxes:"
					+ " English: ' + this.getField('English').value + "
					+ "' French: ' + this.getField('French').value + "
					+ "' Dutch: ' + this.getField('Dutch').value + "
					+ "' Radioboxes: ' + this.getField('language').value);"
					+ "\n}");
			// step 4:
			PdfContentByte cb = writer.getDirectContent();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			String[] languages = { "English", "French", "Dutch" };
			Rectangle rect;

			PdfFormField language = PdfFormField
					.createRadioButton(writer, true);
			language.setFieldName("language");
			language.setValueAsName(languages[0]);
			for (int i = 0; i < languages.length; i++) {
				rect = new Rectangle(40, 806 - i * 40, 60, 788 - i * 40);
				addRadioButton(writer, rect, language, languages[i], i == 0);
				cb.beginText();
				cb.setFontAndSize(bf, 18);
				cb.showTextAligned(Element.ALIGN_LEFT, languages[i], 70,
						790 - i * 40, 0);
				cb.endText();
			}
			writer.addAnnotation(language);

			for (int i = 0; i < languages.length; i++) {
				rect = new Rectangle(260, 806 - i * 40, 280, 788 - i * 40);
				createCheckbox(writer, rect, languages[i]);
				cb.beginText();
				cb.setFontAndSize(bf, 18);
				cb.showTextAligned(Element.ALIGN_LEFT, languages[i], 290,
						790 - i * 40, 0);
				cb.endText();
			}
			PdfTemplate template = cb.createTemplate(150, 150);
			template.setLineWidth(12f);
			template.arc(40f - (float) Math.sqrt(12800), 110f + (float) Math
					.sqrt(12800), 200f - (float) Math.sqrt(12800), -50f
					+ (float) Math.sqrt(12800), 281.25f, 33.75f);
			template.arc(40f, 110f, 200f, -50f, 90f, 45f);
			template.stroke();
			template.setLineCap(PdfContentByte.LINE_JOIN_ROUND);
			template.arc(80f, 30f, 160f, 110f, 90f, 180f);
			template.arc(115f, 65f, 125f, 75f, 0f, 360f);
			template.stroke();

			PushbuttonField push = new PushbuttonField(writer, new Rectangle(
					40, 650, 150, 680), "pushAction");
			push.setBackgroundColor(Color.YELLOW);
			push.setBorderColor(Color.BLACK);
			push.setText("Push");
			push.setTextColor(Color.RED);
			push.setTemplate(template);
			push.setScaleIcon(PushbuttonField.SCALE_ICON_ALWAYS);
			push.setLayout(PushbuttonField.LAYOUT_ICON_LEFT_LABEL_RIGHT);
			PdfFormField pushbutton = push.getField();
			pushbutton.setAction(PdfAction.javaScript("this.showButtonState()",
					writer));
			writer.addAnnotation(pushbutton);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// step 5: we close the document
		document.close();

	}

	private static void addRadioButton(PdfWriter writer, Rectangle rect,
			PdfFormField radio, String name, boolean on) throws IOException,
			DocumentException {
		RadioCheckField check = new RadioCheckField(writer, rect, null, name);
		check.setCheckType(RadioCheckField.TYPE_STAR);
		check.setChecked(on);
		radio.addKid(check.getRadioField());
	}

	private static void createCheckbox(PdfWriter writer, Rectangle rect,
			String name) throws IOException, DocumentException {
		RadioCheckField check = new RadioCheckField(writer, rect, name, "On");
		check.setCheckType(RadioCheckField.TYPE_CROSS);
		writer.addAnnotation(check.getCheckField());
	}
}
