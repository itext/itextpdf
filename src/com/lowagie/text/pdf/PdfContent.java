/*
 * @(#)PdfContent.java				0.37 2000/10/05
 *       release rugPdf0.10:		0.04 99/04/01
 *               rugPdf0.20:		0.13 99/11/30
 *               iText0.3:			0.25 2000/02/14
 *               iText0.36:			0.36 2000/09/10
 *               iText0.37:         0.37 2000/10/05 
 * 
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
import com.lowagie.text.Rectangle;

/**
 * <CODE>PdfContent</CODE> is an object containing the general graphic contents of a page.
 * <P>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 8.4 (General Graphics State; page 323-330) and 8.6 (Paths; page 333-339).<BR>
 * Remark: not al the functions of section 8.4 are implemented in this class.
 *
 * @see		PdfText
 * @see		PdfContents
 *
 * @author  bruno@lowagie.com
 * @version 0.37 2000/10/05
 * @since   rugPdf0.10
 */

public class PdfContent {

// membervariables

	/** This is the actual content */
	protected StringBuffer content = new StringBuffer("");

// constructors

	/**
	 * Constructs a new <CODE>PdfContent</CODE>-object.
	 *
	 * @since		rugPdf0.10
	 */

	protected PdfContent() {
	}

// methods to get the content of this object

	/**
     * Returns the <CODE>String</CODE> representation of this <CODE>PdfContent</CODE>-object.
	 *
	 * @return		a <CODE>String</CODE>
     *
	 * @since		rugPdf0.10
     */

    public String toString() {
		return content.toString();
    }

	/**
     * Returns the PDF representation of this <CODE>PdfContent</CODE>-object.
	 *
	 * @return		a <CODE>String</CODE>
     *
	 * @since		rugPdf0.10
     */

    String toPdf() {
		return toString();
    } 

// methods to add graphical content 

	/**
     * Adds the content of another <CODE>PdfContent</CODE>-object to this object.
	 *
	 * @param		other		another <CODE>PdfContent</CODE>-object
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void add(PdfContent other) {
		content.append(other.toString());
    }

	/**
	 * Changes the <VAR>Flatness</VAR>.
	 * <P>
     * <VAR>Flatness</VAR> sets the maximum permitted distance in device pixels between the
	 * mathematically correct path and an approximation constructed from straight line segments.<BR>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.4.1 (page 323).
	 *
	 * @param		flatness		a value
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void setFlatness(int flatness) {
		if (flatness > -1 && flatness < 101) {
			content.append(flatness).append(" i\n");
		}
    } 

	/**
	 * Changes the <VAR>Line cap style</VAR>.
	 * <P>
     * The <VAR>line cap style</VAR> specifies the shape to be used at the end of open subpaths
	 * when they are stroked.<BR>
	 * Allowed values are 0 (Butt end caps), 1 (Round end caps) and 2 (Projecting square end caps).<BR>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.4.2 (page 324).
	 *
	 * @param		style		a value
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
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
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.4.3 (page 324-325).
	 *
	 * @param		phase		the value of the phase
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void setLineDash(int phase) {
		PdfArray array = new PdfArray();
		content.append(array.toPdf()).append(" ").append(phase).append(" d\n");
    } 

	/**
     * Changes the value of the <VAR>line dash pattern</VAR>.
	 * <P>
	 * The line dash pattern controls the pattern of dashes and gaps used to stroke paths.
	 * It is specified by an <I>array</I> and a <I>phase</I>. The array specifies the length
	 * of the alternating dashes and gaps. The phase specifies the distance into the dash
	 * pattern to start the dash.<BR>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.4.3 (page 324-325).
	 *
	 * @param		phase		the value of the phase
	 * @param		unitsOn		the number of units that must be 'on' (equals the number of units that must be 'off').
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void setLineDash(int unitsOn, int phase) {
		PdfArray array = new PdfArray();
		array.add(new PdfNumber(unitsOn));
		content.append(array.toPdf()).append(" ").append(phase).append(" d\n");
    } 

	/**
     * Changes the value of the <VAR>line dash pattern</VAR>.
	 * <P>
	 * The line dash pattern controls the pattern of dashes and gaps used to stroke paths.
	 * It is specified by an <I>array</I> and a <I>phase</I>. The array specifies the length
	 * of the alternating dashes and gaps. The phase specifies the distance into the dash
	 * pattern to start the dash.<BR>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.4.3 (page 324-325).
	 *
	 * @param		phase		the value of the phase
	 * @param		unitsOn		the number of units that must be 'on'
	 * @param		unitsOff	the number of units that must be 'off'
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void setLineDash(int unitsOn, int unitsOff, int phase) {
		PdfArray array = new PdfArray();
		array.add(new PdfNumber(unitsOn));
		array.add(new PdfNumber(unitsOff));
		content.append(array.toPdf()).append(" ").append(phase).append(" d\n");
    } 

	/**
	 * Changes the <VAR>Line join style</VAR>.
	 * <P>
     * The <VAR>line join style</VAR> specifies the shape to be used at the corners of paths
	 * that are stroked.<BR>
	 * Allowed values are 0 (Miter joins), 1 (Round joins) and 2 (Bevel joins).<BR>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.4.4 (page 325-326).
	 *
	 * @param		style		a value
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
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
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.4.5 (page 326).
	 *
	 * @param		w			a width
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void setLineWidth(double w) {
		content.append(w).append(" w\n");
    } 

	/**
	 * Changes the <VAR>Miter limit</VAR>.
	 * <P>
     * When two line segments meet at a sharp angle and mitered joins have been specified as the
	 * line join style, it is possible for the miter to extend far beyond the thickness of the line
	 * stroking path. The miter limit imposes a maximum on the ratio of the miter length to the line
	 * witdh. When the limit is exceeded, the join is converted from a miter to a bevel.<BR>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.4.6 (page 327).
	 *
	 * @param		miterLimit		a miter limit
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void setMiterLimit(double miterLimit) {
		 if (miterLimit > 1) {
			 content.append(miterLimit).append(" M\n");
		 }
    }

	/**
	 * Changes the currentgray tint for filling paths (device dependent colors!).
	 * <P>
	 * Sets the color space to <B>DeviceGray</B> (or the <B>DefaultGray</B> color space),
	 * and sets the gray tint to use for filling paths.</P>
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.5.2.1 (page 331).</P>
	 * 
	 * @param	gray	a value between 0 (black) and 1 (white)
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
	 */

