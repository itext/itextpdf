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
        CompareTool compareTool = new CompareTool(OUTPUT_FOLDER + file, CMP_FOLDER + file);
        String errorMessage = compareTool.compare(OUTPUT_FOLDER, "diff");
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
        CompareTool compareTool = new CompareTool(OUTPUT_FOLDER + file, CMP_FOLDER + file);
        String errorMessage = compareTool.compare(OUTPUT_FOLDER, "diff");
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