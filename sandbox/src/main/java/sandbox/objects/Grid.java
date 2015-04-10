package sandbox.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class Grid {

    public static final String DEST = "results/objects/grid.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Grid().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws FileNotFoundException, DocumentException {
        Rectangle pagesize = PageSize.LETTER;
        Document document = new Document(pagesize);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();
        for (float x = 0; x < pagesize.getWidth(); ) {
            for (float y = 0; y < pagesize.getHeight(); ) {
                canvas.circle(x, y, 1f);
                y += 72f;
            }
            x += 72f;
        }
        canvas.fill();
        document.close();
    }

}
