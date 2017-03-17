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
    public void testIncompleteTableAdd01() throws DocumentException, IOException, InterruptedException {
        final String file = "incomplete_add01.pdf";

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
    public void testIncompleteTableAdd02() throws DocumentException, IOException, InterruptedException {
        final String file = "incomplete_add02.pdf";

        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, new FileOutputStream(outFolder + file));

        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setHeaderRows(2);
        table.setSplitRows(false);

        table.setComplete(false);

        for (int i = 0; i < 5; i++) {table.addCell("Header1 \n" + i);}
        for (int i = 0; i < 5; i++) {table.addCell("Header2 \n" + i);}

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
    public void testIncompleteTableAdd03() throws DocumentException, IOException, InterruptedException {
        final String file = "incomplete_add03.pdf";

        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, new FileOutputStream(outFolder + file));

        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setHeaderRows(2);
        table.setFooterRows(1);
        table.setSplitRows(false);
        table.setComplete(false);

        for (int i = 0; i < 5; i++) {table.addCell("Header \n" + i);}
        for (int i = 0; i < 5; i++) {table.addCell("Footer \n" + i);}

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
    public void testIncompleteTableAdd04() throws DocumentException, IOException, InterruptedException {
        final String file = "incomplete_add04.pdf";

        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, new FileOutputStream(outFolder + file));

        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setHeaderRows(1);
        table.setFooterRows(1);
        table.setSplitRows(false);
        table.setComplete(false);

        for (int i = 0; i < 5; i++) {table.addCell("Footer \n" + i);}

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
        float[] widths = new float[]{50f, 50f};
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

    @Test
    public void nestedHeaderFooter() throws IOException, DocumentException, InterruptedException {
        final String file = "nested_header_footer.pdf";
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(outFolder + file));
        document.open();
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase("Table XYZ (Continued)"));
        cell.setColspan(5);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Continue on next page"));
        cell.setColspan(5);
        table.addCell(cell);
        table.setHeaderRows(2);
        table.setFooterRows(1);
        table.setSkipFirstHeader(true);
        table.setSkipLastFooter(true);
        for (int i = 0; i < 350; i++) {
            table.addCell(String.valueOf(i+1));
        }
        PdfPTable t = new PdfPTable(1);
        PdfPCell c = new PdfPCell(table);
        c.setBorderColor(BaseColor.RED);
        c.setPadding(3);
        t.addCell(c);
        document.add(t);
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