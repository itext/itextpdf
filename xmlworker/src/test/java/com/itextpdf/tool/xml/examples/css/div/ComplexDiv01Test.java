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
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

public class ComplexDiv01Test extends SampleTest {
    protected String getTestName() {
        return "complexDiv01";
    }

    @Override
    protected void makePdf(String outPdf) throws Exception {
        Document doc = new Document(PageSize.A3.rotate());

        PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(outPdf));
        pdfWriter.createXmpMetadata();

        doc.setMargins(200, 200, 0, 0);
        doc.open();

        CssFilesImpl cssFiles = new CssFilesImpl();
        cssFiles.add(XMLWorkerHelper.getCSS(new FileInputStream(String.format("./src/test/resources/%s/%s/complexDiv_files/main.css", testPath, getTestName()))));
        cssFiles.add(XMLWorkerHelper.getCSS(new FileInputStream(String.format("./src/test/resources/%s/%s/complexDiv_files/widget082.css", testPath, getTestName()))));
        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
        HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider(SampleTest.class.getResource("fonts").getPath())));
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
        hpc.setImageProvider(new SampleTestImageProvider());
        hpc.setPageSize(doc.getPageSize());
        HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(doc, pdfWriter));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(true, worker, Charset.forName("UTF-8"));
        p.parse(new FileInputStream(inputHtml), Charset.forName("UTF-8"));
        //ICC_Profile icc = ICC_Profile.getInstance(ComplexDiv01Test.class.getResourceAsStream("sRGB Color Space Profile.icm"));
        //pdfWriter.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        doc.close();
    }
}
