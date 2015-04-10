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
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class C0403_PathPainting {

    public static final String DEST = "results/pdfabc/chapter04/path_painting.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0403_PathPainting().createPdf(DEST);
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
        // stroke
        triangles1(canvas);
        canvas.stroke();
        // close path and stroke
        triangles2(canvas);
        canvas.closePathStroke();
        // close path and stroke
        triangles3(canvas);
        canvas.closePathStroke();
        // new page
        document.newPage();
        canvas.setColorFill(new GrayColor(0.7f));
        // fill
        triangles1(canvas);
        canvas.fill();
        // close path and stroke
        triangles2(canvas);
        canvas.fill();
        // close path and stroke
        triangles3(canvas);
        canvas.fill();
        // new page
        document.newPage();
        canvas.setColorFill(new GrayColor(0.7f));
        // fill and stroke
        triangles1(canvas);
        canvas.fillStroke();
        // close path and stroke
        triangles2(canvas);
        canvas.closePathFillStroke();
        // close path and stroke
        triangles3(canvas);
        canvas.closePathFillStroke();;
        // new page
        document.newPage();
        canvas.setColorFill(new GrayColor(0.7f));
        // fill
        triangles1(canvas);
        canvas.eoFill();
        // close path and stroke
        triangles2(canvas);
        canvas.eoFill();
        // close path and stroke
        triangles3(canvas);
        canvas.eoFill();
        // new page
        document.newPage();
        canvas.setColorFill(new GrayColor(0.7f));
        // fill and stroke
        triangles1(canvas);
        canvas.eoFillStroke();
        // close path and stroke
        triangles2(canvas);
        canvas.closePathEoFillStroke();
        // close path and stroke
        triangles3(canvas);
        canvas.closePathEoFillStroke();
        // new page
        document.newPage();
        canvas.setColorFill(new GrayColor(0.7f));
        // clipping
        canvas.saveState();
        triangles1(canvas);
        canvas.clip();
        canvas.newPath();
        canvas.rectangle(45, 675, 120, 100);
        canvas.fill();
        canvas.restoreState();
        canvas.saveState();
        triangles2(canvas);
        canvas.clip();
        canvas.newPath();
        canvas.rectangle(195, 675, 120, 100);
        canvas.fill();
        canvas.restoreState();
        canvas.saveState();
        triangles3(canvas);
        canvas.clip();
        canvas.newPath();
        canvas.rectangle(345, 675, 120, 100);
        canvas.fill();
        canvas.restoreState();
        // new page
        document.newPage();
        canvas.setColorFill(new GrayColor(0.7f));
        // clipping
        canvas.saveState();
        triangles1(canvas);
        canvas.eoClip();
        canvas.newPath();
        canvas.rectangle(45, 675, 120, 100);
        canvas.fill();
        canvas.restoreState();
        canvas.saveState();
        triangles2(canvas);
        canvas.eoClip();
        canvas.newPath();
        canvas.rectangle(195, 675, 120, 100);
        canvas.fill();
        canvas.restoreState();
        canvas.saveState();
        triangles3(canvas);
        canvas.eoClip();
        canvas.newPath();
        canvas.rectangle(345, 675, 120, 100);
        canvas.fill();
        canvas.restoreState();
        // new page
        document.newPage();
        // step 5
        document.close();
    }
    
    protected void triangles1(PdfContentByte canvas) {
        canvas.moveTo(50, 760);
        canvas.lineTo(150, 720);
        canvas.lineTo(50, 680);
        canvas.lineTo(50, 760);
        canvas.moveTo(65, 740);
        canvas.lineTo(115, 720);
        canvas.lineTo(65, 700);
    }
    protected void triangles2(PdfContentByte canvas) {
        canvas.moveTo(200, 760);
        canvas.lineTo(300, 720);
        canvas.lineTo(200, 680);
        canvas.lineTo(200, 760);
        canvas.moveTo(215, 740);
        canvas.lineTo(265, 720);
        canvas.lineTo(215, 700);
    }
    protected void triangles3(PdfContentByte canvas) {
        canvas.moveTo(350, 760);
        canvas.lineTo(450, 720);
        canvas.lineTo(350, 680);
        canvas.lineTo(350, 760);
        canvas.moveTo(400, 740);
        canvas.lineTo(350, 720);
        canvas.lineTo(400, 700);
    }
}