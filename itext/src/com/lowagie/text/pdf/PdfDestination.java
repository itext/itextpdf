/*
 * @(#)PdfDestination.java			0.39 2000/11/22
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

/**
 * A <CODE>PdfColor</CODE> defines a Color (it's a <CODE>PdfArray</CODE> containing 3 values).
 *
 * @see		PdfDictionary
 *
 * @author  bruno@lowagie.com
 * @version 0.39 2000/11/22
 * @since   rugPdf0.38
 */

class PdfDestination extends PdfArray {

// public static final member-variables

	/** This is a possible destination type */
	public static final int XYZ = 0;

	/** This is a possible destination type */
	public static final int FIT = 1;

	/** This is a possible destination type */
	public static final int FITH = 2;

	/** This is a possible destination type */
	public static final int FITV = 3;

	/** This is a possible destination type */
	public static final int FITR = 4;

	/** This is a possible destination type */
	public static final int FITB = 5;

	/** This is a possible destination type */
	public static final int FITBH = 6;

	/** This is a possible destination type */
	public static final int FITBV = 7;

// member variables

	/** Is the indirect reference to a page already added? */
	private boolean status = false;

// constructors

	/**
	 * Constructs a new <CODE>PdfDestination</CODE>.
	 * <P>
	 * If <VAR>type</VAR> equals <VAR>FITB</VAR>, the bounding box of a page
	 * will fit the window of the Reader. Otherwise the type will be set to
	 * <VAR>FIT</VAR> so that the entire page will fit to the window.
	 *
	 * @param		type		The destination type
	 * @since		iText0.38
	 */

	PdfDestination(int type) { 
		super();
		if (type == FITB) {
			add(PdfName.FITB);
		}
		else {
			add(PdfName.FIT);
		}
	}

	/**
	 * Constructs a new <CODE>PdfDestination</CODE>.
	 * <P>
	 * If <VAR>type</VAR> equals <VAR>FITBH</VAR> / <VAR>FITBV</VAR>,
	 * the width / height of the bounding box of a page will fit the window
	 * of the Reader. The parameter will specify the y / x coordinate of the
	 * top / left edge of the window. If the <VAR>type</VAR> equals <VAR>FITH</VAR>
	 * or <VAR>FITV</VAR> the width / height of the entire page will fit
	 * the window and the parameter will specify the y / x coordinate of the
	 * top / left edge. In all other cases the type will be set to <VAR>FITH</VAR>.
	 *
	 * @param		type		the destination type
	 * @param		parameter	a parameter to combined with the destination type
	 * @since		iText0.38
	 */

	PdfDestination(int type, int parameter) { 
		super(new PdfNumber(parameter));
		switch(type) {
		default:
			addFirst(PdfName.FITH);
			break;
		case FITV:
			addFirst(PdfName.FITV);
			break;
		case FITBH:
			addFirst(PdfName.FITBH);
			break;
		case FITBV:
			addFirst(PdfName.FITBV);
		}
	}

	/**
	 * Constructs a new <CODE>PdfDestination</CODE>.
	 *
	 * @since		iText0.38
	 */

	PdfDestination(int type, int left, int top, int zoom) { 
		super(PdfName.XYZ);
		add(new PdfNumber(left));
		add(new PdfNumber(top));
		add(new PdfNumber(zoom));
	}

	/**
	 * Constructs a new <CODE>PdfDestination</CODE>.
	 *
	 * @since		iText0.38
	 */

	PdfDestination(int type, int left, int bottom, int right, int top) { 
		super(PdfName.FITR);
		add(new PdfNumber(left));
		add(new PdfNumber(bottom));
		add(new PdfNumber(right));
		add(new PdfNumber(top));
	}

// methods

	/**
	 * Checks if an indirect reference to a page has been added.
	 *
	 * @return	<CODE>true</CODE> or <CODE>false</CODE>
	 *
	 * @since	iText0.39
	 */

	public boolean hasPage() {
		return status;
	}

	/**
	 * Adds the indirect reference of the destination page.
	 *
	 * @param	page	an indirect reference
	 *
	 * @since		iText0.38
	 */

	public boolean addPage(PdfIndirectReference page) {
		if (!status) {
			addFirst(page);
			status = true;
			return true;
		}
		return false;
	}
}