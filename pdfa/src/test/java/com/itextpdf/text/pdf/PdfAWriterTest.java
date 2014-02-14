package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class PdfAWriterTest {

    private static final String outputDir = "./target/test/writer/";

    static {
        new File(outputDir).mkdirs();
    }

    @Test
    public void testCreatePdfA_1() throws DocumentException, IOException {
        Document document;
        PdfAWriter writer;
        try {
            document = new Document();
            writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "testCreatePdfA_1.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
    public void testCreatePdfA_2() throws DocumentException, IOException {
        boolean exceptionThrown = false;
        Document document;
        PdfAWriter writer;
        try {
            document = new Document();
            writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "testCreatePdfA_1.pdf"), PdfAConformanceLevel.PDF_A_1A);
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

    @Test
    public void testPdfAStamper1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "testPdfAStamper.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();

        PdfReader reader = new PdfReader(outputDir + "testPdfAStamper.pdf");
        PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "testPdfAStamper_.pdf"), PdfAConformanceLevel.PDF_A_1B);
        stamper.close();
        reader.close();
    }

    @Test
    public void testPdfAStamper2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "testPdfAStamper.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();

        PdfReader reader = new PdfReader(outputDir + "testPdfAStamper.pdf");
        boolean exceptionThrown = false;
        try {
            PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "testPdfAStamper_.pdf"), PdfAConformanceLevel.PDF_A_1B);
            stamper.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        reader.close();
        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");
    }

    @Test
    public void testPdfAStamper3() throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputDir + "testPdfAStamper.pdf"));
        writer.createXmpMetadata();
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        document.close();

        PdfReader reader = new PdfReader(outputDir + "testPdfAStamper.pdf");
        boolean exceptionThrown = false;
        try {
            PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "testPdfAStamper_.pdf"), PdfAConformanceLevel.PDF_A_1A);
            stamper.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        reader.close();
        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");
    }

}
