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
import com.itextpdf.text.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class TextExpansionTest {

    public static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/TextExpansionTest/";
    public static final String SOURCE_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/TextExpansionTest/";


    @Before
    public void setUp() throws Exception {
        new File(DEST_FOLDER).mkdirs();
    }


    @Test
    public void imageTaggingExpansionTest() throws IOException, DocumentException, InterruptedException {
        String filename = "TextExpansionTest.pdf";
        Document doc = new Document(PageSize.LETTER, 72, 72, 72, 72);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(DEST_FOLDER+filename));
        writer.setTagged();
        doc.open();

        Chunk c1 = new Chunk("ABC");
        c1.setTextExpansion("the alphabet");
        Paragraph p1 = new Paragraph();
        p1.add(c1);
        doc.add(p1);

        PdfTemplate t = writer.getDirectContent().createTemplate(6, 6);
        t.setLineWidth(1f);
        t.circle(3f, 3f, 1.5f);
        t.setGrayFill(0);
        t.fillStroke();
        Image i = Image.getInstance(t);
        Chunk c2 = new Chunk(i, 100, -100);
        doc.add(c2);

        Chunk c3 = new Chunk("foobar");
        c3.setTextExpansion("bar bar bar");
        Paragraph p3 = new Paragraph();
        p3.add(c3);
        doc.add(p3);

        doc.close();


        CompareTool compareTool = new CompareTool();
        String error = compareTool.compareByContent(DEST_FOLDER + filename, SOURCE_FOLDER + "cmp_" + filename, DEST_FOLDER, "diff_");
        if (error != null) {
            Assert.fail(error);
        }
    }
}
