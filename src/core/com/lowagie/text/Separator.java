/*
 * $Name$
 * $Id: PdfDocument.java 3320 2008-05-03 12:22:02Z blowagie $
 *
 * Copyright 2008 by Bruno Lowagie.
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

import java.util.ArrayList;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Draws a horizontal line at the current position
 * unless you set the width percentage to 0.
 * You can also use this object to draw other shapes
 * using PdfContentByte methods. By default he Y position
 * of the origin of the coordinate system corresponds
 * with the baseline of the next line.
 *  
 * @since	2.1.2
 */
public class Separator extends PdfContentByte implements Element {

	/**
	 * Allows you to define the length of the line as a percentage
	 * of the available page width.
	 */
	protected float widthPercentage = 100;
	/**
	 * The height necessary to draw the separator (similar to a leading
	 * in other objects). The default value is the value of the leading.
	 */
	protected float advanceY = Float.NaN;
	/**
	 * The minimum vertical space needed to draw the separator (if
	 * there is less space, a newPage() will be triggered). Most of the
	 * time minimumY will be equal to advanceY minus the offset.
	 * Reason: you don't need the full height to draw a line at the end
	 * of a page.
	 */
	protected float minimumY = Float.NaN;
	/**
	 * The offset used when drawing the line. By default half of advanceY.
	 */
	protected float verticalOffset = Float.NaN;
	/**
	 * The alignment of the line.
	 */
	protected int horizontalAlignment = Element.ALIGN_CENTER;
	
	/**
	 * Creates a separator object.
	 * You can use this object as is to draw a line.
	 * You can use super class methods to change the way
	 * the line is rendered (change its color, dash pattern, thickness,...).
	 * Or you can decide not to draw any line, but to use this object
	 * to draw other stuff.
	 */
	public Separator(PdfWriter writer) {
		super(writer);
	}

	/**
	 * Gets the length of the line as a percentage of the available
	 * page width. If 0 no line is drawn.
	 * @return the widthPercentage
	 */
	public float getWidthPercentage() {
		return widthPercentage;
	}

	/**
	 * Sets the length of the line as a percentage of the available
	 * page width. Set to 0 if you don't want to draw a line.
	 * @param widthPercentage the widthPercentage to set
	 */
	public void setWidthPercentage(float widthPercentage) {
		this.widthPercentage = widthPercentage;
	}

	/**
	 * Returns the amount of vertical space that will be
	 * consumed when drawing the separator. 
	 * @return a leading
	 */
	public float getAdvanceY() {
		return advanceY;
	}

	/**
	 * Sets the amount of vertical space that will be consumed
	 * when drawing the separator.
	 * @param advanceY	a value for the height of the separator.
	 */
	public void setAdvanceY(float advanceY) {
		this.advanceY = advanceY;
	}

	/**
	 * Returns the minimum amount of vertical space
	 * that needs to be available before drawing the separator. 
	 * @return	the minimum space that needs to be available before drawing the separator.
	 */
	public float getMinimumY() {
		return minimumY;
	}

	/**
	 * Sets the minimum amount of vertical space that
	 * needs to be available before drawing the separator.
	 * @param minimumY	a minimum height that needs to be available before drawing this object.
	 */
	public void setMinimumY(float minimumY) {
		this.minimumY = minimumY;
	}

	/**
	 * Returns the vertical offset of the line.
	 * @return the verticalOffset
	 */
	public float getVerticalOffset() {
		return verticalOffset;
	}

	/**
	 * Sets the vertical offset of the line.
	 * @param verticalOffset the verticalOffset to set
	 */
	public void setVerticalOffset(float verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	/**
	 * Sets the horizontal alignment of the line.
	 * @return the horizontalAlignment
	 */
	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	/**
	 * Gets the horizontal alignment of the line.
	 * @param horizontalAlignment the horizontalAlignment to set
	 */
	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	/**
	 * @see com.lowagie.text.Element#getChunks()
	 */
	public ArrayList getChunks() {
		return new ArrayList();
	}

	/**
	 * @see com.lowagie.text.Element#isContent()
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.lowagie.text.Element#isNestable()
	 */
	public boolean isNestable() {
		return false;
	}

	/**
	 * @see com.lowagie.text.Element#process(ElementListener)
	 */
	public boolean process(ElementListener listener) {
		try {
			return listener.add(this);
		} catch (DocumentException e) {
			return false;
		}
	}

	/**
	 * @see com.lowagie.text.Element#type()
	 */
	public int type() {
		return Element.LINE;
	}
}