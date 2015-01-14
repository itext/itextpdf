package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class PdfCleanUpProcessorTest {

    @Test
    public void cleanUpTest01() throws IOException, DocumentException, InterruptedException {
        String input = "./src/test/resources/com/itextpdf/text/pdf/pdfcleanup/page229.pdf";
        String output = "./target/test/com/itextpdf/text/pdf/pdfcleanup/page229_01.pdf";
        new File(output).getParentFile().mkdirs();
        PdfReader reader = new PdfReader(input);
        FileOutputStream fos = new FileOutputStream(output);
        PdfStamper stamper = new PdfStamper(reader, fos);
        Document.compress = false;

        List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<PdfCleanUpLocation>();
        cleanUpLocations.add(new PdfCleanUpLocation(1, new Rectangle(240.0f, 602.3f, 275.7f, 614.8f), BaseColor.GRAY));
        cleanUpLocations.add(new PdfCleanUpLocation(1, new Rectangle(171.3f, 550.3f, 208.4f, 562.8f), BaseColor.GRAY));
        cleanUpLocations.add(new PdfCleanUpLocation(1, new Rectangle(270.7f, 459.2f, 313.1f, 471.7f), BaseColor.GRAY));
        cleanUpLocations.add(new PdfCleanUpLocation(1, new Rectangle(249.9f, 329.3f, 279.6f, 341.8f), BaseColor.GRAY));
        cleanUpLocations.add(new PdfCleanUpLocation(1, new Rectangle(216.2f, 303.3f, 273.0f, 315.8f), BaseColor.GRAY));
        PdfCleanUpProcessor cleaner = new PdfCleanUpProcessor(cleanUpLocations, stamper);
        cleaner.cleanUp();

        stamper.close();
        fos.close();
        reader.close();

        String cmp = "./src/test/resources/com/itextpdf/text/pdf/pdfcleanup/cmp_page229_01.pdf";
        CompareTool cmpTool = new CompareTool();
        String errorMessage = cmpTool.compareByContent(output, cmp, "./target/test/com/itextpdf/text/pdf/pdfcleanup", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void cleanUpTest02() throws IOException, DocumentException, InterruptedException {
        String input = "./src/test/resources/com/itextpdf/text/pdf/pdfcleanup/page166_03.pdf";
        String output = "./target/test/com/itextpdf/text/pdf/pdfcleanup/page166_03.pdf";
        new File(output).getParentFile().mkdirs();
        PdfReader reader = new PdfReader(input);
        FileOutputStream fos = new FileOutputStream(output);
        PdfStamper stamper = new PdfStamper(reader, fos);
        PdfCleanUpProcessor cleaner = new PdfCleanUpProcessor(stamper);
        cleaner.cleanUp();

        stamper.close();
        fos.close();
        reader.close();

        String cmp = "./src/test/resources/com/itextpdf/text/pdf/pdfcleanup/cmp_page166_03.pdf";
        CompareTool cmpTool = new CompareTool();
        String errorMessage = cmpTool.compareByContent(output, cmp, "./target/test/com/itextpdf/text/pdf/pdfcleanup", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void cleanUpTest03() throws IOException, DocumentException, InterruptedException {
        String input = "./src/test/resources/com/itextpdf/text/pdf/pdfcleanup/page166_04.pdf";
        String output = "./target/test/com/itextpdf/text/pdf/pdfcleanup/page166_04.pdf";
        new File(output).getParentFile().mkdirs();
        PdfReader reader = new PdfReader(input);
        FileOutputStream fos = new FileOutputStream(output);
        PdfStamper stamper = new PdfStamper(reader, fos);
        PdfCleanUpProcessor cleaner = new PdfCleanUpProcessor(stamper);
        cleaner.cleanUp();

        stamper.close();
        fos.close();
        reader.close();

        String cmp = "./src/test/resources/com/itextpdf/text/pdf/pdfcleanup/cmp_page166_04.pdf";
        CompareTool cmpTool = new CompareTool();
        String errorMessage = cmpTool.compareByContent(output, cmp, "./target/test/com/itextpdf/text/pdf/pdfcleanup", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void cleanUpTest04() throws IOException, DocumentException, InterruptedException {
        String input = "./src/test/resources/com/itextpdf/text/pdf/pdfcleanup/hello_05.pdf";
        String output = "./target/test/com/itextpdf/text/pdf/pdfcleanup/hello_05.pdf";
        new File(output).getParentFile().mkdirs();
        PdfReader reader = new PdfReader(input);
        FileOutputStream fos = new FileOutputStream(output);
        PdfStamper stamper = new PdfStamper(reader, fos);
        PdfCleanUpProcessor cleaner = new PdfCleanUpProcessor(stamper);
        cleaner.cleanUp();

        stamper.close();
        fos.close();
        reader.close();

        String cmp = "./src/test/resources/com/itextpdf/text/pdf/pdfcleanup/cmp_hello_05.pdf";
        CompareTool cmpTool = new CompareTool();
        String errorMessage = cmpTool.compareByContent(output, cmp, "./target/test/com/itextpdf/text/pdf/pdfcleanup", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}
