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
    
/** a not defined character in a custom PDF encoding */
    public final static String notdef = new String(".notdef");
    
/** table of characters widths for this encoding */
    protected int widths[] = new int[256];
    
/** encoding names */
    protected String differences[] = new String[256];
    
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
    
    static
    {
        BuiltinFonts14.put("Courier", PdfName.COURIER);
        BuiltinFonts14.put("Courier-Bold", PdfName.COURIER_BOLD);
        BuiltinFonts14.put("Courier-BoldOblique", PdfName.COURIER_BOLDOBLIQUE);
        BuiltinFonts14.put("Courier-Oblique", PdfName.COURIER_OBLIQUE);
        BuiltinFonts14.put("Helvetica", PdfName.HELVETICA);
        BuiltinFonts14.put("Helvetica-Bold", PdfName.HELVETICA_BOLD);
        BuiltinFonts14.put("Helvetica-BoldOblique", PdfName.HELVETICA_BOLDOBLIQUE);
        BuiltinFonts14.put("Helvetica-Oblique", PdfName.HELVETICA_OBLIQUE);
        BuiltinFonts14.put("Symbol", PdfName.SYMBOL);
        BuiltinFonts14.put("Times-Roman", PdfName.TIMES_ROMAN);
        BuiltinFonts14.put("Times-Bold", PdfName.TIMES_BOLD);
        BuiltinFonts14.put("Times-BoldItalic", PdfName.TIMES_BOLDITALIC);
        BuiltinFonts14.put("Times-Italic", PdfName.TIMES_ITALIC);
        BuiltinFonts14.put("ZapfDingbats", PdfName.ZAPFDINGBATS);
    }
    
    class StreamFont extends PdfStream {
        
/**
 * Generates the PDF stream with the Type1 and Truetype fonts returning
 * a PdfStream.
 * @param contents the content of the stream
 * @param lengths an array of int that describes the several lengths of each part of the font
 * @throws DocumentException error in the stream compression
 */
        public StreamFont(byte contents[], int lengths[]) throws DocumentException
        {
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
    
/**
 * Creates a new font. This font can be one of the 14 built in types,
 * a Type1 font referred by an AFM file, a TrueType font or a CJK font from the
 * Adobe Asian Font Pack. TrueType fonts and CJK fonts can have an optional style modifier
 * appended to the name. These modifiers are: Bold, Italic and BoldItalic. An
 * example would be "STSong-Light,Bold". Note that this modifiers do not work if
 * the font is embedded.
 * <P>
 * The fonts are cached and if they already exist they are extracted from the cache,
 * not parsed again.
 * @param name the name of the font or it's location on file
 * @param encoding the encoding to be applied to this font
 * @param embedded true if the font is to be embedded in the PDF
 * @return returns a new font. This font may come from the cache
 * @throws DocumentException the font is invalid
 * @throws IOException the font file could not be read
 */
    public static BaseFont createFont(String name, String encoding, boolean embedded) throws DocumentException, IOException
    {
        String nameBase = getBaseName(name);
        encoding = normalizeEncoding(encoding);
        boolean isBuiltinFonts14 = BuiltinFonts14.containsKey(name);
        boolean isCJKFont = isBuiltinFonts14 ? false : CJKFont.isCJKFont(nameBase, encoding);
        if (isBuiltinFonts14 || isCJKFont)
            embedded = false;
        BaseFont fontFound = null;
        BaseFont fontBuilt = null;
        String key = name + "\n" + encoding + "\n" + embedded;
        synchronized (fontCache) {
            fontFound = (BaseFont)fontCache.get(key);
        }
        if (fontFound != null)
            return fontFound;
        if (isBuiltinFonts14 || name.toLowerCase().endsWith(".afm")) {
            fontBuilt = new Type1Font(name, encoding, embedded);
        }
        else if (nameBase.toLowerCase().endsWith(".ttf")) {
            fontBuilt = new TrueTypeFont(name, encoding, embedded);
        }
        else if (isCJKFont)
            fontBuilt = new CJKFont(name, encoding, embedded);
        else
            throw new DocumentException("Font '" + name + "' with '" + encoding + "' is not recognized.");
        synchronized (fontCache) {
            fontFound = (BaseFont)fontCache.get(key);
            if (fontFound != null)
                return fontFound;
            fontCache.put(key, fontBuilt);
        }
        return fontBuilt;
    }
    
/**
 * Gets the name without the modifiers Bold, Italic or BoldItalic.
 * @return the name without the modifiers Bold, Italic or BoldItalic
 */
    protected static String getBaseName(String name)
    {
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
    protected static String normalizeEncoding(String enc)
    {
        if (enc.equals("winansi") || enc.equals(""))
            return "Cp1252";
        else if (enc.equals("macroman"))
            return "MacRoman";
        else
            return enc;
    }
    
/**
 * Creates the <CODE>widths</CODE> and the <CODE>differences</CODE> arrays
 * @throws UnsupportedEncodingException the encoding is not supported
 */
    protected void createEncoding() throws UnsupportedEncodingException
    {
        byte b[] = new byte[256];
        for (int k = 0; k < 256; ++k)
        {
            b[k] = (byte)(k);
        }
        if (fontSpecific) {
            for (int k = 0; k < 256; ++k)
                widths[k] = getRawWidth(k, null);
        }
        else {
            String s = new String(b, encoding);
            for (int k = 0; k < 256; ++k)
            {
                char c = s.charAt(k);
                String name = GlyphList.unicodeToName((int)c);
                if (name == null)
                    name = notdef;
                differences[k] = name;
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
    public int getWidth(char char1)
    {
        return getWidth(new String(new char[]{char1}));
    }
    
/**
 * Gets the width of a <CODE>String</CODE> in normalized 1000 units.
 * @param text the <CODE>String</CODE> to get the witdth of
 * @return the width in normalized 1000 units
 */
    public int getWidth(String text)
    {
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
    public float getWidthPoint(String text, float fontSize)
    {
        return (float)getWidth(text) * 0.001f * fontSize;
    }
    
/**
 * Gets the width of a <CODE>char</CODE> in points.
 * @param char1 the <CODE>char</CODE> to get the witdth of
 * @param fontSize the font size
 * @return the width in points
 */
    public float getWidthPoint(char char1, float fontSize)
    {
        return getWidth(char1) * 0.001f * fontSize;
    }
    
/**
 * Checks if a character can be used to split a <CODE>PdfString</CODE>.
 * <P>
 * for the moment every character less than or equal to SPACE and the character '-' are 'splitCharacters'.
 *
 * @param	c		the character that has to be checked
 * @return	<CODE>true</CODE> if the character can be used to split a string, <CODE>false</CODE> otherwise
 */
    public static boolean isSplitCharacter(char c)
    {
        if (c <= ' ') {
            return true;
        }
        switch(c) {
            case ' ':
            case '-':
            case '\t':
                return true;
                default:
                    return false;
        }
    }
    
/**
 * Converts a <CODE>String</CODE> to a </CODE>byte</CODE> array according
 * to the font's encoding.
 * @param text the <CODE>String</CODE> to be converted
 * @return an array of <CODE>byte</CODE> representing the conversion according to the font's encoding
 */
    byte[] convertToBytes(String text)
    {
        try {
            return text.getBytes(encoding);
        }
        catch (UnsupportedEncodingException e) {
            // Panic! We should not be here
            return text.getBytes();
        }
    }
    
/**
 * Generates the dictionary or stream required to represent the font.
 *  <CODE>index</CODE> will cycle from 0 to 2 with the next cycle beeing fed
 *  with the indirect reference from the previous cycle.
 * @param iobj an indirect reference to a Pdf object. May be null
 * @param index the type of object to generate. It may be 0, 1 or 2
 * @return the object requested
 * @throws DocumentException error in generating the object
 */
    abstract PdfObject getFontInfo(PdfIndirectReference iobj, int index) throws DocumentException;
    
/**
 * Gets the encoding used to convert <CODE>String</CODE> into <CODE>byte[]</CODE>.
 */
    public String getEncoding()
    {
        return encoding;
    }
}
