/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24568386/set-baseurl-of-an-existing-pdf-document
 */
package sandbox.interactive;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BaseURL2 {
    public static final String DEST = "results/interactive/base_url_2.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new BaseURL2().createPdf(DEST);
    }
    
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfDictionary uri = new PdfDictionary(PdfName.URI);
        uri.put(new PdfName("Base"), new PdfString("http://itextpdf.com/"));
        writer.getExtraCatalog().put(PdfName.URI, uri);
        Anchor anchor = new Anchor("Home page");
        anchor.setReference("index.php");
        document.add(anchor);
        // step 5
        document.close();
    }
}
