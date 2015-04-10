/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22095320/can-i-tell-itext-how-to-clip-text-to-fit-in-a-cell
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class ClipCenterCellContent {
    
    public static final String DEST = "results/tables/clip_center_cell_content.pdf";

    public class CenterContent implements PdfPCellEvent {
        protected Paragraph content;
        public CenterContent(Paragraph content) {
            this.content = content;
        }
        public void cellLayout(PdfPCell cell, Rectangle position,
                PdfContentByte[] canvases) {
            try {
                PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
                ColumnText ct = new ColumnText(canvas);
                ct.setSimpleColumn(new Rectangle(0, 0, position.getWidth(), -1000));
                ct.addElement(content);
                ct.go(true);
                float spaceneeded = 0 - ct.getYLine();
                System.out.println(String.format("The content requires %s pt whereas the height is %s pt.", spaceneeded, position.getHeight()));
                float offset = (position.getHeight() - spaceneeded) / 2;
                System.out.println(String.format("The difference is %s pt; we'll need an offset of %s pt.", -2f * offset, offset));
                PdfTemplate tmp = canvas.createTemplate(position.getWidth(), position.getHeight());
                ct = new ColumnText(tmp);
                ct.setSimpleColumn(0, offset, position.getWidth(), offset + spaceneeded);
                ct.addElement(content);
                ct.go();
                canvas.addTemplate(tmp, position.getLeft(), position.getBottom());
            } catch (DocumentException e) {
                throw new ExceptionConverter(e);
            }
        }
    }
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ClipCenterCellContent().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        PdfPCell cell;
        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 5; c++) {
                cell = new PdfPCell();
                if (r == 'D' && c == 2) {
                    cell.setCellEvent(new CenterContent(new Paragraph("D2 is a cell with more content than we can fit into the cell.")));
                }
                else {
                    cell.addElement(new Paragraph(String.valueOf((char) r) + String.valueOf(c)));
                }
                table.addCell(cell);
            }
        }
        document.add(table);
        document.close();
    }
}
