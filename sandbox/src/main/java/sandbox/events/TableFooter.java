/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22122340/creating-table-with-2-rows-in-pdf-footer-using-itext
 */
package sandbox.events;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class TableFooter {
    public static final String DEST = "results/events/table_footer.pdf";
    
    public class FooterTable extends PdfPageEventHelper {
        protected PdfPTable footer;
        public FooterTable(PdfPTable footer) {
            this.footer = footer;
        }
        public void onEndPage(PdfWriter writer, Document document) {
            footer.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
        }
    }
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableFooter().createPdf(DEST);
    }
    
    public void createPdf(String filename) throws IOException, DocumentException {
        // step 1
        Document document = new Document(PageSize.A4, 36, 36, 36, 72);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(523);
        PdfPCell cell = new PdfPCell(new Phrase("This is a test document"));
        cell.setBackgroundColor(BaseColor.ORANGE);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("This is a copyright notice"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        FooterTable event = new FooterTable(table);
        writer.setPageEvent(event);
        // step 3
        document.open();
        // step 4
        for (int i = 0; i < 50; i++)
            document.add(new Paragraph("Hello World!"));
        document.newPage();
        document.add(new Paragraph("Hello World!"));
        document.newPage();
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();
    }
}
