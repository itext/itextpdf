package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSpotColor;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.SpotColor;

@WrapToTest
public class AddSpotColorShape {
    public static final String SRC = "resources/pdfs/image.pdf";
    public static final String DEST = "results/stamper/spot_color.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddSpotColorShape().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte canvas = stamper.getOverContent(1);
        canvas.arc(0, 0, 842, 595, 0, 360);
        canvas.arc(25, 25, 817, 570, 0, 360);
        canvas.arc(50, 50, 792, 545, 0, 360);
        canvas.arc(75, 75, 767, 520, 0, 360);
        canvas.eoClip();
        canvas.newPath();
        PdfSpotColor psc = new PdfSpotColor("mySpotColor", new CMYKColor(0.8f, 0.3f, 0.3f, 0.1f));
        canvas.setColorFill(new SpotColor(psc, 0.4f));
        canvas.rectangle(0, 0, 842, 595);
        canvas.fill();
        stamper.close();
        reader.close();
    }
}
