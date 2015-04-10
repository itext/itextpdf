package book.pdfabc.chapter05;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTextArray;
import com.itextpdf.text.pdf.PdfWriter;

public class C0502_TextState {


    public static final String DEST = "results/pdfabc/chapter05/text_state_2.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0502_TextState().createPdf(DEST);
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
        canvas.showText("Hello World ");
        canvas.moveTextWithLeading(0, -16);
        canvas.showText("Hello World ");
        canvas.newlineText();
        canvas.showText("Hello World ");
        canvas.setTextMatrix(72, 740);
        canvas.showText("Hello World ");
        canvas.setTextMatrix(2, 0, 1, 2, 36, 710);
        canvas.showText("Hello World ");
        canvas.endText();
        
        canvas.beginText();
        canvas.moveText(216, 788);
        canvas.showText("Hello World ");
        canvas.setLeading(16);
        canvas.newlineShowText("Hello World ");
        canvas.newlineShowText(30, 3, "Hello World ");
        canvas.setCharacterSpacing(0);
        canvas.setWordSpacing(0);
        canvas.newlineText();
        PdfTextArray array = new PdfTextArray("H");
        array.add(45);
        array.add("el");
        array.add(85);
        array.add("lo");
        array.add(-250);
        array.add("Wor");
        array.add(35);
        array.add("ld ");
        canvas.showText(array);
        canvas.endText();
        // step 5
        document.close();
    }
    
}
