/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text;

import java.awt.Color;
import java.util.ArrayList;

/**
 * A <CODE>Rectangle</CODE> is the representation of a geometric figure.
 *
 * @see		Element
 * @see		Table
 * @see		Cell
 * @see		HeaderFooter
 *
 * @author  bruno@lowagie.com
 */

public class Rectangle implements Element {
    
    // static membervariables (concerning the presence of borders)
    
/** This is the value that will be used as <VAR>undefined</VAR>. */
    public final static int UNDEFINED = -1;
    
/** This represents one side of the border of the <CODE>Rectangle</CODE>. */
    public final static int TOP = 1;
    
/** This represents one side of the border of the <CODE>Rectangle</CODE>. */
    public final static int BOTTOM = 2;
    
/** This represents one side of the border of the <CODE>Rectangle</CODE>. */
    public final static int LEFT = 4;
    
/** This represents one side of the border of the <CODE>Rectangle</CODE>. */
    public final static int RIGHT = 8;
    
/** This represents a rectangle without borders. */
    public final static int NO_BORDER = 0;
    
/** This represents a type of border. */
    public final static int BOX = TOP + BOTTOM + LEFT + RIGHT;
    
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
    
/** This is the color of the border of this rectangle. */
    protected Color color = null;
    
/** This is the color of the background of this rectangle. */
    protected Color background = null;
    
/** This is the grayscale value of the background of this rectangle. */
    protected float grayFill = 0;
    
    // constructors
    
/**
 * Constructs a <CODE>Rectangle</CODE>-object.
 *
 * @param		llx			lower left x
 * @param		lly			lower left y
 * @param		urx			upper right x
 * @param		ury			upper right y
 */
    
    public Rectangle(float llx, float lly, float urx, float ury) {
        this.llx = llx;
        this.lly = lly;
        this.urx = urx;
        this.ury = ury;
    }
    
/**
 * Constructs a <CODE>Rectangle</CODE>-object starting from the origin (0, 0).
 *
 * @param		urx			upper right x
 * @param		ury			upper right y
 */
    
    public Rectangle(float urx, float ury) {
        this(0, 0, urx, ury);
    }
    
/**
 * Constructs a <CODE>Rectangle</CODE>-object.
 *
 * @para		rect	another <CODE>Rectangle</CODE>
 */
    
    public Rectangle(Rectangle rect) {
        this(rect.left(), rect.bottom(), rect.right(), rect.top());
    }
    
    // implementation of the Element interface
    
/**
 * Processes the element by adding it (or the different parts) to an
 * <CODE>ElementListener</CODE>.
 *
 * @param	listener	an <CODE>ElementListener</CODE>
 * @return	<CODE>true</CODE> if the element was processed successfully
 */
    
    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        }
        catch(DocumentException de) {
            return false;
        }
    }
    
/**
 * Gets the type of the text element.
 *
 * @return	a type
 */
    
    public int type() {
        return Element.RECTANGLE;
    }
    
/**
 * Gets all the chunks in this element.
 *
 * @return	an <CODE>ArrayList</CODE>
 */
    
    public ArrayList getChunks() {
        return new ArrayList();
    }
    
    // methods
    
