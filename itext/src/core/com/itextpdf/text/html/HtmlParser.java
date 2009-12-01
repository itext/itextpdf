/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocListener;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.xml.XmlParser;

/**
 * This class can be used to parse some HTML files.
 */

public class HtmlParser extends XmlParser {
    
/**
 * Constructs an HtmlParser.
 */
    
    public HtmlParser() {
        super();
    }
    
/**
 * Parses a given file.
 * @param document the document the parser will write to
 * @param is the InputSource with the content
 */
    
    public void go(DocListener document, InputSource is) {
        try {
            parser.parse(is, new SAXmyHtmlHandler(document));
        }
        catch(SAXException se) {
            throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document the document the parser will write to
 * @param is the InputSource with the content
 */
    
    public static void parse(DocListener document, InputSource is) {
        HtmlParser p = new HtmlParser();
        p.go(document, is);
    }
    
/**
 * Parses a given file.
 * @param document the document the parser will write to
 * @param file the file with the content
 */
    
    public void go(DocListener document, String file) {
        try {
            parser.parse(file, new SAXmyHtmlHandler(document));
        }
        catch(SAXException se) {
            throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document the document the parser will write to
 * @param file the file with the content
 */
    
    public static void parse(DocListener document, String file) {
        HtmlParser p = new HtmlParser();
        p.go(document, file);
    }
    
/**
 * Parses a given file.
 * @param document the document the parser will write to
 * @param is the InputStream with the content
 */
    
    public void go(DocListener document, InputStream is) {
        try {
            parser.parse(new InputSource(is), new SAXmyHtmlHandler(document));
        }
        catch(SAXException se) {
            throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document the document the parser will write to
 * @param is the InputStream with the content
 */
    
    public static void parse(DocListener document, InputStream is) {
        HtmlParser p = new HtmlParser();
        p.go(document, new InputSource(is));
    }
    
/**
 * Parses a given file.
 * @param document the document the parser will write to
 * @param is the Reader with the content
 */
    
    public void go(DocListener document, Reader is) {
        try {
            parser.parse(new InputSource(is), new SAXmyHtmlHandler(document));
        }
        catch(SAXException se) {
            throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document the document the parser will write to
 * @param is the Reader with the content
 */
    
    public static void parse(DocListener document, Reader is) {
        HtmlParser p = new HtmlParser();
        p.go(document, new InputSource(is));
    }
}