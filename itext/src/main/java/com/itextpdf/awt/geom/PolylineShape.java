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

import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Class that defines a Polyline shape.
 * This class was originally written by wil - amristar.com.au
 * and integrated into iText by Bruno.
 */
public class PolylineShape implements Shape {
	/** All the X-values of the coordinates in the polyline. */
	protected int[] x;
	/** All the Y-values of the coordinates in the polyline. */
	protected int[] y;
	/** The total number of points. */
	protected int np;

	/** Creates a PolylineShape. */
	public PolylineShape(int[] x, int[] y, int nPoints) {
		// Should copy array (as done in Polygon)
		this.np = nPoints;
		// Take a copy.
		this.x = new int[np];
		this.y = new int[np];
		System.arraycopy(x, 0, this.x, 0, np);
		System.arraycopy(y, 0, this.y, 0, np);
	}

	/**
	 * Returns the bounding box of this polyline.
	 *
	 * @return a {@link Rectangle2D} that is the high-precision
	 * 	bounding box of this line.
	 * @see java.awt.Shape#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {
		int[] r = rect();
		return r==null?null:new Rectangle2D.Double(r[0], r[1], r[2], r[3]);
	}
	
	/**
	 * Returns the bounding box of this polyline.
	 * @see java.awt.Shape#getBounds()
	 */
	public Rectangle getBounds() {
		return getBounds2D().getBounds();
	}

	/**
	 * Calculates the origin (X, Y) and the width and height
	 * of a rectangle that contains all the segments of the
	 * polyline.
	 */
	private int[] rect() {
		 if(np==0)return null;
		int xMin = x[0], yMin=y[0], xMax=x[0],yMax=y[0];

		 for(int i=1;i<np;i++) {
			 if(x[i]<xMin)xMin=x[i];
			 else if(x[i]>xMax)xMax=x[i];
			 if(y[i]<yMin)yMin=y[i];
			 else if(y[i]>yMax)yMax=y[i];
		 }

		 return new int[] { xMin, yMin, xMax-xMin, yMax-yMin };
	}

	/**
	 * A polyline can't contain a point.
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) { return false; }
	
	/**
	 * A polyline can't contain a point.
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(Point2D p) { return false; }
	
	/**
	 * A polyline can't contain a point.
	 * @see java.awt.Shape#contains(double, double, double, double)
	 */
	public boolean contains(double x, double y, double w, double h) { return false; }
	
	/**
	 * A polyline can't contain a point.
	 * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
	 */
	public boolean contains(Rectangle2D r) { return false; }

	/**
	 * Checks if one of the lines in the polyline intersects
	 * with a given rectangle.
	 * @see java.awt.Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {
		return intersects(new Rectangle2D.Double(x, y, w, h));
	}

	/**
	 * Checks if one of the lines in the polyline intersects
	 * with a given rectangle.
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		if(np==0)return false;
		Line2D line = new Line2D.Double(x[0],y[0],x[0],y[0]);
		for (int i = 1; i < np; i++) {
			line.setLine(x[i-1], y[i-1], x[i], y[i]);
			if(line.intersects(r))return true;
		}
		return false;
	}

	/**
	 * Returns an iteration object that defines the boundary of the polyline.
	 * @param at the specified {@link AffineTransform}
	 * @return a {@link PathIterator} that defines the boundary of this polyline.
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		return new PolylineShapeIterator(this, at);
	}

	/**
	 * There's no difference with getPathIterator(AffineTransform at);
	 * we just need this method to implement the Shape interface.
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return new PolylineShapeIterator(this, at);
	}

}

