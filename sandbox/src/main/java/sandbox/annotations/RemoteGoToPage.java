/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19999048/how-to-create-hyperlink-from-a-pdf-to-another-pdf-to-a-specified-page-using-itex
 * 
 * Creating a link from one PDF to another
 */
package sandbox.annotations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfWriter;

public class RemoteGoToPage {
    public static final String DEST = "results/annotations/subdir/abc2.pdf";
    public static final String SRC = "results/annotations/xyz2.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        RemoteGoToPage app = new RemoteGoToPage();
        app.createPdf(DEST);
        app.createPdf2(SRC);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        // first document
        Document document1 = new Document();
        PdfWriter.getInstance(document1, new FileOutputStream(dest));
        document1.open();
        document1.add(new Paragraph("page 1"));
        document1.newPage();
        document1.add(new Paragraph("page 2"));
        document1.newPage();
        document1.add(new Paragraph("page 3"));
        document1.newPage();
        document1.add(new Paragraph("page 4"));
        document1.newPage();
        document1.add(new Paragraph("page 5"));
        document1.newPage();
        document1.add(new Paragraph("page 6"));
        document1.newPage();
        document1.add(new Paragraph("page 7"));
        document1.close();
    }
    
    public void createPdf2(String src) throws IOException, DocumentException {
        // second document (with a link to the first one)
        Document document2 = new Document();
        PdfWriter.getInstance(document2, new FileOutputStream(src));
        document2.open();
        Chunk chunk = new Chunk("Link");
        chunk.setAction(new PdfAction("subdir/abc2.pdf", 6));
        document2.add(chunk);
        document2.close();
    }
}
