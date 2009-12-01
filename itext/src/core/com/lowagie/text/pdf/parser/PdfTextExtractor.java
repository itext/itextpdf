/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.lowagie.text.pdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ListIterator;

import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.RandomAccessFileOrArray;

/**
 * Extracts text from a PDF file.
 * @since	2.1.4
 */
public class PdfTextExtractor {

	/** The PdfReader that holds the PDF file. */
    private final PdfReader reader;
    
    /** The {@link TextProvidingRenderListener} that will receive render notifications and provide resultant text */
    private final TextProvidingRenderListener renderListener;
    
    /**
     * Creates a new Text Extractor object, using a {@link SimpleTextExtractingPdfContentRenderListener} as the render listener
     * @param reader	the reader with the PDF
     */
    public PdfTextExtractor(PdfReader reader) {
        this(reader, new SimpleTextExtractingPdfContentRenderListener());
    }

    /**
     * Creates a new Text Extractor object.
     * @param reader    the reader with the PDF
     * @param renderListener the render listener that will be used to analyze renderText operations and provide resultant text
     */
    public PdfTextExtractor(PdfReader reader, TextProvidingRenderListener renderListener) {
        this.reader = reader;
        this.renderListener = renderListener;
    }
    
    /**
     * Gets the content bytes of a page.
     * @param pageNum	the page number of page you want get the content stream from
     * @return	a byte array with the effective content stream of a page
     * @throws IOException
     */
    private byte[] getContentBytesForPage(int pageNum) throws IOException {
        RandomAccessFileOrArray f = reader.getSafeFile();
        try{
            final PdfDictionary pageDictionary = reader.getPageN(pageNum);
            final PdfObject contentObject = pageDictionary.get(PdfName.CONTENTS);
            final byte[] contentBytes = getContentBytesFromContentObject(contentObject);
            return contentBytes;
        } finally {    
            f.close();
        }
    }
    
    /**
     * Gets the content bytes from a content object, which may be a reference
     * a stream or an array.
     * @param contentObject the object to read bytes from
     * @return the content bytes
     * @throws IOException
     */
    private byte[] getContentBytesFromContentObject(final PdfObject contentObject) throws IOException {
          final byte[] result;
          switch (contentObject.type())
          {
            case PdfObject.INDIRECT:
              final PRIndirectReference ref = (PRIndirectReference) contentObject;
              final PdfObject directObject = PdfReader.getPdfObject(ref);
              result = getContentBytesFromContentObject(directObject);
              break;
            case PdfObject.STREAM:
              final PRStream stream = (PRStream) PdfReader.getPdfObject(contentObject);
              result = PdfReader.getStreamBytes(stream);
              break;
            case PdfObject.ARRAY:
              // Stitch together all content before calling processContent(), because
              // processContent() resets state.
              final ByteArrayOutputStream allBytes = new ByteArrayOutputStream();
              final PdfArray contentArray = (PdfArray) contentObject;
              final ListIterator iter = contentArray.listIterator();
              while (iter.hasNext())
              {
                final PdfObject element = (PdfObject) iter.next();
                allBytes.write(getContentBytesFromContentObject(element));
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
     * Gets the text from a page.
     * @param page	the page number of the page
     * @return	a String with the content as plain text (without PDF syntax)
     * @throws IOException
     */
    public String getTextFromPage(int page) throws IOException {
        PdfDictionary pageDic = reader.getPageN(page);
        PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
        
        renderListener.reset();
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(renderListener);
        processor.processContent(getContentBytesForPage(page), resourcesDic);        
        return renderListener.getResultantText();
    }
}
