/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text;

import java.net.URL;
import java.net.MalformedURLException;

import java.util.Iterator;
import java.util.Properties;

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
 */

public class Anchor extends Phrase implements TextElementArray {
    
    // membervariables
    
/** This is the anchor tag. */
    public static final String ANCHOR = "anchor";
    
/** This is the name of the <CODE>Anchor</CODE>. */
    private String name = null;
    
/** This is the reference of the <CODE>Anchor</CODE>. */
    private String reference = null;
    
    // constructors
    
/**
 * Constructs an <CODE>Anchor</CODE> without specifying a leading.
 */
    
    public Anchor() {
        super(16);
    }
    
/**
 * Constructs an <CODE>Anchor</CODE> with a certain leading.
 *
 * @param	leading		the leading
 */
    
    public Anchor(float leading) {
        super(leading);
    }
    
/**
 * Constructs an <CODE>Anchor</CODE> with a certain <CODE>Chunk</CODE>.
 *
 * @param	chunk		a <CODE>Chunk</CODE>
 */
    
    public Anchor(Chunk chunk) {
        super(chunk);
    }
    
/**
 * Constructs an <CODE>Anchor</CODE> with a certain <CODE>String</CODE>.
 *
 * @param	string		a <CODE>String</CODE>
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
 */
    
    public Anchor(float leading, Chunk chunk) {
        super(leading, chunk);
    }
    
/**
 * Constructs an <CODE>Anchor</CODE> with a certain leading
 * and a certain <CODE>String</CODE>.
 *
 * @param	leading		the leading
 * @param	string		a <CODE>String</CODE>
 */
    
    public Anchor(float leading, String string) {
        super(leading, string);
    }
    
/**
 * Constructs an <CODE>Anchor</CODE> with a certain leading,
 * a certain <CODE>String</CODE> and a certain <CODE>Font</CODE>.
 *
 * @param	leading		the leading
 * @param	string		a <CODE>String</CODE>
 * @param	font		a <CODE>Font</CODE>
 */
    
    public Anchor(float leading, String string, Font font) {
        super(leading, string, font);
    }
    
/**
 * Returns an <CODE>Anchor</CODE> that has been constructed taking in account
 * the value of some <VAR>attributes</VAR>.
 *
 * @param	attributes		Some attributes
 * @return	an <CODE>Anchor</CODE>
 */
    
    public Anchor(Properties attributes) {
        this("", new Font(attributes));
        String value;
        if ((value = attributes.getProperty(ElementTags.ITEXT)) != null) {
            add(new Chunk(value));
        }
        if ((value = attributes.getProperty(ElementTags.LEADING)) != null) {
            setLeading(Float.valueOf(value + "f").floatValue());
        }
        if ((value = attributes.getProperty(ElementTags.NAME)) != null) {
            setName(value);
        }
        if ((value = attributes.getProperty(ElementTags.REFERENCE)) != null) {
            setReference(value);
        }
    }
    
    // implementation of the Element-methods
    
/**
 * Processes the element by adding it (or the different parts) to an
 * <CODE>ElementListener</CODE>.
 *
 * @param	listener	an <CODE>ElementListener</CODE>
 * @return	<CODE>true</CODE> if the element was processed successfully
 */
    
    public final boolean process(ElementListener listener) {
        try {
            Chunk chunk;
            Iterator i = getChunks().iterator();
            boolean localDestination = (reference != null && reference.startsWith("#"));
            boolean notGotoOK = true;
            while (i.hasNext()) {
                chunk = (Chunk) i.next();
                if (name != null && notGotoOK && !chunk.isEmpty()) {
                    chunk.setLocalDestination(name);
                    notGotoOK = false;
                }
                if (localDestination) {
                    chunk.setLocalGoto(reference.substring(1));
                }
                listener.add(chunk);
            }
            return true;
        }
        catch(DocumentException de) {
            return false;
        }
    }
    
/**
 * Gets all the chunks in this element.
 *
 * @return	an <CODE>ArrayList</CODE>
 */
    
    public ArrayList getChunks() {
        try {
            ArrayList tmp = new ArrayList();
            Chunk chunk;
            Iterator i = getChunks().iterator();
            boolean localDestination = (reference != null && reference.startsWith("#"));
            boolean notGotoOK = true;
            while (i.hasNext()) {
                chunk = (Chunk) i.next();
                if (name != null && notGotoOK && !chunk.isEmpty()) {
                    chunk.setLocalDestination(name);
                    notGotoOK = false;
                }
                if (localDestination) {
                    chunk.setLocalGoto(reference.substring(1));
                }
                tmp.add(chunk);
            }
            return tmp;
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
    
    public final int type() {
        return Element.ANCHOR;
    }
    
    // methods
    
/**
 * Gets an iterator of <CODE>Element</CODE>s.
 *
 * @return	an <CODE>Iterator</CODE>
 */
    
    // suggestion by by Curt Thompson
    public final Iterator getElements() {
        return this.iterator();
    }
    
/**
 * Sets the name of this <CODE>Anchor</CODE>.
 *
 * @param	name		a new name
 */
    
    public final void setName(String name) {
        this.name = name;
    }
    
/**
 * Sets the reference of this <CODE>Anchor</CODE>.
 *
 * @param	reference		a new reference
 */
    
    public final void setReference(String reference) {
        this.reference = reference;
    }
    
    // methods to retrieve information
    
/**
 * Returns the name of this <CODE>Anchor</CODE>.
 *
 * @return	a name
 */
    
    public final String name() {
        return name;
    }
    
/**
 * Gets the reference of this <CODE>Anchor</CODE>.
 *
 * @return	a reference
 */
    
    public final String reference() {
        return reference;
    }
    
/**
 * Gets the reference of this <CODE>Anchor</CODE>.
 *
 * @return	an <CODE>URL</CODE>
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
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.ANCHOR.equals(tag);
    }
}
