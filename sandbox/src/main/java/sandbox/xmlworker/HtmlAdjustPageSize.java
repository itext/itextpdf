/*
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27186661/adjust-page-height-to-content-height
 */
package sandbox.xmlworker;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HtmlAdjustPageSize {
    
    public static final String HTML = "<table>" +
        "<tr><td class=\"ra\">TIMESTAMP</td><td><b>2014-11-28 11:06:09</b></td></tr>" +
        "<tr><td class=\"ra\">ERROR ID</td><td><b>ERROR-01</b></td></tr>" +
        "<tr><td class=\"ra\">SYSTEM ID</td><td><b>SYSTEM-01</b></td></tr>" +
        "<tr><td class=\"ra\">DESCRIPTION</td><td><b>TEST WITH A VERY, VERY LONG DESCRIPTION LINE THAT NEEDS MULTIPLE LINES</b></td></tr>" +
        "</table>";
    public static final String CSS = "table {width: 200pt; } .ra { text-align: right; }";
    public static final String DEST = "results/xmlworker/html_page_size.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new HtmlAdjustPageSize().createPdf(DEST);
    }

    public void createPdf(String file) throws IOException, DocumentException {
        ElementList el = parseHtml(HTML, CSS);
        float width = 200;
        float max = 10000;
        ColumnText ct = new ColumnText(null);
        ct.setSimpleColumn(new Rectangle(width, max));
        for (Element e : el) {
            ct.addElement(e);
        }
        ct.go(true);
        float y = ct.getYLine();
        Rectangle pagesize = new Rectangle(width, max - y);
        // step 1
        Document document = new Document(pagesize, 0, 0, 0, 0);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        // step 3
        document.open();
        // step 4
        ct = new ColumnText(writer.getDirectContent());
        ct.setSimpleColumn(pagesize);
        for (Element e : el) {
            ct.addElement(e);
        }
        ct.go();
        // step 5
        document.close();
    }
        
    public ElementList parseHtml(String html, String css) throws IOException {
        // CSS
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(css.getBytes()));
        cssResolver.addCss(cssFile);
        
        // HTML
        CssAppliers cssAppliers = new CssAppliersImpl(FontFactory.getFontImp());
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.autoBookmark(false);
        
        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        
        // XML Worker
        XMLWorker worker = new XMLWorker(cssPipeline, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new ByteArrayInputStream(html.getBytes()));
        
        return elements;
    }
}
