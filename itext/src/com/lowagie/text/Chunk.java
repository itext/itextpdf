/*
 * @(#)Chunk.java					0.39 2000/11/23
 *       release iText0.30			0.25 2000/02/14
 *       release iText0.35			0.25 2000/08/11
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
 * This is the smallest significant part of text that can be added to a document.
 * <P>
 * Most elements can be divided in one or more <CODE>Chunk</CODE>s.
 * A chunk is a <CODE>String</CODE> with a certain <CODE>Font</CODE>.
 * all other layoutparameters should be defined in the object to which
 * this chunk of text is added.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * <STRONG>Chunk chunk = new Chunk("Hello world", new Font(Font.COURIER, 20, Font.ITALIC, new Color(255, 0, 0)));</STRONG>
 * document.add(chunk);
 * </PRE></BLOCKQUOTE>
 *
 * @author  bruno@lowagie.com
 * @version 0.39, 2000/11/23
 *
 * @since   iText0.30
 */

public class Chunk implements Element {

// membervariables
		   
	/** This is the content of this chunk of text. */
	private StringBuffer content;

	/** This is the <CODE>Font</CODE> of this chunk of text. */
	private Font font;

// constructors

	/**
	 * Constructs a chunk of text with a certain content and a certain <CODE>Font</CODE>.
	 *
	 * @param	content		the content
	 * @param	font		the font
	 * @since	iText0.30
	 */

	public Chunk(String content, Font font) {
		this.content = new StringBuffer(content);
		this.font = font;
	}

	/**
	 * Constructs a chunk of text with a certain content, without specifying a <CODE>Font</CODE>.
	 *
	 * @param	content		the content
	 * @since	iText0.30
	 */

	public Chunk(String content) {
		this(content, new Font());
	}

// implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to a
	 * <CODE>DocListener</CODE>. 
     *
	 * <CODE>true</CODE> if the element was processed successfully
     * @since   iText0.30
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
     * Gets the type of the text element. 
     *
     * @return	a type
     * @since	iText0.30
     */

    public int type() {
		return Element.CHUNK;
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
		 tmp.add(this);
		 return tmp;
	}

// methods

	/**
	 * appends some text to this <CODE>Chunk</CODE>.
	 *
	 * @param	a <CODE>String</CODE>
	 * @return	a <CODE>StringBuffer</CODE>
	 *
	 * @since	iText0.30
	 */

	public StringBuffer append(String string) {
		return content.append(string);
	}

// methods to retrieve information

	/**
	 * Gets the font of this <CODE>Chunk</CODE>.
	 *
	 * @return	a <CODE>Font</CODE>
	 *
     * @since   iText0.30
	 */

	public final Font font() {
		return font;
	}

	/**
	 * Sets the font of this <CODE>Chunk</CODE>.
	 *
	 * @param	a <CODE>Font</CODE>
	 *
     * @since   iText0.39
	 */

	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Returns the content of this <CODE>Chunk</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 * 
	 * @since	iText0.30
	 */

	public final String content() {
		return content.toString();
	}

	/**
	 * Checks is this <CODE>Chunk</CODE> is empty.
	 *
	 * @return	<CODE>false</CODE> if the Chunk contains other characters than space.
	 * 
	 * @since	iText0.30
	 */

	public final boolean isEmpty() {
		String tmp = content.toString();
		int l = tmp.length();
		for (int i = 0; i < l; i++) {
			if (tmp.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns a representation of this <CODE>Chunk</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 * 
	 * @since	iText0.30
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("<CHUNK>\n");
		buf.append(font);
		buf.append("\t");
		buf.append(content());
		buf.append("\n</CHUNK>\n");
		return buf.toString();
	}
}