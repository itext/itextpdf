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
package com.itextpdf.text.html.simpleparser;

import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.html.Markup;

public class StyleSheet {
	
	/**
	 * Map storing tags and their corresponding styles.
	 * @since 5.0.6 (changed HashMap => Map)
	 */
	protected Map<String, Map<String, String>> tagMap = new HashMap<String, Map<String, String>>();
	
	/**
	 * Map storing possible names of the "class" attribute
	 * and their corresponding styles.
	 * @since 5.0.6 (changed HashMap => Map)
	 */
	protected Map<String, Map<String, String>> classMap = new HashMap<String, Map<String, String>>();

	/**
	 * Creates a new instance of StyleSheet
	 */
	public StyleSheet() {
	}

	/**
	 * Associates a Map containing styles with a tag.
	 * @param	tag		the name of the HTML/XML tag
	 * @param	attrs	a map containing styles
	 */
	public void loadTagStyle(String tag, Map<String, String> attrs) {
		tagMap.put(tag.toLowerCase(), attrs);
	}

	/**
	 * Adds an extra style key-value pair to the styles Map
	 * of a specific tag
	 * @param	tag		the name of the HTML/XML tag
	 * @param	key		the key specifying a specific style
	 * @param	value	the value defining the style
	 */
	public void loadTagStyle(String tag, String key, String value) {
		tag = tag.toLowerCase();
		Map<String, String> styles = tagMap.get(tag);
		if (styles == null) {
			styles = new HashMap<String, String>();
			tagMap.put(tag, styles);
		}
		styles.put(key, value);
	}

	/**
	 * Associates a Map containing styles with a class name.
	 * @param	className	the value of the class attribute
	 * @param	attrs		a map containing styles
	 */
	public void loadStyle(String className, HashMap<String, String> attrs) {
		classMap.put(className.toLowerCase(), attrs);
	}

	/**
	 * Adds an extra style key-value pair to the styles Map
	 * of a specific tag
	 * @param	classsName	the name of the HTML/XML tag
	 * @param	key			the key specifying a specific style
	 * @param	value		the value defining the style
	 */
	public void loadStyle(String className, String key, String value) {
		className = className.toLowerCase();
		Map<String, String> styles = classMap.get(className);
		if (styles == null) {
			styles = new HashMap<String, String>();
			classMap.put(className, styles);
		}
		styles.put(key, value);
	}

	/**
	 * Resolves the styles based on the tag name and the value
	 * of the class attribute.
	 * @param	tag		the tag that needs to be resolved
	 * @param	attrs	existing style map that will be updated
	 */
	public void applyStyle(String tag, HashMap<String, String> attrs) {
		// first fetch the styles corresponding with the tag name
		Map<String, String> map = tagMap.get(tag.toLowerCase());
		if (map != null) {
			// create a new map with properties
			Map<String, String> temp = new HashMap<String, String>(map);
			// override with the existing properties
			temp.putAll(attrs);
			// update the existing properties
			attrs.putAll(temp);
		}
		// look for the class attribute
		String cm = attrs.get(Markup.HTML_ATTR_CSS_CLASS);
		if (cm == null)
			return;
		// fetch the styles corresponding with the class attribute
		map = classMap.get(cm.toLowerCase());
		if (map == null)
			return;
		// remove the class attribute from the properties
		attrs.remove(Markup.HTML_ATTR_CSS_CLASS);
		// create a map with the styles corresponding with the class value
		Map<String, String> temp = new HashMap<String, String>(map);
		// override with the existing properties
		temp.putAll(attrs);
		// update the properties
		attrs.putAll(temp);
	}
}