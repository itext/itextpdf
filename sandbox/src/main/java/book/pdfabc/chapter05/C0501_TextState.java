/**
 * This is an example was written by Bruno Lowagie.
 * It is used in the book "The ABC of PDF".
 * This book can be downloaded here: https://leanpub.com/itext_pdfabc/
 */
package book.pdfabc.chapter05;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class C0501_TextState {

    public static final String DEST = "results/pdfabc/chapter05/text_state_1.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0501_TextState().createPdf(DEST);
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
        canvas.beginText();
        canvas.moveText(36, 788);
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        canvas.showText("Hello World ");
        canvas.endText();
        
        canvas.beginText();
        canvas.moveText(36, 760);
        canvas.setCharacterSpacing(3);
        canvas.showText("Hello World ");
        canvas.setCharacterSpacing(0);
        canvas.setLeading(16);
        canvas.newlineText();
        canvas.setWordSpacing(30);
        canvas.showText("Hello World ");
        canvas.setWordSpacing(0);
        canvas.setHorizontalScaling(150);
        canvas.newlineShowText("Hello World ");
        canvas.setHorizontalScaling(100);
        canvas.setLeading(24);
        canvas.newlineShowText("Hello ");
        canvas.setTextRise(4);
        canvas.showText("World ");
        canvas.setTextRise(0);
        canvas.setLeading(16);
        canvas.setColorFill(BaseColor.BLUE);
        canvas.setLineWidth(0.3f);
        canvas.setColorStroke(BaseColor.RED);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);
        canvas.newlineShowText("Hello World (invisible)");
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_STROKE);
        canvas.newlineShowText("Hello World (stroke)");
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
        canvas.newlineShowText("HelloWorld (fill)");
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
        canvas.newlineShowText("HelloWorld (fill and stroke)");
        canvas.endText();

        canvas.setColorFill(BaseColor.GREEN);
        
        canvas.saveState();
        canvas.beginText();
        canvas.setTextMatrix(36, 624);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_CLIP);
        canvas.showText("Hello World (clip)");
        canvas.endText();
        canvas.rectangle(36, 628, 236, 634);
        canvas.fill();
        canvas.restoreState();

        canvas.saveState();
        canvas.beginText();
        canvas.setTextMatrix(36, 608);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_STROKE_CLIP);
        canvas.showText("Hello World (stroke clip)");
        canvas.endText();
        canvas.rectangle(36, 612, 236, 618);
        canvas.fill();
        canvas.restoreState();

        canvas.saveState();
        canvas.beginText();
        canvas.setTextMatrix(36, 592);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_CLIP);
        canvas.showText("HelloWorld (fill clip)");
        canvas.endText();
        canvas.rectangle(36, 596, 236, 602);
        canvas.fill();
        canvas.restoreState();

        canvas.saveState();
        canvas.beginText();
        canvas.setTextMatrix(36, 576);
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE_CLIP);
        canvas.showText("HelloWorld (fill and stroke clip)");
        canvas.endText();
        canvas.rectangle(36, 580, 236, 586);
        canvas.fill();
        canvas.restoreState();
        // step 5
        document.close();
    }
}
