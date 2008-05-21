/* in_action/chapter17/FoobarLearningAgreement.java */

package in_action.chapter17;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.events.FieldPositioningEvents;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoobarLearningAgreement implements PdfPTableEvent {

	/**
	 * Creates an instance of an Event to add a field.
	 */
	public FoobarLearningAgreement() {
	}

	/**
	 * Generates a learning agreement form.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 17: example Learning Agreement");
		System.out.println("-> Creates a learning agreement form in PDF;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: learning_agreement.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter17/learning_agreement.pdf"));

			FieldPositioningEvents fpe = new FieldPositioningEvents();
			writer.setPageEvent(fpe);
			// step 3: we open the document
			document.open();

			StringBuffer js = new StringBuffer(
					"var code = new Array();\nvar name = new Array();\nvar credits = new Array();\n");
			StringBuffer items = new StringBuffer("''");
			BufferedReader reader = new BufferedReader(new FileReader(
					"resources/in_action/chapter17/courses.csv"));
			String line;
			int i = 0;
			while ((line = reader.readLine()) != null) {
				StringTokenizer js_courses = new StringTokenizer(line, ";");
				line = js_courses.nextToken();
				items.append(", '").append(line).append("'");
				js.append("code[").append(i).append("] = '");
				js.append(line).append("';\n");
				js.append("name[").append(i).append("] = '");
				js.append(js_courses.nextToken()).append("';\n");
				js.append("credits[").append(i).append("] = '");
				js.append(js_courses.nextToken()).append("';\n");
				i++;
			}
			reader.close();
			js.append("for (i = 0; i < 16; i++) {\n");
			js.append("  f = this.getField('course_' + i + '.code');\n");
			js.append("  f.setItems([").append(items.toString())
					.append("]);\n");
			js.append("  f = this.getField('course_' + i + '.name');\n");
			js.append("  f.textSize = 0;\n");
			js.append("  f.multiline = true;\n");
			js.append("};\n");
			js.append("this.getField('academic_year').value = '2006-2007';");
			js.append("this.getField('field_of_study').value = 'ICT';");
			js.append("this.getField('student_name').setFocus();");
			js.append("function updateCourse(event) {\n");
			js.append("  target = event.target.name;\n");
			js.append("  parent = target.substring(0, target.length - 5);\n");
			js.append("  for (c = 0; c < code.length; c++) {\n");
			js.append("    if (event.value == code[c]) {\n");
			js
					.append("         this.getField(parent + '.name').value = name[c];");
			js
					.append("         this.getField(parent + '.credits').value = credits[c];");
			js.append("    }\n");
			js.append("  }\n");
			js.append("  this.getField(parent + '.name').setFocus();");
			js.append("}\n");
			writer.addJavaScript(js.toString());

			// step 4:

			// we create a pushbutton that submits the form
			PdfFormField pushbutton = PdfFormField.createPushButton(writer);
			pushbutton.setFieldName("PushMe");
			// we don't define the position on the page yet
			pushbutton.setWidget(new Rectangle(0, 0),
					PdfAnnotation.HIGHLIGHT_PUSH);
			pushbutton
					.setAction(PdfAction
							.createSubmitForm(
									"http://www.1t3xt.info:8080/itext-in-action/learning_agreement.jsp",
									null, 0));
			// we add the button to the FieldPositioningEvent for later use
			fpe.addField("pushMe", pushbutton);

			Font font = FontFactory.getFont(BaseFont.HELVETICA,
					BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 14);
			Paragraph p;

			// TITLE
			p = new Paragraph("EXCHANGE STUDENTS", font);
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			p = new Paragraph("LEARNING AGREEMENT", font);
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);

			// this chunk will generate a field with name academic_year
			Chunk academic_year = new Chunk("                  ");
			academic_year.setGenericTag("academic_year");
			// this chunk will generate a field with name field_of_study
			Chunk field_of_study = new Chunk("                         ");
			field_of_study.setGenericTag("field_of_study");

			// SUBTITLE
			p = new Paragraph(30, "ACADEMIC YEAR ", font);
			p.add(academic_year);
			p.add(new Phrase(" - FIELD OF STUDY: "));
			p.add(field_of_study);
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);

			// TABLE WITH STUDENT INFORMATION
			PdfPTable table;
			int[] widths = { 12, 16, 8, 14 };
			table = new PdfPTable(4);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);
			table.setTableEvent(new FoobarLearningAgreement());
			table.setWidths(widths);
			table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			// first row, first column
			table.addCell("Name of student:");
			PdfPCell cell;
			// first row, second column: a field to fill in
			cell = new PdfPCell();
			cell.setColspan(3);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell
					.setCellEvent(new FieldPositioningEvents(writer,
							"student_name"));
			table.addCell(cell);
			// second row
			cell = new PdfPCell(new Paragraph("Sending Institution:"));
			cell.setColspan(4);
			cell.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell);
			// third row, first column
			cell = new PdfPCell();
			cell.setPaddingBottom(5);
			cell.setColspan(2);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setCellEvent(new FieldPositioningEvents(writer,
					"sending_institution"));
			table.addCell(cell);
			// third row, second column
			cell = new PdfPCell(new Paragraph("Country:"));
			cell.setPaddingBottom(5);
			cell.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell);
			// third row, third column
			cell = new PdfPCell();
			cell.setPaddingBottom(5);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setCellEvent(new FieldPositioningEvents(writer,
					"sending_country"));
			table.addCell(cell);
			// fourth row
			cell = new PdfPCell(new Paragraph("Letter of Introduction:"));
			cell.setColspan(2);
			cell.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell();
			cell.setColspan(2);
			cell.setBorder(PdfPCell.NO_BORDER);
			TextField letter = new TextField(writer, new Rectangle(0, 0),
					"letter");
			letter.setOptions(TextField.FILE_SELECTION);
			PdfFormField introduction = letter.getTextField();
			introduction
					.setAdditionalActions(
							PdfName.U,
							PdfAction
									.javaScript(
											"this.getField('letter').browseForFileToSubmit();this.getField('receiving_institution').setFocus();",
											writer));
			cell.setCellEvent(new FieldPositioningEvents(writer, introduction));
			table.addCell(cell);
			document.add(table);

			// INFORMATION CONCERNING THE RECEIVING INSTITUTION
			p = new Paragraph("DETAILS OF THE PROPOSED STUDY PROGRAM ABROAD");
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
			table = new PdfPTable(4);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);
			table.setTableEvent(new FoobarLearningAgreement());
			table.setWidths(widths);
			table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			// first row
			cell = new PdfPCell(new Paragraph("Receiving Institution:"));
			cell.setColspan(4);
			cell.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell);
			// second row first column
			cell = new PdfPCell();
			cell.setPaddingBottom(5);
			cell.setColspan(2);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setCellEvent(new FieldPositioningEvents(writer,
					"receiving_institution"));
			table.addCell(cell);
			// second row, second column
			cell = new PdfPCell(new Paragraph("Country:"));
			cell.setPaddingBottom(5);
			cell.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell);
			// second row, second column
			cell = new PdfPCell();
			cell.setPaddingBottom(5);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setCellEvent(new FieldPositioningEvents(writer,
					"receiving_country"));
			table.addCell(cell);
			document.add(table);
			// list of courses
			table = new PdfPTable(3);
			table.setTableEvent(new FoobarLearningAgreement());
			table.getDefaultCell().setBorder(PdfPCell.RIGHT);
			table.addCell("Course code");
			table.addCell("Course unit title");
			table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			table.addCell("Number of ECTS credits");
			PdfFormField[] lines = new PdfFormField[16];
			FieldPositioningEvents kid;
			TextField combo;
			PdfFormField comboField;
			for (i = 0; i < 16; i++) {
				lines[i] = PdfFormField.createEmpty(writer);
				lines[i].setFieldName("course_" + i);
				cell = new PdfPCell();
				cell.setFixedHeight(22);
				cell.setBorder(PdfPCell.RIGHT);
				combo = new TextField(writer, new Rectangle(0, 0), "code");
				combo.setChoices(new String[] {});
				comboField = combo.getComboField();
				comboField.setAdditionalActions(PdfName.K, PdfAction
						.javaScript("updateCourse(event);", writer));
				kid = new FieldPositioningEvents(lines[i], comboField);
				kid.setPadding(0.5f);
				cell.setCellEvent(kid);
				table.addCell(cell);
				cell = new PdfPCell();
				cell.setFixedHeight(22);
				cell.setBorder(PdfPCell.RIGHT);
				kid = new FieldPositioningEvents(writer, lines[i], "name");
				kid.setPadding(0.5f);
				cell.setCellEvent(kid);
				table.addCell(cell);
				cell = new PdfPCell();
				cell.setFixedHeight(22);
				cell.setBorder(PdfPCell.NO_BORDER);
				kid = new FieldPositioningEvents(writer, lines[i], "credits");
				kid.setPadding(0.5f);
				cell.setCellEvent(kid);
				table.addCell(cell);
			}
			document.add(table);
			for (i = 0; i < 16; i++) {
				writer.addAnnotation(lines[i]);
			}
			Chunk submit = new Chunk("    Click to submit     ");
			submit.setGenericTag("pushMe");
			p = new Paragraph(submit);
			p.setAlignment(Element.ALIGN_CENTER);
			document.add(p);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPTableEvent#tableLayout(com.lowagie.text.pdf.PdfPTable,
	 *      float[][], float[], int, int, com.lowagie.text.pdf.PdfContentByte[])
	 */
	public void tableLayout(PdfPTable table, float[][] width, float[] heights,
			int headerRows, int rowStart, PdfContentByte[] canvases) {
		float widths[] = width[0];
		PdfContentByte cb = canvases[PdfPTable.TEXTCANVAS];
		cb.rectangle(widths[0], heights[heights.length - 1],
				widths[widths.length - 1] - widths[0], heights[0]
						- heights[heights.length - 1]);
		cb.stroke();
	}
}