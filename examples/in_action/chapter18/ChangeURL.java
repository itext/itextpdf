/* in_action/chapter18/ChangeURL.java */

package in_action.chapter18;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ChangeURL {

	/**
	 * Generates a PDF file with the text 'Hello World'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 18: example Change URL");
		System.out.println("-> Changes the URL of an AcroForm;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resource needed: learning_agreement.pdf");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   learningagreement.pdf");
		// step 1: creation of a document-object
		try {
			PdfReader reader = new PdfReader(
					"resources/in_action/chapter18/learning_agreement.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter18/learningagreement.pdf"));
			AcroFields form = stamper.getAcroFields();
			HashMap fields = form.getFields();
			AcroFields.Item field = (AcroFields.Item) fields.get("PushMe");
			PRIndirectReference ref = (PRIndirectReference) field.widget_refs
					.iterator().next();
			PdfDictionary object = (PdfDictionary) reader.getPdfObject(ref
					.getNumber());
			PdfDictionary action = (PdfDictionary) object.get(PdfName.A);
			PdfDictionary file = (PdfDictionary) action.get(PdfName.F);
			file
					.put(
							PdfName.F,
							new PdfString(
									"http://www.1t3xt.info:8080/itext-in-action/agreementform.jsp"));
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}
}