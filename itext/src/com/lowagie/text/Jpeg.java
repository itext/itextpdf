/*
 * @(#)Jpeg.java				0.37 2000/10/05
 *       release iText0.35:		0.33 2000/08/11
 *       release iText0.37:		0.37 2000/10/05
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
 * Very special thanks to Paulo Soares who wrote the code to retrieve the width
 * and the height of a Jpeg and added checking methods to see if a Jpeg is valid.
 */

package com.lowagie.text;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An <CODE>Jpeg</CODE> is the representation of a graphic element (JPEG)
 * that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 * @see		Gif
 * 
 * @author  bruno@lowagie.com
 * @version 0.37 2000/10/05
 * @since   iText0.31
 */

public class Jpeg extends Image implements Element {

// public final static membervariables

	/** This is a type of marker. */
	public static final int NOT_A_MARKER = -1;

	/** This is a type of marker. */
	public static final int VALID_MARKER = 0;

	/** Acceptable Jpeg markers. */
	public static final int[] VALID_MARKERS = {0xC0, 0xC1, 0xC2};

	/** This is a type of marker. */
	public static final int UNSUPPORTED_MARKER = 1;

	/** Unsupported Jpeg markers. */
	public static final int[] UNSUPPORTED_MARKERS = {0xC3, 0xC5, 0xC6, 0xC7, 0xC8, 0xC9, 0xCA, 0xCB, 0xCD, 0xCE, 0xCF};

	/** This is a type of marker. */
	public static final int NOPARAM_MARKER = 2;

	/** Jpeg markers without additional parameters. */
	public static final int[] NOPARAM_MARKERS = {0xD0, 0xD1, 0xD2, 0xD3, 0xD4, 0xD5, 0xD6, 0xD7, 0xD8, 0x01};

// Constructors

	/**
	 * Constructs a <CODE>Jpeg</CODE>-object, using a <VAR>filename</VAR>.
	 *
	 * @param		filename	a <CODE>String</CODE>-representation of the file that contains the Image.
	 *
	 * @since		iText0.31
	 */

	public Jpeg(String filename, int width, int height) throws BadElementException, MalformedURLException, IOException {
		this(Image.toURL(filename), width, height);
	}

	/**
	 * Constructs a <CODE>Jpeg</CODE>-object, using an <VAR>url</VAR>.
	 *
	 * @param		url			the <CODE>URL</CODE> where the image can be found.
	 *
	 * @since		iText0.31
	 */

	public Jpeg(URL url, int width, int height) throws BadElementException, IOException {
		this(url);
		scaledWidth = width;
		scaledHeight = height;
	}

	/**
	 * Constructs a <CODE>Jpeg</CODE>-object, using a <VAR>filename</VAR>.
	 *
	 * @param		filename	a <CODE>String</CODE>-representation of the file that contains the Image.
	 *
	 * @since		iText0.36
	 */

	public Jpeg(String filename) throws BadElementException, MalformedURLException, IOException {
		this(Image.toURL(filename));
	}

	/**
	 * Constructs a <CODE>Jpeg</CODE>-object, using an <VAR>url</VAR>.
	 *
	 * @param		url			the <CODE>URL</CODE> where the image can be found.
	 *
	 * @since		iText0.36
	 */

	public Jpeg(URL url) throws BadElementException, IOException {
		 super(url);
		 type = JPEG;
		 InputStream is = null;
		 try {
			is = url.openStream();
			if (is.read() != 0xFF || is.read() != 0xD8)	{
				throw new BadElementException(url.toString() + " is not a valid JPEG-file.");
			}
			while (true) {
				if (is.read() == 0xFF) {
					int marker = is.read();
					int markertype = marker(marker);
					if (markertype == VALID_MARKER) {
						skip(is, 2);					   
						if (is.read() != 0x08) {
							throw new BadElementException(url.toString() + " must have 8 bits per component.");
						}
						scaledHeight = getShort(is);
						setTop((int) scaledHeight);
						scaledWidth = getShort(is);
						setRight((int) scaledWidth);
						colorspace = is.read();
						break;
					}
					else if (markertype == UNSUPPORTED_MARKER) {
						throw new BadElementException(url.toString() + ": unsupported JPEG marker: " + marker);
					}
					else if (markertype != NOPARAM_MARKER) {
						skip(is, getShort(is) - 2);
					}
				}
			}
		 }
		 finally {
			if (is != null) {
				is.close();
			}
			plainWidth = width();
			plainHeight = height();
		 }
	}

// private static methods

	/**
	 * Reads a short from the <CODE>InputStream</CODE>.
	 *
	 * @param	is		the <CODE>InputStream</CODE>
	 * @return	an int
	 */

	private static final int getShort(InputStream is) throws IOException {
		return (is.read() << 8) + is.read(); 
	}

	/**
	 * Returns a type of marker.
	 *
	 * @param	an int
	 * @return	a type: <VAR>VALID_MARKER</CODE>, <VAR>UNSUPPORTED_MARKER</VAR> or <VAR>NOPARAM_MARKER</VAR>
	 */

	private static final int marker(int marker) {
		for (int i = 0; i < VALID_MARKERS.length; i++) {
			if (marker == VALID_MARKERS[i]) {
				return VALID_MARKER;
			}
		}
		for (int i = 0; i < NOPARAM_MARKERS.length; i++) {
			if (marker == NOPARAM_MARKERS[i]) {
				return NOPARAM_MARKER;
			}
		}
		for (int i = 0; i < UNSUPPORTED_MARKERS.length; i++) {
			if (marker == UNSUPPORTED_MARKERS[i]) {
				return UNSUPPORTED_MARKER;
			}
		}
		return NOT_A_MARKER;
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
		StringBuffer buf = new StringBuffer("<JPEG>");
		buf.append(super.toString());
		buf.append("</JPEG>");
		return buf.toString();
	}
}