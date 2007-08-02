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

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
 * This object is described in the 'Portable Document Format Reference Manual version 1.7'
 * section 3.2.6 (page 59-60).
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
 */
    
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        os.write('<');
        os.write('<');

        // loop over all the object-pairs in the HashMap
        PdfName key;
        PdfObject value;
        int type = 0;
        for (Iterator i = hashMap.keySet().iterator(); i.hasNext(); ) {
            key = (PdfName) i.next();
            value = (PdfObject) hashMap.get(key);
            key.toPdf(writer, os);
            type = value.type();
            if (type != PdfObject.ARRAY && type != PdfObject.DICTIONARY && type != PdfObject.NAME && type != PdfObject.STRING)
                os.write(' ');
            value.toPdf(writer, os);
        }
        os.write('>');
        os.write('>');
    }
    
    // methods concerning the HashMap member value
    
/**
 * Adds a <CODE>PdfObject</CODE> and its key to the <CODE>PdfDictionary</CODE>.
 * If the value is <CODE>null</CODE> or <CODE>PdfNull</CODE> the key is deleted.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @param		value	value of the entry (a <CODE>PdfObject</CODE>)
 */
    
    public void put(PdfName key, PdfObject value) {
        if (value == null || value.isNull())
            hashMap.remove(key);
        else
            hashMap.put(key, value);
    }
    
/**
 * Adds a <CODE>PdfObject</CODE> and its key to the <CODE>PdfDictionary</CODE>.
 * If the value is null it does nothing.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @param		value	value of the entry (a <CODE>PdfObject</CODE>)
 */
    public void putEx(PdfName key, PdfObject value) {
        if (value == null)
            return;
        put(key, value);
    }
    
/**
 * Removes a <CODE>PdfObject</CODE> and its key from the <CODE>PdfDictionary</CODE>.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 */
    
    public void remove(PdfName key) {
        hashMap.remove(key);
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
        return type.equals(dictionaryType);
    }
    
/**
 *  Checks if a <CODE>Dictionary</CODE> is of the type FONT.
 *
 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
 */
    
    public boolean isFont() {
        return FONT.equals(dictionaryType);
    }
    
/**
 *  Checks if a <CODE>Dictionary</CODE> is of the type PAGE.
 *
 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
 */
    
    public boolean isPage() {
        return PAGE.equals(dictionaryType);
    }
    
/**
 *  Checks if a <CODE>Dictionary</CODE> is of the type PAGES.
 *
 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
 */
    
    public boolean isPages() {
        return PAGES.equals(dictionaryType);
    }
    
/**
 *  Checks if a <CODE>Dictionary</CODE> is of the type CATALOG.
 *
 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
 */
    
    public boolean isCatalog() {
        return CATALOG.equals(dictionaryType);
    }
    
/**
 *  Checks if a <CODE>Dictionary</CODE> is of the type OUTLINES.
 *
 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
 */
    
    public boolean isOutlineTree() {
        return OUTLINES.equals(dictionaryType);
    }
    
    public void merge(PdfDictionary other) {
        hashMap.putAll(other.hashMap);
    }
    
    public void mergeDifferent(PdfDictionary other) {
        for (Iterator i = other.hashMap.keySet().iterator(); i.hasNext();) {
            Object key = i.next();
            if (!hashMap.containsKey(key)) {
                hashMap.put(key, other.hashMap.get(key));
            }
        }
    }
    
    public Set getKeys() {
        return hashMap.keySet();
    }

    public void putAll(PdfDictionary dic) {
        hashMap.putAll(dic.hashMap);
    }
    
    public int size() {
        return hashMap.size();
    }
    
    public boolean contains(PdfName key) {
        return hashMap.containsKey(key);
    }
    
    public String toString() {
    	if (get(PdfName.TYPE) == null) return "Dictionary";
    	return "Dictionary of type: " + get(PdfName.TYPE);
    }
    
    /**
     * This function behaves the same as 'get', but will never return an indirect reference,
     * it will always look such references up and return the actual object.
     * @param key 
     * @return null, or a non-indirect object
     */
    public PdfObject getDirectObject(PdfName key) {
        return PdfReader.getPdfObject(get(key));
    }
    
    /**
     * All the getAs functions will return either null, or the specified object type
     * This function will automatically look up indirect references. There's one obvious
     * exception, the one that will only return an indirect reference.  All direct objects
     * come back as a null.
     * Mark A Storer (2/17/06)
     * @param key
     * @return the appropriate object in its final type, or null
     */
    public PdfDictionary getAsDict(PdfName key) {
        PdfDictionary dict = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isDictionary())
            dict = (PdfDictionary) orig;
        return dict;
    }
    
    public PdfArray getAsArray(PdfName key) {
        PdfArray array = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isArray())
            array = (PdfArray) orig;
        return array;
    }
    
    public PdfStream getAsStream(PdfName key) {
        PdfStream stream = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isStream())
            stream = (PdfStream) orig;
        return stream;
    }
    
    public PdfString getAsString(PdfName key) {
        PdfString string = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isString())
            string = (PdfString) orig;
        return string;
    }
    
    public PdfNumber getAsNumber(PdfName key) {
        PdfNumber number = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isNumber())
            number = (PdfNumber) orig;
        return number;
    }
    
    public PdfName getAsName(PdfName key) {
        PdfName name = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isName())
            name = (PdfName) orig;
        return name;
    }
    
    public PdfBoolean getAsBoolean(PdfName key) {
        PdfBoolean bool = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isBoolean())
            bool = (PdfBoolean)orig;
        return bool;
    }
    
    public PdfIndirectReference getAsIndirectObject( PdfName key ) {
        PdfIndirectReference ref = null;
        PdfObject orig = get(key); // not getDirect this time.
        if (orig != null && orig.isIndirect())
            ref = (PdfIndirectReference) orig;
        return ref;
    }
}