/*
 * $Id$
 * $Name$
 *
 * Copyright 2000, 2001, 2002 by Paulo Soares.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.pdf;
import java.io.*;
import com.lowagie.text.DocumentException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Base class for the several font types supported
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */

public abstract class BaseFont {
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String COURIER = "Courier";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String COURIER_BOLD = "Courier-Bold";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String COURIER_OBLIQUE = "Courier-Oblique";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String COURIER_BOLDOBLIQUE = "Courier-BoldOblique";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA = "Helvetica";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA_BOLD = "Helvetica-Bold";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA_OBLIQUE = "Helvetica-Oblique";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA_BOLDOBLIQUE = "Helvetica-BoldOblique";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String SYMBOL = "Symbol";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_ROMAN = "Times-Roman";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_BOLD = "Times-Bold";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_ITALIC = "Times-Italic";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_BOLDITALIC = "Times-BoldItalic";
    
    /** This is a possible value of a base 14 type 1 font */
    public static final String ZAPFDINGBATS = "ZapfDingbats";
    
    /** The maximum height above the baseline reached by glyphs in this
     * font, excluding the height of glyphs for accented characters.
     */    
    public static final int ASCENT = 1;    
    /** The y coordinate of the top of flat capital letters, measured from
     * the baseline.
     */    
    public static final int CAPHEIGHT = 2;
    /** The maximum depth below the baseline reached by glyphs in this
     * font. The value is a negative number.
     */    
    public static final int DESCENT = 3;
    /** The angle, expressed in degrees counterclockwise from the vertical,
     * of the dominant vertical strokes of the font. The value is
     * negative for fonts that slope to the right, as almost all italic fonts do.
     */    
    public static final int ITALICANGLE = 4;
    /** The lower left x glyph coordinate.
     */    
    public static final int BBOXLLX = 5;
    /** The lower left y glyph coordinate.
     */    
    public static final int BBOXLLY = 6;
    /** The upper right x glyph coordinate.
     */    
    public static final int BBOXURX = 7;
    /** The upper right y glyph coordinate.
     */    
    public static final int BBOXURY = 8;
    
    public static final int AWT_ASCENT = 9;
    public static final int AWT_DESCENT = 10;
    public static final int AWT_LEADING = 11;
    public static final int AWT_MAXADVANCE = 12;
    
    /** The font is Type 1.
     */    
    public static final int FONT_TYPE_T1 = 0;
    /** The font is True Type with a standard encoding.
     */    
    public static final int FONT_TYPE_TT = 1;
    /** The font is CJK.
     */    
    public static final int FONT_TYPE_CJK = 2;
    /** The font is True Type with a Unicode encoding.
     */    
    public static final int FONT_TYPE_TTUNI = 3;
    /** A font already inside the document.
     */    
    public static final int FONT_TYPE_DOCUMENT = 4;
    /** The Unicode encoding with horizontal writing.
     */    
    public static final String IDENTITY_H = "Identity-H";
    /** The Unicode encoding with vertical writing.
     */    
    public static final String IDENTITY_V = "Identity-V";
    
    /** A possible encoding. */    
    public static final String CP1250 = "Cp1250";
    
    /** A possible encoding. */    
    public static final String CP1252 = "Cp1252";
    
    /** A possible encoding. */    
    public static final String CP1257 = "Cp1257";
    
    /** A possible encoding. */    
    public static final String WINANSI = "Cp1252";
    
    /** A possible encoding. */    
    public static final String MACROMAN = "MacRoman";
    
    
/** if the font has to be embedded */
    public static final boolean EMBEDDED = true;
    
/** if the font doesn't have to be embedded */
    public static final boolean NOT_EMBEDDED = false;
/** if the font has to be cached */
    public static final boolean CACHED = true;
/** if the font doesn't have to be cached */
    public static final boolean NOT_CACHED = false;
    
    /** The path to the font resources. */    
    public static final String RESOURCE_PATH = "com/lowagie/text/pdf/fonts/";
    /** The fake CID code that represents a newline. */    
    public static final char CID_NEWLINE = '\u7fff';
    
