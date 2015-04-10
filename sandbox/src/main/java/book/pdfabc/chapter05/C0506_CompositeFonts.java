package book.pdfabc.chapter05;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class C0506_CompositeFonts {
    
    public static final String DEST = "results/pdfabc/chapter05/fonts_composite.pdf";

    public static final String OT_T1 = "resources/fonts/Puritan2.otf";
    public static final String OT_TT = "resources/fonts/OpenSans-Regular.ttf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0506_CompositeFonts().createPdf(DEST);
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
        canvas.moveText(36, 806);
        canvas.setLeading(16);
    	BaseFont bf;
        bf = BaseFont.createFont(OT_T1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        canvas.setFontAndSize(bf, 12);
        canvas.newlineShowText("Nikogar\u0161nja zemlja");
        bf = BaseFont.createFont(OT_TT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        canvas.setFontAndSize(bf, 12);
        canvas.newlineShowText("\u039d\u03cd\u03c6\u03b5\u03c2");
        bf = BaseFont.createFont(OT_TT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        canvas.setFontAndSize(bf, 12);
        canvas.newlineShowText("\u042f \u043b\u044e\u0431\u043b\u044e \u0442\u0435\u0431\u044f");
        canvas.endText();
        // step 5
        document.close();
    }
}
