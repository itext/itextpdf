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
 * A <CODE>ListItem</CODE> is a <CODE>Paragraph</CODE>
 * that can be added to a <CODE>List</CODE>.
 * <P>
 * <B>Example 1:</B>
 * <BLOCKQUOTE><PRE>
 * List list = new List(true, 20);
 * list.add(<STRONG>new ListItem("First line")</STRONG>);
 * list.add(<STRONG>new ListItem("The second line is longer to see what happens once the end of the line is reached. Will it start on a new line?")</STRONG>);
 * list.add(<STRONG>new ListItem("Third line")</STRONG>);
 * </PRE></BLOCKQUOTE>
 *
 * The result of this code looks like this:
 *	<OL>
 *		<LI>
 *			First line
 *		</LI>
 *		<LI>
 *			The second line is longer to see what happens once the end of the line is reached. Will it start on a new line?
 *		</LI>
 *		<LI>
 *			Third line
 *		</LI>
 *	</OL>
 *
 * <B>Example 2:</B>
 * <BLOCKQUOTE><PRE>
 * List overview = new List(false, 10);
 * overview.add(<STRONG>new ListItem("This is an item")</STRONG>);
 * overview.add("This is another item");
 * </PRE></BLOCKQUOTE>
 *
 * The result of this code looks like this:
 *	<UL>
 *		<LI>
 *			This is an item
 *		</LI>
 *		<LI>
 *			This is another item
 *		</LI>
 *	</UL>
 *
 * @see	Element
 * @see List
 * @see	Paragraph
 *
 * @author  bruno@lowagie.com
 */

public class ListItem extends Paragraph implements TextElementArray {
    
    // membervariables
    
/** this is the symbol that wil proceed the listitem. */
    private Chunk symbol;
    
    // constructors
    
/**
 * Constructs a <CODE>ListItem</CODE>.
 */
    
    public ListItem() {
        super();
    }
    
/**
 * Constructs a <CODE>ListItem</CODE> with a certain leading.
 *
 * @param	leading		the leading
 */
    
    public ListItem(float leading) {
        super(leading);
    }
    
/**
 * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Chunk</CODE>.
 *
 * @param	chunk		a <CODE>Chunk</CODE>
 */
    
    public ListItem(Chunk chunk) {
        super(chunk);
    }
    
/**
 * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>.
 *
 * @param	string		a <CODE>String</CODE>
 */
    
    public ListItem(String string) {
        super(string);
    }
    
/**
 * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>
 * and a certain <CODE>Font</CODE>.
 *
 * @param	string		a <CODE>String</CODE>
 * @param	font		a <CODE>String</CODE>
 */
    
    public ListItem(String string, Font font) {
        super(string, font);
    }
    
/**
 * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Chunk</CODE>
 * and a certain leading.
 *
 * @param	leading		the leading
 * @param	chunk		a <CODE>Chunk</CODE>
 */
    
    public ListItem(float leading, Chunk chunk) {
        super(leading, chunk);
    }
    
/**
 * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>
 * and a certain leading.
 *
 * @param	leading		the leading
 * @param	string		a <CODE>String</CODE>
 */
    
    public ListItem(float leading, String string) {
        super(leading, string);
    }
    
/**
 * Constructs a <CODE>ListItem</CODE> with a certain leading, <CODE>String</CODE>
 * and <CODE>Font</CODE>.
 *
 * @param	leading		the leading
 * @param	string		a <CODE>String</CODE>
 * @param	font		a <CODE>Font</CODE>
 */
    
    public ListItem(float leading, String string, Font font) {
        super(leading, string, font);
    }
    
/**
 * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Phrase</CODE>.
 *
 * @param	phrase		a <CODE>Phrase</CODE>
 */
    
    public ListItem(Phrase phrase) {
        super(phrase);
    }
    
        /**
         * Returns a <CODE>ListItem</CODE> that has been constructed taking in account
         * the value of some <VAR>attributes</VAR>.
         *
         * @param	attributes		Some attributes
         * @return	a <CODE>ListItem</CODE>
         */
    
    public ListItem(Properties attributes) {
        super("", new Font(attributes));
        String value;
        if ((value = attributes.getProperty(ElementTags.ITEXT)) != null) {
            remove(0);
            add(new Chunk(value));
        }
        if ((value = attributes.getProperty(ElementTags.LEADING)) != null) {
            setLeading(Float.valueOf(value + "f").floatValue());
        }
        if ((value = attributes.getProperty(ElementTags.INDENTATIONLEFT)) != null) {
            setIndentationLeft(Float.valueOf(value + "f").floatValue());
        }
        if ((value = attributes.getProperty(ElementTags.INDENTATIONRIGHT)) != null) {
            setIndentationRight(Float.valueOf(value + "f").floatValue());
        }
        if ((value = attributes.getProperty(ElementTags.ALIGN)) != null) {
            setAlignment(value);
        }
    }
    
    // implementation of the Element-methods
    
/**
 * Gets the type of the text element.
 *
 * @return	a type
 */
    
    public int type() {
        return Element.LISTITEM;
    }
    
    // methods
    
/**
 * Sets the listsymbol.
 *
 * @param	symbol	a <CODE>Chunk</CODE>
 */
    
    public final void setListSymbol(Chunk symbol) {
        this.symbol = symbol;
        if (this.symbol.font().isStandardFont()) {
            this.symbol.setFont(font);
        }
    }
    
    // methods to retrieve information
    
/**
 * Returns the listsymbol.
 *
 * @return	a <CODE>Chunk</CODE>
 */
    
    public final Chunk listSymbol() {
        return symbol;
    }
    
/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.LISTITEM.equals(tag);
    }
    
/**
 * Returns a representation of this <CODE>ListItem</CODE>.
 *
 * @return	a <CODE>String</CODE>
 */
    
    public String toString() {
        StringBuffer buf = new StringBuffer("\n<").append(ElementTags.LISTITEM).append(" ").append(ElementTags.LEADING).append("=\"");
        buf.append(leading);
        buf.append("\"").append(font.toString()).append(" ").append(ElementTags.ALIGN).append("=\"").append(ElementTags.getAlignment(alignment));
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
        buf.append("</").append(ElementTags.LISTITEM).append(">");
        return buf.toString();
    }
}
