package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CompareTool;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.options.PropertyOptions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class XmpWriterTest {
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
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUT_FOLDER + fileName));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XmpWriter xmp = new XmpWriter(os);

        xmp.getXmpMeta().appendArrayItem(XMPConst.NS_DC, DublinCoreProperties.SUBJECT, new PropertyOptions(PropertyOptions.ARRAY), "Hello World", null);
        xmp.getXmpMeta().appendArrayItem(XMPConst.NS_DC, DublinCoreProperties.SUBJECT, new PropertyOptions(PropertyOptions.ARRAY), "XMP & Metadata", null);
        xmp.getXmpMeta().appendArrayItem(XMPConst.NS_DC, DublinCoreProperties.SUBJECT, new PropertyOptions(PropertyOptions.ARRAY), "Metadata", null);

        xmp.getXmpMeta().setProperty(XMPConst.NS_PDF, PdfProperties.KEYWORDS, "Hello World, XMP, Metadata");
        xmp.getXmpMeta().setProperty(XMPConst.NS_PDF, PdfProperties.VERSION, "1.4");

        xmp.close();

        writer.setXmpMetadata(os.toByteArray());
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World"));
        // step 5
        document.close();

        CompareTool ct = new CompareTool(CMP_FOLDER + fileName, OUT_FOLDER + fileName);
        Assert.assertNull(ct.compareXmp());
    }

    @Test
    public void createPdfAutomaticTest() throws IOException, DocumentException {
        String fileName = "xmp_metadata_automatic.pdf";
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUT_FOLDER + fileName));
        document.addTitle("Hello World example");
        document.addSubject("This example shows how to add metadata & XMP");
        document.addKeywords("Metadata, iText, step 3");
        document.addCreator("My program using 'iText'");
        document.addAuthor("Bruno Lowagie & Paulo Soares");
        writer.createXmpMetadata();
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World"));
        // step 5
        document.close();
        //CompareTool ct = new CompareTool(CMP_FOLDER + fileName, OUT_FOLDER + fileName);
        //Assert.assertNull(ct.compareXmp());
    }

    @Test
    public void manipulatePdfTest() throws IOException, DocumentException {
        String fileName = "xmp_metadata_added.pdf";
        PdfReader reader = new PdfReader(CMP_FOLDER + "pdf_metadata.pdf");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUT_FOLDER + fileName));
        HashMap<String, String> info = reader.getInfo();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XmpWriter xmp = new XmpWriter(baos, info);
        xmp.close();
        stamper.setXmpMetadata(baos.toByteArray());
        stamper.close();
        reader.close();

        //CompareTool ct = new CompareTool(CMP_FOLDER + fileName, OUT_FOLDER + fileName);
        //Assert.assertNull(ct.compareXmp());
    }

    @Test
    public void deprecatedLogicTest() throws IOException, DocumentException {
        String fileName = "xmp_metadata_deprecated.pdf";
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUT_FOLDER + fileName));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XmpWriter xmp = new XmpWriter(os);
        XmpSchema dc = new com.itextpdf.text.xml.xmp.DublinCoreSchema();
        XmpArray subject = new XmpArray(XmpArray.UNORDERED);
        subject.add("Hello World");
        subject.add("XMP & Metadata");
        subject.add("Metadata");
        dc.setProperty(DublinCoreSchema.SUBJECT, subject);
        xmp.addRdfDescription(dc.getXmlns(), dc.toString());
        PdfSchema pdf = new PdfSchema();
        pdf.setProperty(PdfSchema.KEYWORDS, "Hello World, XMP, Metadata");
        pdf.setProperty(PdfSchema.VERSION, "1.4");
        xmp.addRdfDescription(pdf);
        xmp.close();
        writer.setXmpMetadata(os.toByteArray());
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World"));
        // step 5
        document.close();
        CompareTool ct = new CompareTool(CMP_FOLDER + "xmp_metadata.pdf", OUT_FOLDER + fileName);
        Assert.assertNull(ct.compareXmp());
    }
}
