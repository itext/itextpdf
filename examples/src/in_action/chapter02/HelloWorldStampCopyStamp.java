/* in_action/chapter02/HelloWorldStampCopyStamp.java */

package in_action.chapter02;

import java.io.ByteArrayOutputStream;
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
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.TextField;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldStampCopyStamp {

	/**
	 * Generates a PDF in multiple passes. First do some stamping, then some
	 * copying, finally some more stamping.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 2: example HelloWorldStampCopyStamp");
		System.out.println("-> Creates a PDF in multiple passes;");
		System.out.println("   first do some stamping, then the copying, finally some more stamping.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloLetter.pdf");
		System.out.println("   HelloWorldStampCopyStamp.pdf");
		// we create a PDF file
		createPdf("results/in_action/chapter02/HelloLetter.pdf", "field", "value");
		// now we are going to inspect it
		PdfReader reader;
		PdfStamper stamper;
		AcroFields form;
		try {
			RandomAccessFileOrArray letter = new RandomAccessFileOrArray(
					"results/in_action/chapter02/HelloLetter.pdf");
			reader = new PdfReader(letter, null);
			ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
			stamper = new PdfStamper(reader, baos1);
			form = stamper.getAcroFields();
			form.renameField("field", "field1");
			stamper.close();
			reader = new PdfReader(letter, null);
			ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
			stamper = new PdfStamper(reader, baos2);
			form = stamper.getAcroFields();
			form.renameField("field", "field2");
			stamper.close();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfCopyFields copy = new PdfCopyFields(baos);
			copy.addDocument(new PdfReader(baos1.toByteArray()));
			copy.addDocument(new PdfReader(baos2.toByteArray()));
			copy.close();

			reader = new PdfReader(baos.toByteArray());
			stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapter02/HelloWorldStampCopyStamp.pdf"));
			form = stamper.getAcroFields();
			form.setField("field1", "World");
			form.setField("field2", "People");
			stamper.setFormFlattening(true);
			stamper.partialFormFlattening("field2");
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a PDF file with an AcroForm.
	 * 
	 * @param filename
	 *            the filename of the PDF file.
	 * @param field
	 *            name of a fields that has to be added to the form
	 * @param value
	 *            value of a fields that has to be added to the form
	 */
	private static void createPdf(String filename, String field, String value) {
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
			cb.setLeading(16);
			cb.moveText(36f, 788f);
			cb.showText("Dear");
			cb.newlineShowText("I just wanted to say Hello.");
			cb.endText();

			TextField tf = new TextField(writer, new Rectangle(64, 785, 340,
					800), field);
			tf.setFontSize(12);
			tf.setFont(bf);
			tf.setText(value);
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