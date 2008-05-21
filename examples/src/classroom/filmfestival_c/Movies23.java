/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.filmfestival_c;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BarcodeInter25;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PushbuttonField;

public class Movies23 extends AbstractAccreditation {
	public static final String RESULT1 = "results/classroom/filmfestival/movies23_1.pdf";
	public static final String RESULT2 = "results/classroom/filmfestival/movies23_2.pdf";
	
	public static void main(String[] args) {
		try {
			AccreditationData data = new AccreditationData();
			data.setTypeColor(COLOR[2]);
			data.setTypeName(TYPE[2]);
			data.setName("Ingeborg Willaert");
			data.setNumber("12345");
			data.setPhoto(Image.getInstance(PHOTO));
			data.setFlatten(false);
			// we fill the form
			fillForm(FORM, data, new FileOutputStream(RESULT1));
			data.setTypeColor(COLOR[3]);
			data.setTypeName(TYPE[4]);
			data.setFlatten(true);
			fillForm(FORM, data, new FileOutputStream(RESULT2));
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void fillForm(String filename, AccreditationData data,
			OutputStream out) throws IOException, DocumentException {
			
		PdfReader reader = new PdfReader(filename);
		PdfStamper stamper = new PdfStamper(reader, out);

		AcroFields form = stamper.getAcroFields();
		form.setField("name", data.getName());
		form.setFieldProperty("type", "textcolor", data.getTypeColor(), null);
		form.setField("type", data.getTypeName());
		form.setField("number", data.getNumber(false));
		form.setFieldProperty("filmfestival", "bgcolor", data.getTypeColor(),
			null);
		form.regenerateField("filmfestival");

		if (data.getPhoto() != null) {
			PushbuttonField bt = form.getNewPushbuttonFromField("photo");
			bt.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
			bt.setProportionalIcon(true);
			bt.setImage(data.getPhoto());
			form.replacePushbuttonField("photo", bt.getField());
		}

		try {
			BarcodeInter25 code = new BarcodeInter25();
			code.setGenerateChecksum(true);
			code.setBarHeight(mm2pt(3));
			code.setCode(data.getNumber(true));
			code.setFont(null);
			PdfContentByte cb = new PdfContentByte(stamper.getWriter());
			PdfTemplate template = code.createTemplateWithBarcode(cb, null,
				null);
			PushbuttonField bt = form.getNewPushbuttonFromField("barcode");
			bt.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
			bt.setProportionalIcon(false);
			bt.setTemplate(template);
			form.replacePushbuttonField("barcode", bt.getField());
		} catch (Exception e) {
			// not a valid code, do nothing
		}

		stamper.setFormFlattening(data.isFlatten());
		stamper.close();
	}
}
