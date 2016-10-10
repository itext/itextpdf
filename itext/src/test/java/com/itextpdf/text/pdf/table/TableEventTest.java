package com.itextpdf.text.pdf.table;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Test for pull request https://github.com/itext/itextpdf/pull/22
 */
public class TableEventTest {

    private static final String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/table/TableEventTest/";
    private static final String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/table/TableEventTest/";

    @BeforeClass
    public static void init() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void tableEventTest() throws IOException, DocumentException, InterruptedException {
        String file = "tableEventTest.pdf";

        File fileE = new File(CMP_FOLDER + file);
        System.out.println(fileE.exists());
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + file));
        document.open();
        PdfPTable table = new PdfPTable(1);

        table.setTableEvent(new DummyEvent());
        table.setTotalWidth(400f);
        for (int i=0; i<10; i++) {
            table.addCell("Cell " + i);
        }
        table.writeSelectedRows(4, 8, 100, 800, writer.getDirectContent());
        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    private static class DummyEvent implements PdfPTableEvent {

        @Override
        public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
        }
    }
}
