/*
 * FontDetails.java
 *
 * Created on November 18, 2001, 11:32 AM
 */

package com.lowagie.text.pdf;

import java.util.HashMap;
import java.io.UnsupportedEncodingException;
/** Each font in the document will have an instance of this class
 * where the characters used will be represented.
 *
 * @author  Paulo Soares (psoares@consiste.pt)
 */
class FontDetails {
    
    /** The indirect reference to this font
     */    
    PdfIndirectReference indirectReference;
    /** The font name that appears in the document body stream
     */    
    PdfName fontName;
    /** The font
     */    
    BaseFont baseFont;
    /** The font if its an instance of <CODE>TrueTypeFontUnicode</CODE>
     */    
    TrueTypeFontUnicode ttu;
    /** The array used with single byte encodings
     */    
    byte shortTag[];
    /** The map used with double byte encodings. The key is Integer(glyph) and the
     * value is int[]{glyph, width, Unicode code}
     */    
    HashMap longTag;
    /** The font type
     */    
    int fontType;
    /** <CODE>true</CODE> if the font is symbolic
     */    
    boolean symbolic;
    /** Each font used in a document has an instance of this class.
     * This class stores the characters used in the document and other
     * specifics unique to the current working document.
     * @param fontName the font name
     * @param indirectReference the indirect reference to the font
     * @param baseFont the <CODE>BaseFont</CODE>
     */
    FontDetails(PdfName fontName, PdfIndirectReference indirectReference, BaseFont baseFont) {
        this.fontName = fontName;
        this.indirectReference = indirectReference;
        this.baseFont = baseFont;
        fontType = baseFont.getFontType();
        switch (fontType) {
            case BaseFont.FONT_TYPE_T1:
            case BaseFont.FONT_TYPE_TT:
                shortTag = new byte[256];
                break;
            case BaseFont.FONT_TYPE_CJK:
                break;
            case BaseFont.FONT_TYPE_TTUNI:
                longTag = new HashMap();
                ttu = (TrueTypeFontUnicode)baseFont;
                symbolic = baseFont.isFontSpecific();
                break;
        }
    }
    
    /** Gets the indirect reference to this font.
     * @return the indirect reference to this font
     */    
    PdfIndirectReference getIndirectReference() {
        return indirectReference;
    }
    
    /** Gets the font name as it appears in the document body.
     * @return the font name
     */    
    PdfName getFontName() {
        return fontName;
    }
    
    /** Gets the <CODE>BaseFont</CODE> of this font.
     * @return the <CODE>BaseFont</CODE> of this font
     */    
    BaseFont getBaseFont() {
        return baseFont;
    }
    
    /** Converts the text into bytes to be placed in the document.
     * The conversion is done according to the font and the encoding and the characters
     * used are stored.
     * @param text the text to convert
     * @return the conversion
     */    
    byte[] convertToBytes(String text) {
        byte b[] = null;
        switch (fontType) {
            case BaseFont.FONT_TYPE_T1:
            case BaseFont.FONT_TYPE_TT: {
                b = baseFont.convertToBytes(text);
                int len = b.length;
                for (int k = 0; k < len; ++k)
                    shortTag[((int)b[k]) & 0xff] = 1;
                break;
            }
            case BaseFont.FONT_TYPE_CJK:
                b = baseFont.convertToBytes(text);
                break;
            case BaseFont.FONT_TYPE_TTUNI: {
                try {
                    int len = text.length();
                    int metrics[] = null;
                    char glyph[] = new char[len];
                    int i = 0;
                    if (symbolic) {
                        try {
                            b = text.getBytes(PdfObject.ENCODING);
                        }
                        catch (Exception e) {
                            b = text.getBytes();
                        }
                        len = b.length;
                        for (int k = 0; k < len; ++k) {
                            metrics = ttu.getMetricsTT(b[k] & 0xff);
                            if (metrics == null)
                                continue;
                            longTag.put(new Integer(metrics[0]), new int[]{metrics[0], metrics[1], ttu.getUnicodeDifferences(b[k] & 0xff)});
                            glyph[i++] = (char)metrics[0];
                        }
                    }
                    else {
                        for (int k = 0; k < len; ++k) {
                            char c = text.charAt(k);
                            metrics = ttu.getMetricsTT(c);
                            if (metrics == null)
                                continue;
                            int m0 = metrics[0];
                            Integer gl = new Integer(m0);
                            if (!longTag.containsKey(gl))
                                longTag.put(gl, new int[]{m0, metrics[1], c});
                            glyph[i++] = (char)m0;
                        }
                    }
                    String s = new String(glyph, 0, i);
                    b = s.getBytes(CJKFont.CJK_ENCODING);
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    b = new byte[0];
                }
                break;
            }
        }
        return b;
    }
    
    /** Writes the font definition to the document.
     * @param writer the <CODE>PdfWriter</CODE> of this document
     */    
    void writeFont(PdfWriter writer) {
        try {
            switch (fontType) {
                case BaseFont.FONT_TYPE_T1:
                case BaseFont.FONT_TYPE_TT: {
                    int firstChar;
                    for (firstChar = 0; firstChar < 256; ++firstChar) {
                        if (shortTag[firstChar] != 0)
                            break;
                    }
                    int lastChar;
                    for (lastChar = 255; lastChar >= firstChar; --lastChar) {
                        if (shortTag[lastChar] != 0)
                            break;
                    }
                    if (firstChar > 255) {
                        firstChar = 255;
                        lastChar = 255;
                    }
                    baseFont.writeFont(writer, indirectReference, new Object[]{new Integer(firstChar), new Integer(lastChar), shortTag});
                    break;
                }
                case BaseFont.FONT_TYPE_CJK:
                    baseFont.writeFont(writer, indirectReference, null);
                    break;
                case BaseFont.FONT_TYPE_TTUNI:
                    baseFont.writeFont(writer, indirectReference, new Object[]{longTag});
                    break;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
