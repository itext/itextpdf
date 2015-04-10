/**
 * This example is written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22153449/print-last-5-rows-to-next-page-itext-java
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class RepeatLastRows {
    public static final String DEST = "results/tables/repeat_last_rows.pdf";

    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RepeatLastRows().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        // we create a table that spans the width of the page and that has 99 rows
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(523);
        for (int i = 1; i < 100; i++)
            table.addCell("row " + i);
        // we add the table at an absolute position (starting at the top of the page)
        PdfContentByte canvas = writer.getDirectContent();
        int currentRowStart = 0;
        int currentRow = 0;
        int totalRows = table.getRows().size();
        while (true) {
            // available height of the page
            float available_height = 770;
            // how many rows fit the height?
            while (available_height > 0 && currentRow < totalRows) {
                available_height -= table.getRowHeight(currentRow++);
            }
            // we stop as soon as all the rows are counted
            if (currentRow == totalRows)
                break;
            // we draw part the rows that fit the page and start a new page
            table.writeSelectedRows(currentRowStart, --currentRow, 36, 806, canvas);
            document.newPage();
            currentRowStart = currentRow;
        }
        // if there are less than 5 rows left, we adjust the row start value
        if (currentRow - currentRowStart < 5)
            currentRowStart = currentRow - 5;
        // we write the remaining rows
        table.writeSelectedRows(currentRowStart, currentRow, 36, 806, canvas);
        document.close();
    }

}
