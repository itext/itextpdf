package sandbox.xmlworker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Div;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.FileRetrieveImpl;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class D10_ParseCustomTag {

    public static final String HTML = "resources/xml/test.html";
    public static final String DEST = "results/xmlworker/date.pdf";
    public static final String CSS_DIR = "resources/xml/";
    
    /**
     * Creates a PDF with the words "Hello World"
     * @param file
     * @throws IOException
     * @throws DocumentException
     */
    public void createPdf(String file) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        writer.setInitialLeading(12.5f);
        // step 3
        document.open();
        // step 4
        
        // CSS
        CSSResolver cssResolver =
                XMLWorkerHelper.getInstance().getDefaultCssResolver(false);
        FileRetrieve retrieve = new FileRetrieveImpl(CSS_DIR);
        cssResolver.setFileRetrieve(retrieve);
        
        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        TagProcessorFactory factory = Tags.getHtmlTagProcessorFactory();
        factory.addProcessor(
            new Div(){
                public List<Element> end(WorkerContext ctx, Tag tag, List<Element> l) {
                    List<Element> list = new ArrayList<Element>(1);
                    String date = DateFormat.getDateInstance(DateFormat.LONG, Locale.US).format(new Date());
                    list.add(new Paragraph(date));
                    return list;
                }
            },
            "date");
        htmlContext.setTagFactory(factory);
        htmlContext.autoBookmark(false);
        
        // Pipelines
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
        
        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new FileInputStream(HTML));
        
        // step 5
        document.close();
    }
    
    /**
     * Main method
     */
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new D10_ParseCustomTag().createPdf(DEST);
    }
}