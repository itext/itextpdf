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

package com.lowagie.text.pdf;

import java.awt.Color;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.SplitCharacter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A <CODE>PdfChunk</CODE> is the PDF translation of a <CODE>Chunk</CODE>.
 * <P>
 * A <CODE>PdfChunk</CODE> is a <CODE>PdfString</CODE> in a certain
 * <CODE>PdfFont</CODE> and <CODE>Color</CODE>.
 *
 * @see		PdfString
 * @see		PdfFont
 * @see		com.lowagie.text.Chunk
 * @see		com.lowagie.text.Font
 */

class PdfChunk extends PdfString implements SplitCharacter{
    
/** The allowed attributes in variable <CODE>attributes</CODE>. */
    private static final HashMap keysAttributes = new HashMap();
    
/** The allowed attributes in variable <CODE>noStroke</CODE>. */
    private static final HashMap keysNoStroke = new HashMap();
    
    static {
        keysAttributes.put(Chunk.LINK, null);
        keysAttributes.put(Chunk.STRIKETHRU, null);
        keysAttributes.put(Chunk.UNDERLINE, null);
        keysAttributes.put(Chunk.REMOTEGOTO, null);
        keysAttributes.put(Chunk.LOCALGOTO, null);
        keysAttributes.put(Chunk.LOCALDESTINATION, null);
        keysAttributes.put(Chunk.GENERICTAG, null);
        keysAttributes.put(Chunk.NEWPAGE, null);
        keysAttributes.put(Chunk.IMAGE, null);
        keysNoStroke.put(Chunk.SUBSUPSCRIPT, null);
        keysNoStroke.put(Chunk.SPLITCHARACTER, null);
    }
    
    // membervariables
    
/** The font for this <CODE>PdfChunk</CODE>. */
    protected PdfFont font;
    
/**
 * Metric attributes.
 * <P>
 * This attributes require the mesurement of characters widths when rendering
 * such as underline.
 */
    protected HashMap attributes = new HashMap();
    
/**
 * Non metric attributes.
 * <P>
 * This attributes do not require the mesurement of characters widths when rendering
 * such as Color.
 */
    protected HashMap noStroke = new HashMap();
    
/** <CODE>true</CODE> if the chunk split was cause by a newline. */
    protected boolean newlineSplit;
    
/** The image in this <CODE>PdfChunk</CODE>, if it has one */
    protected Image image;
    
/** The offset in the x direction for the image */
    protected float offsetX;
    
/** The offset in the y direction for the image */
    protected float offsetY;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfChunk</CODE>-object.
 *
 * @param string the content of the <CODE>PdfChunk</CODE>-object
 * @param font the <CODE>PdfFont</CODE>
 * @param attributes the metrics attributes
 * @param noStroke the non metric attributes
 */
    
    private PdfChunk(String string, PdfFont font, HashMap attributes, HashMap noStroke) {
        super(string);
        this.font = font;
        this.attributes = attributes;
        this.noStroke = noStroke;
        Object obj[] = (Object[])attributes.get(Chunk.IMAGE);
        if (obj == null)
            image = null;
        else {
            image = (Image)obj[0];
            offsetX = ((Float)obj[1]).floatValue();
            offsetY = ((Float)obj[2]).floatValue();
        }
        encoding = font.getFont().getEncoding();
    }
    
/**
 * Constructs a <CODE>PdfChunk</CODE>-object.
 *
 * @param chunk the original <CODE>Chunk</CODE>-object
 * @param action the <CODE>PdfAction</CODE> if the <CODE>Chunk</CODE> comes from an <CODE>Anchor</CODE>
 */
    
