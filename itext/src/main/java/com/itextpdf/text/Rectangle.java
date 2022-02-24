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
package com.itextpdf.text;

import com.itextpdf.text.pdf.GrayColor;

import java.util.ArrayList;
import java.util.List;

/**
 * A <CODE>Rectangle</CODE> is the representation of a geometric figure.
 *
 * Rectangles support constant width borders using
 * {@link #setBorderWidth(float)}and {@link #setBorder(int)}.
 * They also support borders that vary in width/color on each side using
 * methods like {@link #setBorderWidthLeft(float)}or
 * {@link #setBorderColorLeft(BaseColor)}.
 *
 * @see Element
 */
public class Rectangle implements Element {

	// CONSTANTS:

	/** This is the value that will be used as <VAR>undefined </VAR>. */
	public static final int UNDEFINED = -1;

	/** This represents one side of the border of the <CODE>Rectangle</CODE>. */
	public static final int TOP = 1;

	/** This represents one side of the border of the <CODE>Rectangle</CODE>. */
	public static final int BOTTOM = 2;

	/** This represents one side of the border of the <CODE>Rectangle</CODE>. */
	public static final int LEFT = 4;

	/** This represents one side of the border of the <CODE>Rectangle</CODE>. */
	public static final int RIGHT = 8;

	/** This represents a rectangle without borders. */
	public static final int NO_BORDER = 0;

	/** This represents a type of border. */
	public static final int BOX = TOP + BOTTOM + LEFT + RIGHT;

	// MEMBER VARIABLES:

	/** the lower left x-coordinate. */
	protected float llx;

	/** the lower left y-coordinate. */
	protected float lly;

	/** the upper right x-coordinate. */
	protected float urx;

	/** the upper right y-coordinate. */
	protected float ury;

	/** The rotation of the Rectangle */
	protected int rotation = 0;

	/** This is the color of the background of this rectangle. */
	protected BaseColor backgroundColor = null;

	/** This represents the status of the 4 sides of the rectangle. */
	protected int border = UNDEFINED;

	/** Whether variable width/color borders are used. */
	protected boolean useVariableBorders = false;

	/** This is the width of the border around this rectangle. */
	protected float borderWidth = UNDEFINED;

	/** The width of the left border of this rectangle. */
	protected float borderWidthLeft = UNDEFINED;

	/** The width of the right border of this rectangle. */
	protected float borderWidthRight = UNDEFINED;

	/** The width of the top border of this rectangle. */
	protected float borderWidthTop = UNDEFINED;

	/** The width of the bottom border of this rectangle. */
	protected float borderWidthBottom = UNDEFINED;

	/** The color of the border of this rectangle. */
	protected BaseColor borderColor = null;

	/** The color of the left border of this rectangle. */
	protected BaseColor borderColorLeft = null;

	/** The color of the right border of this rectangle. */
	protected BaseColor borderColorRight = null;

	/** The color of the top border of this rectangle. */
	protected BaseColor borderColorTop = null;

	/** The color of the bottom border of this rectangle. */
	protected BaseColor borderColorBottom = null;

	// CONSTRUCTORS:

	/**
	 * Constructs a <CODE>Rectangle</CODE> -object.
	 *
	 * @param llx	lower left x
	 * @param lly	lower left y
	 * @param urx	upper right x
	 * @param ury	upper right y
	 */
	public Rectangle(final float llx, final float lly, final float urx, final float ury) {
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
	}

	/**
	 * Constructs a <CODE>Rectangle</CODE>-object.
	 *
	 * @param llx	lower left x
	 * @param lly	lower left y
	 * @param urx	upper right x
	 * @param ury	upper right y
	 * @param rotation the rotation (0, 90, 180, or 270)
	 * @since iText 5.0.6
	 */
	public Rectangle(final float llx, final float lly, final float urx, final float ury, final int rotation) {
		this(llx, lly, urx, ury);
		setRotation(rotation);
	}

