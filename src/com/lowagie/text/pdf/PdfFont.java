/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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

import com.lowagie.text.Image;
import com.lowagie.text.ExceptionConverter;

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
    
    PdfFont(String name, int f, float s, int e) {
        String fontName = BaseFont.HELVETICA;
        size = s;
        switch (f) {
            case COURIER:
                fontName = BaseFont.COURIER;
                break;
            case COURIER_BOLD:
                fontName = BaseFont.COURIER_BOLD;
                break;
            case COURIER_OBLIQUE:
                fontName = BaseFont.COURIER_OBLIQUE;
                break;
            case COURIER_BOLDOBLIQUE:
                fontName = BaseFont.COURIER_BOLDOBLIQUE;
                break;
            case HELVETICA:
                fontName = BaseFont.HELVETICA;
                break;
            case HELVETICA_BOLD:
                fontName = BaseFont.HELVETICA_BOLD;
                break;
            case HELVETICA_OBLIQUE:
                fontName = BaseFont.HELVETICA_OBLIQUE;
                break;
            case HELVETICA_BOLDOBLIQUE:
                fontName = BaseFont.HELVETICA_BOLDOBLIQUE;
                break;
            case SYMBOL:
                fontName = BaseFont.SYMBOL;
                break;
            case TIMES_ROMAN:
                fontName = BaseFont.TIMES_ROMAN;
                break;
            case TIMES_BOLD:
                fontName = BaseFont.TIMES_BOLD;
                break;
            case TIMES_ITALIC:
                fontName = BaseFont.TIMES_ITALIC;
                break;
            case TIMES_BOLDITALIC:
                fontName = BaseFont.TIMES_BOLDITALIC;
                break;
            case ZAPFDINGBATS:
                fontName = BaseFont.ZAPFDINGBATS;
                break;
        }
        try {
            font = BaseFont.createFont(fontName, BaseFont.WINANSI, false);
        }
        catch (Exception ee) {
            throw new ExceptionConverter(ee);
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
    
    PdfFont(BaseFont bf, float size) {
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
    
    public int compareTo(Object object) {
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
    
    BaseFont getFont() {
        return font;
    }
    
    void setImage(Image image) {
        this.image = image;
    }
}
