/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/21718712/itext-pdfcontentbyte-rectanglerectangle-does-not-behave-as-expected
 */
package sandbox.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class DrawRectangle {

    public static final String DEST = "results/objects/draw_rectangle.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DrawRectangle().createPdf(DEST);
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect = new Rectangle(36, 36, 559, 806);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(2);
        canvas.rectangle(rect);
        document.close();
    }
}
