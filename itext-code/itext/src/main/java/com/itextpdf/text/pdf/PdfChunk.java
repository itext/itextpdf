/*
 * $Id: PdfChunk.java 6086 2013-11-26 11:19:45Z pavel-alay $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * A <CODE>PdfChunk</CODE> is the PDF translation of a <CODE>Chunk</CODE>.
 * <P>
 * A <CODE>PdfChunk</CODE> is a <CODE>PdfString</CODE> in a certain
 * <CODE>PdfFont</CODE> and <CODE>BaseColor</CODE>.
 *
 * @see		PdfString
 * @see		com.itextpdf.text.Chunk
 * @see		com.itextpdf.text.Font
 */

public class PdfChunk {

    private static final char singleSpace[] = {' '};
    private static final PdfChunk thisChunk[] = new PdfChunk[1];
    private static final float ITALIC_ANGLE = 0.21256f;
/** The allowed attributes in variable <CODE>attributes</CODE>. */
    private static final HashSet<String> keysAttributes = new HashSet<String>();

/** The allowed attributes in variable <CODE>noStroke</CODE>. */
    private static final HashSet<String> keysNoStroke = new HashSet<String>();
    private static final String TABSTOP = "TABSTOP";

    static {
        keysAttributes.add(Chunk.ACTION);
        keysAttributes.add(Chunk.UNDERLINE);
        keysAttributes.add(Chunk.REMOTEGOTO);
        keysAttributes.add(Chunk.LOCALGOTO);
        keysAttributes.add(Chunk.LOCALDESTINATION);
        keysAttributes.add(Chunk.GENERICTAG);
        keysAttributes.add(Chunk.NEWPAGE);
        keysAttributes.add(Chunk.IMAGE);
        keysAttributes.add(Chunk.BACKGROUND);
        keysAttributes.add(Chunk.PDFANNOTATION);
        keysAttributes.add(Chunk.SKEW);
        keysAttributes.add(Chunk.HSCALE);
        keysAttributes.add(Chunk.SEPARATOR);
        keysAttributes.add(Chunk.TAB);
        keysAttributes.add(Chunk.TABSETTINGS);
        keysAttributes.add(Chunk.CHAR_SPACING);
        keysAttributes.add(Chunk.WORD_SPACING);
        keysAttributes.add(Chunk.LINEHEIGHT);
        keysNoStroke.add(Chunk.SUBSUPSCRIPT);
        keysNoStroke.add(Chunk.SPLITCHARACTER);
        keysNoStroke.add(Chunk.HYPHENATION);
        keysNoStroke.add(Chunk.TEXTRENDERMODE);
    }

    // membervariables

    /** The value of this object. */
    protected String value = PdfObject.NOTHING;

    /** The encoding. */
    protected String encoding = BaseFont.WINANSI;


/** The font for this <CODE>PdfChunk</CODE>. */
    protected PdfFont font;

    protected BaseFont baseFont;

    protected SplitCharacter splitCharacter;
/**
 * Metric attributes.
 * <P>
 * This attributes require the measurement of characters widths when rendering
 * such as underline.
 */
    protected HashMap<String, Object> attributes = new HashMap<String, Object>();

/**
 * Non metric attributes.
 * <P>
 * This attributes do not require the measurement of characters widths when rendering
 * such as BaseColor.
 */
    protected HashMap<String, Object> noStroke = new HashMap<String, Object>();

/** <CODE>true</CODE> if the chunk split was cause by a newline. */
    protected boolean newlineSplit;

/** The image in this <CODE>PdfChunk</CODE>, if it has one */
    protected Image image;
    protected float imageScalePercentage = 1.0f;

/** The offset in the x direction for the image */
    protected float offsetX;

/** The offset in the y direction for the image */
    protected float offsetY;

/** Indicates if the height and offset of the Image has to be taken into account */
    protected boolean changeLeading = false;
    
/** The leading that can overrule the existing leading. */
    protected float leading = 0;

    protected IAccessibleElement accessibleElement = null;

    public static final float UNDERLINE_THICKNESS = 1f / 15;
    public static final float UNDERLINE_OFFSET = -1f / 3;
    // constructors

/**
 * Constructs a <CODE>PdfChunk</CODE>-object.
 *
 * @param string the content of the <CODE>PdfChunk</CODE>-object
 * @param other Chunk with the same style you want for the new Chunk
 */

