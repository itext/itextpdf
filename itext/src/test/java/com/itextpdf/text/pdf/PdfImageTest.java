/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfImageTest {
    private static final String target = "./target/com/itextpdf/test/pdf/PdfImageTest/";
    private static final String source = "./src/test/resources/com/itextpdf/text/pdf/PdfImageTest/";

    @BeforeClass
    public static void setUp() {
        new File(target).mkdirs();
    }

    @Test
    public void pngColorProfileTest() throws DocumentException, InterruptedException, IOException {
        simpleImageTest("pngColorProfileImage.pdf", "test_icc.png");
    }

    @Test
    public void pngColorProfilePalletTest() throws DocumentException, InterruptedException, IOException {
        simpleImageTest("pngColorProfilePalletImage.pdf", "test_icc_pallet.png");
    }

    @Test
    public void pngIncorrectColorProfileTest() throws DocumentException, InterruptedException, IOException {
        simpleImageTest("pngIncorrectProfileImage.pdf", "test_incorrect_icc.png");
    }

    private void simpleImageTest(String fileName, String imageName) throws IOException, DocumentException, InterruptedException {
        String outPath = target + fileName;
        String cmpPath = source + "cmp_" + fileName;
        String imgPath = source + imageName;
        String diff = "diff_" + fileName + "_";

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));

        document.open();
        Image image = Image.getInstance(imgPath);
        image.scaleToFit(new Rectangle(document.left(), document.bottom(), document.right(), document.top()));
        document.add(image);
        document.close();
        writer.close();

        Assert.assertNull(new CompareTool().compareByContent(outPath, cmpPath, target, diff));
    }
}
