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

import java.util.HashMap;
import java.util.Properties;

import org.xml.sax.Attributes;

import com.itextpdf.text.DocListener;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 */

public class SAXmyHandler extends SAXiTextHandler {
    
/**
 * Constructs a new SAXiTextHandler that will translate all the events
 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
 *
 * @param	document	this is the document on which events must be triggered
 * @param myTags a user defined tagmap
 */
    
    public SAXmyHandler(DocListener document, HashMap myTags) {
        super(document, myTags);
    }
    
/**
 * This method gets called when a start tag is encountered.
 * 
	 * @param   uri 		the Uniform Resource Identifier
	 * @param   lname 		the local name (without prefix), or the empty string if Namespace processing is not being performed.
 * @param	name		the name of the tag that is encountered
 * @param	attrs		the list of attributes
 */
    
    public void startElement(String uri, String lname, String name, Attributes attrs) {
        if (myTags.containsKey(name)) {
            XmlPeer peer = (XmlPeer) myTags.get(name);
            handleStartingTags(peer.getTag(), peer.getAttributes(attrs));
        }
        else {
            Properties attributes = new Properties();
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String attribute = attrs.getQName(i);
                    attributes.setProperty(attribute, attrs.getValue(i));
                }
            }
            handleStartingTags(name, attributes);
        }
    }
    
    /**
 	 * This method gets called when an end tag is encountered.
 	 *
	 * @param   uri 		the Uniform Resource Identifier
	 * @param   lname 		the local name (without prefix), or the empty string if Namespace processing is not being performed.
	 * @param	name		the name of the tag that ends
	 */
    
    public void endElement(String uri, String lname, String name) {
        if (myTags.containsKey(name)) {
            XmlPeer peer = (XmlPeer) myTags.get(name);
            handleEndingTags(peer.getTag());
        }
        else {
            handleEndingTags(name);
        }
    }
}