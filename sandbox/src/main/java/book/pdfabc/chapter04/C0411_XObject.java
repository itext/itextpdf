/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter04;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class C0411_XObject {

    public static final String DEST = "results/pdfabc/chapter04/xobject.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0411_XObject().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        PdfTemplate template = canvas.createTemplate(100, 80);
        template.moveTo(0, 80);
        template.lineTo(100, 40);
        template.lineTo(0, 0);
        template.lineTo(0, 80);
        template.moveTo(15, 60);
        template.lineTo(65, 40);
        template.lineTo(15, 20);
        template.lineTo(15, 60);
        template.eoFillStroke();
        canvas.addTemplate(template, 50, 680);
        canvas.saveState();
        canvas.concatCTM(1, 0, 0, 1, 0, 15);
        canvas.setColorFill(BaseColor.GRAY);
        canvas.addTemplate(template, 90, 680);
        canvas.saveState();
        canvas.concatCTM(1, 0, 0, 1, 0, -30);
        canvas.setColorStroke(BaseColor.RED);
        canvas.setColorFill(BaseColor.CYAN);
        canvas.addTemplate(template, 130, 680);
        canvas.saveState();
        canvas.setLineDash(6, 3);
        canvas.concatCTM(1, 0, 0, 1, 0, 15);
        canvas.addTemplate(template, 170, 680);
        canvas.restoreState();
        canvas.addTemplate(template, 210, 680);
        canvas.restoreState();
        canvas.addTemplate(template, 250, 680);
        canvas.restoreState();
        canvas.addTemplate(template, 290, 680);
        // step 5
        document.close();
    }
}