    PdfChunk(String string, PdfChunk other) {
        thisChunk[0] = this;
        value = string;
        this.font = other.font;
        this.attributes = other.attributes;
        this.noStroke = other.noStroke;
        this.baseFont = other.baseFont;
        this.changeLeading = other.changeLeading;
        this.leading = other.leading;
        Object obj[] = (Object[])attributes.get(Chunk.IMAGE);
        if (obj == null)
            image = null;
        else {
            image = (Image)obj[0];
            offsetX = ((Float)obj[1]).floatValue();
            offsetY = ((Float)obj[2]).floatValue();
            changeLeading = ((Boolean)obj[3]).booleanValue();
        }
        encoding = font.getFont().getEncoding();
        splitCharacter = (SplitCharacter)noStroke.get(Chunk.SPLITCHARACTER);
        if (splitCharacter == null)
            splitCharacter = DefaultSplitCharacter.DEFAULT;
        accessibleElement = other.accessibleElement;
    }

/**
 * Constructs a <CODE>PdfChunk</CODE>-object.
 *
 * @param chunk the original <CODE>Chunk</CODE>-object
 * @param action the <CODE>PdfAction</CODE> if the <CODE>Chunk</CODE> comes from an <CODE>Anchor</CODE>
 */

    PdfChunk(Chunk chunk, PdfAction action) {
        thisChunk[0] = this;
        value = chunk.getContent();

        Font f = chunk.getFont();
        float size = f.getSize();
        if (size == Font.UNDEFINED)
            size = 12;
        baseFont = f.getBaseFont();
        int style = f.getStyle();
        if (style == Font.UNDEFINED) {
            style = Font.NORMAL;
        }
        if (baseFont == null) {
            // translation of the font-family to a PDF font-family
            baseFont = f.getCalculatedBaseFont(false);
        }
        else {
            // bold simulation
            if ((style & Font.BOLD) != 0)
                attributes.put(Chunk.TEXTRENDERMODE, new Object[]{Integer.valueOf(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE), new Float(size / 30f), null});
            // italic simulation
            if ((style & Font.ITALIC) != 0)
                attributes.put(Chunk.SKEW, new float[]{0, ITALIC_ANGLE});
        }
        font = new PdfFont(baseFont, size);
        // other style possibilities
        HashMap<String, Object> attr = chunk.getAttributes();
        if (attr != null) {
            for (Map.Entry<String, Object>entry: attr.entrySet()) {
                String name = entry.getKey();
                if (keysAttributes.contains(name)) {
                    attributes.put(name, entry.getValue());
                }
                else if (keysNoStroke.contains(name)) {
                    noStroke.put(name, entry.getValue());
                }
            }
            if ("".equals(attr.get(Chunk.GENERICTAG))) {
                attributes.put(Chunk.GENERICTAG, chunk.getContent());
            }
        }
        if (f.isUnderlined()) {
            Object obj[] = {null, new float[]{0, UNDERLINE_THICKNESS, 0, UNDERLINE_OFFSET, 0}};
            Object unders[][] = Utilities.addToArray((Object[][])attributes.get(Chunk.UNDERLINE), obj);
            attributes.put(Chunk.UNDERLINE, unders);
        }
        if (f.isStrikethru()) {
            Object obj[] = {null, new float[]{0, 1f / 15, 0, 1f / 3, 0}};
            Object unders[][] = Utilities.addToArray((Object[][])attributes.get(Chunk.UNDERLINE), obj);
            attributes.put(Chunk.UNDERLINE, unders);
        }
        if (action != null)
            attributes.put(Chunk.ACTION, action);
        // the color can't be stored in a PdfFont
        noStroke.put(Chunk.COLOR, f.getColor());
        noStroke.put(Chunk.ENCODING, font.getFont().getEncoding());

        Float lh = (Float)attributes.get(Chunk.LINEHEIGHT);
        if (lh != null) {
        	changeLeading = true;
        	leading = lh;
        }
        
        Object[] obj = (Object[])attributes.get(Chunk.IMAGE);
        if (obj == null) {
            image = null;
        }
        else {
            attributes.remove(Chunk.HSCALE); // images are scaled in other ways
            image = (Image)obj[0];
            offsetX = ((Float)obj[1]).floatValue();
            offsetY = ((Float)obj[2]).floatValue();
            changeLeading = ((Boolean)obj[3]).booleanValue();
        }
        Float hs = (Float)attributes.get(Chunk.HSCALE);
        if (hs != null)
            font.setHorizontalScaling(hs.floatValue());
        encoding = font.getFont().getEncoding();
        splitCharacter = (SplitCharacter)noStroke.get(Chunk.SPLITCHARACTER);
        if (splitCharacter == null)
            splitCharacter = DefaultSplitCharacter.DEFAULT;
        accessibleElement = chunk;
    }

