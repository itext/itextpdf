package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import junit.framework.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PdfA2CheckerTest {

    @Test
    public void transparencyCheckTest() throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream("./target/transparencyCheckTest.pdf");
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2A);
        document.open();

        PdfContentByte canvas = writer.getDirectContent();

        canvas.saveState();
        PdfGState gs = new PdfGState();
        gs.setBlendMode(PdfGState.BM_DARKEN);
        canvas.setGState(gs);
        canvas.rectangle(100, 100, 100, 100);
        canvas.fill();
        canvas.restoreState();

        canvas.saveState();
        gs = new PdfGState();
        gs.setBlendMode(new PdfName("Lighten"));
        canvas.setGState(gs);
        canvas.rectangle(200, 200, 100, 100);
        canvas.fill();
        canvas.restoreState();
        canvas.saveState();
        gs = new PdfGState();
        gs.setBlendMode(new PdfName("UnknownBM"));
        canvas.setGState(gs);
        canvas.rectangle(300, 300, 100, 100);
        canvas.fill();
        canvas.restoreState();

        boolean exception = false;
        try {
            document.close();
        } catch (PdfAConformanceException pdface) {
            exception = true;
        }

        if (!exception)
            Assert.fail("PdfAConformance exception should be thrown on unknown blend mode.");

    }

    @Test
    public void imageCheckTest1() throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream("./target/imageCheckTest1.pdf");
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2B);
        document.open();

        String[] pdfaErrors = new String[9];
        for (int i = 1; i <= 9; i++) {
            try {
                Image img = Image.getInstance(String.format("./src/test/resources/com/itextpdf/text/pdf/jpeg2000/file%s.jp2", Integer.toString(i)));
                document.add(img);
                document.newPage();
            } catch (Exception e) {
                pdfaErrors[i - 1] = e.getLocalizedMessage();
            }
        }

        Assert.assertEquals(null, pdfaErrors[0]);
        Assert.assertEquals(null, pdfaErrors[1]);
        Assert.assertEquals(null, pdfaErrors[2]);
        Assert.assertEquals(null, pdfaErrors[3]);
        Assert.assertEquals(true, pdfaErrors[4].contains("0x01"));
        Assert.assertEquals(null, pdfaErrors[5]);
        Assert.assertEquals(true, pdfaErrors[6].contains("0x01"));
        Assert.assertEquals(null, pdfaErrors[7]);
        Assert.assertEquals(null, pdfaErrors[8]);

        document.close();
    }

    @Test
    public void imageCheckTest2() throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream("./target/imageCheckTest2.pdf");
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2B);
        document.open();

        ArrayList<String> pdfaErrors = new ArrayList<String>();
        try {
            Image img = Image.getInstance("./src/test/resources/com/itextpdf/text/pdf/jpeg2000/p0_01.j2k");
            document.add(img);
            document.newPage();
        } catch (Exception e) {
            pdfaErrors.add(e.getLocalizedMessage());
        }

        try {
            Image img = Image.getInstance("./src/test/resources/com/itextpdf/text/pdf/jpeg2000/p0_02.j2k");
            document.add(img);
        } catch (Exception e) {
            pdfaErrors.add(e.getLocalizedMessage());
        }

        try {
            Image img = Image.getInstance("./src/test/resources/com/itextpdf/text/pdf/jpeg2000/p1_01.j2k");
            document.add(img);
        } catch (Exception e) {
            pdfaErrors.add(e.getLocalizedMessage());
        }

        try {
            Image img = Image.getInstance("./src/test/resources/com/itextpdf/text/pdf/jpeg2000/p1_02.j2k");
            document.add(img);
        } catch (Exception e) {
            pdfaErrors.add(e.getLocalizedMessage());
        }

        Assert.assertEquals(4, pdfaErrors.size());
        for (int i = 0; i < 4; i++) {
            Assert.assertEquals(true, pdfaErrors.get(i).contains("JPX"));
        }

        document.close();
    }

    @Test
    public void layerCheckTest1() throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream("./target/layerCheckTest1.pdf");
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2B);
        writer.setViewerPreferences(PdfWriter.PageModeUseOC);
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        document.open();
        PdfLayer layer = new PdfLayer("Do you see me?", writer);
        layer.setOn(true);
        BaseFont bf = BaseFont.createFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, true);
        PdfContentByte cb = writer.getDirectContent();
        cb.beginText();
        cb.setFontAndSize(bf, 18);
        cb.showTextAligned(Element.ALIGN_LEFT, "Do you see me?", 50, 790, 0);
        cb.beginLayer(layer);
        cb.showTextAligned(Element.ALIGN_LEFT, "Peek-a-Boo!!!", 50, 766, 0);
        cb.endLayer();
        cb.endText();
        document.close();
    }

    @Test
    public void layerCheckTest2() throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream("./target/layerCheckTest2.pdf");
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2B);
        writer.setViewerPreferences(PdfWriter.PageModeUseOC);
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        PdfLayer nested = new PdfLayer("Nested layers", writer);
        PdfLayer nested_1 = new PdfLayer("Nested layer 1", writer);
        PdfLayer nested_2 = new PdfLayer("Nested layer 2", writer);
        nested.addChild(nested_1);
        nested.addChild(nested_2);
        writer.lockLayer(nested_2);
        cb.beginLayer(nested);

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, true);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("nested layers", font), 50, 775, 0);
        cb.endLayer();
        cb.beginLayer(nested_1);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("nested layer 1", font), 100, 800, 0);
        cb.endLayer();
        cb.beginLayer(nested_2);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("nested layer 2", font), 100, 750, 0);
        cb.endLayer();

        document.close();
    }

    @Test
    public void egsCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_2A);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.TR, new PdfName("Test"));
        gs.put(PdfName.HTP, new PdfName("Test"));
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_2A);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        PdfDictionary dict = new PdfDictionary();
        dict.put(PdfName.HALFTONETYPE, new PdfNumber(6));
        gs.put(PdfName.HT, dict);
        canvas.setGState(gs);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void egsCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream("./target/egsCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_2A);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        PdfDictionary dict = new PdfDictionary();
        dict.put(PdfName.HALFTONETYPE, new PdfNumber(5));
        dict.put(PdfName.HALFTONENAME, new PdfName("Test"));
        gs.put(PdfName.HT, dict);
        canvas.setGState(gs);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
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
}
