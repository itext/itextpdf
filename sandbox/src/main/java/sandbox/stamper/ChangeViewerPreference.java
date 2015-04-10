package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@WrapToTest
public class ChangeViewerPreference {

    public static final String SRC = "resources/pdfs/united_states.pdf";
    public static final String DEST = "results/stamper/us_duplex.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeViewerPreference().manipulatePdf(SRC, DEST);
    }


    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.addViewerPreference(PdfName.DUPLEX, PdfName.DUPLEXFLIPLONGEDGE);
        stamper.close();
        reader.close();
    }
}
