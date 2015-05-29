package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
}
