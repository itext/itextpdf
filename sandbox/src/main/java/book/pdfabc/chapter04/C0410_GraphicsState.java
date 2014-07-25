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

public class C0410_GraphicsState {

    public static final String DEST = "results/pdfabc/chapter04/graphics_state.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0410_GraphicsState().createPdf(DEST);
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
        triangle(canvas, 50);
        canvas.saveState();
        canvas.concatCTM(1, 0, 0, 1, 0, 15);
        canvas.setColorFill(BaseColor.GRAY);
        triangle(canvas, 90);
        canvas.saveState();
        canvas.concatCTM(1, 0, 0, 1, 0, -30);
        canvas.setColorStroke(BaseColor.RED);
        canvas.setColorFill(BaseColor.CYAN);
        triangle(canvas, 130);
        canvas.saveState();
        canvas.setLineDash(6, 3);
        canvas.concatCTM(1, 0, 0, 1, 0, 15);
        triangle(canvas, 170);
        canvas.restoreState();
        triangle(canvas, 210);
        canvas.restoreState();
        triangle(canvas, 250);
        canvas.restoreState();
        triangle(canvas, 290);
        // step 5
        document.close();
    }
    
    protected void triangle(PdfContentByte canvas, float x) {
        canvas.moveTo(x, 760);
        canvas.lineTo(x + 100, 720);
        canvas.lineTo(x, 680);
        canvas.lineTo(x, 760);
        canvas.moveTo(x + 15, 740);
        canvas.lineTo(x + 65, 720);
        canvas.lineTo(x + 15, 700);
        canvas.lineTo(x + 15, 740);
        canvas.eoFillStroke();
    }
}
