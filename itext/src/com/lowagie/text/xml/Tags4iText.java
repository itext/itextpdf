/*
 * $Id$
 * $Name$
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie.
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

import com.lowagie.text.*;

/**
 * The <CODE>Tags</CODE>-class maps several iText-tags to iText-objects.
 *
 * @author  bruno@lowagie.com
 */

public class Tags4iText implements Tags {

// public static final member variables

	// ROOT

	/** the root tag. */
	public static final String ITEXT = "itext";

	/** attribute of the root tag (also a special tag within a chapter or section) */
	public static final String TITLE = "title";

	/** attribute of the root tag */
	public static final String SUBJECT = "subject";

	/** attribute of the root tag */
	public static final String KEYWORDS = "keywords";

	/** attribute of the root tag */
	public static final String AUTHOR = "author";

	// Chapters and Sections

	/** the chapter tag */
	public static final String CHAPTER = "chapter";

	/** the section tag */
	public static final String SECTION = "section";

	/** attribute of section/chapter tag */
	public static final String DEPTH = "depth";

	/** attribute of section/chapter tag */
	public static final String INDENT = "indent";

	/** attribute of chapter/section/paragraph tag */
	public static final String LEFT = "left";

	/** attribute of chapter/section/paragraph tag */
	public static final String RIGHT = "right";

	// Phrases and Paragraphs

	/** the phrase tag */
	public static final String PHRASE = "phrase";

	/** the paragraph tag */
	public static final String PARAGRAPH = "paragraph";

	/** attribute of phrase/paragraph tag */
	public static final String LEADING = "leading";

	/** attribute of paragraph tag */
	public static final String ALIGN = "align";

	// Chunks

	/** the chunk tag */
	public static final String CHUNK = "chunk";

	/** attribute of the chunk tag */
	public static final String FONT = "font";

	/** attribute of the chunk tag */
	public static final String SIZE = "size";

	/** attribute of the chunk tag */
	public static final String STYLE = "style";

	/** attribute of the chunk tag */
	public static final String RED = "red";

	/** attribute of the chunk tag */
	public static final String GREEN = "green";

	/** attribute of the chunk tag */
	public static final String BLUE = "blue";


// implementation of the Tags-interface

	/**
	 * Checks if a certain <CODE>String</CODE> corresponds with a tagname.
	 *
	 * @param	tag		a presumed tagname
	 * @return	<CODE>true</CODE> if <VAR>tag</VAR> is a tagname, <CODE>false</CODE> otherwise.
	 */

	public boolean isTag(String tag) {
		return isChunk(tag)
			|| isPhrase(tag)
			|| isParagraph(tag)
			|| isSection(tag)
			|| isChapter(tag)
			|| isDocumentRoot(tag);
	}

	/**
	 * Checks if a certain tag corresponds with the roottag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> if <VAR>tag</VAR> equals <CODE>itext</CODE>, <CODE>false</CODE> otherwise.
	 */

	public boolean isDocumentRoot(String tag) {
		return ITEXT.equals(tag);
	}

	/**
	 * Gets the meta type if a certain attribute represents some meta data.
	 *
	 * @param	attribute	an attribute name
	 * @return	the type of meta data
	 */

	public int getMetaType(String attribute) {
		if (TITLE.equals(attribute)) {
			return Element.TITLE;
		}
		else if (SUBJECT.equals(attribute)) {
			return Element.SUBJECT;
		}
		else if (KEYWORDS.equals(attribute)) {
			return Element.KEYWORDS;
		}
		else if (AUTHOR.equals(attribute)) {
			return Element.AUTHOR;
		}
		return Element.HEADER;
	}

	/**
	 * Checks if a certain tag corresponds with the chaptertag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> if <VAR>tag</VAR> equals <CODE>chapter</CODE>, <CODE>false</CODE> otherwise.
	 */

	public boolean isChapter(String tag) {
		return CHAPTER.equals(tag);
	}

	/**
	 * Checks if a certain tag corresponds with the sectiontag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> if <VAR>tag</VAR> equals <CODE>section</CODE>, <CODE>false</CODE> otherwise.
	 */

	public boolean isSection(String tag) {
		return SECTION.equals(tag);
	}				   

	/**
	 * Alters a given <CODE>Section</CODE> following a set of attributes.
	 *
	 * @param	section		the section that has to be changed
	 * @param	attributes	the attributes
	 */

