/*
 * @(#)Meta.java					0.31 2000/04/20
 *       release iText0.3:			0.24 2000/02/14
 *       release iText0.35:         0.31 2000/08/11
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

/**
 * This is an <CODE>Element</CODE> that contains
 * some meta information about the document.
 *
 * An object of type <CODE>Meta</CODE> can not be constructed by the user.
 * Userdefined meta information should be placed in a <CODE>Header</CODE>-object.
 * <CODE>Meta</CODE> is reserved for: Subject, Keywords, Author, Title, Producer
 * and Creationdate information.
 *
 * @see		Element
 * @see		Header
 *
 * @author  bruno@lowagie.com
 * @version 0.31, 2000/04/20
 *
 * @since   iText0.30
 */

public class Meta implements Element {

// membervariables

	/** This is the type of Meta-information this object contains. */
	private int type;
		   
	/** This is the content of the Meta-information. */
	private StringBuffer content;

// constructors

	/**
	 * Constructs a <CODE>Meta</CODE>.
	 *
	 * @param	type		the type of meta-information
	 * @param	content		the content
	 *
	 * @since	iText0.30
	 */

	Meta(int type, String content) {
		this.type = type;
		this.content = new StringBuffer(content);
	}

// implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to a
	 * <CODE>DocListener</CODE>. 
     *
	 * <CODE>true</CODE> if the element was processed successfully
	 *
     * @since   iText0.30
     */

    public final boolean process(DocListener listener) {
		try {
			return listener.add(this);
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

    public final int type() {
		return type;
	}		

    /**
     * Gets all the chunks in this element. 
     *
     * @return	an <CODE>ArrayList</CODE>
	 *
     * @since	iText0.30
     */

    public ArrayList getChunks() {
		 return new ArrayList();
	}

// methods

	/**
	 * appends some text to this <CODE>Meta</CODE>.
	 *
	 * @param	a <CODE>String</CODE>
	 * @return	a <CODE>StringBuffer</CODE>
	 *
	 * @since	iText0.30
	 */

	public final StringBuffer append(String string) {
		return content.append(string);
	}

// methods to retrieve information

	/**
	 * Returns the content of the meta information.
	 *
	 * @return	a <CODE>String</CODE>
	 *
	 * @since	iText0.30
	 */

	public final String content() {
		return content.toString();
	}

	/**
	 * Returns the name of the meta information.
	 *
	 * @return	a <CODE>String</CODE>
	 * 
	 * @since	iText0.30
	 */

	public String name() {
		switch (type) {
		case Element.SUBJECT:
			return "subject";
		case Element.KEYWORDS:
			return "keywords";
		case Element.AUTHOR:
			return "author";
		case Element.TITLE:
			return "title";
		case Element.PRODUCER:
			return "producer";
		case Element.CREATIONDATE:
			return "creationdate";
		default:
			return "unknown";
		}
	}

	/**
	 * Returns the name and content of the meta information.
	 *
	 * @return	a <CODE>String</CODE>
	 *
	 * @since	iText0.30
	 */

	public final String toString() {
		StringBuffer buf = new StringBuffer("\t\t<META NAME=\"");
		buf.append(name());
		buf.append("\" CONTENT=\"");
		buf.append(content());
		buf.append("\"></META>\n");
		return buf.toString();
	}

}