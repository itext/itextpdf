/*
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/26360814/rupee-symbol-is-not-showing-in-android
 */
package sandbox.fonts;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author iText
 */
public class RupeeSymbol {

    public static final String DEST = "results/fonts/rupee.pdf";
    public static final String FONT1 = "resources/fonts/PlayfairDisplay-Regular.ttf";
    public static final String FONT2 = "resources/fonts/PT_Sans-Web-Regular.ttf";
    public static final String FONT3 = "resources/fonts/FreeSans.ttf";
    public static final String RUPEE = "The Rupee character \u20B9 and the Rupee symbol \u20A8";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RupeeSymbol().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(DEST));
        document.open();
        Font f1 = FontFactory.getFont(FONT1, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
        Font f2 = FontFactory.getFont(FONT2, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
        Font f3 = FontFactory.getFont(FONT3, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
        Font f4 = FontFactory.getFont(FONT3, BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph(RUPEE, f1));
        document.add(new Paragraph(RUPEE, f2));
        document.add(new Paragraph(RUPEE, f3));
        document.add(new Paragraph(RUPEE, f4));
        document.close();
    }
}
