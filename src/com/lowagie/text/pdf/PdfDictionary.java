/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import com.lowagie.text.DocWriter;
import com.lowagie.text.ExceptionConverter;

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

public class PdfDictionary extends PdfObject {
    
    // static membervariables (types of dictionary's)
    
/** This is a possible type of dictionary */
    public static final PdfName FONT = PdfName.FONT;
    
/** This is a possible type of dictionary */
    public static final PdfName OUTLINES = PdfName.OUTLINES;
    
/** This is a possible type of dictionary */
    public static final PdfName PAGE = PdfName.PAGE;
    
/** This is a possible type of dictionary */
    public static final PdfName PAGES = PdfName.PAGES;
    
/** This is a possible type of dictionary */
    public static final PdfName CATALOG = PdfName.CATALOG;
    
    // membervariables
    
/** This is the type of this dictionary */
    private PdfName dictionaryType = null;
    
/** This is the hashmap that contains all the values and keys of the dictionary */
    protected HashMap hashMap;
    
    // constructors
    
/**
 * Constructs an empty <CODE>PdfDictionary</CODE>-object.
 */
    
    public PdfDictionary() {
        super(DICTIONARY);
        hashMap = new HashMap();
    }
    
/**
 * Constructs a <CODE>PdfDictionary</CODE>-object of a certain type.
 *
 * @param		type	a <CODE>PdfName</CODE>
 */
    
    public PdfDictionary(PdfName type) {
        this();
        dictionaryType = type;
        put(PdfName.TYPE, dictionaryType);
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
            PdfName key;
            PdfObject value;
            for (Iterator i = hashMap.keySet().iterator(); i.hasNext(); ) {
                key = (PdfName) i.next();
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
    
    public PdfObject put(PdfName key, PdfObject value) {
        return (PdfObject) hashMap.put(key, value);
    }
    
/**
 * Adds a <CODE>PdfObject</CODE> and its key to the <CODE>PdfDictionary</CODE>.
 * If the value is null it does nothing.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @param		value	value of the entry (a <CODE>PdfObject</CODE>)
 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
 */
    public PdfObject putEx(PdfName key, PdfObject value) {
        if (value == null)
            return null;
        return (PdfObject) hashMap.put(key, value);
    }
    
/**
 * Adds a <CODE>PdfObject</CODE> and its key to the <CODE>PdfDictionary</CODE>.
 * If the value is null the key is deleted.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @param		value	value of the entry (a <CODE>PdfObject</CODE>)
 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
 */
    public PdfObject putDel(PdfName key, PdfObject value) {
        if (value == null)
            return (PdfObject) hashMap.remove(key);;
        return (PdfObject) hashMap.put(key, value);
    }
    
/**
 * Removes a <CODE>PdfObject</CODE> and its key from the <CODE>PdfDictionary</CODE>.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
 */
    
    public PdfObject remove(PdfName key) {
        return (PdfObject) hashMap.remove(key);
    }
    
/**
 * Gets a <CODE>PdfObject</CODE> with a certain key from the <CODE>PdfDictionary</CODE>.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
 */
    
    public PdfObject get(PdfName key) {
        return (PdfObject) hashMap.get(key);
    }
    
    // methods concerning the type of Dictionary
    
/**
 * Checks if a <CODE>PdfDictionary</CODE> is of a certain type.
 *
 * @param		type	a type of dictionary
 * @return		<CODE>true</CODE> of <CODE>false</CODE>
 *
 * @deprecated
 */
    
    public boolean isDictionaryType(PdfName type) {
        return dictionaryType.compareTo(type) == 0;
    }
    
/**
 *  Checks if a <CODE>Dictionary</CODE> is of the type FONT.
 *
 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
 */
    
    public boolean isFont() {
        return dictionaryType.compareTo(FONT) == 0;
    }
    
/**
 *  Checks if a <CODE>Dictionary</CODE> is of the type PAGE.
 *
 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
 */
    
    public boolean isPage() {
        return dictionaryType.compareTo(PAGE) == 0;
    }
    
/**
 *  Checks if a <CODE>Dictionary</CODE> is of the type PAGES.
 *
 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
 */
    
    public boolean isPages() {
        return dictionaryType.compareTo(PAGES) == 0;
    }
    
/**
 *  Checks if a <CODE>Dictionary</CODE> is of the type CATALOG.
 *
 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
 */
    
    public boolean isCatalog() {
        return dictionaryType.compareTo(CATALOG) == 0;
    }
    
/**
 *  Checks if a <CODE>Dictionary</CODE> is of the type OUTLINES.
 *
 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
 */
    
    public boolean isOutlineTree() {
        return dictionaryType.compareTo(OUTLINES) == 0;
    }
    
    public void merge(PdfDictionary other) {
        hashMap.putAll(other.hashMap);
    }
    
    public Set getKeys() {
        return hashMap.keySet();
    }

    public void putAll(PdfDictionary dic) {
        hashMap.putAll(dic.hashMap);
    }
}