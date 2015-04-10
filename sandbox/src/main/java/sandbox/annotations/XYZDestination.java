/**
 * This example was written to create a sample file for use in code that answers the following question:
 * http://stackoverflow.com/questions/22828782/all-links-of-existing-pdf-change-the-action-property-to-inherit-zoom-itext-lib
 */
package sandbox.annotations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class XYZDestination {

    public static final String DEST = "results/annotations/xyz_destination.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new XYZDestination().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        for (int i = 0; i < 10; i++) {
            document.add(new Paragraph("Test"));
            document.newPage();
        }
        PdfDestination d;
        Chunk c;
        for (int i = 0; i < 10; ) {
            i++;
            d = new PdfDestination(PdfDestination.XYZ, 36, 806, i);
            c = new Chunk("Goto page " + i);
            c.setAnnotation(PdfAnnotation.createLink(writer, new Rectangle(0, 0), PdfAnnotation.HIGHLIGHT_NONE, i, d));
            document.add(new Paragraph(c));
        }
        document.close();
    }
}
