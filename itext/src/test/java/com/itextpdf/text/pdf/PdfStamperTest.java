package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class PdfStamperTest {

    private static final String RESOURCE_FOLDER ="./src/test/resources/com/itextpdf/text/pdf/PdfStamperTest/";
    private static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/PdfStamperTest/";

    @Before
    public void setUp() {
        new File(DEST_FOLDER).mkdirs();
    }

    @Test
    public void setPageContentTest01() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out1.pdf";
        String testFile = RESOURCE_FOLDER + "in.pdf";
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        reader.eliminateSharedStreams();
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            byte[] bb = reader.getPageContent(i);
            reader.setPageContent(i, bb);
        }
        stamper.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, RESOURCE_FOLDER + "cmp_out1.pdf", DEST_FOLDER, "diff_"));
    }

    @Test
    public void setPageContentTest02() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out2.pdf";
        String testFile = RESOURCE_FOLDER + "in.pdf";
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            byte[] bb = reader.getPageContent(i);
            reader.setPageContent(i, bb);
        }
        reader.removeUnusedObjects();
        stamper.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, RESOURCE_FOLDER + "cmp_out2.pdf", DEST_FOLDER, "diff_"));
    }

    @Test
    public void layerStampingTest() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out3.pdf";
        String testFile = RESOURCE_FOLDER + "House_Plan_Final.pdf";
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));

        PdfLayer logoLayer = new PdfLayer("Logos", stamper.getWriter());
        PdfContentByte cb = stamper.getUnderContent(1);
        cb.beginLayer(logoLayer);

        Image iImage = Image.getInstance(RESOURCE_FOLDER + "Willi-1.jpg");
        iImage.scalePercent(24f);
        iImage.setAbsolutePosition(100, 100);
        cb.addImage(iImage);

        cb.endLayer();
        stamper.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, getClass().getResource("PdfStamperTest/cmp_House_Plan_Final.pdf").getPath(), DEST_FOLDER, "diff_"));
    }
    
    @Test
    public void fixEmptyOCGsTest() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out4.pdf";
        String testFile = RESOURCE_FOLDER + "EmptyOCGs.pdf";
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        stamper.getPdfLayers();
        stamper.close();
        
        Assert.assertNull(new CompareTool().compareByContent(outPdf, getClass().getResource("PdfStamperTest/cmp_EmptyOCGs.pdf").getPath(), DEST_FOLDER, "diff_"));

    }

}
