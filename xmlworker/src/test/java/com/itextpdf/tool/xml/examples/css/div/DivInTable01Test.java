package com.itextpdf.tool.xml.examples.css.div;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.examples.SampleTest;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DivInTable01Test extends SampleTest {
    protected String getTestName() {
        return  "divInTable01";
    }

    @Override
    protected void makePdf(String outPdf) throws Exception {
        XMLWorkerHelper xmlHelper = XMLWorkerHelper.getInstance();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outPdf));
        doc.open();

        xmlHelper.parseXHtml(writer, doc, new FileInputStream(inputHtml));

        doc.close();
    }
}