	public final void setGrayFill(double gray) {
		content.append(gray);
		content.append(" g\n");
	} 

	/**
	 * Changes the current gray tint for filling paths to black.
	 *
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
	 */

	public final void resetGrayFill() {
		content.append("0 g\n");
	}

	/**
	 * Changes the currentgray tint for stroking paths (device dependent colors!).
	 * <P>
	 * Sets the color space to <B>DeviceGray</B> (or the <B>DefaultGray</B> color space),
	 * and sets the gray tint to use for stroking paths.</P>
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.5.2.1 (page 331).</P>
	 * 
	 * @param	gray	a value between 0 (black) and 1 (white)
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
	 */

	public final void setGrayStroke(double gray) {
		content.append(gray);
		content.append(" G\n");
	} 

	/**
	 * Changes the current gray tint for stroking paths to black.
	 *
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
	 */

	public final void resetGrayStroke() {
		content.append("0 G\n");
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
	 * @param	red		the intensity of red 
	 * @param	green	the intensity of green
	 * @param	blue	the intensity of blue
	 * @return	<CODE>void</CODE> 
     *
	 * @since		iText0.30
	 */

	public final void setRGBColorFill(int red, int green, int blue) {
		 content.append((double)(red & 0xFF) / 0xFF);
		 content.append(' ');
		 content.append((double)(green & 0xFF) / 0xFF);
		 content.append(' ');
		 content.append((double)(blue & 0xFF) / 0xFF);
		 content.append(" rg\n");
	} 

	/**
	 * Changes the current color for filling paths to black.
	 *
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
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
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.5.2.1 (page 331).</P>
	 * Following the PDF manual, each operand must be a number between 0 (miniumum intensity) and
	 * 1 (maximum intensity). This method however accepts only integers between 0x00 and 0xFF.
	 * 
	 * @param	red		the intensity of red 
	 * @param	green	the intensity of green
	 * @param	blue	the intensity of blue
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
	 */

	public final void setRGBColorStroke(int red, int green, int blue) {
		 content.append((double)(red & 0xFF) / 0xFF);
		 content.append(' ');
		 content.append((double)(green & 0xFF) / 0xFF);
		 content.append(' ');
		 content.append((double)(blue & 0xFF) / 0xFF);
		 content.append(" RG\n");
	} 

	/**
	 * Changes the current color for stroking paths to black.
	 *
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
	 */

	public final void resetRGBColorStroke() {
		content.append("0 0 0 RG\n");
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
	 * @param	cyan	the intensity of cyan 
	 * @param	magenta	the intensity of magenta
	 * @param	yellow	the intensity of yellow
	 * @param	black	the intensity of black
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
	 */

	public final void setCMYKColorFill(int cyan, int magenta, int yellow, int black) {
		 content.append((double)(cyan & 0xFF) / 0xFF);
		 content.append(' ');
		 content.append((double)(magenta & 0xFF) / 0xFF);
		 content.append(' ');
		 content.append((double)(yellow & 0xFF) / 0xFF);
		 content.append(' ');
		 content.append((double)(black & 0xFF) / 0xFF);
		 content.append(" k\n");
	} 

	/**
	 * Changes the current color for filling paths to black.
	 *
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
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
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.5.2.1 (page 331).</P>
	 * Following the PDF manual, each operand must be a number between 0 (miniumum intensity) and
	 * 1 (maximum intensity). This method however accepts only integers between 0x00 and 0xFF.
	 * 
	 * @param	cyan	the intensity of red 
	 * @param	magenta	the intensity of green
	 * @param	yellow	the intensity of blue
	 * @param	black	the intensity of black
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
	 */

	public final void setCMYKColorStroke(int cyan, int magenta, int yellow, int black) {
		 content.append((double)(cyan & 0xFF) / 0xFF);
		 content.append(' ');
		 content.append((double)(magenta & 0xFF) / 0xFF);
		 content.append(' ');
		 content.append((double)(yellow & 0xFF) / 0xFF);
		 content.append(' ');
		 content.append((double)(black & 0xFF) / 0xFF);
		 content.append(" K\n");
	} 

	/**
	 * Changes the current color for stroking paths to black.
	 *
	 * @return	<CODE>void</CODE>
     *
	 * @since		iText0.30
	 */

	public final void resetCMYKColorStroke() {
		content.append("0 0 0 1 K\n");
	}

	/**
     * Move the current point <I>(x, y)</I>, omitting any connecting line segment.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.1 (page 334-336).
	 *
	 * @param		x				new x-coordinate
	 * @param		y				new y-coordinate
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void moveTo(int x, int y) {
		content.append(x).append(" ").append(y).append(" m\n");
    } 

	/**
     * Appends a straight line segment from the current point <I>(x, y)</I>. The new current
	 * point is <I>(x, y)</I>.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.1 (page 334-336).
	 *
	 * @param		x				new x-coordinate
	 * @param		y				new y-coordinate
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void lineTo(int x, int y) {
		content.append(x).append(" ").append(y).append(" l\n");
    } 

	/**
     * Appends a Bêzier curve to the path, starting from the current point.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.1 (page 334-336).
	 *
	 * @param		x1		x-coordinate of the first control point
	 * @param		y1		y-coordinate of the first control point
	 * @param		x2		x-coordinate of the second control point
	 * @param		y2		y-coordinate of the second control point
	 * @param		x3		x-coordinaat of the ending point (= new current point)
	 * @param		y3		y-coordinaat of the ending point (= new current point)
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void curveTo(int x1, int y1, int x2, int y2, int x3, int y3) {
		content.append(x1).append(' ').append(y1).append(' ').append(x2).append(' ').append(y2).append(' ').append(x3).append(' ').append(y3).append(" c\n");
    } 

	/**
     * Appends a Bêzier curve to the path, starting from the current point.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.1 (page 334-336).
	 *
	 * @param		x2		x-coordinate of the second control point
	 * @param		y2		y-coordinate of the second control point
	 * @param		x3		x-coordinaat of the ending point (= new current point)
	 * @param		y3		y-coordinaat of the ending point (= new current point)
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void curveTo(int x2, int y2, int x3, int y3) {
		content.append(x2).append(' ').append(y2).append(' ').append(x3).append(' ').append(y3).append(" v\n");
    } 

	/**
     * Appends a Bêzier curve to the path, starting from the current point.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.1 (page 334-336).
	 *
	 * @param		x1		x-coordinate of the first control point
	 * @param		y2		y-coordinate of the first control point
	 * @param		x3		x-coordinaat of the ending point (= new current point)
	 * @param		y3		y-coordinaat of the ending point (= new current point)
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void curveFromTo(int x1, int y1, int x3, int y3) {
		content.append(x1).append(' ').append(y1).append(' ').append(x3).append(' ').append(y3).append(" y\n");
    } 

	/**
     * Adds a rectangle to the current path.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.1 (page 334-336).
	 *
	 * @param		x		x-coordinate of the starting point
	 * @param		y		y-coordinate of the starting point
	 * @param		w		width
	 * @param		h		height
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void rectangle(int x, int y, int w, int h) {
		content.append(x).append(' ').append(y).append(' ').append(w).append(' ').append(h).append(" re\n");
    } 

	/**
     * Adds a border (complete or partially) to the current path..
	 *
	 * @param		element		a <CODE>Rectangle</CODE>
	 * @param		relativeY	the value that has to be substrated from the y-coordinates
	 * @return		<CODE>void</CODE>
     *
	 * @since		iText0.30
     */

    public final void rectangle(Rectangle rectangle) {

		// the coordinates of the border are retrieved
		int x1 = rectangle.left();
		int y1 = rectangle.top();
		int x2 = rectangle.right();
		int y2 = rectangle.bottom();

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
			setGrayStroke(rectangle.grayFill());
			setGrayFill(rectangle.grayFill());
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
			setLineWidth(rectangle.borderWidth());
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
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.1 (page 334-336).
	 *
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void closePath() {
		content.append("h\n");
    } 

	/**
     * Ends the path without filling or stroking it.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.2 (page 336-338).
	 *
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void newPath() {
		content.append("n\n");
    } 

	/**
     * Strokes the path.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.2 (page 336-338).
	 *
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void stroke() {
		content.append("S\n");
    }

	/**
     * Closes the path and strokes it.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.2 (page 336-338).
	 *
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void closePathStroke() {
		content.append("s\n");
    } 

	/**
     * Fills the path, using the non-zero winding number rule to determine the region to fill.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.2 (page 336-338).
	 *
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void fill() {
		content.append("f\n");
    } 

	/**
     * Fills the path, using the even-odd rule to determine the region to fill.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.2 (page 336-338).
	 *
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void eoFill() {
		content.append("f*\n");
    } 

	/**
     * Fills the path using the non-zero winding number rule to determine the region to fill and strokes it.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.2 (page 336-338).
	 *
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void fillStroke() {
		content.append("B\n");
    } 				  

	/**
     * Closes the path, fills it using the non-zero winding number rule to determine the region to fill and strokes it.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.2 (page 336-338).
	 *
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void closePathFillStroke() {
		content.append("b\n");
    } 

	/**
     * Fills the path, using the even-odd rule to determine the region to fill and strokes it.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.2 (page 336-338).
	 *
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void eoFillStroke() {
		content.append("B*\n");
    } 

	/**
     * Closes the path, fills it using the even-odd rule to determine the region to fill and strokes it.
	 * <P>
	 * This method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.6.2 (page 336-338).
	 *
	 * @return		<CODE>void</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final void closePathEoFillStroke() {
		content.append("b*\n");
    }

	/**
	 * Adds an image to the content.
	 * <P>
	 * Part of this method is described in the 'Portable Document Format Reference Manual version 1.3'
	 * section 8.3.2 (the use of q a b c d e f cm Q); the other part is described in 8.8.1 (Do).
	 *
	 * @param		name		the <CODE>PdfName</CODE> of the image that has to be drawn
	 * @param		width		the width of the image
	 * @param		height		the height of the image
	 * @param		x			the x position of the image
	 * @param		y			the y position of the image
	 * @return		<CODE>void</CODE>
	 *
	 * @since		iText0.31
	 */

	final void doImage(PdfName name, double[] matrix, int x, int y) {
		content.append("q ");
		content.append(format(matrix[Image.AX])).append(" ");
		content.append(format(matrix[Image.AY])).append(" ");
		content.append(format(matrix[Image.BX])).append(" ");
		content.append(format(matrix[Image.BY])).append(" ");
		content.append(format(x - matrix[Image.CX])).append(" ");
		content.append(format(y - matrix[Image.CY])).append(" cm ");
		content.append(name.toString()).append(" Do Q\n");;
	}

	/**
	 * Formats a double.
	 *
	 * @param	a double
	 * @return	a <CODE>String</CODE>
	 */

	private String format(double d) {
		d *= 10000.0;
		int i = (int) d;
		d = i / 10000.0;
		String result = String.valueOf(d);
		if (result.endsWith(".0")) {
			result = result.substring(0, result.length() - 2);
		}
		return result;
	}
}