    /** The font type.
     */    
    int fontType;
/** a not defined character in a custom PDF encoding */
    public static final String notdef = ".notdef";
    
/** table of characters widths for this encoding */
    protected int widths[] = new int[256];
    
/** encoding names */
    protected String differences[] = new String[256];
/** same as differences but with the unicode codes */
    protected char unicodeDifferences[] = new char[256];
    
/** encoding used with this font */
    protected String encoding;
    
/** true if the font is to be embedded in the PDF */
    protected boolean embedded;
    
/**
 * true if the font must use it's built in encoding. In that case the
 * <CODE>encoding</CODE> is only used to map a char to the position inside
 * the font, not to the expected char name.
 */
    protected boolean fontSpecific = true;
    
/** cache for the fonts already used. */
    protected static HashMap fontCache = new HashMap();
    
/** list of the 14 built in fonts. */
    protected static final HashMap BuiltinFonts14 = new HashMap();
    
    /** Forces the output of the width array. Only matters for the 14
     * built-in fonts.
     */
    protected boolean forceWidthsOutput = false;
    
    /** Converts <CODE>char</CODE> directly to <CODE>byte</CODE>
     * by casting.
     */
    protected boolean directTextToByte = false;
    
    /** Indicates if all the glyphs and widths for that particular
     * encoding should be included in the document.
     */
    protected boolean subset = true;
    
    protected boolean fastWinansi = false;
    
    static {
        BuiltinFonts14.put(COURIER, PdfName.COURIER);
        BuiltinFonts14.put(COURIER_BOLD, PdfName.COURIER_BOLD);
        BuiltinFonts14.put(COURIER_BOLDOBLIQUE, PdfName.COURIER_BOLDOBLIQUE);
        BuiltinFonts14.put(COURIER_OBLIQUE, PdfName.COURIER_OBLIQUE);
        BuiltinFonts14.put(HELVETICA, PdfName.HELVETICA);
        BuiltinFonts14.put(HELVETICA_BOLD, PdfName.HELVETICA_BOLD);
        BuiltinFonts14.put(HELVETICA_BOLDOBLIQUE, PdfName.HELVETICA_BOLDOBLIQUE);
        BuiltinFonts14.put(HELVETICA_OBLIQUE, PdfName.HELVETICA_OBLIQUE);
        BuiltinFonts14.put(SYMBOL, PdfName.SYMBOL);
        BuiltinFonts14.put(TIMES_ROMAN, PdfName.TIMES_ROMAN);
        BuiltinFonts14.put(TIMES_BOLD, PdfName.TIMES_BOLD);
        BuiltinFonts14.put(TIMES_BOLDITALIC, PdfName.TIMES_BOLDITALIC);
        BuiltinFonts14.put(TIMES_ITALIC, PdfName.TIMES_ITALIC);
        BuiltinFonts14.put(ZAPFDINGBATS, PdfName.ZAPFDINGBATS);
    }
    
    /** Generates the PDF stream with the Type1 and Truetype fonts returning
     * a PdfStream.
     */
    static class StreamFont extends PdfStream {
        
        /** Generates the PDF stream with the Type1 and Truetype fonts returning
         * a PdfStream.
         * @param contents the content of the stream
         * @param lengths an array of int that describes the several lengths of each part of the font
         * @throws DocumentException error in the stream compression
         */
        public StreamFont(byte contents[], int lengths[]) throws DocumentException {
            try {
                bytes = contents;
                put(PdfName.LENGTH, new PdfNumber(bytes.length));
                for (int k = 0; k < lengths.length; ++k) {
                    put(new PdfName("Length" + (k + 1)), new PdfNumber(lengths[k]));
                }
                flateCompress();
            }
            catch (Exception e) {
                throw new DocumentException(e);
            }
        }
        
        public StreamFont(byte contents[], String subType) throws DocumentException {
            try {
                bytes = contents;
                put(PdfName.LENGTH, new PdfNumber(bytes.length));
                if (subType != null)
                    put(PdfName.SUBTYPE, new PdfName(subType));
                flateCompress();
            }
            catch (Exception e) {
                throw new DocumentException(e);
            }
        }
    }
    
    /**
     *Creates new BaseFont
     */
    protected BaseFont() {
    }
    
