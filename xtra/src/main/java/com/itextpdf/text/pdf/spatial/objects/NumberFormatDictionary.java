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
package com.itextpdf.text.pdf.spatial.objects;

import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.spatial.units.Fraction;

/**
 * A dictionary that represents a specific unit of measurement (such as miles or feet).
 * It contains information about how each unit shall be expressed in text and factors
 * for calculating the number of units.
 * @since 5.1.0
 */
public class NumberFormatDictionary extends PdfDictionary {
	
	/**
	 * Creates a new NumberFormat dictionary.
	 */
	public NumberFormatDictionary() {
		super(PdfName.NUMBERFORMAT);
	}

	/**
	 * A text string specifying a label for displaying the units represented by
	 * this NumberFormat in a user interface; the label should use a universally
	 * recognized abbreviation.
	 * 
	 * @param label
	 */
	public void setLabel(PdfString label) {
		super.put(PdfName.U, label);
	}

	/**
	 * The conversion factor used to multiply a value in partial units of the
	 * previous number format array element to obtain a value in the units of
	 * this dictionary. When this entry is in the first number format in the
	 * array, its meaning (that is, what it shall be multiplied by) depends on
	 * which entry in the RectilinearMeasure references the NumberFormat
	 * array.
	 * 
	 * @param n
	 */
	public void setConversionFactor(PdfNumber n) {
		super.put(PdfName.C, n);
	}

	/**
	 * Indicate whether and in what manner to display a fractional value from
	 * the result of converting to the units of this NumberFormat means of the
	 * conversion factor entry.
	 * 
	 * @param f
	 */
	public void setFractionalValue(Fraction f) {
		super.put(PdfName.F, f.getPdfName());
	}

	/**
	 * A positive integer that shall specify the precision or denominator of a
	 * fractional amount:
	 * <ul>
	 * <li>
	 * When the Fractional Value is {@link Fraction#DECIMAL}, this entry shall
	 * be the precision of a decimal display; it shall be a multiple of 10.
	 * Low-order zeros may be truncated unless FixedDenominator is true. Default
	 * value: 100 (hundredths, corresponding to two decimal digits).</li>
	 * <li>When the value of F is {@link Fraction#FRACTION}, this entry shall be
	 * the denominator of a fractional display. The fraction may be reduced
	 * unless the value of FD is true. Default value: 16.</li>
	 * </ul>
	 * 
	 * @param precision
	 */
	public void setPrecision(PdfNumber precision) {
		super.put(PdfName.D, precision);
	}

	/**
	 * If true, a fractional value formatted according to Precision may not have
	 * its denominator reduced or low-order zeros truncated.
	 * 
	 * @param isFixedDenominator
	 */
	public void setFixedDenominator(PdfBoolean isFixedDenominator) {
		super.put(PdfName.FD, isFixedDenominator);
	}

	/**
	 * Text that shall be used between orders of thousands in display of
	 * numerical values. An empty string indicates that no text shall be added.<br />
	 * Default value: COMMA "\u002C"
	 * 
	 * @param rt
	 */
	public void setCipherGroupingCharacter(PdfString rt) {
		super.put(PdfName.RT, rt);
	}

	/**
	 * Text that shall be used as the decimal position in displaying numerical
	 * values. An empty string indicates that the default shall be used.<br />
	 * Default value: PERIOD "\u002E"
	 * 
	 * @param dc
	 */
	public void setDecimalChartacter(PdfString dc) {
		super.put(PdfName.RD, dc);
	}

	/**
	 * Text that shall be concatenated to the left of the label specified by
	 * setLabel. An empty string indicates that no text shall be added.<br />
	 * Default value: A single ASCII SPACE character "\u0020"
	 * 
	 * @param ps
	 */
	public void setLabelLeftString(PdfString ps) {
		super.put(PdfName.PS, ps);
	}

	/**
	 * Text that shall be concatenated after the label specified by setLabel. An
	 * empty string indicates that no text shall be added.<br />
	 * Default value: A single ASCII SPACE character "\u0020"
	 * 
	 * @param ss
	 */
	public void setLabelRightString(PdfString ss) {
		super.put(PdfName.SS, ss);
	}

	/**
	 * A name indicating the position of the label specified by setLabel with respect
	 * to the calculated unit value. The characters
	 * specified by setLabelLeftString and setLabelRightString shall be concatenated before considering this
	 * entry. Default value: suffix.
	 * @param pos PdfName.S or PdfName.P
	 */
	public void setLabelPosition(PdfName pos) {
		super.put(PdfName.O, pos);
	}
}
