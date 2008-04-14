/* in_action/chapter15/RegisterForm1.java */
package in_action.chapter16;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class RegisterForm1 implements PdfPCellEvent {

	protected PdfFormField parent;

	protected PdfFormField kid;

	protected float padding;

	public RegisterForm1(PdfFormField parent, PdfFormField kid, float padding) {
		this.kid = kid;
		this.parent = parent;
		this.padding = padding;
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell,
	 *      com.lowagie.text.Rectangle, com.lowagie.text.pdf.PdfContentByte[])
	 */
	public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] cb) {
		kid.setWidget(new Rectangle(rect.getLeft(padding), rect.getBottom(padding),
				rect.getRight(padding), rect.getTop(padding)),
				PdfAnnotation.HIGHLIGHT_INVERT);
		try {
			parent.addKid(kid);
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

	public static void main(String[] args) {
		System.out.println("Chapter 16: example Register Form 1");
		System.out.println("-> creates a form that can be used to register your name;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   register_form1.pdf");
		createPdf();

		try {
			PdfReader reader = new PdfReader("results/in_action/chapter16/register_form1.pdf");
			AcroFields form = reader.getAcroFields();
			HashMap fields = form.getFields();
			String key;
			for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
				key = (String) i.next();
				System.out.print(key + ": ");
				switch (form.getFieldType(key)) {
				case AcroFields.FIELD_TYPE_CHECKBOX:
					System.out.println("Checkbox");
					break;
				case AcroFields.FIELD_TYPE_COMBO:
					System.out.println("Combobox");
					break;
				case AcroFields.FIELD_TYPE_LIST:
					System.out.println("List");
					break;
				case AcroFields.FIELD_TYPE_NONE:
					System.out.println("None");
					break;
				case AcroFields.FIELD_TYPE_PUSHBUTTON:
					System.out.println("Pushbutton");
					break;
				case AcroFields.FIELD_TYPE_RADIOBUTTON:
					System.out.println("Radiobutton");
					break;
				case AcroFields.FIELD_TYPE_SIGNATURE:
					System.out.println("Signature");
					break;
				case AcroFields.FIELD_TYPE_TEXT:
					System.out.println("Text");
					break;
				default:
					System.out.println("?");
				}
			}
			System.out.println("Possible values for person.programming:");
			String[] options = form.getListOptionExport("person.programming");
			String[] values = form.getListOptionDisplay("person.programming");
			for (int i = 0; i < options.length; i++)
				System.out.println(options[i] + ": " + values[i]);
			System.out.println("Possible values for person.language:");
			options = form.getListOptionExport("person.language");
			values = form.getListOptionDisplay("person.language");
			for (int i = 0; i < options.length; i++)
				System.out.println(options[i] + ": " + values[i]);
			System.out.println("Possible values for person.preferred:");
			String[] states = form.getAppearanceStates("person.preferred");
			for (int i = 0; i < states.length; i++)
				System.out.println(states[i]);
			System.out.println("Possible values for person.knowledge.English:");
			states = form.getAppearanceStates("person.knowledge.English");
			for (int i = 0; i < states.length; i++)
				System.out.println(states[i]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createPdf() {
		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter16/register_form1.pdf"));
			// step 3
			document.open();
			// step 4
			PdfFormField person = PdfFormField.createEmpty(writer);
			person.setFieldName("person");
			document.add(createTable(writer, person));
			writer.addAnnotation(person);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5
		document.close();
	}

	private static PdfPTable createTable(PdfWriter writer, PdfFormField parent)
			throws IOException, DocumentException {
		PdfContentByte cb = writer.getDirectContent();
		PdfAppearance[] buttonStates = new PdfAppearance[2];
		buttonStates[0] = cb.createAppearance(20, 20);
		buttonStates[1] = cb.createAppearance(20, 20);
		buttonStates[1].moveTo(0, 0);
		buttonStates[1].lineTo(20, 20);
		buttonStates[1].moveTo(0, 20);
		buttonStates[1].lineTo(20, 0);
		buttonStates[1].stroke();

		PdfPTable table = new PdfPTable(2);
		PdfPCell cell;
		TextField field;
		table.getDefaultCell().setPadding(5f);

		table.addCell("Your name:");
		cell = new PdfPCell();
		field = new TextField(writer, new Rectangle(0, 0), "name");
		field.setFontSize(12);
		cell.setCellEvent(new RegisterForm1(parent, field.getTextField(), 1));
		table.addCell(cell);

		table.addCell("Your home address:");
		cell = new PdfPCell();
		field = new TextField(writer, new Rectangle(0, 0), "address");
		field.setFontSize(12);
		cell.setCellEvent(new RegisterForm1(parent, field.getTextField(), 1));
		table.addCell(cell);

		table.addCell("Postal code:");
		cell = new PdfPCell();
		field = new TextField(writer, new Rectangle(0, 0), "postal_code");
		field.setFontSize(12);
		cell.setCellEvent(new RegisterForm1(parent, field.getTextField(), 1));
		table.addCell(cell);

		table.addCell("Your email address:");
		cell = new PdfPCell();
		field = new TextField(writer, new Rectangle(0, 0), "email");
		field.setFontSize(12);
		cell.setCellEvent(new RegisterForm1(parent, field.getTextField(), 1));
		table.addCell(cell);

		table.addCell("Programming skills:");
		cell = new PdfPCell();
		field = new TextField(writer, new Rectangle(0, 0), "programming");
		field.setFontSize(9);
		String[] list_options = { "JAVA", "C", "CS", "VB" };
		field.setChoiceExports(list_options);
		String[] list_values = { "Java", "C/C++", "C#", "VB" };
		field.setChoices(list_values);
		PdfFormField f = field.getListField();
		f.setFieldFlags(PdfFormField.FF_MULTISELECT);
		cell.setCellEvent(new RegisterForm1(parent, f, 0));
		cell.setMinimumHeight(50);
		table.addCell(cell);

		table.addCell("Mother tongue:");
		cell = new PdfPCell();
		field = new TextField(writer, new Rectangle(0, 0), "language");
		field.setFontSize(9);
		String[] combo_options = { "EN", "FR", "NL" };
		field.setChoiceExports(combo_options);
		String[] combo_values = { "English", "French", "Dutch" };
		field.setChoices(combo_values);
		f = field.getComboField();
		cell.setCellEvent(new RegisterForm1(parent, f, 0));
		table.addCell(cell);

		PdfFormField f1 = PdfFormField.createRadioButton(writer, true);
		f1.setFieldName("preferred");
		parent.addKid(f1);
		table.addCell("Preferred Language:");
		float[] widths = { 1, 10 };
		PdfPTable subtable = new PdfPTable(widths);
		cell = new PdfPCell();
		PdfFormField checkbox = PdfFormField.createEmpty(writer);
		checkbox.setValueAsName("EN");
		checkbox.setAppearanceState("EN");
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off",
				buttonStates[0]);
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "EN",
				buttonStates[1]);
		cell.setCellEvent(new RegisterForm1(f1, checkbox, 0));
		subtable.addCell(cell);
		subtable.addCell("English");
		cell = new PdfPCell();
		checkbox = PdfFormField.createEmpty(writer);
		checkbox.setValueAsName("Off");
		checkbox.setAppearanceState("Off");
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off",
				buttonStates[0]);
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "FR",
				buttonStates[1]);
		cell.setCellEvent(new RegisterForm1(f1, checkbox, 0));
		subtable.addCell(cell);
		subtable.addCell("French");
		cell = new PdfPCell();
		checkbox = PdfFormField.createEmpty(writer);
		checkbox.setValueAsName("Off");
		checkbox.setAppearanceState("Off");
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off",
				buttonStates[0]);
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "NL",
				buttonStates[1]);
		cell.setCellEvent(new RegisterForm1(f1, checkbox, 0));
		subtable.addCell(cell);
		subtable.addCell("Dutch");
		table.addCell(new PdfPCell(subtable));

		PdfFormField f2 = PdfFormField.createEmpty(writer);
		f2.setFieldName("knowledge");
		parent.addKid(f2);
		table.addCell("Knowledge of:");
		subtable = new PdfPTable(widths);
		cell = new PdfPCell();
		checkbox = PdfFormField.createCheckBox(writer);
		checkbox.setFieldName("English");
		checkbox.setValueAsName("Off");
		checkbox.setAppearanceState("Off");
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off",
				buttonStates[0]);
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "On",
				buttonStates[1]);
		cell.setCellEvent(new RegisterForm1(f2, checkbox, 0));
		subtable.addCell(cell);
		subtable.addCell("English");
		cell = new PdfPCell();
		checkbox = PdfFormField.createCheckBox(writer);
		checkbox.setFieldName("French");
		checkbox.setValueAsName("Off");
		checkbox.setAppearanceState("Off");
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off",
				buttonStates[0]);
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "On",
				buttonStates[1]);
		cell.setCellEvent(new RegisterForm1(f2, checkbox, 0));
		subtable.addCell(cell);
		subtable.addCell("French");
		cell = new PdfPCell();
		checkbox = PdfFormField.createCheckBox(writer);
		checkbox.setFieldName("Dutch");
		checkbox.setValueAsName("Off");
		checkbox.setAppearanceState("Off");
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "Off",
				buttonStates[0]);
		checkbox.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, "On",
				buttonStates[1]);
		cell.setCellEvent(new RegisterForm1(f2, checkbox, 0));
		subtable.addCell(cell);
		subtable.addCell("Dutch");
		table.addCell(new PdfPCell(subtable));

		return table;
	}
}
