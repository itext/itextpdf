/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package book.pdfabc.chapter04;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class C0419_TemplateClip {

    public static final String DEST = "results/pdfabc/chapter04/clipped_image.pdf";;
    public static final String RESOURCE = "resources/images/bruno_ingeborg.jpg";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0419_TemplateClip().createPdf(DEST);
    }

    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer
          = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        Image img = Image.getInstance(RESOURCE);
        float w = img.getScaledWidth();
        float h = img.getScaledHeight();
        PdfTemplate t = writer.getDirectContent().createTemplate(850, 600);
        t.addImage(img, w, 0, 0, h, 0, -600);
        Image clipped = Image.getInstance(t);
        clipped.scalePercent(50);
        document.add(new Paragraph("Template clip:"));
        document.add(clipped);
        t = writer.getDirectContent().createTemplate(850, 600);
        t.ellipse(0, 0, 850, 600);
        t.clip();
        t.newPath();
        t.addImage(img, w, 0, 0, h, 0, -600);
        clipped = Image.getInstance(t);
        clipped.scalePercent(50);
        document.add(new Paragraph("Clipping path:"));
        document.add(clipped);
        // step 5
        document.close();
    }
}
