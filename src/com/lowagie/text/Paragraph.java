/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
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
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
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

import java.util.Iterator;
import java.util.Properties;

/**
 * A <CODE>Paragraph</CODE> is a series of <CODE>Chunk</CODE>s and/or <CODE>Phrases</CODE>.
 * <P>
 * A <CODE>Paragraph</CODE> has the same qualities of a <CODE>Phrase</CODE>, but also
 * some additional layout-parameters:
 * <UL>
 * <LI>the indentation
 * <LI>the alignment of the text
 * </UL>
 *
 * Example:
 * <BLOCKQUOTE><PRE>
 * <STRONG>Paragraph p = new Paragraph("This is a paragraph",
 *               new Font(Font.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0, 255)));</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @see		Element
 * @see		Phrase
 * @see		ListItem
 */

public class Paragraph extends Phrase implements TextElementArray {
    
    // membervariables
    
/** The alignment of the text. */
    protected int alignment = Element.ALIGN_UNDEFINED;
    
/** The indentation of this paragraph on the left side. */
    protected float indentationLeft;
    
/** The indentation of this paragraph on the right side. */
    protected float indentationRight;
    
    // constructors
    
/**
 * Constructs a <CODE>Paragraph</CODE>.
 */
    
    public Paragraph() {
        super();
    }
    
/**
 * Constructs a <CODE>Paragraph</CODE> with a certain leading.
 *
 * @param	leading		the leading
 */
    
    public Paragraph(float leading) {
        super(leading);
    }
    
/**
 * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>Chunk</CODE>.
 *
 * @param	chunk		a <CODE>Chunk</CODE>
 */
    
    public Paragraph(Chunk chunk) {
        super(chunk);
    }
    
/**
 * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>Chunk</CODE>
 * and a certain leading.
 *
 * @param	leading		the leading
 * @param	chunk		a <CODE>Chunk</CODE>
 */
    
    public Paragraph(float leading, Chunk chunk) {
        super(leading, chunk);
    }
    
/**
 * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>String</CODE>.
 *
 * @param	string		a <CODE>String</CODE>
 */
    
    public Paragraph(String string) {
        super(string);
    }
    
/**
 * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>String</CODE>
 * and a certain <CODE>Font</CODE>.
 *
 * @param	string		a <CODE>String</CODE>
 * @param	font		a <CODE>Font</CODE>
 */
    
    public Paragraph(String string, Font font) {
        super(string, font);
    }
    
/**
 * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>String</CODE>
 * and a certain leading.
 *
 * @param	leading		the leading
 * @param	string		a <CODE>String</CODE>
 */
    
    public Paragraph(float leading, String string) {
        super(leading, string);
    }
    
/**
 * Constructs a <CODE>Paragraph</CODE> with a certain leading, <CODE>String</CODE>
 * and <CODE>Font</CODE>.
 *
 * @param	leading		the leading
 * @param	string		a <CODE>String</CODE>
 * @param	font		a <CODE>Font</CODE>
 */
    
    public Paragraph(float leading, String string, Font font) {
        super(leading, string, font);
    }
    
/**
 * Constructs a <CODE>Paragraph</CODE> with a certain <CODE>Phrase</CODE>.
 *
 * @param	phrase		a <CODE>Phrase</CODE>
 */
    
    public Paragraph(Phrase phrase) {
        super(phrase.leading(), "", phrase.font());
        super.add(phrase);
    }
    
/**
 * Returns a <CODE>Paragraph</CODE> that has been constructed taking in account
 * the value of some <VAR>attributes</VAR>.
 *
 * @param	attributes		Some attributes
 */
    
    public Paragraph(Properties attributes) {
        this("", new Font(attributes));
        String value;
        if ((value = (String)attributes.remove(ElementTags.ITEXT)) != null) {
            Chunk chunk = new Chunk(value);
            if ((value = (String)attributes.remove(ElementTags.GENERICTAG)) != null) {
                chunk.setGenericTag(value);
            }
            add(chunk);
        }
        if ((value = (String)attributes.remove(ElementTags.ALIGN)) != null) {
            setAlignment(value);
        }
        if ((value = (String)attributes.remove(ElementTags.LEADING)) != null) {
            setLeading(Float.valueOf(value + "f").floatValue());
        }
        else {
            setLeading(16);
        }
        if ((value = (String)attributes.remove(ElementTags.INDENTATIONLEFT)) != null) {
            setIndentationLeft(Float.valueOf(value + "f").floatValue());
        }
        if ((value = (String)attributes.remove(ElementTags.INDENTATIONRIGHT)) != null) {
            setIndentationRight(Float.valueOf(value + "f").floatValue());
        }
    }
    
    // implementation of the Element-methods
    
/**
 * Gets the type of the text element.
 *
 * @return	a type
 */
    
    public int type() {
        return Element.PARAGRAPH;
    }
    
    // methods
    
/**
 * Adds an <CODE>Object</CODE> to the <CODE>Paragraph</CODE>.
 *
 * @param	object		the object to add.
 */
    
    public boolean add(Object o) {
        if (o instanceof List) {
            List list = (List) o;
            list.setIndentationLeft(list.indentationLeft() + indentationLeft);
            list.setIndentationRight(indentationRight);
            return super.add(list);
        }
        else if (o instanceof Image) {
            super.addSpecial((Image) o);
            return true;
        }
        else if (o instanceof Paragraph) {
            super.add(o);
            super.add(Chunk.NEWLINE);
            return true;
        }
        return super.add(o);
    }
    
    // setting the membervariables
    
/**
 * Sets the alignment of this paragraph.
 *
 * @param	alignment		the new alignment
 */
    
    public final void setAlignment(int alignment) {
        this.alignment = alignment;
    }
    
/**
 * Sets the alignment of this paragraph.
 *
 * @param	alignment		the new alignment as a <CODE>String</CODE>
 */
    
    public final void setAlignment(String alignment) {
        if (ElementTags.ALIGN_CENTER.equalsIgnoreCase(alignment)) {
            this.alignment = Element.ALIGN_CENTER;
            return;
        }
        if (ElementTags.ALIGN_RIGHT.equalsIgnoreCase(alignment)) {
            this.alignment = Element.ALIGN_RIGHT;
            return;
        }
        if (ElementTags.ALIGN_JUSTIFIED.equalsIgnoreCase(alignment)) {
            this.alignment = Element.ALIGN_JUSTIFIED;
            return;
        }
        this.alignment = Element.ALIGN_LEFT;
    }
    
/**
 * Sets the indentation of this paragraph on the left side.
 *
 * @param	indentation		the new indentation
 */
    
    public final void setIndentationLeft(float indentation) {
        this.indentationLeft = indentation;
    }
    
/**
 * Sets the indentation of this paragraph on the right side.
 *
 * @param	indentation		the new indentation
 */
    
    public final void setIndentationRight(float indentation) {
        this.indentationRight = indentation;
    }
    
    // methods to retrieve information
    
/**
 * Gets the alignment of this paragraph.
 *
 * @return	alignment
 */
    
    public final int alignment() {
        return alignment;
    }
    
/**
 * Gets the indentation of this paragraph on the left side.
 *
 * @return	the indentation
 */
    
    public final float indentationLeft() {
        return indentationLeft;
    }
    
/**
 * Gets the indentation of this paragraph on the right side.
 *
 * @return	the indentation
 */
    
    public final float indentationRight() {
        return indentationRight;
    }
    
/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.PARAGRAPH.equals(tag);
    }
}
