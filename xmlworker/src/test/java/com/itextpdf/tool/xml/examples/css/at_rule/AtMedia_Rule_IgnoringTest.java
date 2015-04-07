package com.itextpdf.tool.xml.examples.css.at_rule;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.examples.SampleTest;

import java.io.FileOutputStream;

public class AtMedia_Rule_IgnoringTest extends SampleTest {
    protected String getTestName() {
        return "atmedia_rule_ignoring";
    }

    @Override
    protected void makePdf(String outPdf) throws Exception {
        Document doc = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(outPdf));
        doc.open();
        transformHtml2Pdf(doc, pdfWriter, new SampleTestImageProvider(), new XMLWorkerFontProvider(SampleTest.class.getResource("fonts").getPath()), AtMedia_Rule_IgnoringTest.class.getResourceAsStream("atmedia_rule_ignoring/media-print.css"));
        doc.close();
    }
}
