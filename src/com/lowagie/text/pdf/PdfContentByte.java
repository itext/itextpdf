/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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

package com.lowagie.text.pdf;
import java.awt.Color;
import com.lowagie.text.Image;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import java.io.UnsupportedEncodingException;
import com.lowagie.text.Image;
import java.util.ArrayList;

/**
 * <CODE>PdfContentByte</CODE> is an object containing the user positioned
 * text and graphic contents of a page. It knows how to apply the proper
 * font encoding.
 */

public class PdfContentByte {
    
    /**
     * This class keeps the graphic state of the current page
     */
    
    class GraphicState {
        
        /** This is the font in use */
        FontDetails fontDetails;
        
        /** This is the color in use */
        ColorDetails colorDetails;
        
        /** This is the font size in use */
        float size;
        
        /** The x position of the text line matrix. */
        protected float xTLM = 0;
        /** The y position of the text line matrix. */
        protected float yTLM = 0;
        
        /** The current text leading. */
        protected float leading = 0;
    }
    
    /** The alignement is center */
    public static final int ALIGN_CENTER = 0;
    
    /** The alignement is left */
    public static final int ALIGN_LEFT = 1;
    
    /** The alignement is right */
    public static final int ALIGN_RIGHT = 2;
    
    // membervariables
    
    /** This is the actual content */
    protected ByteBuffer content = new ByteBuffer();
    
    /** This is the writer */
    protected PdfWriter writer;
    
    /** This is the PdfDocument */
    protected PdfDocument pdf;
    
    /** This is the GraphicState in use */
    protected GraphicState state = new GraphicState();
    
    /** The list were we save/restore the state */
    protected ArrayList stateList = new ArrayList();
    
    /** The separator between commands.
     */    
    protected int separator = '\n';
    
    // constructors
    
    /**
     * Constructs a new <CODE>PdfContentByte</CODE>-object.
     *
     * @param wr the writer associated to this content
     */
    
    public PdfContentByte(PdfWriter wr) {
        if (wr != null) {
            writer = wr;
            pdf = writer.getPdfDocument();
        }
    }
    
    // methods to get the content of this object
    
    /**
     * Returns the <CODE>String</CODE> representation of this <CODE>PdfContentByte</CODE>-object.
     *
     * @return		a <CODE>String</CODE>
     */
    
    public String toString() {
        return content.toString();
    }
    
    /**
     * Gets the internal buffer.
     * @return the internal buffer
     */
    ByteBuffer getInternalBuffer() {
        return content;
    }
    
    /** Returns the PDF representation of this <CODE>PdfContentByte</CODE>-object.
     *
     * @param writer the <CODE>PdfWriter</CODE>
     * @return a <CODE>byte</CODE> array with the representation
     */
    
    public byte[] toPdf(PdfWriter writer) {
        return content.toByteArray();
    }
    
    // methods to add graphical content
    
    /**
     * Adds the content of another <CODE>PdfContent</CODE>-object to this object.
     *
     * @param		other		another <CODE>PdfByteContent</CODE>-object
     */
    
    public final void add(PdfContentByte other) {
        if (other.writer != null && writer != other.writer)
            throw new RuntimeException("Inconsistent writers. Are you mixing two documents?");
        content.append(other.content);
    }
    
    /**
     * Gets the x position of the text line matrix.
     *
     * @return the x position of the text line matrix
     */
    public float getXTLM() {
        return state.xTLM;
    }
    
    /**
     * Gets the y position of the text line matrix.
     *
     * @return the y position of the text line matrix
     */
    public float getYTLM() {
        return state.yTLM;
    }
    
    /**
     * Gets the current text leading.
     *
     * @return the current text leading
     */
    public float getLeading() {
        return state.leading;
    }
    
    /**
     * Changes the <VAR>Flatness</VAR>.
     * <P>
     * <VAR>Flatness</VAR> sets the maximum permitted distance in device pixels between the
     * mathematically correct path and an approximation constructed from straight line segments.<BR>
     *
     * @param		flatness		a value
     */
    
    public final void setFlatness(float flatness) {
        if (flatness >= 0 && flatness <= 100) {
            content.append(flatness).append(" i").append_i(separator);
        }
    }
    
    /**
     * Changes the <VAR>Line cap style</VAR>.
     * <P>
     * The <VAR>line cap style</VAR> specifies the shape to be used at the end of open subpaths
     * when they are stroked.<BR>
     * Allowed values are 0 (Butt end caps), 1 (Round end caps) and 2 (Projecting square end caps).<BR>
     *
     * @param		style		a value
     */
    
    public final void setLineCap(int style) {
        if (style >= 0 && style <= 2) {
            content.append(style).append(" J").append_i(separator);
        }
    }
    
    /**
     * Changes the value of the <VAR>line dash pattern</VAR>.
     * <P>
     * The line dash pattern controls the pattern of dashes and gaps used to stroke paths.
     * It is specified by an <I>array</I> and a <I>phase</I>. The array specifies the length
     * of the alternating dashes and gaps. The phase specifies the distance into the dash
     * pattern to start the dash.<BR>
     *
     * @param		phase		the value of the phase
     */
    
    public final void setLineDash(float phase) {
        content.append("[] ").append(phase).append(" d").append_i(separator);
    }
    
    /**
     * Changes the value of the <VAR>line dash pattern</VAR>.
     * <P>
     * The line dash pattern controls the pattern of dashes and gaps used to stroke paths.
     * It is specified by an <I>array</I> and a <I>phase</I>. The array specifies the length
     * of the alternating dashes and gaps. The phase specifies the distance into the dash
     * pattern to start the dash.<BR>
     *
     * @param		phase		the value of the phase
     * @param		unitsOn		the number of units that must be 'on' (equals the number of units that must be 'off').
     */
    
    public final void setLineDash(float unitsOn, float phase) {
        content.append("[").append(unitsOn).append("] ").append(phase).append(" d").append_i(separator);
    }
    
    /**
     * Changes the value of the <VAR>line dash pattern</VAR>.
     * <P>
     * The line dash pattern controls the pattern of dashes and gaps used to stroke paths.
     * It is specified by an <I>array</I> and a <I>phase</I>. The array specifies the length
     * of the alternating dashes and gaps. The phase specifies the distance into the dash
     * pattern to start the dash.<BR>
     *
     * @param		phase		the value of the phase
     * @param		unitsOn		the number of units that must be 'on'
     * @param		unitsOff	the number of units that must be 'off'
     */
    
    public final void setLineDash(float unitsOn, float unitsOff, float phase) {
        content.append("[").append(unitsOn).append(' ').append(unitsOff).append("] ").append(phase).append(" d").append_i(separator);
    }
    
    /**
     * Changes the <VAR>Line join style</VAR>.
     * <P>
     * The <VAR>line join style</VAR> specifies the shape to be used at the corners of paths
     * that are stroked.<BR>
     * Allowed values are 0 (Miter joins), 1 (Round joins) and 2 (Bevel joins).<BR>
     *
     * @param		style		a value
     */
    
    public final void setLineJoin(int style) {
        if (style >= 0 && style <= 2) {
            content.append(style).append(" j").append_i(separator);
        }
    }
    
    /**
     * Changes the <VAR>line width</VAR>.
     * <P>
     * The line width specifies the thickness of the line used to stroke a path and is measured
     * in used space units.<BR>
     *
     * @param		w			a width
     */
    
    public final void setLineWidth(float w) {
        content.append(w).append(" w").append_i(separator);
    }
    
    /**
     * Changes the <VAR>Miter limit</VAR>.
     * <P>
     * When two line segments meet at a sharp angle and mitered joins have been specified as the
     * line join style, it is possible for the miter to extend far beyond the thickness of the line
     * stroking path. The miter limit imposes a maximum on the ratio of the miter length to the line
     * witdh. When the limit is exceeded, the join is converted from a miter to a bevel.<BR>
     *
     * @param		miterLimit		a miter limit
     */
    
    public final void setMiterLimit(float miterLimit) {
        if (miterLimit > 1) {
            content.append(miterLimit).append(" M").append_i(separator);
        }
    }
    
    /**
     * Modify the current clipping path by intersecting it with the current path, using the
     * nonzero winding number rule to determine which regions lie inside the clipping
     * path.
     */
    
    public final void clip() {
        content.append("W").append_i(separator);
    }
    
