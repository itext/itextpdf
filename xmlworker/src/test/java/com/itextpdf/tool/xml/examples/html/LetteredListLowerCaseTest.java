package com.itextpdf.tool.xml.examples.html;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.examples.SampleTest;

import java.io.FileOutputStream;

public class LetteredListLowerCaseTest extends SampleTest {
    protected String getTestName() {
        return  "letteredListLowerCase";
    }

    @Override
    protected void makePdf(String outPdf) throws Exception {
        Document doc = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(outPdf));
        doc.open();
        transformHtml2Pdf(doc, pdfWriter, new SampleTestImageProvider(), new XMLWorkerFontProvider(SampleTest.class.getResource("fonts").getPath()),
                LetteredListLowerCaseTest.class.getResourceAsStream("letteredListLowerCase/sampleTest.css"));
        doc.close();
    }
}
