/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/26773942/itext-crop-out-a-part-of-pdf-file
 */
package sandbox.stamper;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ClipPdf {

    public static final String SRC = "resources/pdfs/hero.pdf";
    public static final String DEST = "results/stamper/hero_clipped.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ClipPdf().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        int n = reader.getNumberOfPages();
        PdfDictionary page;
        PdfArray media;
        for (int p = 1; p <= n; p++) {
            page = reader.getPageN(p);
            media = page.getAsArray(PdfName.CROPBOX);
            if (media == null) {
                media = page.getAsArray(PdfName.MEDIABOX);
            }
            float llx = media.getAsNumber(0).floatValue() + 200;
            float lly = media.getAsNumber(1).floatValue() + 200;
            float w = media.getAsNumber(2).floatValue() - media.getAsNumber(0).floatValue() - 400;
            float h = media.getAsNumber(3).floatValue() - media.getAsNumber(1).floatValue() - 400;
            String command = String.format(
                    "\nq %.2f %.2f %.2f %.2f re W n\nq\n",
                    llx, lly, w, h);
            stamper.getUnderContent(p).setLiteral(command);
            stamper.getOverContent(p).setLiteral("\nQ\nQ\n");
        }
        stamper.close();
        reader.close();
    }
}
