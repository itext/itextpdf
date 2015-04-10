/**
 * This example was written by Bruno Lowagie for a prospective customer.
 * The code in this sample works with the latest version of iText.
 * It doesn't work with versions predating iText 5.
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import sandbox.WrapToTest;

@WrapToTest
public class ListInCell {

    public static final String DEST = "results/tables/list_in_cell.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListInCell().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        
        // This is how not to do it (but it works anyway):
        
        // We create a list:
        List list = new List();                
        list.add(new ListItem("Item 1"));
        list.add(new ListItem("Item 2"));
        list.add(new ListItem("Item 3"));
        
        // We wrap this list in a phrase:     
        Phrase phrase = new Phrase();
        phrase.add(list);
        // We add this phrase to a cell
        PdfPCell phraseCell = new PdfPCell();
        phraseCell.addElement(phrase);           
        
        // We add the cell to a table:
        PdfPTable phraseTable = new PdfPTable(2);
        phraseTable.setSpacingBefore(5);
        phraseTable.addCell("List wrapped in a phrase:");
        phraseTable.addCell(phraseCell);

        // We wrap the phrase table in another table:
        Phrase phraseTableWrapper = new Phrase();
        phraseTableWrapper.add(phraseTable);
        
        // We add these nested tables to the document:
        document.add(new Paragraph("A list, wrapped in a phrase, wrapped in a cell, wrapped in a table, wrapped in a phrase:"));
        document.add(phraseTableWrapper);
        
        // This is how to do it:
        
        // We add the list directly to a cell:
        PdfPCell cell = new PdfPCell();
        cell.addElement(list);
        
        // We add the cell to the table:
        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(5);
        table.addCell("List placed directly into cell");
        table.addCell(cell);
        
        // We add the table to the document:
        document.add(new Paragraph("A list, wrapped in a cell, wrapped in a table:"));
        document.add(table);
        
        // Avoid adding tables to phrase (but it works anyway):
        
        Phrase tableWrapper = new Phrase();
        tableWrapper.add(table);document.add(new Paragraph("A list, wrapped in a cell, wrapped in a table, wrapped in a phrase:"));
        document.add(tableWrapper);
        
        document.close();
    }
}
