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

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.*;
import static com.itextpdf.text.pdf.PdfA1CheckerTest.pdfObjectCheck;
import junit.framework.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

public class PdfA2CheckerTest {

    private static final String outputDir = "./target/test/PdfA2/";

    static {
        new File(outputDir).mkdirs();
    }

    @Test
    public void metadaCheckTest() throws IOException, DocumentException {

        FileOutputStream fos = new FileOutputStream(outputDir + "metadaPDFA2CheckTest1.pdf");
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2B);
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
    public void transparencyCheckTest1() throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream(outputDir + "pdfa2TransparencyCheckTest1.pdf");
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
        boolean conformanceExceptionThrown = false;
        try {
            canvas.saveState();
            gs = new PdfGState();
            gs.setBlendMode(new PdfName("UnknownBM"));
            canvas.setGState(gs);
            canvas.rectangle(300, 300, 100, 100);
            canvas.fill();
            canvas.restoreState();

            document.close();
        } catch (PdfAConformanceException pdface) {
            conformanceExceptionThrown = true;
        }

        if (!conformanceExceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown on unknown blend mode.");

    }

    @Test
    public void transparencyCheckTest2() {
        Document document = new Document();
        try {
            // step 2
            PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2TransperancyCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_2B);
            writer.createXmpMetadata();
            // step 3
            document.open();
            PdfDictionary sec = new PdfDictionary();
            sec.put(PdfName.GAMMA, new PdfArray(new float[]{2.2f,2.2f,2.2f}));
            sec.put(PdfName.MATRIX, new PdfArray(new float[]{0.4124f,0.2126f,0.0193f,0.3576f,0.7152f,0.1192f,0.1805f,0.0722f,0.9505f}));
            sec.put(PdfName.WHITEPOINT, new PdfArray(new float[]{0.9505f,1f,1.089f}));
            PdfArray arr = new PdfArray(PdfName.CALRGB);
            arr.add(sec);
            writer.setDefaultColorspace(PdfName.DEFAULTRGB, writer.addToBody(arr).getIndirectReference());
            // step 4
            PdfContentByte cb = writer.getDirectContent();
            float gap = (document.getPageSize().getWidth() - 400) / 3;

            pictureBackdrop(gap, 500f, cb);
            pictureBackdrop(200 + 2 * gap, 500, cb);
            pictureBackdrop(gap, 500 - 200 - gap, cb);
            pictureBackdrop(200 + 2 * gap, 500 - 200 - gap, cb);

            pictureCircles(gap, 500, cb);
            cb.saveState();
            PdfGState gs1 = new PdfGState();
            gs1.setFillOpacity(0.5f);
            cb.setGState(gs1);
            pictureCircles(200 + 2 * gap, 500, cb);
            cb.restoreState();

            cb.saveState();
            PdfTemplate tp = cb.createTemplate(200, 200);
            PdfTransparencyGroup group = new PdfTransparencyGroup();
            tp.setGroup(group);
            pictureCircles(0, 0, tp);
            cb.setGState(gs1);
            cb.addTemplate(tp, gap, 500 - 200 - gap);
            cb.restoreState();

            cb.saveState();
            tp = cb.createTemplate(200, 200);
            tp.setGroup(group);
            PdfGState gs2 = new PdfGState();
            gs2.setFillOpacity(0.5f);
            gs2.setBlendMode(PdfGState.BM_HARDLIGHT);
            tp.setGState(gs2);
            pictureCircles(0, 0, tp);
            cb.addTemplate(tp, 200 + 2 * gap, 500 - 200 - gap);
            cb.restoreState();

            Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, true);
            font.setColor(BaseColor.BLACK);
            cb.resetRGBColorFill();
            ColumnText ct = new ColumnText(cb);
            Phrase ph = new Phrase("Ungrouped objects\nObject opacity = 1.0", font);
            ct.setSimpleColumn(ph, gap, 0, gap + 200, 500, 18, Element.ALIGN_CENTER);
            ct.go();

            ph = new Phrase("Ungrouped objects\nObject opacity = 0.5", font);
            ct.setSimpleColumn(ph, 200 + 2 * gap, 0, 200 + 2 * gap + 200, 500,
                    18, Element.ALIGN_CENTER);
            ct.go();

