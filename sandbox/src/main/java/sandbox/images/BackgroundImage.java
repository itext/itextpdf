/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/21731027/backgroundimage-in-landscape-and-cover-whole-pdf-with-itextsharp
 */
package sandbox.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class BackgroundImage {
    public static final String IMAGE = "resources/images/berlin2013.jpg";
    public static final String DEST = "results/images/background_image.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new BackgroundImage().createPdf(DEST);
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        document.add(new Paragraph("Berlin!"));
        PdfContentByte canvas = writer.getDirectContentUnder();
        Image image = Image.getInstance(IMAGE);
        image.scaleAbsolute(PageSize.A4.rotate());
        image.setAbsolutePosition(0, 0);
        canvas.addImage(image);
        document.close();
    }
}
