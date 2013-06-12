package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import junit.framework.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfA1CheckerTest {

    @Test
    public void trailerCheckTest() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/trailerCheckTest.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        writer.setEncryption(null, null, 1, PdfWriter.STANDARD_ENCRYPTION_40);
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getMessage().contains("Encrypt"))
                exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void fileSpecCheckTest() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/fileSpecCheckTest.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(writer, "./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm", "sRGB Color Space Profile.icm", null);
        writer.getExtraCatalog().put(new PdfName("EmbeddedFile"), fs);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == fs)
                exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void pdfObjectCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        ByteBuffer.HIGH_PRECISION = true;
        PdfNumber num = new PdfNumber(65535.12);
        writer.getExtraCatalog().put(new PdfName("TestNumber"), num);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == num)
                exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

    }

    @Test
    public void pdfObjectCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        String str = "";
        for (int i = 0; i < 65536; i++) {
            str += 'a';
        }
        PdfString string = new PdfString(str);
        writer.getExtraCatalog().put(new PdfName("TestString"), string);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == string)
                exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

    }

    @Test
    public void pdfObjectCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        String str = "";
        for (int i = 0; i < 65535; i++) {
            str += 'a';
        }
        PdfString string = new PdfString(str);
        writer.getExtraCatalog().put(new PdfName("TestString"), string);
        document.close();
    }

    @Test
    public void pdfObjectCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfArray array = new PdfArray();
        for (int i = 0; i < 8192; i++) {
            array.add(new PdfNull());
        }
        writer.getExtraCatalog().put(new PdfName("TestArray"), array);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == array)
                exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

    }

    @Test
    public void pdfObjectCheckTest5() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest5.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfArray array = new PdfArray();
        for (int i = 0; i < 8191; i++) {
            array.add(new PdfNull());
        }
        writer.getExtraCatalog().put(new PdfName("TestArray"), array);
        document.close();
    }

    @Test
    public void pdfObjectCheckTest6() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest6.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfDictionary dictionary = new PdfDictionary();
        for (int i = 0; i < 4096; i++) {
            dictionary.put(new PdfName("a" + Integer.toString(i)), new PdfName("b" + Integer.toString(i)));
        }
        writer.getExtraCatalog().put(new PdfName("TestDictionary"), dictionary);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == dictionary)
                exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

    }

    @Test
    public void pdfObjectCheckTest7() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest7.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfDictionary dictionary = new PdfDictionary();
        for (int i = 0; i < 4095; i++) {
            dictionary.put(new PdfName("a" + Integer.toString(i)), new PdfName("b" + Integer.toString(i)));
        }
        writer.getExtraCatalog().put(new PdfName("TestDictionary"), dictionary);
        document.close();

    }

    @Test
    public void canvasCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/canvasCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        boolean exceptionThrown = false;
        try {
            for (int i = 0; i < 29; i++) {
                canvas.saveState();
            }
        } catch (PdfAConformanceException e) {
            if ("q".equals(e.getObject())) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
        for (int i = 0; i < 28; i++) {
            canvas.restoreState();
        }

        document.close();

    }

    @Test
    public void canvasCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/canvasCheckTestt2.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        for (int i = 0; i < 28; i++) {
            canvas.saveState();
        }
        for (int i = 0; i < 28; i++) {
            canvas.restoreState();
        }
        document.close();
    }

    @Test
    public void colorCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/colorCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        boolean exceptionThrown = false;
        try {
            canvas.setColorFill(new CMYKColor(0.1f, 0.1f, 0.1f, 0.1f));
            canvas.setColorFill(BaseColor.RED);
        } catch (PdfAConformanceException e) {
            if (BaseColor.RED == e.getObject()) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

        document.close();

    }

    @Test
    public void colorCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/colorCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.RED);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        boolean exceptionThrown = false;
        canvas.setColorFill(new CMYKColor(0.1f, 0.1f, 0.1f, 0.1f));
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (BaseColor.RED == e.getObject()) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }


    @Test
    public void egsCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.TR, new PdfName("Test"));
        canvas.setGState(gs);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == gs) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

    }


    @Test
    public void egsCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.TR2, PdfName.DEFAULT);
        canvas.setGState(gs);
        document.close();

    }

    @Test
    public void egsCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.TR2, new PdfName("Test"));
        canvas.setGState(gs);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == gs) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

    }

    @Test
    public void egsCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.RI, new PdfName("Test"));
        canvas.setGState(gs);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == gs) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void transparencyCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/transparencyCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfTemplate template = PdfTemplate.createTemplate(writer, 100, 100);
        PdfTransparencyGroup group = new PdfTransparencyGroup();
        template.setGroup(group);
        canvas.addTemplate(template, 100, 100);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (((PdfFormXObject)e.getObject()).getAsDict(PdfName.GROUP) == group) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void transparencyCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/transparencyCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.SMASK, new PdfName("Test"));
        canvas.setGState(gs);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == gs) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void transparencyCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/transparencyCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.SMASK, PdfName.NONE);

        canvas.setGState(gs);
        document.close();
    }

    @Test
    public void transparencyCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/transparencyCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();

        canvas.setGState(gs);
        document.close();
    }

    @Test
    public void annotationCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/annotationCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, new PdfName("Movie"));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/annotationCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        annot.put(PdfName.CA, new PdfNumber(0.5));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/annotationCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.F, new PdfNumber(0));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/annotationCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT & PdfAnnotation.FLAGS_INVISIBLE));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest5() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/annotationCheckTest5.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        PdfDictionary ap = new PdfDictionary();
        PdfStream s = new PdfStream("Hello World".getBytes());
        ap.put(PdfName.D, new PdfDictionary());
        ap.put(PdfName.N, writer.addToBody(s).getIndirectReference());
        annot.put(PdfName.AP, ap);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest6() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/annotationCheckTest6.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        PdfDictionary ap = new PdfDictionary();
        ap.put(PdfName.N, new PdfDictionary());
        annot.put(PdfName.AP, ap);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest7() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/annotationCheckTest7.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        PdfTemplate template = new PdfTemplate(writer);
        template.rectangle(100, 100, 200, 200);
        annot.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, template);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        document.close();
    }

    @Test
    public void fieldCheckTest1() throws DocumentException, IOException {
        String LANGUAGES[] = new String[] {"Russian", "English", "Dutch", "French"};
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/fieldCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
        document.open();
        writer.createXmpMetadata();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);


        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect;
        PdfFormField field;
        PdfFormField radiogroup
                = PdfFormField.createRadioButton(writer, true);
        radiogroup.setFieldName("language");
        RadioCheckField radio;
        for (int i = 0; i < LANGUAGES.length; i++) {
            rect = new Rectangle(
                    40, 806 - i * 40, 60, 788 - i * 40);
            radio = new PdfARadioCheckField(
                    writer, rect, null, LANGUAGES[i]);
            radio.setFont(font.getBaseFont());
            radio.setBorderColor(GrayColor.GRAYBLACK);
            radio.setBackgroundColor(GrayColor.GRAYWHITE);
            radio.setCheckType(RadioCheckField.TYPE_CIRCLE);
            field = radio.getRadioField();
            radiogroup.addKid(field);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase(LANGUAGES[i], font), 70, 790 - i * 40, 0);
        }
        writer.addAnnotation(radiogroup);


        document.close();
    }

    @Test
    public void fieldCheckTest2() throws DocumentException, IOException {
        String LANGUAGES[] = new String[] {"Russian", "English", "Dutch", "French"};
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/fieldCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
        document.open();
        writer.createXmpMetadata();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);


        PdfFormField radiogroup
                = PdfFormField.createRadioButton(writer, true);
        radiogroup.setFieldName("language");
        Rectangle rect = new Rectangle(40, 806, 60, 788);
        RadioCheckField radio;
        PdfFormField radiofield;
        for (int page = 0; page < LANGUAGES.length; ) {
            radio = new PdfARadioCheckField(writer, rect, null, LANGUAGES[page]);
            radio.setFont(font.getBaseFont());
            radio.setBackgroundColor(new GrayColor(0.8f));
            radiofield = radio.getRadioField();
            radiofield.setPlaceInPage(++page);
            radiogroup.addKid(radiofield);
        }
        writer.addAnnotation(radiogroup);
        PdfContentByte cb = writer.getDirectContent();
        for (int i = 0; i < LANGUAGES.length; i++) {
            cb.beginText();
            cb.setFontAndSize(font.getBaseFont(), 18);
            cb.showTextAligned(Element.ALIGN_LEFT, LANGUAGES[i], 70, 790, 0);
            cb.endText();
            document.newPage();
        }
        document.close();
    }

    @Test
    public void fieldCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/fieldCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
        document.open();
        writer.createXmpMetadata();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);


        Rectangle rect = new Rectangle(300, 806, 360, 788);
        PushbuttonField button
                = new PushbuttonField(writer, rect, "Buttons");
        button.setFont(font.getBaseFont());
        button.setBackgroundColor(new GrayColor(0.75f));
        button.setBorderColor(GrayColor.GRAYBLACK);
        button.setBorderWidth(1);
        button.setBorderStyle(
                PdfBorderDictionary.STYLE_BEVELED);
        button.setTextColor(GrayColor.GRAYBLACK);
        button.setFontSize(12);
        button.setText("Push me");
        button.setLayout(
                PushbuttonField.LAYOUT_ICON_LEFT_LABEL_RIGHT);
        button.setScaleIcon(PushbuttonField.SCALE_ICON_ALWAYS);
        button.setProportionalIcon(true);
        button.setIconHorizontalAdjustment(0);
        PdfFormField field = button.getField();
        writer.addAnnotation(field);

        document.close();
    }

    @Test
    public void fieldCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/fieldCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1B);
        document.open();
        writer.createXmpMetadata();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);


        Rectangle rect = new Rectangle(300, 806, 360, 788);
        TextField text
                = new TextField(writer, rect, String.format("text_%s", 1));
        text.setBackgroundColor(new GrayColor(0.75f));
        text.setFont(font.getBaseFont());
        text.setBorderStyle(PdfBorderDictionary.STYLE_BEVELED);
        text.setText("Enter your name here...");
        text.setFontSize(0);
        text.setAlignment(Element.ALIGN_CENTER);
        text.setOptions(TextField.REQUIRED);
        PdfFormField field = text.getTextField();
        writer.addAnnotation(field);

        document.close();
    }

    @Test
    public void fieldCheckTest5() throws DocumentException, IOException {
        String LANGUAGES[] = new String[] {"Russian", "English", "Dutch", "French"};
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/fieldCheckTest5.pdf"), PdfAConformanceLevel.PDF_A_1B);
        document.open();
        writer.createXmpMetadata();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);


        PdfContentByte canvas = writer.getDirectContent();
        Rectangle rect;
        PdfFormField field;
        PdfFormField radiogroup
                = PdfFormField.createRadioButton(writer, true);
        radiogroup.setFieldName("language");
        RadioCheckField radio;
        for (int i = 0; i < LANGUAGES.length; i++) {
            rect = new Rectangle(
                    40, 806 - i * 40, 60, 788 - i * 40);
            radio = new RadioCheckField(
                    writer, rect, null, LANGUAGES[i]);
            radio.setFont(font.getBaseFont());
            radio.setBorderColor(GrayColor.GRAYBLACK);
            radio.setBackgroundColor(GrayColor.GRAYWHITE);
            field = radio.getRadioField();
            radiogroup.addKid(field);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase(LANGUAGES[i], font), 70, 790 - i * 40, 0);
        }
        writer.addAnnotation(radiogroup);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getLocalizedMessage().contains(".n.") || e.getLocalizedMessage().contains(" N "))
                exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }




}