    /**
     * Modify the current clipping path by intersecting it with the current path, using the
     * even-odd rule to determine which regions lie inside the clipping path.
     */
    
    public final void eoClip() {
        content.append("W*").append_i(separator);
    }
    
    /**
     * Changes the currentgray tint for filling paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceGray</B> (or the <B>DefaultGray</B> color space),
     * and sets the gray tint to use for filling paths.</P>
     *
     * @param	gray	a value between 0 (black) and 1 (white)
     */
    
    public void setGrayFill(float gray) {
        content.append(gray).append(" g").append_i(separator);
    }
    
    /**
     * Changes the current gray tint for filling paths to black.
     */
    
    public void resetGrayFill() {
        content.append("0 g").append_i(separator);
    }
    
    /**
     * Changes the currentgray tint for stroking paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceGray</B> (or the <B>DefaultGray</B> color space),
     * and sets the gray tint to use for stroking paths.</P>
     *
     * @param	gray	a value between 0 (black) and 1 (white)
     */
    
    public void setGrayStroke(float gray) {
        content.append(gray).append(" G").append_i(separator);
    }
    
    /**
     * Changes the current gray tint for stroking paths to black.
     */
    
    public void resetGrayStroke() {
        content.append("0 G").append_i(separator);
    }
    
    /**
     * Helper to validate and write the RGB color components
     * @param	red		the intensity of red. A value between 0 and 1
     * @param	green	the intensity of green. A value between 0 and 1
     * @param	blue	the intensity of blue. A value between 0 and 1
     */
    private void HelperRGB(float red, float green, float blue) {
        if (red < 0)
            red = 0.0f;
        else if (red > 1.0f)
            red = 1.0f;
        if (green < 0)
            green = 0.0f;
        else if (green > 1.0f)
            green = 1.0f;
        if (blue < 0)
            blue = 0.0f;
        else if (blue > 1.0f)
            blue = 1.0f;
        content.append(red).append(' ').append(green).append(' ').append(blue);
    }
    
    /**
     * Changes the current color for filling paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceRGB</B> (or the <B>DefaultRGB</B> color space),
     * and sets the color to use for filling paths.</P>
     * <P>
     * Following the PDF manual, each operand must be a number between 0 (minimum intensity) and
     * 1 (maximum intensity).</P>
     *
     * @param	red		the intensity of red. A value between 0 and 1
     * @param	green	the intensity of green. A value between 0 and 1
     * @param	blue	the intensity of blue. A value between 0 and 1
     */
    
    public void setRGBColorFillF(float red, float green, float blue) {
        HelperRGB(red, green, blue);
        content.append(" rg").append_i(separator);
    }
    
    /**
     * Changes the current color for filling paths to black.
     */
    
    public void resetRGBColorFill() {
        content.append("0 0 0 rg").append_i(separator);
    }
    
    /**
     * Changes the current color for stroking paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceRGB</B> (or the <B>DefaultRGB</B> color space),
     * and sets the color to use for stroking paths.</P>
     * <P>
     * Following the PDF manual, each operand must be a number between 0 (miniumum intensity) and
     * 1 (maximum intensity).
     *
     * @param	red		the intensity of red. A value between 0 and 1
     * @param	green	the intensity of green. A value between 0 and 1
     * @param	blue	the intensity of blue. A value between 0 and 1
     */
    
    public void setRGBColorStrokeF(float red, float green, float blue) {
        HelperRGB(red, green, blue);
        content.append(" RG").append_i(separator);
    }
    
    /**
     * Changes the current color for stroking paths to black.
     *
     */
    
    public void resetRGBColorStroke() {
        content.append("0 0 0 RG").append_i(separator);
    }
    
    /**
     * Helper to validate and write the CMYK color components.
     *
     * @param	cyan	the intensity of cyan. A value between 0 and 1
     * @param	magenta	the intensity of magenta. A value between 0 and 1
     * @param	yellow	the intensity of yellow. A value between 0 and 1
     * @param	black	the intensity of black. A value between 0 and 1
     */
    private void HelperCMYK(float cyan, float magenta, float yellow, float black) {
        if (cyan < 0)
            cyan = 0.0f;
        else if (cyan > 1.0f)
            cyan = 1.0f;
        if (magenta < 0)
            magenta = 0.0f;
        else if (magenta > 1.0f)
            magenta = 1.0f;
        if (yellow < 0)
            yellow = 0.0f;
        else if (yellow > 1.0f)
            yellow = 1.0f;
        if (black < 0)
            black = 0.0f;
        else if (black > 1.0f)
            black = 1.0f;
        content.append(cyan).append(' ').append(magenta).append(' ').append(yellow).append(' ').append(black);
    }
    
    /**
     * Changes the current color for filling paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceCMYK</B> (or the <B>DefaultCMYK</B> color space),
     * and sets the color to use for filling paths.</P>
     * <P>
     * Following the PDF manual, each operand must be a number between 0 (no ink) and
     * 1 (maximum ink).</P>
     *
     * @param	cyan	the intensity of cyan. A value between 0 and 1
     * @param	magenta	the intensity of magenta. A value between 0 and 1
     * @param	yellow	the intensity of yellow. A value between 0 and 1
     * @param	black	the intensity of black. A value between 0 and 1
     */
    
    public void setCMYKColorFillF(float cyan, float magenta, float yellow, float black) {
        HelperCMYK(cyan, magenta, yellow, black);
        content.append(" k").append_i(separator);
    }
    
    /**
     * Changes the current color for filling paths to black.
     *
     */
    
    public void resetCMYKColorFill() {
        content.append("0 0 0 1 k").append_i(separator);
    }
    
    /**
     * Changes the current color for stroking paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceCMYK</B> (or the <B>DefaultCMYK</B> color space),
     * and sets the color to use for stroking paths.</P>
     * <P>
     * Following the PDF manual, each operand must be a number between 0 (miniumum intensity) and
     * 1 (maximum intensity).
     *
     * @param	cyan	the intensity of cyan. A value between 0 and 1
     * @param	magenta	the intensity of magenta. A value between 0 and 1
     * @param	yellow	the intensity of yellow. A value between 0 and 1
     * @param	black	the intensity of black. A value between 0 and 1
     */
    
    public void setCMYKColorStrokeF(float cyan, float magenta, float yellow, float black) {
        HelperCMYK(cyan, magenta, yellow, black);
        content.append(" K").append_i(separator);
    }
    
    /**
     * Changes the current color for stroking paths to black.
     *
     */
    
    public void resetCMYKColorStroke() {
        content.append("0 0 0 1 K").append_i(separator);
    }
    
    /**
     * Move the current point <I>(x, y)</I>, omitting any connecting line segment.
     *
     * @param		x				new x-coordinate
     * @param		y				new y-coordinate
     */
    
    public final void moveTo(float x, float y) {
        content.append(x).append(' ').append(y).append(" m").append_i(separator);
    }
    
    /**
     * Appends a straight line segment from the current point <I>(x, y)</I>. The new current
     * point is <I>(x, y)</I>.
     *
     * @param		x				new x-coordinate
     * @param		y				new y-coordinate
     */
    
    public final void lineTo(float x, float y) {
        content.append(x).append(' ').append(y).append(" l").append_i(separator);
    }
    
    /**
     * Appends a Bêzier curve to the path, starting from the current point.
     *
     * @param		x1		x-coordinate of the first control point
     * @param		y1		y-coordinate of the first control point
     * @param		x2		x-coordinate of the second control point
     * @param		y2		y-coordinate of the second control point
     * @param		x3		x-coordinaat of the ending point (= new current point)
     * @param		y3		y-coordinaat of the ending point (= new current point)
     */
    
