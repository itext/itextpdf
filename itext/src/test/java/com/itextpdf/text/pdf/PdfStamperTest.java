package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.DocumentException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

        new CompareTool().compareByContent(outPdf, getClass().getResource("PdfStamperTest/cmp_out1.pdf").getPath(), DEST_FOLDER, "diff_");
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

        new CompareTool().compareByContent(outPdf, getClass().getResource("PdfStamperTest/cmp_out2.pdf").getPath(), DEST_FOLDER, "diff_");
    }

}
