/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23935566/table-borders-not-expanding-properly-in-pdf-using-itext
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEventAfterSplit;
import com.itextpdf.text.pdf.PdfWriter;

public class CustomBorder {
    
    public static final String DEST = "results/tables/custom_border.pdf";

    public static final String TEXT = "This is some long paragraph that will be added over and over again to prove a point. It should result in rows that are split and rows that aren't.";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CustomBorder().createPdf(DEST);
    }
   
    class BorderEvent implements PdfPTableEventAfterSplit {
    	
    	protected int rowCount;
        protected boolean bottom = true;
        protected boolean top = true;
        
        public void setRowCount(int rowCount) {
        	this.rowCount = rowCount;
        }
        
        public void splitTable(PdfPTable table) {
	    	if (table.getRows().size() != rowCount) {
    	        bottom = false;
	    	}
        }

        public void afterSplitTable(PdfPTable table, PdfPRow startRow, int startIdx) {
        	if (table.getRows().size() != rowCount) {
            // if the table gains a row, a row was split
                rowCount = table.getRows().size();
                top = false;
            }
        }
    	
        public void tableLayout(PdfPTable table, float[][] width, float[] height,
                int headerRows, int rowStart, PdfContentByte[] canvas) {
            float widths[] = width[0];
            float y1 = height[0];
            float y2 = height[height.length - 1];
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            for (int i = 0; i < widths.length; i++) {
                cb.moveTo(widths[i], y1);
                cb.lineTo(widths[i], y2);
            }
            float x1 = widths[0];
            float x2 = widths[widths.length - 1];
            for (int i = top ? 0 : 1; i < (bottom ? height.length : height.length - 1); i++) {
                cb.moveTo(x1, height[i]);
                cb.lineTo(x2, height[i]);
            }
            cb.stroke();
            cb.resetRGBColorStroke();
            bottom = true;
            top = true;
        }
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(500);
        table.setLockedWidth(true);
        BorderEvent event = new BorderEvent();
        table.setTableEvent(event);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setSplitLate(false);
        PdfPCell cell = new PdfPCell(new Phrase(TEXT));
        cell.setBorder(Rectangle.NO_BORDER);
        for (int i = 0; i < 60; ) {
            table.addCell("Cell " + (++i));
            table.addCell(cell);
        }
        event.setRowCount(table.getRows().size());
        document.add(table);
        document.close();
    }
}
