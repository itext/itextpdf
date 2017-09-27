package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.ITextTest;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

public class BarcodeMacroPDF417Test extends ITextTest {

    private static final String CMP_DIR = "./src/test/resources/com/itextpdf/text/pdf/BarcodeMacroPDF417Test/";
    private static final String OUT_DIR = "./target/com/itextpdf/test/pdf/BarcodeMacroPDF417Test/";

    @Before
    public void setUp() {
        new File(OUT_DIR).mkdirs();
    }

    @Override
    protected void makePdf(String outPdf) throws Exception {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPdf));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        Image img = createBarcode(cb, "This is PDF417 segment 0", 1, 1, 0);
        document.add(new Paragraph("This is PDF417 segment 0"));
        document.add(img);

        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));

        img = createBarcode(cb, "This is PDF417 segment 1", 1, 1, 1);
        document.add(new Paragraph("This is PDF417 segment 1"));
        document.add(img);
        document.close();
    }

    public Image createBarcode(PdfContentByte cb, String text, float mh, float mw, int segmentId) throws BadElementException {
        BarcodePDF417 pf = new BarcodePDF417();

        // MacroPDF417 setup
        pf.setOptions(BarcodePDF417.PDF417_USE_MACRO);
        pf.setMacroFileId("12");
        pf.setMacroSegmentCount(2);
        pf.setMacroSegmentId(segmentId);

        pf.setText(text);
        Rectangle size = pf.getBarcodeSize();
        PdfTemplate template = cb.createTemplate(mw * size.getWidth(), mh * size.getHeight());
        pf.placeBarcode(template, BaseColor.BLACK, mh, mw);
        return Image.getInstance(template);
    }

    @Test
    public void test() throws Exception {
        runTest();
    }

    @Override
    protected void comparePdf(String outPdf, String cmpPdf) throws Exception {
        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outPdf, cmpPdf, OUT_DIR, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Override
    protected String getOutPdf() {
        return OUT_DIR + "barcode_macro_pdf417.pdf";
    }

    @Override
    protected String getCmpPdf() {
        return CMP_DIR + "cmp_barcode_macro_pdf417.pdf";
    }
}
