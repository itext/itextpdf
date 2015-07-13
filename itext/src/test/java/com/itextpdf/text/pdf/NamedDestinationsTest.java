package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NamedDestinationsTest {

    private String srcFolder = "./src/test/resources/com/itextpdf/text/pdf/NamedDestinationsTest/";
    private String outFolder = "./target/com/itextpdf/test/pdf/NamedDestinationsTest/";

    @Before
    public void setUp() throws Exception {
        new File(outFolder).mkdirs();
    }

    @Test
    public void nameDestinationsTest01() throws IOException, DocumentException, InterruptedException {

        String outFile = outFolder+"namedDestinations01.pdf";

        FileOutputStream file = new FileOutputStream(outFile);
        PdfReader reader = new PdfReader(new FileInputStream(srcFolder+"documentWithoutDestinations.pdf"));

        PdfStamper stamper = new PdfStamper(reader, file);

        stamper.addNamedDestination("Destination2", 2, new PdfDestination(1, 100, 100, 10));
        stamper.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outFile, srcFolder + "cmp_namedDestinations01.pdf", outFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void nameDestinationsTest02() throws IOException, DocumentException, InterruptedException {

        String outFile = outFolder+"namedDestinations02.pdf";

        FileOutputStream file = new FileOutputStream(outFile);
        PdfReader reader = new PdfReader(new FileInputStream(srcFolder+"documentWithDestinations.pdf"));

        PdfStamper stamper = new PdfStamper(reader, file);

        stamper.addNamedDestination("Destination2", 2, new PdfDestination(1, 100, 100, 10));
        stamper.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outFile, srcFolder + "cmp_namedDestinations02.pdf", outFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void addNavigationTest() throws IOException, DocumentException, InterruptedException {
        String src = srcFolder + "primes.pdf";
        String dest = outFolder + "primes_links.pdf";
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfDestination d = new PdfDestination(PdfDestination.FIT);
        Rectangle rect = new Rectangle(0, 806, 595, 842);
        PdfAnnotation a10 = PdfAnnotation.createLink(stamper.getWriter(), rect, PdfAnnotation.HIGHLIGHT_INVERT, 2, d);
        stamper.addAnnotation(a10, 1);
        PdfAnnotation a1 = PdfAnnotation.createLink(stamper.getWriter(), rect, PdfAnnotation.HIGHLIGHT_PUSH, 1, d);
        stamper.addAnnotation(a1, 2);
        stamper.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(dest, srcFolder + "cmp_primes_links.pdf", outFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}
