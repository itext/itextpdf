/*
 * $Id: RectangleReadOnly.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.text;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.BaseColor;

/**
 * A <CODE>RectangleReadOnly</CODE> is the representation of a geometric figure.
 * It's the same as a <CODE>Rectangle</CODE> but immutable.
 * Rectangles support constant width borders using
 * {@link #setBorderWidth(float)}and {@link #setBorder(int)}.
 * They also support borders that vary in width/color on each side using
 * methods like {@link #setBorderWidthLeft(float)}or
 * {@link #setBorderColorLeft(BaseColor)}.
 *
 * @see Element
 * @since 2.1.2
 */

public class RectangleReadOnly extends Rectangle {

	// CONSTRUCTORS

	/**
	 * Constructs a <CODE>RectangleReadOnly</CODE> -object.
	 *
	 * @param llx	lower left x
	 * @param lly	lower left y
	 * @param urx	upper right x
	 * @param ury	upper right y
	 */
	public RectangleReadOnly(final float llx, final float lly, final float urx, final float ury) {
        super(llx, lly, urx, ury);
	}

	/**
	 * Constructs a <CODE>RectangleReadOnly</CODE> -object.
	 *
	 * @param llx	lower left x
	 * @param lly	lower left y
	 * @param urx	upper right x
	 * @param ury	upper right y
	 * @param rotation	the rotation of the Rectangle (0, 90, 180, 270)
	 * @since iText 5.0.6
	 */
	public RectangleReadOnly(final float llx, final float lly, final float urx, final float ury, final int rotation) {
        super(llx, lly, urx, ury);
        super.setRotation(rotation);
	}

	/**
	 * Constructs a <CODE>RectangleReadOnly</CODE>-object starting from the origin
	 * (0, 0).
	 *
	 * @param urx	upper right x
	 * @param ury	upper right y
	 */
	public RectangleReadOnly(final float urx, final float ury) {
		super(0, 0, urx, ury);
	}

	/**
	 * Constructs a <CODE>RectangleReadOnly</CODE>-object starting from the origin
	 * (0, 0) and with a specific rotation (valid values are 0, 90, 180, 270).
	 *
	 * @param urx	upper right x
	 * @param ury	upper right y
	 * @param rotation the rotation
	 * @since iText 5.0.6
	 */
	public RectangleReadOnly(final float urx, final float ury, final int rotation) {
		super(0, 0, urx, ury);
		super.setRotation(rotation);
	}

	/**
	 * Constructs a <CODE>RectangleReadOnly</CODE> -object.
	 *
	 * @param rect	another <CODE>Rectangle</CODE>
	 */
	public RectangleReadOnly(final Rectangle rect) {
		super(rect.llx, rect.lly, rect.urx, rect.ury);
		super.cloneNonPositionParameters(rect);
	}

