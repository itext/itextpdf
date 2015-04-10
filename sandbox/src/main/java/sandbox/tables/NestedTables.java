/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24562448/the-table-width-must-be-greater-than-zero-exception-when-using-nested-tables
 */
package sandbox.tables;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author iText
 */
public class NestedTables {
    
    public static final String DEST = "results/tables/nested_tables.pdf";
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new NestedTables().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        float[] columnWidths = {183, 31, 88, 49, 35, 25, 35, 35, 35, 32, 32, 33, 35, 60, 46, 26 };
        PdfPTable table = new PdfPTable(columnWidths);
        table.setTotalWidth(770F);
        table.setLockedWidth(true);
        buildNestedTables(table);
        document.add(table);
        document.close();
    }
    
    private void buildNestedTables(PdfPTable outerTable) {
        PdfPTable innerTable1 = new PdfPTable(1);
        PdfPTable innerTable2 = new PdfPTable(2);
        PdfPCell cell;
        innerTable1.addCell("Cell 1");
        innerTable1.addCell("Cell 2");
        outerTable.addCell(innerTable1);
        innerTable2.addCell("Cell 3");
        innerTable2.addCell("Cell 4");
        outerTable.addCell(innerTable2);
        cell = new PdfPCell();
        cell.setColspan(14);
        outerTable.addCell(cell);
   }
}
