/*
 * $Id: TextRenderInfo.java 6583 2014-10-24 10:11:24Z achingarev $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf.parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfString;

/**
 * Provides information and calculations needed by render listeners
 * to display/evaluate text render operations.
 * <br><br>
 * This is passed between the {@link PdfContentStreamProcessor} and
 * {@link RenderListener} objects as text rendering operations are
 * discovered
 */
public class TextRenderInfo {

    private final PdfString string;
    private String text = null;
    private final Matrix textToUserSpaceTransformMatrix;
    private final GraphicsState gs;
    private Float unscaledWidth = null;

    /**
     * Array containing marked content info for the text.
     * @since 5.0.2
     */
    private final Collection<MarkedContentInfo> markedContentInfos;

    /**
     * Creates a new TextRenderInfo object
     * @param string the PDF string that should be displayed
     * @param gs the graphics state (note: at this time, this is not immutable, so don't cache it)
     * @param textMatrix the text matrix at the time of the render operation
     * @param markedContentInfo the marked content sequence, if available
     */
    TextRenderInfo(PdfString string, GraphicsState gs, Matrix textMatrix, Collection<MarkedContentInfo> markedContentInfo) {
        this.string = string;
        this.textToUserSpaceTransformMatrix = textMatrix.multiply(gs.ctm);
        this.gs = gs;
        this.markedContentInfos = new ArrayList<MarkedContentInfo>(markedContentInfo);
    }

    /**
     * Used for creating sub-TextRenderInfos for each individual character
     * @param parent the parent TextRenderInfo
     * @param string the content of a TextRenderInfo
     * @param horizontalOffset the unscaled horizontal offset of the character that this TextRenderInfo represents
     * @since 5.3.3
     */
    private TextRenderInfo(TextRenderInfo parent, PdfString string, float horizontalOffset){
        this.string = string;
    	this.textToUserSpaceTransformMatrix = new Matrix(horizontalOffset, 0).multiply(parent.textToUserSpaceTransformMatrix);
    	this.gs = parent.gs;
    	this.markedContentInfos = parent.markedContentInfos;
    }

    /**
     * @return the text to render
     */
    public String getText(){
        if (text == null)
            text = decode(string);
        return text;
    }

    /**
     * @return original PDF string
     */
    public PdfString getPdfString() { return string; }

	/**
	 * Checks if the text belongs to a marked content sequence
	 * with a given mcid.
	 * @param mcid a marked content id
	 * @return true if the text is marked with this id
	 * @since 5.0.2
	 */
	public boolean hasMcid(int mcid) {
        return hasMcid(mcid, false);
	}

    /**
	 * Checks if the text belongs to a marked content sequence
	 * with a given mcid.
     * @param mcid a marked content id
     * @param checkTheTopmostLevelOnly indicates whether to check the topmost level of marked content stack only
     * @return true if the text is marked with this id
     * @since 5.3.5
     */
    public boolean hasMcid(int mcid, boolean checkTheTopmostLevelOnly) {
        if (checkTheTopmostLevelOnly) {
            if (markedContentInfos instanceof ArrayList) {
                Integer infoMcid = getMcid();
                return (infoMcid != null) ? infoMcid == mcid : false;
            }
        } else {
            for (MarkedContentInfo info : markedContentInfos) {
                if (info.hasMcid())
                    if(info.getMcid() == mcid)
                        return true;
            }
        }
        return false;
    }

    /**
     * @return the marked content associated with the TextRenderInfo instance.
     */
    public Integer getMcid() {
        if (markedContentInfos instanceof ArrayList) {
            ArrayList<MarkedContentInfo> mci = (ArrayList<MarkedContentInfo>)markedContentInfos;
            MarkedContentInfo info = mci.size() > 0 ? mci.get(mci.size() - 1) : null;
            return (info != null && info.hasMcid()) ? info.getMcid() : null;
        }
        return null;
    }

	/**
     * @return the unscaled (i.e. in Text space) width of the text
     */
    float getUnscaledWidth(){
        if (unscaledWidth == null)
            unscaledWidth = Float.valueOf(getPdfStringWidth(string, false));
        return unscaledWidth;
    }

    /**
     * Gets the baseline for the text (i.e. the line that the text 'sits' on)
     * This value includes the Rise of the draw operation - see {@link #getRise()} for the amount added by Rise
     * @return the baseline line segment
     * @since 5.0.2
     */
    public LineSegment getBaseline(){
        return getUnscaledBaselineWithOffset(0 + gs.rise).transformBy(textToUserSpaceTransformMatrix);
    }

