package com.itextpdf.testutils;

import com.itextpdf.text.DocumentException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class CompareToolTest {

    private static final String OUT_PATH = "./target/com/itextpdf/testutils/CompareToolTest/";
    private static final String RESOURCE_PATH = "./src/test/resources/com/itextpdf/testutils/CompareToolTest/";

    @Before
    public void setUp() {
        File dest = new File(OUT_PATH);
        dest.mkdirs();
        File[] files = dest.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Test
    public void compareToolErrorReportTest01() throws DocumentException, InterruptedException, IOException, ParserConfigurationException, SAXException {
        CompareTool compareTool = new CompareTool();
        compareTool.setCompareByContentErrorsLimit(10);
        compareTool.setGenerateCompareByContentXmlReport(true);
        String outPdf = RESOURCE_PATH + "simple_pdf.pdf";
        String cmpPdf = RESOURCE_PATH + "cmp_simple_pdf.pdf";
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull("CompareTool must return differences found between the files", result);
        // Comparing the report to the reference one.
        Assert.assertTrue("CompareTool report differs from the reference one", compareTool.compareXmls(RESOURCE_PATH + "cmp_report01.xml", OUT_PATH + "report.xml"));
    }

    @Test
    public void compareToolErrorReportTest02() throws IOException, InterruptedException, DocumentException, ParserConfigurationException, SAXException {
        CompareTool compareTool = new CompareTool();
        compareTool.setCompareByContentErrorsLimit(10);
        compareTool.setGenerateCompareByContentXmlReport(true);
        String outPdf = RESOURCE_PATH + "tagged_pdf.pdf";
        String cmpPdf = RESOURCE_PATH + "cmp_tagged_pdf.pdf";
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull("CompareTool must return differences found between the files", result);
        // Comparing the report to the reference one.
        Assert.assertTrue("CompareTool report differs from the reference one", compareTool.compareXmls(RESOURCE_PATH + "cmp_report02.xml", OUT_PATH + "report.xml"));
    }

    @Test
    public void compareToolErrorReportTest03() throws DocumentException, InterruptedException, IOException, ParserConfigurationException, SAXException {
        CompareTool compareTool = new CompareTool();
        compareTool.setCompareByContentErrorsLimit(10);
        compareTool.setGenerateCompareByContentXmlReport(true);
        String outPdf = RESOURCE_PATH + "screenAnnotation.pdf";
        String cmpPdf = RESOURCE_PATH + "cmp_screenAnnotation.pdf";
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull("CompareTool must return differences found between the files", result);
        // Comparing the report to the reference one.
        Assert.assertTrue("CompareTool report differs from the reference one", compareTool.compareXmls(RESOURCE_PATH + "cmp_report03.xml", OUT_PATH + "report.xml"));
    }

}
