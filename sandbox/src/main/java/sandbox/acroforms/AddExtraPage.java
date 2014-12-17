/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/26853894/continue-field-output-on-second-page-with-itextsharp
 */
package sandbox.acroforms;

import com.itextpdf.text.Chunk;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class AddExtraPage {

    public static final String SRC = "resources/pdfs/stationery.pdf";
    public static final String DEST = "results/acroforms/more_than_one_page.pdf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddExtraPage().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        Rectangle pagesize = reader.getPageSize(1);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        Paragraph p = new Paragraph();
        p.add(new Chunk("Hello "));
        p.add(new Chunk("World", new Font(FontFamily.HELVETICA, 12, Font.BOLD)));
        AcroFields form = stamper.getAcroFields();
        Rectangle rect = form.getFieldPositions("body").get(0).position;
        int status;
        PdfImportedPage newPage = null;
        ColumnText column = new ColumnText(stamper.getOverContent(1));
        column.setSimpleColumn(rect);
        int pagecount = 1;
        for (int i = 0; i < 100; ) {
            i++;
            column.addElement(new Paragraph("Hello " + i));
            column.addElement(p);
            status = column.go();
            if (ColumnText.hasMoreText(status)) {
                newPage = loadPage(newPage, reader, stamper);
                triggerNewPage(stamper, pagesize, newPage, column, rect, ++pagecount);
            }
        }
        stamper.setFormFlattening(true);
        stamper.close();
        reader.close();
    }
   
    public PdfImportedPage loadPage(PdfImportedPage page, PdfReader reader, PdfStamper stamper) {
        if (page == null) {
            return stamper.getImportedPage(reader, 1);
        }
        return page;
    }
    
    public void triggerNewPage(PdfStamper stamper, Rectangle pagesize, PdfImportedPage page, ColumnText column, Rectangle rect, int pagecount) throws DocumentException {
        stamper.insertPage(pagecount, pagesize);
        PdfContentByte canvas = stamper.getOverContent(pagecount);
        canvas.addTemplate(page, 0, 0);
        column.setCanvas(canvas);
        column.setSimpleColumn(rect);
        column.go();
    }
}