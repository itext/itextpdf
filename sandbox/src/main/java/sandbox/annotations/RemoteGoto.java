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

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfWriter;

public class RemoteGoto {
    public static final String DEST = "results/annotations/subdir/abc.pdf";
    public static final String SRC = "results/annotations/xyz.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        RemoteGoto app = new RemoteGoto();
        app.createPdf(DEST);
        app.createPdf2(SRC);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        // first document
        Document document1 = new Document();
        PdfWriter.getInstance(document1, new FileOutputStream(dest));
        document1.open();
        Anchor anchor = new Anchor("This is a destination");
        anchor.setName("dest");
        document1.add(anchor);
        document1.close();
    }
    
    public void createPdf2(String src) throws IOException, DocumentException {
        // second document (with a link to the first one)
        Document document2 = new Document();
        PdfWriter.getInstance(document2, new FileOutputStream(src));
        document2.open();
        Chunk chunk = new Chunk("Link");
        chunk.setAction(PdfAction.gotoRemotePage("subdir/abc.pdf", "dest", false,  true));
        document2.add(chunk);
        document2.close();
    }
}
