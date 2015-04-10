/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/26983703/itext-how-to-stamp-image-on-existing-pdf-and-create-an-anchor
 */
package sandbox.annotations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderArray;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class AddImageLink {

    public static final String SRC = "resources/pdfs/primes.pdf";
    public static final String IMG = "resources/images/info.png";
    public static final String DEST = "results/annotations/link_image.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddImageLink().manipulatePdf(SRC, DEST);
    }
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        Image img = Image.getInstance(IMG);
        float x = 10;
        float y = 650;
        float w = img.getScaledWidth();
        float h = img.getScaledHeight();
        img.setAbsolutePosition(x, y);
        stamper.getOverContent(1).addImage(img);
        Rectangle linkLocation = new Rectangle(x, y, x + w, y + h);
        PdfDestination destination = new PdfDestination(PdfDestination.FIT);
        PdfAnnotation link = PdfAnnotation.createLink(stamper.getWriter(),
                linkLocation, PdfAnnotation.HIGHLIGHT_INVERT,
                reader.getNumberOfPages(), destination);
        link.setBorder(new PdfBorderArray(0, 0, 0));
        stamper.addAnnotation(link, 1);
        stamper.close();
    }
}
