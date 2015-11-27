package com.itextpdf.text.xml.xmp;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.xmp.XMPException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class PdfAXmpWriterTest {
    public static final String OUT_FOLDER = "./target/com/itextpdf/text/xml/xmp/";
    public static final String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/xml/xmp/";

    @Before
    public void init() {
        new File(OUT_FOLDER).mkdirs();
    }

    @Test
    public void createPdfTest() throws IOException, DocumentException, XMPException {
        String fileName = "xmp_metadata.pdf";
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(OUT_FOLDER + fileName), PdfAConformanceLevel.PDF_A_2B);
        writer.setTagged();
        writer.createXmpMetadata();
        XmpWriter xmp = writer.getXmpWriter();

        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Hello World");
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "XMP & Metadata");
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Metadata");

        PdfProperties.setKeywords(xmp.getXmpMeta(), "Hello World, XMP & Metadata, Metadata");
        PdfProperties.setVersion(xmp.getXmpMeta(), "1.4");

        // step 3
        document.open();
        document.addLanguage("en_US");

        // step 4
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        // step 5
        document.close();

        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareXmp(OUT_FOLDER + fileName, CMP_FOLDER + fileName, true));
    }

    @Test
    public void createPdfAutomaticTest() throws IOException, DocumentException {
        String fileName = "xmp_metadata_automatic.pdf";
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(OUT_FOLDER + fileName), PdfAConformanceLevel.PDF_A_1B);
        document.addTitle("Hello World example");
        document.addSubject("This example shows how to add metadata & XMP");
        document.addKeywords("Metadata, iText, step 3");
        document.addCreator("My program using 'iText'");
        document.addAuthor("Bruno Lowagie & Paulo Soares");
        writer.createXmpMetadata();
        // step 3
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        // step 5
        document.close();
        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareXmp(OUT_FOLDER + fileName, CMP_FOLDER + fileName, true));
    }

    @Test
    public void manipulatePdfTest() throws IOException, DocumentException, XMPException {
        String fileName = "xmp_metadata_added.pdf";
        PdfReader reader = new PdfReader(CMP_FOLDER + "pdf_metadata.pdf");
        PdfStamper stamper = new PdfAStamper(reader, new FileOutputStream(OUT_FOLDER + fileName), PdfAConformanceLevel.PDF_A_1A);
        stamper.createXmpMetadata();
        XmpWriter xmp = stamper.getXmpWriter();
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Hello World");
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "XMP & Metadata");
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Metadata");

        PdfProperties.setVersion(xmp.getXmpMeta(), "1.4");
        stamper.close();
        reader.close();

        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareXmp(OUT_FOLDER + fileName, CMP_FOLDER + fileName, true));
    }

    @Test
    public void manipulatePdf2Test() throws IOException, DocumentException, XMPException {
        String fileName = "xmp_metadata_added2.pdf";
        PdfReader reader = new PdfReader(CMP_FOLDER + "pdf_metadata.pdf");
        PdfStamper stamper = new PdfAStamper(reader, new FileOutputStream(OUT_FOLDER + fileName), PdfAConformanceLevel.PDF_A_1A);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XmpWriter xmp = new PdfAXmpWriter(os, reader.getInfo(), PdfAConformanceLevel.PDF_A_1A, stamper.getWriter());
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Hello World");
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "XMP & Metadata");
        DublinCoreProperties.addSubject(xmp.getXmpMeta(), "Metadata");

        PdfProperties.setVersion(xmp.getXmpMeta(), "1.4");
        xmp.close();
        stamper.setXmpMetadata(os.toByteArray());
        stamper.close();
        reader.close();

        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareXmp(OUT_FOLDER + fileName, CMP_FOLDER + fileName, true));
    }

    @Test
    public void manipulatePdfAutomaticTest() throws IOException, DocumentException, XMPException {
        String fileName = "xmp_metadata_updated.pdf";
        PdfReader reader = new PdfReader(CMP_FOLDER + "pdf_metadata.pdf");
        PdfStamper stamper = new PdfAStamper(reader, new FileOutputStream(OUT_FOLDER + fileName), PdfAConformanceLevel.PDF_A_1A);

        stamper.close();
        reader.close();

        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareXmp(OUT_FOLDER + fileName, CMP_FOLDER + fileName, true));
    }

    @Test
    public void deprecatedLogicTest() throws IOException, DocumentException {
        String fileName = "xmp_metadata_deprecated.pdf";
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(OUT_FOLDER + fileName), PdfAConformanceLevel.PDF_A_2B);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XmpWriter xmp = new PdfAXmpWriter(os, PdfAConformanceLevel.PDF_A_2B, writer);
        XmpSchema dc = new com.itextpdf.text.xml.xmp.DublinCoreSchema();
        XmpArray subject = new XmpArray(XmpArray.UNORDERED);
        subject.add("Hello World");
        subject.add("XMP & Metadata");
        subject.add("Metadata");
        dc.setProperty(DublinCoreSchema.SUBJECT, subject);
        xmp.addRdfDescription(dc.getXmlns(), dc.toString());
        PdfSchema pdf = new PdfSchema();
        pdf.setProperty(PdfSchema.KEYWORDS, "Hello World, XMP & Metadata, Metadata");
        pdf.setProperty(PdfSchema.VERSION, "1.4");
        xmp.addRdfDescription(pdf);
        xmp.close();
        writer.setXmpMetadata(os.toByteArray());
        // step 3
        document.open();
        document.addLanguage("en_US");
        // step 4
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        // step 5
        document.close();
        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareXmp(OUT_FOLDER + fileName, CMP_FOLDER + "xmp_metadata_deprecated.pdf", true));
    }
}

