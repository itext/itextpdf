package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PdfA2CheckerTest {

    @Test
    public void transparencyCheckTest() throws IOException, DocumentException {
        File tmpPdf = File.createTempFile("pdfa2_", ".pdf");
        FileOutputStream fos = new FileOutputStream(tmpPdf);
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

        boolean exception = false;
        try {
            canvas.saveState();
            gs = new PdfGState();
            gs.setBlendMode(new PdfName("UnknownBM"));
            canvas.setGState(gs);
            canvas.rectangle(300, 300, 100, 100);
            canvas.fill();
            canvas.restoreState();
        } catch (PdfAConformanceException pdface) {
            exception = true;
        }

        if (!exception)
            document.close();
        tmpPdf.delete();

        if (!exception)
            Assert.fail("PdfAConformance exception should be thrown on unknown blend mode.");

    }

    @Test
    public void imageCheckTest1() throws IOException, DocumentException {
        File tmpPdf = File.createTempFile("pdfa2_", ".pdf");
        FileOutputStream fos = new FileOutputStream(tmpPdf);
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2A);
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
        tmpPdf.delete();
    }

    @Test
    public void imageCheckTest2() throws IOException, DocumentException {
        File tmpPdf = File.createTempFile("pdfa2_", ".pdf");
        FileOutputStream fos = new FileOutputStream(tmpPdf);
        Document document = new Document();
        PdfWriter writer = PdfAWriter.getInstance(document, fos, PdfAConformanceLevel.PDF_A_2A);
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
        tmpPdf.delete();
    }


}
