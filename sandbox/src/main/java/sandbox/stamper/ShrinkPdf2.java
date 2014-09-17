/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/25356302/shrink-pdf-pages-with-rotation-using-rectangle-in-existing-pdf
 */
package sandbox.stamper;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShrinkPdf2 {

    public static final String SRC = "resources/pdfs/hero.pdf";
    public static final String DEST = "results/stamper/hero_shrink2.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ShrinkPdf2().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        int n = reader.getNumberOfPages();
        float percentage = 0.8f;
        for (int p = 1; p <= n; p++) {
            float offsetX = (reader.getPageSize(p).getWidth() * (1 - percentage)) / 2;
            float offsetY = (reader.getPageSize(p).getHeight() * (1 - percentage)) / 2;
            stamper.getUnderContent(p).setLiteral(
                    String.format("\nq %s 0 0 %s %s %s cm\nq\n", percentage, percentage, offsetX, offsetY));
            stamper.getOverContent(p).setLiteral("\nQ\nQ\n");
        }
        stamper.close();
        reader.close();
    }
}
