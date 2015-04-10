/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22093488/itext-how-do-i-get-the-rendered-dimensions-of-text
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class TruncateTextInCell {
    
    public static final String DEST = "results/tables/truncate_cell_content.pdf";

    public class TruncateContent implements PdfPCellEvent {
        protected String content;
        public TruncateContent(String content) {
            this.content = content;
        }
        public void cellLayout(PdfPCell cell, Rectangle position,
                PdfContentByte[] canvases) {
            try {
                BaseFont bf = BaseFont.createFont();
                Font font = new Font(bf, 12);
                float availableWidth = position.getWidth();
                int contentLength = content.length();
                int leftChar = 0;
                int rightChar = contentLength - 1;
                availableWidth -= bf.getWidthPoint("...", 12);
                while (leftChar < contentLength && rightChar != leftChar) {
                    availableWidth -= bf.getWidthPoint(content.charAt(leftChar), 12);
                    if (availableWidth > 0)
                        leftChar++;
                    else
                        break;
                    availableWidth -= bf.getWidthPoint(content.charAt(rightChar), 12);
                    if (availableWidth > 0)
                        rightChar--;
                    else
                        break;
                }
                String newContent = content.substring(0, leftChar) + "..." + content.substring(rightChar);
                PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
                ColumnText ct = new ColumnText(canvas);
                ct.setSimpleColumn(position);
                ct.addElement(new Paragraph(newContent, font));
                ct.go();
            } catch (DocumentException e) {
                throw new ExceptionConverter(e);
            } catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }
    }
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TruncateTextInCell().createPdf(DEST);
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
                    cell.setCellEvent(new TruncateContent("D2 is a cell with more content than we can fit into the cell."));
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
