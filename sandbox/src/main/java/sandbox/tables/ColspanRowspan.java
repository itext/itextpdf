/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/23989852/itext-combining-rowspan-and-colspan-pdfptable
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ColspanRowspan {

    public static final String DEST = "results/tables/colspan_rowspan.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColspanRowspan().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(4);
        PdfPCell cell = new PdfPCell(new Phrase(" 1,1 "));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(" 1,2 "));
        table.addCell(cell);
        PdfPCell cell23 = new PdfPCell(new Phrase("multi 1,3 and 1,4"));
        cell23.setColspan(2);
        cell23.setRowspan(2);
        table.addCell(cell23);
        cell = new PdfPCell(new Phrase(" 2,1 "));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(" 2,2 "));
        table.addCell(cell);
        document.add(table);
        document.close();
    }
}
