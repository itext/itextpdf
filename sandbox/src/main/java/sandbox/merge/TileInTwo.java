/*
 * Example written by Bruno Lowagie in answer to a question on StackOverflow:
 * http://stackoverflow.com/questions/27011829/divide-one-page-pdf-file-in-two-pages-pdf-file
 */
package sandbox.merge;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TileInTwo {
    
    /** The original PDF file. */
    public static final String SRC
        = "resources/pdfs/united_states.pdf";

    /** The resulting PDF file. */
    public static final String DEST
        = "results/merge/unitedstates_tiled.pdf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TileInTwo().manipulatePdf(SRC, DEST);
    }
    
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException {
    	// Creating a reader
        PdfReader reader = new PdfReader(src);
        int n = reader.getNumberOfPages();
        // step 1
        Rectangle mediabox = new Rectangle(getHalfPageSize(reader.getPageSizeWithRotation(1)));
        Document document = new Document(mediabox);
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // step 3
        document.open();
        // step 4
        PdfContentByte content = writer.getDirectContent();
        PdfImportedPage page;
        int i = 1;
        while (true) {
            page = writer.getImportedPage(reader, i);
            content.addTemplate(page, 0, -mediabox.getHeight());
            document.newPage();
            content.addTemplate(page, 0, 0);
            if (++i > n)
                break;
            mediabox = new Rectangle(getHalfPageSize(reader.getPageSizeWithRotation(i)));
            document.setPageSize(mediabox);
            document.newPage();
        }
        // step 5
        document.close();
        reader.close();
    }
    
    public Rectangle getHalfPageSize(Rectangle pagesize) {
        float width = pagesize.getWidth();
        float height = pagesize.getHeight();
        return new Rectangle(width, height / 2);
    }
}
