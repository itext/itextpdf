package com.itextpdf.text.pdf.table;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class SplitTableTest {

    private static final String cmpFolder = "./src/test/resources/com/itextpdf/text/pdf/table/SplitTableTest/";
    private static final String outFolder = "./target/com/itextpdf/text/pdf/table/SplitTableTest/";

    @BeforeClass
    public static void setUp() {
        new File(outFolder).mkdirs();
        TestResourceUtils.purgeTempFiles();
    }

    @Test
    public void addOnPageBreakSimpleTest() throws IOException, DocumentException, InterruptedException {
        runLargeTableTest("addOnPageBreakSimple", 0, 0, 40, 34);
    }

    @Test
    public void addOnPageBreakHeaderTest() throws IOException, DocumentException, InterruptedException {
        runLargeTableTest("addOnPageBreakHeader", 2, 0, 40, 32);
    }

    @Test
    public void bigCellSplitDefaultTest() throws IOException, DocumentException, InterruptedException {
        runBigRowTest("bigCellSplitDefault", true, false, 700, false);
    }

    @Test
    public void bigCellSplitLateTest() throws IOException, DocumentException, InterruptedException {
        runBigRowTest("bigCellSplitLate", true, true, 700, false);
    }

    @Test
    public void bigCellNoSplitTest() throws IOException, DocumentException, InterruptedException {
        runBigRowTest("bigCellNoSplit", false, false, 700, false);
    }

    @Test
    public void veryBigCellSplitDefaultTest() throws IOException, DocumentException, InterruptedException {
        runBigRowTest("veryBigCellSplitDefault", true, false, 800, true);
    }

    @Test
    public void veryBigCellSplitLateTest() throws IOException, DocumentException, InterruptedException {
        runBigRowTest("veryBigCellSplitLate", true, true, 800, true);
    }

    @Test
    public void veryBigCellNoSplitTest() throws IOException, DocumentException, InterruptedException {
        runBigRowTest("veryBigCellNoSplit", false, false, 800, true);
    }

    private void runBigRowTest(String name, boolean splitRows, boolean splitLate, float bigRowHeight, boolean expectException)
            throws IOException, DocumentException, InterruptedException {
        final String outPdf = outFolder + name + ".pdf";
        final String cmpPdf = cmpFolder + "cmp_" + name + ".pdf";
        final String diff = "diff_" + name + "_";

        final Document document = new Document();
        final FileOutputStream out = new FileOutputStream(outPdf);
        final PdfWriter writer = PdfWriter.getInstance(document, out);

        try {
            document.setPageSize(PageSize.A4);
            document.open();

            final PdfPTable table = new PdfPTable(1);
            for (int i = 0; i < 10; ++i) {
                final PdfPCell cell = new PdfPCell(new Phrase("cell before big one #" + i));
                table.addCell(cell);
            }
            final PdfPCell bigCell = new PdfPCell(new Phrase("Big cell"));
            bigCell.setFixedHeight(bigRowHeight);
            table.addCell(bigCell);
            for (int i = 0; i < 10; ++i) {
                final PdfPCell cell = new PdfPCell(new Phrase("cell after big one #" + i));
                table.addCell(cell);
            }

            table.setSplitRows(splitRows);
            table.setSplitLate(splitLate);
            table.setComplete(true);
            document.add(table);
        } catch (DocumentException e) {
            Assert.assertTrue(expectException);
        } finally {
            document.close();
            writer.close();
            out.close();
        }

        if (!expectException) {
            Assert.assertNull(new CompareTool().compareByContent(outPdf, cmpPdf, outFolder, diff));
        }
    }

    private void runLargeTableTest(String name, int headerRows, int footerRows, int rows, Integer... flushIndexes)
            throws IOException, DocumentException, InterruptedException {
        final String outPdf = outFolder + name + ".pdf";
        final String cmpPdf = cmpFolder + "cmp_" + name + ".pdf";
        final String diff = "diff_" + name + "_";

        final Document document = new Document();
        final FileOutputStream out = new FileOutputStream(outPdf);
        final PdfWriter writer = PdfWriter.getInstance(document, out);

        document.setPageSize(PageSize.A4);
        document.open();

        final PdfPTable table = new PdfPTable(1);
        table.setComplete(false);
        table.setSplitRows(false);
        table.setSplitLate(false);

        int fullHeader = 0;
        if (headerRows > 0) {
            for (int i = 0; i < headerRows; ++i) {
                final PdfPCell header = new PdfPCell();
                header.addElement(new Phrase("Header " + i));
                table.addCell(header);
            }
            fullHeader += headerRows;
        }
        if (footerRows > 0) {
            for (int i = 0; i < footerRows; ++i) {
                final PdfPCell footer = new PdfPCell();
                footer.addElement(new Phrase("Footer " + i));
                table.addCell(footer);
            }
            fullHeader += footerRows;
            table.setFooterRows(footerRows);
        }
        table.setHeaderRows(fullHeader);

        HashSet<Integer> indexes = new HashSet<Integer>(Arrays.asList(flushIndexes));
        for (int i = 0; i < rows; ++i) {
            final PdfPCell cell = new PdfPCell();
            cell.addElement(new Phrase(String.valueOf(i)));
            table.addCell(cell);

            if (indexes.contains(i)) {
                document.add(table);
            }
        }

        table.setComplete(true);
        document.add(table);

        document.close();
        writer.close();
        out.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, cmpPdf, outFolder, diff));
    }
}
