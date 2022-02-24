/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.Rectangle;

/**
 * <CODE>PdfRectangle</CODE> is the PDF Rectangle object.
 * <P>
 * Rectangles are used to describe locations on the page and bounding boxes for several
 * objects in PDF, such as fonts. A rectangle is represented as an <CODE>array</CODE> of
 * four numbers, specifying the lower left <I>x</I>, lower left <I>y</I>, upper right <I>x</I>,
 * and upper right <I>y</I> coordinates of the rectangle, in that order.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.1 (page 183).
 *
 * @see		com.itextpdf.text.Rectangle
 * @see		PdfArray
 */

public class PdfRectangle extends NumberArray {

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

    public PdfRectangle(float llx, float lly, float urx, float ury, int rotation) {
        super();
        if (rotation == 90 || rotation == 270) {
            this.llx = lly;
            this.lly = llx;
            this.urx = ury;
            this.ury = urx;
        }
        else {
            this.llx = llx;
            this.lly = lly;
            this.urx = urx;
            this.ury = ury;
        }
        super.add(new PdfNumber(this.llx));
        super.add(new PdfNumber(this.lly));
        super.add(new PdfNumber(this.urx));
        super.add(new PdfNumber(this.ury));
    }

    public PdfRectangle(float llx, float lly, float urx, float ury) {
        this(llx, lly, urx, ury, 0);
    }

/**
 * Constructs a <CODE>PdfRectangle</CODE>-object starting from the origin (0, 0).
 *
 * @param		urx			upper right x
 * @param		ury			upper right y
 */

    public PdfRectangle(float urx, float ury, int rotation) {
        this(0, 0, urx, ury, rotation);
    }

    public PdfRectangle(float urx, float ury) {
        this(0, 0, urx, ury, 0);
    }

/**
 * Constructs a <CODE>PdfRectangle</CODE>-object with a <CODE>Rectangle</CODE>-object.
 *
 * @param	rectangle	a <CODE>Rectangle</CODE>
 */

    public PdfRectangle(Rectangle rectangle, int rotation) {
        this(rectangle.getLeft(), rectangle.getBottom(), rectangle.getRight(), rectangle.getTop(), rotation);
    }

    public PdfRectangle(Rectangle rectangle) {
        this(rectangle.getLeft(), rectangle.getBottom(), rectangle.getRight(), rectangle.getTop(), 0);
    }

    // methods
    /**
     * Returns the high level version of this PdfRectangle
     * @return this PdfRectangle translated to class Rectangle
     */
    public Rectangle getRectangle() {
    	return new Rectangle(left(), bottom(), right(), top());
    }

/**
 * Overrides the <CODE>add</CODE>-method in <CODE>PdfArray</CODE> in order to prevent the adding of extra object to the array.
 *
 * @param		object			<CODE>PdfObject</CODE> to add (will not be added here)
 * @return		<CODE>false</CODE>
 */

    public boolean add(PdfObject object) {
        return false;
    }

    /**
     * Block changes to the underlying PdfArray
     * @param values stuff we'll ignore.  Ha!
     * @return false.  You can't add anything to a PdfRectangle
     * @since 2.1.5
     */

    public boolean add( float values[] ) {
        return false;
    }

    /**
     * Block changes to the underlying PdfArray
     * @param values stuff we'll ignore.  Ha!
     * @return false.  You can't add anything to a PdfRectangle
     * @since 2.1.5
     */

    public boolean add( int values[] ) {
        return false;
    }

    /**
     * Block changes to the underlying PdfArray
     * @param object Ignored.
     * @since 2.1.5
     */

    public void addFirst( PdfObject object ) {
    }
/**
 * Returns the lower left x-coordinate.
 *
 * @return		the lower left x-coordinate
 */

    public float left() {
        return llx;
    }

/**
 * Returns the upper right x-coordinate.
 *
 * @return		the upper right x-coordinate
 */

    public float right() {
        return urx;
    }

/**
 * Returns the upper right y-coordinate.
 *
 * @return		the upper right y-coordinate
 */

    public float top() {
        return ury;
    }

/**
 * Returns the lower left y-coordinate.
 *
 * @return		the lower left y-coordinate
 */

    public float bottom() {
        return lly;
    }

/**
 * Returns the lower left x-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the lower left x-coordinate
 */

    public float left(int margin) {
        return llx + margin;
    }

/**
 * Returns the upper right x-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the upper right x-coordinate
 */

    public float right(int margin) {
        return urx - margin;
    }

/**
 * Returns the upper right y-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the upper right y-coordinate
 */

    public float top(int margin) {
        return ury - margin;
    }

/**
 * Returns the lower left y-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the lower left y-coordinate
 */

    public float bottom(int margin) {
        return lly + margin;
    }

/**
 * Returns the width of the rectangle.
 *
 * @return		a width
 */

    public float width() {
        return urx - llx;
    }

/**
 * Returns the height of the rectangle.
 *
 * @return		a height
 */

    public float height() {
        return ury - lly;
    }

/**
 * Swaps the values of urx and ury and of lly and llx in order to rotate the rectangle.
 *
 * @return		a <CODE>PdfRectangle</CODE>
 */

    public PdfRectangle rotate() {
        return new PdfRectangle(lly, llx, ury, urx, 0);
    }

    public PdfRectangle transform(AffineTransform transform) {
        float[] pts = {llx, lly, urx, ury};
        transform.transform(pts, 0, pts, 0, 2);
        float[] dstPts = {pts[0], pts[1], pts[2], pts[3]};
        if (pts[0] > pts[2]) {
            dstPts[0] = pts[2];
            dstPts[2] = pts[0];
        }
        if (pts[1] > pts[3]) {
            dstPts[1] = pts[3];
            dstPts[3] = pts[1];
        }
        return new PdfRectangle(dstPts[0], dstPts[1], dstPts[2], dstPts[3]);
    }
}
