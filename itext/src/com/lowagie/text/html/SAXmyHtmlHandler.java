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

package com.lowagie.text.html;

import java.util.HashMap;
import java.util.Properties;

import org.xml.sax.AttributeList;

import com.lowagie.text.xml.*;
import com.lowagie.text.*;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 *
 * @author  bruno@lowagie.com
 */

public class SAXmyHtmlHandler extends SAXmyHandler {
    
/**
 * Constructs a new SAXiTextHandler that will translate all the events
 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
 *
 * @param	document	this is the document on which events must be triggered
 */
    
    public SAXmyHtmlHandler(Document document) {
        super(document, new HtmlTagMap());    }
    
/**
 * Constructs a new SAXiTextHandler that will translate all the events
 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
 *
 * @param	document	this is the document on which events must be triggered
 */
    
    public SAXmyHtmlHandler(Document document, HashMap htmlTags) {
        super(document, htmlTags);
    }
    
/**
 * This method gets called when a start tag is encountered.
 *
 * @param	name		the name of the tag that is encountered
 * @param	attrs		the list of attributes
 */
    
    public void startElement(String name, AttributeList attrs) {
        //System.err.println(name);
        
        if (((HtmlTagMap)myTags).isSpecialTag(name)) {
            if (((HtmlTagMap)myTags).isBody(name)) {
                XmlPeer peer = new XmlPeer(ElementTags.ITEXT, HtmlTags.BODY);
                super.handleStartingTags(peer.getTag(), peer.getAttributes(attrs));
            }
        }
        else if (myTags.containsKey(name)) {
            XmlPeer peer = (XmlPeer) myTags.get(name);
            super.handleStartingTags(peer.getTag(), peer.getAttributes(attrs));
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
            super.handleStartingTags(name, attributes);
        }        
    }
    
/**
 * This method gets called when an end tag is encountered.
 *
 * @param	name		the name of the tag that ends
 */
    
    public void endElement(String name) {
        //System.err.println(name);
        
        if (((HtmlTagMap)myTags).isSpecialTag(name)) {
            if (((HtmlTagMap)myTags).isBody(name)) {
                XmlPeer peer = new XmlPeer(ElementTags.ITEXT, HtmlTags.BODY);
                super.handleEndingTags(peer.getTag());
            }
        }
        else if (myTags.containsKey(name)) {
            XmlPeer peer = (XmlPeer) myTags.get(name);
            super.handleEndingTags(peer.getTag());
        }
        else {
            super.handleEndingTags(name);
        }
    }
}