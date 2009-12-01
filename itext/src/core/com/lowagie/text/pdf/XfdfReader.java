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
package com.lowagie.text.pdf;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import com.lowagie.text.error_messages.MessageLocalization;

import com.lowagie.text.xml.simpleparser.SimpleXMLDocHandler;
import com.lowagie.text.xml.simpleparser.SimpleXMLParser;

/**
 * Reads a XFDF.
 * @author Leonard Rosenthol (leonardr@pdfsages.com)
 */
public class XfdfReader implements SimpleXMLDocHandler {
	// stuff used during parsing to handle state
	private boolean foundRoot = false;
    private Stack fieldNames = new Stack();
    private Stack fieldValues = new Stack();

    // storage for the field list and their values
	HashMap	fields;
	/**
	 * Storage for field values if there's more than one value for a field.
	 * @since	2.1.4
	 */
	protected HashMap listFields;
	
	// storage for the path to referenced PDF, if any
	String	fileSpec;
	
   /** Reads an XFDF form.
     * @param filename the file name of the form
     * @throws IOException on error
     */    
    public XfdfReader(String filename) throws IOException {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(filename);
            SimpleXMLParser.parse(this, fin);
        }
        finally {
            try{if (fin != null) {fin.close();}}catch(Exception e){}
        }
    }
    
    /** Reads an XFDF form.
     * @param xfdfIn the byte array with the form
     * @throws IOException on error
     */    
    public XfdfReader(byte xfdfIn[]) throws IOException {
        SimpleXMLParser.parse( this, new ByteArrayInputStream(xfdfIn));
   }
    
    /** Gets all the fields. The map is keyed by the fully qualified
     * field name and the value is a merged <CODE>PdfDictionary</CODE>
     * with the field content.
     * @return all the fields
     */    
    public HashMap getFields() {
        return fields;
    }
    
    /** Gets the field value.
     * @param name the fully qualified field name
     * @return the field's value
     */    
    public String getField(String name) {
        return (String)fields.get(name);
    }
    
    /** Gets the field value or <CODE>null</CODE> if the field does not
     * exist or has no value defined.
     * @param name the fully qualified field name
     * @return the field value or <CODE>null</CODE>
     */    
    public String getFieldValue(String name) {
        String field = (String)fields.get(name);
        if (field == null)
            return null;
        else
        	return field;
    }
    
    /**
     * Gets the field values for a list or <CODE>null</CODE> if the field does not
     * exist or has no value defined.
     * @param name the fully qualified field name
     * @return the field values or <CODE>null</CODE>
     * @since	2.1.4
     */    
    public List getListValues(String name) {
        return (List)listFields.get(name);
    }
    
    /** Gets the PDF file specification contained in the FDF.
     * @return the PDF file specification contained in the FDF
     */    
    public String getFileSpec() {
        return fileSpec;
    }

    /**
     * Called when a start tag is found.
     * @param tag the tag name
     * @param h the tag's attributes
     */    
    public void startElement(String tag, HashMap h)
    {
        if ( !foundRoot ) {
            if (!tag.equals("xfdf"))
                throw new RuntimeException(MessageLocalization.getComposedMessage("root.element.is.not.xfdf.1", tag));
            else 
            	foundRoot = true;
        }

        if ( tag.equals("xfdf") ){
    		
    	} else if ( tag.equals("f") ) {
    		fileSpec = (String)h.get( "href" );
    	} else if ( tag.equals("fields") ) {
            fields = new HashMap();		// init it!
            listFields = new HashMap();
    	} else if ( tag.equals("field") ) {
    		String	fName = (String) h.get( "name" );
    		fieldNames.push( fName );
    	} else if ( tag.equals("value") ) {
    		fieldValues.push( "" );
    	}
    }
    /**
     * Called when an end tag is found.
     * @param tag the tag name
     */    
    public void endElement(String tag) {
        if ( tag.equals("value") ) {
            String	fName = "";
            for (int k = 0; k < fieldNames.size(); ++k) {
                fName += "." + (String)fieldNames.elementAt(k);
            }
            if (fName.startsWith("."))
                fName = fName.substring(1);
            String fVal = (String) fieldValues.pop();
            String old = (String) fields.put( fName, fVal );
            if (old != null) {
            	List l = (List) listFields.get(fName);
            	if (l == null) {
            		l = new ArrayList();
            		l.add(old);
            	}
            	l.add(fVal);
            	listFields.put(fName, l);
            }
        }
        else if (tag.equals("field") ) {
            if (!fieldNames.isEmpty())
                fieldNames.pop();
        }
    }
    
    /**
     * Called when the document starts to be parsed.
     */    
    public void startDocument()
    {
        fileSpec = "";
    }
    /**
     * Called after the document is parsed.
     */    
    public void endDocument()
	{
    	
	}
    /**
     * Called when a text element is found.
     * @param str the text element, probably a fragment.
     */    
    public void text(String str)
    {
        if (fieldNames.isEmpty() || fieldValues.isEmpty())
            return;
        
        String val = (String)fieldValues.pop();
        val += str;
        fieldValues.push(val);
    }
}
