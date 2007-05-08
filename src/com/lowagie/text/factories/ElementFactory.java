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

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import com.lowagie.text.Anchor;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.ChapterAutoNumber;
import com.lowagie.text.Chunk;
import com.lowagie.text.ElementTags;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.Utilities;
import com.lowagie.text.html.Markup;

/**
 * This class is able to create Element objects based on a list of properties.
 */

public class ElementFactory {

	/**
	 * Creates a Chunk object based on a list of properties.
	 * @param attributes
	 * @return
	 */
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
			chunk.setTextRise(p * chunk.getFont().getSize());
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

	/**
	 * Creates a Phrase object based on a list of properties.
	 * @param attributes
	 * @return
	 */
	public static Phrase getPhrase(Properties attributes) {
		Phrase phrase = new Phrase();
		phrase.setFont(FontFactory.getFont(attributes));
        String value;
        value = attributes.getProperty(ElementTags.LEADING);
        if (value != null) {
            phrase.setLeading(Float.parseFloat(value + "f"));
        }
        value = attributes.getProperty(Markup.CSS_KEY_LINEHEIGHT);
        if (value != null) {
            phrase.setLeading(Markup.parseLength(value));
        }
        value = attributes.getProperty(ElementTags.ITEXT);
        if (value != null) {
            Chunk chunk = new Chunk(value);
            if ((value = attributes.getProperty(ElementTags.GENERICTAG)) != null) {
                chunk.setGenericTag(value);
            }
            phrase.add(chunk);
        }
        return phrase;
	}

	/**
	 * Creates an Anchor object based on a list of properties.
	 * @param attributes
	 * @return
	 */
	public static Anchor getAnchor(Properties attributes) {
		Anchor anchor = new Anchor(getPhrase(attributes));
		String value;
        value = attributes.getProperty(ElementTags.NAME);
        if (value != null) {
            anchor.setName(value);
        }
        value = (String)attributes.remove(ElementTags.REFERENCE);
        if (value != null) {
            anchor.setReference(value);
        }
		return anchor;
	}

	/**
	 * Creates a Paragraph object based on a list of properties.
	 * @param attributes
	 * @return
	 */
	public static Paragraph getParagraph(Properties attributes) {
		Paragraph paragraph = new Paragraph(getPhrase(attributes));
        String value;
        value = attributes.getProperty(ElementTags.ALIGN);
        if (value != null) {
            paragraph.setAlignment(value);
        }
        value = attributes.getProperty(ElementTags.INDENTATIONLEFT);
        if (value != null) {
            paragraph.setIndentationLeft(Float.parseFloat(value + "f"));
        }
        value = attributes.getProperty(ElementTags.INDENTATIONRIGHT);
        if (value != null) {
            paragraph.setIndentationRight(Float.parseFloat(value + "f"));
        }
		return paragraph;
	}

	/**
	 * Creates a ListItem object based on a list of properties.
	 * @param attributes
	 * @return
	 */
	public static ListItem getListItem(Properties attributes) {
		ListItem item = new ListItem(getParagraph(attributes));
		return item;
	}

