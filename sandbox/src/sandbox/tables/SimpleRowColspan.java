/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/20016630/how-to-create-a-table-in-a-generated-pdf-using-itextsharp
 * 
 * We create a table using rowspan and colspan.
 */
package sandbox.tables;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class SimpleRowColspan {

    public static void main(String[] args) throws IOException,
            DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(
            "results/simple_rowspan_colspan.pdf"));
        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setWidths(new int[]{ 1, 2, 2, 2, 1});
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("S/N"));
        cell.setRowspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Name"));
        cell.setColspan(3);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Age"));
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell("SURNAME");
        table.addCell("FIRST NAME");
        table.addCell("MIDDLE NAME");
        table.addCell("1");
        table.addCell("James");
        table.addCell("Fish");
        table.addCell("Stone");
        table.addCell("17");
        document.add(table);
        document.close();
    }
}