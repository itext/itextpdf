/*
 * $Id$
 * $Name$
 *
 * Copyright 2007 by Bruno Lowagie.
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
package com.lowagie.text.factories;

import java.util.Properties;

import com.lowagie.text.Chunk;
import com.lowagie.text.ElementTags;
import com.lowagie.text.FontFactory;
import com.lowagie.text.List;
import com.lowagie.text.html.Markup;

/**
 * This class is able to create Element objects based on a list of properties.
 */

public class ElementFactory {

	public static Chunk getChunk(Properties attributes) {
		Chunk chunk = new Chunk();
		
		chunk.setFont(FontFactory.getFont(attributes));
		String value;
		
		value = attributes.getProperty(ElementTags.ITEXT);
		if (value != null) {
			chunk.append(value);
		}
		value = attributes.getProperty(ElementTags.LOCALGOTO);
		if (value != null) {
			chunk.setLocalGoto(value);
		}
		value = attributes.getProperty(ElementTags.REMOTEGOTO);
		if (value != null) {
			String page = attributes.getProperty(ElementTags.PAGE);
			if (page != null) {
				chunk.setRemoteGoto(value, Integer.parseInt(page));
			}
			else {
				String destination = attributes.getProperty(ElementTags.DESTINATION);
				if (destination != null) {
					chunk.setRemoteGoto(value, destination);
				}
			}
		}
		value = attributes.getProperty(ElementTags.LOCALDESTINATION);
		if (value != null) {
			chunk.setLocalDestination(value);
		}
		value = attributes.getProperty(ElementTags.SUBSUPSCRIPT);
		if (value != null) {
			chunk.setTextRise(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(Markup.CSS_KEY_VERTICALALIGN);
		if (value != null && value.endsWith("%")) {
			float p = Float.parseFloat(
					value.substring(0, value.length() - 1) + "f") / 100f;
			chunk.setTextRise(p * chunk.getFont().size());
		}
		value = attributes.getProperty(ElementTags.GENERICTAG);
		if (value != null) {
			chunk.setGenericTag(value);
		}
		value = attributes.getProperty(ElementTags.BACKGROUNDCOLOR);
		if (value != null) {
			chunk.setBackground(Markup.decodeColor(value));
		}
		return chunk;
	}
	
	public static List getList(Properties attributes) {
		List list = new List();

		list.setNumbered("true".equalsIgnoreCase(attributes.getProperty(ElementTags.NUMBERED)));
		list.setLettered("true".equalsIgnoreCase(attributes.getProperty(ElementTags.LETTERED)));
		list.setLowercase("true".equalsIgnoreCase(attributes.getProperty(ElementTags.LOWERCASE)));
		list.setAutoindent("true".equalsIgnoreCase(attributes.getProperty(ElementTags.AUTO_INDENT_ITEMS)));
		list.setAlignindent("true".equalsIgnoreCase(attributes.getProperty(ElementTags.ALIGN_INDENTATION_ITEMS)));
		
		String value;
		
        value = attributes.getProperty(ElementTags.FIRST);
        if (value != null) {
            char character = value.charAt(0);
            if (Character.isLetter(character) ) {
                list.setFirst(character);
            }
            else {
                list.setFirst(Integer.parseInt(value));
            }
        }
        
		value= attributes.getProperty(ElementTags.LISTSYMBOL);
		if (value != null) {
			list.setListSymbol(new Chunk(value, FontFactory.getFont(attributes)));
		}
        
        value = attributes.getProperty(ElementTags.INDENTATIONLEFT);
        if (value != null) {
            list.setIndentationLeft(Float.parseFloat(value + "f"));
        }
        
        value = attributes.getProperty(ElementTags.INDENTATIONRIGHT);
        if (value != null) {
            list.setIndentationRight(Float.parseFloat(value + "f"));
        }
        
        value = attributes.getProperty(ElementTags.SYMBOLINDENT);
        if (value != null) {
            list.setSymbolIndent(Float.parseFloat(value));
        }
        
		return list;
	}
}