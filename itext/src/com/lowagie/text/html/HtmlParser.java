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

package com.lowagie.text.html;

import java.io.IOException;

import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserFactory;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.xml.XmlParser;

/**
 * This class can be used to parse some HTML files.
 */

public class HtmlParser extends XmlParser {
	
/**
 * Constructs an HtmlParser.
 */
 
    public HtmlParser() throws DocumentException {
		super();
	}
	
/**
 * Parses a given file.
 */
 
	public void go(Document document, String file) throws DocumentException {
		try {
    		parser.setDocumentHandler(new SAXmyHtmlHandler(document));
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
 
	public static void parse(Document document, String file) throws DocumentException {
		HtmlParser p = new HtmlParser();
		p.go(document, file);	
	}
}