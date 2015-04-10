/*
 * $Id: FilterHandlers.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.TIFFFaxDecoder;
import com.itextpdf.text.pdf.codec.TIFFFaxDecompressor;

/**
 * Encapsulates filter behavior for PDF streams.  Classes generally interace with this
 * using the static getDefaultFilterHandlers() method, then obtain the desired {@link FilterHandler}
 * via a lookup.
 * @since 5.0.4
 */
// Dev note:  we eventually want to refactor PdfReader so all of the existing filter functionality is moved into this class
// it may also be better to split the sub-classes out into a separate package 
public final class FilterHandlers {
    
    /**
     * The main interface for creating a new {@link FilterHandler}
     */
    public static interface FilterHandler{
        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, final PdfDictionary streamDictionary) throws IOException;
    }
    
    /** The default {@link FilterHandler}s used by iText */
    private static final Map<PdfName, FilterHandler> defaults;
    static {
        HashMap<PdfName, FilterHandler> map = new HashMap<PdfName, FilterHandler>();
        
        map.put(PdfName.FLATEDECODE, new Filter_FLATEDECODE());
        map.put(PdfName.FL, new Filter_FLATEDECODE());
        map.put(PdfName.ASCIIHEXDECODE, new Filter_ASCIIHEXDECODE());
        map.put(PdfName.AHX, new Filter_ASCIIHEXDECODE());
        map.put(PdfName.ASCII85DECODE, new Filter_ASCII85DECODE());
        map.put(PdfName.A85, new Filter_ASCII85DECODE());
        map.put(PdfName.LZWDECODE, new Filter_LZWDECODE());
        map.put(PdfName.CCITTFAXDECODE, new Filter_CCITTFAXDECODE());
        map.put(PdfName.CRYPT, new Filter_DoNothing());
        map.put(PdfName.RUNLENGTHDECODE, new Filter_RUNLENGTHDECODE());
        
        defaults = Collections.unmodifiableMap(map);
    }
    
    /**
     * @return the default {@link FilterHandler}s used by iText
     */
    public static Map<PdfName, FilterHandler> getDefaultFilterHandlers(){
        return defaults;
    }
    
    /**
     * Handles FLATEDECODE filter
     */
    private static class Filter_FLATEDECODE implements FilterHandler{
        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            b = PdfReader.FlateDecode(b);
            b = PdfReader.decodePredictor(b, decodeParams);
            return b;
        }
    }
    
    /**
     * Handles ASCIIHEXDECODE filter
     */
    private static class Filter_ASCIIHEXDECODE implements FilterHandler{
        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            b = PdfReader.ASCIIHexDecode(b);
            return b;
        }
    }

    /**
     * Handles ASCIIHEXDECODE filter
     */
    private static class Filter_ASCII85DECODE implements FilterHandler{
        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            b = PdfReader.ASCII85Decode(b);
            return b;
        }
    }
    
    /**
     * Handles LZWDECODE filter
     */
    private static class Filter_LZWDECODE implements FilterHandler{
        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            b = PdfReader.LZWDecode(b);
            b = PdfReader.decodePredictor(b, decodeParams);
            return b;
        }
    }

    
    /**
     * Handles CCITTFAXDECODE filter
     */
    private static class Filter_CCITTFAXDECODE implements FilterHandler{
        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            PdfNumber wn = (PdfNumber)PdfReader.getPdfObjectRelease(streamDictionary.get(PdfName.WIDTH));
            PdfNumber hn = (PdfNumber)PdfReader.getPdfObjectRelease(streamDictionary.get(PdfName.HEIGHT));
            if (wn == null || hn == null)
                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("filter.ccittfaxdecode.is.only.supported.for.images"));
            int width = wn.intValue();
            int height = hn.intValue();
            
            PdfDictionary param = decodeParams instanceof PdfDictionary ? (PdfDictionary)decodeParams : null;
            int k = 0;
            boolean blackIs1 = false;
            boolean byteAlign = false;
            if (param != null) {
                PdfNumber kn = param.getAsNumber(PdfName.K);
                if (kn != null)
                    k = kn.intValue();
                PdfBoolean bo = param.getAsBoolean(PdfName.BLACKIS1);
                if (bo != null)
                    blackIs1 = bo.booleanValue();
                bo = param.getAsBoolean(PdfName.ENCODEDBYTEALIGN);
                if (bo != null)
                    byteAlign = bo.booleanValue();
            }
            byte[] outBuf = new byte[(width + 7) / 8 * height];
            TIFFFaxDecompressor decoder = new TIFFFaxDecompressor();
            if (k == 0 || k > 0) {
                int tiffT4Options = k > 0 ? TIFFConstants.GROUP3OPT_2DENCODING : 0;
                tiffT4Options |= byteAlign ? TIFFConstants.GROUP3OPT_FILLBITS : 0;
                decoder.SetOptions(1, TIFFConstants.COMPRESSION_CCITTFAX3, tiffT4Options, 0);
                decoder.decodeRaw(outBuf, b, width, height);
                if (decoder.fails > 0) {
                    byte[] outBuf2 = new byte[(width + 7) / 8 * height];
                    int oldFails = decoder.fails;
                    decoder.SetOptions(1, TIFFConstants.COMPRESSION_CCITTRLE, tiffT4Options, 0);
                    decoder.decodeRaw(outBuf2, b, width, height);
                    if (decoder.fails < oldFails) {
                        outBuf = outBuf2;
                    }
                }
            }
            else {
                TIFFFaxDecoder deca = new TIFFFaxDecoder(1, width, height);
                deca.decodeT6(outBuf, b, 0, height, 0);
            }
            if (!blackIs1) {
                int len = outBuf.length;
                for (int t = 0; t < len; ++t) {
                    outBuf[t] ^= 0xff;
                }
            }
            b = outBuf;       
            return b;
        }
    }
    
    /**
     * A filter that doesn't modify the stream at all
     */
    private static class Filter_DoNothing implements FilterHandler{
        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            return b;
        }
    }

    /**
     * Handles RUNLENGTHDECODE filter
     */
    private static class Filter_RUNLENGTHDECODE implements FilterHandler{

        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
         // allocate the output buffer
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte dupCount = -1;
            for(int i = 0; i < b.length; i++){
                dupCount = b[i];
                if (dupCount == -128) break; // this is implicit end of data
                
                if (dupCount >= 0 && dupCount <= 127){
                    int bytesToCopy = dupCount+1;
                    baos.write(b, i, bytesToCopy);
                    i+=bytesToCopy;
                } else {
                    // make dupcount copies of the next byte
                    i++;
                    for(int j = 0; j < 1-(int)(dupCount);j++){ 
                        baos.write(b[i]);
                    }
                }
            }
            
            return baos.toByteArray();
        }
    }
    
}
