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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

public class C0418_TransparentImage {

    public static final String DEST = "results/pdfabc/chapter04/transparent_image.pdf";
    
    /** One of the resources. */
    public static final String RESOURCE1
        = "resources/images/bruno.jpg";
    /** One of the resources. */
    public static final String RESOURCE2
        = "resources/images/info.png";
    /** One of the resources. */
    public static final String RESOURCE3
        = "resources/images/logo.gif";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new C0418_TransparentImage().createPdf(DEST);
    }

    public void createPdf(String filename) throws IOException, DocumentException {
        Image img1 = Image.getInstance(RESOURCE1);
        Document document = new Document(img1);
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        img1.setAbsolutePosition(0, 0);
        document.add(img1);
        Image img2 = Image.getInstance(RESOURCE2);
        img2.setAbsolutePosition(0, 260);
        document.add(img2);
        Image img3 = Image.getInstance(RESOURCE3);
        img3.setTransparency(new int[]{ 0xF0, 0xFF });
        img3.setAbsolutePosition(0, 0);
        document.add(img3);
        document.close();
    }

}
