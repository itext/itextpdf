/*
 * @(#)Phrase.java					0.38 2000/11/16
 *       release iText0.3:			0.26 2000/02/14
 *       release iText0.35:			0.26 2000/08/11
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A <CODE>Phrase</CODE> is a series of <CODE>Chunk</CODE>s.
 * <P>
 * A <CODE>Phrase</CODE> has a main <CODE>Font</CODE>, but some chunks
 * within the phrase can have a <CODE>Font</CODE> that differs from the
 * main <CODE>Font</CODE>. All the <CODE>Chunk</CODE>s in a <CODE>Phrase</CODE>
 * have the same <VAR>leading</CODE>.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * <STRONG>Phrase phrase1 = new Phrase("this is a phrase");</STRONG>
 * <STRONG>Phrase phrase2 = new Phrase(16, "this is a phrase with leading 16");</STRONG>
 * <STRONG>Phrase phrase3 = new Phrase("this is a phrase with a red, normal font Courier, size 12", new Font(Font.COURIER, 12, Font.NORMAL, new Color(255, 0, 0)));</STRONG>
 * <STRONG>Phrase phrase4 = new Phrase(new Chunk("this is a phrase"));</STRONG>
 * <STRONG>Phrase phrase5 = new Phrase(18, new Chunk("this is a phrase") new Font(Font.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @see		Element
 * @see		Chunk
 * @see		Paragraph
 * @see		Anchor
 *
 * @author  bruno@lowagie.com
 * @version 0.38, 2000/11/16
 *
 * @since   iText0.30
 */

public class Phrase extends ArrayList implements Element {

// membervariables

	/** This is the leading of this phrase. */
	protected int leading;

// constructors

	/**
	 * Constructs a <CODE>Phrase</CODE> without specifying a leading.
	 *
	 * @since	iText0.30
	 */

	public Phrase() {
		this(16);
	}

	/**
	 * Constructs a <CODE>Phrase</CODE> with a certain leading.
	 *
	 * @param	leading		the leading
	 *
	 * @since	iText0.30
	 */

	public Phrase(int leading) {
		this.leading = leading;
	}

	/**
	 * Constructs a <CODE>Phrase</CODE> with a certain <CODE>Chunk</CODE>.
	 *
	 * @param	chunk		a <CODE>Chunk</CODE>
	 *
	 * @since	iText0.30
	 */

	public Phrase(Chunk chunk) {
		this(chunk.font().leading(1.5));
		super.add(chunk);
	}

	/**
	 * Constructs a <CODE>Phrase</CODE> with a certain <CODE>Chunk</CODE>
	 * and a certain leading.
	 *
	 * @param	leading	the leading
	 * @param	chunk		a <CODE>Chunk</CODE>
	 *
	 * @since	iText0.30
	 */

	public Phrase(int leading, Chunk chunk) {
		this(leading);
		super.add(chunk);
	}

	/**
	 * Constructs a <CODE>Phrase</CODE> with a certain <CODE>String</CODE>.
	 *
	 * @param	string		a <CODE>String</CODE>
	 *
	 * @since	iText0.30
	 */

	public Phrase(String string) {
		this(new Font().leading(1.5), string, new Font());
	}

	/**
	 * Constructs a <CODE>Phrase</CODE> with a certain <CODE>String</CODE> and a certain <CODE>Font</CODE>.
	 *
	 * @param	string		a <CODE>String</CODE>
	 * @param	font		a <CODE>Font</CODE>
	 *
	 * @since	iText0.30
	 */

	public Phrase(String string, Font font) {
		this(font.leading(1.5), string, font);
	}

	/**
	 * Constructs a <CODE>Phrase</CODE> with a certain leading and a certain <CODE>String</CODE>.
	 *
	 * @param	leading	the leading
	 * @param	string		a <CODE>String</CODE>
	 *
	 * @since	iText0.30
	 */

	public Phrase(int leading, String string) {
		this(leading, string, new Font());
	}

