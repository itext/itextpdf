/*
 * $Id$
 * $Name$
 * 
 * Copyright 2000 by Bruno Lowagie.
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

import java.net.MalformedURLException;

/**
 * A <CODE>Watermark</CODE> is a graphic element (GIF or JPEG)
 * that is shown on a certain position on each page.
 *
 * @see		Element
 * @see		Jpeg  
 * @see		Gif  
 * @see		Png
 * 
 * @author  bruno@lowagie.com
 */

public class Watermark extends Image implements Element {

// membervariables

	/** This is the offset in x-direction of the Watermark. */
	private int offsetX = 0;

	/** This is the offset in y-direction of the Watermark. */
	private int offsetY = 0;

// Constructors

	/**
	 * Constructs a <CODE>Watermark</CODE>-object, using an <CODE>Image</CODE>.
	 *
	 * @param		image		an <CODE>Image</CODE>-object
	 * @param		offsetX		the offset in x-direction
	 * @param		offsetY		the offset in y-direction
	 */

	public Watermark(Image image, int offsetX, int offsetY) throws MalformedURLException {
		super(image);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

// implementation of the Element interface

    /**
     * Gets the type of the text element. 
     *
     * @return	a type
     */

    public int type() {
		return type;
	}

// methods to retrieve information

	/**
	 * Returns the offset in x direction.
	 *
	 * @return		an offset
	 */

	public int offsetX() {
		return offsetX;
	}

	/**
	 * Returns the offset in y direction.
	 *
	 * @return		an offset 
	 */

	public int offsetY() {
		return offsetY;
	}

	/**
	 * Returns a representation of this <CODE>Rectangle</CODE>.
	 *
	 * @return		a <CODE>String</CODE>
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("<WATERMARK>");
		buf.append(super.toString());
		buf.append("</WATERMARK>");
		return buf.toString();
	}
}
