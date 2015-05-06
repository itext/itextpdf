package com.itextpdf.text.pdf.table;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
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

        compareTablePdf(file);
    }

    @Test
    public void testIncompleteTable2() throws IOException, DocumentException, InterruptedException {
        final String file = "incomplete_table_2.pdf";

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(outFolder + file));
        document.open();
        Font font = new Font();
        float[] widths = new float[] {50f, 50f};
        PdfPTable table = new PdfPTable(widths.length);
        table.setComplete(false);
        table.setWidths(widths);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase("Column #1", font));
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Column #2", font));
        table.addCell(cell);
        table.setHeaderRows(1);

        for (int i = 0; i < 50; i++) {
            cell = new PdfPCell(new Phrase("Table cell #" + i, font));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Blah blah blah", font));
            table.addCell(cell);
            if (i % 40 == 0) {
                document.add(table);
            }
        }
        table.setComplete(true);
        document.add(table);
        document.close();

        compareTablePdf(file);
    }

    @Test
    public void removeRowFromIncompleteTable() throws IOException, DocumentException, InterruptedException {
        final String file = "incomplete_table_row_removed.pdf";

        Document document = new Document();

        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(outFolder + file));

        document.open();

        PdfPTable table = new PdfPTable(1);
        table.setComplete(false);
        table.setTotalWidth(500);
        table.setLockedWidth(true);

        for (int i = 0; i < 21; i++)
        {
            PdfPCell cell = new PdfPCell(new Phrase("Test" + i));
            table.addCell(cell);
        }

        table.getRows().remove(20);
        document.add(table);

        table.setComplete(true);

        document.add(table);

        document.close();

        compareTablePdf(file);
    }


    private void compareTablePdf(String file) throws DocumentException, InterruptedException, IOException {
        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outFolder + file, cmpFolder + file, outFolder, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}