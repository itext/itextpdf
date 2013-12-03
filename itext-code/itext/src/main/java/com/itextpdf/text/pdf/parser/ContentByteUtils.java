/*
 * $Id: ContentByteUtils.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
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
import java.io.IOException;
import java.util.ListIterator;

import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

/**
 * @author kevin
 * @since 5.0.1
 */
public class ContentByteUtils {
    private ContentByteUtils() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Gets the content bytes from a content object, which may be a reference
     * a stream or an array.
     * @param contentObject the object to read bytes from
     * @return the content bytes
     * @throws IOException
     */
    public static byte[] getContentBytesFromContentObject(final PdfObject contentObject) throws IOException {
        final byte[] result;
        switch (contentObject.type())
        {
            case PdfObject.INDIRECT:
                final PRIndirectReference ref = (PRIndirectReference) contentObject;
                final PdfObject directObject = PdfReader.getPdfObjectRelease(ref);
                result = getContentBytesFromContentObject(directObject);
                break;
            case PdfObject.STREAM:
                final PRStream stream = (PRStream) PdfReader.getPdfObjectRelease(contentObject);
                result = PdfReader.getStreamBytes(stream);
                break;
            case PdfObject.ARRAY:
                // Stitch together all content before calling processContent(), because
                // processContent() resets state.
                final ByteArrayOutputStream allBytes = new ByteArrayOutputStream();
                final PdfArray contentArray = (PdfArray) contentObject;
                final ListIterator<PdfObject> iter = contentArray.listIterator();
                while (iter.hasNext())
                {
                    final PdfObject element = iter.next();
                    allBytes.write(getContentBytesFromContentObject(element));
                    allBytes.write((byte)' ');
                }
                result = allBytes.toByteArray();
                break;
            default:
                final String msg = "Unable to handle Content of type " + contentObject.getClass();
            throw new IllegalStateException(msg);
        }
        return result;
    }
    
    /**
     * Gets the content bytes of a page from a reader
     * @param reader  the reader to get content bytes from
     * @param pageNum   the page number of page you want get the content stream from
     * @return  a byte array with the effective content stream of a page
     * @throws IOException
     * @since 5.0.1
     */
    public static byte[] getContentBytesForPage(PdfReader reader, int pageNum) throws IOException {
        final PdfDictionary pageDictionary = reader.getPageN(pageNum);
        final PdfObject contentObject = pageDictionary.get(PdfName.CONTENTS);
        if (contentObject == null)
            return new byte[0];
        
        final byte[] contentBytes = ContentByteUtils.getContentBytesFromContentObject(contentObject);
        return contentBytes;
    }

}
