/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22828782/all-links-of-existing-pdf-change-the-action-property-to-inherit-zoom-itext-lib
 */
package sandbox.annotations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ChangeZoomXYZDestination {

    public static final String SRC = "resources/pdfs/xyz_destination.pdf";
    public static final String DEST = "results/annotations/xyz_zoom.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeZoomXYZDestination().manipulatePdf(SRC, DEST);;
    }
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfDictionary page = reader.getPageN(11);
        PdfArray annots = page.getAsArray(PdfName.ANNOTS); 
        for (int i = 0; i < annots.size(); i++) {
            PdfDictionary annotation = annots.getAsDict(i);
            if (PdfName.LINK.equals(annotation.getAsName(PdfName.SUBTYPE))) {
        	    PdfArray d = annotation.getAsArray(PdfName.DEST);
        	    if (d != null && d.size() == 5 && PdfName.XYZ.equals(d.getAsName(1)))
        	        d.set(4, new PdfNumber(0));
            }
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
    }
}
