/*
 * Copyright (c) 1999, 2000 Bruno Lowagie.
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
    
/** This class keeps the graphic state of the current page
 */    
    class GraphicState
    {
        /** This is the font in use */
        BaseFont font;
        /** This is the font size in use */
        float size;        
        /** The x position of the text line matrix.
         */    
        protected float xTLM = 0;
        /** The y position of the text line matrix.
         */    
        protected float yTLM = 0;
        /** The current text leading.
         */    
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
        
    // constructors
    
    /**
     * Constructs a new <CODE>PdfContentByte</CODE>-object.
     *
     * @param wr the writer associated to this content
     */
    
    public PdfContentByte(PdfWriter wr)
    {
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
    
/** Gets the internal buffer.
 * @return the internal buffer
 */    
    ByteBuffer getInternalBuffer() {
        return content;
    }
    
    /**
     * Returns the PDF representation of this <CODE>PdfContentByte</CODE>-object.
     *
     * @return		a <CODE>String</CODE>
     */
    
    public byte[] toPdf() {
        return content.toByteArray();
    }
    
    // methods to add graphical content
    
    /**
     * Adds the content of another <CODE>PdfContent</CODE>-object to this object.
     *
     * @param		other		another <CODE>PdfByteContent</CODE>-object
     */
    
    public final void add(PdfContentByte other) {
        content.append(other.content);
    }
    
    /** Gets the x position of the text line matrix.
     *
     * @return the x position of the text line matrix
     */
    public float getXTLM()
    {
        return state.xTLM;
    }
    
    /** Gets the y position of the text line matrix.
     *
     * @return the y position of the text line matrix
     */
    public float getYTLM()
    {
        return state.yTLM;
    }
    
    /** Gets the current text leading.
     *
     * @return the current text leading
     */
    public float getLeading()
    {
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
            content.append(flatness).append(" i\n");
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
        if (style == 1 || style == 2 || style == 3) {
            content.append(style).append(" J\n");
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
        content.append("[] ").append(phase).append(" d\n");
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
        content.append("[").append(unitsOn).append("] ").append(phase).append(" d\n");
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
        content.append("[").append(unitsOn).append(' ').append(unitsOff).append("] ").append(phase).append(" d\n");
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
        if (style == 1 || style == 2 || style == 3) {
            content.append(style).append(" j\n");
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
        content.append(w).append(" w\n");
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
            content.append(miterLimit).append(" M\n");
        }
    }
    
    /**
     * Changes the currentgray tint for filling paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceGray</B> (or the <B>DefaultGray</B> color space),
     * and sets the gray tint to use for filling paths.</P>
     *
     * @param	gray	a value between 0 (black) and 1 (white)
     */
    
    public final void setGrayFill(float gray) {
        content.append(gray).append(" g\n");
    }
    
    /**
     * Changes the current gray tint for filling paths to black.
     */
    
    public final void resetGrayFill() {
        content.append("0 g\n");
    }
    
    /**
     * Changes the currentgray tint for stroking paths (device dependent colors!).
     * <P>
     * Sets the color space to <B>DeviceGray</B> (or the <B>DefaultGray</B> color space),
     * and sets the gray tint to use for stroking paths.</P>
     *
     * @param	gray	a value between 0 (black) and 1 (white)
     */
    
    public final void setGrayStroke(float gray) {
        content.append(gray).append(" G\n");
    }
    
    /**
     * Changes the current gray tint for stroking paths to black.
     */
    
    public final void resetGrayStroke() {
        content.append("0 G\n");
    }
    
    /** Helper to validate and write the RGB color components
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
    
    public final void setRGBColorFillF(float red, float green, float blue) {
        HelperRGB(red, green, blue);
        content.append(" rg\n");
    }
    
    /**
     * Changes the current color for filling paths to black.
     */
    
    public final void resetRGBColorFill() {
        content.append("0 0 0 rg\n");
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
    
    public final void setRGBColorStrokeF(float red, float green, float blue) {
        HelperRGB(red, green, blue);
        content.append(" RG\n");
    }
    
    /**
     * Changes the current color for stroking paths to black.
     *
     */
    
    public final void resetRGBColorStroke() {
        content.append("0 0 0 RG\n");
    }
    
    /** Helper to validate and write the CMYK color components
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
    
    public final void setCMYKColorFillF(float cyan, float magenta, float yellow, float black) {
        HelperCMYK(cyan, magenta, yellow, black);
        content.append(" k\n");
    }
    
    /**
     * Changes the current color for filling paths to black.
     *
     */
    
    public final void resetCMYKColorFill() {
        content.append("0 0 0 1 k\n");
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
    
    public final void setCMYKColorStrokeF(float cyan, float magenta, float yellow, float black) {
        HelperCMYK(cyan, magenta, yellow, black);
        content.append(" K\n");
    }
    
    /**
     * Changes the current color for stroking paths to black.
     *
     */
    
    public final void resetCMYKColorStroke() {
        content.append("0 0 0 1 K\n");
    }
    
    /**
     * Move the current point <I>(x, y)</I>, omitting any connecting line segment.
     *
     * @param		x				new x-coordinate
     * @param		y				new y-coordinate
     */
    
    public final void moveTo(float x, float y) {
        content.append(x).append(' ').append(y).append(" m\n");
    }
    
    /**
     * Appends a straight line segment from the current point <I>(x, y)</I>. The new current
     * point is <I>(x, y)</I>.
     *
     * @param		x				new x-coordinate
     * @param		y				new y-coordinate
     */
    
    public final void lineTo(float x, float y) {
        content.append(x).append(' ').append(y).append(" l\n");
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
        content.append(x1).append(' ').append(y1).append(' ').append(x2).append(' ').append(y2).append(' ').append(x3).append(' ').append(y3).append(" c\n");
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
        content.append(x2).append(' ').append(y2).append(' ').append(x3).append(' ').append(y3).append(" v\n");
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
        content.append(x1).append(' ').append(y1).append(' ').append(x3).append(' ').append(y3).append(" y\n");
    }
    
    /** Draws a circle. The endpoint will (x+r, y).
     *
     * @param x x center of circle
     * @param y y center of circle
     * @param r radius of circle
     */
    public final void circle(float x, float y, float r)
    {
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
        content.append(x).append(' ').append(y).append(' ').append(w).append(' ').append(h).append(" re\n");
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
            setRGBColorStroke(background.getRed(), background.getGreen(), background.getBlue());
            setRGBColorFill(background.getRed(), background.getGreen(), background.getBlue());
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
            setRGBColorStroke(color.getRed(), color.getGreen(), color.getBlue());
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
        content.append("h\n");
    }
    
    /**
     * Ends the path without filling or stroking it.
     */
    
    public final void newPath() {
        content.append("n\n");
    }
    
    /**
     * Strokes the path.
     */
    
    public final void stroke() {
        content.append("S\n");
    }
    
    /**
     * Closes the path and strokes it.
     */
    
    public final void closePathStroke() {
        content.append("s\n");
    }
    
    /**
     * Fills the path, using the non-zero winding number rule to determine the region to fill.
     */
    
    public final void fill() {
        content.append("f\n");
    }
    
    /**
     * Fills the path, using the even-odd rule to determine the region to fill.
     */
    
    public final void eoFill() {
        content.append("f*\n");
    }
    
    /**
     * Fills the path using the non-zero winding number rule to determine the region to fill and strokes it.
     */
    
    public final void fillStroke() {
        content.append("B\n");
    }
    
    /**
     * Closes the path, fills it using the non-zero winding number rule to determine the region to fill and strokes it.
     */
    
    public final void closePathFillStroke() {
        content.append("b\n");
    }
    
    /**
     * Fills the path, using the even-odd rule to determine the region to fill and strokes it.
     */
    
    public final void eoFillStroke() {
        content.append("B*\n");
    }
    
    /**
     * Closes the path, fills it using the even-odd rule to determine the region to fill and strokes it.
     */
    
    public final void closePathEoFillStroke() {
        content.append("b*\n");
    }
    
    /** Adds an <CODE>Image</CODE> to the page. The <CODE>Image</CODE> must have
     * absolute positioning.
     * @param image the <CODE>Image</CODE> object
     * @throws DocumentException if the <CODE>Image</CODE> does not have absolute positioning
     */
    final public void addImage(Image image) throws DocumentException
    {
        if (!image.hasAbsolutePosition())
            throw new DocumentException("The image must have absolute positioning.");
        float matrix[] = image.matrix();
        matrix[Image.CX] = image.absoluteX() - matrix[Image.CX];
        matrix[Image.CY] = image.absoluteY() - matrix[Image.CY];
        addImage(image, matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5]);
    }
    
    /** Adds an <CODE>Image</CODE> to the page. The positioning of the <CODE>Image</CODE>
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
    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException
    {
        checkWriter();
        try {
            PdfName name = pdf.addDirectImage(image);
            content.append("q ");
            content.append(a).append(' ');
            content.append(b).append(' ');
            content.append(c).append(' ');
            content.append(d).append(' ');
            content.append(e).append(' ');
            content.append(f).append(" cm ");
            content.append(name.toString()).append(" Do Q\n");
        }
        catch (Exception ee) {
            throw new DocumentException(ee.getMessage());
        }
    }
    
    /** Makes this <CODE>PdfContentByte</CODE> empty.
     */
    public void reset()
    {
        content.reset();
        stateList.clear();
        state = new GraphicState();
    }

    /** Starts the writing of text.
     */
    public void beginText()
    {
        state.xTLM = 0;
        state.yTLM = 0;
        content.append("BT\n");
    }
    
    /** Ends the writing of text and makes the current font invalid.
     */
    public void endText()
    {
        content.append("ET\n");
    }
    
    /** Saves the graphic state. <CODE>saveState</CODE> and
     * <CODE>restoreState</CODE> must be balanced.
     */
    public void saveState()
    {
        content.append("q\n");
        stateList.add(state);
    }
    
    /** Restores the graphic state. <CODE>saveState</CODE> and
     * <CODE>restoreState</CODE> must be balanced.
     */
    public void restoreState()
    {
        content.append("Q\n");
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
    public void setCharacterSpacing(float charSpace)
    {
        content.append(charSpace).append(" Tc\n");
    }
    
	/**
	 * Sets the word spacing parameter.
	 *
	 * @param		wordSpace			a parameter
	 */
    public void setWordSpacing(float wordSpace)
    {
        content.append(wordSpace).append(" Tw\n");
    }
    
	/**
	 * Sets the horizontal scaling parameter.
	 *
	 * @param		scale				a parameter
	 */
    public void setHorizontalScaling(float scale)
    {
        content.append(scale).append(" Tz\n");
    }
    
	/**
	 * Sets the text leading parameter.
	 * <P>
	 * The leading parameter is measured in text space units. It specifies the vertical distance
	 * between the baselines of adjacent lines of text.</P>
	 *
	 * @param		leading			the new leading
	 */
    public void setLeading(float leading)
    {
        state.leading = leading;
        content.append(leading).append(" TL\n");
    }
    
    /** Set the font and the size for the subsequent text writing.
     *
     * @param bf the font
     * @param size the font size in points
     */
    public void setFontAndSize(BaseFont bf, float size)
    {
        checkWriter();
        state.font = bf;
        state.size = size;
        PdfName name = writer.add(bf);
        content.append(name.toPdf()).append(' ').append(size).append(" Tf\n");
    }
    
	/**
	 * Sets the text rendering parameter.
	 *
	 * @param		rendering				a parameter
	 */
    public void setTextRenderingMode(int rendering)
    {
        content.append(rendering).append(" Tr\n");
    }
    
	/**
	 * Sets the text rise parameter.
	 * <P>
	 * This allows to write text in subscript or superscript mode.</P>
	 *
	 * @param		rise				a parameter
	 */
    public void setTextRise(float rise)
    {
        content.append(rise).append(" Ts\n");
    }
    
    /** A helper to insert into the content stream the <CODE>text</CODE>
     * converted to bytes according to the font's encoding.
     *
     * @param text the text to write
     */
    private void showText2(String text)
    {
        if (state.font == null)
            throw new NullPointerException("Font and size must be set before writing any text");
        byte b[] = state.font.convertToBytes(text);
        content.append(escapeString(b));
    }
    
    /** Shows the <CODE>text</CODE>.
     *
     * @param text the text to write
     */
    public void showText(String text)
    {
        showText2(text);
        content.append("Tj\n");
    }
    
    /** Shows the <CODE>text</CODE>.
     *
     * @param text the text to write
     */
    public void showText(PdfPrintable text)
    {
        showText2(text.toString());
        content.append("Tj\n");
    }
    
    /** Moves to the next line and shows <CODE>text</CODE>.
     *
     * @param text the text to write
     */
    public void newlineShowText(String text)
    {
        state.yTLM -= state.leading;
        showText2(text);
        content.append("'\n");
    }
    
    /** Moves to the next line and shows <CODE>text</CODE>.
     *
     * @param text the text to write
     */
    public void newlineShowText(PdfPrintable text)
    {
        state.yTLM -= state.leading;
        showText2(text.toString());
        content.append("'\n");
    }
    
    /** Moves to the next line and shows text string, using the given values of the character and word spacing parameters.
     *
	 * @param		wordSpacing		a parameter
	 * @param		charSpacing		a parameter
     * @param text the text to write
     */
    public void newlineShowText(float wordSpacing, float charSpacing, String text)
    {
        state.yTLM -= state.leading;
        content.append(wordSpacing).append(' ').append(charSpacing);
        showText2(text);
        content.append("\"\n");
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
    public void setTextMatrix(float a, float b, float c, float d, float x, float y)
    {
        state.xTLM = x;
        state.yTLM = y;
        content.append(a).append(' ').append(b).append_i(' ')
        .append(c).append_i(' ').append(d).append_i(' ')
        .append(x).append_i(' ').append(y).append(" Tm\n");
    }
    
	/**
     * Changes the text matrix. The first four parameters are {1,0,0,1}.
	 * <P>
	 * Remark: this operation also initializes the current point position.</P>
	 *
	 * @param		x			operand 3,1 in the matrix
	 * @param		y			operand 3,2 in the matrix
	 */
    public void setTextMatrix(float x, float y)
    {
        setTextMatrix(1, 0, 0, 1, x, y);
    }
    
	/**
	 * Moves to the start of the next line, offset from the start of the current line.
	 *
	 * @param		x			x-coordinate of the new current point
	 * @param		y			y-coordinate of the new current point
	 */
    public void moveText(float x, float y)
    {
        state.xTLM += x;
        state.yTLM += y;
        content.append(x).append(' ').append(y).append(" Td\n");
    }
    
	/**
	 * Moves to the start of the next line, offset from the start of the current line.
	 * <P>
	 * As a side effect, this sets the leading parameter in the text state.</P>
	 *
	 * @param		x			offset of the new current point
	 * @param		y			y-coordinate of the new current point
	 */
    public void moveTextWithLeading(float x, float y)
    {
        state.xTLM += x;
        state.yTLM += y;
        state.leading = -y;
        content.append(x).append(' ').append(y).append(" TD\n");
    }
    
	/**
	 * Moves to the start of the next line.
	 */
    public void newlineText()
    {
        state.yTLM -= state.leading;
        content.append("T*\n");
    }
    
    /** Gets the size of this content.
     *
     * @return the size of the content
     */
    int size()
    {
        return content.size();
    }

    /** Escapes a <CODE>byte</CODE> array according to the PDF conventions.
     *
     * @param b the <CODE>byte</CODE> array to escape
     * @return an escaped <CODE>byte</CODE> array
     */
    static byte[] escapeString(byte b[])
    {
        ByteBuffer content = new ByteBuffer();
        content.append_i('(');
        for (int k = 0; k < b.length; ++k) {
            byte c = b[k];
            switch ((int)c) {
                case '\r':
                    content.append("\\r");
                    break;
                case '\n':
                    content.append("\\n");
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
        return content.toByteArray();
    }
    
    /** Adds an outline to the document.
     *
     * @param outline the outline
     */
    public void addOutline(PdfOutline outline)
    {
        checkWriter();
        pdf.addOutline(outline);
    }

    /** Adds a named outline to the document.
     *
     * @param outline the outline
     * @param name the name for the local destination
 */
    public void addOutline(PdfOutline outline, String name)
    {
        checkWriter();
        pdf.addOutline(outline, name);
    }

    /** Gets the root outline.
     *
     * @return the root outline
     */
    public PdfOutline getRootOutline()
    {
        checkWriter();
        return pdf.getRootOutline();
    }
    
    /** Shows text right, left or center aligned with rotation.
     * @param alignement the alignment can be ALIGN_CENTER, ALIGN_RIGHT or ALIGN_LEFT
     * @param text the text to show
     * @param x the x pivot position
     * @param y the y pivot position
     * @param rotation the rotation to be applied in degrees counterclockwise
     */
    public void showTextAligned(int alignement, String text, float x, float y, float rotation)
    {
        if (state.font == null)
            throw new NullPointerException("Font and size must be set before writing any text");
        if (rotation == 0) {
            switch (alignement) {
                case ALIGN_CENTER:
                    x -= state.font.getWidthPoint(text, state.size) / 2;
                    break;
                case ALIGN_RIGHT:
                    x -= state.font.getWidthPoint(text, state.size);
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
                    len = state.font.getWidthPoint(text, state.size) / 2;
                    x -=  len * cos;
                    y -=  len * sin;
                    break;
                case ALIGN_RIGHT:
                    len = state.font.getWidthPoint(text, state.size);
                    x -=  len * cos;
                    y -=  len * sin;
                    break;
            }
            setTextMatrix(cos, sin, -sin, cos, x, y);
            showText(text);
            setTextMatrix(0f, 0f);
        }
    }

    /** Concatenate a matrix to the current transformation matrix.
     * @param a an element of the transformation matrix
     * @param b an element of the transformation matrix
     * @param c an element of the transformation matrix
     * @param d an element of the transformation matrix
     * @param e an element of the transformation matrix
     * @param f an element of the transformation matrix
     **/
    public void concatCTM(float a, float b, float c, float d, float e, float f)
    {
        content.append(a).append(' ').append(b).append(' ').append(c).append(' ');
        content.append(d).append(' ').append(e).append(' ').append(f).append(" cm\n");
    }
    
/** Generates an array of bezier curves to draw an arc.
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
    public static ArrayList bezierArc(float x1, float y1, float x2, float y2, float startAng, float extent)
    {
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

/** Draws a partial ellipse inscribed within the rectangle x1,y1,x2,y2,
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
    public void arc(float x1, float y1, float x2, float y2, float startAng, float extent)
    {
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

/** Draws an ellipse inscribed within the rectangle x1,y1,x2,y2.
 *
 * @param x1 a corner of the enclosing rectangle
 * @param y1 a corner of the enclosing rectangle
 * @param x2 a corner of the enclosing rectangle
 * @param y2 a corner of the enclosing rectangle
 */    
    public void ellipse(float x1, float y1, float x2, float y2)
    {
        arc(x1, y1, x2, y2, 0f, 360f);
    }
    
    /** Creates a new template.
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
    public PdfTemplate createTemplate(float width, float height)
    {
        checkWriter();
        PdfTemplate template = new PdfTemplate(writer);
        template.setWidth(width);
        template.setHeight(height);
        writer.addDirectTemplateSimple(template);
        return template;
    }
    
    /** Adds a template to this content.
     *
     * @param template the template
     * @param a an element of the transformation matrix
     * @param b an element of the transformation matrix
     * @param c an element of the transformation matrix
     * @param d an element of the transformation matrix
     * @param e an element of the transformation matrix
     * @param f an element of the transformation matrix
     */
    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f)
    {
        checkWriter();
        PdfName name = pdf.addTemplateToPage(template);
        content.append("q ");
        content.append(a).append(' ');
        content.append(b).append(' ');
        content.append(c).append(' ');
        content.append(d).append(' ');
        content.append(e).append(' ');
        content.append(f).append(" cm ");
        content.append(name.toString()).append(" Do Q\n");
    }

    /** Adds a template to this content.
     *
     * @param template the template
     * @param x the x location of this template
     * @param y the y location of this template
     */
    public void addTemplate(PdfTemplate template, float x, float y)
    {
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
     * @since iText0.30
     * @param cyan the intensity of cyan
     * @param magenta the intensity of magenta
     * @param yellow the intensity of yellow
     * @param black the intensity of black
 */
    
    public final void setCMYKColorFill(int cyan, int magenta, int yellow, int black) {
        content.append((float)(cyan & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(magenta & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(yellow & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(black & 0xFF) / 0xFF);
        content.append(" k\n");
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
     * @since iText0.30
     * @param cyan the intensity of red
     * @param magenta the intensity of green
     * @param yellow the intensity of blue
     * @param black the intensity of black
 */
    
    public final void setCMYKColorStroke(int cyan, int magenta, int yellow, int black) {
        content.append((float)(cyan & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(magenta & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(yellow & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(black & 0xFF) / 0xFF);
        content.append(" K\n");
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
     * @since iText0.30
     * @param red the intensity of red
     * @param green the intensity of green
     * @param blue the intensity of blue
 */
    
    public final void setRGBColorFill(int red, int green, int blue) {
        content.append((float)(red & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(green & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(blue & 0xFF) / 0xFF);
        content.append(" rg\n");
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
     * @since iText0.30
     * @param red the intensity of red
     * @param green the intensity of green
     * @param blue the intensity of blue
 */
    
    public final void setRGBColorStroke(int red, int green, int blue) {
        content.append((float)(red & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(green & 0xFF) / 0xFF);
        content.append(' ');
        content.append((float)(blue & 0xFF) / 0xFF);
        content.append(" RG\n");
    }

    /** Check if we have a valid PdfWriter.
     *
     * @throw NullPointerException the writer is invalid
     */
    protected void checkWriter()
    {
        if (writer == null)
            throw new NullPointerException("The writer at PdfContentByte is null.");
    }
    
/** Show an array of text.
 * @param text array of text
 */    
	void showText(PdfTextArray text) {
        if (state.font == null)
            throw new NullPointerException("Font and size must be set before writing any text");
		content.append(text.toPdf()).append(" TJ\n");
	}

/** Gets the <CODE>PdfWriter</CODE> in use by this object.
 * @return the <CODE>PdfWriter</CODE> in use by this object
 */    
    PdfWriter getPdfWriter()
    {
        return writer;
    }

/** Gets the <CODE>PdfDocument</CODE> in use by this object.
 * @return the <CODE>PdfDocument</CODE> in use by this object
 */    
    PdfDocument getPdfDocument()
    {
        return pdf;
    }
    
/** Implements a link to other part of the document. The jump will
 * be made to a local destination with the same name, that must exist.
 * @param name the name for this link
 * @param llx the lower left x corner of the activation area
 * @param lly the lower left y corner of the activation area
 * @param urx the upper right x corner of the activation area
 * @param ury the upper right y corner of the activation area
 */    
    public void localGoto(String name, float llx, float lly, float urx, float ury)
    {
        pdf.localGoto(name, llx, lly, urx, ury);
    }
    
/** The local destination to where a local goto with the same
 * name will jump.
 * @param name the name of this local destination
 * @param destination the <CODE>PdfDestination</CODE> with the jump coordinates
 * @return <CODE>true</CODE> if the local destination was added,
 * <CODE>false</CODE> if a local destination with the same name
 * already exists
 */    
    public boolean localDestination(String name, PdfDestination destination)
    {
        return pdf.localDestination(name, destination);
    }
    
/** Gets a duplicate of this <CODE>PdfContentByte</CODE>. All
 * the members are copied by reference but the buffer stays different.
 * @return a copy of this <CODE>PdfContentByte</CODE>
 */    
    public PdfContentByte getDuplicate()
    {
        return new PdfContentByte(writer);
    }
/** Implements a link to another document.
 * @param filename the filename for the remote document
 * @param name the name to jump to
 * @param llx the lower left x corner of the activation area
 * @param lly the lower left y corner of the activation area
 * @param urx the upper right x corner of the activation area
 * @param ury the upper right y corner of the activation area
 */    
    public void remoteGoto(String filename, String name, float llx, float lly, float urx, float ury)
    {
        remoteGoto(filename, name, llx, lly, urx, ury);        
    }


/** Implements a link to another document.
 * @param filename the filename for the remote document
 * @param page the page to jump to
 * @param llx the lower left x corner of the activation area
 * @param lly the lower left y corner of the activation area
 * @param urx the upper right x corner of the activation area
 * @param ury the upper right y corner of the activation area
 */    
    public void remoteGoto(String filename, int page, float llx, float lly, float urx, float ury)
    {
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
    public void roundRectangle(float x, float y, float w, float h, float r)
    {
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
}
