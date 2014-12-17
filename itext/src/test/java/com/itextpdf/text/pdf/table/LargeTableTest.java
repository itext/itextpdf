package com.itextpdf.text.pdf.table;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class LargeTableTest {


    private String cmpFolder = "./src/test/resources/com/itextpdf/text/pdf/table/largetable/";;
    private String outFolder = "./target/com/itextpdf/test/pdf/table/largetable/";

    @Before
    public void setUp() throws Exception {
        new File(outFolder).mkdirs();
        TestResourceUtils.purgeTempFiles();
    }

    @Test
    public void testNoChangeInSetSkipFirstHeader() throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        PdfPTable table = new PdfPTable(5);
        table.setHeaderRows(1);
        table.setSplitRows(false);
        table.setComplete(false);

        for (int i = 0; i < 5; i++) {
            table.addCell("Header " + i);
        }

        table.addCell("Cell 1");

        document.add(table);

        Assert.assertFalse(table.isSkipFirstHeader());

        table.setComplete(true);

        for (int i = 1; i < 5; i++) {
            table.addCell("Cell " + i);
        }

        document.add(table);

        document.close();
        baos.close();
    }

    @Test
    public void testIncompleteTableAdd() throws DocumentException, IOException, InterruptedException {
        final String file = "incomplete_add.pdf";

        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, new FileOutputStream(outFolder + file));

        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setHeaderRows(1);
        table.setSplitRows(false);
        table.setComplete(false);

        for (int i = 0; i < 5; i++) {table.addCell("Header " + i);}

        for (int i = 0; i < 500; i++) {
            if (i%5 == 0) {
                document.add(table);
            }
            table.addCell("Test " + i);
        }

        table.setComplete(true);
        document.add(table);
        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outFolder + file, cmpFolder + file, outFolder, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

}