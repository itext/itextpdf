package book.pdfabc.chapter06;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author iText
 */
public class ThisIsTheTitle {
    
    public static final String DEST = "results/pdfabc/chapter06/this_is_the_title.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ThisIsTheTitle().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        writer.setTagged();
        document.open();
        Font f = new Font(FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD);
        Paragraph p = new Paragraph("This is the title", f);
        p.setRole(PdfName.H1);
        document.add(p);
        document.add(new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris eleifend, nisi at scelerisque cursus, est augue egestas justo, ac sagittis dolor nulla at ante. Nulla porta, ligula et imperdiet ultrices, erat dolor vehicula libero, eget sollicitudin turpis arcu ut augue. Ut et purus ullamcorper, varius neque et, scelerisque dolor. Pellentesque imperdiet dui at accumsan vehicula. Aenean non leo mi. Ut eu scelerisque ante, scelerisque gravida ligula. Fusce vitae pellentesque odio. Cras sit amet ligula ac sem convallis feugiat quis eu augue. Duis commodo a justo vitae congue. Duis eu bibendum ante, eu semper neque. Vivamus ullamcorper risus at commodo facilisis. Donec id justo vulputate, consectetur ante lobortis, tristique augue. Sed lacinia volutpat congue. Nunc placerat erat condimentum lacinia congue. Mauris ac tellus vel odio suscipit dignissim nec vel lorem. Pellentesque sodales placerat turpis, id fermentum elit sagittis eget."));
        document.close();
    }           
}
