/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22093745/itext-how-do-setminimumsize-and-setfixedsize-interact
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class FixedHeightCell {
    
    public static final String DEST = "results/tables/fixed_height_cell.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FixedHeightCell().createPdf(DEST);
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
                cell.addElement(new Paragraph(String.valueOf((char) r) + String.valueOf(c)));
                if (r == 'D')
                    cell.setFixedHeight(60);
                if (r == 'E') {
                    cell.setFixedHeight(60);
                    if (c == 4)
                        cell.setFixedHeight(120);
                }
                if (r == 'F') {
                    cell.setMinimumHeight(120);
                    cell.setFixedHeight(60);
                    if (c == 2)
                        cell.addElement(new Paragraph("This cell has more content than the other cells"));
                }
                table.addCell(cell);
            }
        }
        document.add(table);
        document.close();
    }
}
