package book.pdfabc.chapter05;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTextArray;
import com.itextpdf.text.pdf.PdfWriter;

public class C0503_TextState {


    public static final String DEST = "results/pdfabc/chapter05/text_state_3.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0503_TextState().createPdf(DEST);
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
    	BaseFont bf = BaseFont.createFont();
        canvas.setFontAndSize(bf, 12);
        canvas.setLeading(16);
        canvas.showText("Hello World ");
        canvas.newlineText();
        PdfTextArray array = PdfContentByte.getKernArray("Hello World ", bf);
        canvas.showText(array);
        canvas.newlineText();
        canvas.showTextKerned("Hello World ");
        canvas.newlineText();
        canvas.showText(String.format("Kerned: %s; not kerned: %s",
                canvas.getEffectiveStringWidth("Hello World ", true),
                canvas.getEffectiveStringWidth("Hello World ", false)));
        canvas.showTextAligned(Element.ALIGN_CENTER, "Hello World ", 144, 790, 30);
        canvas.showTextAlignedKerned(Element.ALIGN_CENTER, "Hello World ", 144, 770, 30);
        canvas.endText();
        // step 5
        document.close();
    }
    
}
