/*
 * @(#)PdfChunk.java				0.39 2000/11/23
 *       release iText0.3:			0.25 2000/02/14
 *               iText0.35:         0.31 2000/08/11
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

package com.lowagie.text.pdf;

import java.awt.Color;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;

/**
 * A <CODE>PdfChunk</CODE> is the PDF translation of a <CODE>Chunk</CODE>.
 * <P>
 * A <CODE>PdfChunk</CODE> is a <CODE>PdfString</CODE> in a certain
 * <CODE>PdfFont</CODE> and <CODE>Color</CODE>.
 *
 * @see		PdfString
 * @see		PdfFont
 * @see		com.lowagie.text.Chunk
 * @see		com.lowagie.text.Font
 *
 * @author  bruno@lowagie.com
 * @version 0.39 2000/11/23
 * @since   iText0.30
 */

class PdfChunk extends PdfString {

// membervariables

	/** This is the font of the chunk. */
	protected PdfFont font;

	/** This is the color of the chunk. */
	protected Color color;

	/** Is the chunk underlined? */
	protected boolean underlined = false;

	/** Is the STRIKETHRU flag on? */
	protected boolean strikethru = false;

// constructors

	/**
	 * Constructs a <CODE>PdfChunk</CODE>-object.
	 *
	 * @param		string		the content of the <CODE>PdfChunk</CODE>-object
	 * @param		font		the <CODE>PdfFont</CODE>
	 * @param		color		the <CODE>Color</CODE>
	 *
	 * @since		iText0.30
	 */

	private PdfChunk(String string, PdfFont font, Color color) {
		super(string);
		this.font = font;
		this.color = color;
	}

	/**
	 * Constructs a <CODE>PdfFont</CODE>-object.
	 *
	 * @param		chunk		the original <CODE>Chunk</CODE>-object
	 *
	 * @since		iText0.30
	 */

	PdfChunk(Chunk chunk) {
		super(chunk.content());
		Font f = chunk.font();
		// translation of the font-family to a PDF font-family
		int family;
		int style = f.style();
		if (style == Font.UNDEFINED) {
			style = Font.NORMAL;
		}
		switch(f.family()) {
		case Font.COURIER:
			switch(style & Font.BOLDITALIC) {
			case Font.BOLD:
				family = PdfFontMetrics.COURIER_BOLD;
				break;
			case Font.ITALIC:
				family = PdfFontMetrics.COURIER_OBLIQUE;
				break;
			case Font.BOLDITALIC:
				family = PdfFontMetrics.COURIER_BOLDOBLIQUE;
				break;
			default:  
			case Font.NORMAL:
				family = PdfFontMetrics.COURIER;
				break;
			}
			break;
		case Font.TIMES_NEW_ROMAN:
			switch(style & Font.BOLDITALIC) {
			case Font.BOLD:
				family = PdfFontMetrics.TIMES_BOLD;
				break;
			case Font.ITALIC:
				family = PdfFontMetrics.TIMES_ITALIC;
				break;
			case Font.BOLDITALIC:
				family = PdfFontMetrics.TIMES_BOLDITALIC;
				break;
			default:  
			case Font.NORMAL:
				family = PdfFontMetrics.TIMES_ROMAN;
				break;
			}
			break;
		case Font.SYMBOL:
			family = PdfFontMetrics.SYMBOL;
			break;
		case Font.ZAPFDINGBATS:
			family = PdfFontMetrics.ZAPFDINGBATS;
			break;
		default:
		case Font.HELVETICA:
			switch(style & Font.BOLDITALIC) {
			case Font.BOLD:
				family = PdfFontMetrics.HELVETICA_BOLD;
				break;
			case Font.ITALIC:
				family = PdfFontMetrics.HELVETICA_OBLIQUE;
				break;
			case Font.BOLDITALIC:
				family = PdfFontMetrics.HELVETICA_BOLDOBLIQUE;
				break;
			default:  
			case Font.NORMAL:
				family = PdfFontMetrics.HELVETICA;
				break;
			}
			break;
		}
		// creation of the PdfFont with the right size
		int size = f.size();
		if (size == Font.UNDEFINED) {
			font = new PdfFont(family, 12);
		}
		else {
			font = new PdfFont(family, size);
		}
		// other style possibilities
		underlined = f.isUnderlined();
		strikethru = f.isStrikethru();
		// the color can't be stored in a PdfFont
		color = f.color();
	}

// methods