    /** Creates a new font. This font can be one of the 14 built in types,
     * a Type1 font referred by an AFM file, a TrueType font (simple or collection) or a CJK font from the
     * Adobe Asian Font Pack. TrueType fonts and CJK fonts can have an optional style modifier
     * appended to the name. These modifiers are: Bold, Italic and BoldItalic. An
     * example would be "STSong-Light,Bold". Note that this modifiers do not work if
     * the font is embedded. Fonts in TrueType collections are addressed by index such as "msgothic.ttc,1".
     * This would get the second font (indexes start at 0), in this case "MS PGothic".
     * <P>
     * The fonts are cached and if they already exist they are extracted from the cache,
     * not parsed again.
     * <P>
     * This method calls:<br>
     * <PRE>
     * createFont(name, encoding, embedded, true, null, null);
     * </PRE>
     * @param name the name of the font or it's location on file
     * @param encoding the encoding to be applied to this font
     * @param embedded true if the font is to be embedded in the PDF
     * @return returns a new font. This font may come from the cache
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
    public static BaseFont createFont(String name, String encoding, boolean embedded) throws DocumentException, IOException {
        return createFont(name, encoding, embedded, true, null, null);
    }
    
    /** Creates a new font. This font can be one of the 14 built in types,
     * a Type1 font referred by an AFM file, a TrueType font (simple or collection) or a CJK font from the
     * Adobe Asian Font Pack. TrueType fonts and CJK fonts can have an optional style modifier
     * appended to the name. These modifiers are: Bold, Italic and BoldItalic. An
     * example would be "STSong-Light,Bold". Note that this modifiers do not work if
     * the font is embedded. Fonts in TrueType collections are addressed by index such as "msgothic.ttc,1".
     * This would get the second font (indexes start at 0), in this case "MS PGothic".
     * <P>
     * The fonts may or may not be cached depending on the flag <CODE>cached</CODE>.
     * If the <CODE>byte</CODE> arrays are present the font will be
     * read from them instead of the name. The name is still required to identify
     * the font type.
     * @param name the name of the font or it's location on file
     * @param encoding the encoding to be applied to this font
     * @param embedded true if the font is to be embedded in the PDF
     * @param cached true if the font comes from the cache or is added to
     * the cache if new. false if the font is always created new
     * @param ttfAfm the true type font or the afm in a byte array
     * @param pfb the pfb in a byte array
     * @return returns a new font. This font may come from the cache but only if cached
     * is true, otherwise it will always be created new
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
    public static BaseFont createFont(String name, String encoding, boolean embedded, boolean cached, byte ttfAfm[], byte pfb[]) throws DocumentException, IOException {
        String nameBase = getBaseName(name);
        encoding = normalizeEncoding(encoding);
        boolean isBuiltinFonts14 = BuiltinFonts14.containsKey(name);
        boolean isCJKFont = isBuiltinFonts14 ? false : CJKFont.isCJKFont(nameBase, encoding);
        if (isBuiltinFonts14 || isCJKFont)
            embedded = false;
        else if (encoding.equals(IDENTITY_H) || encoding.equals(IDENTITY_V))
            embedded = true;
        BaseFont fontFound = null;
        BaseFont fontBuilt = null;
        String key = name + "\n" + encoding + "\n" + embedded;
        if (cached) {
            synchronized (fontCache) {
                fontFound = (BaseFont)fontCache.get(key);
            }
            if (fontFound != null)
                return fontFound;
        }
        if (isBuiltinFonts14 || name.toLowerCase().endsWith(".afm")) {
            fontBuilt = new Type1Font(name, encoding, embedded, ttfAfm, pfb);
            fontBuilt.fastWinansi = encoding.equals(CP1252);
        }
        else if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0) {
            if (encoding.equals(IDENTITY_H) || encoding.equals(IDENTITY_V))
                fontBuilt = new TrueTypeFontUnicode(name, encoding, embedded, ttfAfm);
            else {
                fontBuilt = new TrueTypeFont(name, encoding, embedded, ttfAfm);
                fontBuilt.fastWinansi = encoding.equals(CP1252);
            }
        }
        else if (isCJKFont)
            fontBuilt = new CJKFont(name, encoding, embedded);
        else
            throw new DocumentException("Font '" + name + "' with '" + encoding + "' is not recognized.");
        if (cached) {
            synchronized (fontCache) {
                fontFound = (BaseFont)fontCache.get(key);
                if (fontFound != null)
                    return fontFound;
                fontCache.put(key, fontBuilt);
            }
        }
        return fontBuilt;
    }
    
    /**
     * Creates a font based on an existing document font. The created font font may not
     * behave as expected, depending on the encoding or subset.
     * @param fontRef the reference to the document font
     * @return the font
     */    
    public static BaseFont createFont(PRIndirectReference fontRef) {
        return new DocumentFont(fontRef);
    }
    
