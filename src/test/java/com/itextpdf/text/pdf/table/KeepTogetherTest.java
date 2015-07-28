package com.itextpdf.text.pdf.table;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Raf Hens
 */
public class KeepTogetherTest {

    private String cmpFolder = "./src/test/resources/com/itextpdf/text/pdf/table/keeptogether/";;
    private String outFolder = "./target/com/itextpdf/text/pdf/table/keeptogether/";

    @Before
    public void setUp() throws Exception {
        new File(outFolder).mkdirs();
        TestResourceUtils.purgeTempFiles();
    }

    @Test
    public void testKeepTogether1() throws DocumentException, IOException, InterruptedException {
        testKeepTogether(true, true);
        compareDocuments(true, true);
    }

    @Test
    public void testKeepTogether2() throws DocumentException, IOException, InterruptedException {
        testKeepTogether(true, false);
        compareDocuments(true, false);
    }
    
    @Test
    public void testKeepTogether3() throws DocumentException, IOException, InterruptedException {
        testKeepTogether(false, true);
        compareDocuments(false, true);
    }
    
    @Test
    public void testKeepTogether4() throws DocumentException, IOException, InterruptedException {
        testKeepTogether(false, false);
        compareDocuments(false, false);
    }

    @Test
    public void testSplitLateAndSplitRow1() throws IOException, DocumentException, InterruptedException {
        String filename = "testSplitLateAndSplitRow1.pdf";
        Document doc = new Document(PageSize.LETTER, 72f, 72f, 72f, 72f);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outFolder + filename));
        doc.open();
        PdfContentByte canvas = writer.getDirectContent();

        ColumnText ct = new ColumnText(canvas);

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 21; ++i) {
            text.append(i).append("\n");
        }

        // Add a table with a single row and column that doesn't fit on one page
        PdfPTable t = new PdfPTable(1);
        t.setSplitLate(true);
        t.setSplitRows(true);
        t.setWidthPercentage(100f);

        ct.addElement(new Paragraph("Pushing table down\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\n"));

        PdfPCell c = new PdfPCell();
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setVerticalAlignment(Element.ALIGN_TOP);
        c.setBorder(Rectangle.NO_BORDER);
        c.setBorderWidth(0);
        c.setPadding(0);
        c.addElement(new Paragraph(text.toString()));
        t.addCell(c);

        ct.addElement(t);

        int status = 0;
        while (ColumnText.hasMoreText(status)) {
            ct.setSimpleColumn(doc.left(), doc.bottom(), doc.right(), doc.top());
            status = ct.go();

            if (ColumnText.hasMoreText(status))
                doc.newPage();
        }

        doc.close();

        String errorMessage = new CompareTool().compareByContent(outFolder + filename, cmpFolder + filename, outFolder, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testSplitLateAndSplitRow2() throws IOException, DocumentException, InterruptedException {
        String filename = "testSplitLateAndSplitRow2.pdf";
        Document doc = new Document(PageSize.LETTER, 72f, 72f, 72f, 72f);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outFolder + filename));
        doc.open();
        PdfContentByte canvas = writer.getDirectContent();

        ColumnText ct = new ColumnText(canvas);

        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 42; ++i) {
            text.append(i).append("\n");
        }

        // Add a table with a single row and column that doesn't fit on one page
        PdfPTable t = new PdfPTable(1);
        t.setSplitLate(true);
        t.setSplitRows(true);
        t.setWidthPercentage(100f);

        ct.addElement(new Paragraph("Pushing table down\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\ndown\n"));

        PdfPCell c = new PdfPCell();
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setVerticalAlignment(Element.ALIGN_TOP);
        c.setBorder(Rectangle.NO_BORDER);
        c.setBorderWidth(0);
        c.setPadding(0);
        c.addElement(new Paragraph(text.toString()));
        t.addCell(c);

        ct.addElement(t);

        int status = 0;
        while (ColumnText.hasMoreText(status)) {
            ct.setSimpleColumn(doc.left(), doc.bottom(), doc.right(), doc.top());
            status = ct.go();

            if (ColumnText.hasMoreText(status))
                doc.newPage();
        }

        doc.close();

        String errorMessage = new CompareTool().compareByContent(outFolder + filename, cmpFolder + filename, outFolder, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    public void testKeepTogether(final boolean tagged,
            final boolean keepTogether) throws FileNotFoundException, DocumentException {
        Document document = new Document();
            String file = "tagged_"+tagged+"-keeptogether_"+keepTogether+".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outFolder + file));
            if (tagged)
                writer.setTagged();
            document.open();
            int columns = 3;
            int tables = 3;
            for (int tableCount = 0; tableCount < tables; tableCount++) {
                PdfPTable table = new PdfPTable(columns); 
                for (int rowCount = 0; rowCount < 50; rowCount++) {                    
                    PdfPCell cell1 = new PdfPCell(new Paragraph("t" + tableCount + " r:" + rowCount));
                    PdfPCell cell2 = new PdfPCell(new Paragraph("t" + tableCount + " r:" + rowCount));
                    PdfPCell cell3 = new PdfPCell(new Paragraph("t" + tableCount + " r:" + rowCount));
                    table.addCell(cell1);
                    table.addCell(cell2);
                    table.addCell(cell3);
                }
                table.setSpacingAfter(10f);
                table.setKeepTogether(keepTogether);
                document.add(table);
            }
            document.close();
    }

    /**
     * Utility method that checks the created file against the cmp file
     * @param tagged Tagged document
     * @param keepTogether PdfPTable.setKeepTogether(true/false)
     * @throws DocumentException
     * @throws InterruptedException
     * @throws IOException
     */
    private void compareDocuments(final boolean tagged, final boolean keepTogether) throws DocumentException, InterruptedException, IOException {
        String file = "tagged_"+tagged+"-keeptogether_"+keepTogether+".pdf";
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outFolder + file, cmpFolder + file, outFolder, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}