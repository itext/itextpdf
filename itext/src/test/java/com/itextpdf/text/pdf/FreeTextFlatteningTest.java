package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class FreeTextFlatteningTest {

    private final String FOLDER = "./src/test/resources/com/itextpdf/text/pdf/FreeTextFlatteningTest/";


    @Test
    public void flattenCorrectlyTest() throws IOException, DocumentException, InterruptedException {
        String target = "./target/com/itextpdf/test/pdf/FreeTextFlattening/";
        new File(target).mkdirs();
        String outputFile = target + "freetext-flattened.pdf";

        flattenFreeText(new FileInputStream(FOLDER + "freetext.pdf"), new FileOutputStream(outputFile));
        checkFlattenedPdf(new FileInputStream(outputFile), 0);

        String errorMessage = new CompareTool().compare(outputFile, FOLDER + "flattened.pdf", target, "diff");
        if ( errorMessage != null ) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void flattenWithoutDA() throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        flattenFreeText(new FileInputStream(FOLDER + "freetext-no-da.pdf"), baos);
        checkFlattenedPdf(new ByteArrayInputStream(baos.toByteArray()), 1);
    }

    private void checkFlattenedPdf(InputStream inputStream, int expectedAnnotationsSize) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputStream);
        PdfDictionary pageDictionary = reader.getPageN(1);
        if ( pageDictionary.contains(PdfName.ANNOTS )) {
            PdfArray annotations = pageDictionary.getAsArray(PdfName.ANNOTS);
            Assert.assertTrue(annotations.size() == expectedAnnotationsSize);
        }
    }

    private void flattenFreeText(final InputStream inputStream, OutputStream outputStream) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputStream);
        PdfStamper stamper = new PdfStamper(reader, outputStream);

        stamper.setFormFlattening(true);
        stamper.setFreeTextFlattening(true);
        stamper.setAnnotationFlattening(true);

        stamper.close();
    }
}