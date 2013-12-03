/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19700549/itextsharp-images-are-not-coming-next-to-one-another
 * 
 * We create a table with two columns and two cells.
 * This way, we can add two images next to each other.
 */
package sandbox.tables;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ImagesNextToEachOther {
    public static void main(String[] args) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("results/images_next_to_each_other.pdf"));
        document.open();
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(createImageCell("resources/images/javaone2013.jpg"));
        table.addCell(createImageCell("resources/images/berlin2013.jpg"));
        document.add(table);
        document.close();
    }
    
    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
    	PdfPCell cell = new PdfPCell(img, true);
    	return cell;
    }
}