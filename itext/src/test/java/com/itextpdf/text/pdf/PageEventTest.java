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
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PageEventTest {
    private static String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/PageEventTest/";
    private static String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/PageEventTest/";

    @BeforeClass
    public static void init() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void pageEventTest01() throws IOException, DocumentException, InterruptedException {
        String fileName = "pageEventTest01.pdf";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.LETTER, 144, 144, 144, 144);
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setPageEvent(new MyEventHandler());

        writer.setTagged();
        doc.open();

        Chunk c = new Chunk("This is page 1");
        doc.add(c);

        doc.close();

        FileOutputStream fos = new FileOutputStream(OUTPUT_FOLDER + fileName);
        fos.write(baos.toByteArray());
        baos.close();
        fos.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + fileName, CMP_FOLDER + fileName, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }


    private class MyEventHandler extends PdfPageEventHelper {
        private PdfPTable _header;
        private PdfPTable _footer;

        public MyEventHandler() throws DocumentException {
            _header = new PdfPTable(1);
            PdfPCell hCell = new PdfPCell(new Phrase("HEADER"));
            hCell.setBorder(Rectangle.NO_BORDER);
            _header.addCell(hCell);
            _header.setTotalWidth(new float[]{300f});

            _footer = new PdfPTable(1);
            PdfPCell fCell = new PdfPCell(new Phrase("FOOTER"));
            fCell.setBorder(Rectangle.NO_BORDER);
            _footer.addCell(fCell);
            _footer.setTotalWidth(new float[]{300f});
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            super.onStartPage(writer, document);
            writeHeader(writer);
        }

        public void onEndPage(PdfWriter writer, Document document) {
            super.onEndPage(writer, document);
            writeFooter(writer);
        }

        private void writeHeader(PdfWriter writer) {
            writer.getDirectContent().saveState();
            _header.writeSelectedRows(0, _header.getRows().size(), 72, writer.getPageSize().getHeight() - 72, writer.getDirectContent());
            writer.getDirectContent().restoreState();
        }

        private void writeFooter(PdfWriter writer) {
            writer.getDirectContent().saveState();
            _footer.writeSelectedRows(0, _footer.getRows().size(), 72, 72, writer.getDirectContent());
            writer.getDirectContent().restoreState();
        }
    }
}
