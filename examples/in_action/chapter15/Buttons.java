/* in_action/chapter15/Buttons.java */
package in_action.chapter15;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Buttons {

	/**
	 * Demonstrates the different types of buttons.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example Buttons");
		System.out.println("-> Creates a PDF file with widget annotations of type button;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   buttons.pdf");

		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/buttons.pdf"));
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

			PdfAppearance[] radiobuttonStates = new PdfAppearance[2];
			radiobuttonStates[0] = cb.createAppearance(20, 20);
			radiobuttonStates[0].circle(10, 10, 9);
			radiobuttonStates[0].stroke();
			radiobuttonStates[1] = cb.createAppearance(20, 20);
			radiobuttonStates[1].circle(10, 10, 9);
			radiobuttonStates[1].stroke();
			radiobuttonStates[1].circle(10, 10, 3);
			radiobuttonStates[1].fillStroke();

			PdfFormField language = PdfFormField
					.createRadioButton(writer, true);
			language.setFieldName("language");
			language.setValueAsName(languages[0]);
			for (int i = 0; i < languages.length; i++) {
				rect = new Rectangle(40, 806 - i * 40, 60, 788 - i * 40);
				addRadioButton(writer, rect, language, languages[i],
						radiobuttonStates, i == 0);
				cb.beginText();
				cb.setFontAndSize(bf, 18);
				cb.showTextAligned(Element.ALIGN_LEFT, languages[i], 70,
						790 - i * 40, 0);
				cb.endText();
			}
			writer.addAnnotation(language);
			PdfAppearance[] checkboxStates = new PdfAppearance[2];
			checkboxStates[0] = cb.createAppearance(20, 20);
			checkboxStates[0].rectangle(1, 1, 18, 18);
			checkboxStates[0].stroke();
			checkboxStates[1] = cb.createAppearance(20, 20);
			checkboxStates[1].setRGBColorFill(255, 128, 128);
			checkboxStates[1].rectangle(1, 1, 18, 18);
			checkboxStates[1].fillStroke();
			checkboxStates[1].moveTo(1, 1);
			checkboxStates[1].lineTo(19, 19);
			checkboxStates[1].moveTo(1, 19);
			checkboxStates[1].lineTo(19, 1);
			checkboxStates[1].stroke();

			for (int i = 0; i < languages.length; i++) {
				rect = new Rectangle(260, 806 - i * 40, 280, 788 - i * 40);
				createCheckbox(writer, rect, languages[i], checkboxStates);
				cb.beginText();
				cb.setFontAndSize(bf, 18);
				cb.showTextAligned(Element.ALIGN_LEFT, languages[i], 290,
						790 - i * 40, 0);
				cb.endText();
			}

			PdfAppearance normal = cb.createAppearance(100, 50);
			normal.setColorFill(Color.GRAY);
			normal.rectangle(5, 5, 90, 40);
			normal.fill();
			PdfAppearance rollover = cb.createAppearance(100, 50);
			rollover.setColorFill(Color.RED);
			rollover.rectangle(5, 5, 90, 40);
			rollover.fill();
			PdfAppearance down = cb.createAppearance(100, 50);
			down.setColorFill(Color.BLUE);
			down.rectangle(5, 5, 90, 40);
			down.fill();
			PdfFormField pushbutton = PdfFormField.createPushButton(writer);
			pushbutton.setFieldName("PushAction");
			pushbutton.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, normal);
			pushbutton.setAppearance(PdfAnnotation.APPEARANCE_ROLLOVER,
					rollover);
			pushbutton.setAppearance(PdfAnnotation.APPEARANCE_DOWN, down);
			pushbutton.setWidget(new Rectangle(40, 650, 150, 680),
					PdfAnnotation.HIGHLIGHT_PUSH);
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
			PdfFormField radio, String name, PdfAppearance[] onOff, boolean on) {
		PdfFormField field = PdfFormField.createEmpty(writer);
		field.setWidget(rect, PdfAnnotation.HIGHLIGHT_INVERT);
		if (on)
			field.setAppearanceState(name);
		else
			field.setAppearanceState("Off");
		field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off", onOff[0]);
		field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, name, onOff[1]);
		radio.addKid(field);
	}

	private static void createCheckbox(PdfWriter writer, Rectangle rect,
			String name, PdfAppearance[] onOff) {
		PdfFormField field = PdfFormField.createCheckBox(writer);
		field.setWidget(rect, PdfAnnotation.HIGHLIGHT_INVERT);
		field.setFieldName(name);
		field.setValueAsName("Off");
		field.setAppearanceState("Off");
		field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off", onOff[0]);
		field.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "On", onOff[1]);
		writer.addAnnotation(field);
	}
}
