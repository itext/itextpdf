/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27020542/rotating-pdf-90-degrees-using-itextsharp-in-c-sharp
 */
package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Rotate90Degrees {

    public static final String SRC = "resources/pdfs/pages.pdf";
    public static final String DEST = "results/stamper/pages_rotated90degrees.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Rotate90Degrees().manipulatePdf(SRC, DEST);
    }


    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        PdfDictionary page;
        PdfNumber rotate;
        for (int p = 1; p <= n; p++) {
            page = reader.getPageN(p);
            rotate = page.getAsNumber(PdfName.ROTATE);
            if (rotate == null) {
                page.put(PdfName.ROTATE, new PdfNumber(90));
            }
            else {
                page.put(PdfName.ROTATE, new PdfNumber((rotate.intValue() + 90) % 360));
            }
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
        reader.close();
    }
}
