/*
 * $Id$
 * $Name$
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie, Paulo Soares
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
 * This class was entirely written by Paulo Soares.
 */

package com.lowagie.text;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Raw Image data that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 * 
 * @author  Paulo Soares
 */

public class ImgRaw extends Image implements Element {

	/**
	 * Constructs a <CODE>Jpeg</CODE>-object, using an <VAR>url</VAR>.
	 *
	 * @param		width		the exact width of the image
	 * @param		height		the exact height of the image
	 * @param		components	1,3 or 4 for GrayScale, RGB and CMYK 
	 * @param		bps			bits per component. Must be 1,2,4 or 8
	 * @param		data		the image data	
	 */

	public ImgRaw(int width, int height, int components, int bpc, byte[] data) throws BadElementException{
		super((URL)null);
		type = IMGRAW;
		scaledHeight = height;
		setTop((int) scaledHeight);
		scaledWidth = width;
		setRight((int) scaledWidth);
		if (components != 1 && components != 3 && components != 4)
			throw new BadElementException("Components must be 1, 3, or 4.");
		if (bpc != 1 && bpc != 2 && bpc != 4 && bpc != 8) 
			throw new BadElementException("Bits-per-component must be 1, 2, 4, or 8.");
		colorspace = components;
		this.bpc = bpc;
		rawData = data;
		plainWidth = width();
		plainHeight = height();
	}

// methods to retrieve information

	/**
	 * Returns a representation of this <CODE>Rectangle</CODE>.
	 *
	 * @return		a <CODE>String</CODE>
	 *
	 * @since		iText0.31
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("<IMGRAW>");
		buf.append(super.toString());
		buf.append("</IMGRAW>");
		return buf.toString();
	}
}
