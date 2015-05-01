package com.itextpdf.text.pdf.table;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.Assert;

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