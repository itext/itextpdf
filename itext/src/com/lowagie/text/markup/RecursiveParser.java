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

import java.io.FileNotFoundException;
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
import com.lowagie.text.TextElementArray;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter;

/**
 * This class allows you to parse index.xml files in a tree.
 */
public class RecursiveParser extends DefaultHandler {

	/* Stacks */
	protected Stack filestack;
	protected Stack outline;
	protected Stack tagstack;
	protected Stack attributestack;
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
	/* Markup properties */
	protected MarkupParser markup;
	
	/**
	 * Constructs a recursive parser object.
	 */
	public RecursiveParser(String srcdir) {
		this(srcdir, "title", null, null, null);
	}
	
	/**
	 * Constructs a recursive parser object.
	 */
	public RecursiveParser(String srcdir, String title, String[] structures, String[] titles, int[] counterParents) {
		tagstack = new Stack();
		attributestack = new Stack();
		filestack = new Stack();
		filestack.push(srcdir);
		objectstack = new Stack();
		this.title = title;
		this.structures = structures;
		this.titles = titles;
		this.counterParents = counterParents;
		counters = new int[counterParents.length];
	}
	
	/**
	 * Initializes the recursive parser to produce pdf.
	 * @param document the document to which content will be added.
	 * @param dest the path to the destination file
	 * @param markup the path to the markup file
	 * @param outline if true, an outline tree will be made
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public void initPdfWriter(Document document, String dest, String markupfile, boolean outlines) throws IOException, FileNotFoundException, DocumentException {
		if (this.document != null) throw new DocumentException("You can initialize only one writer per document!");
		markup = new MarkupParser(markupfile);
		this.document = document;
		writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		if (outlines) {
			outline = new Stack();
			PdfContentByte cb = writer.getDirectContent();
			outline.push(cb.getRootOutline());
		}
		try {
			parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(flushObject());
	}

	/**
	 * Initializes the recursive parser to produce pdf.
	 * @param document the document to which content will be added.
	 * @param dest the path to the destination file
	 * @param markup the path to the markup file
	 * @param outline if true, an outline tree will be made
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public void initRtfWriter(Document document, String dest, String markupfile, boolean outlines) throws IOException, FileNotFoundException, DocumentException {
		if (document != null) throw new DocumentException("You can initialize only one writer per document!");
		markup = new MarkupParser(markupfile);
		this.document = document;
		RtfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		try {
			parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(flushObject());
	}
	
	/**
	 * Gets the file on top of the filestack,
	 * parses it
	 * and removes it from the stack.
	 */
	private void parse() throws ParserConfigurationException, IOException, SAXException {
		// gets the file on top of the filestack
		String file = filestack.peek() + "/index.xml";
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
	
	/**
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// push tagname and attributes to the stack
		tagstack.push(qName);
		Properties attrs = new Properties();
		for (int i = 0; i < attributes.getLength(); i++) {
			attrs.put(attributes.getQName(i), attributes.getValue(i));
		}
		attributestack.push(attrs);
		flushCurrentChunk();
		if (attrs != null && title.equals(attrs.getProperty(MarkupTags.ID))) {
			for (int i = 0; i < counters.length; i++) {
				if (structures[i].equals(attrs.getProperty(MarkupTags.CLASS))) {
					counters[i]++;
					for (int j = i + 1; j < counters.length; j++) {
						if (counterParents[j] == i) {
							counters[j] = 0;
							break;
						}
					}
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
					objectstack.push(new Paragraph(s, markup.getFont(attrs)));
					return;
				}
			}
		}
		if (MarkupTags.DIV.equals(qName)) {
			objectstack.push(new Paragraph("", markup.getFont(attrs)));
		}
		else if (MarkupTags.SPAN.equals(qName)) {
			currentChunk = new Chunk("", markup.getFont(attrs));
		}
	}
	
	/**
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		Properties attrs = (Properties)attributestack.peek();
		PdfOutline bookmark = null;
		flushCurrentChunk();
		if (attrs != null && title.equals(attrs.getProperty(MarkupTags.ID)) && outline != null) {
			PdfOutline parent = (PdfOutline)outline.peek();
			PdfDestination dest = new PdfDestination(PdfDestination.FITH, writer.getVerticalPosition(false));
			Paragraph p = (Paragraph)objectstack.peek();
			bookmark = new PdfOutline(parent, dest, p);
		}
		tagstack.pop();
		attributestack.pop();
		flushObject();
		if (bookmark != null) {
			outline.push(bookmark);
		}
	}
	
	/** flushing the CurrentChunk. */
	private void flushCurrentChunk() {
		if (currentChunk != null) {
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
	}
	
	/** flushing the CurrentChunk. */
	private void addToCurrentChunk(String s) {
		if (currentChunk != null) {
			currentChunk.append(s);
		}
		else {
			try {
				currentChunk = new Chunk(s, ((Paragraph) objectstack.peek()).font());
			} catch (EmptyStackException ese) {
				currentChunk = new Chunk(s);
			}
		}
	}
	
	/**
	 * Adds the object on top of the objectstack.
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
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String instruction, String parameter)
			throws SAXException {
		try {
			// parse all the sublevels of the current directory
			if ("parse".equals(instruction)) {
				String file;
				StringTokenizer sublevels = new StringTokenizer(parameter, ",");
				while (sublevels.hasMoreTokens()) {
					file = sublevels.nextToken();
					if (filestack.size() > 0) file = filestack.peek() + "/" + file;
					filestack.push(file.trim());
					parse();
					if (outline != null) outline.pop();
				}
			}
		}
		catch(Exception e) {
			throw new SAXException(e);
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
}