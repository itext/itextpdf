/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19976343/how-to-set-the-paragraph-of-itext-pdf-file-as-rectangle-with-background-color-in
 */
package sandbox.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class ColumnTextParagraphs {

    public static final String DEST = "results/objects/column_paragraphs.pdf";

    public static final String TEXT = "This is some long paragraph that will be added over and over again to prove a point.";
    public static final Rectangle[] COLUMNS = {
        new Rectangle(36, 36, 290, 806),
        new Rectangle(305, 36, 559, 806)
    };
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColumnTextParagraphs().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        ColumnText ct = new ColumnText(canvas);
        int side_of_the_page = 0;
        ct.setSimpleColumn(COLUMNS[side_of_the_page]);
        int paragraphs = 0;
        while (paragraphs < 30) {
            ct.addElement(new Paragraph(String.format("Paragraph %s: %s", ++paragraphs, TEXT)));
            while (ColumnText.hasMoreText(ct.go())) {
                if (side_of_the_page == 0) {
                    side_of_the_page = 1;
                    canvas.moveTo(297.5f, 36);
                    canvas.lineTo(297.5f, 806);
                    canvas.stroke();
                }
                else {
                    side_of_the_page = 0;
                    document.newPage();
                }
                ct.setSimpleColumn(COLUMNS[side_of_the_page]);
            }
        }
        // step 5
        document.close();
    }
}
