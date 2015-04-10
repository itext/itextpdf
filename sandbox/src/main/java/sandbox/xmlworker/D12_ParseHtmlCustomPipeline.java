package sandbox.xmlworker;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.*;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
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
public class D12_ParseHtmlCustomPipeline {

    public static final String HTML = "resources/xml/walden.html";
    public static final String DEST = "results/xmlworker/walden6.pdf";

    class CustomPipeline extends AbstractPipeline<CustomContext> {

        private int indent = -1;
        
        /* (non-Javadoc)
         * @see com.itextpdf.tool.xml.pipeline.AbstractPipeline#open(com.itextpdf.tool.xml.WorkerContext, com.itextpdf.tool.xml.Tag, com.itextpdf.tool.xml.ProcessObject)
         */
        @Override
        public Pipeline<?> open(WorkerContext context, Tag t, ProcessObject po)
                throws PipelineException {
            indent++;
            for (int i = 0; i < indent; i++)
                System.out.print("\t");
            System.out.println("<" + t.getName() + ">");
            return super.open(context, t, po);
        }

        /* (non-Javadoc)
         * @see com.itextpdf.tool.xml.pipeline.AbstractPipeline#close(com.itextpdf.tool.xml.WorkerContext, com.itextpdf.tool.xml.Tag, com.itextpdf.tool.xml.ProcessObject)
         */
        @Override
        public Pipeline<?> close(WorkerContext context, Tag t, ProcessObject po)
                throws PipelineException {
            for (int i = 0; i < indent; i++)
                System.out.print("\t");
            System.out.println("</" + t.getName() + ">");
            indent--;
            return super.close(context, t, po);
        }

        public CustomPipeline(Pipeline<?> next) {
            super(next);
        }
        
    }
    
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
                XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        
        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.autoBookmark(false);
        
        // Pipelines
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        CustomPipeline custom = new CustomPipeline(pdf);
        HtmlPipeline html = new HtmlPipeline(htmlContext, custom);
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
        new D12_ParseHtmlCustomPipeline().createPdf(DEST);
    }
}
