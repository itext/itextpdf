/*
 * $Id$
 * $Name$
 *
 * Copyright (c) 2001 Bruno Lowagie.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;

import org.xml.sax.Parser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

/**
 * This class can be used to parse an XML file.
 */

public class XmlParser {
    
/** We use this parser to parse the document. */
    public static final String PARSER = "org.apache.xerces.parsers.SAXParser";
	
/** This is the instance of the parser. */
	protected Parser parser;
    
/**
 * Constructs an XmlParser.
 */
	
    public XmlParser() throws DocumentException {
		try {
			parser = ParserFactory.makeParser(PARSER);
		}
		catch(ClassNotFoundException cnfe) {
			throw new DocumentException(cnfe.getMessage());
		}
		catch(IllegalAccessException iae) {
			throw new DocumentException(iae.getMessage());
		}
		catch(InstantiationException ie) {
			throw new DocumentException(ie.getMessage());
		}
    }
	
/**
 * Parses a given file.
 */
 
	public void go(Document document, InputSource is) throws DocumentException {
		try {
    		parser.setDocumentHandler(new SAXiTextHandler(document));
			parser.parse(is);
		}
		catch(SAXException se) {
			throw new DocumentException(se.getMessage());
		}
		catch(IOException ioe) {
			throw new DocumentException(ioe.getMessage());
		}
	}
	
/**
 * Parses a given file.
 */
 
	public void go(Document document, InputSource is, String tagmap) throws DocumentException {
		try {
    		parser.setDocumentHandler(new SAXmyHandler(document, new TagMap(tagmap)));
			parser.parse(is);
		}
		catch(SAXException se) {
			throw new DocumentException(se.getMessage());
		}
		catch(IOException ioe) {
			throw new DocumentException(ioe.getMessage());
		}
	}
	
/**
 * Parses a given file.
 */
 
	public void go(Document document, InputSource is, HashMap tagmap) throws DocumentException {
		try {
    		parser.setDocumentHandler(new SAXmyHandler(document, tagmap));
			parser.parse(is);
		}
		catch(SAXException se) {
			throw new DocumentException(se.getMessage());
		}
		catch(IOException ioe) {
			throw new DocumentException(ioe.getMessage());
		}
	}
	
/**
 * Parses a given file.
 */
 
	public void go(Document document, String file) throws DocumentException {
		try {
    		parser.setDocumentHandler(new SAXiTextHandler(document));
			parser.parse(file);
		}
		catch(SAXException se) {
			throw new DocumentException(se.getMessage());
		}
		catch(IOException ioe) {
			throw new DocumentException(ioe.getMessage());
		}
	}
	
/**
 * Parses a given file.
 */
 
	public void go(Document document, String file, String tagmap) throws DocumentException {
		try {
    		parser.setDocumentHandler(new SAXmyHandler(document, new TagMap(tagmap)));
			parser.parse(file);
		}
		catch(SAXException se) {
			throw new DocumentException(se.getMessage());
		}
		catch(IOException ioe) {
			throw new DocumentException(ioe.getMessage());
		}
	}
	
/**
 * Parses a given file.
 */
 
	public void go(Document document, String file, HashMap tagmap) throws DocumentException {
		try {
    		parser.setDocumentHandler(new SAXmyHandler(document, tagmap));
			parser.parse(file);
		}
		catch(SAXException se) {
			throw new DocumentException(se.getMessage());
		}
		catch(IOException ioe) {
			throw new DocumentException(ioe.getMessage());
		}
	}
	
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
 
	public static void parse(Document document, InputSource is) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, is);	
	}
	
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
 
	public static void parse(Document document, InputSource is, String tagmap) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, is, tagmap);	
	}
	
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 */
 
	public static void parse(Document document, InputSource is, HashMap tagmap) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, is, tagmap);	
	}
	
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
 
	public static void parse(Document document, String file) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, file);	
	}
	
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
 
	public static void parse(Document document, String file, String tagmap) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, file, tagmap);	
	}
	
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 */
 
	public static void parse(Document document, String file, HashMap tagmap) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, file, tagmap);	
	}
	
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
 
	public static void parse(Document document, InputStream is) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, new InputSource(is));	
	}
	
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
 
	public static void parse(Document document, InputStream is, String tagmap) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, new InputSource(is), tagmap);	
	}
	
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 */
 
	public static void parse(Document document, InputStream is, HashMap tagmap) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, new InputSource(is), tagmap);	
	}
	
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
 
	public static void parse(Document document, Reader is) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, new InputSource(is));	
	}
	
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 */
 
	public static void parse(Document document, Reader is, String tagmap) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, new InputSource(is), tagmap);	
	}
	
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 */
 
	public static void parse(Document document, Reader is, HashMap tagmap) throws DocumentException {
		XmlParser p = new XmlParser();
		p.go(document, new InputSource(is), tagmap);	
	}
}