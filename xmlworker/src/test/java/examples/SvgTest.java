package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.svg.XMLHelperForSVG;

public class SvgTest {

	public static void main(String[] args) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(
				"./target/test-classes/examples/tiger.pdf")));
		document.open();
		Reader reader = new InputStreamReader(SvgTest.class.getResourceAsStream("tiger.svg"));
		PdfTemplate template = XMLHelperForSVG.getInstance().parseToTemplate(writer.getDirectContent(), reader);
		Image img = Image.getInstance(template);
		img.setBorder(Image.BOX);
		img.setBorderWidth(1);
		document.add(img);
		document.close();
	}
}
