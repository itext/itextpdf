/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24678640/itext-pdfdocument-page-size-inaccurate
 */
package sandbox.stamper;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author iText
 */
public class StampHeader2 {
    
    public static final String SRC = "resources/pdfs/Wrong.pdf";
    public static final String DEST = "results/stamper/stamped_header2.pdf";
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new StampHeader2().manipulatePdf(SRC, DEST);
    }
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.setRotateContents(false);
        Phrase header = new Phrase("Copy", new Font(FontFamily.HELVETICA, 14));
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            float x = reader.getPageSize(i).getWidth() / 2;
            float y = reader.getPageSize(i).getTop(20);
            ColumnText.showTextAligned(
                stamper.getOverContent(i), Element.ALIGN_CENTER,
                header, x, y, 0);
        }
        stamper.close();
        reader.close();
    }
}
