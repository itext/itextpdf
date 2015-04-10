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
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class C0401_ImagingModel {

    public static final String DEST = "results/pdfabc/chapter04/hello_world.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0401_ImagingModel().createPdf(DEST);
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
        // THIS IS NOT THE USUAL WAY TO ADD TEXT; THIS IS THE HARD WAY!!!
        canvas.beginText();                               // BT
        canvas.moveText(36, 788);                         // 36 788 Td
        canvas.setFontAndSize(BaseFont.createFont(), 12); // /F1 12 Tf
        canvas.showText("Hello World ");                  // (Hello World )Tj
        canvas.endText();                                 // ET
        // This creates a path for a line and strokes it
        canvas.saveState();      // q
        canvas.moveTo(0, 0);     // 0 0 m
        canvas.lineTo(595, 842); // 595 842 l
        canvas.stroke();         // S
        canvas.restoreState();   // Q
        // step 5
        document.close();
    }
}