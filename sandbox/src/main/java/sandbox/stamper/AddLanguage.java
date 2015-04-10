/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24370273/set-initial-view-pdf-document-properties-using-itextsharp-with-c-sharp
 */
package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

public class AddLanguage {

    public static final String SRC = "resources/pdfs/hello.pdf";
    public static final String DEST = "results/stamper/hello_english.pdf";
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddLanguage().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.getWriter().getExtraCatalog().put(PdfName.LANG, new PdfString("EN"));
        stamper.close();
        reader.close();
    }

}
