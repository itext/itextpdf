/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Bruno Lowagie.
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

package com.lowagie.text.markup;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.SimpleCell;
import com.lowagie.text.SimpleTable;

/**
 * This class is a HashMap that contains selectors (String) and styles (a
 * Properties object). Given a tag and its attributes (id/class), this class can
 * return an iText object with the according style.
 * 
 * @author blowagie
 */
public class MarkupParser extends HashMap {
	private static final long serialVersionUID = 3724008022202507040L;

	/**
	 * HashMap with styles for each known combination of tag/id/class. The key
	 * is a String-combination, the value a Properties object.
	 */
	protected HashMap stylecache = new HashMap();

	/**
	 * HashMap with fonts for each known combination of tag/id/class. The key is
	 * the same String-combination used for the stylecache.
	 */
	protected HashMap fontcache = new HashMap();

	// processing CSS

	/**
	 * Creates new MarkupParser
	 * 
	 * @param file
	 *            the path to a CSS file.
	 */
	public MarkupParser(String file) {
		super();
		BufferedReader br = null;
		try {
			FileReader reader = new FileReader(file);
			br = new BufferedReader(reader);
			StringBuffer buf = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				buf.append(line.trim());
			}
			String string = buf.toString();
			string = removeComment(string, "/*", "*/");
			StringTokenizer tokenizer = new StringTokenizer(string, "}");
			String tmp;
			int pos;
			String selector;
			String attributes;
			while (tokenizer.hasMoreTokens()) {
				tmp = tokenizer.nextToken();
				pos = tmp.indexOf('{');
				if (pos > 0) {
					selector = tmp.substring(0, pos).trim();
					attributes = tmp.substring(pos + 1).trim();
					if (attributes.endsWith("}"))
						attributes = attributes.substring(0, attributes
								.length() - 1);
					put(selector, parseAttributes(attributes));
				}
			}
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		} finally {
			try { if (br != null) br.close(); } catch (IOException e) {}
		}
	}

	/**
	 * Removes the comments sections of a String.
	 * 
	 * @param string
	 *            the original String
	 * @param startComment
	 *            the String that marks the start of a Comment section
	 * @param endComment
	 *            the String that marks the end of a Comment section.
	 * @return the String stripped of its comment section
	 */
	public static String removeComment(String string, String startComment,
			String endComment) {
		StringBuffer result = new StringBuffer();
		int pos = 0;
		int end = endComment.length();
		int start = string.indexOf(startComment, pos);
		while (start > -1) {
			result.append(string.substring(pos, start));
			pos = string.indexOf(endComment, start) + end;
			start = string.indexOf(startComment, pos);
		}
		result.append(string.substring(pos));
		return result.toString();
	}

	/**
	 * This method parses a String with attributes and returns a Properties
	 * object.
	 * 
	 * @param string
	 *            a String of this form: 'key1="value1"; key2="value2";...
	 *            keyN="valueN" '
	 * @return a Properties object
	 */
	public static Properties parseAttributes(String string) {
		Properties result = new Properties();
		if (string == null)
			return result;
		StringTokenizer keyValuePairs = new StringTokenizer(string, ";");
		StringTokenizer keyValuePair;
		String key;
		String value;
		while (keyValuePairs.hasMoreTokens()) {
			keyValuePair = new StringTokenizer(keyValuePairs.nextToken(), ":");
			if (keyValuePair.hasMoreTokens())
				key = keyValuePair.nextToken().trim();
			else
				continue;
			if (keyValuePair.hasMoreTokens())
				value = keyValuePair.nextToken().trim();
			else
				continue;
			if (value.startsWith("\""))
				value = value.substring(1);
			if (value.endsWith("\""))
				value = value.substring(0, value.length() - 1);
			result.setProperty(key.toLowerCase(), value);
		}
		return result;
	}

	// reading attributevalues

	/**
	 * Parses a length.
	 * 
	 * @param string
	 *            a length in the form of an optional + or -, followed by a
	 *            number and a unit.
	 * @return a float
	 */

	public static float parseLength(String string) {
		int pos = 0;
		int length = string.length();
		boolean ok = true;
		while (ok && pos < length) {
			switch (string.charAt(pos)) {
			case '+':
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
				pos++;
				break;
			default:
				ok = false;
			}
		}
		if (pos == 0)
			return 0f;
		if (pos == length)
			return Float.parseFloat(string + "f");
		float f = Float.parseFloat(string.substring(0, pos) + "f");
		string = string.substring(pos);
		// inches
		if (string.startsWith("in")) {
			return f * 72f;
		}
		// centimeters
		if (string.startsWith("cm")) {
			return (f / 2.54f) * 72f;
		}
		// millimeters
		if (string.startsWith("mm")) {
			return (f / 25.4f) * 72f;
		}
		// picas
		if (string.startsWith("pc")) {
			return f * 12f;
		}
		// default: we assume the length was measured in points
		return f;
	}

	/**
	 * Converts a <CODE>Color</CODE> into a HTML representation of this <CODE>
	 * Color</CODE>.
	 * 
	 * @param color
	 *            the <CODE>Color</CODE> that has to be converted.
	 * @return the HTML representation of this <COLOR>Color </COLOR>
	 */

	public static Color decodeColor(String s) {
        if (s == null)
            return null;
        s = s.toLowerCase().trim();
        Color c = (Color)colorTable.get(s);
        if (c != null)
            return c;
        try {
            if (s.startsWith("#")) {
                if (s.length() == 4)
                    s = "#" + s.substring(1, 2) + s.substring(1, 2)
                        + s.substring(2, 3) + s.substring(2, 3) 
                        + s.substring(3, 4) + s.substring(3, 4);
                if (s.length() == 7)
                    return new Color(Integer.parseInt(s.substring(1), 16));
            }
            else if (s.startsWith("rgb")) {
                StringTokenizer tk = new StringTokenizer(s.substring(3), " \t\r\n\f(),");
                int[] cc = new int [3];
                for (int k = 0; k < 3; ++k) {
                    if (!tk.hasMoreTokens())
                        return null;
                    String t = tk.nextToken();
                    float n;
                    if (t.endsWith("%")) {
                        n = Float.parseFloat(t.substring(0, t.length() - 1));
                        n = n * 255f / 100f;
                    }
                    else
                        n = Float.parseFloat(t);
                    int ni = (int)n;
                    if (ni > 255)
                        ni = 255;
                    else if (ni < 0)
                        ni = 0;
                    cc[k] = ni;
                }
                return new Color(cc[0], cc[1], cc[2]);
            }
        }
        catch (Exception e) {
        }
        return null;
	}

	// helper methods

	/**
	 * Generates a key for an tag/id/class combination and adds the style
	 * attributes to the stylecache.
	 * 
	 * @param attributes
	 *            a Properties object with the tagname and the attributes of the
	 *            tag.
	 * @return a key
	 */
	private String getKey(Properties attributes) {
		String tag = attributes.getProperty(MarkupTags.ITEXT_TAG);
		String id = attributes.getProperty(MarkupTags.HTML_ATTR_CSS_ID);
		String cl = attributes.getProperty(MarkupTags.HTML_ATTR_CSS_CLASS);
		if (id == null) {
			id = "";
		} else {
			id = "#" + id;
		}
		if (cl == null) {
			cl = "";
		} else {
			cl = "." + cl;
		}
		String key = tag + id + cl;
		if (!stylecache.containsKey(key) && key.length() > 0) {
			Properties props = new Properties();
			Properties tagprops = (Properties) get(tag);
			Properties idprops = (Properties) get(id);
			Properties clprops = (Properties) get(cl);
			Properties tagidprops = (Properties) get(tag + id);
			Properties tagclprops = (Properties) get(tag + cl);
			if (tagprops != null)
				props.putAll(tagprops);
			if (idprops != null)
				props.putAll(idprops);
			if (clprops != null)
				props.putAll(clprops);
			if (tagidprops != null)
				props.putAll(tagidprops);
			if (tagclprops != null)
				props.putAll(tagclprops);
			stylecache.put(key, props);
		}
		return key;
	}

	// getting the objects based on the tag and its attributes

	/**
	 * Returns pagebreak information.
	 * 
	 * @param attributes
	 * @return true if a page break is needed before the tag
	 */
	public boolean getPageBreakBefore(Properties attributes) {
		String key = getKey(attributes);
		Properties styleattributes = (Properties) stylecache.get(key);
		if (styleattributes != null
				&& MarkupTags.CSS_VALUE_ALWAYS.equals(styleattributes
						.getProperty(MarkupTags.CSS_KEY_PAGE_BREAK_BEFORE))) {
			return true;
		}
		return false;
	}

	/**
	 * Returns pagebreak information.
	 * 
	 * @param attributes
	 * @return true if a page break is needed after the tag
	 */
	public boolean getPageBreakAfter(Properties attributes) {
		String key = getKey(attributes);
		Properties styleattributes = (Properties) stylecache.get(key);
		if (styleattributes != null
				&& MarkupTags.CSS_VALUE_ALWAYS.equals(styleattributes
						.getProperty(MarkupTags.CSS_KEY_PAGE_BREAK_AFTER))) {
			return true;
		}
		return false;
	}

	/**
	 * Returns an object based on a tag and its attributes.
	 * 
	 * @param attributes
	 *            a Properties object with the tagname and the attributes of the
	 *            tag.
	 * @return an iText object
	 */
	public Element getObject(Properties attributes) {
		String key = getKey(attributes);
		Properties styleattributes = (Properties) stylecache.get(key);
        if (styleattributes == null)
            return null;
		if (MarkupTags.CSS_VALUE_HIDDEN.equals(styleattributes
						.get(MarkupTags.CSS_KEY_VISIBILITY))) {
			return null;
		}
		String display = styleattributes
				.getProperty(MarkupTags.CSS_KEY_DISPLAY);
		Element element = null;
		if (MarkupTags.CSS_VALUE_INLINE.equals(display)) {
			element = retrievePhrase(getFont(attributes), styleattributes);
		} else if (MarkupTags.CSS_VALUE_BLOCK.equals(display)) {
			element = retrieveParagraph(getFont(attributes), styleattributes);
		} else if (MarkupTags.CSS_VALUE_LISTITEM.equals(display)) {
			element = retrieveListItem(getFont(attributes), styleattributes);
		} else if (MarkupTags.CSS_VALUE_TABLECELL.equals(display)) {
			element = retrieveTableCell(attributes, styleattributes);
		} else if (MarkupTags.CSS_VALUE_TABLEROW.equals(display)) {
			element = retrieveTableRow(attributes, styleattributes);
		} else if (MarkupTags.CSS_VALUE_TABLE.equals(display)) {
			element = retrieveTable(attributes, styleattributes);
		}
		return element;
	}

	/**
	 * Returns a font based on the ID and CLASS attributes of a tag.
	 * 
	 * @param attributes
	 *            a Properties object with the tagname and the attributes of the
	 *            tag.
	 * @return an iText Font;
	 */
	public Font getFont(Properties attributes) {
		String key = getKey(attributes);
		Font f = (Font) fontcache.get(key);
		if (f != null) {
			return f;
		} else {
			Properties styleattributes = (Properties) stylecache.get(key);
			f = retrieveFont(styleattributes);
			fontcache.put(key, f);
		}
		return f;
	}

	/**
	 * Returns a rectangle based on the width and height attributes of a tag,
	 * can be overridden by the ID and CLASS attributes.
	 * 
	 * @param attrs
	 *            the attributes that came with the tag
	 * @return an iText Rectangle object
	 */
	public Rectangle getRectangle(Properties attrs) {
		String width = null;
		String height = null;
		String key = getKey(attrs);
		Properties styleattributes = (Properties) stylecache.get(key);
		if (styleattributes != null) {
			width = styleattributes.getProperty(MarkupTags.HTML_ATTR_WIDTH);
			height = styleattributes.getProperty(MarkupTags.HTML_ATTR_HEIGHT);
		}
		if (width == null)
			width = attrs.getProperty(MarkupTags.HTML_ATTR_WIDTH);
		if (height == null)
			height = attrs.getProperty(MarkupTags.HTML_ATTR_HEIGHT);
		if (width == null || height == null)
			return null;
		return new Rectangle(parseLength(width), parseLength(height));
	}

	// retrieving objects based on the styleAttributes

	/**
	 * Retrieves a Phrase based on some style attributes.
	 * 
	 * @param font
	 * @param styleattributes
	 *            a Properties object containing keys and values
	 * @return an iText Phrase object
	 */
	public Element retrievePhrase(Font font, Properties styleattributes) {
		Phrase p = new Phrase("", font);
		if (styleattributes == null)
			return p;
		String leading = styleattributes
				.getProperty(MarkupTags.CSS_KEY_LINEHEIGHT);
		if (leading != null) {
			if (leading.endsWith("%")) {
				p.setLeading(p.font().size() * (parseLength(leading) / 100f));
			} else {
				p.setLeading(parseLength(leading));
			}
		}
		return p;
	}

	/**
	 * Retrieves a Paragraph based on some style attributes.
	 * 
	 * @param font
	 * @param styleattributes
	 *            a Properties object containing keys and values
	 * @return an iText Paragraph object
	 */
	public Element retrieveParagraph(Font font, Properties styleattributes) {
		Paragraph p = new Paragraph((Phrase) retrievePhrase(font,
				styleattributes));
		if (styleattributes == null)
			return p;
		String margin = styleattributes.getProperty(MarkupTags.CSS_KEY_MARGIN);
		float f;
		if (margin != null) {
			f = parseLength(margin);
			p.setIndentationLeft(f);
			p.setIndentationRight(f);
			p.setSpacingBefore(f);
			p.setSpacingAfter(f);
		}
		margin = styleattributes.getProperty(MarkupTags.CSS_KEY_MARGINLEFT);
		if (margin != null) {
			f = parseLength(margin);
			p.setIndentationLeft(f);
		}
		margin = styleattributes.getProperty(MarkupTags.CSS_KEY_MARGINRIGHT);
		if (margin != null) {
			f = parseLength(margin);
			p.setIndentationRight(f);
		}
		margin = styleattributes.getProperty(MarkupTags.CSS_KEY_MARGINTOP);
		if (margin != null) {
			f = parseLength(margin);
			p.setSpacingBefore(f);
		}
		margin = styleattributes.getProperty(MarkupTags.CSS_KEY_MARGINBOTTOM);
		if (margin != null) {
			f = parseLength(margin);
			p.setSpacingAfter(f);
		}
		String align = styleattributes
				.getProperty(MarkupTags.CSS_KEY_TEXTALIGN);
		if (MarkupTags.CSS_VALUE_TEXTALIGNLEFT.equals(align)) {
			p.setAlignment(Element.ALIGN_LEFT);
		} else if (MarkupTags.CSS_VALUE_TEXTALIGNRIGHT.equals(align)) {
			p.setAlignment(Element.ALIGN_RIGHT);
		} else if (MarkupTags.CSS_VALUE_TEXTALIGNCENTER.equals(align)) {
			p.setAlignment(Element.ALIGN_CENTER);
		} else if (MarkupTags.CSS_VALUE_TEXTALIGNJUSTIFY.equals(align)) {
			p.setAlignment(Element.ALIGN_JUSTIFIED);
		}
		return p;
	}

	/**
	 * Gets a table based on the styleattributes.
	 * 
	 * @param attributes
	 * @param styleattributes
	 * @return an iText Table
	 */
	private Element retrieveTable(Properties attributes,
			Properties styleattributes) {
		SimpleTable table = new SimpleTable();
		applyBordersColors(table, attributes, styleattributes);
		return table;
	}

	/**
	 * Returns a Cell based on the styleattributes.
	 * 
	 * @param attributes
	 * @param styleattributes
	 * @return an iText Cell
	 */
	private Element retrieveTableRow(Properties attributes,
			Properties styleattributes) {
		SimpleCell row = new SimpleCell(SimpleCell.ROW);
		applyBordersColors(row, attributes, styleattributes);
		String width = null;
		if (attributes != null)
			width = attributes.getProperty(MarkupTags.HTML_ATTR_WIDTH);
		if (width == null)
			width = styleattributes.getProperty(MarkupTags.HTML_ATTR_WIDTH);
		if (width != null) {
			if (width.endsWith("%")) {
				row.setWidthpercentage(parseLength(width));
			} else {
				row.setWidth(parseLength(width));
			}
		}
		String margin = styleattributes.getProperty(MarkupTags.CSS_KEY_MARGIN);
		float f;
		if (margin != null) {
			f = parseLength(margin);
			row.setSpacing(f);
		}
		margin = styleattributes.getProperty(MarkupTags.CSS_KEY_MARGINLEFT);
		if (margin != null) {
			f = parseLength(margin);
			row.setSpacing_left(f);
		}
		margin = styleattributes.getProperty(MarkupTags.CSS_KEY_MARGINRIGHT);
		if (margin != null) {
			f = parseLength(margin);
			row.setSpacing_right(f);
		}
		margin = styleattributes.getProperty(MarkupTags.CSS_KEY_MARGINTOP);
		if (margin != null) {
			f = parseLength(margin);
			row.setSpacing_top(f);
		}
		margin = styleattributes.getProperty(MarkupTags.CSS_KEY_MARGINBOTTOM);
		if (margin != null) {
			f = parseLength(margin);
			row.setSpacing_bottom(f);
		}
		String padding = styleattributes.getProperty(MarkupTags.CSS_KEY_PADDING);
		if (padding != null) {
			f = parseLength(padding);
			row.setPadding(f);
		}
		padding = styleattributes.getProperty(MarkupTags.CSS_KEY_PADDINGLEFT);
		if (padding != null) {
			f = parseLength(padding);
			row.setSpacing_left(f);
		}
		padding = styleattributes.getProperty(MarkupTags.CSS_KEY_PADDINGRIGHT);
		if (padding != null) {
			f = parseLength(padding);
			row.setSpacing_right(f);
		}
		padding = styleattributes.getProperty(MarkupTags.CSS_KEY_PADDINGTOP);
		if (padding != null) {
			f = parseLength(padding);
			row.setSpacing_top(f);
		}
		padding = styleattributes.getProperty(MarkupTags.CSS_KEY_PADDINGBOTTOM);
		if (padding != null) {
			f = parseLength(padding);
			row.setSpacing_bottom(f);
		}
		return row;
	}

	/**
	 * Returns a Cell based on the styleattributes.
	 * 
	 * @param attributes
	 * @param styleattributes
	 * @return an iText Cell
	 */
	private Element retrieveTableCell(Properties attributes,
			Properties styleattributes) {
		SimpleCell cell = (SimpleCell) retrieveTableRow(attributes,
				styleattributes);
		cell.setCellgroup(false);
		return cell;
	}

	/**
	 * Returns a ListItem based on the styleattributes.
	 * 
	 * @param font
	 * @param styleattributes
	 * @return an iText ListItem
	 */
	private Element retrieveListItem(Font font, Properties styleattributes) {
		ListItem li = new ListItem();
		return li;
	}
	
	/**
	 * Applies colors to a Rectangle object.
	 * @param rect
	 * @param attributes
	 * @param styleattributes
	 */
	private void applyBordersColors(Rectangle rect, Properties attributes,	Properties styleattributes) {
		String s = styleattributes.getProperty(MarkupTags.CSS_KEY_BORDERWIDTH);
		float f;
		if (s != null) {
			f = parseLength(s);
			rect.setBorderWidth(f);
		}
		s = styleattributes.getProperty(MarkupTags.CSS_KEY_BORDERWIDTHLEFT);
		if (s != null) {
			f = parseLength(s);
			rect.setBorderWidthLeft(f);
		}
		s = styleattributes.getProperty(MarkupTags.CSS_KEY_BORDERWIDTHRIGHT);
		if (s != null) {
			f = parseLength(s);
			rect.setBorderWidthRight(f);
		}
		s = styleattributes.getProperty(MarkupTags.CSS_KEY_BORDERWIDTHTOP);
		if (s != null) {
			f = parseLength(s);
			rect.setBorderWidthTop(f);
		}
		s = styleattributes.getProperty(MarkupTags.CSS_KEY_BORDERWIDTHBOTTOM);
		if (s != null) {
			f = parseLength(s);
			rect.setBorderWidthBottom(f);
		}
		s = styleattributes.getProperty(MarkupTags.CSS_KEY_BORDERCOLOR);
		if (s != null) {
			rect.setBorderColor(decodeColor(s));
		}
	}

	/**
	 * Retrieves a font from the FontFactory based on some style attributes.
	 * Looks for the font-family, font-size, font-weight, font-style and color.
	 * Takes the default encoding and embedded value.
	 * 
	 * @param styleAttributes
	 *            a Properties object containing keys and values
	 * @return an iText Font object
	 */

	public Font retrieveFont(Properties styleAttributes) {
		String fontname = null;
		String encoding = FontFactory.defaultEncoding;
		boolean embedded = FontFactory.defaultEmbedding;
		float size = Font.UNDEFINED;
		int style = Font.NORMAL;
		Color color = null;
		String value = (String) styleAttributes
				.get(MarkupTags.CSS_KEY_FONTFAMILY);
		if (value != null) {
			if (value.indexOf(',') == -1) {
				fontname = value.trim();
			} else {
				String tmp;
				while (value.indexOf(',') != -1) {
					tmp = value.substring(0, value.indexOf(',')).trim();
					if (FontFactory.isRegistered(tmp)) {
						fontname = tmp;
						break;
					} else {
						value = value.substring(value.indexOf(',') + 1);
					}
				}
			}
		}
		if ((value = (String) styleAttributes.get(MarkupTags.CSS_KEY_FONTSIZE)) != null) {
			size = MarkupParser.parseLength(value);
		}
		if ((value = (String) styleAttributes
				.get(MarkupTags.CSS_KEY_FONTWEIGHT)) != null) {
			style |= Font.getStyleValue(value);
		}
		if ((value = (String) styleAttributes.get(MarkupTags.CSS_KEY_FONTSTYLE)) != null) {
			style |= Font.getStyleValue(value);
		}
		if ((value = (String) styleAttributes.get(MarkupTags.CSS_KEY_COLOR)) != null) {
			color = MarkupParser.decodeColor(value);
		}
		return FontFactory.getFont(fontname, encoding, embedded, size, style,
				color);
	}
    
    public static HashMap colorTable = new HashMap();

    static {
        colorTable.put("black", new Color(0x000000));
        colorTable.put("green", new Color(0x008000));
        colorTable.put("silver", new Color(0xC0C0C0));
        colorTable.put("lime", new Color(0x00FF00));
        colorTable.put("gray", new Color(0x808080));
        colorTable.put("olive", new Color(0x808000));
        colorTable.put("white", new Color(0xFFFFFF));
        colorTable.put("yellow", new Color(0xFFFF00));
        colorTable.put("maroon", new Color(0x800000));
        colorTable.put("navy", new Color(0x000080));
        colorTable.put("red", new Color(0xFF0000));
        colorTable.put("blue", new Color(0x0000FF));
        colorTable.put("purple", new Color(0x800080));
        colorTable.put("teal", new Color(0x008080));
        colorTable.put("fuchsia", new Color(0xFF00FF));
        colorTable.put("aqua", new Color(0x00FFFF));
    }
}