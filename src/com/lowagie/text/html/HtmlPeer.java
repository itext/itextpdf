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

import java.util.Properties;
import org.xml.sax.AttributeList;

import com.lowagie.text.xml.XmlPeer;

/**
 * This interface is implemented by the peer of all the iText objects.
 *
 * @author  blowagie
 * @version
 */

public class HtmlPeer extends XmlPeer {
    
/**
 * Creates a XmlPeer.
 */
    
    public HtmlPeer(String name, String alias) {
        super(name, alias.toLowerCase());
    }
    
/**
 * Sets an alias for an attribute.
 *
 * @param   name    the iText tagname
 * @param   alias   the custom tagname
 */
    
    public void addAlias(String name, String alias) {
        attributeAliases.put(alias.toLowerCase(), name);
    }
}

