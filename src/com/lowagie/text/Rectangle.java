/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

/**
 * A <CODE>Rectangle</CODE> is the representation of a geometric figure.
 * 
 * Rectangles support constant width borders using
 * {@link #setBorderWidth(float)}and {@link #setBorder(int)}. They also
 * support borders that vary in width/color on each side using methods like
 * {@link #setBorderWidthLeft(float)}or
 * {@link #setBorderColorLeft(java.awt.Color)}.
 * 
 * @see Element
 * @see Table
 * @see Cell
 * @see HeaderFooter
 */

public class Rectangle implements Element, MarkupAttributes {

	// static membervariables (concerning the presence of borders)

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

	// membervariables

	/** the lower left x-coordinate. */
	protected float llx;

	/** the lower left y-coordinate. */
	protected float lly;

	/** the upper right x-coordinate. */
	protected float urx;

	/** the upper right y-coordinate. */
	protected float ury;

	/** This represents the status of the 4 sides of the rectangle. */
	protected int border = UNDEFINED;

	/** This is the width of the border around this rectangle. */
	protected float borderWidth = UNDEFINED;

	/** The color of the border of this rectangle. */
	protected Color color = null;

	/** The color of the left border of this rectangle. */
	protected Color borderColorLeft = null;

	/** The color of the right border of this rectangle. */
	protected Color borderColorRight = null;

	/** The color of the top border of this rectangle. */
	protected Color borderColorTop = null;

	/** The color of the bottom border of this rectangle. */
	protected Color borderColorBottom = null;

	/** The width of the left border of this rectangle. */
	protected float borderWidthLeft = UNDEFINED;

	/** The width of the right border of this rectangle. */
	protected float borderWidthRight = UNDEFINED;

	/** The width of the top border of this rectangle. */
	protected float borderWidthTop = UNDEFINED;

	/** The width of the bottom border of this rectangle. */
	protected float borderWidthBottom = UNDEFINED;

	/** Whether variable width borders are used. */
	protected boolean useVariableBorders = false;

	/** This is the color of the background of this rectangle. */
	protected Color background = null;

	/** This is the grayscale value of the background of this rectangle. */
	protected float grayFill = 0;

	protected int rotation = 0;

	/** Contains extra markupAttributes */
	protected Properties markupAttributes;

	// constructors

	/**
	 * Constructs a <CODE>Rectangle</CODE> -object.
	 * 
	 * @param llx
	 *            lower left x
	 * @param lly
	 *            lower left y
	 * @param urx
	 *            upper right x
	 * @param ury
	 *            upper right y
	 */

	public Rectangle(float llx, float lly, float urx, float ury) {
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
	}

	/**
	 * Constructs a <CODE>Rectangle</CODE> -object starting from the origin
	 * (0, 0).
	 * 
	 * @param urx
	 *            upper right x
	 * @param ury
	 *            upper right y
	 */

	public Rectangle(float urx, float ury) {
		this(0, 0, urx, ury);
	}

	/**
	 * Constructs a <CODE>Rectangle</CODE> -object.
	 * 
	 * @param rect
	 *            another <CODE>Rectangle</CODE>
	 */

	public Rectangle(Rectangle rect) {
		this(rect.llx, rect.lly, rect.urx, rect.ury);
		cloneNonPositionParameters(rect);
	}

	/**
	 * Copies all of the parameters from a <CODE>Rectangle</CODE> object
	 * except the position.
	 * 
	 * @param rect
	 *            <CODE>Rectangle</CODE> to copy from
	 */

	public void cloneNonPositionParameters(Rectangle rect) {
		this.rotation = rect.rotation;
		this.border = rect.border;
		this.borderWidth = rect.borderWidth;
		this.color = rect.color;
		this.background = rect.background;
		this.grayFill = rect.grayFill;
		this.borderColorLeft = rect.borderColorLeft;
		this.borderColorRight = rect.borderColorRight;
		this.borderColorTop = rect.borderColorTop;
		this.borderColorBottom = rect.borderColorBottom;
		this.borderWidthLeft = rect.borderWidthLeft;
		this.borderWidthRight = rect.borderWidthRight;
		this.borderWidthTop = rect.borderWidthTop;
		this.borderWidthBottom = rect.borderWidthBottom;
		this.useVariableBorders = rect.useVariableBorders;
	}

