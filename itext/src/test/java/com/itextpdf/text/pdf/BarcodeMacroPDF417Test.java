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
import com.itextpdf.testutils.ITextTest;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

public class BarcodeMacroPDF417Test extends ITextTest {

    private static final String CMP_DIR = "./src/test/resources/com/itextpdf/text/pdf/BarcodeMacroPDF417Test/";
    private static final String OUT_DIR = "./target/com/itextpdf/test/pdf/BarcodeMacroPDF417Test/";

    @Before
    public void setUp() {
        new File(OUT_DIR).mkdirs();
    }

    @Override
    protected void makePdf(String outPdf) throws Exception {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPdf));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        Image img = createBarcode(cb, "This is PDF417 segment 0", 1, 1, 0);
        document.add(new Paragraph("This is PDF417 segment 0"));
        document.add(img);

        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));
        document.add(new Paragraph("\u00a0"));

        img = createBarcode(cb, "This is PDF417 segment 1", 1, 1, 1);
        document.add(new Paragraph("This is PDF417 segment 1"));
        document.add(img);
        document.close();
    }

    public Image createBarcode(PdfContentByte cb, String text, float mh, float mw, int segmentId) throws BadElementException {
        BarcodePDF417 pf = new BarcodePDF417();

        // MacroPDF417 setup
        pf.setOptions(BarcodePDF417.PDF417_USE_MACRO);
        pf.setMacroFileId("12");
        pf.setMacroSegmentCount(2);
        pf.setMacroSegmentId(segmentId);

        pf.setText(text);
        Rectangle size = pf.getBarcodeSize();
        PdfTemplate template = cb.createTemplate(mw * size.getWidth(), mh * size.getHeight());
        pf.placeBarcode(template, BaseColor.BLACK, mh, mw);
        return Image.getInstance(template);
    }

    @Test
    public void test() throws Exception {
        runTest();
    }

    @Override
    protected void comparePdf(String outPdf, String cmpPdf) throws Exception {
        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outPdf, cmpPdf, OUT_DIR, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Override
    protected String getOutPdf() {
        return OUT_DIR + "barcode_macro_pdf417.pdf";
    }

    @Override
    protected String getCmpPdf() {
        return CMP_DIR + "cmp_barcode_macro_pdf417.pdf";
    }
}
