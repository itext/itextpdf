/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Paulo Soares.
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

package com.lowagie.text.pdf;

import com.lowagie.text.ExceptionConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import com.lowagie.text.pdf.PdfEncryption;

/**
 * <CODE>PdfDictionary</CODE> is the Pdf dictionary object.
 * <P>
 * A dictionary is an associative table containing pairs of objects. The first element
 * of each pair is called the <I>key</I> and the second element is called the <I>value</I>.
 * Unlike dictionaries in the PostScript language, a key must be a <CODE>PdfName</CODE>.
 * A value can be any kind of <CODE>PdfObject</CODE>, including a dictionary. A dictionary is
 * generally used to collect and tie together the attributes of a complex object, with each
 * key-value pair specifying the name and value of an attribute.<BR>
 * A dictionary is represented by two left angle brackets (<<), followed by a sequence of
 * key-value pairs, followed by two right angle brackets (>>).<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.7 (page 40-41).
 * <P>
 *
 * @see		PdfObject
 * @see		PdfName
 * @see		BadPdfFormatException
 */

class PRDictionary extends PRObject {
    
/** This is the hashmap that contains all the values and keys of the dictionary */
    protected HashMap hashMap;
    
    // constructors
    
/**
 * Constructs an empty <CODE>PdfDictionary</CODE>-object.
 */
    
    PRDictionary() {
        super(DICTIONARY);
        hashMap = new HashMap();
    }
    
    // methods overriding some methods in PdfObject
    
/**
 * Returns the PDF representation of this <CODE>PdfDictionary</CODE>.
 *
 * @return		an array of <CODE>byte</CODE>
 */
    
    public byte[] toPdf(PdfWriter writer) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write('<');
            stream.write('<');
            
            // loop over all the object-pairs in the HashMap
            PRName key;
            PdfObject value;
            for (Iterator i = hashMap.keySet().iterator(); i.hasNext(); ) {
                key = (PRName) i.next();
                value = (PdfObject) hashMap.get(key);
                stream.write(key.toPdf(writer));
                stream.write(' ');
                stream.write(value.toPdf(writer));
                stream.write('\n');
            }
            stream.write('>');
            stream.write('>');
            
            return stream.toByteArray();
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
    // methods concerning the HashMap member value
    
/**
 * Adds a <CODE>PdfObject</CODE> and its key to the <CODE>PdfDictionary</CODE>.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @param		value	value of the entry (a <CODE>PdfObject</CODE>)
 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
 */
    
    final PdfObject put(PRName key, PdfObject value) {
        return (PdfObject) hashMap.put(key, value);
    }
    
/**
 * Removes a <CODE>PdfObject</CODE> and its key from the <CODE>PdfDictionary</CODE>.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
 */
    
    final PdfObject remove(PRName key) {
        return (PdfObject) hashMap.remove(key);
    }
    
/**
 * Gets a <CODE>PdfObject</CODE> with a certain key from the <CODE>PdfDictionary</CODE>.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
 */
    
    final PdfObject get(PRName key) {
        return (PdfObject) hashMap.get(key);
    }
    
    final void putAll(PRDictionary dic) {
        hashMap.putAll(dic.hashMap);
    }
    
    final Iterator getIterator() {
        return hashMap.keySet().iterator();
    }
}