/* in_action/chapter16/FillAcroForm1.java */
package in_action.chapter16;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FillAcroForm1 {

	public static void main(String[] args) {
		System.out.println("Chapter 16: example Fill AcroForm1");
		System.out.println("-> fills in a form;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> Resulting PDFs: register_form1.pdf as input");
		System.out.println("   registered1_1.pdf, registered1_2.pdf,");
		System.out.println("   registered1_3.pdf and registered1_4.pdf as output");

		RegisterForm1.createPdf();
		try {
			PdfReader reader;
			PdfStamper stamper;

			reader = new PdfReader("results/in_action/chapter16/register_form1.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter16/registered1_1.pdf"));
			AcroFields form = stamper.getAcroFields();
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

			reader = new PdfReader("results/in_action/chapter16/register_form1.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter16/registered1_2.pdf"));
			form = stamper.getAcroFields();
			form.setField("person.name", "Paulo Soares");
			String[] combo_options = { "EN", "FR", "NL", "PT" };
			String[] combo_values = { "English", "French", "Dutch",
					"Portuguese" };
			form.setListOption("person.language", combo_options, combo_values);
			form.setField("person.language", "PT");
			stamper.close();

			reader = new PdfReader("results/in_action/chapter16/registered1_2.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter16/registered1_3.pdf"));
			stamper.setFormFlattening(true);
			stamper.partialFormFlattening("person.name");
			stamper.close();

			reader = new PdfReader("results/in_action/chapter16/registered1_3.pdf");
			form = reader.getAcroFields();
			System.out.println(form.getField("person.name"));

			reader = new PdfReader("results/in_action/chapter16/registered1_2.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter16/registered1_4.pdf"));
			form = stamper.getAcroFields();
			form.setFieldProperty("person.name", "setfflags",
					PdfFormField.FF_READ_ONLY, null);
			form.setFieldProperty("person.programming", "clrfflags",
					PdfFormField.FF_MULTISELECT, null);
			form
					.setFieldProperty("person.language", "bgcolor", Color.RED,
							null);
			stamper.close();

			reader = new PdfReader("results/in_action/chapter16/registered1_4.pdf");
			form = reader.getAcroFields();
			System.out.println(form.getField("person.name"));

			AcroFields.Item item = form.getFieldItem("person.preferred");
			PdfDictionary dict;
			PdfName name;

			System.out.println("pages: " + item.page);
			System.out.println("tabOrder: " + item.tabOrder);
			System.out.println("------------------------------------");
			System.out.println("values");
			System.out.println("------------------------------------");
			for (Iterator i = item.values.iterator(); i.hasNext();) {
				dict = (PdfDictionary) i.next();
				for (Iterator it = dict.getKeys().iterator(); it.hasNext();) {
					name = (PdfName) it.next();
					System.out.println(name.toString() + ": " + dict.get(name));
				}
				System.out.println("------------------------------------");
			}
			System.out.println("widgets");
			System.out.println("------------------------------------");
			for (Iterator i = item.widgets.iterator(); i.hasNext();) {
				dict = (PdfDictionary) i.next();
				for (Iterator it = dict.getKeys().iterator(); it.hasNext();) {
					name = (PdfName) it.next();
					System.out.println(name.toString() + ": " + dict.get(name));
				}
				System.out.println("------------------------------------");
			}
			System.out.println("merged");
			System.out.println("------------------------------------");
			for (Iterator i = item.merged.iterator(); i.hasNext();) {
				dict = (PdfDictionary) i.next();
				for (Iterator it = dict.getKeys().iterator(); it.hasNext();) {
					name = (PdfName) it.next();
					System.out.println(name.toString() + ": " + dict.get(name));
				}
				System.out.println("------------------------------------");
			}
			float[] positions = form.getFieldPositions("person.preferred");
			for (int i = 0; i < positions.length;) {
				System.out.print("Page: " + positions[i++]);
				System.out.print(" [ " + positions[i++]);
				System.out.print(", " + positions[i++]);
				System.out.print(", " + positions[i++]);
				System.out.print(", " + positions[i++]);
				System.out.println(" ]");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
