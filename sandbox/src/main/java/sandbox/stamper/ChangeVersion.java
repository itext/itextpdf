/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23083220/how-to-set-pdf-version-using-itextsharp
 */
package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ChangeVersion {

    public static final String SRC = "resources/pdfs/OCR.pdf";
    public static final String DEST = "results/stamper/other_version.pdf";
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeVersion().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest), '4');
        stamper.close();
        reader.close();
    }

}
