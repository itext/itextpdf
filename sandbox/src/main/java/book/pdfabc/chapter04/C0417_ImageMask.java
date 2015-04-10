/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package book.pdfabc.chapter04;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class C0417_ImageMask {

    public static final String DEST = "results/pdfabc/chapter04/image_mask.pdf";

    public static final String RESOURCE = "resources/images/bruno.jpg";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0417_ImageMask().createPdf(DEST);;
    }
    
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hard mask:"));
        document.add(getImageHardMask());
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Soft mask:"));
        document.add(getImageSoftMask());
        // step 5
        document.close();
    }
    
    public Image getImageHardMask() throws DocumentException, IOException {
        byte circledata[] = { (byte) 0x3c, (byte) 0x7e, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x7e,
                (byte) 0x3c };
        Image mask = Image.getInstance(8, 8, 1, 1, circledata);
        mask.makeMask();
        mask.setInverted(true);
        Image img = Image.getInstance(RESOURCE);
        img.setImageMask(mask);
        return img;
    }
    
    public Image getImageSoftMask() throws DocumentException, IOException {
        byte gradient[] = new byte[256];
        for (int i = 0; i < 256; i++)
            gradient[i] = (byte) i;
        Image mask = Image.getInstance(256, 1, 1, 8, gradient);
        mask.makeMask();
        Image img = Image.getInstance(RESOURCE);
        img.setImageMask(mask);
        return img;
    }
}
