/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24087786/how-to-set-zoom-level-to-pdf-using-itextsharp-5-5-0-0
 */
package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class AddOpenAction {

    public static final String SRC = "resources/pdfs/hello.pdf";
    public static final String DEST = "results/stamper/hello_open75p.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddOpenAction().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfDestination pdfDest = new PdfDestination(PdfDestination.XYZ, 0, reader.getPageSize(1).getHeight(), 0.75f);
        PdfAction action = PdfAction.gotoLocalPage(1, pdfDest, stamper.getWriter());
        stamper.getWriter().setOpenAction(action);
        stamper.close();
        reader.close();
    }
}
