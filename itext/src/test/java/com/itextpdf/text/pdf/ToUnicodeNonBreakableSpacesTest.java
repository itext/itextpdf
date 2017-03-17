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

import com.itextpdf.text.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;

/**
 *
 */
public class ToUnicodeNonBreakableSpacesTest {

    private BaseFont fontWithToUnicode;

    @Before
    public void setUp() throws Exception {
        PdfReader reader = new PdfReader(
            ToUnicodeNonBreakableSpacesTest.class.getResourceAsStream("/com/itextpdf/text/pdf/ToUnicodeNonBreakableSpacesTest/fontWithToUnicode.pdf"));
        PdfDictionary resourcesDict = reader.getPageResources(1);
        PdfDictionary fontsDict = resourcesDict.getAsDict(PdfName.FONT);
        for (PdfName key : fontsDict.getKeys()) {
            PdfObject pdfFont = fontsDict.get(key);

            if (pdfFont instanceof PRIndirectReference) {
                fontWithToUnicode = BaseFont.createFont((PRIndirectReference) pdfFont);
                break;
            }
        }
    }

    @Test
    public void writeTextWithWordSpacing() throws Exception {
        Document document = new Document();
        FileOutputStream out = new FileOutputStream("target/test-classes/com/itextpdf/text/pdf/ToUnicodeNonBreakableSpacesTest/textWithWorldSpacing.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        document.setPageSize(PageSize.A4);
        document.newPage();
        writer.getDirectContent().setWordSpacing(10);
        ColumnText columnText = new ColumnText(writer.getDirectContent());
        columnText.setSimpleColumn(new Rectangle(30, 0, document.getPageSize().getRight(), document.getPageSize().getTop() - 30));
        columnText.setUseAscender(true);
        columnText.addText(new Chunk("H H H H H H H H H  !", new Font(fontWithToUnicode, 30)));
        columnText.go();
        document.close();
    }
}
