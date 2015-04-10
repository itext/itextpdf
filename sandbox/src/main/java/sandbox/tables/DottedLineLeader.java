/**
 * Example written by Bruno Lowagie in answer to the following question:
 *http://stackoverflow.com/questions/27046352/how-to-generate-table-of-figures-dot-leaders-in-a-pdfpcell-for-the-last-line-of
 */
package sandbox.tables;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DottedLineLeader {
    
    public static final String DEST = "results/tables/dotted_line_leader.pdf";
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DottedLineLeader().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(50);
        table.setWidths(new int[]{1, 3, 1});
        Chunk leader = new Chunk(new DottedLineSeparator());
        Paragraph p;
        table.addCell(getCell(new Paragraph("fig 1"), Element.ALIGN_TOP));
        p = new Paragraph("Title text");
        p.add(leader);
        table.addCell(getCell(p, Element.ALIGN_TOP));
        table.addCell(getCell(new Paragraph("2"), Element.ALIGN_BOTTOM));
        table.addCell(getCell(new Paragraph("fig 2"), Element.ALIGN_TOP));
        p = new Paragraph("This is a longer title text that wraps");
        p.add(leader);
        table.addCell(getCell(p, Element.ALIGN_TOP));
        table.addCell(getCell(new Paragraph("55"), Element.ALIGN_BOTTOM));
        table.addCell(getCell(new Paragraph("fig 3"), Element.ALIGN_TOP));
        p = new Paragraph("Another title text");
        p.add(leader);
        table.addCell(getCell(p, Element.ALIGN_TOP));
        table.addCell(getCell(new Paragraph("89"), Element.ALIGN_BOTTOM));
        document.add(table);
        document.close();
    }
    
    public PdfPCell getCell(Paragraph p, int verticalAlignment) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(verticalAlignment);
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        cell.addElement(p);
        return cell;
    }
}
