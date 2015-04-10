/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24772212/itext-pdf-writer-is-there-any-way-to-allow-unicode-subscript-symbol-in-the-pdf
 */
package sandbox.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class SubSuperScript {

    public static final String DEST = "results/objects/sub_superscript.pdf";
    public static final String FONT = "resources/fonts/Cardo-Regular.ttf";
    
    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SubSuperScript().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font f = new Font(bf, 10);
        Paragraph p = new Paragraph("H\u2082SO\u2074", f);
        document.add(p);
        document.close();
    }
}
