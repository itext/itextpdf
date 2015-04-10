/*
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/26752663/adding-maps-at-itext-java
 */
package sandbox.annotations;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddPointerAnnotation {
        
    public static final String IMG = "resources/images/map_cic.png";
    public static final String DEST = "results/annotations/map_with_pointer_annotation.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddPointerAnnotation().createPdf(DEST);
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        Image img = Image.getInstance(IMG);
        Document document = new Document(img);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        img.setAbsolutePosition(0, 0);
        document.add(img);
        Rectangle rect = new Rectangle(220, 350, 475, 595);
        PdfAnnotation annotation = PdfAnnotation.createLine(writer, rect, "Cambridge Innovation Center", 225, 355, 470, 590);
        PdfArray le = new PdfArray();
        le.add(new PdfName("OpenArrow"));
        le.add(new PdfName("None"));
        annotation.setTitle("You are here:");
        annotation.setColor(BaseColor.RED);
        annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
        annotation.setBorderStyle(
            new PdfBorderDictionary(5, PdfBorderDictionary.STYLE_SOLID));
        annotation.put(new PdfName("LE"), le);
        annotation.put(new PdfName("IT"), new PdfName("LineArrow"));
        writer.addAnnotation(annotation);
        document.close();
    }
}