    /**
     * Constructs a <CODE>PdfChunk</CODE>-object.
     *
     * @param chunk     the original <CODE>Chunk</CODE>-object
     * @param action    the <CODE>PdfAction</CODE> if the <CODE>Chunk</CODE> comes from an <CODE>Anchor</CODE>
     * @param tabSettings  the Phrase tab settings
     */
    PdfChunk(Chunk chunk, PdfAction action, TabSettings tabSettings) {
        this(chunk, action);
        if (tabSettings != null && attributes.get(Chunk.TABSETTINGS) == null)
            attributes.put(Chunk.TABSETTINGS, tabSettings);
    }

    // methods

    /** Gets the Unicode equivalent to a CID.
     * The (inexistent) CID <FF00> is translated as '\n'.
     * It has only meaning with CJK fonts with Identity encoding.
     * @param c the CID code
     * @return the Unicode equivalent
     */
    public int getUnicodeEquivalent(int c) {
        return baseFont.getUnicodeEquivalent(c);
    }

    protected int getWord(String text, int start) {
        int len = text.length();
        while (start < len) {
            if (!Character.isLetter(text.charAt(start)))
                break;
            ++start;
        }
        return start;
    }

/**
 * Splits this <CODE>PdfChunk</CODE> if it's too long for the given width.
 * <P>
 * Returns <VAR>null</VAR> if the <CODE>PdfChunk</CODE> wasn't truncated.
 *
 * @param		width		a given width
 * @return		the <CODE>PdfChunk</CODE> that doesn't fit into the width.
 */

