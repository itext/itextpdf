/* in_action/chapter15/Calculator.java */
package in_action.chapter15;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Calculator {

	public static final BaseFont BF;
	static {
		try {
			BF = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
					BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

	/**
	 * Demonstrates how widget annotations can be used to create a calculator.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example Calculator");
		System.out.println("-> Creates a PDF file with a calculator;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   calculator.pdf");
		Rectangle[] digits = new Rectangle[10];
		digits[0] = createRectangle(3, 1, 1, 1);
		digits[1] = createRectangle(1, 3, 1, 1);
		digits[2] = createRectangle(3, 3, 1, 1);
		digits[3] = createRectangle(5, 3, 1, 1);
		digits[4] = createRectangle(1, 5, 1, 1);
		digits[5] = createRectangle(3, 5, 1, 1);
		digits[6] = createRectangle(5, 5, 1, 1);
		digits[7] = createRectangle(1, 7, 1, 1);
		digits[8] = createRectangle(3, 7, 1, 1);
		digits[9] = createRectangle(5, 7, 1, 1);
		Rectangle plus = createRectangle(7, 7, 1, 1);
		Rectangle minus = createRectangle(9, 7, 1, 1);
		Rectangle mult = createRectangle(7, 5, 1, 1);
		Rectangle div = createRectangle(9, 5, 1, 1);
		Rectangle equals = createRectangle(7, 1, 3, 1);
		Rectangle clearEntry = createRectangle(7, 9, 1, 1);
		Rectangle clear = createRectangle(9, 9, 1, 1);
		Rectangle result = createRectangle(1, 9, 5, 1);
		Rectangle move = createRectangle(8, 3, 1, 1);
		// step 1: creation of a document-object
		Document document = new Document(new Rectangle(360, 360));
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/calculator.pdf"));
			// step 3:
			document.open();
			writer
					.addJavaScript("var previous = 0; var current = 0; var operation = '';\n"
							+ "function showCurrent() { this.getField('result').value = current; }\n"
							+ "function showMove(s) { this.getField('move').value = s; }\n"
							+ "function augment(digit) {\n"
							+ "current = current * 10 + digit;\n"
							+ "showCurrent();\n"
							+ "}\n"
							+ "function register(op) { previous = current; current = 0; operation = op; showCurrent(); }\n"
							+ "function calculate_result() {\n"
							+ "if (operation == '+') current = previous + current;\n"
							+ "else if (operation == '-') current = previous - current;\n"
							+ "else if (operation == '*') current = previous * current;\n"
							+ "else if (operation == '/') current = previous / current;\n"
							+ "showCurrent();\n"
							+ "}\n"
							+ "function reset(all) { current = 0; if(all) previous = 0; showCurrent(); }\n"
							+ "showCurrent();");
			// step 4:
			for (int i = 0; i < 10; i++) {
				addPushButton(writer, digits[i], String.valueOf(i),
						"this.augment(" + i + ")");
			}
			addPushButton(writer, plus, "+", "this.register('+')");
			addPushButton(writer, minus, "-", "this.register('-')");
			addPushButton(writer, mult, "x", "this.register('*')");
			addPushButton(writer, div, ":", "this.register('/')");
			addPushButton(writer, equals, "=", "this.calculate_result()");
			addPushButton(writer, clearEntry, "CE", "this.reset(false)");
			addPushButton(writer, clear, "C", "this.reset(true)");
			addTextField(writer, result, "result");
			addTextField(writer, move, "move");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// step 5: we close the document
		document.close();
	}

	private static void addTextField(PdfWriter writer, Rectangle rect,
			String name) {
		PdfFormField field = PdfFormField.createTextField(writer, false, false,
				0);
		field.setWidget(rect, PdfAnnotation.HIGHLIGHT_NONE);
		field.setQuadding(PdfFormField.Q_RIGHT);
		field.setFieldName(name);
		field.setFieldFlags(PdfFormField.FF_READ_ONLY);
		writer.addAnnotation(field);
	}

	private static void addPushButton(PdfWriter writer, Rectangle rect,
			String btn, String script) {
		float w = rect.getWidth();
		float h = rect.getHeight();
		PdfFormField pushbutton = PdfFormField.createPushButton(writer);
		pushbutton.setFieldName("btn_" + btn);
		pushbutton.setAdditionalActions(PdfName.U, PdfAction.javaScript(script,
				writer));
		pushbutton.setAdditionalActions(PdfName.E, PdfAction.javaScript(
				"this.showMove('" + btn + "');", writer));
		pushbutton.setAdditionalActions(PdfName.X, PdfAction.javaScript(
				"this.showMove(' ');", writer));
		PdfContentByte cb = writer.getDirectContent();
		pushbutton.setAppearance(PdfAnnotation.APPEARANCE_NORMAL,
				createAppearance(cb, btn, Color.GRAY, w, h));
		pushbutton.setAppearance(PdfAnnotation.APPEARANCE_ROLLOVER,
				createAppearance(cb, btn, Color.RED, w, h));
		pushbutton.setAppearance(PdfAnnotation.APPEARANCE_DOWN,
				createAppearance(cb, btn, Color.BLUE, w, h));
		pushbutton.setWidget(rect, PdfAnnotation.HIGHLIGHT_PUSH);
		writer.addAnnotation(pushbutton);
	}

	private static PdfAppearance createAppearance(PdfContentByte cb,
			String btn, Color color, float w, float h) {
		PdfAppearance app = cb.createAppearance(w, h);
		app.setColorFill(color);
		app.rectangle(2, 2, w - 4, h - 4);
		app.fill();
		app.beginText();
		app.setColorFill(Color.BLACK);
		app.setFontAndSize(BF, h / 2);
		app.showTextAligned(Element.ALIGN_CENTER, btn, w / 2, h / 4, 0);
		app.endText();
		return app;
	}

	private static Rectangle createRectangle(int column, int row, int width,
			int height) {
		column = column * 36 - 18;
		row = row * 36 - 18;
		return new Rectangle(column, row, column + width * 36, row + height
				* 36);
	}
}