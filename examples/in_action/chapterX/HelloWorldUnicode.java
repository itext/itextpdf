package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import in_action.chapter02.HelloWorldForm;

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
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class HelloWorldUnicode {

	/**
	 * Fill in a Unicode field.
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		// we create a PDF file
		HelloWorldForm.createPdf("results/in_action/chapterX/HelloWorldForm.pdf");
		// now we are going to add a button
		try {
			PdfReader reader = new PdfReader("results/in_action/chapterX/HelloWorldForm.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapterX/HelloWorldFormWithUnicode.pdf"));
			AcroFields form = stamper.getAcroFields();
			BaseFont unicode = BaseFont.createFont("c:/windows/fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			form.setFieldProperty("Who", "textfont", unicode, null);
			form.setField("Who", "\u7121\u540d");
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		createPdf("results/in_action/chapterX/HelloWorldForm2.pdf");
		// now we are going to add a button
		try {
			PdfReader reader = new PdfReader("results/in_action/chapterX/HelloWorldForm2.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
					"results/in_action/chapterX/HelloWorldFormWithUnicode2.pdf"));
			AcroFields form = stamper.getAcroFields();
			form.setField("Who", "\u7121\u540d");
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
			BaseFont bf = BaseFont.createFont("c:/windows/fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
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
			tf.setText("who?");
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