	/**
	 * Copies all of the parameters from a <CODE>Rectangle</CODE> object
	 * except the position.
	 * 
	 * @param rect
	 *            <CODE>Rectangle</CODE> to copy from
	 */

	public void softCloneNonPositionParameters(Rectangle rect) {
		if (rect.rotation != 0)
			this.rotation = rect.rotation;
		if (rect.border != UNDEFINED)
			this.border = rect.border;
		if (rect.borderWidth != UNDEFINED)
			this.borderWidth = rect.borderWidth;
		if (rect.color != null)
			this.color = rect.color;
		if (rect.background != null)
			this.background = rect.background;
		if (rect.grayFill != 0)
			this.grayFill = rect.grayFill;
		if (rect.borderColorLeft != null)
			this.borderColorLeft = rect.borderColorLeft;
		if (rect.borderColorRight != null)
			this.borderColorRight = rect.borderColorRight;
		if (rect.borderColorTop != null)
			this.borderColorTop = rect.borderColorTop;
		if (rect.borderColorBottom != null)
			this.borderColorBottom = rect.borderColorBottom;
		if (rect.borderWidthLeft != UNDEFINED)
			this.borderWidthLeft = rect.borderWidthLeft;
		if (rect.borderWidthRight != UNDEFINED)
			this.borderWidthRight = rect.borderWidthRight;
		if (rect.borderWidthTop != UNDEFINED)
			this.borderWidthTop = rect.borderWidthTop;
		if (rect.borderWidthBottom != UNDEFINED)
			this.borderWidthBottom = rect.borderWidthBottom;
		if (useVariableBorders)
			this.useVariableBorders = rect.useVariableBorders;
	}

	// implementation of the Element interface

	/**
	 * Processes the element by adding it (or the different parts) to an <CODE>
	 * ElementListener</CODE>.
	 * 
	 * @param listener
	 *            an <CODE>ElementListener</CODE>
	 * @return <CODE>true</CODE> if the element was processed successfully
	 */

