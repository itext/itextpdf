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
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PatternColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPatternPainter;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class C0406_PatternColor {

    /** An image that will be used for a pattern color. */
    public static final String IMG = "resources/images/bulb.gif";
    public static final String DEST = "results/pdfabc/chapter04/pattern_color.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0406_PatternColor().createPdf(DEST);
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
        PdfPatternPainter square = canvas.createPattern(15, 15);
        square.setColorFill(new BaseColor(0xFF, 0xFF, 0x00));
        square.setColorStroke(new BaseColor(0xFF, 0x00, 0x00));
        square.rectangle(5, 5, 5, 5);
        square.fillStroke();
    
        PdfPatternPainter ellipse
            = canvas.createPattern(15, 10, 20, 25);
        ellipse.setColorFill(new BaseColor(0xFF, 0xFF, 0x00));
        ellipse.setColorStroke(new BaseColor(0xFF, 0x00, 0x00));
        ellipse.ellipse(2f, 2f, 13f, 8f);
        ellipse.fillStroke();
    
        PdfPatternPainter circle
            = canvas.createPattern(15, 15, 10, 20, BaseColor.BLUE);
        circle.circle(7.5f, 7.5f, 2.5f);
        circle.fill();
    
        PdfPatternPainter line
            = canvas.createPattern(5, 10, null);
        line.setLineWidth(1);
        line.moveTo(3, -1);
        line.lineTo(3, 11);
        line.stroke();
    
        Image img = Image.getInstance(IMG);
        img.scaleAbsolute(20, 20);
        img.setAbsolutePosition(0, 0);
        PdfPatternPainter img_pattern
            = canvas.createPattern(20, 20, 20, 20);
        img_pattern.addImage(img);
        double d45 = -Math.PI / 4;
        img_pattern.setPatternMatrix(
            (float)Math.cos(d45), (float)Math.sin(d45),
            -(float)Math.sin(d45), (float)Math.cos(d45), 0f, 0f);
    
        colorRectangle(canvas, new PatternColor(square), 36, 696, 126, 126);
        colorRectangle(canvas, new PatternColor(ellipse), 180, 696, 126, 126);
        colorRectangle(canvas, new PatternColor(circle), 324, 696, 126, 126);
        colorRectangle(canvas, new PatternColor(line), 36, 552, 126, 126);
        colorRectangle(canvas, new PatternColor(img_pattern), 36, 408, 126, 126);

        canvas.setPatternFill(line, BaseColor.RED);
        canvas.ellipse(180, 552, 306, 678);
        canvas.fillStroke();
        canvas.setPatternFill(circle, BaseColor.GREEN);
        canvas.ellipse(324, 552, 450, 678);
        canvas.fillStroke();
    
        canvas.setPatternFill(img_pattern);
        canvas.ellipse(180, 408, 450, 534);
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