    /**
     * Gets the name without the modifiers Bold, Italic or BoldItalic.
     * @param name the full name of the font
     * @return the name without the modifiers Bold, Italic or BoldItalic
     */
    protected static String getBaseName(String name) {
        if (name.endsWith(",Bold"))
            return name.substring(0, name.length() - 5);
        else if (name.endsWith(",Italic"))
            return name.substring(0, name.length() - 7);
        else if (name.endsWith(",BoldItalic"))
            return name.substring(0, name.length() - 11);
        else
            return name;
    }
    
    /**
     * Normalize the encoding names. "winansi" is changed to "Cp1252" and
     * "macroman" is changed to "MacRoman".
     * @param enc the encoding to be normalized
     * @return the normalized encoding
     */
    protected static String normalizeEncoding(String enc) {
        if (enc.equals("winansi") || enc.equals(""))
            return CP1252;
        else if (enc.equals("macroman"))
            return MACROMAN;
        else
            return enc;
    }
    
    /**
     * Creates the <CODE>widths</CODE> and the <CODE>differences</CODE> arrays
     * @throws UnsupportedEncodingException the encoding is not supported
     */
    protected void createEncoding() {
        if (fontSpecific) {
            for (int k = 0; k < 256; ++k)
                widths[k] = getRawWidth(k, null);
        }
        else {
            String s;
            String name;
            char c;
            byte b[] = new byte[1];
            for (int k = 0; k < 256; ++k) {
                b[0] = (byte)k;
                s = PdfEncodings.convertToString(b, encoding);
                if (s.length() > 0) {
                    c = s.charAt(0);
                }
                else {
                    c = '?';
                }
                name = GlyphList.unicodeToName((int)c);
                if (name == null)
                    name = notdef;
                differences[k] = name;
                unicodeDifferences[k] = c;
                widths[k] = getRawWidth((int)c, name);
            }
        }
    }
    
    /**
     * Gets the width from the font according to the Unicode char <CODE>c</CODE>
     * or the <CODE>name</CODE>. If the <CODE>name</CODE> is null it's a symbolic font.
     * @param c the unicode char
     * @param name the glyph name
     * @return the width of the char
     */
    abstract int getRawWidth(int c, String name);
    
    /**
     * Gets the kerning between two Unicode chars.
     * @param char1 the first char
     * @param char2 the second char
     * @return the kerning to be applied in normalized 1000 units
     */
    public abstract int getKerning(char char1, char char2);

    /**
     * Sets the kerning between two Unicode chars.
     * @param char1 the first char
     * @param char2 the second char
     * @paran kern the kerning to apply in normalized 1000 units
     * @return <code>true</code> if the kerning was applied, <code>false</code> otherwise
     */
    public abstract boolean setKerning(char char1, char char2, int kern);
    
    /**
     * Gets the width of a <CODE>char</CODE> in normalized 1000 units.
     * @param char1 the unicode <CODE>char</CODE> to get the width of
     * @return the width in normalized 1000 units
     */
    public int getWidth(char char1) {
        if (fastWinansi) {
            if (char1 < 128 || (char1 >= 160 && char1 <= 255))
                return widths[char1];
            return widths[PdfEncodings.winansi.get(char1)];
        }
        return getWidth(new String(new char[]{char1}));
    }
    
