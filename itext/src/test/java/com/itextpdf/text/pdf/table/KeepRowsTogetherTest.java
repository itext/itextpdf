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
        createDocument(file, 1, 10, "Header for table 2 (should be on page 1, not a header, just first row)", false, false);
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