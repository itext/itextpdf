package sandbox.annotations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@WrapToTest
public class AddLinkAnnotation {

    public static final String SRC = "resources/pdfs/primes.pdf";
    public static final String DEST = "results/annotations/link_annotation.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddLinkAnnotation().manipulatePdf(SRC, DEST);;
    }
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        Rectangle linkLocation = new Rectangle(523, 770, 559, 806);
        PdfDestination destination = new PdfDestination(PdfDestination.FIT);
        PdfAnnotation link = PdfAnnotation.createLink(stamper.getWriter(),
                linkLocation, PdfAnnotation.HIGHLIGHT_INVERT,
                3, destination);
        stamper.addAnnotation(link, 1);
        stamper.close();
    }
}
