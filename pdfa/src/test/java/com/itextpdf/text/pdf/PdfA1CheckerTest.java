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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/trailerCheckTest.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/fileSpecCheckTest.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest5.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest6.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/pdfObjectCheckTest7.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/canvasCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/canvasCheckTestt2.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/colorCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/colorCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.TR, new PdfName("Test"));

        boolean exceptionThrown = false;
        try {
            canvas.setGState(gs);
        } catch (PdfAConformanceException e) {
            if (e.getObject() == gs) {
                exceptionThrown = true;
            }
        }
        document.close();
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

    }


    @Test
    public void egsCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.TR2, new PdfName("Test"));

        boolean exceptionThrown = false;
        try {
            canvas.setGState(gs);
        } catch (PdfAConformanceException e) {
            if (e.getObject() == gs) {
                exceptionThrown = true;
            }
        }
        document.close();
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

    }

    @Test
    public void egsCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.RI, new PdfName("Test"));

        boolean exceptionThrown = false;
        try {
            canvas.setGState(gs);
        } catch (PdfAConformanceException e) {
            if (e.getObject() == gs) {
                exceptionThrown = true;
            }
        }
        document.close();
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

    }


}
