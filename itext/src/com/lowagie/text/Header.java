/*
 * @(#)Header.java					0.23 2000/02/02
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

/**
 * This is an <CODE>Element</CODE> that contains
 * some userdefined meta information about the document.
 *
 * @see		Element
 * @see		Meta
 *
 * @author  bruno@lowagie.com
 * @version 0.23, 2000/02/02
 *
 * @since   iText0.30
 */

public class Header extends Meta implements Element {

// membervariables
		   
	/** This is the content of this chunk of text. */
	private StringBuffer name;

// constructors

	/**
	 * Constructs a <CODE>Meta</CODE>.
	 *
	 * @param	type		the type of meta-information
	 * @param	content		the content
	 *
	 * @since	iText0.30
	 */

	public Header(String name, String content) {
		super(Element.HEADER, content);
		this.name = new StringBuffer(name);
	}

// methods to retrieve information

	/**
	 * Returns the name of the meta information.
	 *
	 * @return	a <CODE>String</CODE>
	 *
	 * @since	iText0.30
	 */

	public String name() {
		return name.toString();
	}
}