    /**
     * Gets the width of a <CODE>String</CODE> in normalized 1000 units.
     * @param text the <CODE>String</CODE> to get the witdth of
     * @return the width in normalized 1000 units
     */
    public int getWidth(String text) {
        int total = 0;
        if (fastWinansi) {
            int len = text.length();
            for (int k = 0; k < len; ++k) {
                char char1 = text.charAt(k);
                if (char1 < 128 || (char1 >= 160 && char1 <= 255))
                    total += widths[char1];
                else
                    total += widths[PdfEncodings.winansi.get(char1)];
            }
            return total;
        }
        else {
            byte mbytes[] = convertToBytes(text);
            for (int k = 0; k < mbytes.length; ++k)
                total += widths[0xff & mbytes[k]];
        }
        return total;
    }
    
    /**
     * Gets the width of a <CODE>String</CODE> in points taking kerning
     * into account.
     * @param text the <CODE>String</CODE> to get the witdth of
     * @param fontSize the font size
     * @return the width in points
     */
    public float getWidthPointKerned(String text, float fontSize) {
        float size = (float)getWidth(text) * 0.001f * fontSize;
        if (!hasKernPairs())
            return size;
        int len = text.length() - 1;
        int kern = 0;
        char c[] = text.toCharArray();
        for (int k = 0; k < len; ++k) {
            kern += getKerning(c[k], c[k + 1]);
        }
        return size + kern * 0.001f * fontSize;
    }
    
    /**
     * Gets the width of a <CODE>String</CODE> in points.
     * @param text the <CODE>String</CODE> to get the witdth of
     * @param fontSize the font size
     * @return the width in points
     */
    public float getWidthPoint(String text, float fontSize) {
        return (float)getWidth(text) * 0.001f * fontSize;
    }
    
    /**
     * Gets the width of a <CODE>char</CODE> in points.
     * @param char1 the <CODE>char</CODE> to get the witdth of
     * @param fontSize the font size
     * @return the width in points
     */
    public float getWidthPoint(char char1, float fontSize) {
        return getWidth(char1) * 0.001f * fontSize;
    }
    
    /**
     * Converts a <CODE>String</CODE> to a </CODE>byte</CODE> array according
     * to the font's encoding.
     * @param text the <CODE>String</CODE> to be converted
     * @return an array of <CODE>byte</CODE> representing the conversion according to the font's encoding
     */
    byte[] convertToBytes(String text) {
        if (directTextToByte)
            return PdfEncodings.convertToBytes(text, null);
        return PdfEncodings.convertToBytes(text, encoding);
    }
    
    /** Outputs to the writer the font dictionaries and streams.
     * @param writer the writer for this document
     * @param ref the font indirect reference
     * @param params several parameters that depend on the font type
     * @throws IOException on error
     * @throws DocumentException error in generating the object
     */
    abstract void writeFont(PdfWriter writer, PdfIndirectReference ref, Object params[]) throws DocumentException, IOException;
    
    /** Gets the encoding used to convert <CODE>String</CODE> into <CODE>byte[]</CODE>.
     * @return the encoding name
     */
    public String getEncoding() {
        return encoding;
    }
    
    /** Gets the font parameter identified by <CODE>key</CODE>. Valid values
     * for <CODE>key</CODE> are <CODE>ASCENT</CODE>, <CODE>CAPHEIGHT</CODE>, <CODE>DESCENT</CODE>,
     * <CODE>ITALICANGLE</CODE>, <CODE>BBOXLLX</CODE>, <CODE>BBOXLLY</CODE>, <CODE>BBOXURX</CODE>
     * and <CODE>BBOXURY</CODE>.
     * @param key the parameter to be extracted
     * @param fontSize the font size in points
     * @return the parameter in points
     */
    public abstract float getFontDescriptor(int key, float fontSize);
    
    /** Gets the font type. The font types can be: FONT_TYPE_T1,
     * FONT_TYPE_TT, FONT_TYPE_CJK and FONT_TYPE_TTUNI.
     * @return the font type
     */
    public int getFontType() {
        return fontType;
    }
    
    /** Gets the embedded flag.
     * @return <CODE>true</CODE> if the font is embedded.
     */
    public boolean isEmbedded() {
        return embedded;
    }
    
    /** Gets the symbolic flag of the font.
     * @return <CODE>true</CODE> if the font is symbolic
     */
    public boolean isFontSpecific() {
        return fontSpecific;
    }
    