	/**
	 * Constructs a <CODE>Rectangle</CODE> -object starting from the origin
	 * (0, 0).
	 *
	 * @param urx	upper right x
	 * @param ury	upper right y
	 */
	public Rectangle(final float urx, final float ury) {
		this(0, 0, urx, ury);
	}

	/**
	 * Constructs a <CODE>Rectangle</CODE>-object starting from the origin
	 * (0, 0) and with a specific rotation (valid values are 0, 90, 180, 270).
	 *
	 * @param urx	upper right x
	 * @param ury	upper right y
	 * @param rotation the rotation of the rectangle
	 * @since iText 5.0.6
	 */
	public Rectangle(final float urx, final float ury, final int rotation) {
		this(0, 0, urx, ury, rotation);
	}

	/**
	 * Constructs a <CODE>Rectangle</CODE> -object.
	 *
	 * @param rect	another <CODE>Rectangle</CODE>
	 */
	public Rectangle(final Rectangle rect) {
		this(rect.llx, rect.lly, rect.urx, rect.ury);
		cloneNonPositionParameters(rect);
	}

    /**
     * Constructs a <CODE>Rectangle</CODE>-object based on a <CODE>com.itextpdf.awt.geom.Rectangle</CODE> object
     * @param rect com.itextpdf.awt.geom.Rectangle
     */
    public Rectangle(com.itextpdf.awt.geom.Rectangle rect) {
        this((float) rect.getX(),(float) rect.getY(),(float) (rect.getX() + rect.getWidth()),(float) (rect.getY() + rect.getHeight()));
    }

	// IMPLEMENTATION OF THE ELEMENT INTERFACE:e

	/**
	 * Processes the element by adding it (or the different parts) to an
	 * <CODE>ElementListener</CODE>.
	 *
	 * @param listener	an <CODE>ElementListener</CODE>
	 * @return <CODE>true</CODE> if the element was processed successfully
	 */
	public boolean process(final ElementListener listener) {
		try {
			return listener.add(this);
		}
		catch (DocumentException de) {
			return false;
		}
	}

	/**
	 * Gets the type of the text element.
	 *
	 * @return a type
	 */
	public int type() {
		return Element.RECTANGLE;
	}

