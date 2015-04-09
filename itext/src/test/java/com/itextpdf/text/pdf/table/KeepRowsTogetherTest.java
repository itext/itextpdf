package com.itextpdf.text.pdf.table;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class KeepRowsTogetherTest {

    private String cmpFolder = "./src/test/resources/com/itextpdf/text/pdf/table/keeprowstogether/";;
    private String outFolder = "./target/com/itextpdf/test/pdf/table/keeprowstogether/";

    @Before
    public void setUp() throws Exception {
        new File(outFolder).mkdirs();
        TestResourceUtils.purgeTempFiles();
    }

    @Test
    public void testKeepRowsTogetherInCombinationWithHeaders() throws DocumentException, IOException, InterruptedException {
        String file = "withheaders.pdf";
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outFolder + file));

        document.open();

        PdfPTable table = new PdfPTable(1);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Header for table 1"));
        table.addCell(cell1);
        table.setHeaderRows(1);
        for (int i = 0; i < 40; i++) {
            table.addCell("Tab 1, line " + i);
        }

        document.add(table);

        PdfPTable table2 = new PdfPTable(1);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Header for table 2"));
        table2.addCell(cell2);
        table2.setHeaderRows(1);
        for (int i = 0; i < 40; i++) {
            table2.addCell("Tab 2, line " + i);
        }

        table2.keepRowsTogether(0, 10);
        document.add(table2);

        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outFolder + file, cmpFolder + file, outFolder, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

}