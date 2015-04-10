package book.pdfabc.chapter06;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class C0603_PdfUa {
    public static final String DEST = "results/pdfabc/chapter06/quickbrownfox5.pdf";
    public static final String FOX = "resources/images/fox.bmp";
    public static final String DOG = "resources/images/dog.bmp";
    public static final String FONT = "resources/fonts/FreeSans.ttf";
    
    static public void main(String args[]) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0603_PdfUa().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        //TAGGED PDF
        //Make document tagged
        writer.setTagged();
        //===============
        //PDF/UA
        //Set document metadata
        writer.setViewerPreferences(PdfWriter.DisplayDocTitle);
        document.addLanguage("en-US");
        document.addTitle("English pangram");
        writer.createXmpMetadata();
        //=====================
        document.open();

        Paragraph p = new Paragraph();
        //PDF/UA
        //Embed font
        p.setFont(FontFactory.getFont(FONT, BaseFont.WINANSI, BaseFont.EMBEDDED, 20));
        //==================
        Chunk c = new Chunk("The quick brown ");
        p.add(c);
        Image i = Image.getInstance(FOX);
        c = new Chunk(i, 0, -24);
        //PDF/UA
        //Set alt text
        c.setAccessibleAttribute(PdfName.ALT, new PdfString("Fox"));
        //==============
        p.add(c);
        p.add(new Chunk(" jumps over the lazy "));
        i = Image.getInstance(DOG);
        c = new Chunk(i, 0, -24);
        //PDF/UA
        //Set alt text
        c.setAccessibleAttribute(PdfName.ALT, new PdfString("Dog"));
        //==================
        p.add(c);
        document.add(p);

        document.close();
    }

}
