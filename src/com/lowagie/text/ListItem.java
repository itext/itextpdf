/*
 * @(#)ListItem.java				0.39 2000/11/23
 *       release iText0.3:			0.23 2000/02/14
 *       release iText0.35:         0.23 2000/08/11
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

package com.lowagie.text;

import java.util.Iterator;

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
 * @version 0.39 2000/11/23
 *
 * @since   iText0.30
 */

public class ListItem extends Paragraph implements Element {

// membervariables

	/** this is the symbol that wil proceed the listitem. */
	private Chunk symbol;

// constructors

	/**
	 * Constructs a <CODE>ListItem</CODE>.
	 *
	 * @since	iText0.30
	 */

	public ListItem() {
		super();
	}

	/**
	 * Constructs a <CODE>ListItem</CODE> with a certain leading.
	 *
	 * @param	leading		the leading
	 *
	 * @since	iText0.30
	 */

	public ListItem(int leading) {
		super(leading);
	}

	/**
	 * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Chunk</CODE>.
	 *
	 * @param	chunk		a <CODE>Chunk</CODE>
	 *
	 * @since	iText0.30
	 */

	public ListItem(Chunk chunk) {
		super(chunk);
	}

	/**
	 * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>.
	 *
	 * @param	string		a <CODE>String</CODE>
	 *
	 * @since	iText0.30
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
	 *
	 * @since	iText0.30
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
	 *
	 * @since	iText0.30
	 */

	public ListItem(int leading, Chunk chunk) {
		super(leading, chunk);
	}

	/**
	 * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>
	 * and a certain leading.
	 *
	 * @param	leading		the leading
	 * @param	string		a <CODE>String</CODE>
	 *
	 * @since	iText0.30
	 */

	public ListItem(int leading, String string) {
		super(leading, string);
	}

	/**
	 * Constructs a <CODE>ListItem</CODE> with a certain leading, <CODE>String</CODE>
	 * and <CODE>Font</CODE>.
	 *
	 * @param	leading		the leading
	 * @param	string		a <CODE>String</CODE>
	 * @param	font		a <CODE>Font</CODE>
	 *
	 * @since	iText0.30
	 */

	public ListItem(int leading, String string, Font font) {
		super(leading, string, font);
	}

	/**
	 * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Phrase</CODE>.
	 *
	 * @param	phrase		a <CODE>Phrase</CODE>
	 *
	 * @since	iText0.30
	 */

	public ListItem(Phrase phrase) {
		super(phrase);
	}

// implementation of the Element-methods

    /**
     * Gets the type of the text element. 
     *
     * @return	a type
	 *
     * @since	iText0.30
     */

    public int type() {
		return Element.LISTITEM;
	}

// methods

	/**
	 * Sets the listsymbol.
	 *
	 * @param	symbol	a <CODE>Chunk</CODE>
	 * @return	void
	 *
     * @since	iText0.30
	 */

	public final void setListSymbol(Chunk symbol) {
		this.symbol = symbol;
		if (this.symbol.font().isStandardFont()) {
			this.symbol.setFont(font());
		}
	}

// methods to retrieve information

	/**
	 * Returns the listsymbol.
	 *
	 * @return	a <CODE>Chunk</CODE>					  
	 *
     * @since	iText0.30
	 */

	public final Chunk listSymbol() {
		return symbol;
	}

	/**
	 * Returns a representation of this <CODE>ListItem</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 *
     * @since	iText0.30
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("\t<LISTITEM LEADING=\"");
		buf.append(leading);
		if (indentationLeft != 0) {
			buf.append("\" LEFTINDENTATION=\"");
			buf.append(indentationLeft);
		}
		buf.append("\">\n");
		for (Iterator i = iterator(); i.hasNext(); ) {
			buf.append(i.next().toString());
		}
		buf.append("\n\t</LISTITEM>\n");								
		return buf.toString();
	}
}