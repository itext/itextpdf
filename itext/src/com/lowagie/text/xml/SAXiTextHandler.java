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

import java.util.Stack;
import java.util.EmptyStackException;
import java.util.Properties;

import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;

import com.lowagie.text.*;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 *
 * @author  bruno@lowagie.com
 */

public class SAXiTextHandler extends HandlerBase {

	/** This is the resulting document. */
	private Document document;

	/** This is the Tags-implementation to be used. */
	private Tags tags;

	/** This is a <CODE>Stack</CODE> of objects, waiting to be added to the document. */
	private Stack stack;

	/** Counts the number of chapters in this document. */
	private int chapters = 0;

	/** Indicates the paragraph-status: normal paragraph or title of a section/chapter. */
	private boolean title = false;

	/** This is the current chunk to which characters can be added. */
	private Chunk currentChunk = null;

	/**
	 * Constructs a new SAXiTextHandler that will translate all the events
	 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
	 *
	 * @param	document	this is the document on which events must be triggered
	 */

	public SAXiTextHandler(Document document, Tags tags) {
		super();
		this.document = document;
		this.tags = tags;
		stack = new Stack();
	}

	/**
	 * This method gets called when a start tag is encountered.
	 *
	 * @param	name		the name of the tag that is encountered
	 * @param	attrs		the list of attributes
	 */

	public void startElement(String name, AttributeList attrs) {
		Properties attributes = new Properties();
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				String attribute = attrs.getName(i);
				attributes.put(attribute, attrs.getValue(i));
			}
		}		

		if (tags.isChunk(name)) {
			currentChunk = tags.getChunk(attributes);
		}
		else if (tags.isPhrase(name)) {
			stack.push(tags.getPhrase(attributes));
		}
		else if (tags.isParagraph(name)) {
			stack.push(tags.getParagraph(attributes));
		}
		else if (tags.isTitle(name)) {
			title = true;
		}
		else if (tags.isSection(name)) {
			Element previous = (Element) stack.pop();
			Section section = ((Section)previous).addSection(0, "", 0);
			stack.push(previous);
			tags.setSection(section, attributes);
			stack.push(section);
		}
		else if (tags.isChapter(name)) {
			Chapter chapter = new Chapter("", ++chapters);
			tags.setSection(chapter, attributes);
			stack.push(chapter);
		}
		else if (tags.isDocumentRoot(name)) {
			if (attrs != null) {
				for (int i = 0; i < attrs.getLength(); i++)	{
					switch(tags.getMetaType(attrs.getName(i))) {
					case Element.TITLE:
						document.addTitle(attrs.getValue(i));
						break;
					case Element.SUBJECT:
						document.addSubject(attrs.getValue(i));
						break;
					case Element.KEYWORDS:
						document.addKeywords(attrs.getValue(i));
						break;
					case Element.AUTHOR:
						document.addAuthor(attrs.getValue(i));
						break;
					default:
						// do nothing
					}
				}
			}
			document.open();
		} 
	} 

	/**
	 * This method gets called when ignorable white space encountered.
	 *
	 * @param	ch		an array of characters
	 * @param	start	the start position in the array
	 * @param	length	the number of characters to read from the array
	 */

	public void ignorableWhitespace(char[] ch, int start, int length) {
		// do nothing
	}

	/**
	 * This method gets called when characters are encountered.
	 *
	 * @param	ch		an array of characters
	 * @param	start	the start position in the array
	 * @param	length	the number of characters to read from the array
	 */

	public void characters(char[] ch, int start, int length) {
		if (currentChunk != null) {
			currentChunk.append(new String(ch, start, length));
		}
	}

	/**
	 * This method gets called when an end tag is encountered.
	 *
	 * @param	name		the name of the tag that ends
	 */

	public void endElement(String name) {
		try {
			if (tags.isChunk(name)) {
				TextElementArray current = (TextElementArray) stack.pop();
				current.add(currentChunk);
				stack.push(current);
				currentChunk = null;
			}
			if (tags.isPhrase(name)) {
				Element current = (Element) stack.pop();
				try {
					TextElementArray previous = (TextElementArray) stack.pop();
					previous.add(current);
					stack.push(previous);
				}
				catch(EmptyStackException ese) {
					document.add(current);
				}
			}
			else if (tags.isParagraph(name)) {
				Element current = (Element) stack.pop();
				try {
					TextElementArray previous = (TextElementArray) stack.pop();
					while (current.type() != Element.PARAGRAPH) {
						previous.add(current);
						current = (Element)previous;
						previous = (TextElementArray) stack.pop();
					}
					if (title) {
						((Section)previous).setTitle((Paragraph)current);
					}
					else {
						previous.add(current);
					}
					stack.push(previous);
				}
				catch(EmptyStackException ese) {
					document.add(current);
				}
			}
			else if (tags.isTitle(name)) {
				title = false;
			}
			if (tags.isSection(name)) {
				stack.pop();
			}
			else if (tags.isChapter(name)) {
				document.add((Element) stack.pop());
			}
			else if (tags.isDocumentRoot(name)) {
				document.close();
			}
		}
		catch(DocumentException de) {
			de.printStackTrace();
		}
	}
}