    /**
     * Gets the ascentline for the text (i.e. the line that represents the topmost extent that a string of the current font could have)
     * This value includes the Rise of the draw operation - see {@link #getRise()} for the amount added by Rise
     * @return the ascentline line segment
     * @since 5.0.2
     */
    public LineSegment getAscentLine(){
        float ascent = gs.getFont().getFontDescriptor(BaseFont.ASCENT, gs.getFontSize());
        return getUnscaledBaselineWithOffset(ascent + gs.rise).transformBy(textToUserSpaceTransformMatrix);
    }

    /**
     * Gets the descentline for the text (i.e. the line that represents the bottom most extent that a string of the current font could have).
     * This value includes the Rise of the draw operation - see {@link #getRise()} for the amount added by Rise
     * @return the descentline line segment
     * @since 5.0.2
     */
    public LineSegment getDescentLine(){
        // per getFontDescription() API, descent is returned as a negative number, so we apply that as a normal vertical offset
        float descent = gs.getFont().getFontDescriptor(BaseFont.DESCENT, gs.getFontSize());
        return getUnscaledBaselineWithOffset(descent + gs.rise).transformBy(textToUserSpaceTransformMatrix);
    }

    private LineSegment getUnscaledBaselineWithOffset(float yOffset){

    	// we need to correct the width so we don't have an extra character spacing value at the end.  The extra character space is important for tracking relative text coordinate systems, but should not be part of the baseline
    	float correctedUnscaledWidth = getUnscaledWidth() - gs.characterSpacing * gs.horizontalScaling;

        return new LineSegment(new Vector(0, yOffset, 1), new Vector(correctedUnscaledWidth, yOffset, 1));
    }

	/**
	 * Getter for the font
	 * @return the font
	 * @since iText 5.0.2
	 */
	public DocumentFont getFont() {
		return gs.getFont();
	}

// removing - this shouldn't be needed now that we are exposing getCharacterRenderInfos()
//	/**
//	 * @return The character spacing width, in user space units (Tc value, scaled to user space)
//	 * @since 5.3.3
//	 */
//	public float getCharacterSpacing(){
//		return convertWidthFromTextSpaceToUserSpace(gs.characterSpacing);
//	}
//	
//	/**
//	 * @return The word spacing width, in user space units (Tw value, scaled to user space)
//	 * @since 5.3.3
//	 */
//	public float getWordSpacing(){
//		return convertWidthFromTextSpaceToUserSpace(gs.wordSpacing);
//	}

	/**
	 * The rise represents how far above the nominal baseline the text should be rendered.  The {@link #getBaseline()}, {@link #getAscentLine()} and {@link #getDescentLine()} methods already include Rise.
	 * This method is exposed to allow listeners to determine if an explicit rise was involved in the computation of the baseline (this might be useful, for example, for identifying superscript rendering)
	 * @return The Rise for the text draw operation, in user space units (Ts value, scaled to user space)
	 * @since 5.3.3
	 */
	public float getRise(){
		if (gs.rise == 0) return 0; // optimize the common case

		return convertHeightFromTextSpaceToUserSpace(gs.rise);
	}

	/**
	 *
	 * @param width the width, in text space
	 * @return the width in user space
	 * @since 5.3.3
	 */
	private float convertWidthFromTextSpaceToUserSpace(float width){
        LineSegment textSpace = new LineSegment(new Vector(0, 0, 1), new Vector(width, 0, 1));
        LineSegment userSpace = textSpace.transformBy(textToUserSpaceTransformMatrix);
        return userSpace.getLength();
	}

	/**
	 *
	 * @param height the height, in text space
	 * @return the height in user space
	 * @since 5.3.3
	 */
	private float convertHeightFromTextSpaceToUserSpace(float height){
        LineSegment textSpace = new LineSegment(new Vector(0, 0, 1), new Vector(0, height, 1));
        LineSegment userSpace = textSpace.transformBy(textToUserSpaceTransformMatrix);
        return userSpace.getLength();
	}

