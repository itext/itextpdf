/*
 * $Id: Chapter.java 3373 2008-05-12 16:21:24Z xlv $
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.text.pdf.parser;

import java.awt.Rectangle;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.DocumentFont;

/**
 * Provides information and calculations needed by render listeners
 * to display/evaluate text render operations.
 * <br><br>
 * This is passed between the {@link PdfContentStreamProcessor} and 
 * {@link RenderListener} objects as text rendering operations are
 * discovered
 */
public class TextRenderInfo {
	/**
	 * Enum with possible parameter for getLineSegment.
	 * @since 5.0.2
	 */
	public enum LinePos {BASELINE , ASCENT, DESCENT};
	
    private final String text;
    private final Matrix textToUserSpaceTransformMatrix;
    private final GraphicsState gs;
    private final int mcid;
    
    /**
     * Creates a new TextRenderInfo object
     * @param text the text that should be displayed
     * @param gs the graphics state (note: at this time, this is not immutable, so don't cache it)
     * @param textMatrix the text matrix at the time of the render operation
     * @param mcid the id of the marked content sequence, if any
     */
    TextRenderInfo(String text, GraphicsState gs, Matrix textMatrix, int mcid) {
        this.text = text;
        this.textToUserSpaceTransformMatrix = textMatrix.multiply(gs.ctm);
        this.gs = gs;
        this.mcid = mcid;
    }
    
    /**
     * @return the text to render
     */
    public String getText(){ 
        return text; 
    }

	/**
	 * Getter for the mcid in case the text belongs to a marked content sequence.
	 * @return an mcid
	 * @since 5.0.2
	 */
	public int getMcid() {
		return mcid;
	}

	/**
     * @return the unscaled (i.e. in Text space) width of the text
     */
    float getUnscaledWidth(){ 
        return getStringWidth(text); 
    }
    
    /**
     * Gets the baseline for the text (i.e. the line that the text 'sits' on)
     * @return the baseline line segment
     * @since 5.0.2
     */
    public LineSegment getBaseline(){
        return getUnscaledBaselineWithOffset(0).transformBy(textToUserSpaceTransformMatrix);
    }
    
    /**
     * Gets the ascentline for the text (i.e. the line that represents the topmost extent that a string of the current font could have)
     * @return the ascentline line segment
     * @since 5.0.2
     */
    public LineSegment getAscentLine(){
        float ascent = gs.getFont().getFontDescriptor(BaseFont.ASCENT, gs.getFontSize());
        return getUnscaledBaselineWithOffset(ascent).transformBy(textToUserSpaceTransformMatrix);
    }
    
    /**
     * Gets the descentline for the text (i.e. the line that represents the bottom most extent that a string of the current font could have)
     * @return the descentline line segment
     * @since 5.0.2
     */
    public LineSegment getDescentLine(){
        // per getFontDescription() API, descent is returned as a negative number, so we apply that as a normal vertical offset
        float descent = gs.getFont().getFontDescriptor(BaseFont.DESCENT, gs.getFontSize());
        return getUnscaledBaselineWithOffset(descent).transformBy(textToUserSpaceTransformMatrix);
    }
    
    private LineSegment getUnscaledBaselineWithOffset(float yOffset){
        return new LineSegment(new Vector(0, yOffset, 1), new Vector(getUnscaledWidth(), yOffset, 1));
    }

// KD - removing - use getAscentLine and getDescentLine instead    
//    /**
//     * Gets an object containing the start and end vector of the base line,
//     * ascent or descent.
//     * @param one of the following values: LinePos.ASCENT, LinePos.DESCENT, LinePos.BASELINE
//     * @return a line segment
//     * @since 5.0.2
//     */
//    public LineSegment getLineSegment(LinePos i) {
//    	float diff = 0;
//    	if (i == LinePos.ASCENT)
//    		diff = gs.getFont().getFontDescriptor(BaseFont.ASCENT, gs.getFontSize());
//    	else if (i == LinePos.DESCENT)
//    		diff = gs.getFont().getFontDescriptor(BaseFont.DESCENT, gs.getFontSize());
//    	return new LineSegment(getStartPoint(diff), getEndPoint(diff));
//    }

// KD - removing these for now - if we need access to the font, let's just have a getFont() method    
//    /**
//     * Returns the font size of the string.
//     * @return a font size
//     * @since 5.0.2
//     */
//    public float getFontSize() {
//    	return new Vector(0, gs.getFontSize(), 1).cross(textToUserSpaceTransformMatrix).subtract(getStartPoint(0)).length();
//    }
//
//    /**
//     * Gets the PS font name of the text.
//     * @return a font name
//     * @since 5.0.2
//     */
//    public String getPostscriptFontName() {
//    	return gs.getFont().getPostscriptFontName();
//    }
    
    /**
     * @return The width, in user space units, of a single space character in the current font
     */
    public float getSingleSpaceWidth(){
        LineSegment textSpace = new LineSegment(new Vector(0, 0, 1), new Vector(getUnscaledFontSpaceWidth(), 0, 1));
        LineSegment userSpace = textSpace.transformBy(textToUserSpaceTransformMatrix);
        return userSpace.getLength();
    }
    
    /**
     * @return the text render mode that should be used for the text.  From the
     * PDF specification, this means:
     * <ul>
     *   <li>0 = Fill text</li>
     *   <li>1 = Stroke text</li>
     *   <li>2 = Fill, then stroke text</li>
     *   <li>3 = Invisible</li>
     *   <li>4 = Fill text and add to path for clipping</li>
     *   <li>5 = Stroke text and add to path for clipping</li>
     *   <li>6 = Fill, then stroke text and add to path for clipping</li>
     *   <li>7 = Add text to padd for clipping</li>
     * </ul>
     * @since iText 5.0.1
     */
    public int getTextRenderMode(){
        return gs.renderMode;
    }
    
    /**
     * Calculates the width of a space character.  If the font does not define
     * a width for a standard space character \u0020, we also attempt to use
     * the width of \u00A0 (a non-breaking space in many fonts)
     * @return the width of a single space character in text space units
     */
    private float getUnscaledFontSpaceWidth(){
        char charToUse = ' ';
        if (gs.font.getWidth(charToUse) == 0)
            charToUse = '\u00A0';
        return getStringWidth(String.valueOf(charToUse));
    }
    
    /**
     * Gets the width of a String in text space units
     * @param string    the string that needs measuring
     * @return  the width of a String in text space units
     */
    private float getStringWidth(String string){
        DocumentFont font = gs.font;
        char[] chars = string.toCharArray();
        float totalWidth = 0;
        for (int i = 0; i < chars.length; i++) {
            float w = font.getWidth(chars[i]) / 1000.0f;
            float wordSpacing = chars[i] == 32 ? gs.wordSpacing : 0f;
            totalWidth += (w * gs.fontSize + gs.characterSpacing + wordSpacing) * gs.horizontalScaling;
        }
        
        return totalWidth;
    }
    
}