	/**
	 * Gets all the chunks in this element.
	 *
	 * @return an <CODE>ArrayList</CODE>
	 */
	public List<Chunk> getChunks() {
		return new ArrayList<Chunk>();
	}

	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return false;
	}

	// METHODS TO GET/SET THE DIMENSIONS:

	/**
	 * Sets the lower left x-coordinate.
	 *
	 * @param llx	the new value
	 */
	public void setLeft(final float llx) {
		this.llx = llx;
	}

	/**
	 * Returns the lower left x-coordinate.
	 *
	 * @return the lower left x-coordinate
	 */
	public float getLeft() {
		return llx;
	}

	/**
	 * Returns the lower left x-coordinate, considering a given margin.
	 *
	 * @param margin	a margin
	 * @return the lower left x-coordinate
	 */
	public float getLeft(final float margin) {
		return llx + margin;
	}

	/**
	 * Sets the upper right x-coordinate.
	 *
	 * @param urx	the new value
	 */
	public void setRight(final float urx) {
		this.urx = urx;
	}

	/**
	 * Returns the upper right x-coordinate.
	 *
	 * @return the upper right x-coordinate
	 */
	public float getRight() {
		return urx;
	}

	/**
	 * Returns the upper right x-coordinate, considering a given margin.
	 *
	 * @param margin	a margin
	 * @return the upper right x-coordinate
	 */
	public float getRight(final float margin) {
		return urx - margin;
	}

	/**
	 * Returns the width of the rectangle.
	 *
	 * @return	the width
	 */
	public float getWidth() {
		return urx - llx;
	}

	/**
	 * Sets the upper right y-coordinate.
	 *
	 * @param ury	the new value
	 */
	public void setTop(final float ury) {
		this.ury = ury;
	}

	/**
	 * Returns the upper right y-coordinate.
	 *
	 * @return the upper right y-coordinate
	 */
	public float getTop() {
		return ury;
	}

	/**
	 * Returns the upper right y-coordinate, considering a given margin.
	 *
	 * @param margin	a margin
	 * @return the upper right y-coordinate
	 */
	public float getTop(final float margin) {
		return ury - margin;
	}

	/**
	 * Sets the lower left y-coordinate.
	 *
	 * @param lly	the new value
	 */
	public void setBottom(final float lly) {
		this.lly = lly;
	}

	/**
	 * Returns the lower left y-coordinate.
	 *
	 * @return the lower left y-coordinate
	 */
	public float getBottom() {
		return lly;
	}

	/**
	 * Returns the lower left y-coordinate, considering a given margin.
	 *
	 * @param margin	a margin
	 * @return the lower left y-coordinate
	 */
	public float getBottom(final float margin) {
		return lly + margin;
	}

	/**
	 * Returns the height of the rectangle.
	 *
	 * @return the height
	 */
	public float getHeight() {
		return ury - lly;
	}

	/**
	 * Normalizes the rectangle.
	 * Switches lower left with upper right if necessary.
	 */
	public void normalize() {
		if (llx > urx) {
			float a = llx;
			llx = urx;
			urx = a;
		}
		if (lly > ury) {
			float a = lly;
			lly = ury;
			ury = a;
		}
	}

	// METHODS TO GET/SET THE ROTATION:

	/**
	 * Gets the rotation of the rectangle
	 *
	 * @return a rotation value
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * Sets the rotation of the rectangle. Valid values are 0, 90, 180, and 270.
	 * @param rotation the new rotation value
	 * @since iText 5.0.6
	 */
	public void setRotation(final int rotation) {
		this.rotation = rotation % 360;
		switch (this.rotation) {
		case 90:
		case 180:
		case 270:
            break;
        default:
			this.rotation = 0;
		}
	}

	/**
	 * Rotates the rectangle.
	 * Swaps the values of llx and lly and of urx and ury.
	 *
	 * @return the rotated <CODE>Rectangle</CODE>
	 */
	public Rectangle rotate() {
		Rectangle rect = new Rectangle(lly, llx, ury, urx);
		rect.setRotation(rotation + 90);
		return rect;
	}

	// METHODS TO GET/SET THE BACKGROUND COLOR:

	/**
	 * Gets the backgroundcolor.
	 *
	 * @return a <CODE>BaseColor</CODE>
	 */
	public BaseColor getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Sets the backgroundcolor of the rectangle.
	 *
	 * @param backgroundColor	a <CODE>BaseColor</CODE>
	 */

	public void setBackgroundColor(final BaseColor backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Gets the grayscale.
	 *
	 * @return the grayscale color of the background
     * or 0 if the background has no grayscale color.
	 */
	public float getGrayFill() {
        if (backgroundColor instanceof GrayColor)
            return ((GrayColor)backgroundColor).getGray();
        return 0;
	}

	/**
	 * Sets the the background color to a grayscale value.
	 *
	 * @param value	the new grayscale value
	 */
	public void setGrayFill(final float value) {
        backgroundColor = new GrayColor(value);
	}

//	 METHODS TO GET/SET THE BORDER:

	/**
	 * Returns the exact type of the border.
	 *
	 * @return a value
	 */
	public int getBorder() {
		return border;
	}

	/**
	 * Indicates whether some type of border is set.
	 *
	 * @return a boolean
	 */
	public boolean hasBorders() {
		switch (border) {
			case UNDEFINED:
			case NO_BORDER:
				return false;
			default:
				return borderWidth > 0 || borderWidthLeft > 0
						|| borderWidthRight > 0 || borderWidthTop > 0 || borderWidthBottom > 0;
		}
	}

	/**
	 * Indicates whether the specified type of border is set.
	 *
	 * @param type	the type of border
	 * @return a boolean
	 */
	public boolean hasBorder(final int type) {
		if (border == UNDEFINED)
			return false;
		return (border & type) == type;
	}

	/**
	 * Enables/Disables the border on the specified sides.
	 * The border is specified as an integer bitwise combination of
	 * the constants: <CODE>LEFT, RIGHT, TOP, BOTTOM</CODE>.
	 *
	 * @see #enableBorderSide(int)
	 * @see #disableBorderSide(int)
	 * @param border	the new value
	 */
	public void setBorder(final int border) {
		this.border = border;
	}

	/**
	 * Indicates whether variable width borders are being used.
	 * Returns true if <CODE>setBorderWidthLeft, setBorderWidthRight,
	 * setBorderWidthTop, or setBorderWidthBottom</CODE> has been called.
	 *
	 * @return true if variable width borders are in use
	 */
	public boolean isUseVariableBorders() {
		return useVariableBorders;
	}

	/**
	 * Sets a parameter indicating if the rectangle has variable borders
	 *
	 * @param useVariableBorders indication if the rectangle has variable borders
	 */
	public void setUseVariableBorders(final boolean useVariableBorders) {
		this.useVariableBorders = useVariableBorders;
	}

	/**
	 * Enables the border on the specified side.
	 *
	 * @param side	the side to enable.
	 * One of <CODE>LEFT, RIGHT, TOP, BOTTOM</CODE>
	 */
	public void enableBorderSide(final int side) {
		if (border == UNDEFINED)
			border = 0;
		border |= side;
	}

	/**
	 * Disables the border on the specified side.
	 *
	 * @param side	the side to disable.
	 * One of <CODE>LEFT, RIGHT, TOP, BOTTOM</CODE>
	 */
	public void disableBorderSide(final int side) {
		if (border == UNDEFINED)
			border = 0;
		border &= ~side;
	}

	// METHODS TO GET/SET THE BORDER WIDTH:

	/**
	 * Gets the borderwidth.
	 *
	 * @return a value
	 */
	public float getBorderWidth() {
		return borderWidth;
	}

	/**
	 * Sets the borderwidth of the table.
	 *
	 * @param borderWidth the new value
	 */
	public void setBorderWidth(final float borderWidth) {
		this.borderWidth = borderWidth;
	}

	/**
	 * Helper function returning the border width of a specific side.
	 *
	 * @param	variableWidthValue	a variable width (could be undefined)
	 * @param	side	the border you want to check
	 * @return	the variableWidthValue if not undefined, otherwise the borderWidth
	 */
	private float getVariableBorderWidth(final float variableWidthValue, final int side) {
		if ((border & side) != 0)
			return variableWidthValue != UNDEFINED ? variableWidthValue : borderWidth;
		return 0;
	}

	/**
	 * Helper function updating the border flag for a side
	 * based on the specified width.
	 * A width of 0 will disable the border on that side.
	 * Any other width enables it.
	 *
	 * @param width	width of border
	 * @param side	border side constant
	 */
	private void updateBorderBasedOnWidth(final float width, final int side) {
		useVariableBorders = true;
		if (width > 0)
			enableBorderSide(side);
		else
			disableBorderSide(side);
	}

	/**
	 * Gets the width of the left border.
	 *
	 * @return a width
	 */
	public float getBorderWidthLeft() {
		return getVariableBorderWidth(borderWidthLeft, LEFT);
	}

	/**
	 * Sets the width of the left border.
	 *
	 * @param borderWidthLeft a width
	 */
	public void setBorderWidthLeft(final float borderWidthLeft) {
		this.borderWidthLeft = borderWidthLeft;
		updateBorderBasedOnWidth(borderWidthLeft, LEFT);
	}

	/**
	 * Gets the width of the right border.
	 *
	 * @return a width
	 */
	public float getBorderWidthRight() {
		return getVariableBorderWidth(borderWidthRight, RIGHT);
	}

	/**
	 * Sets the width of the right border.
	 *
	 * @param borderWidthRight a width
	 */
	public void setBorderWidthRight(final float borderWidthRight) {
		this.borderWidthRight = borderWidthRight;
		updateBorderBasedOnWidth(borderWidthRight, RIGHT);
	}

	/**
	 * Gets the width of the top border.
	 *
	 * @return a width
	 */
	public float getBorderWidthTop() {
		return getVariableBorderWidth(borderWidthTop, TOP);
	}

	/**
	 * Sets the width of the top border.
	 *
	 * @param borderWidthTop a width
	 */
	public void setBorderWidthTop(final float borderWidthTop) {
		this.borderWidthTop = borderWidthTop;
		updateBorderBasedOnWidth(borderWidthTop, TOP);
	}

	/**
	 * Gets the width of the bottom border.
	 *
	 * @return a width
	 */
	public float getBorderWidthBottom() {
		return getVariableBorderWidth(borderWidthBottom, BOTTOM);
	}

	/**
	 * Sets the width of the bottom border.
	 *
	 * @param borderWidthBottom a width
	 */
	public void setBorderWidthBottom(final float borderWidthBottom) {
		this.borderWidthBottom = borderWidthBottom;
		updateBorderBasedOnWidth(borderWidthBottom, BOTTOM);
	}

	// METHODS TO GET/SET THE BORDER COLOR:

	/**
	 * Gets the color of the border.
	 *
	 * @return	a <CODE>BaseColor</CODE>
	 */
	public BaseColor getBorderColor() {
		return borderColor;
	}

	/**
	 * Sets the color of the border.
	 *
	 * @param borderColor a <CODE>BaseColor</CODE>
	 */
	public void setBorderColor(final BaseColor borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * Gets the color of the left border.
	 *
	 * @return a <CODE>BaseColor</CODE>
	 */
	public BaseColor getBorderColorLeft() {
		if (borderColorLeft == null)
			return borderColor;
		return borderColorLeft;
	}

	/**
	 * Sets the color of the left border.
	 *
	 * @param borderColorLeft a <CODE>BaseColor</CODE>
	 */
	public void setBorderColorLeft(final BaseColor borderColorLeft) {
		this.borderColorLeft = borderColorLeft;
	}

	/**
	 * Gets the color of the right border.
	 *
	 * @return a <CODE>BaseColor</CODE>
	 */
	public BaseColor getBorderColorRight() {
		if (borderColorRight == null)
			return borderColor;
		return borderColorRight;
	}

	/**
	 * Sets the color of the right border.
	 *
	 * @param borderColorRight a <CODE>BaseColor</CODE>
	 */
	public void setBorderColorRight(final BaseColor borderColorRight) {
		this.borderColorRight = borderColorRight;
	}

	/**
	 * Gets the color of the top border.
	 *
	 * @return a <CODE>BaseColor</CODE>
	 */
	public BaseColor getBorderColorTop() {
		if (borderColorTop == null)
			return borderColor;
		return borderColorTop;
	}

	/**
	 * Sets the color of the top border.
	 *
	 * @param borderColorTop a <CODE>BaseColor</CODE>
	 */
	public void setBorderColorTop(final BaseColor borderColorTop) {
		this.borderColorTop = borderColorTop;
	}

	/**
	 * Gets the color of the bottom border.
	 *
	 * @return a <CODE>BaseColor</CODE>
	 */
	public BaseColor getBorderColorBottom() {
		if (borderColorBottom == null)
			return borderColor;
		return borderColorBottom;
	}

	/**
	 * Sets the color of the bottom border.
	 *
	 * @param borderColorBottom a <CODE>BaseColor</CODE>
	 */
	public void setBorderColorBottom(final BaseColor borderColorBottom) {
		this.borderColorBottom = borderColorBottom;
	}

	// SPECIAL METHODS:

	/**
	 * Gets a Rectangle that is altered to fit on the page.
	 *
	 * @param top		the top position
	 * @param bottom	the bottom position
	 * @return a <CODE>Rectangle</CODE>
	 */
	public Rectangle rectangle(final float top, final float bottom) {
		Rectangle tmp = new Rectangle(this);
		if (getTop() > top) {
			tmp.setTop(top);
			tmp.disableBorderSide(TOP);
		}
		if (getBottom() < bottom) {
			tmp.setBottom(bottom);
			tmp.disableBorderSide(BOTTOM);
		}
		return tmp;
	}

	/**
	 * Copies each of the parameters, except the position, from a
	 * <CODE>Rectangle</CODE> object
	 *
	 * @param rect	<CODE>Rectangle</CODE> to copy from
	 */
	public void cloneNonPositionParameters(final Rectangle rect) {
		this.rotation = rect.rotation;
		this.backgroundColor = rect.backgroundColor;
		this.border = rect.border;
		this.useVariableBorders = rect.useVariableBorders;
		this.borderWidth = rect.borderWidth;
		this.borderWidthLeft = rect.borderWidthLeft;
		this.borderWidthRight = rect.borderWidthRight;
		this.borderWidthTop = rect.borderWidthTop;
		this.borderWidthBottom = rect.borderWidthBottom;
		this.borderColor = rect.borderColor;
		this.borderColorLeft = rect.borderColorLeft;
		this.borderColorRight = rect.borderColorRight;
		this.borderColorTop = rect.borderColorTop;
		this.borderColorBottom = rect.borderColorBottom;
	}

	/**
	 * Copies each of the parameters, except the position, from a
	 * <CODE>Rectangle</CODE> object if the value is set there
	 *
	 * @param rect <CODE>Rectangle</CODE> to copy from
	 */
	public void softCloneNonPositionParameters(final Rectangle rect) {
		if (rect.rotation != 0)
			this.rotation = rect.rotation;
		if (rect.backgroundColor != null)
			this.backgroundColor = rect.backgroundColor;
		if (rect.border != UNDEFINED)
			this.border = rect.border;
		if (useVariableBorders)
			this.useVariableBorders = rect.useVariableBorders;
		if (rect.borderWidth != UNDEFINED)
			this.borderWidth = rect.borderWidth;
		if (rect.borderWidthLeft != UNDEFINED)
			this.borderWidthLeft = rect.borderWidthLeft;
		if (rect.borderWidthRight != UNDEFINED)
			this.borderWidthRight = rect.borderWidthRight;
		if (rect.borderWidthTop != UNDEFINED)
			this.borderWidthTop = rect.borderWidthTop;
		if (rect.borderWidthBottom != UNDEFINED)
			this.borderWidthBottom = rect.borderWidthBottom;
		if (rect.borderColor != null)
			this.borderColor = rect.borderColor;
		if (rect.borderColorLeft != null)
			this.borderColorLeft = rect.borderColorLeft;
		if (rect.borderColorRight != null)
			this.borderColorRight = rect.borderColorRight;
		if (rect.borderColorTop != null)
			this.borderColorTop = rect.borderColorTop;
		if (rect.borderColorBottom != null)
			this.borderColorBottom = rect.borderColorBottom;
	}

	/**
	 * @return	a String representation of the rectangle
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString() {
		StringBuffer buf = new StringBuffer("Rectangle: ");
		buf.append(getWidth());
		buf.append('x');
		buf.append(getHeight());
		buf.append(" (rot: ");
		buf.append(rotation);
		buf.append(" degrees)");
		return buf.toString();
	}

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof Rectangle ) {
            Rectangle other = (Rectangle) obj;
            // should we normalize here?
            // normalization changes the structure and coordinates of the rectangle, so I'm inclined not to call normalize()
            // but it needs to be considered ~ Michaël D.
            return other.llx == this.llx && other.lly == this.lly && other.urx == this.urx && other.ury == this.ury && other.rotation == this.rotation;
        } else {
            return false;
        }
    }
}
