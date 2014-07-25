/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24568386/set-baseurl-of-an-existing-pdf-document
 */
package sandbox.interactive;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BaseURL3 {
    public static final String SRC = "resources/pdfs/base_url.pdf";
    public static final String DEST = "results/interactive/base_url_3.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new BaseURL3().manipulatePdf(SRC, DEST);
    }
    
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfDictionary uri = new PdfDictionary(PdfName.URI);
        uri.put(new PdfName("Base"), new PdfString("http://itextpdf.com/"));
        reader.getCatalog().put(PdfName.URI, uri);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
    }
}
