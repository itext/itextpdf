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
import java.util.StringTokenizer;

import org.xml.sax.AttributeList;

import com.lowagie.text.xml.*;
import com.lowagie.text.*;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 *
 * @author  bruno@lowagie.com
 */

public class SAXmyHtmlHandler extends SAXmyHandler {
    
/** These are the properties of the body section. */
    private Properties bodyAttributes = new Properties();
    
/** This is the status of the table border. */
    private boolean tableBorder = false;
    
/**
 * Constructs a new SAXiTextHandler that will translate all the events
 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
 *
 * @param	document	this is the document on which events must be triggered
 */
    
    public SAXmyHtmlHandler(DocListener document) {
        super(document, new HtmlTagMap());
    }
    
/**
 * Constructs a new SAXiTextHandler that will translate all the events
 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
 *
 * @param	document	this is the document on which events must be triggered
 */
    
    public SAXmyHtmlHandler(DocListener document, HashMap htmlTags) {
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
        
        if (((HtmlTagMap)myTags).isHtml(name)) {
            // we do nothing
            return;
        }
        if (((HtmlTagMap)myTags).isHead(name)) {
            // we do nothing
            return;
        }
        if (((HtmlTagMap)myTags).isTitle(name)) {
            // we do nothing
            return;
        }
        if (((HtmlTagMap)myTags).isMeta(name)) {
            // we look if we can change the body attributes
            String meta = null;
            String content = null;
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String attribute = attrs.getName(i);
                    if (attribute.equalsIgnoreCase(HtmlTags.CONTENT))
                        content = attrs.getValue(i);
                    else if (attribute.equalsIgnoreCase(HtmlTags.NAME))
                        meta = attrs.getValue(i);
                }
            }
            if (meta != null && content != null) {
                bodyAttributes.put(meta, content);
            }
            return;
        }
        if (((HtmlTagMap)myTags).isLink(name)) {
            // we do nothing for the moment, in a later version we could extract the style sheet
            return;
        }
        if (((HtmlTagMap)myTags).isBody(name)) {
            // maybe we could extract some info about the document: color, margins,...
            // but that's for a later version...
            XmlPeer peer = new XmlPeer(ElementTags.ITEXT, name);
            super.handleStartingTags(peer.getTag(), bodyAttributes);
            return;
        }
        if (myTags.containsKey(name)) {
            XmlPeer peer = (XmlPeer) myTags.get(name);
            if (Table.isTag(peer.getTag()) || Cell.isTag(peer.getTag())) {
                Properties p = peer.getAttributes(attrs);
                String value;
                if (Table.isTag(peer.getTag()) && (value = p.getProperty(ElementTags.BORDERWIDTH)) != null) {
                    if (Float.valueOf(value + "f").floatValue() > 0) {
                        tableBorder = true;
                    }
                }
                if (tableBorder) {
                    p.put(ElementTags.LEFT, String.valueOf(true));
                    p.put(ElementTags.RIGHT, String.valueOf(true));
                    p.put(ElementTags.TOP, String.valueOf(true));
                    p.put(ElementTags.BOTTOM, String.valueOf(true));
                }
                super.handleStartingTags(peer.getTag(), p);
                return;
            }
            if (attrs.getValue(HtmlTags.STYLE) != null) {
                Properties p = peer.getAttributes(attrs);
                String style = p.getProperty(ElementTags.STYLE);
                if (style == null) {
                    style = "";
                }
                StringTokenizer tokens = new StringTokenizer(attrs.getValue(HtmlTags.STYLE), ";");
                while (tokens.hasMoreTokens()) {
                    StringTokenizer attr = new StringTokenizer(tokens.nextToken(), ":");
                    String key = "";
                    String value = "";
                    if (attr.hasMoreTokens()) {
                        key = attr.nextToken().trim();
                    }
                    if (attr.hasMoreTokens()) {
                        value = attr.nextToken().trim();
                    }
                    if (HtmlTags.CSS_FONTFAMILY.equals(key)) {
                        p.put(ElementTags.FONT, value);
                    }
                    else if (HtmlTags.CSS_FONTSIZE.equals(key)) {
                        if (value.endsWith("px")) {
                            try {
                                value = value.substring(0, value.length() - 2);
                            }
                            catch(Exception e) {
                            }
                        }
                        p.put(ElementTags.SIZE, value);
                    }
                    else if (HtmlTags.CSS_COLOR.equals(key)) { 
                        p.put(ElementTags.COLOR, value);
                    }
                    if (HtmlTags.CSS_FONTWEIGHT.equals(key) || HtmlTags.CSS_FONTSTYLE.equals(key)) {
                        if (style.length() > 0) style += ",";
                        style += value;
                    }
                    else if (HtmlTags.CSS_TEXTDECORATION.equals(key)) { 
                        if (HtmlTags.CSS_UNDERLINE.equals(value)) { 
                            if (style.length() > 0) style += ",";
                            style += ElementTags.UNDERLINE;
                        }
                        else if (HtmlTags.CSS_LINETHROUGH.equals(value)) {
                            if (style.length() > 0) style += ",";
                            style += ElementTags.STRIKETHRU;
                        }
                    }
                    if (style.length() > 0) {
                        p.put(ElementTags.STYLE, style);
                    }
                }
                super.handleStartingTags(peer.getTag(), p);
                return;
            }
            super.handleStartingTags(peer.getTag(), peer.getAttributes(attrs));
            return;
        }
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
    
/**
 * This method gets called when an end tag is encountered.
 *
 * @param	name		the name of the tag that ends
 */
    
    public void endElement(String name) {
        //System.err.println("End: " + name);
        
        if (((HtmlTagMap)myTags).isHead(name)) {
            // we do nothing
            return;
        }
        if (((HtmlTagMap)myTags).isTitle(name)) {
            if (currentChunk != null) {
                bodyAttributes.put(ElementTags.TITLE, currentChunk.content());
            }
            return;
        }
        if (((HtmlTagMap)myTags).isMeta(name)) {
            // we do nothing
            return;
        }
        if (((HtmlTagMap)myTags).isLink(name)) {
            // we do nothing
            return;
        }
        if (((HtmlTagMap)myTags).isBody(name)) {
            // we do nothing
            return;
        }
        if (myTags.containsKey(name)) {
            XmlPeer peer = (XmlPeer) myTags.get(name);
            if (Table.isTag(peer.getTag())) {
                tableBorder = false;
            }
            super.handleEndingTags(peer.getTag());
            return;
        }
        super.handleEndingTags(name);
    }
}