    PdfChunk split(float width) {
        newlineSplit = false;
        if (image != null) {
            if (image.getScaledWidth() > width) {
                PdfChunk pc = new PdfChunk(Chunk.OBJECT_REPLACEMENT_CHARACTER, this);
                value = "";
                attributes = new HashMap<String, Object>();
                image = null;
                font = PdfFont.getDefaultFont();
                return pc;
            }
            else
                return null;
        }
        HyphenationEvent hyphenationEvent = (HyphenationEvent)noStroke.get(Chunk.HYPHENATION);
        int currentPosition = 0;
        int splitPosition = -1;
        float currentWidth = 0;

        // loop over all the characters of a string
        // or until the totalWidth is reached
        int lastSpace = -1;
        float lastSpaceWidth = 0;
        int length = value.length();
        char valueArray[] = value.toCharArray();
        char character = 0;
        BaseFont ft = font.getFont();
        boolean surrogate = false;
        if (ft.getFontType() == BaseFont.FONT_TYPE_CJK && ft.getUnicodeEquivalent(' ') != ' ') {
            while (currentPosition < length) {
                // the width of every character is added to the currentWidth
                char cidChar = valueArray[currentPosition];
                character = (char)ft.getUnicodeEquivalent(cidChar);
                // if a newLine or carriageReturn is encountered
                if (character == '\n') {
                    newlineSplit = true;
                    String returnValue = value.substring(currentPosition + 1);
                    value = value.substring(0, currentPosition);
                    if (value.length() < 1) {
                        value = "\u0001";
                    }
                    PdfChunk pc = new PdfChunk(returnValue, this);
                    return pc;
                }
                currentWidth += getCharWidth(cidChar);
                if (character == ' ') {
                    lastSpace = currentPosition + 1;
                    lastSpaceWidth = currentWidth;
                }
                if (currentWidth > width)
                    break;
                // if a split-character is encountered, the splitPosition is altered
                if (splitCharacter.isSplitCharacter(0, currentPosition, length, valueArray, thisChunk))
                    splitPosition = currentPosition + 1;
                currentPosition++;
            }
        }
        else {
            while (currentPosition < length) {
                // the width of every character is added to the currentWidth
                character = valueArray[currentPosition];
                // if a newLine or carriageReturn is encountered
                if (character == '\r' || character == '\n') {
                    newlineSplit = true;
                    int inc = 1;
                    if (character == '\r' && currentPosition + 1 < length && valueArray[currentPosition + 1] == '\n')
                        inc = 2;
                    String returnValue = value.substring(currentPosition + inc);
                    value = value.substring(0, currentPosition);
                    if (value.length() < 1) {
                        value = " ";
                    }
                    PdfChunk pc = new PdfChunk(returnValue, this);
                    return pc;
                }
                surrogate = Utilities.isSurrogatePair(valueArray, currentPosition);
                if (surrogate)
                    currentWidth += getCharWidth(Utilities.convertToUtf32(valueArray[currentPosition], valueArray[currentPosition + 1]));
                else
                    currentWidth += getCharWidth(character);
                if (character == ' ') {
                    lastSpace = currentPosition + 1;
                    lastSpaceWidth = currentWidth;
                }
                if (surrogate)
                    currentPosition++;
                if (currentWidth > width)
                    break;
                // if a split-character is encountered, the splitPosition is altered
                if (splitCharacter.isSplitCharacter(0, currentPosition, length, valueArray, null))
                    splitPosition = currentPosition + 1;
                currentPosition++;
            }
        }

        // if all the characters fit in the total width, null is returned (there is no overflow)
        if (currentPosition == length) {
            return null;
        }
        // otherwise, the string has to be truncated
        if (splitPosition < 0) {
            String returnValue = value;
            value = "";
            PdfChunk pc = new PdfChunk(returnValue, this);
            return pc;
        }
        if (lastSpace > splitPosition && splitCharacter.isSplitCharacter(0, 0, 1, singleSpace, null))
            splitPosition = lastSpace;
        if (hyphenationEvent != null && lastSpace >= 0 && lastSpace < currentPosition) {
            int wordIdx = getWord(value, lastSpace);
            if (wordIdx > lastSpace) {
                String pre = hyphenationEvent.getHyphenatedWordPre(value.substring(lastSpace, wordIdx), font.getFont(), font.size(), width - lastSpaceWidth);
                String post = hyphenationEvent.getHyphenatedWordPost();
                if (pre.length() > 0) {
                    String returnValue = post + value.substring(wordIdx);
                    value = trim(value.substring(0, lastSpace) + pre);
                    PdfChunk pc = new PdfChunk(returnValue, this);
                    return pc;
                }
            }
        }
        String returnValue = value.substring(splitPosition);
        value = trim(value.substring(0, splitPosition));
        PdfChunk pc = new PdfChunk(returnValue, this);
        return pc;
    }

