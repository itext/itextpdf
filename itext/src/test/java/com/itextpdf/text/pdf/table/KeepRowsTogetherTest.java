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
import java.io.FileNotFoundException;
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

    /**
     * Creates two tables and both should be on their own page.
     *
     * @throws DocumentException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testKeepRowsTogetherInCombinationWithHeaders() throws DocumentException, IOException, InterruptedException {
        final String file = "withheaders.pdf";
        createDocument(file, 0, 10, "Header for table 2", true, false);
        compareDocuments(file);
    }

    /**
     * Creates two tables and the second table should have one row on pae 1 and every other row on page 2.
     *
     * @throws DocumentException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testKeepRowsTogetherWithoutHeader() throws DocumentException, IOException, InterruptedException {
        final String file = "withoutheader.pdf";
        createDocument(file, 1, 10, "Header for table 2 (should be on page 1)", false, false);
        compareDocuments(file);
    }

    /**
     * Creates two tables. The second table has 1 header row and it should skip the first header. 1 line of table 2 should be on page 1, the rest on page 2.
     *
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    @Test
    public void testKeepRowsTogetherInCombinationWithSkipFirstHeader() throws FileNotFoundException, DocumentException {
        final String file = "withskipfirstheader.pdf";
        createDocument(file, 2, 10, "Header for Table 2", true, true);
    }

    /**
     * Utility method
     *
     * @param file fileName
     * @param start start index for the keepRowsTogether method
     * @param end end index for the keepRowsTogether method
     * @param header2Text text for the second table's header
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    private void createDocument(final String file, final int start, final int end, final String header2Text, final boolean headerRows, final boolean skipFirstHeader) throws FileNotFoundException, DocumentException {
        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(outFolder + file));

        document.open();

        document.add(createTable("Header for table 1", true, 1, skipFirstHeader));

        PdfPTable table = createTable(header2Text, headerRows, 2, skipFirstHeader);
        table.keepRowsTogether(start, end);
        document.add(table);

        document.close();
    }

    /**
     * Utility ethod that creates a table with 41 rows. One of which may or may not be a header.
     *
     * @param headerText text for the first cell
     * @param headerRows is the first row a header
     * @param tableNumber number of the table
     * @return PdfPTable
     */
    private PdfPTable createTable(final String headerText, final boolean headerRows, final int tableNumber, final boolean skipFirstHeader) {
        PdfPTable table = new PdfPTable(1);

        PdfPCell cell1 = new PdfPCell(new Paragraph(headerText));
        table.addCell(cell1);

        if ( headerRows ) {
            table.setHeaderRows(1);

            if ( skipFirstHeader ) {
                table.setSkipFirstHeader(skipFirstHeader);
            }
        }

        for (int i = 0; i < 40; i++) {
            table.addCell("Tab " + tableNumber + ", line " + i);
        }

        return table;
    }

    /**
     * Utility method that checks the created file against the cmp file
     * @param file name of the output document
     * @throws DocumentException
     * @throws InterruptedException
     * @throws IOException
     */
    private void compareDocuments(final String file) throws DocumentException, InterruptedException, IOException {
        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outFolder + file, cmpFolder + file, outFolder, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}