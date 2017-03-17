/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
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
