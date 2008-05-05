/*
 * $Name$
 * $Id: PdfDocument.java 3333 2008-05-05 09:16:50Z blowagie $
 *
 * Copyright 2008 by Paulo Soares.
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

import com.lowagie.text.pdf.PdfContentByte;
import java.awt.Color;

/**
 * Element that draws a line.
 * @author	Paulo Soares
 * @since	2.1.2
 */
public class LineSeparator extends VerticalPositionMark {
    /** The thickness of the line. */
    private float lineWidth = 1;
    /** The width of the line as a percentage of the available page width. */
    private float percentage = 70;
    /** The color of the line. */
    private Color color;
    /** The alignment of the line. */
    private int alignment = Element.ALIGN_CENTER;
    /** Another implementation of the DrawInterface; its draw method will overrule LineSeparator.draw(). */
    private DrawInterface drawInterface;
    /** The offset for the line. */
    private float offset = 0;
    /** The actual advance caused by this separator. */
    private float advanceY = 0;
    /** The minimum advance necessary for this separator. */
    private float minimumY = Float.NaN;
    
    /** Creates a new instance of the LineSeparator class.
     * @param lineWidth		the thickness of the line
     * @param percentage	the width of the line as a percentage of the available page width
     * @param color			the color of the line
     * @param align			the alignment
     * @param offset		the offset of the line relative to the current baseline (negative = under the baseline)
     */
    public LineSeparator(float lineWidth, float percentage, Color color, int align, float offset) {
        this.lineWidth = lineWidth;
        this.percentage = percentage;
        this.color = color;
        this.alignment = align;
        this.offset = offset;
    }

    /**
     * Creates a new instance of the LineSeparator class.
     * @param drawInterface	an implementation of the DrawInterface that will overrule the LineSeparator's draw() method.
     * @param offset	an offset that will be passed to the draw method (added to the y value).
     */
    public LineSeparator(DrawInterface drawInterface, float offset) {
        this.drawInterface = drawInterface;
        this.offset = offset;
    }

    /**
     * Creates a new instance of the LineSeparator class with
     * default values: lineWidth 1 user unit, width 70%, centered with offset 0.
     */
    public LineSeparator() {
    }

    /**
     * @see com.lowagie.text.DrawInterface#draw(com.lowagie.text.pdf.PdfContentByte, float, float, float, float, float, float)
     */
    public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
        if (drawInterface != null) {
            drawInterface.draw(canvas, llx, lly, urx, ury, y + offset);
            return;
        }
        float w;
        if (getPercentage() < 0)
            w = -getPercentage();
        else
            w = (urx - llx) * getPercentage() / 100.0f;
        float s;
        switch (getAlignment()) {
            case Element.ALIGN_LEFT:
                s = 0;
                break;
            case Element.ALIGN_RIGHT:
                s = urx - llx - w;
                break;
            default:
                s = (urx - llx - w) / 2;
                break;
        }
        canvas.saveState();
        canvas.setLineWidth(getLineWidth());
        if (getColor() != null)
            canvas.setColorStroke(getColor());
        canvas.moveTo(s + llx, y + offset);
        canvas.lineTo(s + w + llx, y + offset);
        canvas.stroke();
        canvas.restoreState();
    }    

    /**
     * Getter for the interface with the overruling draw() method.
     * @return	a DrawInterface implementation
     */
    public DrawInterface getDrawInterface() {
        return drawInterface;
    }

    /**
     * Setter for the interface with the overruling draw() method.
     * @param drawInterface a DrawInterface implementation
     */
    public void setDrawInterface(DrawInterface drawInterface) {
        this.drawInterface = drawInterface;
    }

    /**
     * Getter for the line width.
     * @return	the thickness of the line that will be drawn.
     */
    public float getLineWidth() {
        return lineWidth;
    }

    /**
     * Setter for the line width.
     * @param lineWidth	the thickness of the line that will be drawn.
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Setter for the width as a percentage of the available width.
     * @return	a width percentage
     */
    public float getPercentage() {
        return percentage;
    }

    /**
     * Setter for the width as a percentage of the available width.
     * @param percentage	a width percentage
     */
    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    /**
     * Getter for the color of the line that will be drawn.
     * @return	a color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter for the color of the line that will be drawn.
     * @param color	a color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Getter for the alignment of the line.
     * @return	an alignment value
     */
    public int getAlignment() {
        return alignment;
    }

    /**
     * Setter for the alignment of the line.
     * @param align	an alignment value
     */
    public void setAlignment(int align) {
        this.alignment = align;
    }

    /**
     * Getter for the offset relative to the baseline of the current line.
     * @return	an offset
     */
    public float getOffset() {
        return offset;
    }

    /**
     * Setter for the offset. The offset is relative to the current
     * Y position. If you want to underline something, you have to
     * choose a negative offset.
     * @param offset	an offset
     */
    public void setOffset(float offset) {
        this.offset = offset;
    }

	/**
	 * Getter for the actual amount of vertical space taken by this separator.
	 * @return the vertical advance
	 */
	public float getAdvanceY() {
		return advanceY;
	}

	/**
	 * Setter for the actual amount of vertical space taken by this separator.
	 * @param advanceY the vertical advance
	 */
	public void setAdvanceY(float advanceY) {
		this.advanceY = advanceY;
	}

	/**
	 * Getter for the minimum vertical space needed to realize advanceY
	 * (necessary to evaluate if a newPage() is necessary).
	 * @return the minimum vertical advance
	 */
	public float getMinimumY() {
		if (Float.isNaN(minimumY)) {
			return getAdvanceY();
		}
		return minimumY;
	}

	/**
	 * Setter for the minimum vertical space needed to realize advanceY
	 * (necessary to evaluate if a newPage() is necessary).
	 * @param minimumY the minimum vertical advance
	 */
	public void setMinimumY(float minimumY) {
		this.minimumY = minimumY;
	}

    /**
     * @see com.lowagie.text.Element#type()
     */
    public int type() {
    	if (getAdvanceY() == 0) {
    		return YMARK;
    	}
        return Element.LINE;
    }
}