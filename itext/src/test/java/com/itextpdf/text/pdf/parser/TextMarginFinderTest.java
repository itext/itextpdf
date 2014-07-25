/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
package com.itextpdf.text.pdf.parser;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author kevin
 */
public class TextMarginFinderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBasics() throws Exception{
        Rectangle rToDraw = new Rectangle(1.42f*72f, 2.42f*72f, 7.42f*72f, 10.42f*72f);
        rToDraw.setBorder(Rectangle.BOX);
        rToDraw.setBorderWidth(1.0f);
        
        byte[] content = createPdf(rToDraw);
        //TestResourceUtils.openBytesAsPdf(content);
        
        TextMarginFinder finder = new TextMarginFinder();

        new PdfReaderContentParser(new PdfReader(content)).processContent(1, finder);
        
        Assert.assertEquals(1.42f*72f, finder.getLlx(), 0.01f);
        Assert.assertEquals(7.42f*72f, finder.getUrx(), 0.01f);
        Assert.assertEquals(2.42f*72f, finder.getLly(), 0.01f);
        Assert.assertEquals(10.42f*72f, finder.getUry(), 0.01f);

    }
    
    private byte[] createPdf(Rectangle recToDraw) throws DocumentException, IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();
        
        
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        float fontsiz = 12;
        
        float llx = 1.42f * 72f;
        float lly = 2.42f * 72f;
        float urx = 7.42f * 72f;
        float ury = 10.42f * 72f;

        BaseFont font = BaseFont.createFont();
        canvas.setFontAndSize(font, fontsiz);

        float ascent = font.getFontDescriptor(BaseFont.ASCENT, fontsiz);
        float descent = font.getFontDescriptor(BaseFont.DESCENT, fontsiz);

        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "LowerLeft", llx, lly-descent, 0.0f);
        canvas.showTextAligned(PdfContentByte.ALIGN_RIGHT, "LowerRight", urx, lly-descent, 0.0f);
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "UpperLeft", llx, ury - ascent, 0.0f);
        canvas.showTextAligned(PdfContentByte.ALIGN_RIGHT, "UpperRight", urx, ury - ascent, 0.0f);
        canvas.endText();
        
        if (recToDraw != null){
            doc.add(recToDraw);
        }
        
        doc.close();
        
        return baos.toByteArray();
    }
}
