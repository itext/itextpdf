/*
 * $Id$
 * $Name$
 *
 * Copyright (c) 2001 Bruno Lowagie.
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

package com.lowagie.text.xml;

import java.util.HashMap;
import java.util.Properties;

import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;

import com.lowagie.text.*;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 *
 * @author  bruno@lowagie.com
 */

public class SAXmyHandler extends SAXiTextHandler {
    
/** This hashmap contains all the custom keys and peers. */
    protected HashMap myTags;
    
/**
 * Constructs a new SAXiTextHandler that will translate all the events
 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
 *
 * @param	document	this is the document on which events must be triggered
 */
    
    public SAXmyHandler(DocListener document, HashMap myTags) {
        super(document);
        this.myTags = myTags;
    }
    
/**
 * This method gets called when a start tag is encountered.
 *
 * @param	name		the name of the tag that is encountered
 * @param	attrs		the list of attributes
 */
    
    public void startElement(String name, AttributeList attrs) {
        if (myTags.containsKey(name)) {
            XmlPeer peer = (XmlPeer) myTags.get(name);
            handleStartingTags(peer.getTag(), peer.getAttributes(attrs));
        }
        else {
            Properties attributes = new Properties();
            attributes.setProperty(ElementTags.TAGNAME, name);
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String attribute = attrs.getName(i);
                    attributes.setProperty(attribute, attrs.getValue(i));
                }
            }
            handleStartingTags(name, attributes);
        }
    }
    
/**
 * This method gets called when an end tag is encountered.
 *
 * @param	name		the name of the tag that ends
 */
    
    public void endElement(String name) {
        if (myTags.containsKey(name)) {
            XmlPeer peer = (XmlPeer) myTags.get(name);
            handleEndingTags(peer.getTag());
        }
        else {
            handleEndingTags(name);
        }
    }
}