package com.itextpdf.tool.xml.examples.css.div;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.examples.SampleTest;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AvoidDivPaginationTest extends SampleTest{
    protected String getTestName() {
        return "avoidDivPagination";
    }

    @Override
    protected void makePdf(String outPdf) throws Exception {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPdf));
        document.open();

        FileInputStream htmlInputStream = new FileInputStream(inputHtml);

        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);

        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext,  new PdfWriterPipeline(document, writer)));

        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(worker);
        p.parse(htmlInputStream);

        document.close();
    }
}