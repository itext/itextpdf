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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

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
 * // When no parameters are passed, the default leading = 16
 * <STRONG>Phrase phrase0 = new Phrase();</STRONG>
 * <STRONG>Phrase phrase1 = new Phrase("this is a phrase");</STRONG>
 * // In this example the leading is passed as a parameter
 * <STRONG>Phrase phrase2 = new Phrase(16, "this is a phrase with leading 16");</STRONG>
 * // When a Font is passed (explicitely or embedded in a chunk), the default leading = 1.5 * size of the font
 * <STRONG>Phrase phrase3 = new Phrase("this is a phrase with a red, normal font Courier, size 12", new Font(Font.COURIER, 12, Font.NORMAL, new Color(255, 0, 0)));</STRONG>
 * <STRONG>Phrase phrase4 = new Phrase(new Chunk("this is a phrase"));</STRONG>
 * <STRONG>Phrase phrase5 = new Phrase(18, new Chunk("this is a phrase", new Font(Font.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));</STRONG>
 * </PRE></BLOCKQUOTE>
 *
 * @see		Element
 * @see		Chunk
 * @see		Paragraph
 * @see		Anchor
 */

public class Phrase extends ArrayList implements TextElementArray {
    
    // membervariables
    
/** This is the leading of this phrase. */
    protected float leading;
    
/** This is the font of this phrase. */
    protected Font font = new Font();
    
    // constructors
    
/**
 * Constructs a <CODE>Phrase</CODE> without specifying a leading.
 */
    
    public Phrase() {
        this(16);
    }
    
/**
 * Constructs a <CODE>Phrase</CODE> with a certain leading.
 *
 * @param	leading		the leading
 */
    
    public Phrase(float leading) {
        this.leading = leading;
    }
    
/**
 * Constructs a <CODE>Phrase</CODE> with a certain <CODE>Chunk</CODE>.
 *
 * @param	chunk		a <CODE>Chunk</CODE>
 */
    
    public Phrase(Chunk chunk) {
        this(chunk.font().leading(1.5f));
        super.add(chunk);
    }
    
/**
 * Constructs a <CODE>Phrase</CODE> with a certain <CODE>Chunk</CODE>
 * and a certain leading.
 *
 * @param	leading	the leading
 * @param	chunk		a <CODE>Chunk</CODE>
 */
    
    public Phrase(float leading, Chunk chunk) {
        this(leading);
        super.add(chunk);
    }
    
/**
 * Constructs a <CODE>Phrase</CODE> with a certain <CODE>String</CODE>.
 *
 * @param	string		a <CODE>String</CODE>
 */
    
    public Phrase(String string) {
        this(new Font().leading(1.5f), string, new Font());
    }
    
/**
 * Constructs a <CODE>Phrase</CODE> with a certain <CODE>String</CODE> and a certain <CODE>Font</CODE>.
 *
 * @param	string		a <CODE>String</CODE>
 * @param	font		a <CODE>Font</CODE>
 */
    
    public Phrase(String string, Font font) {
        this(font.leading(1.5f), string, font);
        this.font = font;
    }
    
/**
 * Constructs a <CODE>Phrase</CODE> with a certain leading and a certain <CODE>String</CODE>.
 *
 * @param	leading	the leading
 * @param	string		a <CODE>String</CODE>
 */
    
    public Phrase(float leading, String string) {
        this(leading, string, new Font());
    }
    
/**
 * Constructs a <CODE>Phrase</CODE> with a certain leading, a certain <CODE>String</CODE>
 * and a certain <CODE>Font</CODE>.
 *
 * @param	leading	the leading
 * @param	string		a <CODE>String</CODE>
 * @param	font		a <CODE>Font</CODE>
 */
    
    public Phrase(float leading, String string, Font font) {
        this(leading);
        this.font = font;
        if (font.family() != Font.SYMBOL && font.family() != Font.ZAPFDINGBATS && font.getBaseFont() == null) {
            int i = 0;
            int index;
            while((index = Greek.index(string)) > -1) {
                if (index > 0) {
                    String firstPart = string.substring(0, index);
                    /* bugfix [ #461272 ] CODE CHANGE REQUIRED IN Phrase.java
                       by Arekh Nambiar */
                    super.add(new Chunk(firstPart, font));
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
        if (string.length() != 0) {
            super.add(new Chunk(string, font));
        }
    }
    
/**
 * Returns a <CODE>Phrase</CODE> that has been constructed taking in account
 * the value of some <VAR>attributes</VAR>.
 *
 * @param	attributes		Some attributes
 */
    
    public Phrase(Properties attributes) {
        this("", new Font(attributes));
        clear();
        String value = (String)attributes.remove(ElementTags.LEADING);
        if (value != null) {
            setLeading(Float.valueOf(value + "f").floatValue());
        }
        if ((value = (String)attributes.remove(ElementTags.ITEXT)) != null) {
            Chunk chunk = new Chunk(value);
            if ((value = (String)attributes.remove(ElementTags.GENERICTAG)) != null) {
                chunk.setGenericTag(value);
            }
            add(chunk);
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
    
    public boolean process(ElementListener listener) {
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
 */
    
    public int type() {
        return Element.PHRASE;
    }
    
/**
 * Gets all the chunks in this element.
 *
 * @return	an <CODE>ArrayList</CODE>
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
 */
    
    public void add(int index, Object o) {
        try {
            Element element = (Element) o;
            if (element.type() == Element.CHUNK) {
                Chunk chunk = (Chunk) element;
                if (!font.isStandardFont()) {
                    chunk.setFont(font.difference(chunk.font()));
                }
                super.add(index, chunk);
            }
            else if (element.type() == Element.PHRASE ||
            element.type() == Element.ANCHOR ||
            element.type() == Element.ANNOTATION ||
            element.type() == Element.TABLE || // line added by David Freels
            element.type() == Element.GRAPHIC) {
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
 */
    
    public boolean add(Object o) {
        if (o instanceof String) {
            return super.add(new Chunk((String) o, font));
        }
        try {
            Element element = (Element) o;
            switch(element.type()) {
                case Element.CHUNK:
                    return addChunk((Chunk) o);
                case Element.PHRASE:
                case Element.PARAGRAPH:
                    Phrase phrase = (Phrase) o;
                    boolean success = true;
                    Element e;
                    for (Iterator i = phrase.iterator(); i.hasNext(); ) {
                        e = (Element) i.next();
                        if (e instanceof Chunk) {
                            success &= addChunk((Chunk)e);
                        }
                        else {
                            success &= this.add(e);
                        }
                    }
                    return success;
                case Element.ANCHOR:
                    return super.add((Anchor) o);
                case Element.ANNOTATION:
                    return super.add((Annotation) o);
                case Element.TABLE: // case added by David Freels
                    return super.add((Table) o);
                case Element.LIST:
                    return super.add((List) o);
                    default:
                        throw new ClassCastException(String.valueOf(element.type()));
            }
        }
        catch(ClassCastException cce) {
            throw new ClassCastException("Insertion of illegal Element: " + cce.getMessage());
        }
    }
    
/**
 * Adds a Chunk.
 * <p>
 * This method is a hack to solve a problem I had with phrases that were split between chunks
 * in the wrong place.
 */
    
    private synchronized boolean addChunk(Chunk chunk) {
        if (!font.isStandardFont()) {
            chunk.setFont(font.difference(chunk.font()));
        }
        if (size() > 0 && !chunk.hasAttributes()) {
            try {
                Chunk previous = (Chunk) get(size() - 1);
                if (!previous.hasAttributes() && previous.font().compareTo(chunk.font()) == 0 && !"".equals(previous.content().trim()) && !"".equals(chunk.content().trim())) {
                    previous.append(chunk.content());
                    return true;
                }
            }
            catch(ClassCastException cce) {
            }
        }
        return super.add(chunk);
    }
    
/**
 * Adds a collection of <CODE>Chunk</CODE>s
 * to this <CODE>Phrase</CODE>.
 *
 * @param	collection	a collection of <CODE>Chunk</CODE>s, <CODE>Anchor</CODE>s and <CODE>Phrase</CODE>s.
 * @return	<CODE>true</CODE> if the action succeeded, <CODE>false</CODE> if not.
 * @throws	ClassCastException	when you try to add something that isn't a <CODE>Chunk</CODE>, <CODE>Anchor</CODE> or <CODE>Phrase</CODE>
 */
    
    public boolean addAll(Collection collection) {
        for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            this.add(iterator.next());
        }
        return true;
    }
    
/**
 * Adds a <CODE>Image</CODE> to the <CODE>Paragraph</CODE>.
 *
 * @param	image		the image to add.
 */
    
    protected void addSpecial(Object object) {
        super.add(object);
    }
    
    // methods
    
/**
 * Sets the leading of this phrase.
 *
 * @param	leading		the new leading
 * @return	<CODE>void</CODE>
 */
    
    public final void setLeading(float leading) {
        this.leading = leading;
    }
    
    // methods to retrieve information
    
/**
 * Checks is this <CODE>Phrase</CODE> contains no or 1 empty <CODE>Chunk</CODE>.
 *
 * @return	<CODE>false</CODE> if the <CODE>Phrase</CODE>
 * contains more than one or more non-empty<CODE>Chunk</CODE>s.
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
 * @return	the linespacing
 */
    
    public final float leading() {
        return leading;
    }
    
/**
 * Gets the font of the first <CODE>Chunk</CODE> that appears in this <CODE>Phrase</CODE>.
 *
 * @return	a <CODE>Font</CODE>
 */
    
    public final Font font() {
        return font;
    }
    
/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.PHRASE.equals(tag);
    }
}
