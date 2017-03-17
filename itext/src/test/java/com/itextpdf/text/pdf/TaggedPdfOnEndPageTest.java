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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class TaggedPdfOnEndPageTest {

    private static String CMP_FOLDER ="./src/test/resources/com/itextpdf/text/pdf/TaggedPdfOnEndPageTest/";
    private static String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/TaggedPdfOnEndPageTest/";

    @BeforeClass
    public static void init() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void test() throws IOException, DocumentException, InterruptedException {
        String file = "tagged_pdf_end_page.pdf";

        Document document = new Document();
        final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + file));
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        writer.setTagged();
        writer.createXmpMetadata();
        document.setMargins(10, 10, 60, 10);

        PdfPTable headerTable = new PdfPTable();

        final PdfPageHeader header = new PdfPageHeader(writer, 10, headerTable){
            public PdfPTable createTable(int pageNumber, Image total) {
                PdfPTable table = new PdfPTable(3);
                table.setTotalWidth(500);
                table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                table.addCell(new Phrase("Header"));
                table.addCell(new Phrase(String.format("Page %d of ", pageNumber)));
                PdfPCell pageTotal =  new PdfPCell(total);
                pageTotal.setBorder(Rectangle.NO_BORDER);
                table.addCell(pageTotal);
                return table;
            }
        };


        writer.setPageEvent(header);

        document.open();

        PdfPTable table = createContent();
        document.add(table);

        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    private PdfPTable createContent() {
        PdfPTable table = new PdfPTable(4);
        table.setHeaderRows(1);
        table.setWidthPercentage(100f);
        for (int i = 1; i <= 4; i++) {
            table.addCell(new PdfPCell(new Phrase("#" + i)));
        }
        for (int i = 0; i < 200; i++) {
            fillRow(table);
        }
        return table;
    }

    private void fillRow(PdfPTable table) {
        for (int j = 0; j < 3; j++) {
            Phrase phrase = new Phrase("value");
            PdfPCell cell = new PdfPCell(phrase);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    public abstract class PdfPageHeader extends PdfPageEventHelper {

        private final float marginTop;
        private java.util.List<PdfTemplate> templates = new ArrayList<PdfTemplate>();
        private PdfPTable headerTable;

        public PdfPageHeader(final PdfWriter writer, final float marginTop, PdfPTable headerTable)  {
            this.marginTop = marginTop;
            this.headerTable = headerTable;
        }

        public void onStartPage(final PdfWriter writer, final Document document) {
            PdfContentByte canvas = writer.getDirectContentUnder();

            Rectangle rect = document.getPageSize();
            PdfTemplate template = canvas.createTemplate(20, 16);
            Image total = null;
            try {
                total = Image.getInstance(template);
            } catch (BadElementException e) {
                e.printStackTrace();
            }
            total.setAccessibleAttribute(PdfName.ALT, new PdfString("Total"));
            templates.add(template);

            PdfPTable table = createTable(writer.getPageNumber(), total);
            if (table != null) {
                canvas.openMCBlock(headerTable);
                canvas.openMCBlock(headerTable.getBody());
                table.writeSelectedRows(0, -1, document.leftMargin(),
                        rect.getTop(marginTop), canvas);
                canvas.closeMCBlock(headerTable.getBody());
                canvas.closeMCBlock(headerTable);
            }
        }

        public void onCloseDocument(final PdfWriter writer, final Document document) {
            Font font = new Font(Font.FontFamily.COURIER, 15);
            Phrase phrase = new Phrase(String.valueOf(templates.size()), font);
            for(PdfTemplate template : templates){
                ColumnText.showTextAligned(template, Element.ALIGN_LEFT, phrase, 2, 2, 0);
            }
        }

        public abstract PdfPTable createTable(final int pageNumber, final Image total) ;

    }

}
