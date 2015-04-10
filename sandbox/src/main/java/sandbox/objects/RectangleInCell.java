/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21625435/c-sharp-add-rectangle-into-pdfpcell-itextsharp
 * 
 * Adding a Rectangle object inside a table cell.
 */
package sandbox.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import sandbox.WrapToTest;

@WrapToTest
public class RectangleInCell {
    public static final String DEST = "results/objects/rectangle_in_cell.pdf";
    
    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RectangleInCell().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        document.add(new Paragraph("Option 1:"));
        PdfPTable table = new PdfPTable(3);
        table.addCell("A rectangle:");
        PdfTemplate template = writer.getDirectContent().createTemplate(120, 80);
        template.setColorFill(BaseColor.RED);
        template.rectangle(0, 0, 120, 80);
        template.fill();
        writer.releaseTemplate(template);
        table.addCell(Image.getInstance(template));
        table.addCell("The rectangle is scaled to fit inside the cell, you see a padding.");
        document.add(table);
        document.add(new Paragraph("Option 2:"));
        table = new PdfPTable(3);
        table.addCell("A rectangle:");
        PdfPCell cell = new PdfPCell(Image.getInstance(template));
        table.addCell(cell);
        table.addCell("The rectangle keeps its original size, but can overlap other cells in the same row.");
        document.add(table);
        document.add(new Paragraph("Option 3:"));
        table = new PdfPTable(3);
        table.addCell("A rectangle:");
        cell = new PdfPCell(Image.getInstance(template), true);
        table.addCell(cell);
        table.addCell("The rectangle is scaled to fit inside the cell, no padding.");
        document.add(table);
        PdfContentByte cb = writer.getDirectContent();
        cb.moveTo(228, 810);
        cb.lineTo(338, 810);
        cb.stroke();
        document.close();
    }
}
