package com.itextpdf.tool.xml.examples;

import com.itextpdf.testutils.ITextTest;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.PageSize;
import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

public class SampleTest extends ITextTest {

    protected String inputPath;
    protected String testPath;
    protected String outPath;

    protected String inputHtml;

    private final String differenceImagePrefix = "difference";


    public void setup() throws IOException {
        testPath = this.getClass().getPackage().getName().replace(".", "/");

        outPath = String.format("./target/%s/%s/", testPath, getTestName());
        inputPath = String.format("./src/test/resources/%s/%s/", testPath, getTestName());
        inputHtml = String.format("%s%s.html", inputPath, getTestName());

        File dir = new File(outPath);
        if (dir.exists()) deleteFiles(dir);
        else dir.mkdirs();
    }

    @Test(timeout = 120000)
    public void test() throws Exception {
        if (!this.getClass().getName().equals(SampleTest.class.getName())
                && (getTestName().length() > 0)) {
            setup();
            super.runTest();
        }
    }

    @Override
    protected void makePdf(String outPdf) throws Exception {
        Document doc = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(outPdf));
        doc.open();
        transformHtml2Pdf(doc, pdfWriter, new SampleTestImageProvider(), new XMLWorkerFontProvider(SampleTest.class.getResource("fonts").getPath()), SampleTest.class.getResourceAsStream("sampleTest.css"));
        doc.close();
    }

    @Override
    protected String getOutPdf() {
        return String.format("%s%s.pdf", outPath, getTestName());
    }

    @Override
    protected String getCmpPdf() {
        return String.format("%s%s.pdf", inputPath, getTestName());
    }

    @Override
    protected void comparePdf(String outPdf, String cmpPdf) throws Exception {
        if (!detectCrashesAndHangUpsOnly()) {
            CompareTool compareTool = new CompareTool();
            String errorMessage = compareTool.compareByContent(outPdf, cmpPdf, outPath, differenceImagePrefix);
            if (errorMessage != null) {
                Assert.fail(errorMessage);
            }
        }
    }

    protected String getTestName() {
        return "";
    }

    protected boolean detectCrashesAndHangUpsOnly() {
        return false;
    }

    protected class SampleTestImageProvider extends AbstractImageProvider {
        final String imageRootPath;

        public SampleTestImageProvider() {
            imageRootPath = String.format("./target/test-classes/%s/%s/", testPath, getTestName());
        }

        public String getImageRootPath() {
            return imageRootPath;
        }
    }

    protected void transformHtml2Pdf(Document doc, PdfWriter pdfWriter, ImageProvider imageProvider, FontProvider fontProvider, InputStream cssFile) throws IOException, DocumentException, InterruptedException {
        CssFilesImpl cssFiles = new CssFilesImpl();
        if (cssFile == null)
            cssFile = SampleTest.class.getResourceAsStream("/default.css");
        cssFiles.add(XMLWorkerHelper.getCSS(cssFile));
        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
        HtmlPipelineContext hpc;

        if (fontProvider != null)
            hpc = new HtmlPipelineContext(new CssAppliersImpl(fontProvider));
        else
            hpc = new HtmlPipelineContext(null);

        hpc.setImageProvider(imageProvider);
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
        HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(doc, pdfWriter));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser xmlParse = new XMLParser(true, worker, Charset.forName("UTF-8"));
        xmlParse.parse(new FileInputStream(inputHtml), Charset.forName("UTF-8"));
    }
}
