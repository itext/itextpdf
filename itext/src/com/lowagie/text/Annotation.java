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
import java.util.Iterator;
import java.util.Properties;

/**
 * An <CODE>Annotation</CODE> is a little note that can be added to a page
 * on a document.
 *
 * @see		Element
 * @see		Anchor
 *
 * @author  bruno@lowagie.com
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
 */
    
    public Annotation(String title, String text) {
        this.title = title;
        this.text = text;
    }
    
        /**
         * Returns an <CODE>Annotation</CODE> that has been constructed taking in account
         * the value of some <VAR>attributes</VAR>.
         *
         * @param	attributes		Some attributes
         * @return	an <CODE>Annotation</CODE>
         */
    
    public Annotation(Properties attributes) {
        title = attributes.getProperty(ElementTags.TITLE);
        text = attributes.getProperty(ElementTags.CONTENT);
        if (title == null) {
            title = "";
        }
        if (text == null) {
            text = "";
        }
    }
    
    // implementation of the Element-methods
    
/**
 * Gets the type of the text element.
 *
 * @return	a type
 */
    
    public final int type() {
        return Element.ANNOTATION;
    }
    
    // methods
    
/**
 * Processes the element by adding it (or the different parts) to an
 * <CODE>ElementListener</CODE>.
 *
 * @param	listener 	an <CODE>ElementListener</CODE>
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
 * Gets an iterator of <CODE>Element</CODE>s.
 *
 * @return	an <CODE>Iterator</CODE>
 */
    
    public final Iterator getElements() {
        return new ArrayList().iterator();
    }
    
/**
 * Gets all the chunks in this element.
 *
 * @return	an <CODE>ArrayList</CODE>
 */
    
    public ArrayList getChunks() {
        return new ArrayList();
    }
    
    // methods to retrieve information
    
/**
 * Returns the title of this <CODE>Annotation</CODE>.
 *
 * @return	a name
 */
    
    public final String title() {
        return title;
    }
    
/**
 * Gets the content of this <CODE>Annotation</CODE>.
 *
 * @return	a reference
 */
    
    public final String content() {
        return text;
    }
    
/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.ANNOTATION.equals(tag);
    }
    
/**
 * Returns a representation of this <CODE>Anchor</CODE>.
 *
 * @return	a <CODE>String</CODE>
 */
    
    public String toString() {
        StringBuffer buf = new StringBuffer("<").append(ElementTags.ANNOTATION).append("");
        if (title != null) {
            buf.append(" ").append(ElementTags.TITLE).append("=\"");
            buf.append(title);
        }
        if (text != null) {
            buf.append("\" ").append(ElementTags.CONTENT).append("=\"");
            buf.append(text);
        }
        buf.append("\" />");
        return buf.toString();
    }
}
