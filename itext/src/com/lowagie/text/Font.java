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
 */

package com.lowagie.text;

import java.awt.Color;
import java.util.Properties;

import com.lowagie.text.pdf.BaseFont;

/**
 * Contains all the specifications of a font: fontfamily, size, style and color.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * Paragraph p = new Paragraph("This is a paragraph",
 *               <STRONG>new Font(Font.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0, 255))</STRONG>);
 * </PRE></BLOCKQUOTE>
 *
 * @author  bruno@lowagie.com
 */

public class Font implements Comparable {
    
    // static membervariables for the different families
    
/** a possible value of a font family. */
    public static final int COURIER = 0;
    
/** a possible value of a font family. */
    public static final int HELVETICA = 1;
    
/** a possible value of a font family. */
    public static final int TIMES_NEW_ROMAN = 2;
    
/** a possible value of a font family. */
    public static final int SYMBOL = 3;
    
/** a possible value of a font family. */
    public static final int ZAPFDINGBATS = 4;
    
    // static membervariables for the different styles
    
/** this is a possible style. */
    public static final int NORMAL		= 0;
    
/** this is a possible style. */
    public static final int BOLD		= 1;
    
/** this is a possible style. */
    public static final int ITALIC		= 2;
    
/** this is a possible style. */
    public static final int UNDERLINE	= 4;
    
/** this is a possible style. */
    public static final int STRIKETHRU	= 8;
    
/** this is a possible style. */
    public static final int BOLDITALIC	= BOLD | ITALIC;
    
    // static membervariables
    
/** the value of an undefined attribute. */
    public static final int UNDEFINED = -1;
    
/** the value of the default size. */
    public static final int DEFAULTSIZE = 12;
    
    // membervariables
    
/** the value of the fontfamily. */
    private int family = UNDEFINED;
    
/** the value of the fontsize. */
    private float size = UNDEFINED;
    
/** the value of the style. */
    private int style = UNDEFINED;
    
/** the value of the color. */
    private Color color = null;
    
/** the external font */
    private BaseFont baseFont = null;
    
    // constructors
    
/**
 * Constructs a Font.
 *
 * @param	family	the family to which this font belongs
 * @param	size	the size of this font
 * @param	style	the style of this font
 * @param	color	the <CODE>Color</CODE> of this font.
 */
    
    public Font(int family, float size, int style, Color color) {
        this.family = family;
        this.size = size;
        this.style = style;
        this.color = color;
    }
    
/**
 * Constructs a Font.
 *
 * @param	bf	    the external font
 * @param	size	the size of this font
 * @param	style	the style of this font
 * @param	color	the <CODE>Color</CODE> of this font.
 */
    
    public Font(BaseFont bf, float size, int style, Color color) {
        this.family = family;
        this.size = size;
        this.style = style;
        this.color = color;
        this.baseFont = bf;
    }
    
/**
 * Constructs a Font.
 *
 * @param	bf	    the external font
 * @param	size	the size of this font
 * @param	style	the style of this font
 */
    public Font(BaseFont bf, float size, int style) {
        this(bf, size, style, null);
    }
    
/**
 * Constructs a Font.
 *
 * @param	bf	    the external font
 * @param	size	the size of this font
 */
    public Font(BaseFont bf, float size) {
        this(bf, size, UNDEFINED, null);
    }
    
/**
 * Constructs a Font.
 *
 * @param	bf	    the external font
 */
    public Font(BaseFont bf) {
        this(bf, UNDEFINED, UNDEFINED, null);
    }
    
/**
 * Constructs a Font.
 *
 * @param	family	the family to which this font belongs
 * @param	size	the size of this font
 * @param	style	the style of this font
 */
    
    public Font(int family, float size, int style) {
        this(family, size, style, null);
    }
    
/**
 * Constructs a Font.
 *
 * @param	family	the family to which this font belongs
 * @param	size	the size of this font
 */
    
    public Font(int family, float size) {
        this(family, size, UNDEFINED, null);
    }
    
/**
 * Constructs a Font.
 *
 * @param	family	the family to which this font belongs
 */
    
    public Font(int family) {
        this(family, UNDEFINED, UNDEFINED, null);
    }
    
/**
 * Constructs a Font.
 */
    
