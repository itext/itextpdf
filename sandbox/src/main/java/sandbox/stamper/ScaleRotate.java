/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21871027/rotating-in-itextsharp-while-preserving-comment-location-orientation
 * 
 * Example that shows how to scale an existing PDF using the UserUnit and how to remove the rotation of a page.
 */
package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@WrapToTest
public class ScaleRotate {

    public static final String SRC = "resources/pdfs/pages.pdf";
    public static final String DEST = "results/stamper/pages_altered.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ScaleRotate().manipulatePdf(SRC, DEST);
    }


    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        PdfDictionary page;
        for (int p = 1; p <= n; p++) {
            page = reader.getPageN(p);
            if (page.getAsNumber(PdfName.USERUNIT) == null)
                page.put(PdfName.USERUNIT, new PdfNumber(2.5f));
            page.remove(PdfName.ROTATE);
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
        reader.close();
    }
}
