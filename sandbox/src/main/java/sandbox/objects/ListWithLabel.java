/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21579204/how-to-set-label-to-itext-list
 * 
 * Workaround to add a label to a list.
 */
package sandbox.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class ListWithLabel {

    public static final String DEST = "results/objects/list_with_label.pdf";
    
    public static void main(String[] args) throws IOException,
            DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListWithLabel().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(200);
        table.setWidths(new int[]{ 1, 10 });
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        PdfPCell cell;
        cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.addElement(new Paragraph("Label"));
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        List list = new List(List.UNORDERED);
        list.add(new ListItem(new Chunk("Value 1")));
        list.add(new ListItem(new Chunk("Value 2")));
        list.add(new ListItem(new Chunk("Value 3")));
        cell.addElement(list);
        table.addCell(cell);
        document.add(table);
        document.close();
    }
}
