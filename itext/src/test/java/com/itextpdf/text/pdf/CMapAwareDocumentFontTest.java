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
 * Created on Nov 2, 2011
 * (c) 2011 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.parser.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Kevin
 */
public class CMapAwareDocumentFontTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWidths() throws Exception{
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "fontwithwidthissue.pdf");

        try {
            PdfDictionary fontsDic = pdfReader.getPageN(1).getAsDict(PdfName.RESOURCES).getAsDict(PdfName.FONT);
            PRIndirectReference fontDicIndirect = (PRIndirectReference)fontsDic.get(new PdfName("F1"));
            
            CMapAwareDocumentFont f = new CMapAwareDocumentFont(fontDicIndirect);
            Assert.assertTrue("Width should not be 0", f.getWidth('h') != 0);
        } finally {
            pdfReader.close();
        }
    }
    
    @Test
    public void illegalDifference() throws IOException {
        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, "illegalDifference.pdf");
        // this call will throw an exception and make the test fail if we remove the error-catching code
        PdfTextExtractor.getTextFromPage(reader, 1);
    }

    @Test
    public void weirdHyphensTest() throws IOException {
        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, "WeirdHyphens.pdf");
        ArrayList<String> textChunks = new ArrayList<String>();
        RenderListener listener
                = new MyTextRenderListener(textChunks);
        PdfContentStreamProcessor processor
                = new PdfContentStreamProcessor(listener);
        PdfDictionary pageDic = reader.getPageN(1);
        PdfDictionary resourcesDic
                = pageDic.getAsDict(PdfName.RESOURCES);
        processor.processContent(ContentByteUtils
                .getContentBytesForPage(reader, 1), resourcesDic);
        /**
         * This assertion makes sure that encoding has been read properly from FontDescriptor.
         * If not the vallue will be "\u0000 14".
         */
        Assert.assertEquals("\u0096 14", textChunks.get(18));
        reader.close();
    }

    static class MyTextRenderListener implements RenderListener {

        ArrayList<String> textChunks;

        MyTextRenderListener(ArrayList<String> textChunks) {
            this.textChunks = textChunks;
        }

        public void beginTextBlock() {
        }

        public void endTextBlock() {
        }

        public void renderImage(ImageRenderInfo renderInfo) {
        }

        public void renderText(TextRenderInfo renderInfo) {
            textChunks.add(renderInfo.getText());
        }
    }


}
