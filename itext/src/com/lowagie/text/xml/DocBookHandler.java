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

package com.lowagie.text.xml;

import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.lowagie.text.Chapter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocWriter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Section;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This class controls the DocBook XML to PDF conversion.
 */
public class DocBookHandler extends DefaultHandler implements DocBookTags {

	/** Locator of the original XML Document. */
	protected Locator locator;
	
	/** A listener that will invoke all kinds of actions on a Document. */
	protected Document document;
	
	/** If you are converting to PDF, the PDF writer that writes the actual PDF file. */
	protected PdfWriter pdfWriter;

	/** This is a <CODE>Stack</CODE> of objects, waiting to be added to the document. */
	protected Stack iTextObjectStack;
	
	/** This is a <CODE>Stack</CODE> of tags embracing subtags. */
	protected Stack tagStack;

	/** Counts the number of chapters in this document. */
	protected int chapters = 0;

	/** This is the current chunk to which characters can be added. */
	protected Chunk currentChunk = null;

	/** This is a flag that can be set, if you want to open and close the Document-object yourself. */
	protected boolean controlOpenClose = true;

	/** This is a reference to a class that can be used to check the type of a DocBookTag. */
	protected DocBookTagChecker checker = new DocBookTagChecker();

	/**
	 * Constructs a new DocBookHandler that will translate all the events
	 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
	 */
	public DocBookHandler(Document document, DocWriter writer) {
		this.document = document;
		if (writer instanceof PdfWriter) {
			pdfWriter = (PdfWriter) writer;
		}
		tagStack = new Stack();
		iTextObjectStack = new Stack();
	}
	
	/**
	 * This method gets called when a start tag is encountered.
	 *
	 * @param	name		the name of the tag that is encountered
	 * @param	attrs		the list of attributes
	 */

