package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class PdfAWriterTest {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testCreatePdfA_1() throws DocumentException, IOException {
        Document document = null;
        PdfAWriter writer = null;
        try {
            document = new Document();
            writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/testCreatePdfA_1.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        Document document = null;
        PdfAWriter writer = null;
        try {
            document = new Document();
            writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/testCreatePdfA_1.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
    public void PdfA1BMetadataTest() throws DocumentException, IOException {
        System.out.print("#> RUNING...");
        Document document = new Document(PageSize.A4);

        File outputFile = new File("./target/out-pdfa1b.pdf");
        FileOutputStream fos = new FileOutputStream(outputFile);
        PdfAWriter writer = PdfAWriter.getInstance(document, fos,
                PdfAConformanceLevel.PDF_A_1B);

        String title    = new String("Test Title");
        String author   = new String("Test Author");
        String subject  = new String("Test Subject");
        String creator  = new String("Test Creator");
        String keywords = new String("Keyword Test0, Keyword Test1");

        document.addTitle(title);
        document.addAuthor(author);
        document.addSubject(subject);
        document.addCreator(creator);
        document.addKeywords(keywords);
        document.addProducer();
        document.addCreationDate();
        document.addLanguage("en-US");
        writer.createXmpMetadata();
        document.open();

        String FONT = "c:/windows/fonts/arial.ttf";
        Font font = FontFactory.getFont(FONT, BaseFont.CP1252, BaseFont.EMBEDDED);
        document.add(new Paragraph("Hello World", font));

        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        document.close();
        writer.close();
        fos.flush();
        fos.close();

        System.out.println("DONE.");
    }

    @Test
    public void testPdfAStamper1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/testPdfAStamper.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.createXmpMetadata();
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();

        PdfReader reader = new PdfReader("./target/testPdfAStamper.pdf");
        PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream("./target/testPdfAStamper_.pdf"), PdfAConformanceLevel.PDF_A_1A);
        stamper.close();
        reader.close();
    }

    @Test
    public void testPdfAStamper2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/testPdfAStamper.pdf"), PdfAConformanceLevel.PDF_A_2A);
        writer.createXmpMetadata();
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();

        PdfReader reader = new PdfReader("./target/testPdfAStamper.pdf");
        boolean exceptionThrown = false;
        try {
            PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream("./target/testPdfAStamper_.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("./target/testPdfAStamper.pdf"));
        writer.createXmpMetadata();
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        document.close();

        PdfReader reader = new PdfReader("./target/testPdfAStamper.pdf");
        boolean exceptionThrown = false;
        try {
            PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream("./target/testPdfAStamper_.pdf"), PdfAConformanceLevel.PDF_A_1A);
            stamper.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        reader.close();
        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");
    }

}
