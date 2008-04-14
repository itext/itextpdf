/* in_action/chapter16/FillAcroForm3.java */
package in_action.chapter16;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.FdfReader;
import com.lowagie.text.pdf.FdfWriter;
import com.lowagie.text.pdf.PdfAppearance;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;
import com.lowagie.text.pdf.XfdfReader;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FillAcroForm3 {

	public static void main(String[] args) {
		System.out.println("Chapter 16: example Fill AcroForm3");
		System.out.println("-> fills in a form;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> Resource needed: formfields.xfdf");
		System.out.println("-> Using the PDFs: register_form1.pdf, register_form3.pdf,");
		System.out.println("   registered1.fdf and register_form3.fdf");
		System.out.println("-> Resulting PDFs: registered_1_1.pdf, registered3.pdf and registered3X.pdf");

		createPdf();
		try {
			FdfWriter fdf = new FdfWriter();
			fdf.setFieldAsString("person.name", "Bruno Lowagie");
			fdf.setFieldAsString("person.address",
					"Baeyensstraat 121, Sint-Amandsberg");
			fdf.setFieldAsString("person.postal_code", "BE-9040");
			fdf.setFieldAsString("person.email", "bruno@lowagie.com");
			fdf.setFile("register_form3.pdf");
			fdf.writeTo(new FileOutputStream("results/in_action/chapter16/register_form3.fdf"));

			// merging the FDF file
			PdfReader reader = new PdfReader("results/in_action/chapter16/register_form3.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter16/registered3.pdf"));
			FdfReader fdfreader = new FdfReader("results/in_action/chapter16/register_form3.fdf");
			AcroFields form = stamper.getAcroFields();
			form.setFields(fdfreader);
			stamper.close();

			RegisterForm1.createPdf();

			reader = new PdfReader("results/in_action/chapter16/register_form1.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter16/registered_1_1.pdf"));
			form = stamper.getAcroFields();
			form.setField("person.name", "Laura Specimen");
			form.setField("person.address", "Paulo Soares Way 1");
			form.setField("person.postal_code", "F00b4R", "FOOBAR");
			form.setField("person.email", "laura@lowagie.com");
			form.setField("person.programming", "JAVA");
			form.setField("person.language", "FR");
			form.setField("person.preferred", "EN");
			form.setField("person.knowledge.English", "On");
			form.setField("person.knowledge.French", "On");
			form.setField("person.knowledge.Dutch", "Off");
			stamper.close();

			reader = new PdfReader("results/in_action/chapter16/registered_1_1.pdf");
			form = reader.getAcroFields();
			fdf = new FdfWriter();
			form.exportAsFdf(fdf);
			fdf.setFile("results/in_action/chapter16/register_form1.pdf");
			fdf.writeTo(new FileOutputStream("results/in_action/chapter16/registered1.fdf"));

			fdfreader = new FdfReader("results/in_action/chapter16/registered1.fdf");
			System.out.println(fdfreader.getFileSpec());
			HashMap fields = fdfreader.getFields();
			String key;
			for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
				key = (String) i.next();
				System.out.println(key + ": " + fdfreader.getFieldValue(key));
			}

			XfdfReader xfdfreader = new XfdfReader(
					"resources/in_action/chapter16/formfields.xfdf");
			System.out.println(xfdfreader.getFileSpec());
			fields = xfdfreader.getFields();
			for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
				key = (String) i.next();
				System.out.println(key + ": " + xfdfreader.getFieldValue(key));
			}
			reader = new PdfReader(xfdfreader.getFileSpec());
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter16/registered3X.pdf"));
			form = stamper.getAcroFields();
			form.setFields(xfdfreader);
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
					new FileOutputStream("results/in_action/chapter16/register_form3.pdf"));
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

		return table;
	}
}