	public void startElement(
		String uri,
		String lname,
		String name,
		Attributes attrs)
		throws SAXException {
		// attributes are put in a Properties object
		Properties attributes = new Properties();
		attributes.setProperty(DOCBOOKTAGNAME, name);
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				String attribute = attrs.getQName(i);
				attributes.setProperty(attribute, attrs.getValue(i));
			}
		}
		handleStartingTags(name, attributes);
	}

	/**
	 * This method deals with the starting tags.
	 *
	 * @param       name        the name of the tag
	 * @param       attributes  the list of attributes
	 */

	public void handleStartingTags(String name, Properties attributes)
		throws SAXException {

		// adding data that was before the tag to the Object Stack
		if (currentChunk != null) {
			TextElementArray current;
			try {
				current = (TextElementArray) pop();
			} catch (Exception e) {
				current = getNewParagraph(new Properties());
			}
			current.add(currentChunk);
			push(current);
			currentChunk = null;
		}
			
		// keeping track of the tags
		tagStack.push(name);
		
//System.err.println("Start: " + tagStack.peek());
		
		// tags that can be ignored
		if (checker.isIgnore(name) || checker.isNewPage(name) || tagStack.size() == 1) {
			// info tags are ignored
			return;
		}
		
		// chunks
		if (checker.isChunk(name)) {
			currentChunk = getNewChunk(attributes);
		}
		// paragraphs
		if (checker.isParagraph(name) || checker.isTitle(name)) {
			getNewParagraph(attributes);
			return;
		}
		// sections
		if (checker.isSection(name)) {
			getNewSection(attributes);
			return;	
		}
	}
	
	/**
	 * This method gets called when ignorable white space encountered.
	 *
	 * @param	ch		an array of characters
	 * @param	start	the start position in the array
	 * @param	length	the number of characters to read from the array
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length)
		throws SAXException {
		// do nothing: we handle white space ourselves in the characters method
	}

	/**
	 * This method gets called when characters are encountered.
	 *
	 * @param	ch		an array of characters
	 * @param	start	the start position in the array
	 * @param	length	the number of characters to read from the array
     *
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
		throws SAXException {

		if (ignoreContent() && document.isOpen()) return;

		String content = new String(ch, start, length);

		// ignore whitespace
		if (content.trim().length() == 0) {
			return;
		}
		
//System.err.println("'" + content + "'");

		// double spaces, tabs, carriage returns are not taken into account.
		// newlines are changed into spaces if necessary
		StringBuffer buf = new StringBuffer();
		int len = content.length();
		char character;
		boolean newline = false;
		for (int i = 0; i < len; i++) {
			switch (character = content.charAt(i)) {
				case ' ' :
					if (!newline) {
						buf.append(character);
					}
					break;
				case '\n' :
					if (i > 0 && buf.length() > 0) {
						newline = true;
						buf.append(' ');
					}
					break;
				case '\r' :
					break;
				case '\t' :
					break;
				default :
					newline = false;
					buf.append(character);
			}
		}
		// currentchunk is updated
		if (currentChunk == null) {
			currentChunk = getNewChunk(new Properties());
		}
		currentChunk.append(buf.toString());
	}

	/**
	 * This method gets called when an end tag is encountered.
	 *
	 * @param	name		the name of the tag that ends
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String lname, String name)
		throws SAXException {
		handleEndingTags(name);
	}

	/**
	 * This method deals with the starting tags.
	 *
	 * @param       name        the name of the tag
	 */

	public void handleEndingTags(String name) throws SAXParseException {
		// keeping track of the tags
		String openTag = (String) tagStack.pop();
		
//System.err.println("Stop: " + name);
		
		if (!name.equals(openTag)) {
			throw new SAXParseException("Closing tag " + name + " doesn't match opentag " + openTag, locator);
		}
		try {
			// tags that can be ignored
			if (checker.isIgnore(name) || checker.isChunk(name) || tagStack.size() == 1) {
				return;
			}
			// paragraphs
			if (checker.isParagraph(name)) {
				open();
				Element current = getTextElementArray();
				try {
					TextElementArray previous = (TextElementArray) pop();
					previous.add(current);
					push(previous);
				} catch (Exception e) {
					document.add(current);
				}
				return;
			}
			// sections
			if (checker.isSection(name)) {
				open();
				if (peek() instanceof Chapter) {
					document.add(pop());
				}	
				else {
					pop();
				}
				return;
			}
			// titles
			if (checker.isTitle(name)) {
				TextElementArray current = getTextElementArray();
				if (insideInfo()) {
					// titles within info blocks are only added if the document isn't open yet
					if (!document.isOpen()) {
						document.addTitle(getString(current));
						return;
					}
					// otherwise they are ignored
					else {
						return;
					}
				}
				else { 
					open();
					// titles can be registered as titles of a section
					if (peek() instanceof Section) {
						Section previous = (Section) pop();
						previous.setTitle((Paragraph)current);
						push(previous);
					}
					// titles can be plain paragraphs
					else {
						document.add(current);
					}
				}
				return;
			}
			// forced pagebreak

			// newpage
			if (checker.isNewPage(name)) {
				push(getTextElementArray());
				currentChunk = new Chunk("");
				currentChunk.setNewPage();
				push(getTextElementArray());
				return;
			}
			// metainformation
			if (COPYRIGHT.equals(name) && !document.isOpen() && currentChunk != null && insideInfo()) {
				document.addCreator(getString(getTextElementArray()));
				return;
			}
			if (AUTHOR.equals(name) && !document.isOpen() && currentChunk != null && insideInfo()) {
				document.addAuthor(getString(getTextElementArray()));
				return;
			}
		}
		catch(DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	/**
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		try {
			while (peek() != null) document.add(pop());
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
		document.close();
	}
	
	/**
	 * Gets the current TextElementArray from the Stack and add the current Chunk if necessary.
	 * If there is no TextElementArray on the Stack, a new one is created. 
	 */
	private TextElementArray getTextElementArray() {
		if (currentChunk != null) {
			TextElementArray current;
			try {
				current = (TextElementArray) pop();
			} catch (Exception e) {
				current = getNewParagraph(new Properties());
			}
			current.add(currentChunk);
			currentChunk = null;
			return current;
		}
		return getNewParagraph(new Properties());
	}
	/**
	 * Creates a new Chunk.
	 * @return
	 */
	protected Chunk getNewChunk(Properties attributes) {
		return new Chunk(attributes);
	}
	/**
	 * Creates a new Paragraph.
	 * @return
	 */
	protected Paragraph getNewParagraph(Properties attributes) {
		return (Paragraph)push(new Paragraph());
	}
	/**
	 * Creates a new Section.
	 * This can be a Chapter object (toplevel) or a Section object (sublevel).
	 * @return
	 */
	protected Section getNewSection(Properties attributes) {
		Element previous = (Element) pop();
		if (previous != null) { 
			Section section;
			try { 
				section = ((Section) previous).addSection(attributes);
			} catch (ClassCastException cce) { 
				throw new ExceptionConverter(cce);
			}
			push(previous);
			section.setNumberDepth(0);
			return (Section) push(section);
		}
		else { 
			chapters++;
			Chapter chapter = new Chapter(attributes, chapters);
			chapter.setNumberDepth(0);
			return (Chapter) push(chapter);
		}
	}
	
	/**
	 * Checks if there is a tag on the Stack of tags that tells the handler
	 * to ignore the content.
	 * 
	 * @return true if the content can be ignored
	 */
	protected boolean ignoreContent() {
		for (Iterator i = tagStack.iterator(); i.hasNext(); ) {
			if (checker.isIgnoreContent(i.next())) return true;
		}
		return false;
	}
	
	/**
	 * Checks if there is a tag on the Stack of tags that tells the handler
	 * to ignore the content.
	 * 
	 * @return true if the content can be ignored
	 */
	protected boolean insideInfo() {
		for (Iterator i = tagStack.iterator(); i.hasNext(); ) {
			if (checker.isInfo(i.next())) return true;
		}
		return false;
	}
	
	/**
	 * Gets all the parts of a TextElementArray and returns them as a String.
	 */
	protected static String getString(TextElementArray current) {StringBuffer title = new StringBuffer();
		StringBuffer buf = new StringBuffer();
		Chunk chunk;
		for (Iterator i = current.getChunks().iterator(); i.hasNext(); ) {
			chunk = (Chunk)i.next();
			buf.append(chunk.content());
		}
		return buf.toString();
	}
	
	/**
	 * Pushes an element to the iText Object Stack.
	 * 
	 * @return the element that was pushed to the Stack
	 */
	private Element push(Element element) {
		return (Element) iTextObjectStack.push(element);
	}
	
	/**
	 * Gets and removes the element on top of the iText Object Stack.
	 * 
	 * @return the element on top of the Stack or null if the Stack is empty
	 */
	private Element pop() {
		try {
		    return (Element) iTextObjectStack.pop();
		}
		catch(EmptyStackException ese) {
			return null;
		}
	}
	
	/**
	 * Looks at the element on top of the iText Object Stack without removing it.
	 *
	 * @return the element on top of the Stack or null if the Stack is empty
	 */
	private Element peek() {
		try {
			return (Element) iTextObjectStack.peek();
		}
		catch(EmptyStackException ese) {
			return null;
		}
	}
	
	/**
	 * Opens the document if it wasn't already open.
	 */
	private void open() {
		if (document.isOpen()) return;
		iTextObjectStack.clear();
		document.open();
	}
}