    /**
     * Truncates this <CODE>PdfChunk</CODE> if it's too long for the given width.
     * <P>
     * Returns <VAR>null</VAR> if the <CODE>PdfChunk</CODE> wasn't truncated.
     *
     * @param		width		a given width
     * @return		the <CODE>PdfChunk</CODE> that doesn't fit into the width.
     */
    PdfChunk truncate(float width) {
        if (image != null) {
            if (image.getScaledWidth() > width) {
            	// Image does not fit the line, resize if requested
            	if (image.isScaleToFitLineWhenOverflow()) {
            		//float scalePercent = width / image.getWidth() * 100;
            		//image.scalePercent(scalePercent);
            		this.setImageScalePercentage(width / image.getWidth());
            		return null;
            	}
                PdfChunk pc = new PdfChunk("", this);
                value = "";
                attributes.remove(Chunk.IMAGE);
                image = null;
                font = PdfFont.getDefaultFont();
                return pc;
            }
            else
                return null;
        }

        int currentPosition = 0;
        float currentWidth = 0;

        // it's no use trying to split if there isn't even enough place for a space
        if (width < font.width()) {
            String returnValue = value.substring(1);
            value = value.substring(0, 1);
            PdfChunk pc = new PdfChunk(returnValue, this);
            return pc;
        }

        // loop over all the characters of a string
        // or until the totalWidth is reached
        int length = value.length();
        boolean surrogate = false;
        while (currentPosition < length) {
            // the width of every character is added to the currentWidth
            surrogate = Utilities.isSurrogatePair(value, currentPosition);
            if (surrogate)
                currentWidth += getCharWidth(Utilities.convertToUtf32(value, currentPosition));
            else
                currentWidth += getCharWidth(value.charAt(currentPosition));
            if (currentWidth > width)
                break;
            if (surrogate)
                currentPosition++;
            currentPosition++;
        }

        // if all the characters fit in the total width, null is returned (there is no overflow)
        if (currentPosition == length) {
            return null;
        }

        // otherwise, the string has to be truncated
        //currentPosition -= 2;
        // we have to chop off minimum 1 character from the chunk
        if (currentPosition == 0) {
            currentPosition = 1;
            if (surrogate)
                ++currentPosition;
        }
        String returnValue = value.substring(currentPosition);
        value = value.substring(0, currentPosition);
        PdfChunk pc = new PdfChunk(returnValue, this);
        return pc;
    }

    // methods to retrieve the membervariables

/**
 * Returns the font of this <CODE>Chunk</CODE>.
 *
 * @return	a <CODE>PdfFont</CODE>
 */

    PdfFont font() {
        return font;
    }

/**
 * Returns the color of this <CODE>Chunk</CODE>.
 *
 * @return	a <CODE>BaseColor</CODE>
 */

    BaseColor color() {
        return (BaseColor)noStroke.get(Chunk.COLOR);
    }

/**
 * Returns the width of this <CODE>PdfChunk</CODE>.
 *
 * @return	a width
 */

    float width() {
        return width(value);
    }

    float width(String str) {
        if (isAttribute(Chunk.SEPARATOR)) {
            return 0;
        }
        if (isImage()) {
            return getImageWidth();
        }

        float width = font.width(str);
    	
        if (isAttribute(Chunk.CHAR_SPACING)) {
            Float cs = (Float) getAttribute(Chunk.CHAR_SPACING);
            width += str.length() * cs.floatValue();
        }
        if (isAttribute(Chunk.WORD_SPACING)) {
            int numberOfSpaces = 0;
            int idx = -1;
            while ((idx = str.indexOf(' ', idx + 1)) >= 0)
                ++numberOfSpaces;
        	Float ws = (Float) getAttribute(Chunk.WORD_SPACING);
            width += numberOfSpaces * ws;
		}
        return width;
    }

    float height() {
        if (isImage()) {
            return getImageHeight();
        } else {
            return font.size();
        }
    }

/**
 * Checks if the <CODE>PdfChunk</CODE> split was caused by a newline.
 * @return <CODE>true</CODE> if the <CODE>PdfChunk</CODE> split was caused by a newline.
 */

    public boolean isNewlineSplit()
    {
        return newlineSplit;
    }

/**
 * Gets the width of the <CODE>PdfChunk</CODE> taking into account the
 * extra character and word spacing.
 * @param charSpacing the extra character spacing
 * @param wordSpacing the extra word spacing
 * @return the calculated width
 */

    public float getWidthCorrected(float charSpacing, float wordSpacing)
    {
        if (image != null) {
            return image.getScaledWidth() + charSpacing;
        }
        int numberOfSpaces = 0;
        int idx = -1;
        while ((idx = value.indexOf(' ', idx + 1)) >= 0)
            ++numberOfSpaces;
        return font.width(value) + value.length() * charSpacing + numberOfSpaces * wordSpacing;
    }

    /**
     * Gets the text displacement relative to the baseline.
     * @return a displacement in points
     */
    public float getTextRise() {
    	Float f = (Float) getAttribute(Chunk.SUBSUPSCRIPT);
    	if (f != null) {
    		return f.floatValue();
    	}
    	return 0.0f;
    }

/**
 * Trims the last space.
 * @return the width of the space trimmed, otherwise 0
 */

