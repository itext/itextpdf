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
import com.itextpdf.text.pdf.PdfWriter;

public class C0409_CoordinateSystem {

    public static final String DEST = "results/pdfabc/chapter04/coordinate_system.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0409_CoordinateSystem().createPdf(DEST);
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
        canvas.setColorFill(BaseColor.GRAY);
        triangle(canvas);
        canvas.concatCTM(1, 0, 0, 1, 100, 40);
        triangle(canvas);
        canvas.concatCTM(0, -1, -1, 0, 150, 150);
        triangle(canvas);
        canvas.concatCTM(0.5f, 0, 0, 0.3f, 100, 0);
        triangle(canvas);
        canvas.concatCTM(3, 0.2f, 0.4f, 2, -150, -150);
        triangle(canvas);
        document.newPage();
        canvas.setColorFill(BaseColor.GRAY);
        triangle(canvas);
        canvas.concatCTM(1, 0, 0, 1, 100, 40);
        triangle(canvas);
        canvas.concatCTM(0.5f, 0, 0, 0.3f, 100, 0);
        triangle(canvas);
        canvas.concatCTM(0, -1, -1, 0, 150, 150);
        triangle(canvas);
        canvas.concatCTM(3, 0.2f, 0.4f, 2, -150, -150);
        triangle(canvas);
        // step 5
        document.close();
    }
    
    protected void triangle(PdfContentByte canvas) {
        canvas.moveTo(0, 80);
        canvas.lineTo(100, 40);
        canvas.lineTo(0, 0);
        canvas.lineTo(0, 80);
        canvas.moveTo(15, 60);
        canvas.lineTo(65, 40);
        canvas.lineTo(15, 20);
        canvas.lineTo(15, 60);
        canvas.eoFillStroke();
    }
}
