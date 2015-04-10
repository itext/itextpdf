/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/26814958/pdf-vertical-postion-method-gives-the-next-page-position-instead-of-current-page
 */
package sandbox.images;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WatermarkedImages2 {
    public static final String IMAGE1 = "resources/images/bruno.jpg";
    public static final String IMAGE2 = "resources/images/dog.bmp";
    public static final String IMAGE3 = "resources/images/fox.bmp";
    public static final String IMAGE4 = "resources/images/bruno_ingeborg.jpg";
    public static final Font FONT = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, GrayColor.GRAYWHITE);
    public static final String DEST = "results/images/watermark_table.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new WatermarkedImages2().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell;
        cell = new PdfPCell(Image.getInstance(IMAGE1), true);
        cell.setCellEvent(new WatermarkedCell("Bruno"));
        table.addCell(cell);
        cell = new PdfPCell(Image.getInstance(IMAGE2), true);
        cell.setCellEvent(new WatermarkedCell("Dog"));
        table.addCell(cell);
        cell = new PdfPCell(Image.getInstance(IMAGE3), true);
        cell.setCellEvent(new WatermarkedCell("Fox"));
        table.addCell(cell);
        cell = new PdfPCell(Image.getInstance(IMAGE4), true);
        cell.setCellEvent(new WatermarkedCell("Bruno and Ingeborg"));
        table.addCell(cell);
        document.add(table);
        document.close();
    }
    
    class WatermarkedCell implements PdfPCellEvent {
        String watermark;
        
        public WatermarkedCell(String watermark) {
            this.watermark = watermark;
        }
        
        public void cellLayout(PdfPCell cell, Rectangle position,
            PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER,
                new Phrase(watermark, FONT),
                (position.getLeft() + position.getRight()) / 2,
                (position.getBottom() + position.getTop()) / 2, 30);
        }
    }
}