	public boolean process(ElementListener listener) {
		try {
			return listener.add(this);
		} catch (DocumentException de) {
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

	public ArrayList getChunks() {
		return new ArrayList();
	}

	// methods

	/**
	 * Switches lowerleft with upperright
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

	/**
	 * Gets a Rectangle that is altered to fit on the page.
	 * 
	 * @param top
	 *            the top position
	 * @param bottom
	 *            the bottom position
	 * @return a <CODE>Rectangle</CODE>
	 */

	public Rectangle rectangle(float top, float bottom) {
		Rectangle tmp = new Rectangle(this);
		if (top() > top) {
			tmp.setTop(top);
			tmp.setBorder(border - (border & TOP));
		}
		if (bottom() < bottom) {
			tmp.setBottom(bottom);
			tmp.setBorder(border - (border & BOTTOM));
		}
		return tmp;
	}

	/**
	 * Swaps the values of urx and ury and of lly and llx in order to rotate the
	 * rectangle.
	 * 
	 * @return a <CODE>Rectangle</CODE>
	 */

	public Rectangle rotate() {
		Rectangle rect = new Rectangle(lly, llx, ury, urx);
		rect.rotation = rotation + 90;
		rect.rotation %= 360;
		return rect;
	}

	// methods to set the membervariables

	/**
	 * Sets the lower left x-coordinate.
	 * 
	 * @param value
	 *            the new value
	 */

	public void setLeft(float value) {
		llx = value;
	}

	/**
	 * Sets the upper right x-coordinate.
	 * 
	 * @param value
	 *            the new value
	 */

	public void setRight(float value) {
		urx = value;
	}

	/**
	 * Sets the upper right y-coordinate.
	 * 
	 * @param value
	 *            the new value
	 */

	public void setTop(float value) {
		ury = value;
	}

	/**
	 * Sets the lower left y-coordinate.
	 * 
	 * @param value
	 *            the new value
	 */

	public void setBottom(float value) {
		lly = value;
	}

	/**
	 * Enables/Disables the border on the specified sides. The border is
	 * specified as an integer bitwise combination of the constants: <CODE>
	 * LEFT, RIGHT, TOP, BOTTOM</CODE>.
	 * 
	 * @see #enableBorderSide(int)
	 * @see #disableBorderSide(int)
	 * @param value
	 *            the new value
	 */

	public void setBorder(int value) {
		border = value;
	}

	/**
	 * Enables the border on the specified side.
	 * 
	 * @param side
	 *            the side to enable. One of <CODE>LEFT, RIGHT, TOP, BOTTOM
	 *            </CODE>
	 */
	public void enableBorderSide(int side) {
		if (border == UNDEFINED) {
			border = 0;
		}
		border |= side;
	}

	/**
	 * Disables the border on the specified side.
	 * 
	 * @param side
	 *            the side to disable. One of <CODE>LEFT, RIGHT, TOP, BOTTOM
	 *            </CODE>
	 */
	public void disableBorderSide(int side) {
		if (border == UNDEFINED) {
			border = 0;
		}
		border &= ~side;
	}

	/**
	 * Sets the borderwidth of the table.
	 * 
	 * @param value
	 *            the new value
	 */

	public void setBorderWidth(float value) {
		borderWidth = value;
	}

	/**
	 * Sets the color of the border.
	 * 
	 * @param value
	 *            the new value
	 */

	public void setBorderColor(Color value) {
		color = value;
	}

	/**
	 * Sets the value of the border color
	 * 
	 * @param value
	 *            a color value
	 */
	public void setBorderColorRight(Color value) {
		borderColorRight = value;
	}

	/**
	 * Sets the value of the border color
	 * 
	 * @param value
	 *            a color value
	 */
	public void setBorderColorLeft(Color value) {
		borderColorLeft = value;
	}

	/**
	 * Sets the value of the border color
	 * 
	 * @param value
	 *            a color value
	 */
	public void setBorderColorTop(Color value) {
		borderColorTop = value;
	}

	/**
	 * Sets the value of the border color
	 * 
	 * @param value
	 *            a color value
	 */
	public void setBorderColorBottom(Color value) {
		borderColorBottom = value;
	}

	/**
	 * Sets the backgroundcolor of the rectangle.
	 * 
	 * @param value
	 *            the new value
	 */

	public void setBackgroundColor(Color value) {
		background = value;
	}

	/**
	 * Sets the grayscale of the rectangle.
	 * 
	 * @param value
	 *            the new value
	 */

	public void setGrayFill(float value) {
		if (value >= 0 && value <= 1.0) {
			grayFill = value;
		}
	}

	// methods to get the membervariables

	/**
	 * Returns the lower left x-coordinate.
	 * 
	 * @return the lower left x-coordinate
	 */

	public float left() {
		return llx;
	}

	/**
	 * Returns the upper right x-coordinate.
	 * 
	 * @return the upper right x-coordinate
	 */

	public float right() {
		return urx;
	}

	/**
	 * Returns the upper right y-coordinate.
	 * 
	 * @return the upper right y-coordinate
	 */

	public float top() {
		return ury;
	}

	/**
	 * Returns the lower left y-coordinate.
	 * 
	 * @return the lower left y-coordinate
	 */

	public float bottom() {
		return lly;
	}

	/**
	 * Returns the lower left x-coordinate, considering a given margin.
	 * 
	 * @param margin
	 *            a margin
	 * @return the lower left x-coordinate
	 */

	public float left(float margin) {
		return llx + margin;
	}

	/**
	 * Returns the upper right x-coordinate, considering a given margin.
	 * 
	 * @param margin
	 *            a margin
	 * @return the upper right x-coordinate
	 */

	public float right(float margin) {
		return urx - margin;
	}

	/**
	 * Returns the upper right y-coordinate, considering a given margin.
	 * 
	 * @param margin
	 *            a margin
	 * @return the upper right y-coordinate
	 */

	public float top(float margin) {
		return ury - margin;
	}

	/**
	 * Returns the lower left y-coordinate, considering a given margin.
	 * 
	 * @param margin
	 *            a margin
	 * @return the lower left y-coordinate
	 */

	public float bottom(float margin) {
		return lly + margin;
	}

	/**
	 * Returns the width of the rectangle.
	 * 
	 * @return a width
	 */

	public float width() {
		return urx - llx;
	}

	/**
	 * Returns the height of the rectangle.
	 * 
	 * @return a height
	 */

	public float height() {
		return ury - lly;
	}

	/**
	 * Indicates if the table has borders.
	 * 
	 * @return a boolean
	 */

	public boolean hasBorders() {
		return (border > 0)
				&& ((borderWidth > 0) || (borderWidthLeft > 0)
						|| (borderWidthRight > 0) || (borderWidthTop > 0) || (borderWidthBottom > 0));
	}

	/**
	 * Indicates if the table has a some type of border.
	 * 
	 * @param type
	 *            the type of border
	 * @return a boolean
	 */

	public boolean hasBorder(int type) {
		return border != UNDEFINED && (border & type) == type;
	}

	/**
	 * Returns the exact type of the border.
	 * 
	 * @return a value
	 */

	public int border() {
		return border;
	}

	/**
	 * Gets the borderwidth.
	 * 
	 * @return a value
	 */

	public float borderWidth() {
		return borderWidth;
	}

	/**
	 * Gets the color of the border.
	 * 
	 * @return a value
	 */

	public Color borderColor() {
		return color;
	}

	/**
	 * Gets the backgroundcolor.
	 * 
	 * @return a value
	 */

	public Color backgroundColor() {
		return background;
	}

	/**
	 * Gets the grayscale.
	 * 
	 * @return a value
	 */

	public float grayFill() {
		return grayFill;
	}

	/**
	 * Gets the rotation of the rectangle
	 * 
	 * @return a rotation value
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * @see com.lowagie.text.MarkupAttributes#setMarkupAttribute(java.lang.String,
	 *      java.lang.String)
	 */
	public void setMarkupAttribute(String name, String value) {
		if (markupAttributes == null)
			markupAttributes = new Properties();
		markupAttributes.put(name, value);
	}

	/**
	 * @see com.lowagie.text.MarkupAttributes#setMarkupAttributes(java.util.Properties)
	 */
	public void setMarkupAttributes(Properties markupAttributes) {
		this.markupAttributes = markupAttributes;
	}

	/**
	 * @see com.lowagie.text.MarkupAttributes#getMarkupAttribute(java.lang.String)
	 */
	public String getMarkupAttribute(String name) {
		return (markupAttributes == null) ? null : String
				.valueOf(markupAttributes.get(name));
	}

	/**
	 * @see com.lowagie.text.MarkupAttributes#getMarkupAttributeNames()
	 */
	public Set getMarkupAttributeNames() {
		return Chunk.getKeySet(markupAttributes);
	}

	/**
	 * @see com.lowagie.text.MarkupAttributes#getMarkupAttributes()
	 */
	public Properties getMarkupAttributes() {
		return markupAttributes;
	}

	/**
	 * Gets the color of a border.
	 * 
	 * @return a color value
	 */
	public Color getBorderColorLeft() {
		return borderColorLeft;
	}

	/**
	 * Gets the color of a border.
	 * 
	 * @return a color value
	 */
	public Color getBorderColorRight() {
		return borderColorRight;
	}

	/**
	 * Gets the color of a border.
	 * 
	 * @return a color value
	 */
	public Color getBorderColorTop() {
		return borderColorTop;
	}

	/**
	 * Gets the color of a border.
	 * 
	 * @return a color value
	 */
	public Color getBorderColorBottom() {
		return borderColorBottom;
	}

	/**
	 * Gets the width of a border.
	 * 
	 * @return a width
	 */
	public float getBorderWidthLeft() {
		return getVariableBorderWidth(borderWidthLeft, LEFT);
	}

	/**
	 * Sets the width of a border
	 * 
	 * @param borderWidthLeft
	 *            a width
	 */
	public void setBorderWidthLeft(float borderWidthLeft) {
		this.borderWidthLeft = borderWidthLeft;
		updateBorderBasedOnWidth(borderWidthLeft, LEFT);
	}

	/**
	 * Gets the width of a border.
	 * 
	 * @return a width
	 */
	public float getBorderWidthRight() {
		return getVariableBorderWidth(borderWidthRight, RIGHT);
	}

	/**
	 * Sets the width of a border
	 * 
	 * @param borderWidthRight
	 *            a width
	 */
	public void setBorderWidthRight(float borderWidthRight) {
		this.borderWidthRight = borderWidthRight;
		updateBorderBasedOnWidth(borderWidthRight, RIGHT);
	}

	/**
	 * Gets the width of a border.
	 * 
	 * @return a width
	 */
	public float getBorderWidthTop() {
		return getVariableBorderWidth(borderWidthTop, TOP);
	}

	/**
	 * Sets the width of a border
	 * 
	 * @param borderWidthTop
	 *            a width
	 */
	public void setBorderWidthTop(float borderWidthTop) {
		this.borderWidthTop = borderWidthTop;
		updateBorderBasedOnWidth(borderWidthTop, TOP);
	}

	/**
	 * Gets the width of a border.
	 * 
	 * @return a width
	 */
	public float getBorderWidthBottom() {
		return getVariableBorderWidth(borderWidthBottom, BOTTOM);
	}

	/**
	 * Sets the width of a border
	 * 
	 * @param borderWidthBottom
	 *            a width
	 */
	public void setBorderWidthBottom(float borderWidthBottom) {
		this.borderWidthBottom = borderWidthBottom;
		updateBorderBasedOnWidth(borderWidthBottom, BOTTOM);
	}

	/**
	 * Updates the border flag for a side based on the specified width. A width
	 * of 0 will disable the border on that side. Any other width enables it.
	 * 
	 * @param width
	 *            width of border
	 * @param side
	 *            border side constant
	 */

	private void updateBorderBasedOnWidth(float width, int side) {
		useVariableBorders = true;
		if (width > 0) {
			enableBorderSide(side);
		} else {
			disableBorderSide(side);
		}
	}

	private float getVariableBorderWidth(float variableWidthValue, int side) {
		if ((border & side) != 0) {
			return variableWidthValue != UNDEFINED ? variableWidthValue
					: borderWidth;
		} else {
			return 0;
		}
	}

	/**
	 * Indicates whether variable width borders are being used. Returns true if
	 * <CODE>setBorderWidthLeft, setBorderWidthRight, setBorderWidthTop, or
	 * setBorderWidthBottom</CODE> has been called.
	 * 
	 * @return true if variable width borders are in use
	 *  
	 */
	public boolean isUseVariableBorders() {
		return useVariableBorders;
	}

	/**
	 * Sets a parameter indicating if the rectangle has variable borders
	 * 
	 * @param useVariableBorders
	 *            indication if the rectangle has variable borders
	 */
	public void setUseVariableBorders(boolean useVariableBorders) {
		this.useVariableBorders = useVariableBorders;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer("Rectangle: ");
		buf.append(width());
		buf.append("x");
		buf.append(height());
		buf.append(" rotated ");
		buf.append(rotation);
		buf.append(" degrees");
		return buf.toString();
	}

}