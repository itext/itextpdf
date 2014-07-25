/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24807638/writing-a-large-graphics-2d-document-to-multiple-pdf
 */
package sandbox.graphics2d;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author iText
 */
public class LargeTemplate {
    
    public static final String DEST = "results/graphics2d/large_template.pdf";    
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new LargeTemplate().createPdf(DEST);
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        float width = 602;
        float height = 15872;
        float maxHeight = 5000;
        Document document = new Document(new Rectangle(width, maxHeight));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DEST));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        PdfTemplate template = canvas.createTemplate(width, height);
        Graphics2D g2d = new PdfGraphics2D(template, width, height);
        for (int x = 10; x < width; x += 100) {
            for (int y = 10; y < height; y += 100) {
                g2d.drawString(String.format("(%s,%s)", x, y), x, y);
            }
        }
        g2d.dispose();
        int pages = ((int)height / (int)maxHeight) + 1;
        for (int p = 0; p < pages; ) {
            p++;
            canvas.addTemplate(template, 0, (p * maxHeight) - height);
            document.newPage();
        }
        document.close();
    }
}
