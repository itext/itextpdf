/*
 * @(#)Annotation.java					0.37 2000/10/05
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
import java.util.Iterator;

/**
 * An <CODE>Annotation</CODE> is a little note that can be added to a page
 * on a document.
 *
 * @see		Element
 * @see		Anchor
 *
 * @author  bruno@lowagie.com
 * @version 0.37, 2000/10/05
 *
 * @since   iText0.37
 */

public class Annotation implements Element {

// membervariables

	/** This is the title of the <CODE>Annotation</CODE>. */
	private String title;

	/** This is the content of the <CODE>Annotation</CODE>. */
	private String text;

// constructors

	/**
	 * Constructs an <CODE>Annotation</CODE> with a certain title and some text.
	 *
	 * @param	title	the title of the annotation
	 * @param	text	the content of the annotation
	 *
	 * @since	iText0.37
	 */

	public Annotation(String title, String text) {
		this.title = title;
		this.text = text;
	}

// implementation of the Element-methods

    /**
     * Gets the type of the text element. 
     *
     * @return	a type
     * @since	iText0.37
     */

    public final int type() {
		return Element.ANNOTATION;
	}

// methods

    /**
     * Processes the element by adding it (or the different parts) to a
	 * <CODE>DocListener</CODE>. 
     *
	 * <CODE>true</CODE> if the element was processed successfully
     * @since   iText0.37
     */

    public boolean process(DocListener listener) {
		try {
			return listener.add(this);
		}
		catch(DocumentException de) {
			return false;
		}
	}


	/**
	 * Gets an iterator of <CODE>Element</CODE>s. 
     *
     * @return	an <CODE>Iterator</CODE>.
	 *
     * @since	iText0.37
	 */

	public final Iterator getElements() {
		return new ArrayList().iterator();
	}		

    /**
     * Gets all the chunks in this element. 
     *
     * @return	an <CODE>ArrayList</CODE>
	 *
     * @since	iText0.37
     */

    public ArrayList getChunks() {
		 return new ArrayList();
	}

// methods to retrieve information

	/**
	 * Returns the title of this <CODE>Annotation</CODE>.
	 *
	 * @return	a name
	 * @since	iText0.37
	 */

	public final String title() {
		return title;
	}

	/**
	 * Gets the content of this <CODE>Annotation</CODE>.
	 *
	 * @return	a reference
	 * @since	iText0.37
	 */

	public final String content() {
		return text;			  	
	}

	/**
	 * Returns a representation of this <CODE>Anchor</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 * @since	iText0.30
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("<ANNOTATION");
		if (title != null) {
			buf.append(" TITLE=\"");
			buf.append(title);
		}
		if (text != null) {
			buf.append("\" CONTENT=\"");
			buf.append(text);
		}
		buf.append("\">\n");
		buf.append("</ANNOTATION>\n");								
		return buf.toString();
	}
}