package classroom.filmfestival_c;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PushbuttonField;
import com.lowagie.text.pdf.TextField;

public class Movies22 extends AbstractAccreditation {

	public static void main(String[] args) {
		// step 1
		Document document = new Document(new Rectangle(WIDTH, HEIGHT));
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FORM));
			writer.setViewerPreferences(PdfWriter.PageLayoutSinglePage);
			// step 3
			document.open();
			// step 4
			PdfContentByte cb = writer.getDirectContent();

			// top part of the card
			cb.setColorFill(RED);
			cb.rectangle(0, mm2pt(57f), WIDTH, HEIGHT - mm2pt(57f));
			cb.fill();
			cb.setColorFill(BLACK);
			cb.rectangle(0, mm2pt(61), WIDTH, mm2pt(21f));
			cb.fill();
			cb.setColorFill(WHITE);

			Image kubrick = Image.getInstance(KUBRICK);
			kubrick.scaleToFit(WIDTH, mm2pt(21));
			kubrick.setAbsolutePosition(0, mm2pt(61));
			cb.addImage(kubrick);

			Image logo = Image.getInstance(LOGO);
			logo.scaleToFit(mm2pt(14), mm2pt(14));
			logo.setAbsolutePosition(mm2pt(37), mm2pt(65));
			cb.addImage(logo);

			cb.beginText();
			cb.setFontAndSize(FONT, 7);
			cb.showTextAligned(Element.ALIGN_CENTER,
					"Flanders International Film Festival-Ghent", WIDTH / 2,
					mm2pt(58.5f), 0);
			cb.endText();

			// bottom part of the card

			TextField filmfestival = new TextField(writer, new Rectangle(0, 0,
					WIDTH, mm2pt(9)), "filmfestival");
			filmfestival.setBackgroundColor(GRAY);
			filmfestival.setTextColor(WHITE);
			filmfestival.setText("www.filmfestival.be");
			filmfestival.setFontSize(14);
			filmfestival.setOptions(TextField.READ_ONLY);
			filmfestival.setAlignment(Element.ALIGN_CENTER);
			writer.addAnnotation(filmfestival.getTextField());

			cb.beginText();
			cb.moveText(mm2pt(1.5f), mm2pt(12));
			cb.setFontAndSize(FONT, 21);
			cb.setColorFill(RED);
			cb.showText("10");
			cb.setColorFill(BLUE);
			cb.showText("/");
			cb.setColorFill(RED);
			cb.showText("21");
			cb.setColorFill(BLUE);
			cb.showText("OCT");
			cb.setColorFill(GREEN);
			cb.showText("2006");
			cb.endText();
			cb.setColorStroke(BLUE);
			cb.moveTo(mm2pt(24.5f), mm2pt(29));
			cb.lineTo(mm2pt(24.5f), mm2pt(36));
			cb.moveTo(mm2pt(24.5f), mm2pt(48));
			cb.lineTo(mm2pt(24.5f), mm2pt(54));
			cb.stroke();

			// central part of the card
			PushbuttonField photo = new PushbuttonField(writer, new Rectangle(
					mm2pt(3), mm2pt(29), mm2pt(24), mm2pt(54)), "photo");
			PdfTemplate t1 = cb.createTemplate(mm2pt(21), mm2pt(25));
			t1.setColorFill(GRAY);
			t1.rectangle(0, 0, mm2pt(21), mm2pt(25));
			t1.fill();
			photo.setTemplate(t1);
			photo.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
			writer.addAnnotation(photo.getField());

			TextField type = new TextField(writer, new Rectangle(mm2pt(26),
					mm2pt(46), WIDTH - mm2pt(1.5f), mm2pt(54)), "type");
			type.setTextColor(GRAY);
			type.setText("TYPE");
			type.setFontSize(0);
			writer.addAnnotation(type.getTextField());

			TextField number = new TextField(writer, new Rectangle(
					mm2pt(26), mm2pt(44), WIDTH - mm2pt(1.5f), mm2pt(48)), "number");
			number.setText("N° 0000000");
			number.setFontSize(8);
			writer.addAnnotation(number.getTextField());

			TextField name = new TextField(writer, new Rectangle(
					mm2pt(26), mm2pt(28), WIDTH - mm2pt(1.5f), mm2pt(40)), "name");
			name.setText("Name");
			name.setFontSize(8);
			name.setOptions(TextField.MULTILINE);
			writer.addAnnotation(name.getTextField());

			PushbuttonField barcode = new PushbuttonField(writer,
					new Rectangle(mm2pt(3), mm2pt(23), WIDTH - mm2pt(3),
							mm2pt(28)), "barcode");
			PdfTemplate t2 = cb.createTemplate(WIDTH - mm2pt(6), mm2pt(5));
			t2.setColorFill(GRAY);
			t2.rectangle(0, 0, WIDTH - mm2pt(6), mm2pt(5));
			t2.fill();
			barcode.setTemplate(t2);
			barcode.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
			writer.addAnnotation(barcode.getField());

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// step 4
		document.close();
	}
}