    public final void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        content.append(x1).append(' ').append(y1).append(' ').append(x2).append(' ').append(y2).append(' ').append(x3).append(' ').append(y3).append(" c").append_i(separator);
    }
    
    /**
     * Appends a Bêzier curve to the path, starting from the current point.
     *
     * @param		x2		x-coordinate of the second control point
     * @param		y2		y-coordinate of the second control point
     * @param		x3		x-coordinaat of the ending point (= new current point)
     * @param		y3		y-coordinaat of the ending point (= new current point)
     */
    
    public final void curveTo(float x2, float y2, float x3, float y3) {
        content.append(x2).append(' ').append(y2).append(' ').append(x3).append(' ').append(y3).append(" v").append_i(separator);
    }
    
    /**
     * Appends a Bêzier curve to the path, starting from the current point.
     *
     * @param		x1		x-coordinate of the first control point
     * @param		y1		y-coordinate of the first control point
     * @param		x3		x-coordinaat of the ending point (= new current point)
     * @param		y3		y-coordinaat of the ending point (= new current point)
     */
    
    public final void curveFromTo(float x1, float y1, float x3, float y3) {
        content.append(x1).append(' ').append(y1).append(' ').append(x3).append(' ').append(y3).append(" y").append_i(separator);
    }
    
    /** Draws a circle. The endpoint will (x+r, y).
     *
     * @param x x center of circle
     * @param y y center of circle
     * @param r radius of circle
     */
    public final void circle(float x, float y, float r) {
        float b = 0.5523f;
        moveTo(x + r, y);
        curveTo(x + r, y + r * b, x + r * b, y + r, x, y + r);
        curveTo(x - r * b, y + r, x - r, y + r * b, x - r, y);
        curveTo(x - r, y - r * b, x - r * b, y - r, x, y - r);
        curveTo(x + r * b, y - r, x + r, y - r * b, x + r, y);
    }
    
    
    
    /**
     * Adds a rectangle to the current path.
     *
     * @param		x		x-coordinate of the starting point
     * @param		y		y-coordinate of the starting point
     * @param		w		width
     * @param		h		height
     */
    
    public final void rectangle(float x, float y, float w, float h) {
        content.append(x).append(' ').append(y).append(' ').append(w).append(' ').append(h).append(" re").append_i(separator);
    }
    
    /**
     * Adds a border (complete or partially) to the current path..
     *
     * @param		rectangle		a <CODE>Rectangle</CODE>
     */
    
    public final void rectangle(Rectangle rectangle) {
        
        // the coordinates of the border are retrieved
        float x1 = rectangle.left();
        float y1 = rectangle.top();
        float x2 = rectangle.right();
        float y2 = rectangle.bottom();
        
        // the backgroundcolor is set
        Color background = rectangle.backgroundColor();
        if (background != null) {
            setColorStroke(background);
            setColorFill(background);
            rectangle(x1, y1, x2 - x1, y2 - y1);
            closePathFillStroke();
            resetRGBColorFill();
            resetRGBColorStroke();
        }
        else if (rectangle.grayFill() > 0.0) {
            setGrayStroke((float)rectangle.grayFill());
            setGrayFill((float)rectangle.grayFill());
            rectangle(x1, y1, x2 - x1, y2 - y1);
            closePathFillStroke();
            resetGrayFill();
            resetGrayStroke();
        }
        
        
        // if the element hasn't got any borders, nothing is added
        if (! rectangle.hasBorders()) {
            return;
        }
        
        // the width is set to the width of the element
        if (rectangle.borderWidth() != Rectangle.UNDEFINED) {
            setLineWidth((float)rectangle.borderWidth());
        }
        
        // the color is set to the color of the element
        Color color = rectangle.borderColor();
        if (color != null) {
            setColorStroke(color);
        }
        
        // if the box is a rectangle, it is added as a rectangle
        if (rectangle.hasBorder(Rectangle.BOX)) {
            rectangle(x1, y1, x2 - x1, y2 - y1);
        }
        // if the border isn't a rectangle, the different sides are added apart
        else {
            if (rectangle.hasBorder(Rectangle.RIGHT)) {
                moveTo(x2, y1);
                lineTo(x2, y2);
            }
            if (rectangle.hasBorder(Rectangle.LEFT)) {
                moveTo(x1, y1);
                lineTo(x1, y2);
            }
            if (rectangle.hasBorder(Rectangle.BOTTOM)) {
                moveTo(x1, y2);
                lineTo(x2, y2);
            }
            if (rectangle.hasBorder(Rectangle.TOP)) {
                moveTo(x1, y1);
                lineTo(x2, y1);
            }
        }
        
        stroke();
        
        if (color != null) {
            resetRGBColorStroke();
        }
    }
    
    /**
     * Closes the current subpath by appending a straight line segment from the current point
     * to the starting point of the subpath.
     */
    
    public final void closePath() {
        content.append("h").append_i(separator);
    }
    
    /**
     * Ends the path without filling or stroking it.
     */
    
    public final void newPath() {
        content.append("n").append_i(separator);
    }
    
    /**
     * Strokes the path.
     */
    
    public final void stroke() {
        content.append("S").append_i(separator);
    }
    
    /**
     * Closes the path and strokes it.
     */
    
    public final void closePathStroke() {
        content.append("s").append_i(separator);
    }
    
    /**
     * Fills the path, using the non-zero winding number rule to determine the region to fill.
     */
    
    public final void fill() {
        content.append("f").append_i(separator);
    }
    
    /**
     * Fills the path, using the even-odd rule to determine the region to fill.
     */
    
    public final void eoFill() {
        content.append("f*").append_i(separator);
    }
    
    /**
     * Fills the path using the non-zero winding number rule to determine the region to fill and strokes it.
     */
    
    public final void fillStroke() {
        content.append("B").append_i(separator);
    }
    
    /**
     * Closes the path, fills it using the non-zero winding number rule to determine the region to fill and strokes it.
     */
    
    public final void closePathFillStroke() {
        content.append("b").append_i(separator);
    }
    
    /**
     * Fills the path, using the even-odd rule to determine the region to fill and strokes it.
     */
    
    public final void eoFillStroke() {
        content.append("B*").append_i(separator);
    }
    
    /**
     * Closes the path, fills it using the even-odd rule to determine the region to fill and strokes it.
     */
    
    public final void closePathEoFillStroke() {
        content.append("b*").append_i(separator);
    }
    
    /**
     * Adds an <CODE>Image</CODE> to the page. The <CODE>Image</CODE> must have
     * absolute positioning.
     * @param image the <CODE>Image</CODE> object
     * @throws DocumentException if the <CODE>Image</CODE> does not have absolute positioning
     */
    final public void addImage(Image image) throws DocumentException {
        if (!image.hasAbsolutePosition())
            throw new DocumentException("The image must have absolute positioning.");
        float matrix[] = image.matrix();
        matrix[Image.CX] = image.absoluteX() - matrix[Image.CX];
        matrix[Image.CY] = image.absoluteY() - matrix[Image.CY];
        addImage(image, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
    }
    
    /**
     * Adds an <CODE>Image</CODE> to the page. The positioning of the <CODE>Image</CODE>
     * is done with the transformation matrix. To position an <CODE>image</CODE> at (x,y)
     * use addImage(image, image_width, 0, 0, image_height, x, y).
     * @param image the <CODE>Image</CODE> object
     * @param a an element of the transformation matrix
     * @param b an element of the transformation matrix
     * @param c an element of the transformation matrix
     * @param d an element of the transformation matrix
     * @param e an element of the transformation matrix
     * @param f an element of the transformation matrix
     * @throws DocumentException on error
     */
    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        checkWriter();
        try {
            if (image.isImgTemplate()) {
                pdf.addDirectImage(image);
                PdfTemplate template = image.templateData();
                float w = template.getWidth();
                float h = template.getHeight();
                addTemplate(template, a / w, b / w, c / h, d / h, e, f);
            }
            else {
                PdfName name = pdf.addDirectImage(image);
                content.append("q ");
                content.append(a).append(' ');
                content.append(b).append(' ');
                content.append(c).append(' ');
                content.append(d).append(' ');
                content.append(e).append(' ');
                content.append(f).append(" cm ");
                content.append(name.toString()).append(" Do Q").append_i(separator);
            }
        }
        catch (Exception ee) {
            throw new DocumentException(ee.getMessage());
        }
    }
    
    /**
     * Makes this <CODE>PdfContentByte</CODE> empty.
     */
    public void reset() {
        content.reset();
        stateList.clear();
        state = new GraphicState();
    }
    
    /**
     * Starts the writing of text.
     */
    public void beginText() {
        state.xTLM = 0;
        state.yTLM = 0;
        content.append("BT").append_i(separator);
    }
    
    /**
     * Ends the writing of text and makes the current font invalid.
     */
    public void endText() {
        content.append("ET").append_i(separator);
    }
    
    /**
     * Saves the graphic state. <CODE>saveState</CODE> and
     * <CODE>restoreState</CODE> must be balanced.
     */
    public void saveState() {
        content.append("q").append_i(separator);
        stateList.add(state);
    }
    
    /**
     * Restores the graphic state. <CODE>saveState</CODE> and
     * <CODE>restoreState</CODE> must be balanced.
     */
    public void restoreState() {
        content.append("Q").append_i(separator);
        int idx = stateList.size() - 1;
        if (idx < 0)
            throw new RuntimeException("Unbalanced save/restore state operators.");
        state = (GraphicState)stateList.get(idx);
        stateList.remove(idx);
    }
    
    /**
     * Sets the character spacing parameter.
     *
     * @param		charSpace			a parameter
     */
    public void setCharacterSpacing(float charSpace) {
        content.append(charSpace).append(" Tc").append_i(separator);
    }
    
    /**
     * Sets the word spacing parameter.
     *
     * @param		wordSpace			a parameter
     */
    public void setWordSpacing(float wordSpace) {
        content.append(wordSpace).append(" Tw").append_i(separator);
    }
    
    /**
     * Sets the horizontal scaling parameter.
     *
     * @param		scale				a parameter
     */
    public void setHorizontalScaling(float scale) {
        content.append(scale).append(" Tz").append_i(separator);
    }
    
    /**
     * Sets the text leading parameter.
     * <P>
     * The leading parameter is measured in text space units. It specifies the vertical distance
     * between the baselines of adjacent lines of text.</P>
     *
     * @param		leading			the new leading
     */
    public void setLeading(float leading) {
        state.leading = leading;
        content.append(leading).append(" TL").append_i(separator);
    }
    
    /**
     * Set the font and the size for the subsequent text writing.
     *
     * @param bf the font
     * @param size the font size in points
     */
    public void setFontAndSize(BaseFont bf, float size) {
        checkWriter();
        state.size = size;
        state.fontDetails = writer.add(bf);
        content.append(state.fontDetails.getFontName().toPdf(null)).append(' ').append(size).append(" Tf").append_i(separator);
    }
    
    /**
     * Sets the text rendering parameter.
     *
     * @param		rendering				a parameter
     */
    public void setTextRenderingMode(int rendering) {
        content.append(rendering).append(" Tr").append_i(separator);
    }
    
    /**
     * Sets the text rise parameter.
     * <P>
     * This allows to write text in subscript or superscript mode.</P>
     *
     * @param		rise				a parameter
     */
    public void setTextRise(float rise) {
        content.append(rise).append(" Ts").append_i(separator);
    }
    
    /**
     * A helper to insert into the content stream the <CODE>text</CODE>
     * converted to bytes according to the font's encoding.
     *
     * @param text the text to write
     */
    private void showText2(String text) {
        if (state.fontDetails == null)
            throw new NullPointerException("Font and size must be set before writing any text");
        byte b[] = state.fontDetails.convertToBytes(text);
        escapeString(b, content);
    }
    
    /**
     * Shows the <CODE>text</CODE>.
     *
     * @param text the text to write
     */
    public void showText(String text) {
        showText2(text);
        content.append("Tj").append_i(separator);
    }
    
    /**
     * Shows the <CODE>text</CODE>.
     *
     * @param text the text to write
     */
    public void showText(PdfPrintable text) {
        showText2(text.toString());
        content.append("Tj").append_i(separator);
    }
    
    /**
     * Moves to the next line and shows <CODE>text</CODE>.
     *
     * @param text the text to write
     */
    public void newlineShowText(String text) {
        state.yTLM -= state.leading;
        showText2(text);
        content.append("'").append_i(separator);
    }
    
    /**
     * Moves to the next line and shows <CODE>text</CODE>.
     *
     * @param text the text to write
     */
    public void newlineShowText(PdfPrintable text) {
        state.yTLM -= state.leading;
        showText2(text.toString());
        content.append("'").append_i(separator);
    }
    
    /**
     * Moves to the next line and shows text string, using the given values of the character and word spacing parameters.
     *
     * @param		wordSpacing		a parameter
     * @param		charSpacing		a parameter
     * @param text the text to write
     */
    public void newlineShowText(float wordSpacing, float charSpacing, String text) {
        state.yTLM -= state.leading;
        content.append(wordSpacing).append(' ').append(charSpacing);
        showText2(text);
        content.append("\"").append_i(separator);
    }
    
    /**
     * Changes the text matrix.
     * <P>
     * Remark: this operation also initializes the current point position.</P>
     *
     * @param		a			operand 1,1 in the matrix
     * @param		b			operand 1,2 in the matrix
     * @param		c			operand 2,1 in the matrix
     * @param		d			operand 2,2 in the matrix
     * @param		x			operand 3,1 in the matrix
     * @param		y			operand 3,2 in the matrix
     */
    public void setTextMatrix(float a, float b, float c, float d, float x, float y) {
        state.xTLM = x;
        state.yTLM = y;
        content.append(a).append(' ').append(b).append_i(' ')
        .append(c).append_i(' ').append(d).append_i(' ')
        .append(x).append_i(' ').append(y).append(" Tm").append_i(separator);
    }
    
    /**
     * Changes the text matrix. The first four parameters are {1,0,0,1}.
     * <P>
     * Remark: this operation also initializes the current point position.</P>
     *
     * @param		x			operand 3,1 in the matrix
     * @param		y			operand 3,2 in the matrix
     */
    public void setTextMatrix(float x, float y) {
        setTextMatrix(1, 0, 0, 1, x, y);
    }
    
    /**
     * Moves to the start of the next line, offset from the start of the current line.
     *
     * @param		x			x-coordinate of the new current point
     * @param		y			y-coordinate of the new current point
     */
    public void moveText(float x, float y) {
        state.xTLM += x;
        state.yTLM += y;
        content.append(x).append(' ').append(y).append(" Td").append_i(separator);
    }
    
    /**
     * Moves to the start of the next line, offset from the start of the current line.
     * <P>
     * As a side effect, this sets the leading parameter in the text state.</P>
     *
     * @param		x			offset of the new current point
     * @param		y			y-coordinate of the new current point
     */
    public void moveTextWithLeading(float x, float y) {
        state.xTLM += x;
        state.yTLM += y;
        state.leading = -y;
        content.append(x).append(' ').append(y).append(" TD").append_i(separator);
    }
    
    /**
     * Moves to the start of the next line.
     */
    public void newlineText() {
        state.yTLM -= state.leading;
        content.append("T*").append_i(separator);
    }
    
    /**
     * Gets the size of this content.
     *
     * @return the size of the content
     */
    int size() {
        return content.size();
    }
    
    /**
     * Escapes a <CODE>byte</CODE> array according to the PDF conventions.
     *
     * @param b the <CODE>byte</CODE> array to escape
     * @return an escaped <CODE>byte</CODE> array
     */
    static byte[] escapeString(byte b[]) {
        ByteBuffer content = new ByteBuffer();
        escapeString(b, content);
        return content.toByteArray();
    }
    
    /**
     * Escapes a <CODE>byte</CODE> array according to the PDF conventions.
     *
     * @param b the <CODE>byte</CODE> array to escape
     * @return an escaped <CODE>byte</CODE> array
     */
    static void escapeString(byte b[], ByteBuffer content) {
        content.append_i('(');
        for (int k = 0; k < b.length; ++k) {
            byte c = b[k];
            switch ((int)c) {
                case '\r':
                    content.append("\\r");
                    break;
                case '\n':
                    content.append("\n");
                    break;
                case '(':
                case ')':
                case '\\':
                    content.append_i('\\').append_i(c);
                    break;
                default:
                    content.append_i(c);
            }
        }
        content.append(")");
    }
    
    /**
     * Adds an outline to the document.
     *
     * @param outline the outline
     */
    public void addOutline(PdfOutline outline) {
        checkWriter();
        pdf.addOutline(outline);
    }
    /**
     * Adds a named outline to the document.
     *
     * @param outline the outline
     * @param name the name for the local destination
     */
    public void addOutline(PdfOutline outline, String name) {
        checkWriter();
        pdf.addOutline(outline, name);
    }
    /**
     * Gets the root outline.
     *
     * @return the root outline
     */
    public PdfOutline getRootOutline() {
        checkWriter();
        return pdf.getRootOutline();
    }
    
    /**
     * Shows text right, left or center aligned with rotation.
     * @param alignement the alignment can be ALIGN_CENTER, ALIGN_RIGHT or ALIGN_LEFT
     * @param text the text to show
     * @param x the x pivot position
     * @param y the y pivot position
     * @param rotation the rotation to be applied in degrees counterclockwise
     */
    public void showTextAligned(int alignement, String text, float x, float y, float rotation) {
        if (state.fontDetails == null)
            throw new NullPointerException("Font and size must be set before writing any text");
        BaseFont bf = state.fontDetails.getBaseFont();
        if (rotation == 0) {
            switch (alignement) {
                case ALIGN_CENTER:
                    x -= bf.getWidthPoint(text, state.size) / 2;
                    break;
                case ALIGN_RIGHT:
                    x -= bf.getWidthPoint(text, state.size);
                    break;
            }
            setTextMatrix(x, y);
            showText(text);
        }
        else {
            double alpha = rotation * Math.PI / 180.0;
            float cos = (float)Math.cos(alpha);
            float sin = (float)Math.sin(alpha);
            float len;
            switch (alignement) {
                case ALIGN_CENTER:
                    len = bf.getWidthPoint(text, state.size) / 2;
                    x -=  len * cos;
                    y -=  len * sin;
                    break;
                case ALIGN_RIGHT:
                    len = bf.getWidthPoint(text, state.size);
                    x -=  len * cos;
                    y -=  len * sin;
                    break;
            }
            setTextMatrix(cos, sin, -sin, cos, x, y);
            showText(text);
            setTextMatrix(0f, 0f);
        }
    }
    /**
     * Concatenate a matrix to the current transformation matrix.
     * @param a an element of the transformation matrix
     * @param b an element of the transformation matrix
     * @param c an element of the transformation matrix
     * @param d an element of the transformation matrix
     * @param e an element of the transformation matrix
     * @param f an element of the transformation matrix
     **/
    public void concatCTM(float a, float b, float c, float d, float e, float f) {
        content.append(a).append(' ').append(b).append(' ').append(c).append(' ');
        content.append(d).append(' ').append(e).append(' ').append(f).append(" cm").append_i(separator);
    }
    
    /**
     * Generates an array of bezier curves to draw an arc.
     * <P>
     * (x1, y1) and (x2, y2) are the corners of the enclosing rectangle.
     * Angles, measured in degrees, start with 0 to the right (the positive X
     * axis) and increase counter-clockwise.  The arc extends from startAng
     * to startAng+extent.  I.e. startAng=0 and extent=180 yields an openside-down
     * semi-circle.
     * <P>
     * The resulting coordinates are of the form float[]{x1,y1,x2,y2,x3,y3, x4,y4}
     * such that the curve goes from (x1, y1) to (x4, y4) with (x2, y2) and
     * (x3, y3) as their respective Bezier control points.
     * <P>
     * Note: this code was taken from ReportLab (www.reportlab.com), an excelent
     * PDF generator for Python.
     *
     * @param x1 a corner of the enclosing rectangle
     * @param y1 a corner of the enclosing rectangle
     * @param x2 a corner of the enclosing rectangle
     * @param y2 a corner of the enclosing rectangle
     * @param startAng starting angle in degrees
     * @param extent angle extent in degrees
     * @return a list of float[] with the bezier curves
     */
    public static ArrayList bezierArc(float x1, float y1, float x2, float y2, float startAng, float extent) {
        float tmp;
        if (x1 > x2) {
            tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (y2 > y1) {
            tmp = y1;
            y1 = y2;
            y2 = tmp;
        }
        
        float fragAngle;
        int Nfrag;
        if (Math.abs(extent) <= 90f) {
            fragAngle = extent;
            Nfrag = 1;
        }
        else {
            Nfrag = (int)(Math.ceil(Math.abs(extent)/90f));
            fragAngle = extent / Nfrag;
        }
        float x_cen = (x1+x2)/2f;
        float y_cen = (y1+y2)/2f;
        float rx = (x2-x1)/2f;
        float ry = (y2-y1)/2f;
        float halfAng = (float)(fragAngle * Math.PI / 360.);
        float kappa = (float)(Math.abs(4. / 3. * (1. - Math.cos(halfAng)) / Math.sin(halfAng)));
        ArrayList pointList = new ArrayList();
        for (int i = 0; i < Nfrag; ++i) {
            float theta0 = (float)((startAng + i*fragAngle) * Math.PI / 180.);
            float theta1 = (float)((startAng + (i+1)*fragAngle) * Math.PI / 180.);
            float cos0 = (float)Math.cos(theta0);
            float cos1 = (float)Math.cos(theta1);
            float sin0 = (float)Math.sin(theta0);
            float sin1 = (float)Math.sin(theta1);
            if (fragAngle > 0f) {
                pointList.add(new float[]{x_cen + rx * cos0,
                y_cen - ry * sin0,
                x_cen + rx * (cos0 - kappa * sin0),
                y_cen - ry * (sin0 + kappa * cos0),
                x_cen + rx * (cos1 + kappa * sin1),
                y_cen - ry * (sin1 - kappa * cos1),
                x_cen + rx * cos1,
                y_cen - ry * sin1});
            }
            else {
                pointList.add(new float[]{x_cen + rx * cos0,
                y_cen - ry * sin0,
                x_cen + rx * (cos0 + kappa * sin0),
                y_cen - ry * (sin0 - kappa * cos0),
                x_cen + rx * (cos1 - kappa * sin1),
                y_cen - ry * (sin1 + kappa * cos1),
                x_cen + rx * cos1,
                y_cen - ry * sin1});
            }
        }
        return pointList;
    }
    
    /**
     * Draws a partial ellipse inscribed within the rectangle x1,y1,x2,y2,
     * starting at startAng degrees and covering extent degrees. Angles
     * start with 0 to the right (+x) and increase counter-clockwise.
     *
     * @param x1 a corner of the enclosing rectangle
     * @param y1 a corner of the enclosing rectangle
     * @param x2 a corner of the enclosing rectangle
     * @param y2 a corner of the enclosing rectangle
     * @param startAng starting angle in degrees
     * @param extent angle extent in degrees
     */
    public void arc(float x1, float y1, float x2, float y2, float startAng, float extent) {
        ArrayList ar = bezierArc(x1, y1, x2, y2, startAng, extent);
        if (ar.size() == 0)
            return;
        float pt[] = (float [])ar.get(0);
        moveTo(pt[0], pt[1]);
        for (int k = 0; k < ar.size(); ++k) {
            pt = (float [])ar.get(k);
            curveTo(pt[2], pt[3], pt[4], pt[5], pt[6], pt[7]);
        }
    }
    
    /**
     * Draws an ellipse inscribed within the rectangle x1,y1,x2,y2.
     *
     * @param x1 a corner of the enclosing rectangle
     * @param y1 a corner of the enclosing rectangle
     * @param x2 a corner of the enclosing rectangle
     * @param y2 a corner of the enclosing rectangle
     */
    public void ellipse(float x1, float y1, float x2, float y2) {
        arc(x1, y1, x2, y2, 0f, 360f);
    }
    
    /**
     * Create a new colored tiling pattern.
     *
     * @param width the width of the pattern
     * @param height the height of the pattern
     * @param xstep the desired horizontal spacing between pattern cells.
     * May be either positive or negative, but not zero.
     * @param ystep the desired vertical spacing between pattern cells.
     * May be either positive or negative, but not zero.
     * @return the <CODE>PdfPatternPainter</CODE> where the pattern will be created
     */
    public PdfPatternPainter createPattern(float width, float height, float xstep, float ystep) {
        checkWriter();
        if ( xstep == 0.0f || ystep == 0.0f )
            throw new RuntimeException("XStep or YStep can not be ZERO.");
        PdfPatternPainter painter = new PdfPatternPainter(writer);
        painter.setWidth(width);
        painter.setHeight(height);
        painter.setXStep(xstep);
        painter.setYStep(ystep);
        writer.addSimplePattern(painter);
        return painter;
    }
    
    /**
     * Create a new colored tiling pattern. Variables xstep and ystep are set to the same values
     * of width and height.
     * @param width the width of the pattern
     * @param height the height of the pattern
     * @return the <CODE>PdfPatternPainter</CODE> where the pattern will be created
     */
    public PdfPatternPainter createPattern(float width, float height) {
        return createPattern(width, height, width, height);
    }
    
    /**
     * Create a new uncolored tiling pattern.
     *
     * @param width the width of the pattern
     * @param height the height of the pattern
     * @param xstep the desired horizontal spacing between pattern cells.
     * May be either positive or negative, but not zero.
     * @param ystep the desired vertical spacing between pattern cells.
     * May be either positive or negative, but not zero.
     * @param color the default color. Can be <CODE>null</CODE>
     * @return the <CODE>PdfPatternPainter</CODE> where the pattern will be created
     */
    public PdfPatternPainter createPattern(float width, float height, float xstep, float ystep, Color color) {
        checkWriter();
        if ( xstep == 0.0f || ystep == 0.0f )
            throw new RuntimeException("XStep or YStep can not be ZERO.");
        PdfPatternPainter painter = new PdfPatternPainter(writer, color);
        painter.setWidth(width);
        painter.setHeight(height);
        painter.setXStep(xstep);
        painter.setYStep(ystep);
        writer.addSimplePattern(painter);
        return painter;
    }
    
    /**
     * Create a new uncolored tiling pattern.
     * Variables xstep and ystep are set to the same values
     * of width and height.
     * @param width the width of the pattern
     * @param height the height of the pattern
     * @param color the default color. Can be <CODE>null</CODE>
     * @return the <CODE>PdfPatternPainter</CODE> where the pattern will be created
     */
    public PdfPatternPainter createPattern(float width, float height, Color color) {
        return createPattern(width, height, width, height, color);
    }
    
    /**
     * Creates a new template.
     * <P>
     * Creates a new template that is nothing more than a form XObject. This template can be included
     * in this <CODE>PdfContentByte</CODE> or in another template. Templates are only written
     * to the output when the document is closed permitting things like showing text in the first page
     * that is only defined in the last page.
     *
     * @param width the bounding box width
     * @param height the bounding box height
     * @return the templated created
     */
    public PdfTemplate createTemplate(float width, float height) {
        checkWriter();
        PdfTemplate template = new PdfTemplate(writer);
        template.setWidth(width);
        template.setHeight(height);
        writer.addDirectTemplateSimple(template);
        return template;
    }
    
    /**
     * Creates a new appearance to be used with form fields.
     *
     * @param width the bounding box width
     * @param height the bounding box height
     * @return the appearance created
     */
    public PdfAppearance createAppearance(float width, float height) {
        checkWriter();
        PdfAppearance template = new PdfAppearance(writer);
        template.setWidth(width);
        template.setHeight(height);
        writer.addDirectTemplateSimple(template);
        return template;
    }
    
    /**
     * Adds a template to this content.
     *
     * @param template the template
     * @param a an element of the transformation matrix
     * @param b an element of the transformation matrix
     * @param c an element of the transformation matrix
     * @param d an element of the transformation matrix
     * @param e an element of the transformation matrix
     * @param f an element of the transformation matrix
     */
    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
        checkWriter();
        checkNoPattern(template);
        PdfName name = pdf.addTemplateToPage(template);
        content.append("q ");
        content.append(a).append(' ');
        content.append(b).append(' ');
        content.append(c).append(' ');
        content.append(d).append(' ');
        content.append(e).append(' ');
        content.append(f).append(" cm ");
        content.append(name.toString()).append(" Do Q").append_i(separator);
    }
    
    /**
     * Adds a template to this content.
     *
     * @param template the template
     * @param x the x location of this template
     * @param y the y location of this template
     */
    public void addTemplate(PdfTemplate template, float x, float y) {
        addTemplate(template, 1, 0, 0, 1, x, y);
    }
    
    /**
     * Changes the current color for filling paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceCMYK</B> (or the <B>DefaultCMYK</B> color space),
     * and sets the color to use for filling paths.</P>
     * <P>
     * This method is described in the 'Portable Document Format Reference Manual version 1.3'
     * section 8.5.2.1 (page 331).</P>
     * <P>
     * Following the PDF manual, each operand must be a number between 0 (no ink) and
     * 1 (maximum ink). This method however accepts only integers between 0x00 and 0xFF.</P>
     *
     * @param cyan the intensity of cyan
     * @param magenta the intensity of magenta
     * @param yellow the intensity of yellow
     * @param black the intensity of black
     */
    
    public void setCMYKColorFill(int cyan, int magenta, int yellow, int black) {
        content.append((float)(cyan & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(magenta & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(yellow & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(black & 0xFF) / 0xFF);
        content.append(" k").append_i(separator);
    }
    /**
     * Changes the current color for stroking paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceCMYK</B> (or the <B>DefaultCMYK</B> color space),
     * and sets the color to use for stroking paths.</P>
     * <P>
     * This method is described in the 'Portable Document Format Reference Manual version 1.3'
     * section 8.5.2.1 (page 331).</P>
     * Following the PDF manual, each operand must be a number between 0 (miniumum intensity) and
     * 1 (maximum intensity). This method however accepts only integers between 0x00 and 0xFF.
     *
     * @param cyan the intensity of red
     * @param magenta the intensity of green
     * @param yellow the intensity of blue
     * @param black the intensity of black
     */
    
    public void setCMYKColorStroke(int cyan, int magenta, int yellow, int black) {
        content.append((float)(cyan & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(magenta & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(yellow & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(black & 0xFF) / 0xFF);
        content.append(" K").append_i(separator);
    }
    
    /**
     * Changes the current color for filling paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceRGB</B> (or the <B>DefaultRGB</B> color space),
     * and sets the color to use for filling paths.</P>
     * <P>
     * This method is described in the 'Portable Document Format Reference Manual version 1.3'
     * section 8.5.2.1 (page 331).</P>
     * <P>
     * Following the PDF manual, each operand must be a number between 0 (miniumum intensity) and
     * 1 (maximum intensity). This method however accepts only integers between 0x00 and 0xFF.</P>
     *
     * @param red the intensity of red
     * @param green the intensity of green
     * @param blue the intensity of blue
     */
    
    public void setRGBColorFill(int red, int green, int blue) {
        content.append((float)(red & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(green & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(blue & 0xFF) / 0xFF);
        content.append(" rg").append_i(separator);
    }
    
    /**
     * Changes the current color for stroking paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceRGB</B> (or the <B>DefaultRGB</B> color space),
     * and sets the color to use for stroking paths.</P>
     * <P>
     * This method is described in the 'Portable Document Format Reference Manual version 1.3'
     * section 8.5.2.1 (page 331).</P>
     * Following the PDF manual, each operand must be a number between 0 (miniumum intensity) and
     * 1 (maximum intensity). This method however accepts only integers between 0x00 and 0xFF.
     *
     * @param red the intensity of red
     * @param green the intensity of green
     * @param blue the intensity of blue
     */
    
    public void setRGBColorStroke(int red, int green, int blue) {
        content.append((float)(red & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(green & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(blue & 0xFF) / 0xFF);
        content.append(" RG").append_i(separator);
    }
    
    /** Sets the stroke color. <CODE>color</CODE> can be an
     * <CODE>ExtendedColor</CODE>.
     * @param color the color
     */    
    public void setColorStroke(Color color) {
        int type = ExtendedColor.getType(color);
        switch (type) {
            case ExtendedColor.TYPE_GRAY: {
                setGrayStroke(((GrayColor)color).getGray());
                break;
            }
            case ExtendedColor.TYPE_CMYK: {
                CMYKColor cmyk = (CMYKColor)color;
                setCMYKColorStrokeF(cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack());
                break;
            }
            case ExtendedColor.TYPE_SEPARATION: {
                SpotColor spot = (SpotColor)color;
                setColorStroke(spot.getPdfSpotColor(), spot.getTint());
                break;
            }
            case ExtendedColor.TYPE_PATTERN: {
                PatternColor pat = (PatternColor) color;
                setPatternStroke(pat.getPainter());
                break;
            }
            default:
                setRGBColorStroke(color.getRed(), color.getGreen(), color.getBlue());
        }
    }
    
    /** Sets the fill color. <CODE>color</CODE> can be an
     * <CODE>ExtendedColor</CODE>.
     * @param color the color
     */    
    public void setColorFill(Color color) {
        int type = ExtendedColor.getType(color);
        switch (type) {
            case ExtendedColor.TYPE_GRAY: {
                setGrayFill(((GrayColor)color).getGray());
                break;
            }
            case ExtendedColor.TYPE_CMYK: {
                CMYKColor cmyk = (CMYKColor)color;
                setCMYKColorFillF(cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack());
                break;
            }
            case ExtendedColor.TYPE_SEPARATION: {
                SpotColor spot = (SpotColor)color;
                setColorFill(spot.getPdfSpotColor(), spot.getTint());
                break;
            }
            case ExtendedColor.TYPE_PATTERN: {
                PatternColor pat = (PatternColor) color;
                setPatternFill(pat.getPainter());
                break;
            }
            default:
                setRGBColorFill(color.getRed(), color.getGreen(), color.getBlue());
        }
    }
    
    /** Sets the fill color to a spot color.
     * @param sp the spot color
     * @param tint the tint for the spot color. 0 is no color and 1
     * is 100% color
     */    
    public void setColorFill(PdfSpotColor sp, float tint) {
        checkWriter();
        state.colorDetails = writer.add(sp);
        content.append(state.colorDetails.getColorName().toPdf(null)).append(" cs ").append(tint).append(" scn").append_i(separator);
    }
    
    /** Sets the stroke color to a spot color.
     * @param sp the spot color
     * @param tint the tint for the spot color. 0 is no color and 1
     * is 100% color
     */    
    public void setColorStroke(PdfSpotColor sp, float tint) {
        checkWriter();
        state.colorDetails = writer.add(sp);
        content.append(state.colorDetails.getColorName().toPdf(null)).append(" CS ").append(tint).append(" SCN").append_i(separator);
    }
    
    /** Sets the fill color to a pattern. The pattern can be
     * colored or uncolored.
     * @param p the pattern
     */    
    public void setPatternFill(PdfPatternPainter p) {
        if (p.isStencil()) {
            setPatternFill(p, p.getDefaultColor());
            return;
        }
        checkWriter();
        PdfName name = pdf.addPatternToPage(p);
        content.append(PdfName.PATTERN.toPdf(null)).append(" cs ").append(name.toPdf(null)).append(" scn").append_i(separator);
    }
    
    /** Outputs the color values to the content.
     * @param color The color
     * @param tint the tint if it is a spot color, ignored otherwise
     */    
    void outputColorNumbers(Color color, float tint) {
        int type = ExtendedColor.getType(color);
        switch (type) {
            case ExtendedColor.TYPE_RGB:
                content.append((float)(color.getRed()) / 0xFF);
                content.append(' ');
                content.append((float)(color.getGreen()) / 0xFF);
                content.append(' ');
                content.append((float)(color.getBlue()) / 0xFF);
                break;
            case ExtendedColor.TYPE_GRAY:
                content.append(((GrayColor)color).getGray());
                break;
            case ExtendedColor.TYPE_CMYK: {
                CMYKColor cmyk = (CMYKColor)color;
                content.append(cmyk.getCyan()).append(' ').append(cmyk.getMagenta());
                content.append(' ').append(cmyk.getYellow()).append(' ').append(cmyk.getBlack());
                break;
            }
            case ExtendedColor.TYPE_SEPARATION:
                content.append(tint);
                break;
            default:
                throw new RuntimeException("Invalid color type.");                
        }
    }
    
    /** Sets the fill color to an uncolored pattern.
     * @param p the pattern
     * @param color the color of the pattern
     */    
    public void setPatternFill(PdfPatternPainter p, Color color) {
        if (ExtendedColor.getType(color) == ExtendedColor.TYPE_SEPARATION)
            setPatternFill(p, color, ((SpotColor)color).getTint());
        else
            setPatternFill(p, color, 0);
    }
    
    /** Sets the fill color to an uncolored pattern.
     * @param p the pattern
     * @param color the color of the pattern
     * @param tint the tint if the color is a spot color, ignored otherwise
     */    
    public void setPatternFill(PdfPatternPainter p, Color color, float tint) {
        checkWriter();
        if (!p.isStencil())
            throw new RuntimeException("An uncolored pattern was expected.");
        PdfName name = pdf.addPatternToPage(p);
        ColorDetails csDetail = writer.addSimplePatternColorspace(color);
        pdf.addColor(csDetail.getColorName(), csDetail.getIndirectReference());
        content.append(csDetail.getColorName().toPdf(null)).append(" cs").append_i(separator);
        outputColorNumbers(color, tint);
        content.append(' ').append(name.toPdf(null)).append(" scn").append_i(separator);
    }
    
    /** Sets the stroke color to an uncolored pattern.
     * @param p the pattern
     * @param color the color of the pattern
     */    
    public void setPatternStroke(PdfPatternPainter p, Color color) {
        if (ExtendedColor.getType(color) == ExtendedColor.TYPE_SEPARATION)
            setPatternStroke(p, color, ((SpotColor)color).getTint());
        else
            setPatternStroke(p, color, 0);
    }
    
    /** Sets the stroke color to an uncolored pattern.
     * @param p the pattern
     * @param color the color of the pattern
     * @param tint the tint if the color is a spot color, ignored otherwise
     */    
    public void setPatternStroke(PdfPatternPainter p, Color color, float tint) {
        checkWriter();
        if (!p.isStencil())
            throw new RuntimeException("An uncolored pattern was expected.");
        PdfName name = pdf.addPatternToPage(p);
        ColorDetails csDetail = writer.addSimplePatternColorspace(color);
        pdf.addColor(csDetail.getColorName(), csDetail.getIndirectReference());
        content.append(csDetail.getColorName().toPdf(null)).append(" CS").append_i(separator);
        outputColorNumbers(color, tint);
        content.append(' ').append(name.toPdf(null)).append(" SCN").append_i(separator);
    }
    
    /** Sets the stroke color to a pattern. The pattern can be
     * colored or uncolored.
     * @param p the pattern
     */    
    public void setPatternStroke(PdfPatternPainter p) {
        if (p.isStencil()) {
            setPatternStroke(p, p.getDefaultColor());
            return;
        }
        checkWriter();
        PdfName name = pdf.addPatternToPage(p);
        content.append(PdfName.PATTERN.toPdf(null)).append(" CS ").append(name.toPdf(null)).append(" SCN").append_i(separator);
    }
    
    /** Check if we have a valid PdfWriter.
     *
     */
    protected void checkWriter() {
        if (writer == null)
            throw new NullPointerException("The writer in PdfContentByte is null.");
    }
    
    /**
     * Show an array of text.
     * @param text array of text
     */
    void showText(PdfTextArray text) {
        if (state.fontDetails == null)
            throw new NullPointerException("Font and size must be set before writing any text");
        content.append("[");
        ArrayList arrayList = text.getArrayList();
        boolean lastWasNumber = false;
        for (int k = 0; k < arrayList.size(); ++k) {
            Object obj = arrayList.get(k);
            if (obj instanceof String) {
                showText2((String)obj);
                lastWasNumber = false;
            }
            else {
                if (lastWasNumber)
                    content.append(' ');
                else
                    lastWasNumber = true;
                content.append(((Float)obj).floatValue());
            }
        }
        content.append("]TJ").append_i(separator);
    }
    
    /**
     * Gets the <CODE>PdfWriter</CODE> in use by this object.
     * @return the <CODE>PdfWriter</CODE> in use by this object
     */
    PdfWriter getPdfWriter() {
        return writer;
    }
    
    /**
     * Gets the <CODE>PdfDocument</CODE> in use by this object.
     * @return the <CODE>PdfDocument</CODE> in use by this object
     */
    PdfDocument getPdfDocument() {
        return pdf;
    }
    
    /**
     * Implements a link to other part of the document. The jump will
     * be made to a local destination with the same name, that must exist.
     * @param name the name for this link
     * @param llx the lower left x corner of the activation area
     * @param lly the lower left y corner of the activation area
     * @param urx the upper right x corner of the activation area
     * @param ury the upper right y corner of the activation area
     */
    public void localGoto(String name, float llx, float lly, float urx, float ury) {
        pdf.localGoto(name, llx, lly, urx, ury);
    }
    
    /**
     * The local destination to where a local goto with the same
     * name will jump.
     * @param name the name of this local destination
     * @param destination the <CODE>PdfDestination</CODE> with the jump coordinates
     * @return <CODE>true</CODE> if the local destination was added,
     * <CODE>false</CODE> if a local destination with the same name
     * already exists
     */
    public boolean localDestination(String name, PdfDestination destination) {
        return pdf.localDestination(name, destination);
    }
    
    /**
     * Gets a duplicate of this <CODE>PdfContentByte</CODE>. All
     * the members are copied by reference but the buffer stays different.
     *
     * @return a copy of this <CODE>PdfContentByte</CODE>
     */
    public PdfContentByte getDuplicate() {
        return new PdfContentByte(writer);
    }
    
    /**
     * Implements a link to another document.
     * @param filename the filename for the remote document
     * @param name the name to jump to
     * @param llx the lower left x corner of the activation area
     * @param lly the lower left y corner of the activation area
     * @param urx the upper right x corner of the activation area
     * @param ury the upper right y corner of the activation area
     */
    public void remoteGoto(String filename, String name, float llx, float lly, float urx, float ury) {
        remoteGoto(filename, name, llx, lly, urx, ury);
    }
    
    /**
     * Implements a link to another document.
     * @param filename the filename for the remote document
     * @param page the page to jump to
     * @param llx the lower left x corner of the activation area
     * @param lly the lower left y corner of the activation area
     * @param urx the upper right x corner of the activation area
     * @param ury the upper right y corner of the activation area
     */
    public void remoteGoto(String filename, int page, float llx, float lly, float urx, float ury) {
        pdf.remoteGoto(filename, page, llx, lly, urx, ury);
    }
    /**
     * Adds a round rectangle to the current path.
     *
     * @param x x-coordinate of the starting point
     * @param y y-coordinate of the starting point
     * @param w width
     * @param h height
     * @param r radius of the arc corner
     */
    public void roundRectangle(float x, float y, float w, float h, float r) {
        float b = 0.4477f;
        moveTo(x + r, y);
        lineTo(x + w - r, y);
        curveTo(x + w - r * b, y, x + w, y + r * b, x + w, y + r);
        lineTo(x + w, y + h - r);
        curveTo(x + w, y + h - r * b, x + w - r * b, y + h, x + w - r, y + h);
        lineTo(x + r, y + h);
        curveTo(x + r * b, y + h, x, y + h - r * b, x, y + h - r);
        lineTo(x, y + r);
        curveTo(x, y + r * b, x + r * b, y, x + r, y);
    }
    
    /** Implements an action in an area.
     * @param action the <CODE>PdfAction</CODE>
     * @param llx the lower left x corner of the activation area
     * @param lly the lower left y corner of the activation area
     * @param urx the upper right x corner of the activation area
     * @param ury the upper right y corner of the activation area
     */
    public void setAction(PdfAction action, float llx, float lly, float urx, float ury) {
        pdf.setAction(action, llx, lly, urx, ury);
    }
    
    /** Outputs a <CODE>String</CODE> directly to the content.
     * @param s the <CODE>String</CODE>
     */    
    public void setLiteral(String s) {
        content.append(s);
    }
    
    /** Outputs a <CODE>char</CODE> directly to the content.
     * @param c the <CODE>char</CODE>
     */    
    public void setLiteral(char c) {
        content.append(c);
    }
    
    /** Outputs a <CODE>float</CODE> directly to the content.
     * @param n the <CODE>float</CODE>
     */    
    public void setLiteral(float n) {
        content.append(n);
    }
    
    /** Throws an error if it is a pattern.
     * @param t the object to check
     */    
    void checkNoPattern(PdfTemplate t) {
        if (t.getType() == PdfTemplate.TYPE_PATTERN)
            throw new RuntimeException("Invalid use of a pattern. A template was expected.");
    }
    
/**
 * Draws a TextField.
 */
    
    public void drawTextField(float llx, float lly, float urx, float ury, String text, BaseFont bf, float size) {
        if (llx > urx) { float x = llx; llx = urx; urx = x; }
        if (lly > ury) { float y = lly; lly = ury; ury = y; }
        // silver rectangle not filled
        setColorStroke(new Color(0xC0, 0xC0, 0xC0));
        setLineWidth(1);
        setLineCap(0);
        rectangle(llx - 0.5f, lly - 0.5f, urx - llx + 1f, ury - lly + 1f);
        stroke();
        // white rectangle filled
        setLineWidth(1);
        setLineCap(0);
        setColorFill(new Color(0xFF, 0xFF, 0xFF));
        rectangle(llx, lly, urx - llx, ury -lly);
        fill();
        // gray lines
        setColorStroke(new Color(0xA0, 0xA0, 0xA0));
        setLineWidth(1);
        setLineCap(0);
        moveTo(llx + 0.5f, lly + 1f);
        lineTo(llx + 0.5f, ury - 0.5f);
        lineTo(urx - 1f, ury - 0.5f);
        stroke();
        // silver lines
        setColorStroke(new Color(0xC0, 0xC0, 0xC0));
        setLineWidth(1);
        setLineCap(0);
        moveTo(llx + 1f, lly + 1.5f);
        lineTo(urx - 1.5f, lly + 1.5f);
        lineTo(urx - 1.5f, ury - 1f);
        stroke();
        // black lines
        setColorStroke(new Color(0x00, 0x00, 0x00));
        setLineWidth(1);
        setLineCap(0);
        moveTo(llx + 1.5f, lly + 2f);
        lineTo(llx + 1.5f, ury - 1.5f);
        lineTo(urx - 2f, ury - 1.5f);
        stroke();
        // text
        resetRGBColorFill();
        beginText();
        setFontAndSize(bf, size);
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(text, "\n");
        float yPos = ury;
        while (tokenizer.hasMoreTokens()) {
            yPos -= size * 1.5f;
            showTextAligned(PdfContentByte.ALIGN_LEFT, tokenizer.nextToken(), llx + 3, yPos, 0);
        }
        endText();
    }
    
/**
 * Draws a button.
 */
    
    public void drawButton(float llx, float lly, float urx, float ury, String text, BaseFont bf, float size) {
        if (llx > urx) { float x = llx; llx = urx; urx = x; }
        if (lly > ury) { float y = lly; lly = ury; ury = y; }
        // black rectangle not filled
        setColorStroke(new Color(0x00, 0x00, 0x00));
        setLineWidth(1);
        setLineCap(0);
        rectangle(llx - 0.5f, lly - 0.5f, urx - llx + 1f, ury - lly + 1f);
        stroke();
        // silver rectangle filled
        setLineWidth(1);
        setLineCap(0);
        setColorFill(new Color(0xC0, 0xC0, 0xC0));
        rectangle(llx, lly, urx - llx, ury -lly);
        fill();
        // white lines
        setColorStroke(new Color(0xFF, 0xFF, 0xFF));
        setLineWidth(1);
        setLineCap(0);
        moveTo(llx, lly + 0.5f);
        lineTo(llx, ury);
        lineTo(urx - 0.5f, ury);
        stroke();
        // dark grey lines
        setColorStroke(new Color(0xA0, 0xA0, 0xA0));
        setLineWidth(1);
        setLineCap(0);
        moveTo(llx + 0.5f, lly + 0.5f);
        lineTo(urx - 0.5f, lly + 0.5f);
        lineTo(urx - 0.5f, ury - 0.5f);
        stroke();
        // text
        resetRGBColorFill();
        beginText();
        setFontAndSize(bf, size);
        showTextAligned(PdfContentByte.ALIGN_CENTER, text, llx + (urx - llx) / 2, lly + (ury - lly) / 2, 0);
        endText();
    }
    
    /** Gets a <CODE>Graphics2D</CODE> to write on. The graphics
     * are translated to PDF commands.
     * @param width the width of the panel
     * @param height the height of the panel
     * @return a <CODE>Graphics2D</CODE>
     */    
    public java.awt.Graphics2D createGraphics(float width, float height) {
        return createGraphics(width, height, null);
    }

    /** Gets a <CODE>Graphics2D</CODE> to write on. The graphics
     * are translated to PDF commands.
     * @param width the width of the panel
     * @param height the height of the panel
     * @param fontMapper the mapping from awt fonts to <CODE>BaseFont</CODE>
     * @return a <CODE>Graphics2D</CODE>
     */    
    public java.awt.Graphics2D createGraphics(float width, float height, FontMapper fontMapper) {
        return new PdfGraphics2D(this, width, height, fontMapper);
    }
}