	/**
	 * Constructs a <CODE>Phrase</CODE> with a certain leading, a certain <CODE>String</CODE>
	 * and a certain <CODE>Font</CODE>.
	 *
	 * @param	leading	the leading
	 * @param	string		a <CODE>String</CODE>
	 * @param	font		a <CODE>Font</CODE>
	 *
	 * @since	iText0.30
	 */

	public Phrase(int leading, String string, Font font) {
		this(leading);
		if (font.family() != Font.SYMBOL && font.family() != Font.ZAPFDINGBATS) {
			int i = 0;
			int index;
			while((index = Greek.index(string)) > -1) {
				if (index == 0) {
					String firstpart = string.substring(0, index);
					super.add(new Chunk(firstpart, font));
					string = string.substring(index);
				}
				Font symbol = new Font(Font.SYMBOL, font.size(), font.style(), font.color());
				StringBuffer buf = new StringBuffer();
				buf.append(Greek.getCorrespondingSymbol(string.charAt(0)));
				string = string.substring(1);
				while (Greek.index(string) == 0) {
					buf.append(Greek.getCorrespondingSymbol(string.charAt(0)));
					string = string.substring(1);
				}
				super.add(new Chunk(buf.toString(), symbol));
			}
		}
		super.add(new Chunk(string, font));
	}

// implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to a
	 * <CODE>DocListener</CODE>. 
     *
	 * @return	<CODE>true</CODE> if the element was processed successfully
	 *
     * @since   iText0.30
     */

    public final boolean process(DocListener listener) {
		try {
			for (Iterator i = iterator(); i.hasNext(); ) {
				listener.add((Element) i.next());
			}
			return true;
		}
		catch(DocumentException de) {
			return false;
		}
	}

    /**
     * Gets the type of the text element. 
     *
     * @return	a type
	 *
     * @since	iText0.30
     */

    public int type() {
		return Element.PHRASE;
	}		

    /**
     * Gets all the chunks in this element. 
     *
     * @return	an <CODE>ArrayList</CODE>
	 *
     * @since	iText0.30
     */

    public ArrayList getChunks() {
		 ArrayList tmp = new ArrayList();
		 for (Iterator i = iterator(); i.hasNext(); ) {
			 tmp.addAll(((Element) i.next()).getChunks());
		 }
		 return tmp;
	}

// overriding some of the ArrayList-methods

	/**
	 * Adds a <CODE>Chunk</CODE>, an <CODE>Anchor</CODE> or another <CODE>Phrase</CODE>
	 * to this <CODE>Phrase</CODE>.
	 *
	 * @param	index	index at which the specified element is to be inserted
	 * @param	object	an object of type <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
	 * @return	<CODE>void</CODE>
	 * @throws	ClassCastException	when you try to add something that isn't a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
	 *
	 * @since	iText0.30
	 */

	public void add(int index, Object o) {
		try {
			Element element = (Element) o;
			if (element.type() == Element.CHUNK ||
				element.type() == Element.PHRASE ||
				element.type() == Element.ANCHOR) {
				super.add(index, element);
			}
			else {
				throw new ClassCastException(String.valueOf(element.type()));
			}
		}
		catch(ClassCastException cce) {
			throw new ClassCastException("Insertion of illegal Element: " + cce.getMessage());
		}
	}

	/**
	 * Adds a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or another <CODE>Phrase</CODE>
	 * to this <CODE>Phrase</CODE>.
	 *
	 * @param	object	an object of type <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
	 * @return	a boolean
	 * @throws	ClassCastException	when you try to add something that isn't a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
	 *
	 * @since	iText0.30
	 */

	public boolean add(Object o) {
		try {
			Element element = (Element) o;
			switch(element.type()) {
			case Element.CHUNK:
				Chunk chunk = (Chunk) o;
				return super.add(chunk);
			case Element.PHRASE:
				Phrase phrase = (Phrase) o;
				return this.add(phrase);
			case Element.ANCHOR:
				Anchor anchor = (Anchor) o;
				return this.add(anchor);
			case Element.LIST:
				List list = (List) o;
				return super.add(list);
			default:
				throw new ClassCastException(String.valueOf(element.type()));
			}
		}
		catch(ClassCastException cce) {
			throw new ClassCastException("Insertion of illegal Element: " + cce.getMessage());
		}
	}

