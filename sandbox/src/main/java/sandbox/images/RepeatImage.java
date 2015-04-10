/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/21720802/how-to-make-text-invisible-in-an-existing-pdf
 * 
 * In this example, we take an image that is present in the background,
 * and we add the same image (by its reference) to the foreground so that
 * it covers the OCR'd text.
 */
package sandbox.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@WrapToTest
public class RepeatImage {

    public static final String SRC = "resources/pdfs/chinese.pdf";
    public static final String DEST = "results/images/chinese.pdf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RepeatImage().manipulatePdf(SRC, DEST);
    }
    
    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        // We assume that there's a single large picture on the first page
        PdfDictionary page = reader.getPageN(1);
        PdfDictionary resources = page.getAsDict(PdfName.RESOURCES);
        PdfDictionary xobjects = resources.getAsDict(PdfName.XOBJECT);
        PdfName imgName = xobjects.getKeys().iterator().next();
        Image img = Image.getInstance((PRIndirectReference)xobjects.getAsIndirectObject(imgName));
        img.setAbsolutePosition(0, 0);
        img.scaleAbsolute(reader.getPageSize(1));
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.getOverContent(1).addImage(img);
        stamper.close();
        reader.close();
    }

}
