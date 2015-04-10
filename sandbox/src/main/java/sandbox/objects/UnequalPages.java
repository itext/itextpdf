/**
 * This example is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23117200/itext-create-document-with-unequal-page-sizes
 */
package sandbox.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class UnequalPages {

    public static final String DEST = "results/objects/unequal_pages.pdf";
    
    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new UnequalPages().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        Rectangle one = new Rectangle(70,140);
        Rectangle two = new Rectangle(700,400);
        document.setPageSize(one);
        document.setMargins(2, 2, 2, 2);
        document.open();
        Paragraph p = new Paragraph("Hi");
        document.add(p);
        document.setPageSize(two);
        document.setMargins(20, 20, 20, 20);
        document.newPage();
        document.add(p);
        document.close();
    }
}
