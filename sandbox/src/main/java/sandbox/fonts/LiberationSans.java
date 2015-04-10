/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24220668/fontfactory-lowagie-java-getting-java-io-eofexception-when-trying-to-use-gre
 */
package sandbox.fonts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class LiberationSans {


    public static final String DEST = "results/fonts/LiberationSans.pdf";
    public static final String FONT = "resources/fonts/LiberationSans-Regular.ttf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new LiberationSans().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(DEST));
        document.open();
        FontFactory.register(FONT,"Greek-Regular");
        Font f = FontFactory.getFont("Greek-Regular", "Cp1253", true);
        Paragraph p = new Paragraph("\u039d\u03cd\u03c6\u03b5\u03c2", f);
        document.add(p);
        document.close();
    }
}
