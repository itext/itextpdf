package com.itextpdf.tool.xml.examples;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.CompareTool;
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
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

public class SampleTest {

    protected String outPath;
    protected String outPdf;
    protected String inputHtml;
    private String cmpPdf;
    private String differenceImagePrefix;
    private CompareTool compareTool;
    protected String testPath;
    protected String testName;

    @Before
    public void setup() throws IOException {
        testPath = this.getClass().getName();
        testPath = testPath.replace(".", File.separator);
        testPath = testPath.substring(0, testPath.lastIndexOf(File.separator) + 1);
        testName = getTestName();
        if (testName.length() > 0) {
            if (testName.contains(File.separator)) {
                testName = testName.substring(testName.lastIndexOf(File.separator) + 1, testName.length());
            }
            outPath = "." + File.separator + "target" + File.separator + testPath + testName + File.separator;
            String inputPath = "." + File.separator + "target" + File.separator + "test-classes" + File.separator + testPath + File.separator + testName + File.separator;
            differenceImagePrefix = "difference";
            outPdf = outPath + testName + ".pdf";
            inputHtml = inputPath + "<testName>.html".replaceAll("<testName>", testName);
            cmpPdf = inputPath + "<testName>.pdf".replaceAll("<testName>", testName);
            compareTool = new CompareTool(outPdf, cmpPdf);
            File dir = new File(outPath);
            if (dir.exists()) {
                deleteDirectory(dir);
            }
            dir.mkdirs();
        }
    }

    @Test(timeout = 120000)
    public void test() throws IOException, DocumentException, InterruptedException {
        String testName = getTestName();
        if (!this.getClass().getName().equals(SampleTest.class.getName()) && (testName.length() > 0)) {
            transformHtml2Pdf();
            if (detectCrashesAndHangUpsOnly() == false) {
                String errorMessage = compareTool.compare(outPath, differenceImagePrefix);
                if (errorMessage != null) {
                    Assert.fail(errorMessage);
                }
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
            imageRootPath = "." + File.separator + "target" + File.separator + "test-classes" + File.separator + testPath + testName + File.separator;
        }

        public String getImageRootPath() {
            return imageRootPath;
        }
    }

    protected void transformHtml2Pdf() throws IOException, DocumentException, InterruptedException {
        Document doc = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(outPdf));
        doc.open();
        transformHtml2Pdf(doc, pdfWriter, new SampleTestImageProvider(), new XMLWorkerFontProvider(SampleTest.class.getResource("fonts").getPath()), SampleTest.class.getResourceAsStream("sampleTest.css"));
        doc.close();
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

    private void deleteDirectory(File path) {
        if (path == null)
            return;
        if (path.exists()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                    f.delete();
                } else {
                    f.delete();
                }
            }
            path.delete();
        }
    }
}
