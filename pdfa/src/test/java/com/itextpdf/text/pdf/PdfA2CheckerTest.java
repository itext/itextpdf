package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
            Assert.fail("PdfAConformance exception should be thrown on unknown blend mode.");

        if (!exception)
            document.close();

        tmpPdf.delete();
    }

}
