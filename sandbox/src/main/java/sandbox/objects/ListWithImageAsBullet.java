package sandbox.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class ListWithImageAsBullet {

    public static final String IMG = "resources/images/bulb.gif";
    public static final String DEST = "results/objects/list_with_image_bullet.pdf";
    
    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListWithImageAsBullet().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        Image image = Image.getInstance(IMG);
        image.scaleAbsolute(12, 12);
        image.setScaleToFitHeight(false);
        List list = new List();
        list.setListSymbol(new Chunk(Image.getInstance(image), 0, 0));
        list.add("Hello World");
        list.add("This is a list item with a lot of text. It will certainly take more than one line. This shows that the list item is indented and that the image is used as bullet.");
        list.add("This is a test");
        document.add(list);
        document.close();
    }

}