    /**
     * @return The width, in user space units, of a single space character in the current font
     */
    public float getSingleSpaceWidth(){
    	return convertWidthFromTextSpaceToUserSpace(getUnscaledFontSpaceWidth());
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
     * @return the current fill color.
     */
    public BaseColor getFillColor() {
    	return gs.fillColor;
    }


    /**
     * @return the current stroke color.
     */
    public BaseColor getStrokeColor() {
    	return gs.strokeColor;
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
     * @return          the width of a String in text space units
     */
    private float getStringWidth(String string){
        float totalWidth = 0;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            float w = gs.font.getWidth(c) / 1000.0f;
            float wordSpacing = c == 32 ? gs.wordSpacing : 0f;
            totalWidth += (w * gs.fontSize + gs.characterSpacing + wordSpacing) * gs.horizontalScaling;
        }
        return totalWidth;
    }

    /**
     * Gets the width of a PDF string in text space units
     * @param string        the string that needs measuring
     * @return  the width of a String in text space units
     */
    private float getPdfStringWidth(PdfString string, boolean singleCharString){
        if (singleCharString) {
            float[] widthAndWordSpacing = getWidthAndWordSpacing(string, singleCharString);
            return (widthAndWordSpacing[0] * gs.fontSize + gs.characterSpacing + widthAndWordSpacing[1]) * gs.horizontalScaling;
        } else {
            float totalWidth = 0;
            for (PdfString str : splitString(string)) {
                totalWidth += getPdfStringWidth(str, true);
            }
            return totalWidth;
        }
    }

    /**
     * Provides detail useful if a listener needs access to the position of each individual glyph in the text render operation
     * @return  A list of {@link TextRenderInfo} objects that represent each glyph used in the draw operation. The next effect is if there was a separate Tj opertion for each character in the rendered string
     * @since   5.3.3
     */
    public List<TextRenderInfo> getCharacterRenderInfos(){
        List<TextRenderInfo> rslt = new ArrayList<TextRenderInfo>(string.length());
        PdfString[] strings = splitString(string);
        float totalWidth = 0;
        for (int i = 0; i < strings.length; i++) {
            float[] widthAndWordSpacing = getWidthAndWordSpacing(strings[i], true);
            TextRenderInfo subInfo = new TextRenderInfo(this, strings[i], totalWidth);
            rslt.add(subInfo);
            totalWidth += (widthAndWordSpacing[0] * gs.fontSize + gs.characterSpacing + widthAndWordSpacing[1]) * gs.horizontalScaling;
        }
        for (TextRenderInfo tri : rslt)
            tri.getUnscaledWidth();
        return rslt;
    }

    /**
     * Calculates width and word spacing of a single character PDF string.
     * @param string            a character to calculate width.
     * @param singleCharString  true if PDF string represents single character, false otherwise.
     * @return                  array of 2 items: first item is a character width, second item is a calculated word spacing.
     */
    private float[] getWidthAndWordSpacing(PdfString string, boolean singleCharString) {
        if (singleCharString == false)
            throw new UnsupportedOperationException();
        float[] result = new float[2];
        String decoded = decode(string);
        result[0] = gs.font.getWidth(getCharCode(decoded)) / 1000.0f;
        result[1] = decoded.equals(" ") ? gs.wordSpacing : 0;
        return result;
    }

    /**
     * Decodes a PdfString (which will contain glyph ids encoded in the font's encoding)
     * based on the active font, and determine the unicode equivalent
     * @param in	the String that needs to be encoded
     * @return	    the encoded String
     */
    private String decode(PdfString in){
        byte[] bytes = in.getBytes();
        return gs.font.decode(bytes, 0, bytes.length);
    }

    /**
     * Converts a single character string to char code.
     *
     * @param string single character string to convert to.
     * @return char code.
     */
    private int getCharCode(String string) {
        try {
            byte[] b = string.getBytes("UTF-16BE");
            int value = 0;
            for (int i = 0; i < b.length - 1; i++) {
                value += b[i] & 0xff;
                value <<= 8;
            }
            value += b[b.length - 1] & 0xff;
            return value;
        } catch (UnsupportedEncodingException e) {
        }
        return 0;
    }

    /**
     * Split PDF string into array of single character PDF strings.
     * @param string    PDF string to be splitted.
     * @return          splitted PDF string.
     */
    private PdfString[] splitString(PdfString string) {
        List<PdfString> strings = new ArrayList<PdfString>();
        String stringValue = string.toString();
        for (int i = 0; i < stringValue.length(); i++) {
            PdfString newString = new PdfString(stringValue.substring(i, i + 1), string.getEncoding());
            String text = decode(newString);
            if (text.length() == 0 && i < stringValue.length() - 1) {
                newString = new PdfString(stringValue.substring(i, i + 2), string.getEncoding());
                i++;
            }
            strings.add(newString);
        }
        return strings.toArray(new PdfString[strings.size()]);
    }

}
