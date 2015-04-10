/**
 * This sample is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27070222/how-to-create-unique-images-with-image-getinstance
 */
package sandbox.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class RawImages {
    public static final String DEST = "results/images/raw_images.pdf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RawImages().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        Image gray = Image.getInstance(1, 1, 1, 8, new byte[] { (byte)0x80 });
        gray.scaleAbsolute(30, 30);
        Image red = Image.getInstance(1, 1, 3, 8, new byte[] { (byte)255, (byte)0, (byte)0 });
        red.scaleAbsolute(30, 30);
        Image green = Image.getInstance(1, 1, 3, 8, new byte[] { (byte)0, (byte)255, (byte)0 });
        green.scaleAbsolute(30, 30);
        Image blue = Image.getInstance(1, 1, 3, 8, new byte[] { (byte)0, (byte)0, (byte)255, });
        blue.scaleAbsolute(30, 30);
        Image cyan = Image.getInstance(1, 1, 4, 8, new byte[] { (byte)255, (byte)0, (byte)0, (byte)0 });
        cyan.scaleAbsolute(30, 30);
        Image magenta = Image.getInstance(1, 1, 4, 8, new byte[] { (byte)0, (byte)255, (byte)0, (byte)0 });
        magenta.scaleAbsolute(30, 30);
        Image yellow = Image.getInstance(1, 1, 4, 8, new byte[] { (byte)0, (byte)0, (byte)255, (byte)0 });
        yellow.scaleAbsolute(30, 30);
        Image black = Image.getInstance(1, 1, 4, 8, new byte[] { (byte)0, (byte)0, (byte)0, (byte)255 });
        black.scaleAbsolute(30, 30);
        document.add(gray);
        document.add(red);
        document.add(green);
        document.add(blue);
        document.add(cyan);
        document.add(magenta);
        document.add(yellow);
        document.add(black);
        document.close();
    }
}