    public float trimLastSpace()
    {
        BaseFont ft = font.getFont();
        if (ft.getFontType() == BaseFont.FONT_TYPE_CJK && ft.getUnicodeEquivalent(' ') != ' ') {
            if (value.length() > 1 && value.endsWith("\u0001")) {
                value = value.substring(0, value.length() - 1);
                return font.width('\u0001');
            }
        }
        else {
            if (value.length() > 1 && value.endsWith(" ")) {
                value = value.substring(0, value.length() - 1);
                return font.width(' ');
            }
        }
        return 0;
    }
    public float trimFirstSpace()
    {
        BaseFont ft = font.getFont();
        if (ft.getFontType() == BaseFont.FONT_TYPE_CJK && ft.getUnicodeEquivalent(' ') != ' ') {
            if (value.length() > 1 && value.startsWith("\u0001")) {
                value = value.substring(1);
                return font.width('\u0001');
            }
        }
        else {
            if (value.length() > 1 && value.startsWith(" ")) {
                value = value.substring(1);
                return font.width(' ');
            }
        }
        return 0;
    }

/**
 * Gets an attribute. The search is made in <CODE>attributes</CODE>
 * and <CODE>noStroke</CODE>.
 * @param name the attribute key
 * @return the attribute value or null if not found
 */

    Object getAttribute(String name)
    {
        if (attributes.containsKey(name))
            return attributes.get(name);
        return noStroke.get(name);
    }

/**
 *Checks if the attribute exists.
 * @param name the attribute key
 * @return <CODE>true</CODE> if the attribute exists
 */

    boolean isAttribute(String name)
    {
        if (attributes.containsKey(name))
            return true;
        return noStroke.containsKey(name);
    }

/**
 * Checks if this <CODE>PdfChunk</CODE> needs some special metrics handling.
 * @return <CODE>true</CODE> if this <CODE>PdfChunk</CODE> needs some special metrics handling.
 */

    boolean isStroked()
    {
        return !attributes.isEmpty();
    }

    /**
     * Checks if this <CODE>PdfChunk</CODE> is a Separator Chunk.
     * @return	true if this chunk is a separator.
     * @since	2.1.2
     */
    boolean isSeparator() {
    	return isAttribute(Chunk.SEPARATOR);
    }

    /**
     * Checks if this <CODE>PdfChunk</CODE> is a horizontal Separator Chunk.
     * @return	true if this chunk is a horizontal separator.
     * @since	2.1.2
     */
    boolean isHorizontalSeparator() {
    	if (isAttribute(Chunk.SEPARATOR)) {
    		Object[] o = (Object[])getAttribute(Chunk.SEPARATOR);
    		return !((Boolean)o[1]).booleanValue();
    	}
    	return false;
    }

    /**
     * Checks if this <CODE>PdfChunk</CODE> is a tab Chunk.
     * @return	true if this chunk is a separator.
     * @since	2.1.2
     */
    boolean isTab() {
    	return isAttribute(Chunk.TAB);
    }

    /**
     * Correction for the tab position based on the left starting position.
     * @param	newValue	the new value for the left X.
     * @since	2.1.2
     */
    @Deprecated
    void adjustLeft(float newValue) {
    	Object[] o = (Object[])attributes.get(Chunk.TAB);
    	if (o != null) {
    		attributes.put(Chunk.TAB, new Object[]{o[0], o[1], o[2], new Float(newValue)});
    	}
    }

    static TabStop getTabStop(PdfChunk tab, float tabPosition) {
        TabStop tabStop = null;
        Object[] o = (Object[])tab.attributes.get(Chunk.TAB);
        if (o != null) {
            Float tabInterval = (Float) o[0];
            if (Float.isNaN(tabInterval)) {
                tabStop = TabSettings.getTabStopNewInstance(tabPosition, (TabSettings) tab.attributes.get(Chunk.TABSETTINGS));
            } else {
                tabStop = TabStop.newInstance(tabPosition, tabInterval);
            }
        }
        return tabStop;
    }

    TabStop getTabStop() {
        return (TabStop)attributes.get(TABSTOP);
    }
    
