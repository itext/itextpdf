/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
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
 * Thanks to Javier Escote (jescote@deister.es) for the suggestion that more sizes should be supported.
 *
 * Very special thanks to Stefan Mainz (Stefan.Mainz@Dynaware.de) from Dynaware Systemberatung GmbH
 * for different suggestions to optimize the font-classes.
 */

package com.lowagie.text.pdf;

//import com.lowagie.text.pdf.font.*;

/**
 * <CODE>PdfFontMetrics</CODE> is an abstract superclass containing methods to retrieve
 * the metrics of a font.
 * <P>
 * There are the 14 Type 1 fonts that inherit from this superclass:
 * courier, courier bold, courier oblique, courier boldoblique,
 * helvetica, helvetica bold, helvetica oblique, helvetica boldoblique,
 * symbol, times roman, times bold, times italic, times bolditalic,
 * zapfdingbats).<BR>
 * There are 4 standard encodings: standard, MacRoman, MacExpert, WinAnsi
 * and MacExpert. However only the standard encoding is supported in these classes.<BR>
 * The FontMetrics are generated from the Adobe Font Metrics-files (AFM-files).
 * These files can be found on the following FTP-site:
 * ftp://ftp.adobe.com/pub/adobe/type/win/all/afmfiles/
 * (subdirectories /base17 and /001-050/003)
 */

public abstract class PdfFontMetrics {
    
    // static membervariables for the fonttypes
    
/** This is a possible value of a base 14 type 1 font */
    public static final int COURIER = 0;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int COURIER_BOLD = 1;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int COURIER_OBLIQUE = 2;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int COURIER_BOLDOBLIQUE = 3;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int HELVETICA = 4;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int HELVETICA_BOLD = 5;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int HELVETICA_OBLIQUE = 6;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int HELVETICA_BOLDOBLIQUE = 7;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int SYMBOL = 8;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int TIMES_ROMAN = 9;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int TIMES_BOLD = 10;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int TIMES_ITALIC = 11;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int TIMES_BOLDITALIC = 12;
    
/** This is a possible value of a base 14 type 1 font */
    public static final int ZAPFDINGBATS = 13;
    
    // static membervariables for some standard fontsizes (not necessary)
    
/** This is a possible value of a fontsize */
    public static final float SIZE_8 = 8;
    
/** This is a possible value of a fontsize */
    public static final float SIZE_10 = 10;
    
/** This is a possible value of a fontsize */
    public static final float SIZE_12 = 12;
    
/** This is a possible value of a fontsize */
    public static final float SIZE_14 = 14;
    
/** This is a possible value of a fontsize */
    public static final float SIZE_18 = 18;
    
/** This is a possible value of a fontsize */
    public static final float SIZE_24 = 24;
    
    // static membervariables for the different encodings
    
/** This is a possible value of an encoding */
    public static final int STANDARD = 0;
    
/** This is a possible value of an encoding */
    public static final int MAC_ROMAN = 1;
    
/** This is a possible value of an encoding */
    public static final int MAC_EXPERT = 2;
    
/** This is a possible value of an encoding */
    public static final int WIN_ANSI = 3;
    
/** this is an array containing all encodings */
    private static PdfObject[] encodings;
    
    static {
        encodings	= new PdfObject[4];
        
        encodings[STANDARD]		= PdfNull.PDFNULL;
        encodings[MAC_ROMAN]		= PdfName.MAC_ROMAN_ENCODING;
        encodings[MAC_EXPERT]	= PdfName.MAC_EXPERT_ENCODING;
        encodings[WIN_ANSI]		= PdfName.WIN_ANSI_ENCODING;
    }
    
    // static membervariables for the different styles
    
/** this is a possible style. */
    public static final int NORMAL		= 0;
    
/** this is a possible style. */
    public static final int BOLD		= 1;
    
/** this is a possible style. */
    public static final int ITALIC		= 2;
    
/** this is a possible style. */
    public static final int BOLDITALIC	= 3;
    
    // static membervariables of some ASCII characters
    
/** this is the value of a certain ASCII character */
    public static final char SPACE = 32;
    
/** this is the value of a certain ASCII character */
    public static final char ZERO = 48;
    
/** this is the value of a certain ASCII character */
    public static final char ELLIPSIS_STANDARD = 188;
    
/** this is the value of a certain ASCII character */
    public static final char ELLIPSIS_MAC_ROMAN = 201;
    
/** this is the value of a certain ASCII character */
    public static final char ELLIPSIS_WIN_ANSI = 133;
    
    // membervariables
    
/** The encoding of the font. */
    private int encoding;
    
/** The size of the font. */
    private float size;
    
/** Array containing the width of each character. */
    private int[] width;
    
/** Twodimensional array containing the kerning of certain character pairs. */
    private int[][] kerning = null;
    
    // constructors
    
/**
 * Construct a <CODE>PdfFontMetrics</CODE> object.
 *
 * @param	encoding	the encoding that will be used.
 */
    
    protected PdfFontMetrics(int encoding, float size) {
        this.encoding = checkEncoding(encoding);
        this.size = checkFontsize(size);
    }
    
    // methods
    
    
/**
 * Sets the array containing the width of each character.
 *
 * @param	width	an array
 * @return	<CODE>void</CODE>
 */
    
