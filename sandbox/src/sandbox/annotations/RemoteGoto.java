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
    public static void main(String[] args) throws IOException, DocumentException {
    	File subdir = new File("results/subdir");
    	subdir.mkdirs();
    	// first document
        Document document1 = new Document();
        PdfWriter.getInstance(document1, new FileOutputStream(
            "results/subdir/abc.pdf"));
        document1.open();
        Anchor anchor = new Anchor("This is a destination");
        anchor.setName("dest");
        document1.add(anchor);
        document1.close();
        // second document (with a link to the first one)
        Document document2 = new Document();
        PdfWriter.getInstance(document2, new FileOutputStream(
            "results/xyz.pdf"));
        document2.open();
        Chunk chunk = new Chunk("Link");
        chunk.setAction(PdfAction.gotoRemotePage("subdir/abc.pdf", "dest", false,  true));
        document2.add(chunk);
        document2.close();
    }
}