    void setTabStop(TabStop tabStop) {
        attributes.put(TABSTOP, tabStop);
    }

/**
 * Checks if there is an image in the <CODE>PdfChunk</CODE>.
 * @return <CODE>true</CODE> if an image is present
 */

    boolean isImage()
    {
        return image != null;
    }

/**
 * Gets the image in the <CODE>PdfChunk</CODE>.
 * @return the image or <CODE>null</CODE>
 */

    Image getImage()
    {
        return image;
    }
    
    float getImageHeight() {
    	return image.getScaledHeight() * imageScalePercentage;
    }
    
    float getImageWidth() {
    	return image.getScaledWidth() * imageScalePercentage;
    }
    
    /**
     * Returns a scalePercentage in case the image needs to be scaled.
     * @return the imageScalePercentage
     */
    public float getImageScalePercentage() {
    	return imageScalePercentage;
    }

    /**
     * Sets a scale percentage in case the image needs to be scaled.
     * @param imageScalePercentage the imageScalePercentage to set
     */
    public void setImageScalePercentage(float imageScalePercentage) {
    	this.imageScalePercentage = imageScalePercentage;
    }

/**
 * Sets the image offset in the x direction
 * @param  offsetX the image offset in the x direction
 */

    void setImageOffsetX(float offsetX)
    {
        this.offsetX = offsetX;
    }

/**
 * Gets the image offset in the x direction
 * @return the image offset in the x direction
 */

    float getImageOffsetX()
    {
        return offsetX;
    }

/**
 * Sets the image offset in the y direction
 * @param  offsetY the image offset in the y direction
 */

    void setImageOffsetY(float offsetY)
    {
        this.offsetY = offsetY;
    }

/**
 * Gets the image offset in the y direction
 * @return Gets the image offset in the y direction
 */

    float getImageOffsetY()
    {
        return offsetY;
    }

/**
 * sets the value.
 * @param value content of the Chunk
 */

    void setValue(String value)
    {
        this.value = value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Tells you if this string is in Chinese, Japanese, Korean or Identity-H.
     * @return true if the Chunk has a special encoding
     */

    boolean isSpecialEncoding() {
        return encoding.equals(CJKFont.CJK_ENCODING) || encoding.equals(BaseFont.IDENTITY_H);
    }

    /**
     * Gets the encoding of this string.
     *
     * @return		a <CODE>String</CODE>
     */

    String getEncoding() {
        return encoding;
    }

    int length() {
        return value.length();
    }

    int lengthUtf32() {
        if (!BaseFont.IDENTITY_H.equals(encoding))
            return value.length();
        int total = 0;
        int len = value.length();
        for (int k = 0; k < len; ++k) {
            if (Utilities.isSurrogateHigh(value.charAt(k)))
                ++k;
            ++total;
        }
        return total;
    }

    boolean isExtSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
        return splitCharacter.isSplitCharacter(start, current, end, cc, ck);
    }

/**
 * Removes all the <VAR>' '</VAR> and <VAR>'-'</VAR>-characters on the right of a <CODE>String</CODE>.
 * <P>
 * @param	string		the <CODE>String<CODE> that has to be trimmed.
 * @return	the trimmed <CODE>String</CODE>
 */
    String trim(String string) {
        BaseFont ft = font.getFont();
        if (ft.getFontType() == BaseFont.FONT_TYPE_CJK && ft.getUnicodeEquivalent(' ') != ' ') {
            while (string.endsWith("\u0001")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        else {
            while (string.endsWith(" ") || string.endsWith("\t")) {
                string = string.substring(0, string.length() - 1);
            }
        }
        return string;
    }

    public boolean changeLeading() {
        return changeLeading;
    }
    
    public float getLeading() {
    	return leading;
    }

    float getCharWidth(int c) {
        if (noPrint(c))
            return 0;
        if (isAttribute(Chunk.CHAR_SPACING)) {
        	Float cs = (Float) getAttribute(Chunk.CHAR_SPACING);
			return font.width(c) + cs.floatValue() * font.getHorizontalScaling();
		}
        if (isImage()) {
            return getImageWidth();
        }
        return font.width(c);
    }

    public static boolean noPrint(int c) {
        return c >= 0x200b && c <= 0x200f || c >= 0x202a && c <= 0x202e;
    }



}
