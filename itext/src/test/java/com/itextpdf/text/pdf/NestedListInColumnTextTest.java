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
package com.itextpdf.text.pdf;


import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.RomanList;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NestedListInColumnTextTest {
    public static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/NestedListInColumnTextTest/";
    public static final String SOURCE_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/NestedListInColumnTextTest/";

    @Before
    public void Init() throws IOException {
        File dir = new File(DEST_FOLDER);
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
        } else
            dir.mkdirs();
    }

    //SUP-879 Nested List items not displaying properly
    @Test
    public void nestedListAtTheEndOfAnotherNestedList() throws DocumentException, IOException, InterruptedException {
        String pdfFile = "nestedListAtTheEndOfAnotherNestedList.pdf";
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(DEST_FOLDER + pdfFile));
        // step 3
        document.open();
        // step 4
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.ORANGE);

        RomanList romanlist = new RomanList(true, 20);
        romanlist.setIndentationLeft(10f);
        romanlist.add("One");
        romanlist.add("Two");
        romanlist.add("Three");

        RomanList romanlist2 = new RomanList(true, 20);
        romanlist2.setIndentationLeft(10f);
        romanlist2.add("One");
        romanlist2.add("Two");
        romanlist2.add("Three");

        romanlist.add(romanlist2);
        //romanlist.add("Four");

        com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED, 20f);
        list.setListSymbol("\u2022");
        list.setIndentationLeft(20f);
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        list.add("Roman List");
        list.add(romanlist);

        list.add("Five");
        list.add("Six");

        cell.addElement(list);
        table.addCell(cell);
        document.add(table);
        // step 5
        document.close();

        CompareTool compareTool = new CompareTool();
        String error = compareTool.compareByContent(DEST_FOLDER + pdfFile, SOURCE_FOLDER + pdfFile, DEST_FOLDER, "diff_");
        if (error != null) {
            Assert.fail(error);
        }
    }

}
