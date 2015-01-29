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
        String outPdf = new File(CompareToolTest.class.getResource("/com/itextpdf/testutils/CompareToolTest/simple_pdf.pdf").getPath()).getCanonicalPath();
        String cmpPdf = new File(CompareToolTest.class.getResource("/com/itextpdf/testutils/CompareToolTest/cmp_simple_pdf.pdf").getPath()).getCanonicalPath();
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void test2() throws IOException, InterruptedException, DocumentException {
        CompareTool compareTool = new CompareTool();
        compareTool.setCompareByContentErrorsLimit(10);
        compareTool.setGenerateCompareByContentXmlReport(true);
        String outPdf = new File(CompareToolTest.class.getResource("/com/itextpdf/testutils/CompareToolTest/tagged_pdf.pdf").getPath()).getCanonicalPath();
        String cmpPdf = new File(CompareToolTest.class.getResource("/com/itextpdf/testutils/CompareToolTest/cmp_tagged_pdf.pdf").getPath()).getCanonicalPath();
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull(result);
    }

}
