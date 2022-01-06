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
package com.itextpdf.awt.geom;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;
import com.itextpdf.text.error_messages.MessageLocalization;
/**
 * PathIterator for PolylineShape.
 * This class was originally written by wil - amristar.com.au
 * and integrated into iText by Bruno.
 */
public class PolylineShapeIterator implements PathIterator {
	/** The polyline over which we are going to iterate. */
	protected PolylineShape poly;
	/** an affine transformation to be performed on the polyline. */
	protected AffineTransform affine;
	/** the index of the current segment in the iterator. */
	protected int index;
	
	/** Creates a PolylineShapeIterator. */
	PolylineShapeIterator(PolylineShape l, AffineTransform at) {
		this.poly = l;
		this.affine = at;
	}
	
	/**
	 * Returns the coordinates and type of the current path segment in
	 * the iteration. The return value is the path segment type:
	 * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
	 * A double array of length 6 must be passed in and may be used to
	 * store the coordinates of the point(s).
	 * Each point is stored as a pair of double x,y coordinates.
	 * SEG_MOVETO and SEG_LINETO types will return one point,
	 * SEG_QUADTO will return two points,
	 * SEG_CUBICTO will return 3 points
	 * and SEG_CLOSE will not return any points.
	 * @see #SEG_MOVETO
	 * @see #SEG_LINETO
	 * @see #SEG_QUADTO
	 * @see #SEG_CUBICTO
	 * @see #SEG_CLOSE
	 * @see java.awt.geom.PathIterator#currentSegment(double[])
	 */
	public int currentSegment(double[] coords) {
		if (isDone()) {
			throw new NoSuchElementException(MessageLocalization.getComposedMessage("line.iterator.out.of.bounds"));
		}
		int type = (index==0)?SEG_MOVETO:SEG_LINETO;
		coords[0] = poly.x[index];
		coords[1] = poly.y[index];
		if (affine != null) {
			affine.transform(coords, 0, coords, 0, 1);
		}
		return type;
	}
	
	/**
	 * Returns the coordinates and type of the current path segment in
	 * the iteration. The return value is the path segment type:
	 * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
	 * A double array of length 6 must be passed in and may be used to
	 * store the coordinates of the point(s).
	 * Each point is stored as a pair of double x,y coordinates.
	 * SEG_MOVETO and SEG_LINETO types will return one point,
	 * SEG_QUADTO will return two points,
	 * SEG_CUBICTO will return 3 points
	 * and SEG_CLOSE will not return any points.
	 * @see #SEG_MOVETO
	 * @see #SEG_LINETO
	 * @see #SEG_QUADTO
	 * @see #SEG_CUBICTO
	 * @see #SEG_CLOSE
	 * @see java.awt.geom.PathIterator#currentSegment(float[])
	 */
	public int currentSegment(float[] coords) {
		if (isDone()) {
			throw new NoSuchElementException(MessageLocalization.getComposedMessage("line.iterator.out.of.bounds"));
		}
		int type = (index==0)?SEG_MOVETO:SEG_LINETO;
		coords[0] = poly.x[index];
		coords[1] = poly.y[index];
		if (affine != null) {
			affine.transform(coords, 0, coords, 0, 1);
		}
		return type;
	}

	/**
	 * Return the winding rule for determining the insideness of the
	 * path.
	 * @see #WIND_EVEN_ODD
	 * @see #WIND_NON_ZERO
	 * @see java.awt.geom.PathIterator#getWindingRule()
	 */
	public int getWindingRule() {
		return WIND_NON_ZERO;
	}

	/**
	 * Tests if there are more points to read.
	 * @return true if there are more points to read
	 * @see java.awt.geom.PathIterator#isDone()
	 */
	public boolean isDone() {
		return (index >= poly.np);
	}

	/**
	 * Moves the iterator to the next segment of the path forwards
	 * along the primary direction of traversal as long as there are
	 * more points in that direction.
	 * @see java.awt.geom.PathIterator#next()
	 */
	public void next() {
		index++;
	}

}
