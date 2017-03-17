/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
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