    public Font() {
        this(UNDEFINED, UNDEFINED, UNDEFINED, null);
    }
    
/**
 * Constructs a Font.
 */
    
    public Font(Properties attributes) {
        this(UNDEFINED, UNDEFINED, UNDEFINED, null);
        String value;
        if ((value = attributes.getProperty(ElementTags.FONT)) != null) {
            setFamily(value);
        }
        if ((value = attributes.getProperty(ElementTags.SIZE)) != null) {
            setSize(Float.valueOf(value + "f").floatValue());
        }
        if ((value = attributes.getProperty(ElementTags.STYLE)) != null) {
            setStyle(value);
        }
        String r = attributes.getProperty(ElementTags.RED);
        String g = attributes.getProperty(ElementTags.GREEN);
        String b = attributes.getProperty(ElementTags.BLUE);
        if (r != null || g != null || b != null) {
            int red = 0;
            int green = 0;
            int blue = 0;
            if (r != null) red = Integer.parseInt(r);
            if (g != null) green = Integer.parseInt(g);
            if (b != null) blue = Integer.parseInt(b);
            setColor(new Color(red, green, blue));
        }
        else if ((value = attributes.getProperty(ElementTags.COLOR)) != null) {
            setColor(ElementTags.decodeColor(value));
        }
    }
    
    // implementation of the Comparable interface
    
/**
 * Compares this <CODE>Font</CODE> with another
 *
 * @param	object	the other <CODE>Font</CODE>
 * @return	a value
 */
    
    public int compareTo(Object object) {
        if (object == null) {
            return -1;
        }
        Font font;
        try {
            font = (Font) object;
            if (this.family != font.family()) {
                return 1;
            }
            if (this.size != font.size()) {
                return 2;
            }
            if (this.style != font.style()) {
                return 3;
            }
            if (this.color == null) {
                if (font.color == null) {
                    return 0;
                }
                return 4;
            }
            if (font.color == null) {
                return 4;
            }
            if (this.color.equals(font.color())) {
                return 0;
            }
            return 4;
        }
        catch(ClassCastException cce) {
            return -2;
        }
    }
    
    // methods
    
/**
 * Sets the family using a <CODE>String</CODE> ("Courier",
 * "Helvetica", "Times New Roman", "Symbol" or "ZapfDingbats").
 *
 * @param	family		A <CODE>String</CODE> representing a certain font-family.
 */
    
    public void setFamily(String family) {
        this.family = getFamilyIndex(family);
    }
    
/**
 * Translates a <CODE>String</CODE>-value of a certain family
 * into the index that is used for this family in this class.
 *
 * @param	family		A <CODE>String</CODE> representing a certain font-family
 * @return	the corresponding index
 */
    
    public static int getFamilyIndex(String family) {
        if (family.equalsIgnoreCase(ElementTags.COURIER)) {
            return COURIER;
        }
        if (family.equalsIgnoreCase(ElementTags.HELVETICA)) {
            return HELVETICA;
        }
        if (family.equalsIgnoreCase(ElementTags.TIMES_NEW_ROMAN)) {
            return TIMES_NEW_ROMAN;
        }
        if (family.equalsIgnoreCase(ElementTags.SYMBOL)) {
            return SYMBOL;
        }
        if (family.equalsIgnoreCase(ElementTags.ZAPFDINGBATS)) {
            return ZAPFDINGBATS;
        }
        return UNDEFINED;
    }
    
/**
 * Sets the size.
 *
 * @param	size		The new size of the font.
 */
    
    public void setSize(float size) {
        this.size = size;
    }
    
/**
 * Sets the style using a <CODE>String</CODE> containing one of
 * more of the following values: normal, bold, italic, underline, strike.
 *
 * @param	style	A <CODE>String</CODE> representing a certain style.
 */
    
    public void setStyle(String style) {
        this.style = getStyleValue(style);
    }
    
/**
 * Translates a <CODE>String</CODE>-value of a certain style
 * into the index value is used for this style in this class.
 *
 * @param	style			A <CODE>String</CODE>
 * @return	the corresponding value
 */
    
