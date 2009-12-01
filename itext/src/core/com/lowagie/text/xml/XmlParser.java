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
package com.lowagie.text.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lowagie.text.DocListener;
import com.lowagie.text.ExceptionConverter;

/**
 * This class can be used to parse an XML file.
 */

public class XmlParser {
    
/** This is the instance of the parser. */
    protected SAXParser parser;
    
/**
 * Constructs an XmlParser.
 */
    
    public XmlParser() {
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
        }
        catch(ParserConfigurationException pce) {
            throw new ExceptionConverter(pce);
        }
        catch(SAXException se) {
            throw new ExceptionConverter(se);
        }
    }
    
/**
 * Parses a given file.
 * @param document	The document that will listen to the parser
 * @param is	The InputStream with the contents
 */
    
    public void go(DocListener document, InputSource is) {
        try {
            parser.parse(is, new SAXiTextHandler(document));
        }
        catch(SAXException se) {
            throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file.
 * @param document The document that will listen to the parser
 * @param is The inputsource with the content
 * @param tagmap A user defined tagmap
 */
    
    public void go(DocListener document, InputSource is, String tagmap) {
        try {
            parser.parse(is, new SAXmyHandler(document, new TagMap(tagmap)));
        }
        catch(SAXException se) {
            throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
    /**
     * Parses a given file.
     * @param document The document that will listen to the parser
     * @param is the inputsource with the content
     * @param tagmap an inputstream to a user defined tagmap
     */
        
        public void go(DocListener document, InputSource is, InputStream tagmap) {
            try {
                parser.parse(is, new SAXmyHandler(document, new TagMap(tagmap)));
            }
            catch(SAXException se) {
                throw new ExceptionConverter(se);
            }
            catch(IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
        }
    
/**
 * Parses a given file.
 * @param document The document that will listen to the parser
 * @param is the inputsource with the content
 * @param tagmap a user defined tagmap
 */
    
    public void go(DocListener document, InputSource is, HashMap tagmap) {
        try {
            parser.parse(is, new SAXmyHandler(document, tagmap));
        }
        catch(SAXException se) {
            throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file.
 * @param document The document that will listen to the parser
 * @param file The path to a file with the content
 */
    
    public void go(DocListener document, String file) {
        try {
            parser.parse(file, new SAXiTextHandler(document));
        }
        catch(SAXException se) {
            throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file.
 * @param document the document that will listen to the parser
 * @param file the path to a file with the content
 * @param tagmap a user defined tagmap
 */
    
    public void go(DocListener document, String file, String tagmap) {
        try {
            parser.parse(file, new SAXmyHandler(document, new TagMap(tagmap)));
        }
        catch(SAXException se) {
            throw new ExceptionConverter(se);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Parses a given file.
 * @param document The document that will listen to the parser
 * @param file the path to a file with the content
 * @param tagmap a user defined tagmap
 */
    
    public void go(DocListener document, String file, HashMap tagmap) {
        try {
            parser.parse(file, new SAXmyHandler(document, tagmap));
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
 * @param document The document that will listen to the parser
 * @param is the inputsource with the content
 */
    
    public static void parse(DocListener document, InputSource is) {
        XmlParser p = new XmlParser();
        p.go(document, is);
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document The document that will listen to the parser
 * @param is The inputsource with the content
 * @param tagmap a user defined tagmap
 */
    
    public static void parse(DocListener document, InputSource is, String tagmap) {
        XmlParser p = new XmlParser();
        p.go(document, is, tagmap);
    }
    
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 * @param document The document that will listen to the parser
 * @param is The inputsource with the content
 * @param tagmap a user defined tagmap
 */
    
    public static void parse(DocListener document, InputSource is, HashMap tagmap) {
        XmlParser p = new XmlParser();
        p.go(document, is, tagmap);
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document The document that will listen to the parser
 * @param file The path to a file with the content
 */
    
    public static void parse(DocListener document, String file) {
        XmlParser p = new XmlParser();
        p.go(document, file);
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document The document that will listen to the parser
 * @param file The path to a file with the content
 * @param tagmap A user defined tagmap
 */
    
    public static void parse(DocListener document, String file, String tagmap) {
        XmlParser p = new XmlParser();
        p.go(document, file, tagmap);
    }
    
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 * @param document The document that will listen to the parser
 * @param file The path to a file with the content
 * @param tagmap A user defined tagmap
 */
    
    public static void parse(DocListener document, String file, HashMap tagmap) {
        XmlParser p = new XmlParser();
        p.go(document, file, tagmap);
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document The document that will listen to the parser
 * @param is The inputsource with the content
 */
    
    public static void parse(DocListener document, InputStream is) {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is));
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document The document that will listen to the parser
 * @param is The inputstream with the content
 * @param tagmap A user defined tagmap
 */
    
    public static void parse(DocListener document, InputStream is, String tagmap) {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is), tagmap);
    }
    
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 * @param document The document that will listen to the parser
 * @param is The InputStream with the content
 * @param tagmap A user defined tagmap
 */
    
    public static void parse(DocListener document, InputStream is, HashMap tagmap) {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is), tagmap);
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document The document that will listen to the parser
 * @param is The reader that reads the content
 */
    
    public static void parse(DocListener document, Reader is) {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is));
    }
    
/**
 * Parses a given file that validates with the iText DTD and writes the content to a document.
 * @param document The document that will listen to the parser
 * @param is The reader that reads the content
 * @param tagmap A user defined tagmap
 */
    
    public static void parse(DocListener document, Reader is, String tagmap) {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is), tagmap);
    }
    
/**
 * Parses a given file and writes the content to a document, using a certain tagmap.
 * @param document The document that will listen to the parser
 * @param is The reader that reads the content
 * @param tagmap A user defined tagmap
 */
    
    public static void parse(DocListener document, Reader is, HashMap tagmap) {
        XmlParser p = new XmlParser();
        p.go(document, new InputSource(is), tagmap);
    }
}