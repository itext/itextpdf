/*
 * @(#)PdfFont.java					0.25 2000/02/02
 *       release rugPdf0.10:		0.03 99/04/28
 *               rugPdf0.20:		0.13 99/11/30
 *               iText0.3:			0.25 2000/02/14
 *               iText0.35:         0.25 2000/08/11
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
 * Special thanks to Javier Escote who fixed a bug in the method 'split' in rugPdf0.10
 * http://www.deister.es/ (jescote@deister.es)
 * However, I rewrote this method entirely in iText0.30.
 */

package com.lowagie.text.pdf;

import java.util.ArrayList;
import java.util.Iterator;

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
 * @see		PdfFontMetrics
 * @see		PdfName
 * @see		PdfDictionary
 * @see		BadPdfFormatException
 *
 * @author  bruno@lowagie.com
 * @version 0.25 2000/02/02
 * @since   rugPdf0.10
 */

class PdfFont implements Comparable {

// membervariables

	/** the name of this font. */
	private PdfName name;

	/** the font metrics. */
	private BaseFont font;
    
    private float size;

// constructors

	/**
	 * Constructs a new <CODE>PdfFont</CODE>-object.
	 *
	 * @param		name		name of the font
	 * @param		f			the base 14 type
	 * @param		s			value of the size
	 * @param		e			value of the encodingtype
	 *
	 * @since		rugPdf0.10
	 */

