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
package com.itextpdf.text.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.pdf.fonts.cmaps.CMap;
import com.itextpdf.text.pdf.fonts.cmaps.CMapParser;


/**
 * Implementation of DocumentFont used while parsing PDF streams.
 * @since 2.1.4
 */
public class CMapAwareDocumentFont extends DocumentFont {

	/** The font dictionary. */
    private PdfDictionary fontDic;
    /** the width of a space for this font, in normalized 1000 point units */
    private int spaceWidth;
    /** The CMap constructed from the ToUnicode map from the font's dictionary, if present.
	 *  This CMap transforms CID values into unicode equivalent
	 */
    private CMap toUnicodeCmap;
	/**
	 *	Mapping between CID code (single byte only for now) and unicode equivalent
	 *  as derived by the font's encoding.  Only needed if the ToUnicode CMap is not provided.
	 */
    private char[] cidbyte2uni;
    
    /**
     * Creates an instance of a CMapAwareFont based on an indirect reference to a font.
     * @param refFont	the indirect reference to a font
     */
    public CMapAwareDocumentFont(PRIndirectReference refFont) {
        super(refFont);
        fontDic = (PdfDictionary)PdfReader.getPdfObjectRelease(refFont);

        processToUnicode();
        if (toUnicodeCmap == null)
            processUni2Byte();
        
        spaceWidth = super.getWidth(' ');
        if (spaceWidth == 0){
            spaceWidth = computeAverageWidth();
        }
        
    }

    /**
     * Parses the ToUnicode entry, if present, and constructs a CMap for it
     * @since 2.1.7
     */
    private void processToUnicode(){
        
        PdfObject toUni = fontDic.get(PdfName.TOUNICODE);
        if (toUni != null){
            
            try {
                byte[] touni = PdfReader.getStreamBytes((PRStream)PdfReader.getPdfObjectRelease(toUni));
    
                CMapParser cmapParser = new CMapParser();
                toUnicodeCmap = cmapParser.parse(new ByteArrayInputStream(touni));
            } catch (IOException e) {
                // technically, we should log this or provide some sort of feedback... but sometimes the cmap will be junk, but it's still possible to get text, so we don't want to throw an exception
                //throw new IllegalStateException("Unable to process ToUnicode map - " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Inverts DocumentFont's uni2byte mapping to obtain a cid-to-unicode mapping based
     * on the font's encoding
     * @since 2.1.7
     */
    private void processUni2Byte(){
        IntHashtable uni2byte = getUni2Byte();
        int e[] = uni2byte.toOrderedKeys();
        
        cidbyte2uni = new char[256];
        for (int k = 0; k < e.length; ++k) {
            int n = uni2byte.get(e[k]);
            
            // this is messy, messy - an encoding can have multiple unicode values mapping to the same cid - we are going to arbitrarily choose the first one
            // what we really need to do is to parse the encoding, and handle the differences info ourselves.  This is a huge duplication of code of what is already
            // being done in DocumentFont, so I really hate to go down that path without seriously thinking about a change in the organization of the Font class hierarchy
            if (cidbyte2uni[n] == 0)
                cidbyte2uni[n] = (char)e[k];
        }
        IntHashtable diffmap = getDiffmap();
        if (diffmap != null) {
            // the difference array overrides the existing encoding
            e = diffmap.toOrderedKeys();
            for (int k = 0; k < e.length; ++k) {
                int n = diffmap.get(e[k]);
                cidbyte2uni[n] = (char)e[k];
            }
        }
    }
    

    
    /**
     * For all widths of all glyphs, compute the average width in normalized 1000 point units.
     * This is used to give some meaningful width in cases where we need an average font width 
     * (such as if the width of a space isn't specified by a given font)
     * @return the average width of all non-zero width glyphs in the font
     */
    private int computeAverageWidth(){
        int count = 0;
        int total = 0;
        for(int i = 0; i < super.widths.length; i++){
            if(super.widths[i] != 0){
                total += super.widths[i];
                count++;
            }
        }
        return count != 0 ? total/count : 0;
    }
    
    /**
     * @since 2.1.5
     * Override to allow special handling for fonts that don't specify width of space character
     * @see com.itextpdf.text.pdf.DocumentFont#getWidth(int)
     */
    public int getWidth(int char1) {
        if (char1 == ' ')
            return spaceWidth;
        
        return super.getWidth(char1);
    }
    
    /**
     * Decodes a single CID (represented by one or two bytes) to a unicode String.
     * @param bytes		the bytes making up the character code to convert
     * @param offset	an offset
     * @param len		a length
     * @return	a String containing the encoded form of the input bytes using the font's encoding.
     */
    private String decodeSingleCID(byte[] bytes, int offset, int len){
        if (toUnicodeCmap != null){
            if (offset + len > bytes.length)
                throw new ArrayIndexOutOfBoundsException(MessageLocalization.getComposedMessage("invalid.index.1", offset + len));
            return toUnicodeCmap.lookup(bytes, offset, len);
        }

        if (len == 1){
            return new String(cidbyte2uni, 0xff & bytes[offset], 1);
        }
        
        throw new Error("Multi-byte glyphs not implemented yet");
    }

    /**
     * Decodes a string of bytes (encoded in the font's encoding) into a unicode string
     * This will use the ToUnicode map of the font, if available, otherwise it uses
     * the font's encoding
     * @param cidbytes    the bytes that need to be decoded
     * @return  the unicode String that results from decoding
     * @since 2.1.7
     */
    public String decode(byte[] cidbytes, final int offset, final int len){
        StringBuffer sb = new StringBuffer(); // it's a shame we can't make this StringBuilder
        for(int i = offset; i < offset + len; i++){
            String rslt = decodeSingleCID(cidbytes, i, 1);
            if (rslt == null && i < offset + len - 1){
                rslt = decodeSingleCID(cidbytes, i, 2);
                i++;
            }
            sb.append(rslt);
        }

        return sb.toString();
    }

    /**
     * Encodes bytes to a String.
     * @param bytes		the bytes from a stream
     * @param offset	an offset
     * @param len		a length
     * @return	a String encoded taking into account if the bytes are in unicode or not.
     * @deprecated method name is not indicative of what it does.  Use <code>decode</code> instead.
     */
    public String encode(byte[] bytes, int offset, int len){
        return decode(bytes, offset, len);    
    }
}
