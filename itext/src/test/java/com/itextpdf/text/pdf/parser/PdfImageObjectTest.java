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
/*
 * Created on Sep 2, 2011
 * (c) 2011 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

/**
 * @author kevin
 */
public class PdfImageObjectTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    private void testFile(String filename, int page, String objectid) throws Exception{
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, filename);
        try{
            PdfDictionary resources = pdfReader.getPageResources(page);
            PdfDictionary xobjects = resources.getAsDict(PdfName.XOBJECT);
            PdfIndirectReference objRef = xobjects.getAsIndirectObject(new PdfName(objectid));
            if (objRef == null)
                throw new NullPointerException("Reference " + objectid + " not found - Available keys are " + xobjects.getKeys());
            PRStream stream = (PRStream)PdfReader.getPdfObject(objRef);
            PdfDictionary colorSpaceDic = resources != null ? resources.getAsDict(PdfName.COLORSPACE) : null;
            PdfImageObject img = new PdfImageObject(stream, colorSpaceDic);
            byte[] result = img.getImageAsBytes();
            Assert.assertNotNull(result);
            int zeroCount = 0;
            for (byte b : result) {
                if (b == 0) zeroCount++;
            }
            Assert.assertTrue(zeroCount > 0);
        } finally {
            pdfReader.close();
        }
    }
    
    @Test
    public void testMultiStageFilters() throws Exception{
        testFile("multistagefilter1.pdf", 1, "Obj13");
    }

    @Test
    public void testAscii85Filters() throws Exception{
        testFile("ASCII85_RunLengthDecode.pdf", 1, "Im9");
    }

    @Test
    public void testCcittFilters() throws Exception{
        testFile("ccittfaxdecode.pdf", 1, "background0");
    }

    @Test
    public void testFlateDecodeFilters() throws Exception{
        testFile("flatedecode_runlengthdecode.pdf", 1, "Im9");
    }

    @Test
    public void testDctDecodeFilters() throws Exception{
        testFile("dctdecode.pdf", 1, "im1");
    }
    
    @Test
    public void testjbig2Filters() throws Exception{
        testFile("jbig2decode.pdf", 1, "2");
    }
    
}
