/*
 * @(#)PdfRectangle.java			0.23 2000/02/02
 *       release rugPdf0.10:		0.04 99/03/30
 *               rugPdf0.20:		0.13 99/11/30
 *               iText0.3:			0.23 2000/02/14
 *               iText0.35:         0.23 2000/08/11
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

import com.lowagie.text.Rectangle;

/**
 * <CODE>PdfRectangle</CODE> is the PDF Rectangle object.
 * <P>
 * Rectangles are used to describe locations on the page and bounding boxes for several
 * objects in PDF, such as fonts. A rectangle is represented as an <CODE>array</CODE> of
 * four numbers, specifying the lower lef <I>x</I>, lower left <I>y</I>, upper right <I>x</I>,
 * and upper right <I>y</I> coordinates of the rectangle, in that order.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.1 (page 183).
 *
 * @see		com.lowagie.text.Rectangle
 * @see		PdfArray
 * @see		PdfMediaBox
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 * @since   rugPdf0.10
 */

class PdfRectangle extends PdfArray {

// membervariables

	/** lower left x */
	private float llx = 0;

	/** lower left y */
	private float lly = 0;

	/** upper right x */
	private float urx = 0;

	/** upper right y */
	private float ury = 0;

// constructors

	/**
	 * Constructs a <CODE>PdfRectangle</CODE>-object.
	 *
	 * @param		llx			lower left x
	 * @param		lly			lower left y
	 * @param		urx			upper right x
	 * @param		ury			upper right y
	 *
	 * @since		rugPdf0.10
	 */

	PdfRectangle(float llx, float lly, float urx, float ury) {
		super();
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
		super.add(new PdfNumber(llx));
		super.add(new PdfNumber(lly));
		super.add(new PdfNumber(urx));
		super.add(new PdfNumber(ury));
	}

	/**
	 * Constructs a <CODE>PdfRectangle</CODE>-object starting from the origin (0, 0).
	 *
	 * @param		urx			upper right x
	 * @param		ury			upper right y
	 *
	 * @since		rugPdf0.10
	 */

	PdfRectangle(float urx, float ury) {
		this(0, 0, urx, ury);
	}

	/**
	 * Constructs a <CODE>PdfRectangle</CODE>-object with a <CODE>Rectangle</CODE>-object.
	 *
	 * @param	rectangle	a <CODE>Rectangle</CODE>
	 *
	 * @since		iText0.30
	 */

	PdfRectangle(Rectangle rectangle) {
		this(rectangle.left(), rectangle.bottom(), rectangle.right(), rectangle.top());
	}

// methods

	/**
	 * Overrides the <CODE>add</CODE>-method in <CODE>PdfArray</CODE> in order to prevent the adding of extra object to the array.
	 *
	 * @param		object			<CODE>PdfObject</CODE> to add (will not be added here)
	 * @return		<CODE>false</CODE>
	 *
	 * @since		rugPdf0.10
	 */

	final boolean add(PdfObject object) {
		return false;
	}

	/**
	 * Returns the lower left x-coordinate.
	 *
	 * @return		the lower left x-coordinaat
	 *
	 * @since		rugPdf0.10
	 */

	final float left() {
		return llx;
	}

	/**
	 * Returns the upper right x-coordinate.
	 *
	 * @return		the upper right x-coordinate
	 *
	 * @since		rugPdf0.10
	 */

	final float right() {
		return urx;
	}

	/**
	 * Returns the upper right y-coordinate.
	 *
	 * @return		the upper right y-coordinate
	 *
	 * @since		rugPdf0.10
	 */

	final float top() {
		return ury;
	}

	/**
	 * Returns the lower left y-coordinate.
	 *
	 * @return		the lower left y-coordinate
	 *
	 * @since		rugPdf0.10
	 */

	final float bottom() {
		return lly;
	}

	/**
	 * Returns the lower left x-coordinate, considering a given margin.
	 *
	 * @param		margin		a margin
	 * @return		the lower left x-coordinate
	 *
	 * @since		rugPdf0.10
	 */

	final float left(int margin) {
		return llx + margin;
	}

	/**
	 * Returns the upper right x-coordinate, considering a given margin.
	 *
	 * @param		margin		a margin
	 * @return		the upper right x-coordinate
	 *
	 * @since		rugPdf0.10
	 */

	final float right(int margin) {
		return urx - margin;
	}

	/**
	 * Returns the upper right y-coordinate, considering a given margin.
	 *
	 * @param		margin		a margin
	 * @return		the upper right y-coordinate
	 *
	 * @since		rugPdf0.10
	 */

	final float top(int margin) {
		return ury - margin;
	}

	/**
	 * Returns the lower left y-coordinate, considering a given margin.
	 *
	 * @param		margin		a margin
	 * @return		the lower left y-coordinate
	 *
	 * @since		rugPdf0.10
	 */

	final float bottom(int margin) {
		return lly + margin;
	}

	/**
	 * Returns the width of the rectangle.
	 *
	 * @return		a width
	 *
	 * @since		rugPdf0.10
	 */

	final float width() {
		return urx - llx;
	}

	/**
	 * Returns the height of the rectangle.
	 *
	 * @return		a height
	 *
	 * @since		rugPdf0.10
	 */

	final float height() {
		return ury - lly;
	}

	/**
	 * Swaps the values of urx and ury and of lly and llx in order to rotate the rectangle.
	 *
	 * @return		a <CODE>PdfRectangle</CODE>
	 *
	 * @since		rugPdf0.10
	 */

	final PdfRectangle rotate() {
		return new PdfRectangle(lly, llx, ury, urx);
	}
}