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

import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;
import org.xml.sax.Parser;
import org.xml.sax.helpers.ParserFactory;

import com.lowagie.text.*;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 *
 * @author  bruno@lowagie.com
 */

public class TagMap extends HashMap {
    
    class AttributeHandler extends HandlerBase {
        
/** This is a tag */
        public static final String TAG = "tag";
        
/** This is a tag */
        public static final String ATTRIBUTE = "attribute";
        
/** This is an attribute */
        public static final String NAME = "name";
        
/** This is an attribute */
        public static final String ALIAS = "alias";
        
/** This is an attribute */
        public static final String VALUE = "value";
        
/** This is the tagmap using the AttributeHandler */
        private HashMap tagMap;
        
/** This is the current peer. */
        private XmlPeer currentPeer;
        
/**
 * Constructs a new SAXiTextHandler that will translate all the events
 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
 *
 * @param	document	this is the document on which events must be triggered
 */
        
        public AttributeHandler(HashMap tagMap) {
            super();
            this.tagMap = tagMap;
        }
        
/**
 * This method gets called when a start tag is encountered.
 *
 * @param	name		the name of the tag that is encountered
 * @param	attrs		the list of attributes
 */
        
        public void startElement(String tag, AttributeList attrs) {
            String name = attrs.getValue(NAME);
            String alias = attrs.getValue(ALIAS);
            String value = attrs.getValue(VALUE);
            if (name != null) {
                if(TAG.equals(tag)) {
                    currentPeer = new XmlPeer(name, alias);
                }
                else if (ATTRIBUTE.equals(tag)) {
                    if (alias != null) {
                        currentPeer.addAlias(name, alias);
                    }
                    if (value != null) {
                        currentPeer.addValue(name, value);
                    }
                }
            }
            
        }
        
/**
 * This method gets called when ignorable white space encountered.
 *
 * @param	ch		an array of characters
 * @param	start	the start position in the array
 * @param	length	the number of characters to read from the array
 */
        
        public void ignorableWhitespace(char[] ch, int start, int length) {
            // do nothing
        }
        
/**
 * This method gets called when characters are encountered.
 *
 * @param	ch		an array of characters
 * @param	start	the start position in the array
 * @param	length	the number of characters to read from the array
 */
        
        public void characters(char[] ch, int start, int length) {
            // do nothing
        }
        
/**
 * This method gets called when an end tag is encountered.
 *
 * @param	name		the name of the tag that ends
 */
        
        public void endElement(String tag) {
            if (TAG.equals(tag))
                tagMap.put(currentPeer.getAlias(), currentPeer);
        }
    }
    
/** We use this parser to parse the tagfile. */
    private static final String PARSER = "org.apache.xerces.parsers.SAXParser";
    
    public TagMap(String tagfile) {
        super();
        try {
            Parser parser = ParserFactory.makeParser(PARSER);
            parser.setDocumentHandler(new AttributeHandler(this));
            parser.parse(tagfile);
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}