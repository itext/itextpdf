/*
 * @(#)Anchor.java					0.37 2000/10/05
 *       release iText0.3:			0.22 2000/02/14
 *       release iText0.35:         0.22 2000/08/11
 *       release iText0.37:         0.37 2000/10/05
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

import java.net.URL;
import java.net.MalformedURLException;

import java.util.Iterator;

/**
 * An <CODE>Anchor</CODE> can be a reference or a destination of a reference.
 * <P>
 * An <CODE>Anchor</CODE> is a special kind of <CODE>Phrase</CODE>.
 * It is constructed in the same way.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * <STRONG>Anchor anchor = new Anchor("this is a link");</STRONG>
 * <STRONG>anchor.setName("LINK");</STRONG>
 * <STRONG>anchor.setReference("http://www.lowagie.com");</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @see		Element
 * @see		Phrase
 *
 * @author  bruno@lowagie.com
 * @version 0.22, 2000/02/02
 *
 * @since   iText0.30
 */

public class Anchor extends Phrase implements Element {

// membervariables

	/** This is the name of the <CODE>Anchor</CODE>. */
	private String name;

	/** This is the reference of the <CODE>Anchor</CODE>. */
	private String reference;

// constructors

	/**
	 * Constructs an <CODE>Anchor</CODE> without specifying a leading.
	 *
	 * @since	iText0.30
	 */

	public Anchor() {
		super(16);
	}

	/**
	 * Constructs an <CODE>Anchor</CODE> with a certain leading.
	 *
	 * @param	leading		the leading
	 * @since	iText0.30
	 */

	public Anchor(int leading) {
		super(leading);
	}

	/**
	 * Constructs an <CODE>Anchor</CODE> with a certain <CODE>Chunk</CODE>.
	 *
	 * @param	chunk		a <CODE>Chunk</CODE>
	 * @since	iText0.30
	 */

	public Anchor(Chunk chunk) {
		super(chunk);
	}

	/**
	 * Constructs an <CODE>Anchor</CODE> with a certain <CODE>String</CODE>.
	 *
	 * @param	string		a <CODE>String</CODE>
	 * @since	iText0.30
	 */

	public Anchor(String string) {
		super(string);
	}

	/**
	 * Constructs an <CODE>Anchor</CODE> with a certain <CODE>String</CODE>
	 * and a certain <CODE>Font</CODE>.
	 *
	 * @param	string		a <CODE>String</CODE>
	 * @param	font		a <CODE>Font</CODE>
	 * @since	iText0.30
	 */

	public Anchor(String string, Font font) {
		super(string, font);
	}

	/**
	 * Constructs an <CODE>Anchor</CODE> with a certain <CODE>Chunk</CODE>
	 * and a certain leading.
	 *
	 * @param	leading		the leading
	 * @param	chunk		a <CODE>Chunk</CODE>
	 * @since	iText0.30
	 */

	public Anchor(int leading, Chunk chunk) {
		super(leading, chunk);
	}

	/**
	 * Constructs an <CODE>Anchor</CODE> with a certain leading
	 * and a certain <CODE>String</CODE>.
	 *
	 * @param	leading		the leading
	 * @param	string		a <CODE>String</CODE>
	 * @since	iText0.30
	 */

	public Anchor(int leading, String string) {
		super(leading, string);
	}

	/**
	 * Constructs an <CODE>Anchor</CODE> with a certain leading,
	 * a certain <CODE>String</CODE> and a certain <CODE>Font</CODE>.
	 *
	 * @param	leading		the leading
	 * @param	string		a <CODE>String</CODE>
	 * @param	font		a <CODE>Font</CODE>
	 * @since	iText0.30
	 */

	public Anchor(int leading, String string, Font font) {
		super(leading, string, font);
	}

// implementation of the Element-methods

    /**
     * Gets the type of the text element. 
     *
     * @return	a type
     * @since	iText0.30
     */

    public final int type() {
		return Element.ANCHOR;
	}

// methods

	/**
	 * Gets an iterator of <CODE>Element</CODE>s. 
     *
     * @return	an <CODE>Iterator</CODE>.
	 *
     * @since	iText0.37
	 */

	// suggestion by by Curt Thompson
	public final Iterator getElements() {
		return this.iterator();
	}

	/**
	 * Sets the name of this <CODE>Anchor</CODE>.
	 *
	 * @param	name		a new name
	 * @return	<CODE>void</CODE>
	 * @since	iText0.30
	 */

	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the reference of this <CODE>Anchor</CODE>.
	 *
	 * @param	reference		a new reference
	 * @return	<CODE>void</CODE>
	 * @since	iText0.30
	 */

	public final void setReference(String reference) {
		this.reference = reference;
	}

// methods to retrieve information

	/**
	 * Returns the name of this <CODE>Anchor</CODE>.
	 *
	 * @return	a name
	 * @since	iText0.30
	 */

	public final String name() {
		return name;
	}

	/**
	 * Gets the reference of this <CODE>Anchor</CODE>.
	 *
	 * @return	a reference
	 * @since	iText0.30
	 */

	public final String reference() {
		return reference;			  	
	}

	/**
	 * Gets the reference of this <CODE>Anchor</CODE>.
	 *
	 * @return	an <CODE>URL</CODE>
	 * @since	iText0.37
	 */

	public final URL url() {
		try {
			return new URL(reference);
		}
		catch(MalformedURLException mue) {
			return null;
		}
	}

	/**
	 * Returns a representation of this <CODE>Anchor</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 * @since	iText0.30
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("<ANCHOR LEADING=\"");
		buf.append(leading);
		if (name != null) {
			buf.append("\" NAME=\"");
			buf.append(name);
		}
		if (reference != null) {
			buf.append("\" REFERENCE=\"");
			buf.append(reference);
		}
		buf.append("\">\n");
		for (Iterator i = iterator(); i.hasNext(); ) {
			buf.append(i.next().toString());
		}
		buf.append("</ANCHOR>\n");								
		return buf.toString();
	}
}