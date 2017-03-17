/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.error_messages.MessageLocalization;
import junit.framework.Assert;
import org.junit.Test;

import java.io.*;

public class PdfA1CheckerTest {

    private static final String outputDir = "./target/test/PdfA1/";

    static {
        new File(outputDir).mkdirs();
        try {
            MessageLocalization.setLanguage("en", "US");
        } catch (IOException e) {
        }
    }

    @Test
    public void metadaCheckTest() throws IOException, DocumentException {

        FileOutputStream fos = new FileOutputStream(outputDir + "metadaPDFA1CheckTest1.pdf");
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_1B);
        document.open();
        PdfContentByte canvas = writer.getDirectContent();

        canvas.setColorFill(BaseColor.LIGHT_GRAY);
        canvas.moveTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
        canvas.lineTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
        canvas.fill();

        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException exc) {
            exceptionThrown = true;
        }

        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown on unknown blend mode.");
    }

    @Test
    public void trailerCheckTest() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "trailerCheckTest.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fileSpecCheckTest.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfObjectCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        } finally {
            ByteBuffer.HIGH_PRECISION = false;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void pdfObjectCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfObjectCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfObjectCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
    
    // This method is used in the PdfA2CheckerTest and PdfA3CheckerTest, too
    static void pdfObjectCheck(String output, PdfAConformanceLevel level, boolean exceptionExpected) throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(output), level);
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
        if (exceptionThrown != exceptionExpected) {
            String error = exceptionExpected ? "" : " not";
            error = String.format("PdfAConformanceException should%s be thrown.", error);
            
            Assert.fail(error);
        }
    }

    @Test
    public void pdfObjectCheckTest4() throws DocumentException, IOException {
        pdfObjectCheck(outputDir + "pdfObjectCheckTest4.pdf", PdfAConformanceLevel.PDF_A_1B, true);
    }
    
    @Test
    public void pdfNamedDestinationsOverflow() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfNamedDestinationsOverflow.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.createXmpMetadata();
        writer.setTagged();
        document.open();
        document.addLanguage("en-US");

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        
        PdfDocument pdf = writer.getPdfDocument();
        for (int i=0;i<8200;i++) {
            PdfDestination dest = new PdfDestination(PdfDestination.FITV);
            pdf.localDestination("action" + i, dest);
        }
        document.close();
    }

    @Test
    public void pdfObjectCheckTest5() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfObjectCheckTest5.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfObjectCheckTest6.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfObjectCheckTest7.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "canvasCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa1CanvasCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa1ColorCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
            canvas.moveTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
            canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
            canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
            canvas.fill();
            canvas.setColorFill(BaseColor.RED);
            canvas.moveTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
            canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
            canvas.lineTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
            canvas.fill();
            document.close();
        } catch (PdfAConformanceException e) {
            if (BaseColor.RED.equals(e.getObject())) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void colorCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa1ColorCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.RED);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        boolean exceptionThrown = false;
        canvas.setColorFill(new CMYKColor(0.1f, 0.1f, 0.1f, 0.1f));
        canvas.moveTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
        canvas.fill();
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (BaseColor.RED.equals(e.getObject())) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void colorCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa1ColorCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));

        PdfContentByte canvas = writer.getDirectContent();
        canvas.setColorFill(BaseColor.GREEN);
        canvas.moveTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
        canvas.fill();

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");

        document = new Document();
        writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa1ColorCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        canvas = writer.getDirectContent();
        canvas.setColorFill(BaseColor.GREEN);
        canvas.moveTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
        canvas.fill();

        document.close();
    }

    @Test
    public void colorCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa1ColorCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        document.close();

        PdfReader reader = new PdfReader(outputDir + "pdfa1ColorCheckTest4.pdf");
        PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "pdfa1ColorCheckTest4_updating_failed.pdf"), PdfAConformanceLevel.PDF_A_2B);
        boolean exceptionThrown = false;
        try {
            icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
            stamper.getWriter().setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
            font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
            font.setColor(BaseColor.RED);
            PdfContentByte canvas = stamper.getOverContent(1);
            canvas.setFontAndSize(font.getBaseFont(), 12);
            canvas.setColorFill(BaseColor.RED);
            ColumnText.showTextAligned(canvas,
                    Element.ALIGN_LEFT, new Paragraph("Hello World", font), 36, 775, 760);
            stamper.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        reader.close();

        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");

    }


    @Test
    public void egsCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "egsCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "egsCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "egsCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "egsCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "transparencyCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "transparencyCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "transparencyCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "transparencyCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
            if (e.getObject() == annot && e.getMessage()
                    .equals("Annotation type /Movie not allowed.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.TEXT);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        annot.put(PdfName.CA, new PdfNumber(0.5));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getMessage()
                    .equals("An annotation dictionary shall not contain the CA key with a value other than 1.0.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.TEXT);
        annot.put(PdfName.F, new PdfNumber(0));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getMessage()
                    .equals("The F key's Print flag bit shall be set to 1 and its Hidden, Invisible and NoView flag bits shall be set to 0.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.TEXT);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT & PdfAnnotation.FLAGS_INVISIBLE));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getMessage()
                    .equals("The F key's Print flag bit shall be set to 1 and its Hidden, Invisible and NoView flag bits shall be set to 0.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest5() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest5.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.WIDGET);
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
            if (e.getObject() == annot && e.getMessage()
                    .equals("Appearance dictionary shall contain only the N key with stream value.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest6() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest6.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.WIDGET);
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
            if (e.getObject() == annot && e.getMessage()
                    .equals("Appearance dictionary shall contain only the N key with stream value.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest7() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest7.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.TEXT);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT | PdfAnnotation.FLAGS_NOZOOM | PdfAnnotation.FLAGS_NOROTATE));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        document.close();
    }

    @Test
    public void annotationCheckTest8() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest8.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.STAMP);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getMessage()
                    .equals("Annotation of type /Stamp should have Contents key.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest9() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest9.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.createXmpMetadata();
        writer.setTagged();
        document.open();
        document.addLanguage("en-US");

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.STAMP);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        annot.put(PdfName.CONTENTS, new PdfString("Hello World"));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        document.close();
    }

    @Test
    public void annotationCheckTest10() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest10.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.POLYGON);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getMessage()
                    .equals("Annotation type /Polygon not allowed.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest11() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest11.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.POLYLINE);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getMessage()
                    .equals("Annotation type /PolyLine not allowed.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest12() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest12.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.CARET);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getMessage()
                    .equals("Annotation type /Caret not allowed.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest13() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest13.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.WATERMARK);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getMessage()
                    .equals("Annotation type /Watermark not allowed.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest14() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest14.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.FILEATTACHMENT);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getMessage()
                    .equals("Annotation type /FileAttachment not allowed.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void fieldCheckTest1() throws DocumentException, IOException {
        String LANGUAGES[] = new String[] {"Russian", "English", "Dutch", "French", "Spanish", "German"};
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fieldCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
            radio.setBorderColor(GrayColor.GRAYBLACK);
            radio.setBackgroundColor(GrayColor.GRAYWHITE);
            radio.setCheckType(i+1);
            radio.setChecked(true);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fieldCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fieldCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fieldCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fieldCheckTest5.pdf"), PdfAConformanceLevel.PDF_A_1B);
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

    @Test
    public void markInfoCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "markInfoCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.setTagged();
        writer.createXmpMetadata();
        document.open();
        document.addLanguage("en-us");


        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 72);
        Chunk c = new Chunk("Document Header", font);
        Paragraph h1 = new Paragraph(c);
        h1.setRole(PdfName.H1);
        document.add(h1);

        document.add(new Paragraph("Hello World Hello World Hello World Hello World Hello World Hello World Hello World Hello World Hello World Hello World Hello World Hello World Hello World Hello World Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        document.close();
    }

    @Test
    public void markInfoCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "markInfoCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getLocalizedMessage().contains("markinfo") || e.getLocalizedMessage().contains("MarkInfo"))
                exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void roleMapCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "roleMapCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.setTagged();
        writer.createXmpMetadata();
        document.open();
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 72);
        Paragraph p = new Paragraph("Hello World", font);
        p.setRole(new PdfName("HW"));

        boolean exceptionThrown = false;
        try {
            document.add(p);
        } catch (DocumentException e) {
            if (e.getMessage().contains("/HW")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown) {
            Assert.fail("DocumentException should be thrown");
        }
    }

    @Test
    public void roleMapCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "roleMapCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.setTagged();
        writer.createXmpMetadata();
        document.open();
        document.addLanguage("en-us");
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 72);
        Paragraph p = new Paragraph("Hello World", font);
        p.setRole(new PdfName("HW"));
        writer.getStructureTreeRoot().mapRole(new PdfName("HW"), PdfName.P);

        document.add(p);
        document.close();
    }

    @Test
    public void roleMapCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "roleMapCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_1A);
        writer.setTagged();
        writer.createXmpMetadata();
        document.open();
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 72);
        Paragraph p = new Paragraph("Hello World", font);
        p.setRole(new PdfName("HW"));
        writer.getStructureTreeRoot().mapRole(new PdfName("HW"), PdfName.P);

        document.add(p);

        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject().toString().contains("/Catalog")) {
                exceptionThrown = true;
            }
        }
        if (exceptionThrown) {
            Assert.fail("PdfAConformanceException should be thrown");
        }

    }

    @Test
    public void fontCheckTest1() throws IOException {
        boolean exceptionThrown = false;
        try {
            Document document = new Document();
            PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2FontCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_1A);
            writer.createXmpMetadata();
            writer.setTagged();
            document.open();
            document.addLanguage("en-US");

            Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, false/*BaseFont.EMBEDDED*/, 12);
            document.add(new Paragraph("Hello World", font));
            ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
            writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

            document.close();
        } catch (DocumentException docExc) {
            exceptionThrown = true;
        } catch (PdfAConformanceException exc) {
            exceptionThrown = true;
        }

        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");
    }

    @Test
    public void stamperColorCheckTest() throws IOException, DocumentException {
        boolean exceptionThrown = false;
        try {
            PdfReader reader = new PdfReader("./src/test/resources/com/itextpdf/text/pdf/pdfa1.pdf");
            PdfAStamper stamper = new PdfAStamper(reader, new ByteArrayOutputStream(), PdfAConformanceLevel.PDF_A_1A);
            PdfContentByte canvas = stamper.getOverContent(1);
            Rectangle rect = stamper.getWriter().getPageSize();
            canvas.setColorFill(new CMYKColor(0.1f, 0.1f, 0.1f, 0.1f));
            canvas.moveTo(rect.getLeft(), rect.getBottom());
            canvas.lineTo(rect.getRight(), rect.getBottom());
            canvas.lineTo(rect.getRight(), rect.getTop());
            canvas.fill();
            stamper.close();
            reader.close();
        } catch (PdfAConformanceException exc) {
            exceptionThrown = true;
        }

        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");
    }

    @Test
    public void stamperTextTest() throws IOException, DocumentException {
        PdfReader reader = new PdfReader("./src/test/resources/com/itextpdf/text/pdf/pdfa1.pdf");
        PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "stamperTextTest.pdf"), PdfAConformanceLevel.PDF_A_1A);
        PdfArtifact artifact = new PdfArtifact();
        BaseFont bf = BaseFont.createFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf",
                BaseFont.WINANSI, BaseFont.EMBEDDED);
        artifact.setType(PdfArtifact.ArtifactType.LAYOUT);
        PdfContentByte canvas = stamper.getOverContent(1);
        canvas.openMCBlock(artifact);
        canvas.beginText();
        canvas.setFontAndSize(bf, 120);
        canvas.showTextAligned(Element.ALIGN_CENTER, "TEST", 200, 400, 45);
        canvas.endText();
        canvas.closeMCBlock(artifact);

        stamper.close();
        reader.close();
    }
}
