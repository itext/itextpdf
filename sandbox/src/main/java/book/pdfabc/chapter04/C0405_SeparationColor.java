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
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfSpotColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.SpotColor;

@WrapToTest
public class C0405_SeparationColor {

    public static final String DEST = "results/pdfabc/chapter04/separation_color.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0405_SeparationColor().createPdf(DEST);
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
        PdfSpotColor psc_g = new PdfSpotColor(
            "iTextSpotColorGray", new GrayColor(0.9f));
        PdfSpotColor psc_rgb = new PdfSpotColor(
            "iTextSpotColorRGB", new BaseColor(0x64, 0x95, 0xed));
        PdfSpotColor psc_cmyk = new PdfSpotColor(
            "iTextSpotColorCMYK", new CMYKColor(0.3f, .9f, .3f, .1f));

        colorRectangle(canvas, new SpotColor(psc_g, 0.5f), 36, 770, 36, 36);
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.1f), 90, 770, 36, 36);
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.2f), 144, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.3f), 198, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.4f), 252, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.5f), 306, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.6f), 360, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_rgb, 0.7f), 416, 770, 36, 36); 
        colorRectangle(canvas, new SpotColor(psc_cmyk, 0.25f), 470, 770, 36, 36);
            
        canvas.setColorFill(psc_g, 0.5f);
        canvas.rectangle(36, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(psc_g, 0.9f);
        canvas.rectangle(90, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(psc_rgb, 0.5f);
        canvas.rectangle(144, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(psc_rgb, 0.9f);
        canvas.rectangle(198, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(psc_cmyk, 0.5f);
        canvas.rectangle(252, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(psc_cmyk, 0.9f);
        canvas.rectangle(306, 716, 36, 36);
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
