package com.itextpdf.testutils;

import com.itextpdf.text.DocumentException;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class CompareToolTest {

    private static final String OUT_PATH = "./target/com/itextpdf/testutils/CompareToolTest/";

    public void setUp() {
        new File(OUT_PATH).mkdirs();
    }

    @Test
    public void test() throws DocumentException, InterruptedException, IOException {
        CompareTool compareTool = new CompareTool();
        compareTool.setCompareByContentErrorsLimit(10);
        compareTool.setGenerateCompareByContentXmlReport(true);
        String outPdf = "./src/test/resources/com/itextpdf/testutils/CompareToolTest/simple_pdf.pdf";
        String cmpPdf = "./src/test/resources/com/itextpdf/testutils/CompareToolTest/cmp_simple_pdf.pdf";
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void test2() throws IOException, InterruptedException, DocumentException {
        CompareTool compareTool = new CompareTool();
        compareTool.setCompareByContentErrorsLimit(10);
        compareTool.setGenerateCompareByContentXmlReport(true);
        String outPdf = "./src/test/resources/com/itextpdf/testutils/CompareToolTest/tagged_pdf.pdf";
        String cmpPdf = "./src/test/resources/com/itextpdf/testutils/CompareToolTest/cmp_tagged_pdf.pdf";
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void test3() throws DocumentException, InterruptedException, IOException {
        CompareTool compareTool = new CompareTool();
        compareTool.setCompareByContentErrorsLimit(10);
        compareTool.setGenerateCompareByContentXmlReport(true);
        String outPdf = "./src/test/resources/com/itextpdf/testutils/CompareToolTest/screenAnnotation.pdf";
        String cmpPdf = "./src/test/resources/com/itextpdf/testutils/CompareToolTest/cmp_screenAnnotation.pdf";
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull(result);
    }

}
