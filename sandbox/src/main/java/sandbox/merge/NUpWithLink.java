/**
 * Example written by Bruno Lowagie in answer to a question on StackOverflow
 * 
 * When concatenating documents, we add a named destination every time
 * a new document is started. After we've finished merging, we add an extra
 * page with the table of contents and links to the named destinations.
 */
package sandbox.merge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class NUpWithLink {
    public static final String SRC1 = "resources/pdfs/links1.pdf";
    public static final String SRC2 = "resources/pdfs/links2.pdf";
    public static final String DEST = "results/merge/nup_links.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new NUpWithLink().createPdf(DEST);
    }
    
    public void createPdf(String filename) throws IOException, DocumentException {
         Document document = new Document();
         PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
         float W = PageSize.A4.getWidth() / 2;
         float H = PageSize.A4.getHeight() / 2;
         document.open();
         int firstPage = 1;
         String[] files = new String[]{SRC1, SRC2};
         PdfContentByte cb = writer.getDirectContent();
         for (int i = 0; i < files.length; i++) {
            PdfReader currentReader = new PdfReader(files[i]);
            currentReader.consolidateNamedDestinations();
            for (int page = 1; page <= currentReader.getNumberOfPages(); page++) {
                PdfImportedPage importedPage = writer.getImportedPage(currentReader, page);
                float a = 0.5f;
                float e = (page % 2 == 0) ? W : 0;
                float f = (page % 4 == 1 || page % 4 == 2) ? H : 0;
                ArrayList<PdfAnnotation.PdfImportedLink> links = currentReader.getLinks(page);
                cb.addTemplate(importedPage, a, 0, 0, a, e, f);
                for (int j = 0; j < links.size(); j++) {
                    PdfAnnotation.PdfImportedLink link = (PdfAnnotation.PdfImportedLink)links.get(j);
                    if (link.isInternal()) {
                        int dPage = link.getDestinationPage();
                        int newDestPage = (dPage-1)/4 + firstPage;
                        float ee = (dPage % 2 == 0) ? W : 0;
                        float ff = (dPage % 4 == 1 || dPage % 4 == 2) ? H : 0;
                        link.setDestinationPage(newDestPage);
                        link.transformDestination(a, 0, 0, a, ee, ff);
                    }
                    link.transformRect(a, 0, 0, a, e, f);
                    writer.addAnnotation(link.createAnnotation(writer));
                }
                if (page % 4 == 0)
                document.newPage();
            }
            if (i < files.length - 1)
                document.newPage();
            firstPage += (currentReader.getNumberOfPages()+3)/4;
         }
         document.close();
    }
}
