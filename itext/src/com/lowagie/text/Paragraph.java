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
 *
 * @author  bruno@lowagie.com
 */

public class Paragraph extends Phrase implements TextElementArray {
    
    // membervariables
    
/** The alignment of the text. */
    protected int alignment;
    
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
        super(phrase.leading());
        add(phrase);
    }
    
/**
 * Returns a <CODE>Paragraph</CODE> that has been constructed taking in account
 * the value of some <VAR>attributes</VAR>.
 *
 * @param	attributes		Some attributes
 * @return	a <CODE>Paragraph</CODE>
 */
    
    public Paragraph(Properties attributes) {
        this("", new Font(attributes));
        String value;
        if ((value = attributes.getProperty(ElementTags.ITEXT)) != null) {
            remove(0);
            add(new Chunk(value));
        }
        if ((value = attributes.getProperty(ElementTags.ALIGN)) != null) {
            setAlignment(value);
        }
        if ((value = attributes.getProperty(ElementTags.LEADING)) != null) {
            setLeading(Float.parseFloat(value + "f"));
        }
        if ((value = attributes.getProperty(ElementTags.LEFT)) != null) {
            setIndentationLeft(Float.parseFloat(value + "f"));
        }
        if ((value = attributes.getProperty(ElementTags.RIGHT)) != null) {
            setIndentationRight(Float.parseFloat(value + "f"));
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
        if (ElementTags.ALIGN_CENTER.equals(alignment)) {
            this.alignment = Element.ALIGN_CENTER;
            return;
        }
        if (ElementTags.ALIGN_RIGHT.equals(alignment)) {
            this.alignment = Element.ALIGN_RIGHT;
            return;
        }
        if (ElementTags.ALIGN_JUSTIFIED.equals(alignment)) {
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
    
/**
 * Returns an XML representation of this <CODE>Paragraph</CODE>.
 *
 * @return	a <CODE>String</CODE>
 */
    
    public String toXml(int indent) {
        StringBuffer buf = new StringBuffer();
        DocWriter.addTabs(buf, indent);
        buf.append("<").append(ElementTags.PARAGRAPH);
        
        buf.append(" ").append(ElementTags.LEADING).append("=\"").append(leading).append("\"");
        buf.append(font.toString());
        buf.append(" ").append(ElementTags.ALIGN).append("=\"").append(ElementTags.getAlignment(alignment));
        if (indentationLeft != 0) {
            buf.append("\" ").append(ElementTags.INDENTATIONLEFT).append("=\"").append(indentationLeft);
        }
        if (indentationRight != 0) {
            buf.append("\" ").append(ElementTags.INDENTATIONRIGHT).append("=\"").append(indentationRight);
        }
        buf.append("\">\n");
        for (Iterator i = iterator(); i.hasNext(); ) {
            buf.append(((Element)i.next()).toXml(indent + 1));
        }
        DocWriter.addTabs(buf, indent);
        buf.append("</").append(ElementTags.PARAGRAPH).append(">\n");
        return buf.toString();
    }
    
/**
 * Returns a representation of this <CODE>Paragraph</CODE>.
 *
 * @return	a <CODE>String</CODE>
 */
    
    public String toString() {
        StringBuffer buf = new StringBuffer("<").append(ElementTags.PARAGRAPH).append(" ").append(ElementTags.LEADING).append("=\"");
        buf.append(leading).append("\"").append(font.toString());
        buf.append(" ").append(ElementTags.ALIGN).append("=\"");
        buf.append(ElementTags.getAlignment(alignment));
        if (indentationLeft != 0) {
            buf.append("\" ").append(ElementTags.INDENTATIONLEFT).append("=\"");
            buf.append(indentationLeft);
        }
        if (indentationRight != 0) {
            buf.append("\" ").append(ElementTags.INDENTATIONRIGHT).append("=\"");
            buf.append(indentationRight);
        }
        buf.append("\">");
        for (Iterator i = iterator(); i.hasNext(); ) {
            buf.append(i.next().toString());
        }
        buf.append("</").append(ElementTags.PARAGRAPH).append(">\n");
        return buf.toString();
    }
}