    public static int getStyleValue(String style) {
        int s = 0;
        if (style.indexOf(ElementTags.NORMAL) != -1) {
            s |= NORMAL;
        }
        if (style.indexOf(ElementTags.BOLD) != -1) {
            s |= BOLD;
        }
        if (style.indexOf(ElementTags.ITALIC) != -1) {
            s |= ITALIC;
        }
        if (style.indexOf(ElementTags.UNDERLINE) != -1) {
            s |= UNDERLINE;
        }
        if (style.indexOf(ElementTags.STRIKETHRU) != -1) {
            s |= STRIKETHRU;
        }
        return s;
    }
    
/**
 * Sets the color.
 *
 * @param	color		the new color of the font
 */
    
    public void setColor(Color color) {
        this.color = color;
    }
    
/**
 * Sets the color.
 *
 * @param	red			the red-value of the new color
 * @param	green		the green-value of the new color
 * @param	blue		the blue-value of the new color
 */
    
    public void setColor(int red, int green, int blue) {
        this.color = new Color(red, green, blue);
    }
    
/**
 * Gets the leading that can be used with this font.
 *
 * @param	linespacing		a certain linespacing
 * @return	the height of a line
 */
    
    public float leading(float linespacing) {
        if (size == UNDEFINED) {
            return linespacing * DEFAULTSIZE;
        }
        return linespacing * size;
    }
    
/**
 * Checks if the properties of this font are undefined or null.
 * <P>
 * If so, the standard should be used.
 *
 * @return	a <CODE>boolean</CODE>
 */
    
    public boolean isStandardFont() {
        return (family == UNDEFINED
        && size == UNDEFINED
        && style == UNDEFINED
        && color == null
        && baseFont == null);
    }
    
/**
 * Replaces the attributes that are equal to <VAR>null</VAR> with
 * the attributes of a given font.
 *
 * @param	font	the font of a bigger element class
 * @return	a <CODE>Font</CODE>
 */
    
    public Font difference(Font font) {
        Font difference = new Font();
        if (font.family() == UNDEFINED) {
            difference.family = this.family;
        }
        else {
            difference.family = font.family;
        }
        if (font.size() == UNDEFINED) {
            difference.size = this.size;
        }
        else {
            difference.size = font.size;
        }
        int style1 = this.style;
        int style2 = font.style();
        if (style1 == UNDEFINED) style1 = 0;
        if (style2 == UNDEFINED) style2 = 0;
        difference.style = style1 | style2;
        if (font.color == null) {
            difference.color = this.color;
        }
        else {
            difference.color = font.color();
        }
        return difference;
    }
    
    // methods to retrieve the membervariables
    
/**
 * Gets the family of this font.
 *
 * @return	the value of the family
 */
    
    public int family() {
        return family;
    }
    
/**
 * Gets the size of this font.
 *
 * @return	a size
 */
    
    public float size() {
        return size;
    }
    
/**
 * Gets the style of this font.
 *
 * @return	a size
 */
    
    public int style() {
        return style;
    }
    
/**
 * checks if this font is Bold.
 *
 * @return	a <CODE>boolean</CODE>
 */
    
    public boolean isBold() {
        if (style == UNDEFINED) {
            return false;
        }
        return (style &	BOLD) == BOLD;
    }
    
/**
 * checks if this font is Bold.
 *
 * @return	a <CODE>boolean</CODE>
 */
    
    public boolean isItalic() {
        if (style == UNDEFINED) {
            return false;
        }
        return (style &	ITALIC) == ITALIC;
    }
    
/**
 * checks if this font is underlined.
 *
 * @return	a <CODE>boolean</CODE>
 */
    
    public boolean isUnderlined() {
        if (style == UNDEFINED) {
            return false;
        }
        return (style &	UNDERLINE) == UNDERLINE;
    }
    
/**
 * checks if the style of this font is STRIKETHRU.
 *
 * @return	a <CODE>boolean</CODE>
 */
    
    public boolean isStrikethru() {
        if (style == UNDEFINED) {
            return false;
        }
        return (style &	STRIKETHRU) == STRIKETHRU;
    }
    
/**
 * Gets the color of this font.
 *
 * @return	a color
 */
    
    public Color color() {
        return color;
    }
    
 /** Gets the <CODE>BaseFont</CODE> inside this object.
  * @return the <CODE>BaseFont</CODE>
  */
    
    public BaseFont getBaseFont()
    {
        return baseFont;
    }
}