/* in_action/chapter02/HelloWorldForm.java */

package in_action.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldForm {

	/**
	 * Generates a PDF file with an AcroForm, that will be filled afterwards.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 2: example HelloWorldForm");
		System.out.println("-> Creates a PDF file with an AcroForm;");
		System.out.println("   then reads the file and fills the form.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldForm.pdf");
		System.out.println("   HelloWorldFilledInForm.pdf");
		System.out.println("   HelloWorldFilledInFlattened.pdf");
		// we create a PDF file
		createPdf("results/in_action/chapter02/HelloWorldForm.pdf");
		// now we are going to inspect it
		try {
			PdfReader reader;
			PdfStamper stamper;
			AcroFields form;
			reader = new PdfReader("results/in_action/chapter02/HelloWorldForm.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter02/HelloWorldFilledInForm.pdf"));
			form = stamper.getAcroFields();
			form.setField("Who", "World");
			stamper.close();
			reader = new PdfReader("results/in_action/chapter02/HelloWorldForm.pdf");
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter02/HelloWorldFilledInFlattened.pdf"));
			form = stamper.getAcroFields();
			form.setField("Who", "People");
			stamper.setFormFlattening(true);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a PDF file with a form.
	 * 
	 * @param filename
	 *            the filename of the PDF file.
	 */
	public static void createPdf(String filename) {
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(filename));
			// step 3: we open the document
			document.open();
			// step 4:
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent();
			cb.beginText();
			cb.setFontAndSize(bf, 12);
			cb.moveText(36f, 788);
			cb.showText("Hello");
			cb.endText();

			TextField tf = new TextField(writer, new Rectangle(67, 785, 340,
					800), "Who");
			tf.setFontSize(12);
			tf.setFont(bf);
			tf.setText("Who?");
			tf.setTextColor(new GrayColor(0.5f));
			writer.addAnnotation(tf.getTextField());

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}