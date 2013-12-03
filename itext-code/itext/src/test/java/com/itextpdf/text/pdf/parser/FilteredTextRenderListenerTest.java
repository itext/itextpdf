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
package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author kevin
 */
public class FilteredTextRenderListenerTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRegion() throws Exception {
        byte[] pdf = createPdfWithCornerText();
        
        PdfReader reader = new PdfReader(pdf);
        float pageHeight = reader.getPageSize(1).getHeight();
        Rectangle upperLeft = new Rectangle(0, (int)pageHeight-30, 250, (int)pageHeight);
        
        Assert.assertTrue(textIsInRectangle(reader, "Upper Left", upperLeft));
        Assert.assertFalse(textIsInRectangle(reader, "Upper Right", upperLeft));
    }
    
    private boolean textIsInRectangle(PdfReader reader, String text, Rectangle rect) throws Exception{
        
        FilteredTextRenderListener filterListener = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), new RegionTextRenderFilter(rect) );
        
        String extractedText = PdfTextExtractor.getTextFromPage(reader, 1, filterListener);
        
        return extractedText.equals(text);
    }
    
    private byte[] createPdfWithCornerText() throws Exception{
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.open();
        
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();

        canvas.setFontAndSize(BaseFont.createFont(), 12);
        
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "Upper Left", 10, document.getPageSize().getHeight() - 10 - 12, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Upper Right", document.getPageSize().getWidth() - 10, document.getPageSize().getHeight() - 10 - 12, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "Lower Left", 10, 10, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Lower Right", document.getPageSize().getWidth() - 10, 10, 0);
        
        canvas.endText();

        document.close();

        final byte[] pdfBytes = byteStream.toByteArray();

        return pdfBytes;

    }
}