    PdfChunk(Chunk chunk, PdfAction action) {
        super(chunk.content());
        
        Font f = chunk.font();
        float size = f.size();
        if (size == Font.UNDEFINED)
            size = 12;
        BaseFont bf = f.getBaseFont();
        if (bf == null) {
            // translation of the font-family to a PDF font-family
            int family;
            int style = f.style();
            if (style == Font.UNDEFINED) {
                style = Font.NORMAL;
            }
            switch(f.family()) {
                case Font.COURIER:
                    switch(style & Font.BOLDITALIC) {
                        case Font.BOLD:
                            family = PdfFont.COURIER_BOLD;
                            break;
                        case Font.ITALIC:
                            family = PdfFont.COURIER_OBLIQUE;
                            break;
                        case Font.BOLDITALIC:
                            family = PdfFont.COURIER_BOLDOBLIQUE;
                            break;
                            default:
                        case Font.NORMAL:
                            family = PdfFont.COURIER;
                            break;
                    }
                    break;
                case Font.TIMES_NEW_ROMAN:
                    switch(style & Font.BOLDITALIC) {
                        case Font.BOLD:
                            family = PdfFont.TIMES_BOLD;
                            break;
                        case Font.ITALIC:
                            family = PdfFont.TIMES_ITALIC;
                            break;
                        case Font.BOLDITALIC:
                            family = PdfFont.TIMES_BOLDITALIC;
                            break;
                            default:
                        case Font.NORMAL:
                            family = PdfFont.TIMES_ROMAN;
                            break;
                    }
                    break;
                case Font.SYMBOL:
                    family = PdfFont.SYMBOL;
                    break;
                case Font.ZAPFDINGBATS:
                    family = PdfFont.ZAPFDINGBATS;
                    break;
                    default:
                case Font.HELVETICA:
                    switch(style & Font.BOLDITALIC) {
                        case Font.BOLD:
                            family = PdfFont.HELVETICA_BOLD;
                            break;
                        case Font.ITALIC:
                            family = PdfFont.HELVETICA_OBLIQUE;
                            break;
                        case Font.BOLDITALIC:
                            family = PdfFont.HELVETICA_BOLDOBLIQUE;
                            break;
                            default:
                        case Font.NORMAL:
                            family = PdfFont.HELVETICA;
                            break;
                    }
                    break;
            }
            // creation of the PdfFont with the right size
            font = new PdfFont(family, size);
        }
        else
            font = new PdfFont(bf, size);
        // other style possibilities
        HashMap attr = chunk.getAttributes();
        if (attr != null) {
            for (Iterator i = attr.keySet().iterator(); i.hasNext();) {
                Object name = i.next();
                if (keysAttributes.containsKey(name)) {
                    attributes.put(name, attr.get(name));
                }
                else if (keysNoStroke.containsKey(name)) {
                    noStroke.put(name, attr.get(name));
                }
            }
            if ("".equals(attr.get(Chunk.GENERICTAG))) {
                attributes.put(Chunk.GENERICTAG, chunk.content());
            }
        }
        if (f.isUnderlined())
            attributes.put(Chunk.UNDERLINE, null);
        if (f.isStrikethru())
            attributes.put(Chunk.STRIKETHRU, null);
        if (action != null)
            attributes.put(Chunk.LINK, action);
        // the color can't be stored in a PdfFont
        noStroke.put(Chunk.COLOR, f.color());
        noStroke.put(Chunk.ENCODING, font.getFont().getEncoding());
        Object obj[] = (Object[])attributes.get(Chunk.IMAGE);
        if (obj == null)
            image = null;
        else {
            image = (Image)obj[0];
            offsetX = ((Float)obj[1]).floatValue();
            offsetY = ((Float)obj[2]).floatValue();
        }
        font.setImage(image);
        encoding = font.getFont().getEncoding();
    }
    