	PdfFont(String name, int f, float s, int e)
    {
        String fontName = "Helvetica";
        size = s;
        switch (f) {
            case PdfFontMetrics.COURIER:
                fontName = "Courier";
                break;
            case PdfFontMetrics.COURIER_BOLD:
                fontName = "Courier-Bold";
                break;
            case PdfFontMetrics.COURIER_OBLIQUE:
                fontName = "Courier-Oblique";
                break;
            case PdfFontMetrics.COURIER_BOLDOBLIQUE:
                fontName = "Courier-BoldOblique";
                break;
            case PdfFontMetrics.HELVETICA:
                fontName = "Helvetica";
                break;
            case PdfFontMetrics.HELVETICA_BOLD:
                fontName = "Helvetica-Bold";
                break;
            case PdfFontMetrics.HELVETICA_OBLIQUE:
                fontName = "Helvetica-Oblique";
                break;
            case PdfFontMetrics.HELVETICA_BOLDOBLIQUE:
                fontName = "Helvetica-BoldOblique";
                break;
            case PdfFontMetrics.SYMBOL:
                fontName = "Symbol";
                break;
            case PdfFontMetrics.TIMES_ROMAN:
                fontName = "Times-Roman";
                break;
            case PdfFontMetrics.TIMES_BOLD:
                fontName = "Times-Bold";
                break;
            case PdfFontMetrics.TIMES_ITALIC:
                fontName = "Times-Italic";
                break;
            case PdfFontMetrics.TIMES_BOLDITALIC:
                fontName = "Times-BoldItalic";
                break;
            case PdfFontMetrics.ZAPFDINGBATS:
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
	 *
	 * @since		rugPdf0.10
	 */

	PdfFont(int f, float s, int e) {
		this(new StringBuffer("F").append(f).toString(), f, s, e);
	}
	 
	/**
	 * Constructs a new <CODE>PdfFont</CODE>-object.
	 *
	 * @param		f			the base 14 type
	 * @param		s			value of the size
	 *
	 * @since		rugPdf0.10
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
	 * @since	iText0.30
	 */

	public final int compareTo(Object object) {
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
	 *
	 * @since		rugPdf0.10
	 */

	float size() {
		return size;
	}

	/**
	 * Returns the name of this font.
	 *
	 * @return		a <CODE>PdfName</CODE>
	 *
	 * @since		rugPdf0.10
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
	 *
	 * @since		rugPdf0.10
	 */

	float width() {
		return font.getWidthPoint(" ", size);
	} 

	/**
	 * Returns the width of a certain character of this font.
	 *
	 * @param		character	a certain character
	 * @return		a width in Text Space
	 *
	 * @since		rugPdf0.10
	 */

	float width(char character) {
		return font.getWidthPoint(character, size);
	}

	/**
	 * Returns the width (user space) of a <CODE>PdfPrintable</CODE>-object.
	 *
	 * @param		text		a <CODE>PdfPrintable</CODE>-object
	 * @return		a width
	 *
	 * @since		rugPdf0.10
	 */

	float width(PdfPrintable text) {
		return font.getWidthPoint(text.toString(), size);
	}
					   
	/**
	 * Returns the offset from the start position of the <CODE>PdfPrintable</CODE>-object
	 * (y direction; user space).
	 *
	 * @param		object		a <CODE>PdfPrintable</CODE>-object
	 * @param		factor		the used scaling factor
	 * @param		alignment	the value of the alignment
	 * @param		leftX		the left x-coordinate of the interval
	 * @param		rightX		the right x-coordinate of the interval
	 *
	 * @deprecated
	 * @since		rugPdf0.10
	 */

	double offset(PdfPrintable object, double factor, int alignment, int leftX, int rightX) {

		// the default alignment is set
		if (alignment == PdfPrintable.DEFAULT) {
			// PdfString-objects are aligned to the LEFT
			if (object.isString()) {
				alignment = PdfPrintable.LEFT;
			}
			// all other PdfPrintable objects are aligned to the RIGHT
			else {
				alignment = PdfPrintable.RIGHT;
			}
		}

		// if the alignment is LEFT, the answer can be returned immediately
		if (alignment == PdfPrintable.LEFT) {
			return 0.0;
		}

		// some calculations are made
		double width = (double) (rightX - leftX);
		double scaledLength = factor * width(object);
		// the value of the offset is returned
		switch (alignment) {
		case PdfPrintable.CENTER:
			return (width - scaledLength) / 2;
		case PdfPrintable.RIGHT:
			return width - scaledLength;
		default:
			return 0.0;
		}
	}

	/**
	 * Returns a <CODE>PdfString</CODE>-object that fits into the given width.
	 *
	 * @param		text		a <CODE>PdfPrintable</CODE>-object
	 * @param		totalWidth	a given width
	 * @param		factor		the used scaling factor
	 * @return		a truncated <CODE>PdfString</CODE>
	 *
	 * @deprecated
	 * @since		rugPdf0.10
	 */

	PdfString truncate(PdfPrintable text, double totalWidth, double factor) {
		char ellipsis = '\u2016';
		int currentPosition = 0;
		double currentWidth = font.getWidthPoint(ellipsis, size) * factor;
        if (currentWidth == 0.0) {
            ellipsis = ' ';
            currentWidth = font.getWidthPoint(ellipsis, size) * factor;
        }

		// it's no use trying to truncate if there isn't even enough place for an ellipsis
		if (totalWidth < currentWidth) {
			return new PdfString(String.valueOf(ellipsis), font);
		}

		// loop over all the characters of a string
		// or until the totalWidth is reached
		char character;
		String string = text.toString();
		int length = string.length();
		while (currentPosition < length && currentWidth < totalWidth) {
				character = string.charAt(currentPosition);
				currentWidth += font.getWidthPoint(character, size) * factor;
				currentPosition++;
		}		

		// if all the characters fit in the total width, the whole PdfPrintable is returned
		if (currentPosition == length) {
			return new PdfString(text, font);
		}

		// otherwise, the string has to be truncated
		currentPosition -= 2;
		if (currentPosition < 0) {
			return new PdfString(String.valueOf(ellipsis), font);
		}
		return new PdfString(string.substring(0, currentPosition) + ellipsis, font);
	}

	/**
	 * Returns an iterator of <CODE>PdfString</CODE>-objects that all fit into the given width.
	 *
	 * @param		text		a <CODE>PdfPrintable</CODE>-object
	 * @param		totalWidth	a given width (user space) 
	 * @param		factor		the used scaling factor
	 * @return		an <CODE>Iterator</CODE>
	 *
	 * @deprecated
	 * @since		rugPdf0.10
	 */

	Iterator split(PdfPrintable text, double totalWidth, double factor) {
		ArrayList array = new ArrayList();
		// the total width has to be positive
		if (totalWidth <= 0.0) {
			return array.iterator();
		}

		// initialisation of some variables
		String string = text.toString();
		
		int previousPosition = 0;
		int splitPosition = 0;
		int currentPosition = 0;
		int length = string.length();

		char character;
		double currentWidth = font.getWidthPoint(" ", size) * factor;

		while (currentPosition < length) {
			character = string.charAt(currentPosition);
			currentPosition++;

			// if the end of the string is reached
			if (currentPosition >= length) {
				array.add(new PdfString(string.substring(previousPosition), font));
				break;
			}

			// if a newLine or carriageReturn is encountered
			if (character == '\r' || character == '\n') {
				array.add(new PdfString(string.substring(previousPosition, currentPosition - 1), font));
				currentWidth = font.getWidthPoint(" ", size) * factor;
				previousPosition = currentPosition;
				continue;
			}

			// if a split-character is encountered, the splitPosition is altered
			if (font.isSplitCharacter(character)) {
				splitPosition = currentPosition + 1;
			}

			// checks if the totalWidth is reached
			currentWidth += font.getWidthPoint(" ", size) * factor;
			if (currentWidth > totalWidth) {
				if (previousPosition >= splitPosition) {
					splitPosition = Math.max(previousPosition + 1, currentPosition - 2);
				}
				array.add(new PdfString(PdfFontMetrics.trim(string.substring(previousPosition, splitPosition)), font));
				currentPosition = splitPosition;
				currentWidth = font.getWidthPoint(" ", size) * factor;
				previousPosition = currentPosition;
				continue;
			}
		}

		return array.iterator();			   
	}
    
    BaseFont getFont()
    {
        return font;
    }
}
