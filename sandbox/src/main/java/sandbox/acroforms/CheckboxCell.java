/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21342034/resizing-a-form-field-in-itextsharp
 * 
 * Given a cell, how do you add a check box with a specific size at a specific position.
 */
package sandbox.acroforms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;
import sandbox.WrapToTest;

@WrapToTest
public class CheckboxCell {
    
    public static final String DEST = "results/acroforms/checkbox_in_cell.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CheckboxCell().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        // We create a table with 5 columns
        PdfPTable table = new PdfPTable(5);
        PdfPCell cell;
        // We add 5 cells
        for (int i = 0; i < 5; i++) {
            cell = new PdfPCell();
            cell.setCellEvent(new CheckboxCellEvent("cb" + i));
            // We create cell with height 50
            cell.setMinimumHeight(50);
            table.addCell(cell);
        }
        document.add(table);
        document.close();
    }
    
    class CheckboxCellEvent implements PdfPCellEvent {
        // The name of the check box field
        protected String name;
        // We create a cell event
        public CheckboxCellEvent(String name) {
            this.name = name;
        }
        // We create and add the check box field
        public void cellLayout(PdfPCell cell, Rectangle position,
            PdfContentByte[] canvases) {
            PdfWriter writer = canvases[0].getPdfWriter(); 
            // define the coordinates of the middle
            float x = (position.getLeft() + position.getRight()) / 2;
            float y = (position.getTop() + position.getBottom()) / 2;
            // define the position of a check box that measures 20 by 20
            Rectangle rect = new Rectangle(x - 10, y - 10, x + 10, y + 10);
            // define the check box
            RadioCheckField checkbox = new RadioCheckField(
                    writer, rect, name, "Yes");
            // add the check box as a field
            try {
                writer.addAnnotation(checkbox.getCheckField());
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }
    }
}
