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
 * Special thanks to Javier Escote who fixed a bug in the method 'split' in rugPdf0.10
 * http://www.deister.es/ (jescote@deister.es)
 * However, I rewrote this method entirely in iText0.30.
 */

package com.lowagie.text.pdf;

import java.util.ArrayList;
import java.util.Iterator;
import com.lowagie.text.Image;

/**
 * <CODE>PdfFont</CODE> is the Pdf Font object.
 * <P>
 * Limitation: in this class only base 14 Type 1 fonts (courier, courier bold, courier oblique,
 * courier boldoblique, helvetica, helvetica bold, helvetica oblique, helvetica boldoblique,
 * symbol, times roman, times bold, times italic, times bolditalic, zapfdingbats) and their
 * standard encoding (standard, MacRoman, (MacExpert,) WinAnsi) are supported.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.7 (page 198-203).
 *
 * @see		PdfName
 * @see		PdfDictionary
 * @see		BadPdfFormatException
 */

class PdfFont implements Comparable {
    
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
    // membervariables
    
/** the name of this font. */
    private PdfName name;
    
/** the font metrics. */
    private BaseFont font;
    
/** the size. */
    private float size;
    
/** an image. */
    protected Image image;
    
    // constructors
    
/**
 * Constructs a new <CODE>PdfFont</CODE>-object.
 *
 * @param		name		name of the font
 * @param		f			the base 14 type
 * @param		s			value of the size
 * @param		e			value of the encodingtype
 */
    
    PdfFont(String name, int f, float s, int e)
    {
        String fontName = "Helvetica";
        size = s;
        switch (f) {
            case COURIER:
                fontName = "Courier";
                break;
            case COURIER_BOLD:
                fontName = "Courier-Bold";
                break;
            case COURIER_OBLIQUE:
                fontName = "Courier-Oblique";
                break;
            case COURIER_BOLDOBLIQUE:
                fontName = "Courier-BoldOblique";
                break;
            case HELVETICA:
                fontName = "Helvetica";
                break;
            case HELVETICA_BOLD:
                fontName = "Helvetica-Bold";
                break;
            case HELVETICA_OBLIQUE:
                fontName = "Helvetica-Oblique";
                break;
            case HELVETICA_BOLDOBLIQUE:
                fontName = "Helvetica-BoldOblique";
                break;
            case SYMBOL:
                fontName = "Symbol";
                break;
            case TIMES_ROMAN:
                fontName = "Times-Roman";
                break;
            case TIMES_BOLD:
                fontName = "Times-Bold";
                break;
            case TIMES_ITALIC:
                fontName = "Times-Italic";
                break;
            case TIMES_BOLDITALIC:
                fontName = "Times-BoldItalic";
                break;
            case ZAPFDINGBATS:
                fontName = "ZapfDingbats";
                break;
        }
        try {
            font = BaseFont.createFont(fontName, "winansi", false);
        }
        catch (Exception ee) {
            throw new NullPointerException(ee.getMessage());
        }
    }
    
/**
 * Constructs a new <CODE>PdfFont</CODE>-object.
 *
 * @param		f			the base 14 type
 * @param		s			value of the size
 * @param		e			value of the encodingtype
 */
    
    PdfFont(int f, float s, int e) {
        this(new StringBuffer("F").append(f).toString(), f, s, e);
    }
    
/**
 * Constructs a new <CODE>PdfFont</CODE>-object.
 *
 * @param		f			the base 14 type
 * @param		s			value of the size
 */
    
    PdfFont(int f, float s) {
        this(new StringBuffer("F").append(f).toString(), f, s, -1);
    }
    
    PdfFont(BaseFont bf, float size)
    {
        this.size = size;
        font = bf;
    }
    
    // methods
    
/**
 * Compares this <CODE>PdfFont</CODE> with another
 *
 * @param	object	the other <CODE>PdfFont</CODE>
 * @return	a value
 */
    
    public final int compareTo(Object object) {
        if (image != null)
            return 0;
        if (object == null) {
            return -1;
        }
        PdfFont pdfFont;
        try {
            pdfFont = (PdfFont) object;
            if (font != pdfFont.font) {
                return 1;
            }
            if (this.size() != pdfFont.size()) {
                return 2;
            }
            return 0;
        }
        catch(ClassCastException cce) {
            return -2;
        }
    }
    
/**
 * Returns the size of this font.
 *
 * @return		a size
 */
    
    float size() {
        if (image == null)
            return size;
        else {
            return image.scaledHeight();
        }
    }
    
/**
 * Returns the name of this font.
 *
 * @return		a <CODE>PdfName</CODE>
 */
    
    PdfName getName() {
        return name;
    }
    
    void setName(PdfName name) {
        this.name = name;
    }
    
/**
 * Returns the approximative width of 1 character of this font.
 *
 * @return		a width in Text Space
 */
    
    float width() {
        if (image == null)
            return font.getWidthPoint(" ", size);
        else
            return image.scaledWidth();
    }
    
/**
 * Returns the width of a certain character of this font.
 *
 * @param		character	a certain character
 * @return		a width in Text Space
 */
    
    float width(char character) {
        if (image == null)
            return font.getWidthPoint(character, size);
        else
            return image.scaledWidth();
    }
    
/**
 * Returns the width (user space) of a <CODE>PdfPrintable</CODE>-object.
 *
 * @param		text		a <CODE>PdfPrintable</CODE>-object
 * @return		a width
 */
    
    float width(PdfPrintable text) {
        if (image == null)
            return font.getWidthPoint(text.toString(), size);
        else
            return image.scaledWidth();
        
    }
    
    BaseFont getFont()
    {
        return font;
    }
    
    void setImage(Image image)
    {
        this.image = image;
    }
}
