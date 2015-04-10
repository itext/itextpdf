/**
 * Example written by Bruno Lowagie.
 */
package sandbox.merge;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class TilingHero {

    /** The original PDF file. */
    public static final String RESOURCE
        = "resources/pdfs/hero.pdf";

    /** The resulting PDF file. */
    public static final String RESULT
        = "results/merge/superman.pdf";
    
    /**
     * Manipulates a PDF file src with the file dest as result
     * @param src the original PDF
     * @param dest the resulting PDF
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest)
        throws IOException, DocumentException {
    	// Creating a reader
        PdfReader reader = new PdfReader(src);
        Rectangle pagesize = reader.getPageSizeWithRotation(1);
        float width = pagesize.getWidth();
        float height = pagesize.getHeight();
        // step 1
        Rectangle mediabox = new Rectangle(0, 3 * height, width, 4 * height);
        Document document = new Document(mediabox);
        // step 2
        PdfWriter writer
            = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // step 3
        document.open();
        // step 4
        PdfContentByte content = writer.getDirectContent();
        PdfImportedPage page = writer.getImportedPage(reader, 1);
        // adding the same page 16 times with a different offset
        for (int i = 0; i < 16; ) {
            content.addTemplate(page, 4, 0, 0, 4, 0, 0);
            i++;
            mediabox = new Rectangle(
                    (i % 4) * width, (4 - (i / 4)) * height,
                    ((i % 4) + 1) * width, (3 - (i / 4)) * height);
            document.setPageSize(mediabox);
            document.newPage();
        }
        // step 4
        document.close();
        reader.close();
    }

    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args)
        throws IOException, DocumentException {
        new TilingHero().manipulatePdf(RESOURCE, RESULT);
    }
}

