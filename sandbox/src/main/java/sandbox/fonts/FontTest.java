/**
 * Example written by Bruno Lowagie, showing that not all fonts contain
 * all glyphs for all languages.
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
import java.util.Set;
import java.util.TreeSet;

public class FontTest {

    public static final String DEST = "results/fonts/overview.pdf";
    public static final String FONTDIR = "resources/fonts";
    public static final String TEXT = "Quick brown fox jumps over the lazy dog; 0123456789";
    public static final String CP1250 = "Nikogar\u0161nja zemlja";
    public static final String CP1251 = "\u042f \u043b\u044e\u0431\u043b\u044e \u0442\u0435\u0431\u044f";
    public static final String CP1252 = "Un long dimanche de fian\u00e7ailles";
    public static final String CP1253 = "\u039d\u03cd\u03c6\u03b5\u03c2";
    public static final String CHINESE = "\u5341\u950a\u57cb\u4f0f";
    public static final String JAPANESE = "\u8ab0\u3082\u77e5\u3089\u306a\u3044";
    public static final String KOREAN = "\ube48\uc9d1";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FontTest().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(DEST));
        document.open();
        FontFactory.registerDirectory(FONTDIR);
        Set<String> fonts = new TreeSet<String>(FontFactory.getRegisteredFonts());
        for (String fontname : fonts) {
            showFontInfo(document, fontname);
        }
        document.close();
    }
    
    protected void showFontInfo(Document document, String fontname) throws DocumentException {
        System.out.println(fontname);
        Font font = null;
        try {
            font = FontFactory.getFont(fontname, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }
        catch(Exception e) {
            document.add(new Paragraph(String.format("The font %s doesn't have unicode support: %s", fontname, e.getMessage())));
            return;
        }
        document.add(new Paragraph(
                String.format("Postscript name for %s: %s", fontname, font.getBaseFont().getPostscriptFontName())));
        document.add(new Paragraph(TEXT, font));
        document.add(new Paragraph(String.format("CP1250: %s", CP1250), font));
        document.add(new Paragraph(String.format("CP1251: %s", CP1251), font));
        document.add(new Paragraph(String.format("CP1252: %s", CP1252), font));
        document.add(new Paragraph(String.format("CP1253: %s", CP1253), font));
        document.add(new Paragraph(String.format("CHINESE: %s", CHINESE), font));
        document.add(new Paragraph(String.format("JAPANESE: %s", JAPANESE), font));
        document.add(new Paragraph(String.format("KOREAN: %s", KOREAN), font));
    }
}
