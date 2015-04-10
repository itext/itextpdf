/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/22382717/write-two-itext-paragraphs-on-the-same-position
 * 
 * We create a Chunk and add a background color.
 */
package sandbox.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import sandbox.WrapToTest;

@WrapToTest
public class UnderlineWithDottedLine {
    public static final String DEST = "results/objects/underline_dotted.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new UnderlineWithDottedLine().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        Paragraph p = new Paragraph("This line will be underlined with a dotted line.");
        DottedLineSeparator dottedline = new DottedLineSeparator();
        dottedline.setOffset(-2);
        dottedline.setGap(2f);
        p.add(dottedline);
        document.add(p);
        document.close();
    }
}
