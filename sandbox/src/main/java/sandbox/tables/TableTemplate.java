/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22093993/itext-whats-an-easy-to-print-first-right-then-down
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class TableTemplate {
    
    public static final String DEST = "results/tables/table_template.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableTemplate().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(15);
        table.setTotalWidth(1500);
        PdfPCell cell;
        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 15; c++) {
                cell = new PdfPCell();
                cell.setFixedHeight(50);
                cell.addElement(new Paragraph(String.valueOf((char) r) + String.valueOf(c)));
                table.addCell(cell);
            }
        }
        PdfContentByte canvas = writer.getDirectContent();
        PdfTemplate tableTemplate = canvas.createTemplate(1500, 1300);
        table.writeSelectedRows(0, -1, 0, 1300, tableTemplate);
        PdfTemplate clip;
        for (int j = 0; j < 1500; j += 500) {
            for (int i = 1300; i > 0; i -= 650) {
                clip = canvas.createTemplate(500, 650);
                clip.addTemplate(tableTemplate, -j, 650 - i);
                canvas.addTemplate(clip, 36, 156);
                document.newPage();
            }
        }
        document.close();
    }
}
