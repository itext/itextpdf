/*
 * @(#)Font.java					0.39 2000/11/23
 *       release iText0.3:			0.25 2000/02/14
 *       release iText0.31:			0.25 2000/02/11
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie.
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
 * @version 0.39, 2000/11/23
 *
 * @since   iText0.30
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
	private int size = UNDEFINED;

	/** the value of the style. */
	private int style = UNDEFINED;

	/** the value of the color. */
	private Color color = null;

// constructors

	/**
	 * Constructs a Font.
	 *
	 * @param	family	the family to which this font belongs
	 * @param	size	the size of this font
	 * @param	style	the style of this font
	 * @paran	color	the <CODE>Color</CODE> of this font.
	 * @since iText0.30
	 */

	public Font(int family, int size, int style, Color color) {
		 this.family = family;
		 this.size = size;
		 this.style = style;
		 this.color = color;
	}

	/**
	 * Constructs a Font.
	 *
	 * @param	family	the family to which this font belongs
	 * @param	size	the size of this font
	 * @param	style	the style of this font
	 * @since iText0.30
	 */

	public Font(int family, int size, int style) {
		 this(family, size, style, null);
	}

	/**
	 * Constructs a Font.
	 *
	 * @param	family	the family to which this font belongs
	 * @param	size	the size of this font
	 * @since iText0.30
	 */

	public Font(int family, int size) {
		 this(family, size, UNDEFINED, null);
	}

	/**
	 * Constructs a Font.
	 *
	 * @param	family	the family to which this font belongs
	 * @since iText0.30
	 */

	public Font(int family) {
		 this(family, UNDEFINED, UNDEFINED, null);
	}

	/**
	 * Constructs a Font.
	 *
	 * @since iText0.30
	 */

	public Font() {
		this(UNDEFINED, UNDEFINED, UNDEFINED, null);
	}

// implementation of the Comparable interface

	/**
	 * Compares this <CODE>Font</CODE> with another
	 *
	 * @param	object	the other <CODE>Font</CODE>
	 * @return	a value
	 *
	 * @since	iText0.30
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
     * Gets the leading that can be used with this font. 
     *
	 * @param	linespacing		a certain linespacing
     * @return	the height of a line
     * @since	itext0.30
     */

    public int leading(double linespacing) {
		if (size == UNDEFINED) {
			return (int) (linespacing * (double) DEFAULTSIZE + 0.5);
		}
		return (int) (linespacing * (double) size + 0.5);
	}

	/**
	 * Checks if the properties of this font are undefined or null.
	 * <P>
	 * If so, the standard should be used.
	 *
	 * @return	a <CODE>boolean</CODE>
	 *
	 * @since	iText0.30
	 */

	public boolean isStandardFont() {
		return (family == UNDEFINED
				&& size == UNDEFINED
				&& color == null);
	}

	/**
	 * Replaces the attributes that are equal to <VAR>null</VAR> with
	 * the attributes of a given font.
	 *
	 * @param	font	the font of a bigger element class
	 * @return	a <CODE>Font</CODE>
	 *
	 * @since	iText0.30
	 */

	public Font difference(Font font) {
		Font difference = new Font();
		if (this.family != font.family()) {
			difference.family = this.family;
		}
		if (this.size != font.size()) {
			difference.size = this.size;
		}
		if (this.style != font.style()) {
			if (this.style != UNDEFINED) {
				difference.style = this.style;
				if (font.style() != UNDEFINED) {
					difference.style -= this.style & font.style();
				}
			}
		}
		if (this.color == null || ! this.color.equals(font.color())) {
			difference.color = this.color;
		}
		return difference;
	}

// methods to retrieve the membervariables

    /**
     * Gets the family of this font. 
     *
	 * @return	the value of the family.
	 *
     * @since   iText0.30
     */

    public int family() {
		return family;
	}

    /**
     * Gets the size of this font. 
     *
     * @return	a size
	 *
     * @since	iText0.30
     */

    public int size() {
		return size;
	}

    /**
     * Gets the style of this font. 
     *
     * @return	a size
	 *
     * @since	iText0.30
     */

    public int style() {
		return style;
	}

	/**
	 * checks if this font is Bold.
	 *
	 * @return	a <CODE>boolean</CODE>
	 *
	 * @since	iText0.30
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
	 *
	 * @since	iText0.30
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
	 *
	 * @since	iText0.38
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
	 *
	 * @since	iText0.38
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
	 *
     * @since	iText0.30
     */

    public Color color() {
		return color;
	}

	/**
	 * Gives the String representation of a <CODE>Font</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 * 
	 * @since	iText0.30
	 */

	public String toString() {
		if (isStandardFont()) {
			return "";
		}
		StringBuffer buffer = new StringBuffer("<FONT>\n");
		if (family != UNDEFINED) {
			buffer.append("\t<FAMILY>");
			switch(family) {
			case COURIER:
				buffer.append("Courier");
				break;
			case HELVETICA:
				buffer.append("Helvetica");
				break;
			case TIMES_NEW_ROMAN:
				buffer.append("Times New Roman");
				break;
			case SYMBOL:
				buffer.append("Symbol");
				break;
			case ZAPFDINGBATS:
				buffer.append("ZapfDingbats");
				break;
			}
			buffer.append("</FAMILY>\n");
		}
		if (size != UNDEFINED) {
			buffer.append("\t<SIZE>");
			buffer.append(size);
			buffer.append("</SIZE>\n");
		}
		if (style != UNDEFINED) {
			buffer.append("\t<STYLE>");
			switch(style & BOLDITALIC) {
			case NORMAL:
				buffer.append("normal");
				break;
			case BOLD:
				buffer.append("bold");
				break;
			case ITALIC:
				buffer.append("italic");
				break;
			case BOLDITALIC:
				buffer.append("bold, italic");
				break;
			}
			buffer.append("</STYLE>\n");
		}
		if (color != null) {
			buffer.append("\t<COLOR>\n");
			buffer.append("\t\t<RED>");
			buffer.append(color.getRed());
			buffer.append("</RED>\n");;
			buffer.append("\t\t<GREEN>");
			buffer.append(color.getGreen());
			buffer.append("</GREEN>\n"); ;
			buffer.append("\t\t<BLUE>");
			buffer.append(color.getBlue());
			buffer.append("</BLUE>\n");
			buffer.append("\t</COLOR>\n");
		}
		return buffer.append("</FONT>\n").toString();
	}
}