	/**
	 * Splits this <CODE>PdfChunk</CODE> if it's too long for the given width.
	 * <P>
	 * Returns <VAR>null</VAR> if the <CODE>PdfChunk</CODE> wasn't truncated.
	 *
	 * @param		width		a given width
	 * @return		the <CODE>PdfChunk</CODE> that doesn't fit into the width.
	 *
	 * @since		iText0.30
	 */

	PdfChunk split(double width) {

		int currentPosition = 0;
		int splitPosition = -1;
		double currentWidth = font.width();

		// loop over all the characters of a string
		// or until the totalWidth is reached
		int length = value.length();
		char character;
		while (currentPosition < length && currentWidth < width) {
			// the width of every character is added to the currentWidth
			character = value.charAt(currentPosition);
			currentWidth += font.width(character);
			// if a newLine or carriageReturn is encountered
			if (character == '\r' || character == '\n') {
				String returnValue = value.substring(currentPosition + 1);
				value = value.substring(0, currentPosition);
				if (value.length() < 1) {
					value = " ";
				}
				setContent(value);
				return new PdfChunk(returnValue, font, color);
			} 
			// if a split-character is encountered, the splitPosition is altered
			if (PdfFontMetrics.isSplitCharacter(character)) {
				splitPosition = currentPosition + 1;
			}
			currentPosition++;
		}		

		// if all the characters fit in the total width, null is returned (there is no overflow)
		if (currentPosition == length) {
			return null;
		}

		// otherwise, the string has to be truncated
		if (splitPosition < 0) {
			String returnValue = value;
			value = "";
			setContent(value);
			return new PdfChunk(returnValue, font, color);
		}
		String returnValue = value.substring(splitPosition);
		value = PdfFontMetrics.trim(value.substring(0, splitPosition));
		setContent(value);
		return new PdfChunk(returnValue, font, color);			   
	}

	/**
	 * Truncates this <CODE>PdfChunk</CODE> if it's too long for the given width.
	 * <P>
	 * Returns <VAR>null</VAR> if the <CODE>PdfChunk</CODE> wasn't truncated.
	 *
	 * @param		width		a given width
	 * @return		the <CODE>PdfChunk</CODE> that doesn't fit into the width.
	 *
	 * @since		iText0.30
	 */

	PdfChunk truncate(double width) {

		int currentPosition = 0;
		double currentWidth = font.width();

		// it's no use trying to split if there isn't even enough place for a space
		if (width < currentWidth) {
			String returnValue = value.substring(1);
			value = value.substring(0, 1);
			setContent(value);
			return new PdfChunk(returnValue, font, color);
		}

		// loop over all the characters of a string
		// or until the totalWidth is reached
		int length = value.length();
		char character;
		while (currentPosition < length && currentWidth < width) {
			// the width of every character is added to the currentWidth
			character = value.charAt(currentPosition);
			currentWidth += font.width(character);
			currentPosition++;
		}		

		// if all the characters fit in the total width, null is returned (there is no overflow)
		if (currentPosition == length) {
			return null;
		}

		// otherwise, the string has to be truncated
		currentPosition -= 2;
		// we have to chop off minimum 1 character from the chunk
		if (currentPosition < 0) {
			currentPosition = 1;
		}
		String returnValue = value.substring(currentPosition);
		value = value.substring(0, currentPosition);
		setContent(value);
		return new PdfChunk(returnValue, font, color);			   
	}

// methods to retrieve the membervariables

	/**
	 * Returns the font of this <CODE>Chunk</CODE>.
	 *
	 * @return	a <CODE>PdfFont</CODE>
	 *
	 * @since	iText0.30
	 */

	PdfFont font() {
		return font;
	} 

	/**
	 * Returns the color of this <CODE>Chunk</CODE>.
	 *
	 * @return	a <CODE>Color</CODE>
	 *
	 * @since	iText0.30
	 */

	Color color() {
		return color;
	}

	/**
	 * Returns the width of this <CODE>PdfChunk</CODE>.
	 *
	 * @return	a width
	 *
	 * @since	iText0.30
	 */

	double width() {
		double width = 0.0;
		int length = value.length();
		for (int i = 0; i < length; i++) {
			width += font.width(value.charAt(i));
		}
		return width;
	}

	/**
	 * checks if this chunk is underlined.
	 *
	 * @return	a <CODE>boolean</CODE>
	 *
	 * @since	iText0.38
	 */

	public boolean isUnderlined() {
		return underlined;
	}										   

	/**
	 * checks if the style of this chunk is STRIKETHRU.
	 *
	 * @return	a <CODE>boolean</CODE>
	 *
	 * @since	iText0.38
	 */

	public boolean isStrikethru() {
		return strikethru;
	}
}
