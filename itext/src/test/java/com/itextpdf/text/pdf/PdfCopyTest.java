/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;

/**
 * @author kevin
 */
public class PdfCopyTest {

    @Before
    public void setUp() throws Exception {
        TestResourceUtils.purgeTempFiles();
    }

    @After
    public void tearDown() throws Exception {
        TestResourceUtils.purgeTempFiles();
    }

    @Test
    /**
     * Test to demonstrate issue https://sourceforge.net/tracker/?func=detail&aid=3013642&group_id=15255&atid=115255
     */
    public void testExtraXObjects() throws Exception {
        PdfReader sourceR = new PdfReader(createImagePdf());
        try{
		        int sourceXRefCount = sourceR.getXrefSize();
		        
		        final Document document = new Document();
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        PdfCopy copy = new PdfCopy(document, out);
		        document.open();
		        PdfImportedPage importedPage = copy.getImportedPage(sourceR, 1);
		        copy.addPage(importedPage);
		        document.close();
		        
		        PdfReader targetR = new PdfReader(out.toByteArray());
		        int destinationXRefCount = targetR.getXrefSize();
		        
		//        TestResourceUtils.saveBytesToFile(createImagePdf(), new File("./source.pdf"));
		//        TestResourceUtils.saveBytesToFile(out.toByteArray(), new File("./result.pdf"));
		        
		        Assert.assertEquals(sourceXRefCount, destinationXRefCount);
        } finally {
        	sourceR.close();
        }
    }
    
    private static byte[] createImagePdf() throws Exception {

        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.setPageSize(PageSize.LETTER);

        document.open();
        
        BufferedImage awtImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB); 
        Graphics2D g2d = awtImg.createGraphics();
        g2d.setColor(Color.green);
        g2d.fillRect(10, 10, 80, 80);
        g2d.dispose();
        
        com.itextpdf.text.Image itextImg = com.itextpdf.text.Image.getInstance(awtImg, null);
        document.add(itextImg);               

        document.close();

        final byte[] pdfBytes = byteStream.toByteArray();

        return pdfBytes;
    }
}