	/**
	 * Throws an error because of the read only nature of this object.
	 */
    private void throwReadOnlyError() {
        throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("rectanglereadonly.this.rectangle.is.read.only"));
    }

	/**
	 * Sets the rotation of the rectangle. Valid values are 0, 90, 180, and 270.
	 * @param rotation the new rotation value
	 * @since iText 5.0.6
	 */
	@Override
	public void setRotation(final int rotation) {
        throwReadOnlyError();
    }

    // OVERWRITE METHODS SETTING THE DIMENSIONS:

	/**
	 * Sets the lower left x-coordinate.
	 *
	 * @param llx	the new value
	 */
	@Override
	public void setLeft(final float llx) {
		throwReadOnlyError();
	}

	/**
	 * Sets the upper right x-coordinate.
	 *
	 * @param urx	the new value
	 */

	@Override
	public void setRight(final float urx) {
		throwReadOnlyError();
	}

	/**
	 * Sets the upper right y-coordinate.
	 *
	 * @param ury	the new value
	 */
	@Override
	public void setTop(final float ury) {
		throwReadOnlyError();
	}

	/**
	 * Sets the lower left y-coordinate.
	 *
	 * @param lly	the new value
	 */
	@Override
	public void setBottom(final float lly) {
		throwReadOnlyError();
	}

	/**
	 * Normalizes the rectangle.
     * Switches lower left with upper right if necessary.
	 */
	@Override
	public void normalize() {
		throwReadOnlyError();
	}

	// OVERWRITE METHODS SETTING THE BACKGROUND COLOR:

	/**
	 * Sets the backgroundcolor of the rectangle.
	 *
	 * @param value	the new value
	 */
	@Override
	public void setBackgroundColor(final BaseColor value) {
		throwReadOnlyError();
	}

	/**
	 * Sets the grayscale of the rectangle.
	 *
	 * @param value	the new value
	 */
	@Override
	public void setGrayFill(final float value) {
		throwReadOnlyError();
	}

	// OVERWRITE METHODS SETTING THE BORDER:

	/**
	 * Enables/Disables the border on the specified sides.
	 * The border is specified as an integer bitwise combination of
	 * the constants: <CODE>LEFT, RIGHT, TOP, BOTTOM</CODE>.
	 *
	 * @see #enableBorderSide(int)
	 * @see #disableBorderSide(int)
	 * @param border	the new value
	 */
	@Override
	public void setBorder(final int border) {
		throwReadOnlyError();
	}

	/**
	 * Sets a parameter indicating if the rectangle has variable borders
	 *
	 * @param useVariableBorders	indication if the rectangle has variable borders
	 */
	@Override
	public void setUseVariableBorders(final boolean useVariableBorders) {
		throwReadOnlyError();
	}

	/**
	 * Enables the border on the specified side.
	 *
	 * @param side	the side to enable.
	 * One of <CODE>LEFT, RIGHT, TOP, BOTTOM</CODE>
	 */
	@Override
	public void enableBorderSide(final int side) {
		throwReadOnlyError();
	}

	/**
	 * Disables the border on the specified side.
	 *
	 * @param side	the side to disable.
	 * One of <CODE>LEFT, RIGHT, TOP, BOTTOM</CODE>
	 */
	@Override
	public void disableBorderSide(final int side) {
		throwReadOnlyError();
	}

	// OVERWRITE METHODS SETTING THE BORDER WIDTH:

	/**
	 * Sets the borderwidth of the table.
	 *
	 * @param borderWidth	the new value
	 */

	@Override
	public void setBorderWidth(final float borderWidth) {
		throwReadOnlyError();
	}

	/**
	 * Sets the width of the left border
	 *
	 * @param borderWidthLeft	a width
	 */
	@Override
	public void setBorderWidthLeft(final float borderWidthLeft) {
		throwReadOnlyError();
	}

	/**
	 * Sets the width of the right border
	 *
	 * @param borderWidthRight	a width
	 */
	@Override
	public void setBorderWidthRight(final float borderWidthRight) {
		throwReadOnlyError();
	}

	/**
	 * Sets the width of the top border
	 *
	 * @param borderWidthTop	a width
	 */
	@Override
	public void setBorderWidthTop(final float borderWidthTop) {
		throwReadOnlyError();
	}

	/**
	 * Sets the width of the bottom border
	 *
	 * @param borderWidthBottom	a width
	 */
	@Override
	public void setBorderWidthBottom(final float borderWidthBottom) {
		throwReadOnlyError();
	}

	// METHODS TO GET/SET THE BORDER COLOR:

	/**
	 * Sets the color of the border.
	 *
	 * @param borderColor	a <CODE>BaseColor</CODE>
	 */

	@Override
	public void setBorderColor(final BaseColor borderColor) {
		throwReadOnlyError();
	}

	/**
	 * Sets the color of the left border.
	 *
	 * @param borderColorLeft	a <CODE>BaseColor</CODE>
	 */
	@Override
	public void setBorderColorLeft(final BaseColor borderColorLeft) {
		throwReadOnlyError();
	}

	/**
	 * Sets the color of the right border
	 *
	 * @param borderColorRight	a <CODE>BaseColor</CODE>
	 */
	@Override
	public void setBorderColorRight(final BaseColor borderColorRight) {
		throwReadOnlyError();
	}

	/**
	 * Sets the color of the top border.
	 *
	 * @param borderColorTop	a <CODE>BaseColor</CODE>
	 */
	@Override
	public void setBorderColorTop(final BaseColor borderColorTop) {
		throwReadOnlyError();
	}

	/**
	 * Sets the color of the bottom border.
	 *
	 * @param borderColorBottom	a <CODE>BaseColor</CODE>
	 */
	@Override
	public void setBorderColorBottom(final BaseColor borderColorBottom) {
		throwReadOnlyError();
	}

	// SPECIAL METHODS:

	/**
	 * Copies each of the parameters, except the position, from a
     * <CODE>Rectangle</CODE> object
	 *
	 * @param rect	<CODE>Rectangle</CODE> to copy from
	 */
	@Override
	public void cloneNonPositionParameters(final Rectangle rect) {
		throwReadOnlyError();
	}

	/**
	 * Copies each of the parameters, except the position, from a
     * <CODE>Rectangle</CODE> object if the value is set there.
	 *
	 * @param rect	<CODE>Rectangle</CODE> to copy from
	 */
	@Override
	public void softCloneNonPositionParameters(final Rectangle rect) {
		throwReadOnlyError();
	}

	/**
	 * @return	String version of the most important rectangle properties
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("RectangleReadOnly: ");
		buf.append(getWidth());
		buf.append('x');
		buf.append(getHeight());
		buf.append(" (rot: ");
		buf.append(rotation);
		buf.append(" degrees)");
		return buf.toString();
	}
}
