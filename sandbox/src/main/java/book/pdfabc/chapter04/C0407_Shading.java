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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfShading;
import com.itextpdf.text.pdf.PdfShadingPattern;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.ShadingColor;

@WrapToTest
public class C0407_Shading {

    public static final String DEST = "results/pdfabc/chapter04/shading_color.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0407_Shading().createPdf(DEST);
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
        PdfShading axial = PdfShading.simpleAxial(writer, 36, 716, 396,
                788, BaseColor.ORANGE, BaseColor.BLUE);
        canvas.paintShading(axial);
        document.newPage();
        PdfShading radial = PdfShading.simpleRadial(writer,
            200, 700, 50, 300, 700, 100,
            new BaseColor(0xFF, 0xF7, 0x94),
            new BaseColor(0xF7, 0x8A, 0x6B),
            false, false);
        canvas.paintShading(radial);

        PdfShadingPattern shading = new PdfShadingPattern(axial);
        colorRectangle(canvas, new ShadingColor(shading), 150, 420, 126, 126);
        canvas.setShadingFill(shading);
        canvas.rectangle(300, 420, 126, 126);
        canvas.fillStroke();
        // step 5
        document.close();
    }
    public void colorRectangle(PdfContentByte canvas,
            BaseColor color, float x, float y, float width, float height) {
        canvas.saveState();
        canvas.setColorFill(color);
        canvas.rectangle(x, y, width, height);
        canvas.fillStroke();
        canvas.restoreState();
    }
}
