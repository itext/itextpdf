package sandbox.xmlworker.reporting;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.html.Tags;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FillTemplate3 {

    public static final String DEST = "results/xmlworker/report3.pdf";
    public static final String PDF = "resources/pdfs/stationery_landscape.pdf";
    public static final String HTML = "resources/xml/movies.html";
    public static final String CSS = "resources/xml/style3.css";
    
    public void createPdf(String result) throws IOException, DocumentException {
        FillTemplateHelper template = new FillTemplateHelper(PDF);
        template.setSender("Bruno Lowagie\nAdolf Baeyensstraat 121\n9040 Sint-Amandsberg\nBELGIUM");
        template.setReceiver("iText Software Corp.\nCambridge Innovation Center\n1 Broadway, 14th Floor\nCambridge, MA 02142 USA");
        // step 1
        Document document = new Document(template.getPageSize());
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(result));
        writer.setPageEvent(template);
        // step 3
        document.open();
        // step 4
        ColumnText ct = new ColumnText(writer.getDirectContent());
        ct.setSimpleColumn(template.getBody());
	ElementList elements = FillTemplateHelper.parseHtml(HTML, CSS, Tags.getHtmlTagProcessorFactory());
        int status;
        float yLine;
        int rowsDrawn;
        for (Element e : elements) {
            if (!ColumnText.isAllowedElement(e))
                continue;
            if (e instanceof PdfPTable)
                ((PdfPTable)e).setHeaderRows(1);
            yLine = ct.getYLine();
            ct.addElement(e);
            status = ct.go(true);
            // the content fits:
            if (!ColumnText.hasMoreText(status)) {
                 // return to the original position
                ct.setYLine(yLine);
                // add the element FOR REAL
                ct.addElement(e);
                ct.go();
            }
            else {
                rowsDrawn = ct.getRowsDrawn();
                ct.setText(null);
                ct.addElement(e);
                ct.setSimpleColumn(template.getBody());
                status = ct.go(true);
                if (ColumnText.hasMoreText(status) && rowsDrawn > 2) {
                    ct.setYLine(yLine);
                    ct.setText(null);
                    ct.addElement(e);
                    ct.go();
                }
                else {
                    ct.setText(null);
                    ct.addElement(e);
                }
                document.newPage();
                ct.setSimpleColumn(template.getBody());
                status = ct.go();
                while (ColumnText.hasMoreText(status)) {
                    document.newPage();
                    ct.setSimpleColumn(template.getBody());
                    status = ct.go();
                }
            }
        }
        // step 5
        document.close();
    }
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillTemplate3().createPdf(DEST);
    }
}
