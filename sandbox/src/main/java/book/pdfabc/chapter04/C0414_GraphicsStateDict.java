/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter04;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfWriter;

public class C0414_GraphicsStateDict {

    public static final String DEST = "results/pdfabc/chapter04/gs_dict.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0414_GraphicsStateDict().createPdf(DEST);
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
        PdfGState gs = new PdfGState();
        gs.put(new PdfName("LW"), new PdfNumber(3));
        gs.put(new PdfName("LJ"), new PdfNumber(1));
        PdfArray dashArray = new PdfArray(new int[]{12, 1});
        PdfArray dashPattern = new PdfArray();
        dashPattern.add(dashArray);
        dashPattern.add(new PdfNumber(0));
        gs.put(new PdfName("D"), dashPattern);
        canvas.setGState(gs);
        triangle(canvas);
        document.newPage();
        triangle(canvas);
        document.newPage();
        canvas.setGState(gs);
        triangle(canvas);
        // step 5
        document.close();
    }
    
    public void triangle(PdfContentByte canvas) {
        canvas.moveTo(50, 680);
        canvas.lineTo(150, 640);
        canvas.lineTo(50, 600);
        canvas.lineTo(50, 680);
        canvas.moveTo(65, 660);
        canvas.lineTo(115, 640);
        canvas.lineTo(65, 620);
        canvas.lineTo(65, 660);
        canvas.stroke();
    }
}
