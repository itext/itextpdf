package sandbox.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import sandbox.WrapToTest;

@WrapToTest
public class ReuseImage {

    public static final String SRC = "resources/pdfs/single_image.pdf";
    public static final String DEST = "results/images/image_on_A4.pdf";

    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ReuseImage().manipulatePdf(SRC, DEST);
    }
    
    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        // We assume that there's a single large picture on the first page
        PdfDictionary page = reader.getPageN(1);
        PdfDictionary resources = page.getAsDict(PdfName.RESOURCES);
        PdfDictionary xobjects = resources.getAsDict(PdfName.XOBJECT);
        PdfName imgRef = xobjects.getKeys().iterator().next();
        PRStream imgStream = (PRStream) xobjects.getAsStream(imgRef);
        // We now create a new Image object based on the bytes in the stream
        PdfImageObject imgObject = new PdfImageObject(imgStream);
        reader.close();
        Image img = Image.getInstance(imgObject.getImageAsBytes());
        img.scaleToFit(842, 595);
        img.setAbsolutePosition((842 - img.getScaledWidth()) / 2, (595 - img.getScaledHeight()) / 2);
        // We create a new document with the correct size
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        document.add(img);
        document.close();
    }
}
