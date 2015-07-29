package com.itextpdf.text.pdf.table;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NestedTablesTest {
    private String cmpFolder = "./src/test/resources/com/itextpdf/text/pdf/table/nestedTablesTest/";
    private String outFolder = "./target/com/itextpdf/text/pdf/table/nestedTablesTest/";

    @Before
    public void setUp() throws Exception {
        new File(outFolder).mkdirs();
        TestResourceUtils.purgeTempFiles();
    }

    @Test(timeout = 30000)
    public void colorsInTaggedDocumentsTest1() throws IOException, DocumentException, InterruptedException, ParserConfigurationException, SAXException {
        String output = "nestedTablesTest.pdf";
        String cmp = "cmp_nestedTablesTest.pdf";

        long startTime = System.nanoTime();

        Document doc = new Document(PageSize.A4);
        FileOutputStream fos = new FileOutputStream(outFolder + output);
        PdfWriter writer = PdfWriter.getInstance(doc, fos);
        doc.open();

        ColumnText column = new ColumnText(writer.getDirectContent());
        column.setSimpleColumn(72, 72, 523, 770);
        column.addElement(createNestedTables(15));
        column.go();

        doc.close();

        System.out.println((System.nanoTime() - startTime) / 1e9);

        compareDocuments(output, cmp, false);
    }

    private static PdfPTable createNestedTables(int n) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Chunk("Hello"));

        if (n > 0)
            cell.addElement(createNestedTables(n - 1));

        PdfPTable table = new PdfPTable(1);
        table.addCell(cell);
        return table;
    }


    private void compareDocuments(String out, String cmp, boolean visuallyOnly) throws DocumentException, InterruptedException, IOException {
        CompareTool compareTool = new CompareTool();
        String errorMessage;
        if (visuallyOnly) {
            errorMessage = compareTool.compare(outFolder + out, cmpFolder + cmp, outFolder, "diff");
        } else {
            errorMessage = compareTool.compareByContent(outFolder + out, cmpFolder + cmp, outFolder, "diff");
        }
        if (errorMessage != null)
            Assert.fail(errorMessage);
    }
}