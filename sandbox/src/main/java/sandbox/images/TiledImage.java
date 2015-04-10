/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/26859473/how-to-show-an-image-with-large-dimensions-across-multiple-pages-in-itextpdf
 */
package sandbox.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class TiledImage {
    public static final String IMAGE = "resources/images/bruno_ingeborg.jpg";
    public static final String DEST = "results/images/tiled_image.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TiledImage().createPdf(DEST);
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        Image image = Image.getInstance(IMAGE);
        float width = image.getScaledWidth();
        float height = image.getScaledHeight();
        Rectangle page = new Rectangle(width / 2, height / 2);
        Document document = new Document(page);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfContentByte canvas = writer.getDirectContentUnder();
        canvas.addImage(image, width, 0, 0, height, 0, -height / 2);
        document.newPage();
        canvas.addImage(image, width, 0, 0, height, 0, 0);
        document.newPage();
        canvas.addImage(image, width, 0, 0, height, -width / 2, - height / 2);
        document.newPage();
        canvas.addImage(image, width, 0, 0, height, -width / 2, 0);
        document.close();
    }
}
