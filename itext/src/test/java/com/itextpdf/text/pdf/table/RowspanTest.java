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
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Michael Demey
 */

public class RowspanTest {

    private static final String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/table/RowspanTest/";
    private static final String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/table/RowspanTest/";

    @BeforeClass
    public static void init() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void rowspanTest() throws IOException, DocumentException, InterruptedException {
        String file = "rowspantest.pdf";

        File fileE = new File(CMP_FOLDER + file);
        System.out.println(fileE.exists());
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + file));
        document.open();
        PdfContentByte contentByte = writer.getDirectContent();

        Rectangle rect = document.getPageSize();

        PdfPTable table = new PdfPTable(4);

        table.setTotalWidth(rect.getRight()-rect.getLeft()+1);
        table.setLockedWidth(true);

        float[] widths = new float[]
                {
                        0.1f, 0.54f, 0.12f, 0.25f
                };

        table.setWidths(widths);

        PdfPCell cell_1_1 = new PdfPCell(new Phrase("1-1"));
        cell_1_1.setColspan(4);
        table.addCell(cell_1_1);

        PdfPCell cell_2_1 = new PdfPCell(new Phrase("2-1"));
        cell_2_1.setRowspan(2);
        table.addCell(cell_2_1);

        PdfPCell cell_2_2 = new PdfPCell(new Phrase("2-2"));
        cell_2_2.setColspan(2);
        table.addCell(cell_2_2);

        PdfPCell cell_2_4 = new PdfPCell(new Phrase("2-4"));
        cell_2_4.setRowspan(3);
        table.addCell(cell_2_4);

        PdfPCell cell_3_2 = new PdfPCell(new Phrase("3-2"));
        table.addCell(cell_3_2);

        PdfPCell cell_3_3 = new PdfPCell(new Phrase("3-3"));
        table.addCell(cell_3_3);

        PdfPCell cell_4_1 = new PdfPCell(new Phrase("4-1"));
        cell_4_1.setColspan(3);
        table.addCell(cell_4_1);

        table.writeSelectedRows(0, -1, rect.getLeft(), rect.getTop(), contentByte);

        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void nestedTableTest() throws DocumentException, IOException, InterruptedException {
        Document doc = new Document(PageSize.A4);
        String file = "nestedtabletest.pdf";
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(OUTPUT_FOLDER + file));
        doc.open();

        ColumnText col = new ColumnText(writer.getDirectContent());
        col.setSimpleColumn(
                Utilities.millimetersToPoints(25),
                Utilities.millimetersToPoints(25),
                PageSize.A4.getRight() - Utilities.millimetersToPoints(25),
                PageSize.A4.getTop() - Utilities.millimetersToPoints(25));

        PdfPTable table = new PdfPTable(3);
        table.setHeaderRows(1);
        table.addCell("H1");
        table.addCell("H2");
        table.addCell("H3");

        for (int i = 0; i < 15; i++) {
            PdfPCell cell = new PdfPCell(createNestedTable());
            cell.setRowspan(3);
            cell.setColspan(3);
            table.addCell(cell);
        }
        col.addElement(table);

        while (ColumnText.hasMoreText(col.go())) {
            doc.newPage();
            col.setYLine(PageSize.A4.getTop() - Utilities.millimetersToPoints(25));
        }

        doc.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    private PdfPTable createNestedTable() {
        PdfPTable table = new PdfPTable(3);
        table.addCell("S1");
        table.addCell("S2");
        table.addCell("S3");

        for (int i = 0; i < 2; i++) {
            table.addCell("    " + (i + 1));
            table.addCell("    " + (i + 1));
            table.addCell("    " + (i + 1));
        }
        return table;
    }
}