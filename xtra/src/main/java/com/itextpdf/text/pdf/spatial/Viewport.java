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

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfRectangle;
import com.itextpdf.text.pdf.PdfString;

/**
 * A ViewPort dictionary.
 * @since 5.1.0
 */
public class Viewport extends PdfDictionary {

	/**
	 * Creates a ViewPort dictionary.
	 */
	public Viewport() {
		super(PdfName.VIEWPORT);
	}

	/**
	 * (Required) A rectangle in default user space coordinates specifying the
	 * location of the viewport on the page.<br />
	 * The two coordinate pairs of the rectangle shall be specified in
	 * normalized form; that is, lower-left followed by upper-right, relative to
	 * the measuring coordinate system. This ordering shall determine the
	 * orientation of the measuring coordinate system (that is, the direction of
	 * the positive x and y-axes) in this viewport, which may have a different
	 * rotation from the page.<br />
	 * The coordinates of this rectangle are independent of the origin of the
	 * measuring coordinate system, specified in the Origin entry of the
	 * measurement dictionary specified by Measure.
	 *
	 * @param bbox
	 */
	public void setBBox(final Rectangle bbox) {
		super.put(PdfName.BBOX, new PdfRectangle(bbox, bbox.getRotation()));
	}

	/**
	 * (Optional) A descriptive text string or title of the viewport, intended
	 * for use in a user interface.
	 *
	 * @param value
	 */
	public void setName(final PdfString value) {
		super.put(PdfName.NAME, value);
	}

	/**
	 * A measure dictionary that specifies the scale and units that shall apply
	 * to measurements taken on the contents within the viewport.
	 *
	 * @param measure
	 */
	public void setMeasure(final Measure measure) {
		super.put(PdfName.MEASURE, measure);
	}

	/**
	 * {@link PointData} that shall specify the extended geospatial data that
	 * applies to the image.
	 *
	 * @param ptData
	 */
	public void setPtData(final PointData ptData) {
		super.put(PdfName.PTDATA, ptData);
	}
}