            ph = new Phrase("Transparency group\nObject opacity = 1.0\nGroup opacity = 0.5\nBlend mode = Normal", font);
            ct.setSimpleColumn(ph, gap, 0, gap + 200, 500 - 200 - gap, 18, Element.ALIGN_CENTER);
            ct.go();

            ph = new Phrase("Transparency group\nObject opacity = 0.5\nGroup opacity = 1.0\nBlend mode = HardLight", font);
            ct.setSimpleColumn(ph, 200 + 2 * gap, 0, 200 + 2 * gap + 200, 500 - 200 - gap,
                    18, Element.ALIGN_CENTER);
            ct.go();
            //ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
            //writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

        boolean conformanceExceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException pdface) {
            conformanceExceptionThrown = true;
        }

        if (!conformanceExceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown on unknown blend mode.");
    }

    @Test
    public void transparencyCheckTest3() {
        Document document = new Document();
        try {
            // step 2
            PdfAWriter writer = PdfAWriter.getInstance(
                    document,
                    new FileOutputStream(outputDir + "pdfa2TransperancyCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_2B);
            writer.createXmpMetadata();
            // step 3
            document.open();
            PdfDictionary sec = new PdfDictionary();
            sec.put(PdfName.GAMMA, new PdfArray(new float[]{2.2f,2.2f,2.2f}));
            sec.put(PdfName.MATRIX, new PdfArray(new float[]{0.4124f,0.2126f,0.0193f,0.3576f,0.7152f,0.1192f,0.1805f,0.0722f,0.9505f}));
            sec.put(PdfName.WHITEPOINT, new PdfArray(new float[]{0.9505f,1f,1.089f}));
            PdfArray arr = new PdfArray(PdfName.CALRGB);
            arr.add(sec);
            writer.setDefaultColorspace(PdfName.DEFAULTRGB, writer.addToBody(arr).getIndirectReference());

            // step 4
            PdfContentByte cb = writer.getDirectContent();
            float gap = (document.getPageSize().getWidth() - 400) / 3;

            pictureBackdrop(gap, 500, cb, writer);
            pictureBackdrop(200 + 2 * gap, 500, cb, writer);
            pictureBackdrop(gap, 500 - 200 - gap, cb, writer);
            pictureBackdrop(200 + 2 * gap, 500 - 200 - gap, cb, writer);
            PdfTemplate tp;
            PdfTransparencyGroup group;

            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp, writer);
            group = new PdfTransparencyGroup();
            group.setIsolated(true);
            group.setKnockout(true);
            tp.setGroup(group);
            cb.addTemplate(tp, gap, 500);

            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp, writer);
            group = new PdfTransparencyGroup();
            group.setIsolated(true);
            group.setKnockout(false);
            tp.setGroup(group);
            cb.addTemplate(tp, 200 + 2 * gap, 500);

            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp, writer);
            group = new PdfTransparencyGroup();
            group.setIsolated(false);
            group.setKnockout(true);
            tp.setGroup(group);
            cb.addTemplate(tp, gap, 500 - 200 - gap);

            tp = cb.createTemplate(200, 200);
            pictureCircles(0, 0, tp, writer);
            group = new PdfTransparencyGroup();
            group.setIsolated(false);
            group.setKnockout(false);
            tp.setGroup(group);
            cb.addTemplate(tp, 200 + 2 * gap, 500 - 200 - gap);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

        boolean conformanceException = false;
        try {
            document.close();
        } catch (PdfAConformanceException pdface) {
            conformanceException = true;
        }

        if (!conformanceException)
            Assert.fail("PdfAConformance exception should be thrown on unknown blend mode.");
    }

    @Test
    public void transparencyCheckTest4() throws DocumentException, IOException {
        // step 1
        Document document = new Document(new Rectangle(850, 600));
        // step 2
        PdfAWriter writer
                = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2TransperancyCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();

        // add the clipped image
        Image img = Image.getInstance("./src/test/resources/com/itextpdf/text/pdf/img/bruno_ingeborg.jpg");
        float w = img.getScaledWidth();
        float h = img.getScaledHeight();
        canvas.ellipse(1, 1, 848, 598);
        canvas.clip();
        canvas.newPath();
        canvas.addImage(img, w, 0, 0, h, 0, -600);

        // Create a transparent PdfTemplate
        PdfTemplate t2 = writer.getDirectContent().createTemplate(850, 600);
        PdfTransparencyGroup transGroup = new PdfTransparencyGroup();
        transGroup.put(PdfName.CS, PdfName.DEVICEGRAY);
        transGroup.setIsolated(true);
        transGroup.setKnockout(false);
        t2.setGroup(transGroup);

        // Add transparent ellipses to the template
        int gradationStep = 30;
        float[] gradationRatioList = new float[gradationStep];
        for(int i = 0; i < gradationStep; i++) {
            gradationRatioList[i] = 1 - (float)Math.sin(Math.toRadians(90.0f / gradationStep * (i + 1)));
        }
        for(int i = 1; i < gradationStep + 1; i++) {
            t2.setLineWidth(5 * (gradationStep + 1 - i));
            t2.setGrayStroke(gradationRatioList[gradationStep - i]);
            t2.ellipse(0, 0, 850, 600);
            t2.stroke();
        }

        // Create an image mask for the direct content
        PdfDictionary maskDict = new PdfDictionary();
        maskDict.put(PdfName.TYPE, PdfName.MASK );
        maskDict.put(PdfName.S, new PdfName("Luminosity"));
        maskDict.put(new PdfName("G"), t2.getIndirectReference());
        PdfGState gState = new PdfGState();
        gState.put(PdfName.SMASK, maskDict );
        canvas.setGState(gState);

        canvas.addTemplate(t2, 0, 0);

        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        // step 5
        document.close();
    }

    @Test
    public void imageCheckTest1() throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream(outputDir + "imageCheckTest1.pdf");
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
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
        FileOutputStream fos = new FileOutputStream(outputDir + "imageCheckTest2.pdf");
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
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
        FileOutputStream fos = new FileOutputStream(outputDir + "layerCheckTest1.pdf");
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
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
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();
    }

    @Test
    public void layerCheckTest2() throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream(outputDir + "layerCheckTest2.pdf");
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
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
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();
    }

    @Test
    public void egsCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2egsCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.TR, new PdfName("Test"));
        gs.put(PdfName.HTP, new PdfName("Test"));
        canvas.saveState();
        canvas.setGState(gs);
        canvas.restoreState();
        canvas.moveTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
        canvas.fill();
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "egsCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_2A);
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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "egsCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_2A);
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
    public void egsCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2egsCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));

        PdfContentByte canvas = writer.getDirectContent();
        PdfGState gs = new PdfGState();
        gs.put(PdfName.TR2, new PdfName("Test"));
        gs.put(PdfName.HTP, new PdfName("Test"));
        canvas.saveState();
        canvas.setGState(gs);
        canvas.restoreState();
        canvas.moveTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
        canvas.fill();
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

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
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "canvasCheckTestt2.pdf"), PdfAConformanceLevel.PDF_A_1B);
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
    public void pdfObjectCheckTest() throws DocumentException, IOException {
        PdfA1CheckerTest.pdfObjectCheck(outputDir + "pdfObjectCheckTest.pdf", PdfAConformanceLevel.PDF_A_2B, false);
    }

    @Test
    public void annotationCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_2B);
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
            if (e.getObject() == annot && e.getLocalizedMessage().equals("Annotation type /Movie not allowed.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getLocalizedMessage().equals("Annotation type null not allowed.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest2_1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest2_1.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.POPUP);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        document.close();
    }

    @Test
    public void annotationCheckTest2_2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest2_2.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 200, 100, 200));
        annot.put(PdfName.SUBTYPE, PdfName.WIDGET);
        annot.put(PdfName.CONTENTS, new PdfDictionary());
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        document.close();
    }

    @Test
    public void annotationCheckTest2_3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest2_3.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.WIDGET);
        annot.put(PdfName.CONTENTS, new PdfDictionary());
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getLocalizedMessage()
                    .equals("Every annotation shall have at least one appearance dictionary")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_2B);
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
                    .equals("The F key's Print flag bit shall be set to 1 and its Hidden, Invisible, NoView and ToggleNoView flag bits shall be set to 0.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void annotationCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_2B);
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
                    .equals("The F key's Print flag bit shall be set to 1 and its Hidden, Invisible, NoView and ToggleNoView flag bits shall be set to 0.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest5() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest5.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        annot.put(PdfName.SUBTYPE, PdfName.WIDGET);
        annot.put(PdfName.CONTENTS, new PdfDictionary());
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
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest6() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest6.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        annot.put(PdfName.SUBTYPE, PdfName.WIDGET);
        annot.put(PdfName.CONTENTS, new PdfDictionary());
        annot.put(PdfName.FT, new PdfName("Btn"));
        PdfDictionary ap = new PdfDictionary();
        PdfStream s = new PdfStream("Hello World".getBytes());
        //PdfDictionary s = new PdfDictionary();
        ap.put(PdfName.N, writer.addToBody(s).getIndirectReference());
        annot.put(PdfName.AP, ap);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            if (e.getObject() == annot && e.getMessage()
                    .equals("Appearance dictionary of Widget subtype and Btn field type shall contain only the n key with dictionary value")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest7() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest7.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        annot.put(PdfName.SUBTYPE, PdfName.WIDGET);
        annot.put(PdfName.CONTENTS, new PdfDictionary());
        PdfDictionary ap = new PdfDictionary();
        //PdfStream s = new PdfStream("Hello World".getBytes());
        PdfDictionary s = new PdfDictionary();
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
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest8() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest8.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        annot.put(PdfName.SUBTYPE, PdfName.WIDGET);
        annot.put(PdfName.CONTENTS, new PdfDictionary());
        PdfDictionary ap = new PdfDictionary();
        PdfStream s = new PdfStream("Hello World".getBytes());
        ap.put(PdfName.N, writer.addToBody(s).getIndirectReference());
        annot.put(PdfName.AP, ap);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        document.close();
    }

    @Test
    public void annotationCheckTest9() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest9.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 200, 200));
        annot.put(PdfName.SUBTYPE, PdfName.TEXT);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT | PdfAnnotation.FLAGS_NOZOOM | PdfAnnotation.FLAGS_NOROTATE));
        PdfDictionary ap = new PdfDictionary();
        PdfStream s = new PdfStream("Hello World".getBytes());
        ap.put(PdfName.N, writer.addToBody(s).getIndirectReference());
        annot.put(PdfName.AP, ap);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        document.close();
    }

    @Test
    public void annotationCheckTest10() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest10.pdf"), PdfAConformanceLevel.PDF_A_2A);
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
            if (e.getObject() == annot && e.getLocalizedMessage()
                    .equals("Annotation of type /Stamp should have Contents key.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void annotationCheckTest11() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest11.pdf"), PdfAConformanceLevel.PDF_A_2A);
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
        PdfDictionary ap = new PdfDictionary();
        PdfStream s = new PdfStream("Hello World".getBytes());
        ap.put(PdfName.N, writer.addToBody(s).getIndirectReference());
        annot.put(PdfName.AP, ap);
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        document.close();
    }

    @Test
    public void annotationCheckTest12() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "annotationCheckTest12.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfDictionary ap = new PdfDictionary();
        PdfStream s = new PdfStream("Hello World".getBytes());
        ap.put(PdfName.N, writer.addToBody(s).getIndirectReference());

        PdfAnnotation annot = new PdfAnnotation(writer, new Rectangle(100, 100, 120, 120));
        annot.put(PdfName.AP, ap);
        annot.put(PdfName.SUBTYPE, PdfName.POLYGON);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        PdfContentByte canvas = writer.getDirectContent();
        canvas.addAnnotation(annot);
        annot = new PdfAnnotation(writer, new Rectangle(130, 130, 150, 150));
        annot.put(PdfName.SUBTYPE, PdfName.POLYLINE);
        annot.put(PdfName.AP, ap);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        canvas.addAnnotation(annot);
        annot = new PdfAnnotation(writer, new Rectangle(160, 160, 180, 180));
        annot.put(PdfName.SUBTYPE, PdfName.CARET);
        annot.put(PdfName.AP, ap);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        canvas.addAnnotation(annot);
        annot = new PdfAnnotation(writer, new Rectangle(190, 190, 210, 210));
        annot.put(PdfName.SUBTYPE, PdfName.WATERMARK);
        annot.put(PdfName.AP, ap);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        annot = new PdfAnnotation(writer, new Rectangle(220, 220, 240, 240));
        annot.put(PdfName.SUBTYPE, PdfName.FILEATTACHMENT);
        annot.put(PdfName.AP, ap);
        annot.put(PdfName.F, new PdfNumber(PdfAnnotation.FLAGS_PRINT));
        canvas.addAnnotation(annot);
        boolean exceptionThrown = false;
        try {
            document.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        Assert.assertFalse(exceptionThrown);
    }

    @Test
    public void colorCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2ColorCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();
        PdfDictionary sec = new PdfDictionary();
        sec.put(PdfName.GAMMA, new PdfArray(new float[]{2.2f,2.2f,2.2f}));
        sec.put(PdfName.MATRIX, new PdfArray(new float[]{0.4124f,0.2126f,0.0193f,0.3576f,0.7152f,0.1192f,0.1805f,0.0722f,0.9505f}));
        sec.put(PdfName.WHITEPOINT, new PdfArray(new float[]{0.9505f,1f,1.089f}));
        PdfArray arr = new PdfArray(PdfName.CALRGB);
        arr.add(sec);
        writer.setDefaultColorspace(PdfName.DEFAULTCMYK, writer.addToBody(arr).getIndirectReference());

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        font.setColor(GrayColor.GRAYBLACK);
        document.add(new Paragraph("Hello World", font));
        font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        font.setColor(new CMYKColor(0, 100, 0, 0));
        document.add(new Paragraph("Hello World", font));
        font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        font.setColor(new BaseColor(0, 255, 0));
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        PdfContentByte canvas = writer.getDirectContent();
        canvas.setColorFill(new CMYKColor(0.1f, 0.1f, 0.1f, 0.1f));
        canvas.moveTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
        canvas.fill();

        document.close();
    }

    @Test
    public void colorCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2ColorCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_2B);
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
            exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void colorCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2ColorCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();
        PdfDictionary sec = new PdfDictionary();
        sec.put(PdfName.GAMMA, new PdfArray(new float[]{2.2f,2.2f,2.2f}));
        sec.put(PdfName.MATRIX, new PdfArray(new float[]{0.4124f,0.2126f,0.0193f,0.3576f,0.7152f,0.1192f,0.1805f,0.0722f,0.9505f}));
        sec.put(PdfName.WHITEPOINT, new PdfArray(new float[]{0.9505f,1f,1.089f}));
        PdfArray arr = new PdfArray(PdfName.CALRGB);
        arr.add(sec);
        writer.setDefaultColorspace(PdfName.DEFAULTGRAY, writer.addToBody(arr).getIndirectReference());
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        document.close();

        PdfReader reader = new PdfReader(outputDir + "pdfa2ColorCheckTest3.pdf");
        PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "pdfa2ColorCheckTest3_updating_failed.pdf"), PdfAConformanceLevel.PDF_A_2B);
        boolean exceptionThrown = false;
        try {
            font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
            font.setColor(BaseColor.RED);
            PdfContentByte canvas = stamper.getOverContent(1);
            canvas.setFontAndSize(font.getBaseFont(), 12);
            canvas.setColorFill(BaseColor.RED);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Paragraph("Hello World", font), 36, 775, 0);
            stamper.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        reader.close();

        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");

        reader = new PdfReader(outputDir + "pdfa2ColorCheckTest3.pdf");
        stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "pdfa2ColorCheckTest3_updating_ok.pdf"), PdfAConformanceLevel.PDF_A_2B);
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        stamper.getWriter().setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        font.setColor(BaseColor.RED);
        PdfContentByte canvas = stamper.getOverContent(1);
        canvas.setFontAndSize(font.getBaseFont(), 12);
        canvas.setColorFill(BaseColor.RED);
        ColumnText.showTextAligned(canvas,
                Element.ALIGN_LEFT, new Paragraph("Hello World", font), 36, 775, 0);
        stamper.close();
        reader.close();
    }

    @Test
    public void colorCheckTest4() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2ColorCheckTest4.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        document.close();

        PdfReader reader = new PdfReader(outputDir + "pdfa2ColorCheckTest4.pdf");
        PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "pdfa2ColorCheckTest4_updating_failed.pdf"), PdfAConformanceLevel.PDF_A_2B);
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
    public void colorCheckTest5() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2ColorCheckTest5.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();
        boolean exceptionThrown = false;
        try {
            Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
            document.add(new Paragraph("Hello World", font));

            PdfContentByte canvas = writer.getDirectContent();

            canvas.setColorFill(BaseColor.LIGHT_GRAY);
            canvas.moveTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
            canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
            canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
            canvas.lineTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
            canvas.fill();


            canvas.setFontAndSize(font.getBaseFont(), 20);
            canvas.setColorStroke(new CMYKColor(0, 0, 0, 1f));
            canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_STROKE);
            canvas.saveState();
            canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_CLIP);
            canvas.restoreState();
            canvas.beginText();
            canvas.showTextAligned(Element.ALIGN_LEFT, "Hello World", 36, 770, 0);
            canvas.endText();

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
    public void colorCheckTest6() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2ColorCheckTest6.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));

        PdfContentByte canvas = writer.getDirectContent();

        canvas.setColorFill(BaseColor.LIGHT_GRAY);
        canvas.moveTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getBottom());
        canvas.lineTo(writer.getPageSize().getRight(), writer.getPageSize().getTop());
        canvas.lineTo(writer.getPageSize().getLeft(), writer.getPageSize().getBottom());
        canvas.fill();


        canvas.setFontAndSize(font.getBaseFont(), 20);
        canvas.setColorStroke(new CMYKColor(0, 0, 0, 1f));
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);
        canvas.beginText();
        canvas.showTextAligned(Element.ALIGN_LEFT, "Hello World", 36, 770, 0);
        canvas.endText();
        canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
        canvas.beginText();
        canvas.showTextAligned(Element.ALIGN_LEFT, "Hello World", 36, 750, 0);
        canvas.endText();

        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        document.close();
    }

    @Test
    public void fontCheckTest1() throws IOException {
        boolean exceptionThrown = false;
        try {
            Document document = new Document();
            PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "pdfa2FontCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_2A);
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

    /**
     * Prints a square and fills half of it with a gray rectangle.
     *
     * @param x
     * @param y
     * @param cb
     * @throws Exception
     */
    private void pictureBackdrop(float x, float y, PdfContentByte cb) {
        cb.setColorStroke(BaseColor.BLACK);
        cb.setColorFill(BaseColor.LIGHT_GRAY);
        cb.rectangle(x, y, 100, 200);
        cb.fill();
        cb.setLineWidth(2);
        cb.rectangle(x, y, 200, 200);
        cb.stroke();
    }

    /**
     * Prints 3 circles in different colors that intersect with eachother.
     *
     * @param x
     * @param y
     * @param cb
     * @throws Exception
     */
    private void pictureCircles(float x, float y, PdfContentByte cb) {
        cb.setColorFill(BaseColor.RED);
        cb.circle(x + 70, y + 70, 50);
        cb.fill();
        cb.setColorFill(BaseColor.YELLOW);
        cb.circle(x + 100, y + 130, 50);
        cb.fill();
        cb.setColorFill(BaseColor.BLUE);
        cb.circle(x + 130, y + 70, 50);
        cb.fill();
    }

    /**
     * Prints a square and fills half of it with a gray rectangle.
     *
     * @param x
     * @param y
     * @param cb
     * @throws Exception
     */
    private void pictureBackdrop(float x, float y, PdfContentByte cb,
                                       PdfWriter writer) {
        PdfShading axial = PdfShading.simpleAxial(writer, x, y, x + 200, y,
                BaseColor.YELLOW, BaseColor.RED);
        PdfShadingPattern axialPattern = new PdfShadingPattern(axial);
        cb.setShadingFill(axialPattern);
        cb.setColorStroke(BaseColor.BLACK);
        cb.setLineWidth(2);
        cb.rectangle(x, y, 200, 200);
        cb.fillStroke();
    }

    /**
     * Prints 3 circles in different colors that intersect with eachother.
     *
     * @param x
     * @param y
     * @param cb
     * @throws Exception
     */
    private void pictureCircles(float x, float y, PdfContentByte cb, PdfWriter writer) {
        PdfGState gs = new PdfGState();
        gs.setBlendMode(PdfGState.BM_MULTIPLY);
        gs.setFillOpacity(1f);
        cb.setGState(gs);
        cb.setColorFill(BaseColor.LIGHT_GRAY);
        cb.circle(x + 75, y + 75, 70);
        cb.fill();
        cb.circle(x + 75, y + 125, 70);
        cb.fill();
        cb.circle(x + 125, y + 75, 70);
        cb.fill();
        cb.circle(x + 125, y + 125, 70);
        cb.fill();
    }

    @Test
    public void fileSpecCheckTest1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fileSpecCheckTest1.pdf"), PdfAConformanceLevel.PDF_A_2B);
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
    public void cidFontCheckTest1() throws DocumentException, IOException, InterruptedException {
        String outPdf = outputDir + "cidFontCheckTest1.pdf";
        String resourceDir = "./src/test/resources/com/itextpdf/text/pdf/";
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outPdf), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont(resourceDir + "FreeMonoBold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream(resourceDir + "sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, resourceDir + "cidset/cmp_cidFontCheckTest1.pdf", outputDir, "diff_"));
    }

    @Test
    public void cidFontCheckTest2() throws DocumentException, IOException, InterruptedException {
        String outPdf = outputDir + "cidFontCheckTest2.pdf";
        String resourceDir = "./src/test/resources/com/itextpdf/text/pdf/";
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outPdf), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont(resourceDir + "Puritan2.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream(resourceDir + "sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, resourceDir + "cidset/cmp_cidFontCheckTest2.pdf", outputDir, "diff_"));
    }

    @Test
    public void cidFontCheckTest3() throws DocumentException, IOException, InterruptedException {
        String outPdf = outputDir + "cidFontCheckTest3.pdf";
        String resourceDir = "./src/test/resources/com/itextpdf/text/pdf/";
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outPdf), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont(resourceDir + "NotoSansCJKjp-Bold.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream(resourceDir + "sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, resourceDir + "cidset/cmp_cidFontCheckTest3.pdf", outputDir, "diff_"));
    }

    @Test
    public void fileSpecCheckTest2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fileSpecCheckTest2.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();

        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        FileInputStream is = new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/pdfa.pdf");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        writer.addPdfAttachment("some pdf file", os.toByteArray(), "foo.pdf", "foo.pdf");

        document.close();
    }

    @Test
    public void fileSpecCheckTest3() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "fileSpecCheckTest3.pdf"), PdfAConformanceLevel.PDF_A_2B);
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
            writer.addFileAttachment("foo file", txt.toByteArray(), "foo.xml", "foo.xml", "application/xml",
                    AFRelationshipValue.Source);
        } catch (PdfAConformanceException e) {
            if (e.getObject() != null && e.getLocalizedMessage().equals("Embedded file shall contain correct pdf mime type.")) {
                exceptionThrown = true;
            }
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformanceException with correct message should be thrown.");
    }

    @Test
    public void textFieldTest() throws IOException, DocumentException {
        Document d = new Document();
        PdfWriter w = PdfAWriter.getInstance(d, new FileOutputStream(outputDir + "textField.pdf"), PdfAConformanceLevel.PDF_A_2B);
        w.createXmpMetadata();
        d.open();
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        w.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        TextField text = new TextField(w, new Rectangle(50,700,150,750), "text1");
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        text.setFont(font.getBaseFont());
        text.setText("test");
        PdfFormField field = text.getTextField();
        w.addAnnotation(field);
        d.close();
    }
}
