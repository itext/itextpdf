/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
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

import java.util.TreeMap;
import java.util.Iterator;

/**
 * This class contains a series of attributes of a Tag.
 *
 * @author  bruno@lowagie.com
 */

class HtmlAttributes extends TreeMap {
    
    // constructor
    
/**
 * Constructs this object.
 */
    
    public HtmlAttributes() {
        super(new com.lowagie.text.StringCompare());
    }
    
    // methods
    
/**
 * Shows all the attributes as a <CODE>String</CODE> that can be
 * inserted into a tag.
 *
 * @return	a <CODE>String</CODE>
 */
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        String key;
        String value;
        for (java.util.Iterator i = keySet().iterator(); i.hasNext(); ) {
            key = (String) i.next();
            value = (String) get(key);
            buffer.append(' ');
            buffer.append(key);
            if (value != null) {
                buffer.append('=');
                buffer.append('"');
                buffer.append(value);
                buffer.append('"');
            }
        }
        return buffer.toString();
    }
    
}