	public void setSection(Section section, Properties attributes) {
		String value;
		if ((value = attributes.getProperty(DEPTH)) != null) {
			section.setNumberDepth(Integer.parseInt(value));
		}
		if ((value = attributes.getProperty(INDENT)) != null) {
			section.setIndentation(Integer.parseInt(value));
		}
		if ((value = attributes.getProperty(LEFT)) != null) {
			section.setIndentationLeft(Integer.parseInt(value));
		}
		if ((value = attributes.getProperty(RIGHT)) != null) {
			section.setIndentationRight(Integer.parseInt(value));
		}
	}				   

	/**
	 * Checks if a certain tag corresponds with the titletag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> if <VAR>tag</VAR> equals <CODE>title</CODE>, <CODE>false</CODE> otherwise.
	 */

	public boolean isTitle(String tag) {
		return "title".equals(tag);
	}				   

	/**
	 * Checks if a certain tag corresponds with the paragraph-tag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> if <VAR>tag</VAR> equals <CODE>paragraph</CODE>, <CODE>false</CODE> otherwise.
	 */

	public boolean isParagraph(String tag) {
		return PARAGRAPH.equals(tag);
	}				   					   

	/**
	 * Returns a <CODE>Paragraph</CODE> that has been constructed taking in account
	 * the value of some <VAR>attributes</VAR>.
	 *
	 * @param	attributes		Some attributes
	 * @return	a <CODE>Paragraph</CODE>
	 */

	public Paragraph getParagraph(Properties attributes) {
		Paragraph paragraph = new Paragraph();
		String value;
		if ((value = attributes.getProperty(LEADING)) != null) {
			paragraph.setLeading(Integer.parseInt(value));
		}
		if ((value = attributes.getProperty(LEFT)) != null) {
			paragraph.setIndentationLeft(Integer.parseInt(value));
		}								
		if ((value = attributes.getProperty(RIGHT)) != null) {
			paragraph.setIndentationRight(Integer.parseInt(value));
		}								 
		if ((value = attributes.getProperty(ALIGN)) != null) {
			paragraph.setAlignment(value);
		}
		return paragraph;
	}

	/**
	 * Checks if a certain tag corresponds with the phrase-tag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> if <VAR>tag</VAR> equals <CODE>phrase</CODE>, <CODE>false</CODE> otherwise.
	 */

	public boolean isPhrase(String tag) {
		return PHRASE.equals(tag);
	}					   

	/**
	 * Returns a <CODE>Phrase</CODE> that has been constructed taking in account
	 * the value of some <VAR>attributes</VAR>.
	 *
	 * @param	attributes		Some attributes
	 * @return	a <CODE>Phrase</CODE>
	 */

	public Phrase getPhrase(Properties attributes) {
		if (attributes.getProperty(LEADING) != null) {
			return new Phrase(Integer.parseInt(attributes.getProperty(LEADING)));
		}
		else {
			return new Phrase();
		}
	}				   

	/**
	 * Checks if a certain tag corresponds with the chunk-tag.
	 *
	 * @param	tag			a presumed tagname
	 * @return	<CODE>true</CODE> if <VAR>tag</VAR> equals <CODE>chunk</CODE>, <CODE>false</CODE> otherwise.
	 */

	public boolean isChunk(String tag) {
		return CHUNK.equals(tag);
	}					   

	/**
	 * Returns a <CODE>Chunk</CODE> that has been constructed taking in account
	 * the value of some <VAR>attributes</VAR>.
	 *
	 * @param	attributes		Some attributes
	 * @return	a <CODE>Chunk</CODE>
	 */

	public Chunk getChunk(Properties attributes) {
		Font font = new Font();
		String value;
		if ((value = attributes.getProperty(FONT)) != null) {
			font.setFamily(value);
		}
		if ((value = attributes.getProperty(SIZE)) != null) {
			font.setSize(Integer.parseInt(value));
		}
		if ((value = attributes.getProperty(STYLE)) != null) {
			font.setStyle(value);
		}
		if (attributes.getProperty(RED) != null &&
			attributes.getProperty(GREEN) != null &&
			attributes.getProperty(BLUE) != null) {
			font.setColor(Integer.parseInt(attributes.getProperty(RED)),
						  Integer.parseInt(attributes.getProperty(GREEN)),
						  Integer.parseInt(attributes.getProperty(BLUE)));
		}
		return new Chunk("", font);
	}	
}
