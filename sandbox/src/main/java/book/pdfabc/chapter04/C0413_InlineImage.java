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
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class C0413_InlineImage {
    public static final String IMG = "resources/images/bulb.gif";
    public static final String DEST = "results/pdfabc/chapter04/inline_image.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0413_InlineImage().createPdf(DEST);
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
        Image img = Image.getInstance(IMG);
        canvas.addImage(img, 20, 0, 0, 20, 36, 786, true);
        canvas.addImage(img, 20, 0, 0, 20, 56, 786, true);
        canvas.addImage(img, 20, 0, 0, 20, 76, 786, true);
        // step 5
        document.close();
    }
}
