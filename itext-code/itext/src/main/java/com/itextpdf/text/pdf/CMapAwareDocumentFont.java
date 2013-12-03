/*
 * $Id: CMapAwareDocumentFont.java 5914 2013-07-28 14:18:11Z blowagie $
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
package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Utilities;
import java.io.IOException;
//import java.util.ArrayList;
import java.util.Map;

import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.pdf.fonts.cmaps.CMapByteCid;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCache;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCidUni;
import com.itextpdf.text.pdf.fonts.cmaps.CMapParserEx;
import com.itextpdf.text.pdf.fonts.cmaps.CMapSequence;
import com.itextpdf.text.pdf.fonts.cmaps.CMapToUnicode;
import com.itextpdf.text.pdf.fonts.cmaps.CidLocationFromByte;
import com.itextpdf.text.pdf.fonts.cmaps.IdentityToUnicode;


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
    private CMapToUnicode toUnicodeCmap;
    private CMapByteCid byteCid;
    private CMapCidUni cidUni;
	/**
	 *	Mapping between CID code (single byte only for now) and unicode equivalent
	 *  as derived by the font's encoding.  Only needed if the ToUnicode CMap is not provided.
	 */
    private char[] cidbyte2uni;
    
    private Map<Integer,Integer> uni2cid;
    
    public CMapAwareDocumentFont(PdfDictionary font) {
        super(font);
        fontDic = font;
        initFont();
    }
    
    /**
     * Creates an instance of a CMapAwareFont based on an indirect reference to a font.
     * @param refFont	the indirect reference to a font
     */
    public CMapAwareDocumentFont(PRIndirectReference refFont) {
        super(refFont);
        fontDic = (PdfDictionary)PdfReader.getPdfObjectRelease(refFont);
        initFont();
    }

    private void initFont() {
        processToUnicode();
        try {
        	//if (toUnicodeCmap == null)
            processUni2Byte();
        
            spaceWidth = super.getWidth(' ');
            if (spaceWidth == 0){
            	spaceWidth = computeAverageWidth();
            }
            if (cjkEncoding != null) {
                	byteCid = CMapCache.getCachedCMapByteCid(cjkEncoding);
                	cidUni = CMapCache.getCachedCMapCidUni(uniMap);
            }
        }
        catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }
    /**
     * Parses the ToUnicode entry, if present, and constructs a CMap for it
     * @since 2.1.7
     */
    private void processToUnicode(){
        PdfObject toUni = PdfReader.getPdfObjectRelease(fontDic.get(PdfName.TOUNICODE));
        if (toUni instanceof PRStream){
            try {
                byte[] touni = PdfReader.getStreamBytes((PRStream)toUni);
                CidLocationFromByte lb = new CidLocationFromByte(touni);
                toUnicodeCmap = new CMapToUnicode();
                CMapParserEx.parseCid("", toUnicodeCmap, lb);
                uni2cid = toUnicodeCmap.createReverseMapping();
            } catch (IOException e) {
                toUnicodeCmap = null;
                uni2cid = null;
                // technically, we should log this or provide some sort of feedback... but sometimes the cmap will be junk, but it's still possible to get text, so we don't want to throw an exception
                //throw new IllegalStateException("Unable to process ToUnicode map - " + e.getMessage(), e);
            }
        }
        else if (isType0) {
            // fake a ToUnicode for CJK Identity-H fonts
            try {
                PdfName encodingName = fontDic.getAsName(PdfName.ENCODING);
                if (encodingName == null)
                    return;
                String enc = PdfName.decodeName(encodingName.toString());
                if (!enc.equals("Identity-H"))
                    return;
                PdfArray df = (PdfArray)PdfReader.getPdfObjectRelease(fontDic.get(PdfName.DESCENDANTFONTS));
                PdfDictionary cidft = (PdfDictionary)PdfReader.getPdfObjectRelease(df.getPdfObject(0));
                PdfDictionary cidinfo = cidft.getAsDict(PdfName.CIDSYSTEMINFO);
                if (cidinfo == null)
                    return;
                PdfString ordering = cidinfo.getAsString(PdfName.ORDERING);
                if (ordering == null)
                    return;
                CMapToUnicode touni = IdentityToUnicode.GetMapFromOrdering(ordering.toUnicodeString());
                if (touni == null)
                    return;
                toUnicodeCmap = touni;
                uni2cid = toUnicodeCmap.createReverseMapping();
            } catch (IOException e) {
                toUnicodeCmap = null;
                uni2cid = null;
            }
        }
    }
    
    /**
     * Inverts DocumentFont's uni2byte mapping to obtain a cid-to-unicode mapping based
     * on the font's encoding
     * @throws IOException 
     * @since 2.1.7
     */
    private void processUni2Byte() throws IOException{
        //IntHashtable uni2byte = getUni2Byte();
        //int e[] = uni2byte.toOrderedKeys();
        //if (e.length == 0)
        //    return;
    	
    	IntHashtable byte2uni = getByte2Uni();
    	int e[] = byte2uni.toOrderedKeys();
    	if (e.length == 0)
    		return;
    	
        cidbyte2uni = new char[256];
        for (int k = 0; k < e.length; ++k) {
            int key = e[k];
            cidbyte2uni[key] = (char)byte2uni.get(key);
        }
        if (toUnicodeCmap != null) {
        	/*
        	for (int k = 0; k < e.length; ++k) {
        		// Kevin Day:
        		// this is messy, messy - an encoding can have multiple unicode values mapping to the same cid - we are going to arbitrarily choose the first one
        		// what we really need to do is to parse the encoding, and handle the differences info ourselves.  This is a huge duplication of code of what is already
        		// being done in DocumentFont, so I really hate to go down that path without seriously thinking about a change in the organization of the Font class hierarchy
        		
        		// Bruno Lowagie:
        		// I wish I could fix this in a better way, for instance by creating a uni2byte intHashtable in DocumentFont.
        		// However, I chose a quick & dirty solution, allowing intHashtable to store an array of int values.
        		ArrayList<Integer> nList = uni2byte.getValues(e[k]);
        		for (int n : nList) {
        			if (n < 256 && cidbyte2uni[n] == 0)
        				cidbyte2uni[n] = (char)e[k];
        		}
        	}
        	*/
        	Map<Integer, Integer> dm = toUnicodeCmap.createDirectMapping();
        	for (Map.Entry<Integer, Integer> kv : dm.entrySet()) {
        		if (kv.getKey() < 256) {
        			cidbyte2uni[kv.getKey().intValue()] = (char)kv.getValue().intValue();
        		}
        	}
        }
        IntHashtable diffmap = getDiffmap();
        if (diffmap != null) {
            // the difference array overrides the existing encoding
            e = diffmap.toOrderedKeys();
            for (int k = 0; k < e.length; ++k) {
                int n = diffmap.get(e[k]);
                if (n < 256)
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
    @Override
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
            String s = toUnicodeCmap.lookup(bytes, offset, len);
            if (s != null)
                return s;
            if (len != 1 || cidbyte2uni == null)
                return null;
        }

        if (len == 1){
            if (cidbyte2uni == null)
                return "";
            else
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
        StringBuilder sb = new StringBuilder();
        if (toUnicodeCmap == null && byteCid != null) {
            CMapSequence seq = new CMapSequence(cidbytes, offset, len);
            String cid = byteCid.decodeSequence(seq);
            for (int k = 0; k < cid.length(); ++k) {
                int c = cidUni.lookup(cid.charAt(k));
                if (c > 0)
                    sb.append(Utilities.convertFromUtf32(c));
            }
        }
        else {
            for(int i = offset; i < offset + len; i++){
                String rslt = decodeSingleCID(cidbytes, i, 1);
                if (rslt == null && i < offset + len - 1){
                    rslt = decodeSingleCID(cidbytes, i, 2);
                    i++;
                }
                if (rslt != null)
                    sb.append(rslt);
            }
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
