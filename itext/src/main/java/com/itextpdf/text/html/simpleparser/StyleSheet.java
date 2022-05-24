/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
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
import java.util.Properties;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;

/**
 * Old class to define styles for HTMLWorker.
 * We've completely rewritten HTML to PDF functionality; see project XML Worker.
 * XML Worker is able to parse CSS files and "style" attribute values.
 * @deprecated since 5.5.2
 */
@Deprecated
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
	 * @param	className	the name of the HTML/XML tag
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
	public void applyStyle(String tag, Map<String, String> attrs) {
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
		String cm = attrs.get(HtmlTags.CLASS);
		if (cm == null)
			return;
		// fetch the styles corresponding with the class attribute
		map = classMap.get(cm.toLowerCase());
		if (map == null)
			return;
		// remove the class attribute from the properties
		attrs.remove(HtmlTags.CLASS);
		// create a map with the styles corresponding with the class value
		Map<String, String> temp = new HashMap<String, String>(map);
		// override with the existing properties
		temp.putAll(attrs);
		// update the properties
		attrs.putAll(temp);
	}

	/**
	 * Method contributed by Lubos Strapko
	 * @param h
	 * @param chain
	 * @since 2.1.3
	 */
	public static void resolveStyleAttribute(Map<String, String> h, ChainedProperties chain) {
		String style = h.get(HtmlTags.STYLE);
		if (style == null)
			return;
		Properties prop = HtmlUtilities.parseAttributes(style);
		for (Object element : prop.keySet()) {
			String key = (String) element;
			if (key.equals(HtmlTags.FONTFAMILY)) {
				h.put(HtmlTags.FACE, prop.getProperty(key));
			} else if (key.equals(HtmlTags.FONTSIZE)) {
				float actualFontSize = HtmlUtilities.parseLength(chain
						.getProperty(HtmlTags.SIZE),
						HtmlUtilities.DEFAULT_FONT_SIZE);
				if (actualFontSize <= 0f)
					actualFontSize = HtmlUtilities.DEFAULT_FONT_SIZE;
				h.put(HtmlTags.SIZE, Float.toString(HtmlUtilities.parseLength(prop
						.getProperty(key), actualFontSize))
						+ "pt");
			} else if (key.equals(HtmlTags.FONTSTYLE)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals(HtmlTags.ITALIC) || ss.equals(HtmlTags.OBLIQUE))
					h.put(HtmlTags.I, null);
			} else if (key.equals(HtmlTags.FONTWEIGHT)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals(HtmlTags.BOLD) || ss.equals("700") || ss.equals("800")
						|| ss.equals("900"))
					h.put(HtmlTags.B, null);
			} else if (key.equals(HtmlTags.TEXTDECORATION)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals(HtmlTags.UNDERLINE))
					h.put(HtmlTags.U, null);
			} else if (key.equals(HtmlTags.COLOR)) {
				BaseColor c = HtmlUtilities.decodeColor(prop.getProperty(key));
				if (c != null) {
					int hh = c.getRGB();
					String hs = Integer.toHexString(hh);
					hs = "000000" + hs;
					hs = "#" + hs.substring(hs.length() - 6);
					h.put(HtmlTags.COLOR, hs);
				}
			} else if (key.equals(HtmlTags.LINEHEIGHT)) {
				String ss = prop.getProperty(key).trim();
				float actualFontSize = HtmlUtilities.parseLength(chain
						.getProperty(HtmlTags.SIZE),
						HtmlUtilities.DEFAULT_FONT_SIZE);
				if (actualFontSize <= 0f)
					actualFontSize = HtmlUtilities.DEFAULT_FONT_SIZE;
				float v = HtmlUtilities.parseLength(prop.getProperty(key),
						actualFontSize);
				if (ss.endsWith("%")) {
					h.put(HtmlTags.LEADING, "0," + v / 100);
					return;
				}
				if (HtmlTags.NORMAL.equalsIgnoreCase(ss)) {
					h.put(HtmlTags.LEADING, "0,1.5");
					return;
				}
				h.put(HtmlTags.LEADING, v + ",0");
			} else if (key.equals(HtmlTags.TEXTALIGN)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				h.put(HtmlTags.ALIGN, ss);
			} else if (key.equals(HtmlTags.PADDINGLEFT)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				h.put(HtmlTags.INDENT, Float.toString(HtmlUtilities.parseLength(ss)));
			}
		}
	}
}
