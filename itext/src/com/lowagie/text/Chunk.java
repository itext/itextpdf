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
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
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
 * <STRONG>Chunk chunk = new Chunk("Hello world", FontFactory.getFont(FontFactory.COURIER, 20, Font.ITALIC, new Color(255, 0, 0)));</STRONG>
 * document.add(chunk);
 * </PRE></BLOCKQUOTE>
 */

public class Chunk implements Element, MarkupAttributes {
    
    // membervariables
    
/** This is a Chunk containing a newline. */
    public static final Chunk NEWLINE = new Chunk("\n");
    
/** This is the content of this chunk of text. */
    protected StringBuffer content;
    
/** This is the <CODE>Font</CODE> of this chunk of text. */
    protected Font font;
    
/** Contains some of the attributes for this Chunk. */
    protected HashMap attributes;

/** Contains extra markupAttributes */
    protected Properties markupAttributes;
    
/** Key for sub/superscript. */
    public static final String SUBSUPSCRIPT = "SUBSUPSCRIPT";
    
/** Key for underline. */
    public static final String UNDERLINE = "UNDERLINE";
    
/** Key for strikethru. */
    public static final String STRIKETHRU = "STRIKETHRU";
    
/** Key for color. */
    public static final String COLOR = "COLOR";
    
/** Key for encoding. */
    public static final String ENCODING = "ENCODING";
    
/** Key for remote goto. */
    public static final String REMOTEGOTO = "REMOTEGOTO";
    
/** Key for local goto. */
    public static final String LOCALGOTO = "LOCALGOTO";
    
/** Key for local destination. */
    public static final String LOCALDESTINATION = "LOCALDESTINATION";
    
/** Key for image. */
    public static final String IMAGE = "IMAGE";
    
/** Key for generic tag. */
    public static final String GENERICTAG = "GENERICTAG";
    
/** Key for newpage. */
    public static final String NEWPAGE = "NEWPAGE";
    
/** Key for split character. */
    public static final String SPLITCHARACTER = "SPLITCHARACTER";
    
/** Key for Action. */
    public static final String ACTION = "ACTION";
    
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
    
/**
 * Constructs a chunk containing an <CODE>Image</CODE>.
 *
 * @param image the image
 * @param offsetX the image offset in the x direction
 * @param offsetY the image offset in the y direction
 */
    
    public Chunk(Image image, float offsetX, float offsetY) {
        this("*", new Font());
        setAttribute(IMAGE, new Object[]{image, new Float(offsetX), new Float(offsetY)});
    }
    
/**
 * Returns a <CODE>Chunk</CODE> that has been constructed taking in account
 * the value of some <VAR>attributes</VAR>.
 *
 * @param	attributes		Some attributes
 */
    
    public Chunk(Properties attributes) {
        this("", FontFactory.getFont(attributes));
        String value;
        if ((value = (String)attributes.remove(ElementTags.ITEXT)) != null) {
            append(value);
        }
        if ((value = (String)attributes.remove(ElementTags.LOCALGOTO)) != null) {
            setLocalGoto(value);
        }
        if ((value = (String)attributes.remove(ElementTags.REMOTEGOTO)) != null) {
            String destination = (String) attributes.remove(ElementTags.DESTINATION);
            String page = (String) attributes.remove(ElementTags.PAGE);
            if (page != null) { 
                setRemoteGoto(value, Integer.valueOf(page).intValue());
            }
            else if (destination != null) {
                setRemoteGoto(value, destination);
            }
        }
        if ((value = (String)attributes.remove(ElementTags.LOCALDESTINATION)) != null) {
            setLocalDestination(value);
        }
        if ((value = (String)attributes.remove(ElementTags.SUBSUPSCRIPT)) != null) {
            setTextRise(Float.valueOf(value + "f").floatValue());
        }
        if ((value = (String)attributes.remove(ElementTags.GENERICTAG)) != null) {
            setGenericTag(value);
        }
        if (attributes.size() > 0) setMarkupAttributes(attributes);
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
        return (content.toString().trim().length() == 0) && (attributes == null);
    }
    
/**
 * Sets the text displacement relative to the baseline. Positive values rise the text,
 * negative values lower the text.
 * <P>
 * It can be used to implement sub/superscript.
 * @param rise the displacement in points
 * @return this <CODE>Chunk</CODE>
 */
    
    public Chunk setTextRise(float rise) {
        return setAttribute(SUBSUPSCRIPT, new Float(rise));
    }
    
/**
 * Sets an action for this <CODE>Chunk</CODE>.
 *
 * @param action the action
 * @return this <CODE>Chunk</CODE>
 */
    
    public Chunk setAction(PdfAction action) {
        return setAttribute(ACTION, action);
    }
    
/**
 * Sets an anchor for this <CODE>Chunk</CODE>.
 *
 * @param url the <CODE>URL</CODE> to link to
 * @return this <CODE>Chunk</CODE>
 */
    
    public Chunk setAnchor(URL url) {
        return setAttribute(ACTION, new PdfAction(url.toExternalForm()));
    }
    
/**
 * Sets an anchor for this <CODE>Chunk</CODE>.
 *
 * @param url the url to link to
 * @return this <CODE>Chunk</CODE>
 */
    
