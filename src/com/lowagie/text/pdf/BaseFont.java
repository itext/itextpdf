/*
 * $Id$
 * $Name$
 *
 * Copyright 2000, 2001 by Paulo Soares.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text.pdf;
import java.io.*;
import com.lowagie.text.DocumentException;
import java.util.HashMap;

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
    public final static int ASCENT = 1;    
    /** The y coordinate of the top of flat capital letters, measured from
     * the baseline.
     */    
    public final static int CAPHEIGHT = 2;
    /** The maximum depth below the baseline reached by glyphs in this
     * font. The value is a negative number.
     */    
    public final static int DESCENT = 3;
    /** The angle, expressed in degrees counterclockwise from the vertical,
     * of the dominant vertical strokes of the font. The value is
     * negative for fonts that slope to the right, as almost all italic fonts do.
     */    
    public final static int ITALICANGLE = 4;
    /** The lower left x glyph coordinate.
     */    
    public final static int BBOXLLX = 5;
    /** The lower left y glyph coordinate.
     */    
    public final static int BBOXLLY = 6;
    /** The upper right x glyph coordinate.
     */    
    public final static int BBOXURX = 7;
    /** The upper right y glyph coordinate.
     */    
    public final static int BBOXURY = 8;
    
    /** The font is Type 1.
     */    
    public final static int FONT_TYPE_T1 = 0;
    /** The font is True Type with a standard encoding.
     */    
    public final static int FONT_TYPE_TT = 1;
    /** The font is CJK.
     */    
    public final static int FONT_TYPE_CJK = 2;
    /** The font is True Type with a Unicode encoding.
     */    
    public final static int FONT_TYPE_TTUNI = 3;
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
    public final static boolean EMBEDDED = true;
    
/** if the font doesn't have to be embedded */
    public final static boolean NOT_EMBEDDED = false;
/** if the font has to be cached */
    public final static boolean CACHED = true;
/** if the font doesn't have to be cached */
    public final static boolean NOT_CACHED = false;
    
    /** The font type.
     */    
    int fontType;
/** a not defined character in a custom PDF encoding */
    public final static String notdef = ".notdef";
    
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
    
    /** The subset prefix to be added to the font name when the font is embedded.
     */    
    protected static char subsetPrefix[] = {'A', 'B', 'C', 'D', 'E', 'E', '+'};
    
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
    class StreamFont extends PdfStream {
        
        /** Generates the PDF stream with the Type1 and Truetype fonts returning
         * a PdfStream.
         * @param contents the content of the stream
         * @param lengths an array of int that describes the several lengths of each part of the font
         * @throws DocumentException error in the stream compression
         */
        public StreamFont(byte contents[], int lengths[]) throws DocumentException {
            try {
                bytes = contents;
                dictionary.put(PdfName.LENGTH, new PdfNumber(bytes.length));
                for (int k = 0; k < lengths.length; ++k) {
                    dictionary.put(new PdfName("Length" + (k + 1)), new PdfNumber(lengths[k]));
                }
                flateCompress();
            }
            catch (Exception e) {
                throw new DocumentException(e.getMessage());
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
        }
        else if (nameBase.toLowerCase().endsWith(".ttf") || nameBase.toLowerCase().indexOf(".ttc,") > 0) {
            if (encoding.equals(IDENTITY_H) || encoding.equals(IDENTITY_V))
                fontBuilt = new TrueTypeFontUnicode(name, encoding, embedded, ttfAfm);
            else
                fontBuilt = new TrueTypeFont(name, encoding, embedded, ttfAfm);
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
    protected void createEncoding() throws UnsupportedEncodingException {
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
                s = new String(b, encoding);
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
    protected abstract int getRawWidth(int c, String name);
    
    /**
     * Gets the kerning between two Unicode chars.
     * @param char1 the first char
     * @param char2 the second char
     * @return the kerning to be applied
     */
    public abstract int getKerning(char char1, char char2);
    
    /**
     * Gets the width of a <CODE>char</CODE> in normalized 1000 units.
     * @param char1 the unicode <CODE>char</CODE> to get the width of
     * @return the width in normalized 1000 units
     */
    public int getWidth(char char1) {
        return getWidth(new String(new char[]{char1}));
    }
    
    /**
     * Gets the width of a <CODE>String</CODE> in normalized 1000 units.
     * @param text the <CODE>String</CODE> to get the witdth of
     * @return the width in normalized 1000 units
     */
    public int getWidth(String text) {
        int total = 0;
        try {
            byte mbytes[] = text.getBytes(encoding);
            for (int k = 0; k < mbytes.length; ++k)
                total += widths[0xff & mbytes[k]];
        }
        catch (UnsupportedEncodingException e) {
        }
        return total;
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
        try {
            return text.getBytes(encoding);
        }
        catch (UnsupportedEncodingException e) {
            // Panic! We should not be here
            e.printStackTrace();
            return text.getBytes();
        }
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
    String createSubsetPrefix() {
        synchronized(subsetPrefix) {
            for (int k = 0; k < subsetPrefix.length - 1; ++k) {
                int c = subsetPrefix[k];
                if (c == 'Z')
                    subsetPrefix[k] = 'A';
                else {
                    subsetPrefix[k] = (char)(c + 1);
                    break;
                }
            }
            return new String(subsetPrefix);
        }
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
}
