package com.itextpdf.tool.xml.examples.css.div;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.examples.SampleTest;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DivInTable02Test extends SampleTest {
    protected String getTestName() {
        return  "divInTable02";
    }

    @Override
    protected void makePdf(String outPdf) throws Exception {
        XMLWorkerHelper xmlHelper = XMLWorkerHelper.getInstance();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outPdf));
        doc.open();

        xmlHelper.parseXHtml(writer, doc, new FileInputStream(inputHtml),
                null, null, new XMLWorkerFontProvider(), String.format("./src/test/resources/%s/%s/", testPath, getTestName()));

        doc.close();
    }
}
