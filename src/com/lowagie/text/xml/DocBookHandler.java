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

import java.util.Properties;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocListener;
import com.lowagie.text.DocWriter;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This class controls the DocBook XML to PDF conversion.
 */
public class DocBookHandler extends DefaultHandler {

	/** Locator of the original XML Document. */
	protected Locator locator;
	/** A listener that will invoke all kinds of actions on a Document. */
	protected DocListener document;
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

	/**
	 * Constructs a new DocBookHandler that will translate all the events
	 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
	 */
	public DocBookHandler(DocListener document, DocWriter writer) {
		this.document = document;
		if (writer instanceof PdfWriter) {
			pdfWriter = (PdfWriter) writer;
		}
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
		Properties attributes = new Properties();
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

	public void handleStartingTags(String name, Properties attributes) {
		tagStack.push(name);
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

	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
		throws SAXException {
		String content = new String(ch, start, length);
		//System.err.println("'" + content + "'");

		if (content.trim().length() == 0) {
			return;
		}

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
					if (i > 0) {
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
		if (currentChunk == null) {
			currentChunk = new Chunk(buf.toString());
		} else {
			currentChunk.append(buf.toString());
		}
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
		String openTag = (String) tagStack.pop();
		if (!name.equals(openTag)) { throw new SAXParseException("Closing tag " + name + " doesn't match opentag " + openTag, locator); } 
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

}
