package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfAWriterTest {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testCreatePdfA_1() throws FileNotFoundException, DocumentException, IOException {
        try {
            Document document = new Document();
            PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/testCreatePdfA_1.pdf"), PdfAConformanceLevel.PDF_A_1A);
            writer.createXmpMetadata();
            document.open();
            Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
            document.add(new Paragraph("Hello World", font));
            ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
            writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
            document.close();
        } catch (PdfAConformanceException e) {
            Assert.fail("PdfAConformance exception should not be thrown: " + e.getLocalizedMessage());
        }
    }

    @Test
    public void testCreatePdfA_2() throws FileNotFoundException, DocumentException, IOException {
        boolean exceptionThrown = false;
        try {
            Document document = new Document();
            PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/testCreatePdfA_1.pdf"), PdfAConformanceLevel.PDF_A_1A);
            writer.createXmpMetadata();
            document.open();
            Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 12, Font.BOLD);
            document.add(new Paragraph("Hello World", font));
            ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
            writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
            document.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");
    }
}
