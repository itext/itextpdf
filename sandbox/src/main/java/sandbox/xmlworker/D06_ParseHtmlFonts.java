package sandbox.xmlworker;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

@WrapToTest
public class D06_ParseHtmlFonts {

    public static final String HTML = "resources/xml/walden.html";
    public static final String CSS = "resources/xml/walden.css";
    public static final String DEST = "results/xmlworker/walden4.pdf";

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
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream(CSS));
        cssResolver.addCss(cssFile);
        
        // HTML
        XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontProvider.register("resources/fonts/Cardo-Regular.ttf");
        fontProvider.register("resources/fonts/Cardo-Bold.ttf");
        fontProvider.register("resources/fonts/Cardo-Italic.ttf");
        fontProvider.addFontSubstitute("lowagie", "cardo");
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        
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
        new D06_ParseHtmlFonts().createPdf(DEST);
    }
}
