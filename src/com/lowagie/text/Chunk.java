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

import java.util.ArrayList;
import java.util.HashMap;
import java.net.URL;
import com.lowagie.text.pdf.PdfAction;

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
 */

public class Chunk implements Element {

// membervariables
		   
	/** This is the content of this chunk of text. */
	private StringBuffer content;

	/** This is the <CODE>Font</CODE> of this chunk of text. */
	private Font font;
    
/** Contains some of the attributes for this Chunk.
 */    
    private HashMap attributes;
    
/** Key for an URL link.
 */    
    public static final String LINK = "LINK";
/** Key for sub/superscript.
 */    
    public static final String SUBSUPSCRIPT = "SUBSUPSCRIPT";
/** Key for underline.
 */    
    public static final String UNDERLINE = "UNDERLINE";
/** Key for strikethru.
 */    
    public static final String STRIKETHRU = "STRIKETHRU";
/** Key for color.
 */    
    public static final String COLOR = "COLOR";
/** Key for encoding.
 */    
    public static final String ENCODING = "ENCODING";

/** Key for local goto.
 */    
    public static final String LOCALGOTO = "LOCALGOTO";

/** Key for local destination.
 */    
    public static final String LOCALDESTINATION = "LOCALDESTINATION";

/** Key for generic tag.
 */    
    public static final String GENERICTAG = "GENERICTAG";

// constructors

	/**
	 * Constructs a chunk of text with a certain content and a certain <CODE>Font</CODE>.
	 *
	 * @param	content		the content
	 * @param	font		the font
	 */

	public Chunk(String content, Font font) {
		this.content = new StringBuffer(content);
		this.font = font;
	}

	/**
	 * Constructs a chunk of text with a certain content, without specifying a <CODE>Font</CODE>.
	 *
	 * @param	content		the content
	 */

	public Chunk(String content) {
		this(content, new Font());
	}

// implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to an
	 * <CODE>ElementListener</CODE>. 
     *
	 * @param	listener	an <CODE>ElementListener</CODE>
	 * @return	<CODE>true</CODE> if the element was processed successfully
     */

    public boolean process(ElementListener listener) {
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
     */

    public int type() {
		return Element.CHUNK;
	}		

    /**
     * Gets all the chunks in this element. 
     *
     * @return	an <CODE>ArrayList</CODE>
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
	 * @param	string <CODE>String</CODE>
	 * @return	a <CODE>StringBuffer</CODE>
	 */

	public StringBuffer append(String string) {
		return content.append(string);
	}

// methods to retrieve information

	/**
	 * Gets the font of this <CODE>Chunk</CODE>.
	 *
	 * @return	a <CODE>Font</CODE>
	 */

	public final Font font() {
		return font;
	}

	/**
	 * Sets the font of this <CODE>Chunk</CODE>.
	 *
	 * @param	font a <CODE>Font</CODE>
	 */

	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Returns the content of this <CODE>Chunk</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 */

	public final String content() {
		return content.toString();
	}

	/**
	 * Checks is this <CODE>Chunk</CODE> is empty.
	 *
	 * @return	<CODE>false</CODE> if the Chunk contains other characters than space.
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
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("<chunk");
		buf.append(font.toString());
		buf.append(">");
		buf.append("\t");
		buf.append(content());
		buf.append("\n</chunk>\n");
		return buf.toString();
	}

/** Sets the text displacement relative to the baseline. Positive values rise the text,
 * negative values lower the text.
 * <P>
 * It can be used to implement sub/superscript.
 * @param rise the displacement in points
 * @return this <CODE>Chunk</CODE>
 */    
    public Chunk setTextRise(float rise)
    {
        return setAttribute(SUBSUPSCRIPT, new Float(rise));
    }
    
/** Sets an anchor for this <CODE>Chunk</CODE>.
 * @param url the <CODE>URL</CODE> to link to
 * @return this <CODE>Chunk</CODE>
 */    
    public Chunk setAnchor(URL url)
    {
        return setAttribute(LINK, new PdfAction(url));
    }
    
/** Sets a local goto for this <CODE>Chunk</CODE>.
 * There must be a local destination matching the name.
 * @param name the name of the destination to go to
 * @return this <CODE>Chunk</CODE>
 */    
    public Chunk setLocalGoto(String name)
    {
        return setAttribute(LOCALGOTO, name);
    }
    
/** Sets a local destination for this <CODE>Chunk</CODE>.
 * @param name the name for this destination
 * @return this <CODE>Chunk</CODE>
 */    
    public Chunk setLocalDestination(String name)
    {
        return setAttribute(LOCALDESTINATION, name);
    }
    
/** Sets the generic tag <CODE>Chunk</CODE>.
 * The text for this tag can be retrieved with <CODE>PdfPageEvent</CODE>.
 * @param text the text for the tag
 * @return this <CODE>Chunk</CODE>
 */    
    public Chunk setGenericTag(String text)
    {
        return setAttribute(GENERICTAG, text);
    }
    
/** Sets an arbitrary attribute.
 * @param name the key for the attribute
 * @param obj the value of the attribute
 * @return this <CODE>Chunk</CODE>
 */    
    private Chunk setAttribute(String name, Object obj)
    {
        if (attributes == null)
            attributes = new HashMap();
        attributes.put(name, obj);
        return this;
    }

/** Gets the attributes for this <CODE>Chunk</CODE>.<BR>
 * It may be null.
 * @return the attributes for this <CODE>Chunk</CODE>
 */    
    public HashMap getAttributes()
    {
        return attributes;
    }
}