package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.io.StreamUtil;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class PdfStamperTest {

    public static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/PdfStamperTest/";

    @Before
    public void setUp() {
        new File(DEST_FOLDER).mkdirs();
    }

    @Test
    public void setPageContentTest01() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out1.pdf";
        String testFile = getClass().getResource("PdfStamperTest/in.pdf").getFile();
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        reader.eliminateSharedStreams();
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            byte[] bb = reader.getPageContent(i);
            reader.setPageContent(i, bb);
        }
        stamper.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, getClass().getResource("PdfStamperTest/cmp_out1.pdf").getPath(), DEST_FOLDER, "diff_"));
    }

    @Test
    public void setPageContentTest02() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out2.pdf";
        String testFile = getClass().getResource("PdfStamperTest/in.pdf").getFile();
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            byte[] bb = reader.getPageContent(i);
            reader.setPageContent(i, bb);
        }
        reader.removeUnusedObjects();
        stamper.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, getClass().getResource("PdfStamperTest/cmp_out2.pdf").getPath(), DEST_FOLDER, "diff_"));
    }

    @Test
    public void layerStampingTest() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out3.pdf";
        String testFile = getClass().getResource("PdfStamperTest/House_Plan_Final.pdf").getFile();
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));

        PdfLayer logoLayer = new PdfLayer("Logos", stamper.getWriter());
        PdfContentByte cb = stamper.getUnderContent(1);
        cb.beginLayer(logoLayer);

        Image iImage = Image.getInstance(getClass().getResource("PdfStamperTest/Willi-1.jpg").getPath());
        iImage.scalePercent(24f);
        iImage.setAbsolutePosition(100, 100);
        cb.addImage(iImage);

        cb.endLayer();
        stamper.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, getClass().getResource("PdfStamperTest/cmp_House_Plan_Final.pdf").getPath(), DEST_FOLDER, "diff_"));
    }

}
