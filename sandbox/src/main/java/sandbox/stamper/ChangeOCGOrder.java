/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/23280083/itextsharp-change-order-of-optional-content-groups
 */
package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ChangeOCGOrder {

    public static final String SRC = "resources/pdfs/ocg.pdf";
    public static final String DEST = "results/stamper/ocg_reordered.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeOCGOrder().manipulatePdf(SRC, DEST);
    }


    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary ocProps = catalog.getAsDict(PdfName.OCPROPERTIES);
        PdfDictionary occd = ocProps.getAsDict(PdfName.D);
        PdfArray order = occd.getAsArray(PdfName.ORDER);
        PdfObject nestedLayers = order.getPdfObject(0);
        PdfObject nestedLayerArray = order.getPdfObject(1);
        PdfObject groupedLayers = order.getPdfObject(2);
        PdfObject radiogroup = order.getPdfObject(3);
        order.set(0, radiogroup);
        order.set(1, nestedLayers);
        order.set(2, nestedLayerArray);
        order.set(3, groupedLayers);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
        reader.close();
    }

}
