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