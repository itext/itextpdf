/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19703715/centered-text-in-itext-pdf-table-cell
 * 
 * We create a table with a single column and a single cell.
 * We add some content that needs to be centered vertically.
 */
package sandbox.tables;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CenteredTextInCell {

    public static void main(String[] args) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("results/centered_text.pdf"));
        document.open();
        Font font = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
        Paragraph para = new Paragraph("Test", font);
        para.setLeading(0, 1);
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setMinimumHeight(50);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.addElement(para);
        table.addCell(cell);
        document.add(table);
        document.close();
    }
}