/**
 * Gets a Rectangle that is altered to fit on the page.
 *
 * @param	top		the top position
 * @param	bottom	the bottom position
 * @return	a <CODE>Rectangle</CODE>
 */
    
    public Rectangle rectangle(float top, float bottom) {
        Rectangle tmp = new Rectangle(this);
        tmp.setBorder(border);
        tmp.setBorderWidth(borderWidth);
        tmp.setBorderColor(color);
        tmp.setBackgroundColor(background);
        tmp.setGrayFill(grayFill);
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
 * Swaps the values of urx and ury and of lly and llx in order to rotate the rectangle.
 *
 * @return		a <CODE>Rectangle</CODE>
 */
    
    public Rectangle rotate() {
        return new Rectangle(lly, llx, ury, urx);
    }
    
    // methods to set the membervariables
    
/**
 * Sets the lower left x-coordinate.
 *
 * @param	value	the new value
 */
    
    public void setLeft(float value) {
        llx = value;
    }
    
/**
 * Sets the upper right x-coordinate.
 *
 * @param	value	the new value
 */
    
    public void setRight(float value) {
        urx = value;
    }
    
/**
 * Sets the upper right y-coordinate.
 *
 * @param	value	the new value
 */
    
    public void setTop(float value) {
        ury = value;
    }
    
/**
 * Sets the lower left y-coordinate.
 *
 * @param	value	the new value
 */
    
    public void setBottom(float value) {
        lly = value;
    }
    
/**
 * Sets the border.
 *
 * @param	value	the new value
 */
    
    public void setBorder(int value) {
        border = value;
    }
    
/**
 * Sets the borderwidth of the table.
 *
 * @param	value	the new value
 */
    
    public void setBorderWidth(float value) {
        borderWidth = value;
    }
    
/**
 * Sets the color of the border.
 *
 * @param	value	the new value
 */
    
    public void setBorderColor(Color value) {
        color = value;
    }
    
/**
 * Sets the backgroundcolor of the rectangle.
 *
 * @param	value	the new value
 */
    
    public void setBackgroundColor(Color value) {
        background = value;
    }
    
/**
 * Sets the grayscale of the rectangle.
 *
 * @param	value	the new value
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
    
    public float left(float margin) {
        return llx + margin;
    }
    
/**
 * Returns the upper right x-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the upper right x-coordinate
 */
    
    public float right(float margin) {
        return urx - margin;
    }
    
/**
 * Returns the upper right y-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the upper right y-coordinate
 */
    
    public float top(float margin) {
        return ury - margin;
    }
    
/**
 * Returns the lower left y-coordinate, considering a given margin.
 *
 * @param		margin		a margin
 * @return		the lower left y-coordinate
 */
    
    public float bottom(float margin) {
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
 * Indicates if the table has borders.
 *
 * @return	a boolean
 */
    
    public final boolean hasBorders() {
        return border > 0 && borderWidth > 0;
    }
    
/**
 * Indicates if the table has a some type of border.
 *
 * @param	the type of border
 * @return	a boolean
 */
    
    public final boolean hasBorder(int type) {
        return border != UNDEFINED && (border & type) == type;
    }
    
/**
 * Returns the exact type of the border.
 *
 * @return	a value
 */
    
    public final int border() {
        return border;
    }
    
/**
 * Gets the borderwidth.
 *
 * @return	a value
 */
    
    public float borderWidth() {
        return borderWidth;
    }
    
/**
 * Gets the color of the border.
 *
 * @return	a value
 */
    
    public final Color borderColor() {
        return color;
    }
    
/**
 * Gets the backgroundcolor.
 *
 * @return	a value
 */
    
    public final Color backgroundColor() {
        return background;
    }
    
/**
 * Gets the grayscale.
 *
 * @return	a value
 */
    
    public final float grayFill() {
        return grayFill;
    }
    
/**
 * Returns the XML representation of this <CODE>Rectangle</CODE>.
 *
 * @return		a <CODE>String</CODE>
 */
    
    public String toXml(int indent) {
        return toString();
    }
    
/**
 * Returns a representation of this <CODE>Rectangle</CODE>.
 *
 * @return	a <CODE>String</CODE>
 */
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (borderWidth != UNDEFINED) {
            buf.append(" ").append(ElementTags.BORDERWIDTH).append("=\"");
            buf.append(borderWidth);
            buf.append("\"");
            if (hasBorder(LEFT)) {
                buf.append(" ").append(ElementTags.LEFT).append("=\"").append(true).append("\"");
            }
            if (hasBorder(RIGHT)) {
                buf.append(" ").append(ElementTags.RIGHT).append("=\"").append(true).append("\"");
            }
            if (hasBorder(TOP)) {
                buf.append(" ").append(ElementTags.TOP).append("=\"").append(true).append("\"");
            }
            if (hasBorder(BOTTOM)) {
                buf.append(" ").append(ElementTags.BOTTOM).append("=\"").append(true).append("\"");
            }
        }
        if (color != null) {
            buf.append(" ").append(ElementTags.RED).append("=\"");
            buf.append(color.getRed());
            buf.append("\" ").append(ElementTags.GREEN).append("=\"");
            buf.append(color.getGreen());
            buf.append("\" ").append(ElementTags.BLUE).append("=\"");
            buf.append(color.getBlue());
            buf.append("\"");
        }
        if (background != null) {
            buf.append(" ").append(ElementTags.BGRED).append("=\"");
            buf.append(background.getRed());
            buf.append("\" ").append(ElementTags.BGGREEN).append("=\"");
            buf.append(background.getGreen());
            buf.append("\" ").append(ElementTags.BGBLUE).append("=\"");
            buf.append(background.getBlue());
            buf.append("\"");
        }
        if (grayFill > 0) {
            buf.append(" ").append(ElementTags.GRAYFILL).append("=\"");
            buf.append(grayFill);
            buf.append("\"");
        }
        return buf.toString();
    }
}
