/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, Balder Van Camp, et al.
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
package com.itextpdf.text.pdf.spatial;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.spatial.objects.NumberFormatArray;
import com.itextpdf.text.pdf.spatial.objects.XYArray;

/**
 * Rectilinear Measure dictionary.
 * @since 5.1.0
 */
public class MeasureRectilinear extends Measure {
	
	/**
	 * Gets the subtype.
	 * In this case RL for a rectilinear coordinate system.
	 */
	PdfName getSubType() {
		return PdfName.RL;
	}
	
	/**
	 * A text string expressing the scale ratio of the drawing in the region
	 * corresponding to this dictionary. Universally recognized unit
	 * abbreviations should be used, either matching those of the number format
	 * arrays in this dictionary or those of commonly used scale ratios.<br />
	 * If the scale ratio differs in the x and y directions, both scales should
	 * be specified.
	 * 
	 * @param scaleratio
	 */
	public void setScaleRatio(PdfString scaleratio) {
		put(new PdfName("R"), scaleratio);
	}

	/**
	 * A number format array for measurement of change along the x axis and, if
	 * Y is not present, along the y axis as well. The first element in the
	 * array shall contain the scale factor for converting from default user
	 * space units to the largest units in the measuring coordinate system along
	 * that axis.<br />
	 * The directions of the x and y axes are in the measuring coordinate system
	 * and are independent of the page rotation. These directions shall be
	 * determined by the BBox of the containing {@link Viewport}
	 * 
	 * @param x
	 */
	public void setX(NumberFormatArray x) {
		put(new PdfName("X"), x);
	}

	/**
	 * A number format array for measurement of change along the y axis. The
	 * first element in the array shall contain the scale factor for converting
	 * from default user space units to the largest units in the measuring
	 * coordinate system along the y axis.(Required when the x and y scales have
	 * different units or conversion factors)
	 * 
	 * @param y
	 */
	public void setY(NumberFormatArray y) {
		put(new PdfName("Y"), y);
	}

	/**
	 * A number format array for measurement of distance in any direction. The
	 * first element in the array shall specify the conversion to the largest
	 * distance unit from units represented by the first element in X. The scale
	 * factors from X, Y (if present) and CYX (if Y is present) shall be used to
	 * convert from default user space to the appropriate units before applying
	 * the distance function.
	 * 
	 * @param d
	 */
	public void setD(NumberFormatArray d) {
		put(new PdfName("D"), d);
	}

	/**
	 * A number format array for measurement of area. The first element in the
	 * array shall specify the conversion to the largest area unit from units
	 * represented by the first element in X, squared. The scale factors from X,
	 * Y (if present) and CYX (if Y is present) shall be used to convert from
	 * default user space to the appropriate units before applying the area
	 * function.
	 * 
	 * @param a
	 */
	public void setA(NumberFormatArray a) {
		put(new PdfName("A"), a);
	}

	/**
	 * A number format array for measurement of angles. The first element in the
	 * array shall specify the conversion to the largest angle unit from
	 * degrees. The scale factor from CYX (if present) shall be used to convert
	 * from default user space to the appropriate units before applying the
	 * angle function.
	 * 
	 * @param t a PdfArray containing PdfNumber objects
	 */
	public void setT(NumberFormatArray t) {
		put(new PdfName("T"), t);
	}

	/**
	 * A number format array for measurement of the slope of a line. The first
	 * element in the array shall specify the conversion to the largest slope
	 * unit from units represented by the first element in Y divided by the
	 * first element in X. The scale factors from X, Y (if present) and CYX (if
	 * Y is present) shall be used to convert from default user space to the
	 * appropriate units before applying the slope function.
	 * 
	 * @param s a PdfArray containing PdfNumber objects
	 */
	public void setS(NumberFormatArray s) {
		put(new PdfName("S"), s);
	}

	/**
	 * An array of two numbers that shall specify the origin of the measurement
	 * coordinate system in default user space coordinates. The directions by
	 * which x and y increase in value from this origin shall be determined by
	 * {@link Viewport#setBBox(com.itextpdf.text.Rectangle)} entry.<br />
	 * Default value: the first coordinate pair (lower-left corner) of the
	 * rectangle specified by the viewport's BBox entry.
	 * 
	 * @param o an XYArray
	 */
	public void setO(XYArray o) {
		put(new PdfName("O"), o);
	}

	/**
	 * A factor that shall be used to convert the largest units along the y axis
	 * to the largest units along the x axis. It shall be used for calculations
	 * (distance, area, and angle) where the units are be equivalent; if not
	 * specified, these calculations may not be performed (which would be the
	 * case in situations such as x representing time and y representing
	 * temperature). Other calculations (change in x, change in y, and slope)
	 * shall not require this value.
	 * 
	 * @param cyx
	 */
	public void setCYX(PdfNumber cyx) {
		put(PdfName.CYX, cyx);
	}
}
