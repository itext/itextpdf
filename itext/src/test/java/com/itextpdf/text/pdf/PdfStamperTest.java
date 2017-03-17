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
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class PdfStamperTest {

    private static final String RESOURCE_FOLDER ="./src/test/resources/com/itextpdf/text/pdf/PdfStamperTest/";
    private static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/PdfStamperTest/";

    @Before
    public void setUp() {
        new File(DEST_FOLDER).mkdirs();
    }

    @Test
    public void setPageContentTest01() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out1.pdf";
        String testFile = RESOURCE_FOLDER + "in.pdf";
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        reader.eliminateSharedStreams();
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            byte[] bb = reader.getPageContent(i);
            reader.setPageContent(i, bb);
        }
        stamper.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, RESOURCE_FOLDER + "cmp_out1.pdf", DEST_FOLDER, "diff_"));
    }

    @Test
    public void setPageContentTest02() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out2.pdf";
        String testFile = RESOURCE_FOLDER + "in.pdf";
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        int total = reader.getNumberOfPages() + 1;
        for (int i = 1; i < total; i++) {
            byte[] bb = reader.getPageContent(i);
            reader.setPageContent(i, bb);
        }
        reader.removeUnusedObjects();
        stamper.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, RESOURCE_FOLDER + "cmp_out2.pdf", DEST_FOLDER, "diff_"));
    }

    @Test
    public void layerStampingTest() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out3.pdf";
        String testFile = RESOURCE_FOLDER + "House_Plan_Final.pdf";
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));

        PdfLayer logoLayer = new PdfLayer("Logos", stamper.getWriter());
        PdfContentByte cb = stamper.getUnderContent(1);
        cb.beginLayer(logoLayer);

        Image iImage = Image.getInstance(RESOURCE_FOLDER + "Willi-1.jpg");
        iImage.scalePercent(24f);
        iImage.setAbsolutePosition(100, 100);
        cb.addImage(iImage);

        cb.endLayer();
        stamper.close();

        Assert.assertNull(new CompareTool().compareByContent(outPdf, getClass().getResource("PdfStamperTest/cmp_House_Plan_Final.pdf").getPath(), DEST_FOLDER, "diff_"));
    }
    
    @Test
    public void fixEmptyOCGsTest() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "out4.pdf";
        String testFile = RESOURCE_FOLDER + "EmptyOCGs.pdf";
        PdfReader reader = new PdfReader(testFile);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        stamper.getPdfLayers();
        stamper.close();
        
        Assert.assertNull(new CompareTool().compareByContent(outPdf, getClass().getResource("PdfStamperTest/cmp_EmptyOCGs.pdf").getPath(), DEST_FOLDER, "diff_"));

    }

}
