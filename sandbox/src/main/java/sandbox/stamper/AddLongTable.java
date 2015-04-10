/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21838846/inserting-a-table-into-multiple-pdf-pages-with-columntext
 * 
 * The person asking the question forgot to set the canvas for the ColumnText object.
 */
package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@WrapToTest
public class AddLongTable {


    public static final String SRC = "resources/pdfs/hello.pdf";
    public static final String DEST = "results/stamper/hello_table.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddLongTable().manipulatePdf(SRC, DEST);
    }


    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfPTable table = new PdfPTable(2);
        for (int i = 0; i < 250; ) {
            table.addCell("Row " + (++i));
            table.addCell("Test");
        }
        PdfReader reader = new PdfReader(src);
        int pageNum = 1;
        Rectangle page = reader.getPageSize(pageNum);
        Rectangle rect = new Rectangle(36, 36, page.getRight(36), page.getTop(72));
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        ColumnText ct = new ColumnText(stamper.getOverContent(pageNum));
        ct.addElement(table);
        while(true) {
            ct.setSimpleColumn(rect);
            if (!ColumnText.hasMoreText(ct.go()))
                break;
            stamper.insertPage(++pageNum, page);
            ct.setCanvas(stamper.getOverContent(pageNum));
        }
        stamper.close();
        reader.close();
    }
}
