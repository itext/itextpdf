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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TableColorsTest {
    private String cmpFolder = "./src/test/resources/com/itextpdf/text/pdf/table/tableColorsTest/";
    private String outFolder = "./target/com/itextpdf/text/pdf/table/tableColorsTest/";

    private String errorMessage;

    @Before
    public void setUp() throws Exception {
        new File(outFolder).mkdirs();
        TestResourceUtils.purgeTempFiles();
    }

    @Test
    public void coloredTablesTest1() throws IOException, DocumentException, InterruptedException, ParserConfigurationException, SAXException {
        String output = "coloredTables.pdf";
        String cmp = "cmp_coloredTables.pdf";

        createColoredTablesFile(outFolder + output, true);
        compareDocuments(output, cmp, false);
    }

    @Test
    public void coloredTablesTest2() throws IOException, DocumentException, InterruptedException, ParserConfigurationException, SAXException {
        String output = "coloredTables.pdf";
        String cmp = "cmp_coloredTables.pdf";

        //visually comparing results of tagged and non-tagged colored tables
        createColoredTablesFile(outFolder + output, false);
        compareDocuments(output, cmp, true);
    }

    private void createColoredTablesFile(String outPath, boolean tagged) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
        if (tagged)
            writer.setTagged();
        document.open();

        BaseColor color = new BaseColor(255,255,240);
        Font coloredFont = new Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL, color);

        //First table
        PdfPTable table = new PdfPTable(4);
        int rowsNum = 10;
        int columnsNum = 4;
        for (int i = 0; i < rowsNum; ++i) {
            for (int j = 0; j < columnsNum; ++j) {
                PdfPCell cell = new PdfPCell(new Paragraph("text", coloredFont));
                cell.setBorderWidth(2);
                cell.setBorderColor(BaseColor.DARK_GRAY);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }
        }
        document.add(table);
        document.newPage();


        Font fontRed = new Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL, new BaseColor(255, 0, 0));
        Font fontGreen = new Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL, new BaseColor(0, 255, 0));
        Font fontBlue = new Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL, new BaseColor(0, 0, 255));

        //Second table
        table = new PdfPTable(4);

        PdfPCell cell11 = new PdfPCell(new Paragraph("text", fontRed));
        PdfPCell cell12 = new PdfPCell(new Paragraph("text", fontBlue));
        PdfPCell cell13 = new PdfPCell(new Paragraph("text", fontGreen));


        PdfPCell cell21 = new PdfPCell(new Paragraph("text", fontRed));
        PdfPCell cell22 = new PdfPCell(new Paragraph("text", fontGreen));
        PdfPCell cell23 = new PdfPCell(new Paragraph("text", fontBlue));

        PdfPCell cell32 = new PdfPCell(new Paragraph("text", fontBlue));
        PdfPCell cell33 = new PdfPCell(new Paragraph("text", fontRed));
        PdfPCell cell34 = new PdfPCell(new Paragraph("text", fontGreen));

        table.addCell(cell11);
        table.addCell(cell12);
        table.addCell(cell13);

        table.addCell(cell21);
        table.addCell(cell22);
        table.addCell(cell23);

        table.addCell(cell32);
        table.addCell(cell33);
        table.addCell(cell34);

        document.add(table);

        document.add(new Phrase("  "));

        //Third table
        table = new PdfPTable(4);

        cell11 = new PdfPCell(new Paragraph("text", fontRed));
        cell11.setBackgroundColor(BaseColor.YELLOW);
        cell11.setBorderWidth(3);
        cell11.setBorderColor(new BaseColor(0, 0, 255));
        cell12 = new PdfPCell(new Paragraph("text", fontBlue));
        cell13 = new PdfPCell(new Paragraph("text", fontGreen));


        cell21 = new PdfPCell(new Paragraph("text", fontRed));
        cell21.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell21.setBorderColor(BaseColor.PINK);
        cell21.setBorderWidth(3);
        cell22 = new PdfPCell(new Paragraph("text", fontGreen));
        cell22.setBackgroundColor(BaseColor.YELLOW);
        cell22.setBorderColor(BaseColor.BLUE);
        cell22.setBorderWidth(3);
        cell23 = new PdfPCell(new Paragraph("text", fontBlue));
        cell23.setBackgroundColor(BaseColor.GREEN);
        cell23.setBorderWidth(3);
        cell23.setBorderColor(BaseColor.WHITE);

        cell32 = new PdfPCell(new Paragraph("text", fontBlue));
        cell32.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell32.setBorderColor(BaseColor.MAGENTA);
        cell32.setBorderWidth(3);
        cell33 = new PdfPCell(new Paragraph("text", fontRed));
        cell33.setBackgroundColor(BaseColor.PINK);
        cell33.setBorderColor(BaseColor.CYAN);
        cell33.setBorderWidth(3);
        cell34 = new PdfPCell(new Paragraph("text", fontGreen));
        cell34.setBackgroundColor(BaseColor.ORANGE);
        cell34.setBorderColor(BaseColor.WHITE);
        cell34.setBorderWidth(3);

        table.addCell(cell11);
        table.addCell(cell12);
        table.addCell(cell13);

        table.addCell(cell21);
        table.addCell(cell22);
        table.addCell(cell23);

        table.addCell(cell32);
        table.addCell(cell33);
        table.addCell(cell34);

        document.add(table);

        document.close();
    }

    private void compareDocuments(String out, String cmp, boolean visuallyOnly) throws DocumentException, InterruptedException, IOException, ParserConfigurationException, SAXException {
        CompareTool compareTool = new CompareTool();
        if (visuallyOnly) {
            addError(compareTool.compare(outFolder + out, cmpFolder + cmp, outFolder, "diff"));
        } else {
            addError(compareTool.compareByContent(outFolder + out, cmpFolder + cmp, outFolder, "diff"));
            addError(compareTool.compareTagStructures(outFolder + out, cmpFolder + cmp));
        }
        if (errorMessage != null)
            Assert.fail(errorMessage);
    }

    private void addError(String error) {
        if (error != null && error.length() > 0) {
            if (errorMessage == null)
                errorMessage = "";
            else
                errorMessage += "\n";

            errorMessage += error;
        }
    }
}
