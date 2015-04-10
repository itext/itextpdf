/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter04;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class C0402_PathConstruction {

    public static final String DEST = "results/pdfabc/chapter04/path_construction.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0402_PathConstruction().createPdf(DEST);
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
        // a line
        canvas.moveTo(36, 806);
        canvas.lineTo(559, 806);
        // lines and curves
        canvas.moveTo(70, 680);
        canvas.lineTo(80, 750);
        canvas.moveTo(140, 770);
        canvas.lineTo(160, 710);
        canvas.moveTo(70, 680);
        canvas.curveTo(80, 750, 140, 770, 160, 710);
        canvas.moveTo(300, 770);
        canvas.lineTo(320, 710);
        canvas.moveTo(230, 680);
        canvas.curveTo(300, 770, 320, 710);
        canvas.moveTo(390, 680);
        canvas.lineTo(400, 750);
        canvas.moveTo(390, 680);
        canvas.curveTo(400, 750, 480, 710);
        // two sides of a triangle
        canvas.moveTo(36, 650);
        canvas.lineTo(559, 650);
        canvas.lineTo(559, 675);
        // three sides of a triangle
        canvas.moveTo(36, 600);
        canvas.lineTo(559, 600);
        canvas.lineTo(559, 625);
        canvas.closePath();
        // a rectangle
        canvas.rectangle(36, 550, 523, 25);
        // nothing is drawn unless we stroke:
        canvas.stroke();
        // step 5
        document.close();
    }
}