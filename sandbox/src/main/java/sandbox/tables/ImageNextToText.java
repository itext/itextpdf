/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/25780258/how-to-display-image-and-text-beside-each-other-itext
 */
package sandbox.tables;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageNextToText {
    public static final String DEST = "results/tables/image_next_to_text.pdf";
    public static final String IMG1 = "resources/images/javaone2013.jpg";

    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ImageNextToText().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 2});
        table.addCell(createImageCell(IMG1));
        table.addCell(createTextCell("This picture was taken at Java One.\nIt shows the iText crew at Java One in 2013."));
        document.add(table);
        document.close();
    }
    
    public static PdfPCell createImageCell(String path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }
    
    public static PdfPCell createTextCell(String text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}