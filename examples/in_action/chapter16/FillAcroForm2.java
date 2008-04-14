/* in_action/chapter16/FillAcroForm2.java */
package in_action.chapter16;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PushbuttonField;
import com.lowagie.text.pdf.TextField;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FillAcroForm2 {

	public static void main(String[] args) {
		System.out.println("Chapter 16: example Fill AcroForm 2");
		System.out.println("-> fills in a form;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> Resource needed: laura.jpg");
		System.out.println("-> Using the PDF: register_form2.pdf");
		System.out.println("-> Resulting PDFs: registered2.pdf, registered2_1.pdf,");
		System.out.println("   registered2_2.pdf, registered2_X.pdf, and registered2_Y.pdf");

		String[][] db = {
				{ "Laura Specimen", "Paulo Soares Way 1", "F00b4R",
						"laura@lowagie.com" },
				{ "Bruno Lowagie", "Baeyensstraat 121",
						"BE 9040 Sint-Amandsberg", "bruno@lowagie.com" } };

		createPdf();
		try {
			PdfReader reader;
			PdfStamper stamper;

			reader = new PdfReader("results/in_action/chapter16/register_form2.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter16/registered2.pdf"));
			AcroFields form = stamper.getAcroFields();
			form.setField("person.name", "Laura Specimen");
			form.setField("person.address", "Paulo Soares Way 1");
			form.setField("person.postal_code", "F00b4r");
			form.setField("person.email", "laura@lowagie.com");
			float[] photograph = form.getFieldPositions("person.photograph");
			Rectangle rect = new Rectangle(photograph[1], photograph[2],
					photograph[3], photograph[4]);
			Image laura = Image.getInstance("resources/in_action/chapter16/Laura.jpg");
			laura.scaleToFit(rect.getWidth(), rect.getHeight());
			laura.setAbsolutePosition(photograph[1]
					+ (rect.getWidth() - laura.getScaledWidth()) / 2, photograph[2]
					+ (rect.getHeight() - laura.getScaledHeight()) / 2);
			PdfContentByte cb = stamper.getOverContent((int) photograph[0]);
			cb.addImage(laura);
			stamper.setFormFlattening(true);
			stamper.close();

			HashMap fieldCache = new HashMap();
			for (int i = 0; i < db.length; i++) {
				reader = new PdfReader("results/in_action/chapter16/register_form2.pdf");
				stamper = new PdfStamper(reader, new FileOutputStream(
						"results/in_action/chapter16/registered2_" + (i + 1) + ".pdf"));
				form = stamper.getAcroFields();
				form.setFieldCache(fieldCache);
				form.setExtraMargin(12, -3);
				form.setField("person.name", db[i][0]);
				form.setField("person.address", db[i][1]);
				form.setField("person.postal_code", db[i][2]);
				form.setField("person.email", db[i][3]);
				stamper.setFormFlattening(true);
				stamper.close();
			}

			reader = new PdfReader("results/in_action/chapter16/register_form2.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter16/registered2_X.pdf"));
			form = stamper.getAcroFields();
			form.setField("person.name",
					"Somebody with a very, very long name.");
			form
					.setField("person.address",
							"and a very, very long address too");
			stamper.setFormFlattening(true);
			stamper.close();

			reader = new PdfReader("results/in_action/chapter16/register_form2.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter16/registered2_Y.pdf"));
			form = stamper.getAcroFields();
			form
					.setFieldProperty("person.name", "textsize", new Float(0),
							null);
			form.setField("person.name",
					"Somebody with a very, very long name.");
			form.setFieldProperty("person.address", "textsize", new Float(0),
					null);
			form
					.setField("person.address",
							"and a very, very long address too");
			stamper.setFormFlattening(true);
			stamper.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private static void createPdf() {
		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter16/register_form2.pdf"));
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

		float[] widths = { 1, 4 };
		PdfPTable outer = new PdfPTable(widths);
		cell = new PdfPCell();
		PushbuttonField photograph = new PushbuttonField(writer, new Rectangle(
				0, 0), "photograph");
		photograph.setBackgroundColor(Color.GRAY);
		cell.setCellEvent(new RegisterForm1(parent, photograph.getField(), 2));
		outer.addCell(cell);
		outer.addCell(table);
		return outer;
	}
}