    /** Creates a unique subset prefix to be added to the font name when the font is embedded and subset.
     * @return the subset prefix
     */
    public static String createSubsetPrefix() {
        String s = "";
        for (int k = 0; k < 6; ++k)
            s += (char)(Math.random() * 26 + 'A');
        return s + "+";
    }
    
    /** Gets the Unicode character corresponding to the byte output to the pdf stream.
     * @param index the byte index
     * @return the Unicode character
     */
    char getUnicodeDifferences(int index) {
        return unicodeDifferences[index];
    }
    
    /** Gets the postscript font name.
     * @return the postscript font name
     */
    public abstract String getPostscriptFontName();
    
    /**
     * Sets the font name that will appear in the pdf font dictionary.
     * Use with care as it can easily make a font unreadable if not embedded.
     * @param name the new font name
     */    
    public abstract void setPostscriptFontName(String name);
    
    /** Gets the full name of the font. If it is a True Type font
     * each array element will have {Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"", "", "",
     * font name}.
     * @return the full name of the font
     */
    public abstract String[][] getFullFontName();
    
    /** Gets the full name of the font. If it is a True Type font
     * each array element will have {Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"", "", "",
     * font name}.
     * @param name the name of the font
     * @param encoding the encoding of the font
     * @param ttfAfm the true type font or the afm in a byte array
     * @throws DocumentException on error
     * @throws IOException on error
     * @return the full name of the font
     */    
    public static String[][] getFullFontName(String name, String encoding, byte ttfAfm[]) throws DocumentException, IOException {
        String nameBase = getBaseName(name);
        BaseFont fontBuilt = null;
        if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0)
            fontBuilt = new TrueTypeFont(name, CP1252, false, ttfAfm, true);
        else
            fontBuilt = createFont(name, encoding, false, false, ttfAfm, null);
        return fontBuilt.getFullFontName();
    }
    
    /** Gets all the names from the font. Only the required tables are read.
     * @param name the name of the font
     * @param encoding the encoding of the font
     * @param ttfAfm the true type font or the afm in a byte array
     * @throws DocumentException on error
     * @throws IOException on error
     * @return an array of Object[] built with {getPostscriptFontName(), getFamilyFontName(), getFullFontName()}
     */    
    public static Object[] getAllFontNames(String name, String encoding, byte ttfAfm[]) throws DocumentException, IOException {
        String nameBase = getBaseName(name);
        BaseFont fontBuilt = null;
        if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().endsWith(".otf") || nameBase.toLowerCase().indexOf(".ttc,") > 0)
            fontBuilt = new TrueTypeFont(name, CP1252, false, ttfAfm, true);
        else
            fontBuilt = createFont(name, encoding, false, false, ttfAfm, null);
        return new Object[]{fontBuilt.getPostscriptFontName(), fontBuilt.getFamilyFontName(), fontBuilt.getFullFontName()};
    }
    
    /** Gets the family name of the font. If it is a True Type font
     * each array element will have {Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"", "", "",
     * font name}.
     * @return the family name of the font
     */
    public abstract String[][] getFamilyFontName();
    
    /** Gets the code pages supported by the font. This has only meaning
     * with True Type fonts.
     * @return the code pages supported by the font
     */
    public String[] getCodePagesSupported() {
        return new String[0];
    }
    
    /** Enumerates the postscript font names present inside a
     * True Type Collection.
     * @param ttcFile the file name of the font
     * @throws DocumentException on error
     * @throws IOException on error
     * @return the postscript font names
     */    
    public static String[] enumerateTTCNames(String ttcFile) throws DocumentException, IOException {
        return new EnumerateTTC(ttcFile).getNames();
    }

    /** Enumerates the postscript font names present inside a
     * True Type Collection.
     * @param ttcArray the font as a <CODE>byte</CODE> array
     * @throws DocumentException on error
     * @throws IOException on error
     * @return the postscript font names
     */    
    public static String[] enumerateTTCNames(byte ttcArray[]) throws DocumentException, IOException {
        return new EnumerateTTC(ttcArray).getNames();
    }
    
    /** Gets the font width array.
     * @return the font width array
     */    
    public int[] getWidths() {
        return widths;
    }

    /** Gets the array with the names of the characters.
     * @return the array with the names of the characters
     */    
    public String[] getDifferences() {
        return differences;
    }

    /** Gets the array with the unicode characters.
     * @return the array with the unicode characters
     */    
    public char[] getUnicodeDifferences() {
        return unicodeDifferences;
    }
    
    /** Gets the state of the property.
     * @return value of property forceWidthsOutput
     */
    public boolean isForceWidthsOutput() {
        return forceWidthsOutput;
    }
    
    /** Set to <CODE>true</CODE> to force the generation of the
     * widths array.
     * @param forceWidthsOutput <CODE>true</CODE> to force the generation of the
     * widths array
     */
    public void setForceWidthsOutput(boolean forceWidthsOutput) {
        this.forceWidthsOutput = forceWidthsOutput;
    }
    
    /** Gets the direct conversion of <CODE>char</CODE> to <CODE>byte</CODE>.
     * @return value of property directTextToByte.
     * @see #setDirectTextToByte(boolean directTextToByte)
     */
    public boolean isDirectTextToByte() {
        return directTextToByte;
    }
    
    /** Sets the conversion of <CODE>char</CODE> directly to <CODE>byte</CODE>
     * by casting. This is a low level feature to put the bytes directly in
     * the content stream without passing through String.getBytes().
     * @param directTextToByte New value of property directTextToByte.
     */
    public void setDirectTextToByte(boolean directTextToByte) {
        this.directTextToByte = directTextToByte;
    }
    
    /** Indicates if all the glyphs and widths for that particular
     * encoding should be included in the document.
     * @return <CODE>false</CODE> to include all the glyphs and widths.
     */
    public boolean isSubset() {
        return subset;
    }
    
    /** Indicates if all the glyphs and widths for that particular
     * encoding should be included in the document. Set to <CODE>false</CODE>
     * to include all.
     * @param subset new value of property subset
     */
    public void setSubset(boolean subset) {
        this.subset = subset;
    }

    /** Gets the font resources.
     * @param key the full name of the resource
     * @return the <CODE>InputStream</CODE> to get the resource or
     * <CODE>null</CODE> if not found
     */    
    public static InputStream getResourceStream(String key) {
        return getResourceStream(key, null);
    }
    
    /** Gets the font resources.
     * @param key the full name of the resource
     * @param loader the ClassLoader to load the resource or null to try the ones available
     * @return the <CODE>InputStream</CODE> to get the resource or
     * <CODE>null</CODE> if not found
     */    
    public static InputStream getResourceStream(String key, ClassLoader loader) {
        if (key.startsWith("/"))
            key = key.substring(1);
        InputStream is = null;
        if (loader != null) {
            is = loader.getResourceAsStream(key);
            if (is != null)
                return is;
        }
        // Try to use Context Class Loader to load the properties file.
        try {
            java.lang.reflect.Method getCCL =
                Thread.class.getMethod("getContextClassLoader", new Class[0]);
            if (getCCL != null) {
                ClassLoader contextClassLoader =
                    (ClassLoader)getCCL.invoke(Thread.currentThread(),
                                               new Object[0]);
                if (contextClassLoader != null)
                    is = contextClassLoader.getResourceAsStream(key);
            }
        } catch (Throwable e) {}

        if (is == null) {
            is = BaseFont.class.getResourceAsStream("/" + key);
        }
        if (is == null) {
            is = ClassLoader.getSystemResourceAsStream(key);
        }
        return is;
    }
    
    /** Gets the Unicode equivalent to a CID.
     * The (inexistent) CID <FF00> is translated as '\n'. 
     * It has only meaning with CJK fonts with Identity encoding.
     * @param c the CID code
     * @return the Unicode equivalent
     */    
    public char getUnicodeEquivalent(char c) {
        return c;
    }
    
    /** Gets the CID code given an Unicode.
     * It has only meaning with CJK fonts.
     * @param c the Unicode
     * @return the CID equivalent
     */    
    public char getCidCode(char c) {
        return c;
    }

    /** Checks if the font has any kerning pairs.
     * @return <CODE>true</CODE> if the font has any kerning pairs
     */    
    public abstract boolean hasKernPairs();
    
    /**
     * Checks if a character exists in this font.
     * @param c the character to check
     * @return <CODE>true</CODE> if the character has a glyph,
     * <CODE>false</CODE> otherwise
     */    
    public boolean charExists(char c) {
        byte b[] = convertToBytes(new String(new char[]{c}));
        return b.length > 0;
    }
    
    /**
     * Sets the character advance.
     * @param c the character
     * @param advance the character advance normalized to 1000 units
     * @return <CODE>true</CODE> if the advance was set,
     * <CODE>false</CODE> otherwise
     */    
    public boolean setCharAdvance(char c, int advance) {
        byte b[] = convertToBytes(new String(new char[]{c}));
        if (b.length == 0)
            return false;
        widths[0xff & b[0]] = advance;
        return true;
    }
    
    private static void addFont(PRIndirectReference fontRef, IntHashtable hits, ArrayList fonts) {
        PdfObject obj = PdfReader.getPdfObject(fontRef);
        if (!obj.isDictionary())
            return;
        PdfDictionary font = (PdfDictionary)obj;
        PdfName subtype = (PdfName)PdfReader.getPdfObject(font.get(PdfName.SUBTYPE));
        if (!PdfName.TYPE1.equals(subtype) && !PdfName.TRUETYPE.equals(subtype))
            return;
        PdfName name = (PdfName)PdfReader.getPdfObject(font.get(PdfName.BASEFONT));
        fonts.add(new Object[]{PdfName.decodeName(name.toString()), fontRef});
        hits.put(fontRef.getNumber(), 1);
    }
    
    private static void recourseFonts(PdfDictionary page, IntHashtable hits, ArrayList fonts, int level) {
        ++level;
        if (level > 50) // in case we have an endless loop
            return;
        PdfDictionary resources = (PdfDictionary)PdfReader.getPdfObject(page.get(PdfName.RESOURCES));
        if (resources == null)
            return;
        PdfDictionary font = (PdfDictionary)PdfReader.getPdfObject(resources.get(PdfName.FONT));
        if (font != null) {
            for (Iterator it = font.getKeys().iterator(); it.hasNext();) {
                PdfObject ft = font.get((PdfName)it.next());        
                if (ft == null || !ft.isIndirect())
                    continue;
                int hit = ((PRIndirectReference)ft).getNumber();
                if (hits.containsKey(hit))
                    continue;
                addFont((PRIndirectReference)ft, hits, fonts);
            }
        }
        PdfDictionary xobj = (PdfDictionary)PdfReader.getPdfObject(resources.get(PdfName.XOBJECT));
        if (xobj != null) {
            for (Iterator it = xobj.getKeys().iterator(); it.hasNext();) {
                recourseFonts((PdfDictionary)PdfReader.getPdfObject(xobj.get((PdfName)it.next())), hits, fonts, level);
            }
        }
    }
    
    /**
     * Gets a list of all document fonts. Each element of the <CODE>ArrayList</CODE>
     * contains a <CODE>Object[]{String,PRIndirectReference}</CODE> with the font name
     * and the indirect reference to it.
     * @param reader the document where the fonts are to be listed from
     * @return the list of fonts and references
     */    
    public static ArrayList getDocumentFonts(PdfReader reader) {
        IntHashtable hits = new IntHashtable();
        ArrayList fonts = new ArrayList();
        int npages = reader.getNumberOfPages();
        for (int k = 1; k <= npages; ++k)
            recourseFonts(reader.getPageN(k), hits, fonts, 1);
        return fonts;
    }
    
    /**
     * Gets a list of the document fonts in a particular page. Each element of the <CODE>ArrayList</CODE>
     * contains a <CODE>Object[]{String,PRIndirectReference}</CODE> with the font name
     * and the indirect reference to it.
     * @param reader the document where the fonts are to be listed from
     * @param page the page to list the fonts from
     * @return the list of fonts and references
     */    
    public static ArrayList getDocumentFonts(PdfReader reader, int page) {
        IntHashtable hits = new IntHashtable();
        ArrayList fonts = new ArrayList();
        recourseFonts(reader.getPageN(page), hits, fonts, 1);
        return fonts;
    }
}
