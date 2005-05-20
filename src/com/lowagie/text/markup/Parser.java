/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Bruno Lowagie.
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
 * the Initial Developer are Copyright (C) 1999-2005 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2005 by Paulo Soares. All Rights Reserved.
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter;

/**
 * This class allows you to parse files in a tree.
 */
public class Parser extends DefaultHandler {

	/* Stacks */
	protected Stack filestack;
	protected Stack outline;
	protected Stack tagstack;
	protected Stack objectstack;
	protected Chunk currentChunk = null;
	/* Output objects */
	protected Document document;
	protected PdfWriter writer;
	/* Counters */
	protected String title;
	protected String[] structures;
	protected String[] titles;
	protected int[] counterParents;
	protected int[] counters;
	protected int previoustitle = -1;
	/* Markup properties */
	protected MarkupParser markup;
	
	/**
	 * Constructs a recursive parser object.
	 * @param srcfile the file that has to be parsed.
	 */
	public Parser(String srcfile) {
		this(srcfile, "title", null, null, null);
	}
	
	/**
	 * Constructs a recursive parser object.
	 * @param srcfile the file that has to be parsed
	 * @param title the value of the id selector marking a title
	 * @param structures an array with the values of the class selectors marking titles in the complete structure
	 * @param titles the strings that have to be added to the titlenumber
	 * @param counterParents an array with references from each child in the structure to its parent
	 */
	public Parser(String srcfile, String title, String[] structures, String[] titles, int[] counterParents) {
		// stacks
		tagstack = new Stack();
		objectstack = new Stack();
		filestack = new Stack();
		filestack.push(srcfile);
		this.title = title;
		if (structures == null || titles == null || counterParents == null) {
			counters = new int[0];
		}
		else {
			this.structures = structures;
			this.titles = titles;
			this.counterParents = counterParents;
			counters = new int[counterParents.length];
		}
		// create the document
		document = new Document();
		// parse the file
		try {
			parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(flushObject());
		// close the document
		document.close();
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
//System.err.println("start: " + qName);
		// push tagname and attributes to the stack
		Properties attrs = new Properties();
		attrs.put(MarkupTags.ITEXT_TAG, qName);
		for (int i = 0; i < attributes.getLength(); i++) {
			attrs.put(attributes.getQName(i), attributes.getValue(i));
		}
		tagstack.push(attrs);
		// add the object to the objectstack
		if (document.isOpen()) {
			// first we flush the content that wasn't inside a tag.
			flushCurrentChunk();
			// we ask the markup parser for the corresponing object.
			Element element = markup.getObject(attrs);
			if (element != null) {
				switch(element.type()) {
				case Element.PHRASE:
					addObject((Phrase)element);
					break;
				case Element.PARAGRAPH:
					addObject((Paragraph)element);
					break;
				}
			}
		}
		else {
			// check if the document should be opened
			if (MarkupTags.HTML_TAG_BODY.equals(qName)) {
				Rectangle rect = markup.getRectangle(attrs);
				if (rect != null) document.setPageSize(rect);
				document.open();
				if (writer != null) {
					outline = new Stack();
					PdfContentByte cb = writer.getDirectContent();
					outline.push(cb.getRootOutline());
				}
			}
			else if (MarkupTags.HTML_TAG_LINK.equals(qName) && MarkupTags.HTML_ATTR_STYLESHEET.equals(attrs.getProperty(MarkupTags.HTML_ATTR_REL))) {
				String parent = new File((String)filestack.peek()).getParent();
				String markupfile = attrs.getProperty(MarkupTags.HTML_ATTR_HREF);
				if (markupfile.startsWith("/")) {
					markupfile = parent + markupfile;
				}
				else {
					markupfile = parent + "/" + markupfile;
				}
				markup = new MarkupParser(markupfile);
			}
		}
	}
	
	/**
	 * This method gets called when characters are encountered.
	 * 
	 * @param ch
	 *            an array of characters
	 * @param start
	 *            the start position in the array
	 * @param length
	 *            the number of characters to read from the array
	 */
	public void characters(char[] ch, int start, int length) {
		String content = new String(ch, start, length);
//System.err.println("Characters: " + content);
		if (content.trim().length() == 0) {
			return;
		}

		StringBuffer buf = new StringBuffer();
		int len = content.length();
		char character;
		boolean newline = false;
		for (int i = 0; i < len; i++) {
			switch (character = content.charAt(i)) {
			case ' ':
				if (!newline) {
					buf.append(character);
				}
				break;
			case '\n':
				if (i > 0) {
					newline = true;
					buf.append(' ');
				}
				break;
			case '\r':
				break;
			case '\t':
				break;
			default:
				newline = false;
				buf.append(character);
			}
		}
		addToCurrentChunk(buf.toString());
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
//System.err.println("end: " + qName);
		Properties attrs = (Properties)tagstack.peek();
		PdfOutline bookmark = null;
		flushCurrentChunk();
		if (attrs != null && title.equals(attrs.getProperty(MarkupTags.HTML_ATTR_CSS_ID)) && outline != null) {
			PdfOutline parent = (PdfOutline)outline.peek();
			PdfDestination dest = new PdfDestination(PdfDestination.FITH, writer.getVerticalPosition(false));
			Paragraph p = (Paragraph)objectstack.peek();
			bookmark = new PdfOutline(parent, dest, p);
		}
		tagstack.pop();
		flushObject();
		if(markup.getPageBreakAfter(attrs)) {
			while(flushObject());
			try {
				document.newPage();
			}
			catch(DocumentException de) {
				// this shouldn't happen
			}
		}
		if (bookmark != null) {
			outline.push(bookmark);
		}
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String instruction, String parameter)
			throws SAXException {
//System.err.println("Processing: " + instruction);
		try {
			// parse all the sublevels of the current directory
			if ("parse".equals(instruction)) {
				String file;
				StringTokenizer sublevels = new StringTokenizer(parameter, ",");
				while (sublevels.hasMoreTokens()) {
					file = sublevels.nextToken();
					if (filestack.size() > 0) file = new File((String)filestack.peek()).getParent() + "/" + file.trim();
					filestack.push(file);
					parse();
				}
			}
			if (document.isOpen()) return;
			// start listening with the PDF writer
			if ("pdfwriter".equals(instruction)) {
				writer = PdfWriter.getInstance(document, new FileOutputStream(parameter));
			}
			else if ("htmlwriter".equals(instruction)) {
				HtmlWriter.getInstance(document, new FileOutputStream(parameter));
			}
			else if ("rtfwriter".equals(instruction)) {
				RtfWriter.getInstance(document, new FileOutputStream(parameter));
			}
		}
		catch(Exception e) {
			throw new SAXException(e);
		}
	}
	
	/** flushing the CurrentChunk. */
	private void flushCurrentChunk() {
		if (currentChunk == null || !document.isOpen()) return;
		TextElementArray current;
		try {
			current = (TextElementArray) objectstack.pop();
		} catch (EmptyStackException ese) {
			current = new Paragraph();
		}
		current.add(currentChunk);
		objectstack.push(current);
		currentChunk = null;
	}
	
	/** extending the CurrentChunk. 
	 * @param s*/
	private void addToCurrentChunk(String s) {
		if (currentChunk != null) {
			currentChunk.append(s);
		}
		else {
			try {
				currentChunk = new Chunk(s, ((Phrase) objectstack.peek()).font());
			} catch (EmptyStackException ese) {
				currentChunk = new Chunk(s);
			}
		}
	}
	
	/**
	 * Creates a new Object and puts it on top of the objectstack.
	 * @param phrase
	 */
	private void addObject(Phrase phrase) {
		// we put the element on top of the objectstack
		objectstack.push(phrase);
	}
	
	/**
	 * Creates a new Object and puts it on top of the objectstack.
	 * @param paragraph
	 */
	private void addObject(Paragraph paragraph) {
		// now we get the attributes on top of the tagstack
		Properties attrs = (Properties)tagstack.peek();
		if (attrs == null) {
			return;
		}
		// if it's a Paragraph, it could be a title
		if (title.equals(attrs.getProperty(MarkupTags.HTML_ATTR_CSS_ID))) {
			for (int i = 0; i < counters.length; i++) {
				// where does this title fit in the hierarchy of the document?
				if (structures[i].equals(attrs.getProperty(MarkupTags.HTML_ATTR_CSS_CLASS))) {
					// we increment the number of this hierarchy element
					counters[i]++;
					while (previoustitle > -1 && counterParents[previoustitle] >= counterParents[i]) {
						outline.pop();
						previoustitle = counterParents[previoustitle];
					}
					previoustitle = i;
					// we set the counter of the child to 0 if necessary
					for (int j = i + 1; j < counters.length; j++) {
						if (counterParents[j] == i && titles[j] == null) {
							counters[j] = 0;
							break;
						}
					}
					// we construct the string that will proceed the title
					String s = titles[i];
					if (s == null) {
						s = "";
						int j = counterParents[i];
						while (j > 1) {
							s = String.valueOf(counters[j]) + "." + s;
							j = counterParents[j];
						}
						if (i > 0) {
							s += String.valueOf(counters[i]) + " ";
						}
					}
					else {
						s += " " + counters[i] + ": ";
					}
					((Paragraph)paragraph).add(new Chunk(s));
				}
			}
		}
		if(markup.getPageBreakBefore(attrs)) {
			while(flushObject());
			try {
				document.newPage();
			}
			catch(DocumentException de) {
				// this shouldn't happen
			}
		}
		// we put the element on top of the objectstack
		objectstack.push(paragraph);
	}
	
	/**
	 * Deals with the object on top of the objectstack.
	 * @return false if there was no valid object on the objectstack. 
	 */
	private boolean flushObject() {
		if (objectstack.size() == 0) {
			return false;
		}
		Element current = (Element) objectstack.pop();
		try {
			TextElementArray previous = (TextElementArray) objectstack.pop();
			previous.add(current);
			objectstack.push(previous);
			return true;
		} catch (EmptyStackException ese) {
			try {
				document.add(current);
				return true;
			} catch (DocumentException e) {
				return false;
			}
		}
	}
	
	/**
	 * Gets the file on top of the filestack,
	 * parses it
	 * and removes it from the stack.
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	private void parse() throws ParserConfigurationException, IOException, SAXException {
		// gets the file on top of the filestack
		String file = (String) filestack.peek();
//System.err.println("Parsing: " + file);
		// create the parser
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance(); 
		SAXParser saxParser = saxParserFactory.newSAXParser(); 
		XMLReader parser = saxParser.getXMLReader();
		parser.setContentHandler(this);
		// parse the file
	    parser.parse(new InputSource(file));
	    // remove the file from the stack
	    filestack.pop();
	}
}