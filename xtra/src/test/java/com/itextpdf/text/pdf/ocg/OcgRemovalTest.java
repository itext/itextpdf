package com.itextpdf.text.pdf.ocg;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Assert;

public class OcgRemovalTest {

    private static final String INPUT_DIR = "./src/test/resources/com/itextpdf/text/pdf/ocg/";
    private static final String INPUT = INPUT_DIR + "Example.pdf";
    private static final String CMP = INPUT_DIR + "cmp_Example.pdf";
    private static final String OUTPUT_DIR = "./target/test/com/itextpdf/text/pdf/ocg/";
    private static final String OUTPUT = OUTPUT_DIR + "Example.pdf";

    @Test
    public void removeOcgLayer() throws IOException, DocumentException, InterruptedException {
        PdfReader reader = new PdfReader(INPUT);
        OCGRemover ocgRemover = new OCGRemover();
        ocgRemover.removeLayers(reader, "ecc.pricebutton");
        new File(OUTPUT_DIR).mkdirs();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUTPUT));
        stamper.close();
        reader.close();
        CompareTool cmpTool = new CompareTool();
        String errorMessage = cmpTool.compareByContent(OUTPUT, CMP, OUTPUT_DIR, "diff");

        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}