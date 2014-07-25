/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19700549/itextsharp-images-are-not-coming-next-to-one-another
 * 
 * We create a table with two columns and two cells.
 * This way, we can add two images next to each other.
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import sandbox.WrapToTest;

@WrapToTest
public class ImagesNextToEachOther {
    public static final String DEST = "results/tables/images_next_to_each_other.pdf";
    public static final String IMG1 = "resources/images/javaone2013.jpg";
    public static final String IMG2 = "resources/images/berlin2013.jpg";

    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ImagesNextToEachOther().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(createImageCell(IMG1));
        table.addCell(createImageCell(IMG2));
        document.add(table);
        document.close();
    }
    
    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }
}