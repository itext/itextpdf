/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/25808367/rotate-multiline-text-with-columntext-itextsharp
 */
package sandbox.stamper;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfTemplate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddRotatedTemplate {

    public static final String SRC = "resources/pdfs/hello.pdf";
    public static final String DEST = "results/stamper/hello_template.pdf";
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddRotatedTemplate().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfContentByte cb = stamper.getOverContent(1);
        PdfTemplate xobject = cb.createTemplate(80, 120);
        ColumnText column = new ColumnText(xobject);
        column.setSimpleColumn(new Rectangle(80, 120));
        column.addElement(new Paragraph("Some long text that needs to be distributed over several lines."));
        column.go();
        cb.addTemplate(xobject, 36, 600);
        double angle = Math.PI / 4;
        cb.addTemplate(xobject,
                (float)Math.cos(angle), -(float)Math.sin(angle),
                (float)Math.cos(angle), (float)Math.sin(angle),
                150, 600);
        stamper.close();
        reader.close();
    }

}
