/* in_action/chapter15/SenderReceiver.java */
package in_action.chapter15;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
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

public class SenderReceiver implements PdfPCellEvent {

	protected PdfFormField parent;

	protected String partialFieldName;

	protected PdfWriter writer;

	protected boolean required;

	public SenderReceiver(PdfWriter writer, PdfFormField parent, String name,
			boolean required) {
		this.writer = writer;
		this.parent = parent;
		this.partialFieldName = name;
		this.required = required;
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell,
	 *      com.lowagie.text.Rectangle, com.lowagie.text.pdf.PdfContentByte[])
	 */
	public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] cb) {
		TextField tf = new TextField(writer, new Rectangle(rect.getLeft(2), rect
				.getBottom(2), rect.getRight(2), rect.getTop(2)), partialFieldName);
		if (required)
			tf.setOptions(TextField.REQUIRED);
		tf.setFontSize(12);
		try {
			parent.addKid(tf.getTextField());
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

	public static void main(String[] args) {
		System.out.println("Chapter 15: example Sender Receiver");
		System.out.println("-> Creates a PDF file with an AcroForm");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   sender_receiver.pdf");
		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/sender_receiver.pdf"));
			// step 3
			document.open();
			// step 4
			document.add(new Paragraph("Sender"));
			PdfFormField sender = PdfFormField.createEmpty(writer);
			sender.setFieldName("sender");
			document.add(createTable(writer, sender));
			writer.addAnnotation(sender);
			document.add(new Paragraph("Receiver"));
			PdfFormField receiver = PdfFormField.createEmpty(writer);
			receiver.setFieldName("receiver");
			document.add(createTable(writer, receiver));
			writer.addAnnotation(receiver);

			PushbuttonField button1 = new PushbuttonField(writer,
					new Rectangle(150, 560, 200, 590), "submitPOST");
			button1.setBackgroundColor(Color.BLUE);
			button1.setText("POST");
			button1.setOptions(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
			PdfFormField submit1 = button1.getField();
			submit1.setAction(PdfAction
					.createSubmitForm(
							"http://www.1t3xt.infp:8080/itext-in-action/form.jsp",
							null, PdfAction.SUBMIT_HTML_FORMAT
									| PdfAction.SUBMIT_COORDINATES));
			writer.addAnnotation(submit1);

			PushbuttonField button2 = new PushbuttonField(writer,
					new Rectangle(250, 560, 300, 590), "submitFDF");
			button2.setBackgroundColor(Color.BLUE);
			button2.setText("FDF");
			button2.setOptions(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
			PdfFormField submit2 = button2.getField();
			submit2.setAction(PdfAction
					.createSubmitForm(
							"http://www.1t3xt.info:8080/itext-in-action/form.jsp",
							null, 0));
			writer.addAnnotation(submit2);

			PushbuttonField button3 = new PushbuttonField(writer,
					new Rectangle(350, 560, 400, 590), "submitXFDF");
			button3.setBackgroundColor(Color.BLUE);
			button3.setText("XFDF");
			button3.setOptions(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
			PdfFormField submit3 = button3.getField();
			submit3.setAction(PdfAction.createSubmitForm(
					"http://www.1t3xt.info:8080/itext-in-action/form.jsp", null,
					PdfAction.SUBMIT_XFDF));
			writer.addAnnotation(submit3);

			PushbuttonField button4 = new PushbuttonField(writer,
					new Rectangle(450, 560, 500, 590), "reset");
			button4.setBackgroundColor(Color.YELLOW);
			button4.setText("RESET");
			button4.setOptions(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
			PdfFormField reset = button4.getField();
			reset.setAction(PdfAction.createResetForm(null, 0));
			writer.addAnnotation(reset);

			String[] buttons = { "submitPOST", "submitFDF", "submitXFDF" };

			PushbuttonField button5 = new PushbuttonField(writer,
					new Rectangle(200, 520, 250, 550), "hide");
			button5.setBackgroundColor(Color.RED);
			button5.setText("HIDE");
			button5.setOptions(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
			PdfFormField hide = button5.getField();
			hide.setAction(PdfAction.createHide(buttons, true));
			writer.addAnnotation(hide);

			PushbuttonField button6 = new PushbuttonField(writer,
					new Rectangle(300, 520, 350, 550), "show");
			button6.setBackgroundColor(Color.GREEN);
			button6.setText("SHOW");
			button6.setOptions(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
			PdfFormField show = button6.getField();
			show.setAction(PdfAction.createHide(buttons, false));
			writer.addAnnotation(show);

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
		// step 5
		document.close();
	}

	private static PdfPTable createTable(PdfWriter writer, PdfFormField parent) {

		PdfPTable table = new PdfPTable(2);
		PdfPCell cell;
		table.getDefaultCell().setPadding(5f);

		table.addCell("Your name:");
		cell = new PdfPCell();
		cell.setCellEvent(new SenderReceiver(writer, parent, "name", true));
		table.addCell(cell);

		table.addCell("Your home address:");
		cell = new PdfPCell();
		cell.setCellEvent(new SenderReceiver(writer, parent, "address", false));
		table.addCell(cell);

		table.addCell("Postal code:");
		cell = new PdfPCell();
		cell.setCellEvent(new SenderReceiver(writer, parent, "postal_code",
				false));
		table.addCell(cell);

		table.addCell("Your email address:");
		cell = new PdfPCell();
		cell.setCellEvent(new SenderReceiver(writer, parent, "email", false));
		table.addCell(cell);

		return table;
	}
}
