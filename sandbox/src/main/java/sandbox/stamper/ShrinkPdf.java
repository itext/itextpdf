/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/25356302/shrink-pdf-pages-with-rotation-using-rectangle-in-existing-pdf
 */
package sandbox.stamper;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShrinkPdf {

    public static final String SRC = "resources/pdfs/hero.pdf";
    public static final String DEST = "results/stamper/hero_shrink.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ShrinkPdf().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        int n = reader.getNumberOfPages();
        PdfDictionary page;
        PdfArray crop;
        PdfArray media;
        for (int p = 1; p <= n; p++) {
            page = reader.getPageN(p);
            media = page.getAsArray(PdfName.CROPBOX);
            if (media == null) {
                media = page.getAsArray(PdfName.MEDIABOX);
            }
            crop = new PdfArray();
            crop.add(new PdfNumber(0));
            crop.add(new PdfNumber(0));
            crop.add(new PdfNumber(media.getAsNumber(2).floatValue() / 2));
            crop.add(new PdfNumber(media.getAsNumber(3).floatValue() / 2));
            page.put(PdfName.MEDIABOX, crop);
            page.put(PdfName.CROPBOX, crop);
            stamper.getUnderContent(p).setLiteral("\nq 0.5 0 0 0.5 0 0 cm\nq\n");
            stamper.getOverContent(p).setLiteral("\nQ\nQ\n");
        }
        stamper.close();
        reader.close();
    }
}
