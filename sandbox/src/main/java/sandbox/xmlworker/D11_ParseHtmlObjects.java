package sandbox.xmlworker;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

@WrapToTest
public class D11_ParseHtmlObjects {

    public static final String HTML = "resources/xml/walden.html";
    public static final String DEST = "results/xmlworker/walden5.pdf";

    public void createPdf(String file) throws IOException, DocumentException {
        
        // Parse HTML into Element list
        
        // CSS
        CSSResolver cssResolver =
                XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        
        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.autoBookmark(false);
        
        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline html = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
        
        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new FileInputStream(HTML));
        
        // step 1
        Document document = new Document(PageSize.LEGAL.rotate());
        
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        writer.setInitialLeading(12.5f);
        
        // step 3
        document.open();
        
        // step 4
        Rectangle left = new Rectangle(36, 36, 486, 586);
        Rectangle right = new Rectangle(522, 36, 972, 586);
        ColumnText column = new ColumnText(writer.getDirectContent());
        column.setSimpleColumn(left);
        boolean leftside = true;
        int status = ColumnText.START_COLUMN;
        for (Element e : elements) {
            if (ColumnText.isAllowedElement(e)) {
                column.addElement(e);
                status = column.go();
                while (ColumnText.hasMoreText(status)) {
                    if (leftside) {
                        leftside = false;
                        column.setSimpleColumn(right);
                    }
                    else {
                        document.newPage();
                        leftside = true;
                        column.setSimpleColumn(left);
                    }
                    status = column.go();
                }
            }
        }
        
        // step 5
        document.close();
    }
    
    /**
     * Main method
     */
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new D11_ParseHtmlObjects().createPdf(DEST);
    }
}