    protected final void setWidth(int[] width) {
        this.width = width;
    }
    
/**
 * Sets the twodimensional array containing the kerning of certain character pairs.
 *
 * @param	kerning	a twodimensional array
 * @return	<CODE>void</CODE>
 */
    
    protected final void setKerning(int[][] kerning) {
        this.kerning = kerning;
    }
    
/**
 * Gets the name of the font.
 *
 * return	a name
 */
    
    public abstract PdfName name();
    
/**
 * Gets the name of the encoding of the font.
 *
 * return	a name
 */
    
    final PdfObject encoding() {
        return encodings[encoding];
    }
    
/**
 * Gets the size of the font.
 *
 * return	a name
 */
    
    final float size() {
        return size;
    }
    
/**
 * Gets the width of a certain character.
 *
 * @param	character		a certain character
 * @return	a width
 */
    
    private final int width(char character) {
        return width[(int) character & 0xFF];
    }
    
/**
 * Returns the approximative text space (= user space) width of a character in this font.
 * <P>
 * 1000 font metrics units correspond to 1 unit in text space.
 *
 * @param	character		a character
 * @return	the width of this character
 */
    
    final double widthTextSpace(char character) {
        return 0.001 * ((double) width(character) * size);
    }
    
/**
 * Returns the approximative text space (= user space) width of a character in this font.
 * <P>
 * 1000 font metrics units correspond to 1 unit in text space.
 * I have chosen the width of ZERO as approximative characterwidth.
 *
 * @return		the approximative with of a character
 */
    
    final double widthTextSpace() {
        return widthTextSpace(SPACE);
    }
    
/**
 * Gets the kerning of a certain pair of characters.
 *
 * @param	character1	the first character
 * @param	character2	the second character
 * @return	the kerning
 *
 * @author	Stefan Mainz
 */
    
    public int kerning(char character1, char character2) {
        
        if (kerning == null) return 0;
        
        int char1 = (int) character1 & 0xFF;
        int char2 = (int) character2 & 0xFF;
        
        int[] kerningsForFirstChar = kerning[char1];
        if (kerningsForFirstChar == null) {
            return 0;
        }
        
        for (int i = 0; i < kerningsForFirstChar.length; i += 2) {
            if (kerningsForFirstChar[i] == char2) {
                return kerningsForFirstChar[i + 1];
            }
            if (kerningsForFirstChar[i] > char2) {
                return 0;
            }
        }
        
        return 0;
    }
    
/**
 * Gets the fonttype of the font from the same family, but in another style.
 *
 * @param	style		a certain style
 * @return	a fonttype
 */
    
    public abstract int getStyle(int style);
    
/**
 * Gets the character that represents an 'ellipsis'.
 *
 * @return		a <CODE>char</CODE>
 */
    
    char ellipsis() {
        switch(encoding) {
            case MAC_ROMAN:
                return ELLIPSIS_MAC_ROMAN;
            case WIN_ANSI:
                return ELLIPSIS_WIN_ANSI;
                default:
                    return ELLIPSIS_STANDARD;
        }
    }
    
/**
 * Checks if a character can be used to split a <CODE>PdfString</CODE>.
 * <P>
 * for the moment every character less than or equal to SPACE and the character '-' are 'splitCharacters'.
 *
 * @param	character		the character that has to be checked
 * @return	<CODE>true</CODE> if the character can be used to split a string, <CODE>false</CODE> otherwise
 */
    
    static boolean isSplitCharacter(char character) {
        if (character <= SPACE) {
            return true;
        }
        switch(character) {
            case ' ':
            case '-':
            case '\t':
                return true;
                default:
                    return false;
        }
    }
    
/**
 * Removes all the <VAR>' '</VAR> and <VAR>'-'</VAR>-characters on the right of a <CODE>String</CODE>.
 * <P>
 * @param	string		the <CODE>String<CODE> that has to be trimmed.
 * @return	the trimmed <CODE>String</CODE>
 */
    
    static String trim(String string) {
        while (string.endsWith(" ") || string.endsWith("\t")) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }
    
    // methods to check the parameters of a font
    
/**
 * Returns a legal value of a fonttype.
 *
 * @param		fonttype		a given fonttype
 * @return		the given fonttype if it was legal; the default fonttype if it wasn't.
 */
    
    static final int checkFonttype(int fonttype) {
        if (fonttype < 0 || fonttype > 13) return COURIER;
        return fonttype;
    }
    
/**
 * Returns a legal value of a fontsize.
 *
 * @param		fontsize		a given fontsize
 * @return		the given fontsize if it was legal; the default fontsize if it wasn't.
 */
    
    static final float checkFontsize(float fontsize) {
        if (fontsize < 0 || fontsize > 1000) return SIZE_12;
        return fontsize;
    }
    
/**
 * Returns a legal value of an encoding.
 *
 * @param		e				a given encoding
 * @return		the given encoding if it was legal; the default encoding if it wasn't.
 */
    
    static final int checkEncoding(int e) {
        if (e < 0 || e > 3) {
            return WIN_ANSI;
        }
        return e;
    }
    
/**
 * Returns the name of the encoding.
 *
 * @param		e				a given value of an encoding
 * @return		a name of an encoding
 * @deprecated
 */
    
    static final PdfObject getEncoding(int e) {
        return encodings[checkEncoding(e)];
    }
}
