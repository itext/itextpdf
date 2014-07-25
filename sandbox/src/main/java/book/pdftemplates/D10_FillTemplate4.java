package book.pdftemplates;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.html.DefaultTagProcessorFactory;
import com.itextpdf.tool.xml.html.Div;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.table.Table;
import com.itextpdf.tool.xml.html.table.TableData;
import com.itextpdf.tool.xml.html.table.TableRow;
import com.itextpdf.tool.xml.html.table.TableRowElement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class D10_FillTemplate4 {
    
    public static final String DEST = "results/pdftemplates/report4.pdf";
    public static final String PDF = "resources/pdfs/stationery.pdf";
    public static final String XML = "resources/xml/movies.xml";
    public static final String CSS = "resources/xml/style4.css";
    
    
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
	ElementList elements = FillTemplateHelper.parseHtml(XML, CSS, getTagProcessorFactory());
        int status;
        float yLine;
        for (Element e : elements) {
            if (!ColumnText.isAllowedElement(e))
                continue;
            yLine = ct.getYLine();
            ct.addElement(e);
            status = ct.go(true);
            if (ColumnText.hasMoreText(status) && yLine < 140) {
                document.newPage();
                ct.setSimpleColumn(template.getBody());
            }
            else {
                ct.setYLine(yLine);
            }
            ct.setText(null);
            ct.addElement(e);
            status = ct.go();
            while (ColumnText.hasMoreText(status)) {
                document.newPage();
                ct.setSimpleColumn(template.getBody());
                status = ct.go();
            }
        }
        // step 5
        document.close();
    }
    
    protected TagProcessorFactory getTagProcessorFactory() {
        TagProcessorFactory factory = new DefaultTagProcessorFactory();
        factory.addProcessor(new Div(), "description", "moviecountry");
        factory.addProcessor(new Div(), "title", "country");
        factory.addProcessor(new Table(), "movies");
        factory.addProcessor(new TableRow(), "movie");
        factory.addProcessor(new TableRow() {
	    public List<Element> end(WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
    	        List<Element> l = new ArrayList<Element>(1);
                l.add(new TableRowElement(currentContent, TableRowElement.Place.HEADER));
                return l;
            }
        }, "movieheader");
        factory.addProcessor(new TableData(), "column1", "columnheader1", "column2", "columnheader2", "column3", "columnheader3");
        return factory;
    }
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new D10_FillTemplate4().createPdf(DEST);
    }
}