    // methods
    
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
            if (image.scaledWidth() > width) {
                PdfChunk pc = new PdfChunk("*", font, attributes, noStroke);
                value = "";
                attributes = new HashMap();
                image = null;
                font = new PdfFont(PdfFont.HELVETICA, 12);
                return pc;
            }
            else
                return null;
        }
        SplitCharacter splitCharacter = (SplitCharacter)noStroke.get(Chunk.SPLITCHARACTER);
        if (splitCharacter == null)
            splitCharacter = this;
        int currentPosition = 0;
        int splitPosition = -1;
        float currentWidth = 0;
        
        // loop over all the characters of a string
        // or until the totalWidth is reached
        int length = value.length();
        char character = 0;
        while (currentPosition < length) {
            // the width of every character is added to the currentWidth
            character = value.charAt(currentPosition);
            // if a newLine or carriageReturn is encountered
            if (character == '\r' || character == '\n') {
                newlineSplit = true;
                int inc = 1;
                if (character == '\r' && currentPosition + 1 < length && value.charAt(currentPosition) == '\n')
                    inc = 2;
                String returnValue = value.substring(currentPosition + inc);
                value = value.substring(0, currentPosition);
                if (value.length() < 1) {
                    value = " ";
                }
                setContent(value);
                PdfChunk pc = new PdfChunk(returnValue, font, attributes, noStroke);
                return pc;
            }
            currentWidth += font.width(character);
            if (currentWidth > width)
                break;
            // if a split-character is encountered, the splitPosition is altered
            if (splitCharacter.isSplitCharacter(character))
                splitPosition = currentPosition + 1;
            currentPosition++;
        }
        
        // if all the characters fit in the total width, null is returned (there is no overflow)
        if (currentPosition == length) {
            return null;
        }
        // otherwise, the string has to be truncated
        if (splitPosition < 0) {
            String returnValue = value;
            value = "";
            setContent(value);
            PdfChunk pc = new PdfChunk(returnValue, font, attributes, noStroke);
            return pc;
        }
        String returnValue = value.substring(splitPosition);
        value = trim(value.substring(0, splitPosition));
        setContent(value);
        PdfChunk pc = new PdfChunk(returnValue, font, attributes, noStroke);
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
            if (image.scaledWidth() > width) {
                PdfChunk pc = new PdfChunk("", font, attributes, noStroke);
                value = "";
                attributes.remove(Chunk.IMAGE);
                image = null;
                font = new PdfFont(PdfFont.HELVETICA, 12);
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
            setContent(value);
            PdfChunk pc = new PdfChunk(returnValue, font, attributes, noStroke);
            return pc;
        }
        
        // loop over all the characters of a string
        // or until the totalWidth is reached
        int length = value.length();
        char character;
        while (currentPosition < length) {
            // the width of every character is added to the currentWidth
            character = value.charAt(currentPosition);
            currentWidth += font.width(character);
            if (currentWidth > width)
                break;
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
        }
        String returnValue = value.substring(currentPosition);
        value = value.substring(0, currentPosition);
        setContent(value);
        PdfChunk pc = new PdfChunk(returnValue, font, attributes, noStroke);
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
 * @return	a <CODE>Color</CODE>
 */
    
    Color color() {
        return (Color)noStroke.get(Chunk.COLOR);
    }
    
/**
 * Returns the width of this <CODE>PdfChunk</CODE>.
 *
 * @return	a width
 */
    
    float width() {
        if (image != null)
            return image.scaledWidth();
        return font.getFont().getWidthPoint(value, font.size());
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
 * extra charracter and word spacing.
 * @param charSpacing the extra character spacing
 * @param wordSpacing the extra word spacing
 * @return the calculated width
 */
    
    public float getWidthCorrected(float charSpacing, float wordSpacing)
    {
        if (image != null) {
            return image.scaledWidth() + charSpacing;
        }
        int numberOfSpaces = 0;
        int idx = -1;
        while ((idx = value.indexOf(' ', idx + 1)) >= 0)
            ++numberOfSpaces;
        return font.getFont().getWidthPoint(value, font.size()) + value.length() * charSpacing + numberOfSpaces * wordSpacing;
    }
    
/**
 * Trims the last space.
 * @return the width of the space trimmed, otherwise 0
 */
    
    public float trimLastSpace()
    {
        if (value.length() > 1 && value.endsWith(" ")) {
            value = value.substring(0, value.length() - 1);
            setContent(value);
            return font.width(' ');
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
        return (attributes.size() > 0);
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
    
/**
 * Gets the image offset in the x direction
 * @return the image offset in the x direction
 */
    
    float getImageOffsetX()
    {
        return offsetX;
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
 */
    
    void setValue(String value)
    {
        this.value = value;
        setContent(value);
    }

/**
 * Checks if a character can be used to split a <CODE>PdfString</CODE>.
 * <P>
 * for the moment every character less than or equal to SPACE and the character '-' are 'splitCharacters'.
 *
 * @param	c		the character that has to be checked
 * @return	<CODE>true</CODE> if the character can be used to split a string, <CODE>false</CODE> otherwise
 */
    public boolean isSplitCharacter(char c)
    {
        if (c <= ' ' || c == '-') {
            return true;
        }
        if (c < 0x2e80)
            return false;
        return ((c >= 0x2e80 && c < 0xd7a0)
        || (c >= 0xf900 && c < 0xfb00)
        || (c >= 0xfe30 && c < 0xfe50)
        || (c >= 0xff61 && c < 0xffa0));
    }
    
/**
 * Removes all the <VAR>' '</VAR> and <VAR>'-'</VAR>-characters on the right of a <CODE>String</CODE>.
 * <P>
 * @param	string		the <CODE>String<CODE> that has to be trimmed.
 * @return	the trimmed <CODE>String</CODE>
 */    
    static String trim(String string) {
        while (string.endsWith(" ") || string.endsWith("\t")) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }
}