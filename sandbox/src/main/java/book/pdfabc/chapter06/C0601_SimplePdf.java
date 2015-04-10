package book.pdfabc.chapter06;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class C0601_SimplePdf {

    public static final String DEST = "results/pdfabc/chapter06/quickbrownfox1.pdf";
    public static final String FOX = "resources/images/fox.bmp";
    public static final String DOG = "resources/images/dog.bmp";

    static public void main(String args[]) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0601_SimplePdf().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        document.open();
        Paragraph p = new Paragraph();
        p.setFont(new Font(Font.FontFamily.HELVETICA, 20));
        Chunk c = new Chunk("The quick brown ");
        p.add(c);
        Image i = Image.getInstance(FOX);
        c = new Chunk(i, 0, -24);
        p.add(c);
        c = new Chunk(" jumps over the lazy ");
        p.add(c);
        i = Image.getInstance(DOG);
        c = new Chunk(i, 0, -24);
        p.add(c);
        document.add(p);
        document.close();
    }

}
