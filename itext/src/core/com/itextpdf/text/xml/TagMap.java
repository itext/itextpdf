/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import com.itextpdf.text.ExceptionConverter;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 */

public class TagMap extends HashMap {

    private static final long serialVersionUID = -6809383366554350820L;

	class AttributeHandler extends DefaultHandler {
        
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
        
/** This is an attribute */
        public static final String CONTENT = "content";
        
/** This is the tagmap using the AttributeHandler */
        private HashMap tagMap;
        
/** This is the current peer. */
        private XmlPeer currentPeer;
        
/**
 * Constructs a new SAXiTextHandler that will translate all the events
 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
 *
 * @param	tagMap  A Hashmap containing XmlPeer-objects
 */
        
        public AttributeHandler(HashMap tagMap) {
            super();
            this.tagMap = tagMap;
        }
        
/**
 * This method gets called when a start tag is encountered.
 *
 * @param   uri 		the Uniform Resource Identifier
 * @param   lname 		the local name (without prefix), or the empty string if Namespace processing is not being performed.
 * @param	tag 		the name of the tag that is encountered
 * @param	attrs		the list of attributes
 */
        
        public void startElement(String uri, String lname, String tag, Attributes attrs) {
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
            value = attrs.getValue(CONTENT);
            if (value != null) {
                currentPeer.setContent(value);
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
 * @param   uri 		the Uniform Resource Identifier
 * @param   lname 		the local name (without prefix), or the empty string if Namespace processing is not being performed.
 * @param	tag		the name of the tag that ends
 */
        
        public void endElement(String uri, String lname, String tag) {
            if (TAG.equals(tag))
                tagMap.put(currentPeer.getAlias(), currentPeer);
        }
    }
    
    /**
     * Constructs a TagMap
     * @param tagfile the path to an XML file with the tagmap
     */
    public TagMap(String tagfile) {
        super();
        try {
            init(TagMap.class.getClassLoader().getResourceAsStream(tagfile));
        }catch(Exception e) {
        	try {
				init(new FileInputStream(tagfile));
			} catch (FileNotFoundException fnfe) {
				throw new ExceptionConverter(fnfe);
			}
        }
    }

    /**
     * Constructs a TagMap.
     * @param in	An InputStream with the tagmap xml
     */
    public TagMap(InputStream in) {
        super();
        init(in);
    }

    protected void init(InputStream in) {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new InputSource(in), new AttributeHandler(this));
        }
        catch(Exception e) {
            throw new ExceptionConverter(e);
        }
    }


}
