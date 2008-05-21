/* in_action/chapter02/HelloWorldCopyForm.java */

package in_action.chapter02;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
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

public class HelloWorldCopyForm {

	/**
	 * Generates some files with an AcroForm, that copies them into one. 
	 * DO NOT USE THIS EXAMPLE; THIS EXAMPLE SHOWS HOW YOU SHOULDN'T DO IT. 
	 * Take a look at HelloWorldCopyFields instead.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 2: example HelloWorldCopyForm");
		System.out.println("-> This is an example of how you SHOULDN'T copy forms.");
		System.out.println("   Take a look at HelloWorldCopyFields to know how it should be done.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldForm1.pdf");
		System.out.println("   HelloWorldForm2.pdf");
		System.out.println("   HelloWorldForm3.pdf");
		System.out.println("   HelloWorldCopyForm.pdf");
		// we create a PDF file
		createPdf("results/in_action/chapter02/HelloWorldForm1.pdf", "field1", "value1.1");
		createPdf("results/in_action/chapter02/HelloWorldForm2.pdf", "field1", "value1.2");
		createPdf("results/in_action/chapter02/HelloWorldForm3.pdf", "field2", "value2");
		// now we are going to inspect it
		try {
			PdfReader reader = new PdfReader("results/in_action/chapter02/HelloWorldForm1.pdf");
			// step 1
			Document document = new Document(reader.getPageSizeWithRotation(1));
			// step 2
			PdfCopy writer = new PdfCopy(document, new FileOutputStream(
					"results/in_action/chapter02/HelloWorldCopyForm.pdf"));
			// step 3
			document.open();
			// step 4
			writer.addPage(writer.getImportedPage(reader, 1));
			reader = new PdfReader("results/in_action/chapter02/HelloWorldForm2.pdf");
			writer.addPage(writer.getImportedPage(reader, 1));
			reader = new PdfReader("results/in_action/chapter02/HelloWorldForm3.pdf");
			writer.addPage(writer.getImportedPage(reader, 1));
			// step 5
			document.close();
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
			cb.moveText(36f, 788);
			cb.showText("Hello");
			cb.endText();

			TextField tf = new TextField(writer, new Rectangle(67, 785, 340,
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