	/**
	 * Creates a List object based on a list of properties.
	 * @param attributes
	 * @return
	 */
	public static List getList(Properties attributes) {
		List list = new List();

		list.setNumbered(Utilities.checkTrueOrFalse(attributes, ElementTags.NUMBERED));
		list.setLettered(Utilities.checkTrueOrFalse(attributes, ElementTags.LETTERED));
		list.setLowercase(Utilities.checkTrueOrFalse(attributes, ElementTags.LOWERCASE));
		list.setAutoindent(Utilities.checkTrueOrFalse(attributes, ElementTags.AUTO_INDENT_ITEMS));
		list.setAlignindent(Utilities.checkTrueOrFalse(attributes, ElementTags.ALIGN_INDENTATION_ITEMS));
		
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

	/**
	 * Creates a Cell object based on a list of properties.
	 * @param attributes
	 * @return
	 */
	public static Cell getCell(Properties attributes) {
		Cell cell = new Cell();
		String value;

		cell.setHorizontalAlignment(attributes.getProperty(ElementTags.HORIZONTALALIGN));
		cell.setVerticalAlignment(attributes.getProperty(ElementTags.VERTICALALIGN));
		cell.setWidth(attributes.getProperty(ElementTags.WIDTH));
		
		value = attributes.getProperty(ElementTags.COLSPAN);
		if (value != null) {
			cell.setColspan(Integer.parseInt(value));
		}
		value = attributes.getProperty(ElementTags.ROWSPAN);
		if (value != null) {
			cell.setRowspan(Integer.parseInt(value));
		}
		value = attributes.getProperty(ElementTags.LEADING);
		if (value != null) {
			cell.setLeading(Float.parseFloat(value + "f"));
		}
		cell.setHeader(Utilities.checkTrueOrFalse(attributes, ElementTags.HEADER));
		if (Utilities.checkTrueOrFalse(attributes, ElementTags.NOWRAP)) {
			cell.setMaxLines(1);
		}
		value = attributes.getProperty(ElementTags.BORDERWIDTH);
		if (value != null) {
			cell.setBorderWidth(Float.parseFloat(value + "f"));
		}
		int border = 0;
		if (Utilities.checkTrueOrFalse(attributes, ElementTags.LEFT)) {
			border |= Rectangle.LEFT;
		}
		if (Utilities.checkTrueOrFalse(attributes, ElementTags.RIGHT)) {
			border |= Rectangle.RIGHT;
		}
		if (Utilities.checkTrueOrFalse(attributes, ElementTags.TOP)) {
			border |= Rectangle.TOP;
		}
		if (Utilities.checkTrueOrFalse(attributes, ElementTags.BOTTOM)) {
			border |= Rectangle.BOTTOM;
		}
		cell.setBorder(border);
		
		String r = attributes.getProperty(ElementTags.RED);
		String g = attributes.getProperty(ElementTags.GREEN);
		String b = attributes.getProperty(ElementTags.BLUE);
		if (r != null || g != null || b != null) {
			int red = 0;
			int green = 0;
			int blue = 0;
			if (r != null) red = Integer.parseInt(r);
			if (g != null) green = Integer.parseInt(g);
			if (b != null) blue = Integer.parseInt(b);
			cell.setBorderColor(new Color(red, green, blue));
		}
		else {
			cell.setBorderColor(Markup.decodeColor(attributes.getProperty(ElementTags.BORDERCOLOR)));
		}
		r = (String)attributes.remove(ElementTags.BGRED);
		g = (String)attributes.remove(ElementTags.BGGREEN);
		b = (String)attributes.remove(ElementTags.BGBLUE);
		value = attributes.getProperty(ElementTags.BACKGROUNDCOLOR);
		if (r != null || g != null || b != null) {
			int red = 0;
			int green = 0;
			int blue = 0;
			if (r != null) red = Integer.parseInt(r);
			if (g != null) green = Integer.parseInt(g);
			if (b != null) blue = Integer.parseInt(b);
			cell.setBackgroundColor(new Color(red, green, blue));
		}
		else if (value != null) {
			cell.setBackgroundColor(Markup.decodeColor(value));
		}
		else {
			value = attributes.getProperty(ElementTags.GRAYFILL);
			if (value != null) {
				cell.setGrayFill(Float.parseFloat(value + "f"));
			}
		}
		return cell;
	}

	/**
	 * Creates a ChapterAutoNumber object based on a list of properties.
	 * @param attributes
	 * @return
	 */
	public static ChapterAutoNumber getChapter(Properties attributes) {
		ChapterAutoNumber chapter = new ChapterAutoNumber("");
		setSectionParameters(chapter, attributes);
		return chapter;
	}

	/**
	 * Creates a Section object based on a list of properties.
	 * @param attributes
	 * @return
	 */
	public static Section getSection(Section parent, Properties attributes) {
		Section section = parent.addSection("");
		setSectionParameters(section, attributes);
		return section;
	}

	/**
	 * Helper method to create a Chapter/Section object.
	 * @param attributes
	 * @return
	 */
	private static void setSectionParameters(Section section, Properties attributes) {
		String value;
		value = attributes.getProperty(ElementTags.NUMBERDEPTH);
		if (value != null) {
			section.setNumberDepth(Integer.parseInt(value));
		}
		value = attributes.getProperty(ElementTags.INDENT);
		if (value != null) {
			section.setIndentation(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(ElementTags.INDENTATIONLEFT);
		if (value != null) {
			section.setIndentationLeft(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(ElementTags.INDENTATIONRIGHT);
		if (value != null) {
			section.setIndentationRight(Float.parseFloat(value + "f"));
		}
	}

	/**
	 * Creates an Image object based on a list of properties.
	 * @param attributes
	 * @return
	 */
	public static Image getImage(Properties attributes)
			throws BadElementException, MalformedURLException, IOException {
		String value;
		
		value = attributes.getProperty(ElementTags.URL);
		if (value == null)
			throw new MalformedURLException("The URL of the image is missing.");
		Image image = Image.getInstance(value);
		
		value = attributes.getProperty(ElementTags.ALIGN);
		int align = 0;
		if (value != null) {
			if (ElementTags.ALIGN_LEFT.equalsIgnoreCase(value))
				align |= Image.LEFT;
			else if (ElementTags.ALIGN_RIGHT.equalsIgnoreCase(value))
				align |= Image.RIGHT;
			else if (ElementTags.ALIGN_MIDDLE.equalsIgnoreCase(value))
				align |= Image.MIDDLE;
		}
		if ("true".equalsIgnoreCase(attributes.getProperty(ElementTags.UNDERLYING)))
			align |= Image.UNDERLYING;
		if ("true".equalsIgnoreCase(attributes.getProperty(ElementTags.TEXTWRAP)))
			align |= Image.TEXTWRAP;
		image.setAlignment(align);
		
		value = attributes.getProperty(ElementTags.ALT);
		if (value != null) {
			image.setAlt(value);
		}
		
		String x = attributes.getProperty(ElementTags.ABSOLUTEX);
		String y = attributes.getProperty(ElementTags.ABSOLUTEY);
		if ((x != null)	&& (y != null)) {
			image.setAbsolutePosition(Float.parseFloat(x + "f"),
					Float.parseFloat(y + "f"));
		}
		value = attributes.getProperty(ElementTags.PLAINWIDTH);
		if (value != null) {
			image.scaleAbsoluteWidth(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(ElementTags.PLAINHEIGHT);
		if (value != null) {
			image.scaleAbsoluteHeight(Float.parseFloat(value + "f"));
		}
		value = attributes.getProperty(ElementTags.ROTATION);
		if (value != null) {
			image.setRotation(Float.parseFloat(value + "f"));
		}
		return image;
	}
}