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

import java.util.Properties;
import org.xml.sax.AttributeList;

import com.lowagie.text.ElementTags;

/**
 * This interface is implemented by the peer of all the iText objects.
 *
 * @author  blowagie
 * @version 
 */

public class XmlPeer {

/** This is the name of the alias. */    
    private String tagname;

/** This is the name of the alias. */    
    private String customTagname;
    
/** This is the Map that contains the aliases of the attributes. */
    private Properties attributeAliases = new Properties();
    
/** This is the Map that contains the default values of the attributes. */
    private Properties attributeValues = new Properties();
    
/** This is String that contains the default content of the attributes. */
    private String defaultContent = null;
    
/**
 * Creates a XmlPeer.
 */
    
    public XmlPeer(String name, String alias) {
        this.tagname = name;
        this.customTagname = alias;
    }
    
/**
 * Gets the tagname of the peer.
 */
    
    public String getTag() {
        return tagname;
    }    
    
/**
 * Gets the tagname of the peer.
 */
    
    public String getAlias() {
        return customTagname;
    }
    
/** Gets the list of attributes of the peer. */
    public Properties getAttributes(AttributeList attrs) {
        Properties attributes = new Properties();
        attributes.setProperty(ElementTags.TAGNAME, customTagname);
        attributes.putAll(attributeValues);
        if (defaultContent != null) {
            attributes.put(ElementTags.ITEXT, defaultContent);
        }
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String attribute = getName(attrs.getName(i));
                attributes.setProperty(attribute, attrs.getValue(i));
            }
        }
        return attributes;
    }
    
/**
 * Sets an alias for an attribute.
 *
 * @param   name    the iText tagname
 * @param   alias   the custom tagname
 */
    
    public void addAlias(String name, String alias) {
        attributeAliases.put(alias, name);
    }    
    
/**
 * Sets a value for an attribute.
 *
 * @param   name    the iText tagname
 * @param   value   the default value for this tag
 */
    
    public void addValue(String name, String value) {
        attributeValues.put(name, value);
    }    
    
/**
 * Sets the default content.
 *
 * @param   content    the default content
 */
    
    public void setContent(String content) {
        this.defaultContent = content;
    }
    
/**
 * Returns the iText attribute name.
 *
 * @param   alias   the custom attribute name
 */
    
    public String getName(String name) {
        String value;
        if ((value = attributeAliases.getProperty(name)) != null) {
            return value;
        }
        return name;
    }
    
/**
 * Returns the default values.
 */
    
    public Properties getDefaultValues() {
        return attributeValues;
    }
}

