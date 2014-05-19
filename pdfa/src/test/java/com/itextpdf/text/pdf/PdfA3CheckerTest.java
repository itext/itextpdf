package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.xml.xmp.PdfAXmpWriter;
import com.itextpdf.xmp.XMPException;
import junit.framework.Assert;
import org.junit.Test;

import java.io.*;

public class PdfA3CheckerTest {

    private static final String outputDir = "./target/test/PdfA3/";

    static {
        new File(outputDir).mkdirs();
    }

    @Test
    public void fileSpecCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fileSpecCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_3B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        ByteArrayOutputStream txt = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(txt);
        out.print("<foo><foo2>Hello world</foo2></foo>");
        out.close();
        writer.addFileAttachment("foo file", txt.toByteArray(), "foo.xml", "foo.xml", "application/xml",
                AFRelationshipValue.Source);

        document.close();
    }

    @Test
    public void fileSpecCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fileSpecCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_3B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        ByteArrayOutputStream txt = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(txt);
        out.print("<foo><foo2>Hello world</foo2></foo>");
        out.close();

        PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(writer, null, "foo.xml", txt.toByteArray());
        fs.put(PdfName.AFRELATIONSHIP, AFRelationshipValue.Unspecified);

        writer.addFileAttachment(fs);

        document.close();
    }

    @Test
    public void fileSpecCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fileSpecCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_3B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        byte[] somePdf = new byte[25];
        writer.addFileAttachment("some pdf file", somePdf, "foo.pdf", "foo.pdf", PdfAWriter.MimeTypePdf,
                AFRelationshipValue.Data);

        document.close();
    }

    @Test
    public void fileSpecCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fileSpecCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_3B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        byte[] somePdf = new byte[25];
        writer.addPdfAttachment("some pdf file", somePdf, "foo.pdf", "foo.pdf");

        document.close();
    }

    @Test
    public void fileSpecCheckTest5() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fileSpecCheckTest5.pdf"), PdfAConformanceLevel.PDF_A_3B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        ByteArrayOutputStream txt = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(txt);
        out.print("<foo><foo2>Hello world</foo2></foo>");
        out.close();

        boolean exceptionThrown = false;
        try {
            PdfFileSpecification fs
                    = PdfFileSpecification.fileEmbedded(writer,
                    null, "foo.xml", txt.toByteArray());
            writer.addFileAttachment(fs);
        } catch (PdfAConformanceException e) {
            if (e.getObject() != null && e.getLocalizedMessage().equals("The file specification dictionary for an embedded file shall contain correct AFRelationship key.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void fileSpecCheckTest6() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fileSpecCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_3B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfDictionary params = new PdfDictionary();
        params.put(PdfName.MODDATE, new PdfDate());
        PdfFileSpecification fileSpec = PdfFileSpecification.fileEmbedded(
                writer, "./src/test/resources/com/itextpdf/text/pdf/foo.xml", "foo.xml", null, false, "text/xml", params);
        fileSpec.put(PdfName.AFRELATIONSHIP, AFRelationshipValue.Data);

        writer.addFileAttachment(fileSpec);

        document.close();
    }

    @Test
    public void fileSpecCheckTest7() throws DocumentException, IOException {
        FileInputStream inPdf = new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/fileSpec.pdf");
        ByteArrayOutputStream xml = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(xml);
        out.print("<foo><foo2>Hello world</foo2></foo>");
        out.close();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(inPdf);
        PdfAStamper stamper = new PdfAStamper(reader, output, PdfAConformanceLevel.PDF_A_3B);

        stamper.createXmpMetadata();

        PdfDictionary embeddedFileParams = new PdfDictionary();
        embeddedFileParams.put(PdfName.MODDATE, new PdfDate());
        PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(stamper.getWriter(), "foo", "foo",
                xml.toByteArray() , "text/xml", embeddedFileParams, 0);
        fs.put(PdfName.AFRELATIONSHIP, AFRelationshipValue.Source);
        stamper.addFileAttachment("description", fs);

        stamper.close();
        reader.close();
    }

    @Test
    public void barcodesTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document,new
                FileOutputStream(outputDir + "barcodesTest1.pdf"), PdfAConformanceLevel.PDF_A_3A);

        writer.setTagged();
        document.open();
        writer.setViewerPreferences(PdfWriter.DisplayDocTitle);
        document.addTitle("Some title");
        document.addLanguage("en-us");
        writer.createXmpMetadata();

        document.newPage();

        // Set output intent. PDF/A requirement.
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org",
                "sRGB IEC61966-2.1", icc);

        // All fonts shall be embedded. PDF/A requirement.
        Font normal9 = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 9);
        Font normal8 = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 8);

        BaseColor color = new BaseColor(111,211,11);
        normal8.setColor(color);

        PdfContentByte cb = writer.getDirectContent();

        String code = "119716-500023718";
        Barcode barcode = new Barcode39();
        barcode.setCode(code);
        barcode.setStartStopText(false);
        barcode.setFont(normal9.getBaseFont());
        barcode.setExtended(true);

        Image image = barcode.createImageWithBarcode(cb, color, color);
        image.setAlt("Bla Bla");
        document.add(image);

        document.close();
    }

    @Test
    public void zugferdInvoiceTest() throws DocumentException, IOException, XMPException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "zugferdInvoiceTest.pdf"), PdfAConformanceLevel.ZUGFeRD);
        writer.createXmpMetadata();
        writer.getXmpWriter().setProperty(PdfAXmpWriter.zugferdSchemaNS, PdfAXmpWriter.zugferdDocumentFileName, "invoice.xml");
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.MODDATE, new PdfDate());
        PdfFileSpecification fileSpec = PdfFileSpecification.fileEmbedded(
                writer, "./src/test/resources/com/itextpdf/text/pdf/invoice.xml",
                "invoice.xml", null, "application/xml", parameters, 0);
        fileSpec.put(PdfName.AFRELATIONSHIP, AFRelationshipValue.Alternative);
        writer.addFileAttachment("invoice.xml", fileSpec);
        PdfArray array = new PdfArray();
        array.add(fileSpec.getReference());
        writer.getExtraCatalog().put(new PdfName("AF"), array);

        document.close();
    }



}