    public Chunk setAnchor(String url) {
        return setAttribute(ACTION, new PdfAction(url));
    }
    
/**
 * Sets a local goto for this <CODE>Chunk</CODE>.
 * <P>
 * There must be a local destination matching the name.
 * @param name the name of the destination to go to
 * @return this <CODE>Chunk</CODE>
 */
    
    public Chunk setLocalGoto(String name) {
        return setAttribute(LOCALGOTO, name);
    }
    
/**
 * Sets a goto for a remote destination for this <CODE>Chunk</CODE>.
 *
 * @param filename the file name of the destination document
 * @param name the name of the destination to go to
 * @return this <CODE>Chunk</CODE>
 */
    
    public Chunk setRemoteGoto(String filename, String name) {
        return setAttribute(REMOTEGOTO, new Object[]{filename, name});
    }
    
/**
 * Sets a goto for a remote destination for this <CODE>Chunk</CODE>.
 *
 * @param filename the file name of the destination document
 * @param page the page of the destination to go to. First page is 1
 * @return this <CODE>Chunk</CODE>
 */
    
    public Chunk setRemoteGoto(String filename, int page) {
        return setAttribute(REMOTEGOTO, new Object[]{filename, new Integer(page)});
    }
    
/**
 * Sets a local destination for this <CODE>Chunk</CODE>.
 *
 * @param name the name for this destination
 * @return this <CODE>Chunk</CODE>
 */
    public Chunk setLocalDestination(String name) {
        return setAttribute(LOCALDESTINATION, name);
    }
    
/**
 * Sets the generic tag <CODE>Chunk</CODE>.
 * <P>
 * The text for this tag can be retrieved with <CODE>PdfPageEvent</CODE>.
 *
 * @param text the text for the tag
 * @return this <CODE>Chunk</CODE>
 */
    
    public Chunk setGenericTag(String text) {
        return setAttribute(GENERICTAG, text);
    }
    
/**
 * Sets the split characters.
 *
 * @param splitCharacter the <CODE>SplitCharacter</CODE> interface
 * @return this <CODE>Chunk</CODE>
 */
    
    public Chunk setSplitCharacter(SplitCharacter splitCharacter) {
        return setAttribute(SPLITCHARACTER, splitCharacter);
    }
    
/**
 * Sets the generic tag <CODE>Chunk</CODE>.
 * The text for this tag can be retrieved with <CODE>PdfPageEvent</CODE>.
 * @param text the text for the tag
 * @return this <CODE>Chunk</CODE>
 */
    
    public Chunk setNewPage() {
        return setAttribute(NEWPAGE, null);
    }
    
/**
 * Sets an arbitrary attribute.
 *
 * @param name the key for the attribute
 * @param obj the value of the attribute
 * @return this <CODE>Chunk</CODE>
 */
    
    private Chunk setAttribute(String name, Object obj) {
        if (attributes == null)
            attributes = new HashMap();
        attributes.put(name, obj);
        return this;
    }
    
/**
 * Gets the attributes for this <CODE>Chunk</CODE>.
 * <P>
 * It may be null.
 *
 * @return the attributes for this <CODE>Chunk</CODE>
 */
    
    public HashMap getAttributes() {
        return attributes;
    }
    
/**
 * Checks the attributes of this <CODE>Chunk</CODE>.
 *
 * @return false if there aren't any.
 */
    
    public boolean hasAttributes() {
        return attributes != null;
    }
    
/**
 * Returns the image.
 */
    
    public Image getImage() {
        Object obj[] = (Object[])attributes.get(Chunk.IMAGE);
        if (obj == null)
            return null;
        else {
            return (Image)obj[0];
        }
    }
    
/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.CHUNK.equals(tag);
    }
    
    
/**
 * @see com.lowagie.text.MarkupAttributes#setMarkupAttribute(java.lang.String, java.lang.String)
 */
    public void setMarkupAttribute(String name, String value) {
        markupAttributes = (markupAttributes == null) ? new Properties() : markupAttributes;
        markupAttributes.put(name, value);
    }
    
/**
 * @see com.lowagie.text.MarkupAttributes#setMarkupAttributes(java.util.Properties)
 */
    public void setMarkupAttributes(Properties markupAttributes) {
        this.markupAttributes = markupAttributes;
    }
    
/**
 * @see com.lowagie.text.MarkupAttributes#getMarkupAttribute(java.lang.String)
 */
    public String getMarkupAttribute(String name) {
        return (markupAttributes == null) ? null : String.valueOf(markupAttributes.get(name));
    }
    
/**
 * @see com.lowagie.text.MarkupAttributes#getMarkupAttributeNames()
 */
    public Set getMarkupAttributeNames() {
        return getKeySet(markupAttributes);
    }
    
/**
 * @see com.lowagie.text.MarkupAttributes#getMarkupAttributes()
 */
    public Properties getMarkupAttributes() {
        return markupAttributes;
    }
    
    public static Set getKeySet(Hashtable table) {
        // for java2
        return (table == null) ? Collections.EMPTY_SET : table.keySet();
        // for jdk1.1.x
        /*
        if (table == null) return Collections.EMPTY_SET;
        HashSet hash = new HashSet();
        for (Enumeration e = table.keys(); e.hasMoreElements();) hash.add(e.nextElement());
        return hash;
        */
    }
}