	/**
	 * Adds a <CODE>Phrase</CODE> to this <CODE>Phrase</CODE>.
	 *
	 * @param	phrase		a <CODE>Phrase</CODE>
	 * @return	a boolean
	 *
	 * @since	iText0.30
	 */

	public boolean add(Phrase phrase) {
		if (phrase.type() == Element.ANCHOR) {
			return super.add(phrase);
		}
		Chunk chunk;
		boolean success = true;
		for (Iterator i = phrase.iterator(); i.hasNext(); ) {
			success &= super.add(i.next());
		}
		return success;
	}

	/**
	 * Adds a <CODE>String</CODE> to this <CODE>Phrase</CODE>.
	 * <P>
	 * The <CODE>String</CODE> is first converted to a Chunk with the font of the phrase.
	 *
	 * @param	phrase		a <CODE>Phrase</CODE>
	 * @return	a boolean
	 *
	 * @since	iText0.30
	 */

	public boolean add(String string) {
		return super.add(new Chunk(string, font()));
	}

	/**
	 * Adds a collection of <CODE>Chunk</CODE>s
	 * to this <CODE>Phrase</CODE>.
	 *
	 * @param	collection	a collection of <CODE>Chunk</CODE>s, <CODE>Anchor</CODE>s and <CODE>Phrase</CODE>s.
	 * @return	<CODE>true</CODE> if the action succeeded, <CODE>false</CODE> if not.
	 * @throws	ClassCastException	when you try to add something that isn't a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
	 *
	 * @since	iText0.30
	 */

	public boolean addAll(Collection collection) {	
		for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
			this.add(iterator.next());
		}
		return true;
	}

// methods

	/**
	 * Sets the leading of this phrase.
	 *
	 * @param	leading		the new leading
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public final void setLeading(int leading) {
		this.leading = leading;
	}

// methods to retrieve information

	/**
	 * Checks is this <CODE>Phrase</CODE> contains no or 1 empty <CODE>Chunk</CODE>.
	 *
	 * @return	<CODE>false</CODE> if the <CODE>Phrase</CODE>
	 * contains more than one or more non-empty<CODE>Chunk</CODE>s.
	 * 
	 * @since	iText0.30
	 */

	public final boolean isEmpty() {
		switch(size()) {
		case 0:
			return true;
		case 1:
			Element element = (Element) get(0);
			if (element.type() == Element.CHUNK && ((Chunk) element).isEmpty()) {
				return true;
			}
			return false;
		default:
			return false;
		}
	}

	/**
	 * Gets the leading of this phrase.
	 *
	 * @return	linespacing
	 *
	 * @since	iText0.30
	 */

	public final int leading() {
		return leading;
	}

	/**
	 * Gets the font of the first <CODE>Chunk</CODE> that appears in this <CODE>Phrase</CODE>.
	 *
	 * @return	a <CODE>Font</CODE>
	 *
	 * @since	iText0.30
	 */

	public final Font font() {
		if (size() < 1) {
			return new Font();
		}
		try {
			Element element = (Element) get(0);
			switch(element.type()) {
			case Element.CHUNK:
				return ((Chunk) element).font();
			case Element.PHRASE:
			case Element.ANCHOR:
				return ((Phrase) element).font();
			default:
				return new Font();
			}
		}
		catch(ClassCastException cce) {
			return new Font();
		}
	}

	/**
	 * Returns a representation of this <CODE>Phrase</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 *
	 * @since	iText0.30
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("<PHRASE LEADING=\"");
		buf.append(leading);
		buf.append("\">\n");
		for (Iterator i = iterator(); i.hasNext(); ) {
			buf.append(i.next().toString());
		}
		buf.append("</PHRASE>\n");								
		return buf.toString();
	}
}