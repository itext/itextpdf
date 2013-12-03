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
    public static void main(String[] args) throws IOException, DocumentException {
    	File subdir = new File("results/subdir");
    	subdir.mkdirs();
    	// first document
        Document document1 = new Document();
        PdfWriter.getInstance(document1, new FileOutputStream(
            "results/subdir/abc2.pdf"));
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
        // second document (with a link to the first one)
        Document document2 = new Document();
        PdfWriter.getInstance(document2, new FileOutputStream(
            "results/xyz2.pdf"));
        document2.open();
        Chunk chunk = new Chunk("Link");
        chunk.setAction(new PdfAction("subdir/abc2.pdf", 6));
        document2.add(chunk);
        document2.close();
    }
}
