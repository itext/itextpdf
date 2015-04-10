/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23374557/itextsharp-why-cell-background-image-is-rotated-90-degress-clockwise
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPatternPainter;
import com.itextpdf.text.pdf.PdfWriter;

public class TiledBackground {

    class TiledImageBackground implements PdfPCellEvent {

        protected Image image;
        
        public TiledImageBackground(Image image) {
            this.image = image;
        }
        
        public void cellLayout(PdfPCell cell, Rectangle position,
                PdfContentByte[] canvases) {
            try {
                PdfContentByte cb = canvases[PdfPTable.BACKGROUNDCANVAS];
                PdfPatternPainter patternPainter = cb.createPattern(image.getScaledWidth(), image.getScaledHeight());
                image.setAbsolutePosition(0, 0);
                patternPainter.addImage(image);
                cb.saveState();
                cb.setPatternFill(patternPainter);
                cb.rectangle(position.getLeft(), position.getBottom(), position.getWidth(), position.getHeight());
                cb.fill();
                cb.restoreState();
            } catch (DocumentException e) {
                throw new ExceptionConverter(e);
            }
        }
        
    }
    
    public static final String DEST = "results/tables/tiled_pattern.pdf";
    public static final String IMG1 = "resources/images/ALxRF.png";
    public static final String IMG2 = "resources/images/bulb.gif";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TiledBackground().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell = new PdfPCell();
        Image image = Image.getInstance(IMG1);
        cell.setCellEvent(new TiledImageBackground(image));
        cell.setFixedHeight(770);
        table.addCell(cell);
        cell = new PdfPCell();
        image = Image.getInstance(IMG2);
        cell.setCellEvent(new TiledImageBackground(image));
        cell.setFixedHeight(770);
        table.addCell(cell);
        document.add(table);
        document.close();
    }
}
