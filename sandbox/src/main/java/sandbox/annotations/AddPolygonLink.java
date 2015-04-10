/*
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27083206/itextshape-clickable-polygon-or-path
 */
package sandbox.annotations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class AddPolygonLink {

    public static final String SRC = "resources/pdfs/hello.pdf";
    public static final String DEST = "results/annotations/link_polygon.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddPolygonLink().manipulatePdf(SRC, DEST);;
    }
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte canvas = stamper.getOverContent(1);
        canvas.moveTo(36, 700);
        canvas.lineTo(72, 760);
        canvas.lineTo(144, 720);
        canvas.lineTo(72, 730);
        canvas.closePathStroke();
        Rectangle linkLocation = new Rectangle(36, 700, 144, 760);
        PdfArray array = new PdfArray(new int[]{72, 730, 144, 720, 72, 760, 36, 700});
        PdfDestination destination = new PdfDestination(PdfDestination.FIT);
        PdfAnnotation link = PdfAnnotation.createLink(stamper.getWriter(),
                linkLocation, PdfAnnotation.HIGHLIGHT_INVERT,
                1, destination);
        link.put(PdfName.QUADPOINTS, array);
        stamper.addAnnotation(link, 1);
        stamper.close();
    }
}
