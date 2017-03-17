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

import com.itextpdf.text.pdf.NumberArray;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.spatial.units.Angular;
import com.itextpdf.text.pdf.spatial.units.Linear;
import com.itextpdf.text.pdf.spatial.units.Square;

/**
 * Geospatial Measure dictionary.
 * @since 5.1.0
 */
public class MeasureGeospatial extends Measure {

	/**
	 * Gets the subtype.
	 * In this case RL for a rectalinear coordinate system.
	 */
	PdfName getSubType() {
		return PdfName.GEO;
	}

	/**
	 * An array of numbers that shall be taken pairwise to define a series of
	 * points that describes the bounds of an area for which geospatial
	 * transformations are valid.
	 *
	 * @param bounds
	 */
	public void setBounds(NumberArray bounds) {
		super.put(PdfName.BOUNDS, bounds);
	}

	/**
	 * A projected or geographic coordinate system dictionary.
	 *
	 * @param cs
	 */
	public void setCoordinateSystem(CoordinateSystem cs) {
		super.put(PdfName.GCS, cs);
	}

	/**
	 * Optional coordinate system that allows a document to be authored
	 * to display values in a coordinate system other than that associated
	 * with the source data. For example, a map may be created in a state
	 * plane coordinate system based on a 1927 datum, but it is possible
	 * to display its latitude and longitude values in the WGS84 datum
	 * corresponding to values reported by a GPS device.
	 *
	 * @param cs
	 */
	public void setDisplayCoordinateSystem(GeographicCoordinateSystem cs) {
		super.put(PdfName.DCS, cs);
	}

	/**
	 * Three names that identify in order a linear display unit, an area display
	 * unit, and an angular display unit.
	 *
	 * @param l
	 * @param s
	 * @param a
	 */
	public void setDisplayUnits(Linear l, Square s, Angular a) {
		PdfArray arr = new PdfArray();
		arr.add(l.getPdfName());
		arr.add(s.getPdfName());
		arr.add(a.getPdfName());
		super.put(PdfName.PDU, arr);
	}

	/**
	 * An array of numbers that shall be taken pairwise, defining points in
	 * geographic space as degrees of latitude and longitude. These values shall
	 * be based on the geographic coordinate system described in the GCS
	 * dictionary.
	 *
	 * @param pairedpoints
	 */
	public void setGPTS(NumberArray pairedpoints) {
		put(PdfName.GPTS, pairedpoints);
	}

	/**
	 * An array of numbers that shall be taken pairwise to define points in a 2D
	 * unit square. The unit square is mapped to the rectangular bounds of the
	 * {@link Viewport}, image XObject, or forms XObject that contains the
	 * measure dictionary. This array shall contain the same number of number
	 * pairs as the GPTS array; each number pair is the unit square object
	 * position corresponding to the geospatial position in the GPTS array.
	 *
	 * @param pairedpoints
	 */
	public void setLPTS(NumberArray pairedpoints) {
		put(PdfName.LPTS, pairedpoints);
	}
}
