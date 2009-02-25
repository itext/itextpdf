/*
 * $Id$
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * <CODE>PdfArray</CODE> is the PDF Array object.
 * <P>
 * An array is a sequence of PDF objects. An array may contain a mixture of object types.
 * An array is written as a left square bracket ([), followed by a sequence of objects,
 * followed by a right square bracket (]).<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.7'
 * section 3.2.5 (page 58).
 *
 * @see		PdfObject
 */

public class PdfArray extends PdfObject {

    // membervariables

/** this is the actual array of PdfObjects */
    protected ArrayList arrayList;

    // constructors

/**
 * Constructs an empty <CODE>PdfArray</CODE>-object.
 */

    public PdfArray() {
        super(ARRAY);
        arrayList = new ArrayList();
    }

/**
 * Constructs an <CODE>PdfArray</CODE>-object, containing 1 <CODE>PdfObject</CODE>.
 *
 * @param	object		a <CODE>PdfObject</CODE> that has to be added to the array
 */

    public PdfArray(PdfObject object) {
        super(ARRAY);
        arrayList = new ArrayList();
        arrayList.add(object);
    }

    public PdfArray(float values[]) {
        super(ARRAY);
        arrayList = new ArrayList();
        add(values);
    }

    public PdfArray(int values[]) {
        super(ARRAY);
        arrayList = new ArrayList();
        add(values);
    }

    /**
     * Constructs a PdfArray with the elements of an ArrayList.
     * Throws a ClassCastException if the ArrayList contains something
     * that isn't a PdfObject.
     * @param	l 	an ArrayList with PdfObjects
     * @since 2.1.3
     */
    public PdfArray(ArrayList l) {
        this();
        for (Iterator i = l.iterator(); i.hasNext(); )
        	add((PdfObject)i.next());
    }

/**
 * Constructs an <CODE>PdfArray</CODE>-object, containing all the <CODE>PdfObject</CODE>s in a given <CODE>PdfArray</CODE>.
 *
 * @param	array		a <CODE>PdfArray</CODE> that has to be added to the array
 */

    public PdfArray(PdfArray array) {
        super(ARRAY);
        arrayList = new ArrayList(array.arrayList);
    }

    // methods overriding some methods in PdfObject

/**
 * Returns the PDF representation of this <CODE>PdfArray</CODE>.
 */

    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        os.write('[');

        Iterator i = arrayList.iterator();
        PdfObject object;
        int type = 0;
        if (i.hasNext()) {
            object = (PdfObject) i.next();
            if (object == null)
                object = PdfNull.PDFNULL;
            object.toPdf(writer, os);
        }
        while (i.hasNext()) {
            object = (PdfObject) i.next();
            if (object == null)
                object = PdfNull.PDFNULL;
            type = object.type();
            if (type != PdfObject.ARRAY && type != PdfObject.DICTIONARY && type != PdfObject.NAME && type != PdfObject.STRING)
                os.write(' ');
            object.toPdf(writer, os);
        }
        os.write(']');
    }

    // methods concerning the ArrayList-membervalue


    /**
     * Overwrites a given location of the array, returning
     * the previous value
     * @param idx index to overwrite
     * @param obj new value for the given index
     * @return the previous value
     * @since 2.1.5
     */

    public PdfObject set( int idx, PdfObject obj) {
        return (PdfObject) arrayList.set( idx, obj );
    }

    /**
     * Remove the given element from the array
     * @param idx index of the element to be removed.
     * @since 2.1.5
     */

    public PdfObject remove( int idx) {
        return (PdfObject) arrayList.remove( idx );
    }

    /**
     * Get the internal arrayList for this PdfArray.  Not Recommended.
     * @deprecated
     * @return the internal ArrayList.  Naughty Naughty.
     */

    public ArrayList getArrayList() {
        return arrayList;
    }

    /**
     * Returns the number of entries in the array.
     *
     * @return		the size of the ArrayList
     */

    public int size() {
        return arrayList.size();
    }

/**
 * Adds a <CODE>PdfObject</CODE> to the <CODE>PdfArray</CODE>.
 *
 * @param		object			<CODE>PdfObject</CODE> to add
 * @return		<CODE>true</CODE>
 */

    public boolean add(PdfObject object) {
        return arrayList.add(object);
    }

    public boolean add(float values[]) {
        for (int k = 0; k < values.length; ++k)
            arrayList.add(new PdfNumber(values[k]));
        return true;
    }

    public boolean add(int values[]) {
        for (int k = 0; k < values.length; ++k)
            arrayList.add(new PdfNumber(values[k]));
        return true;
    }

/**
 * Adds a <CODE>PdfObject</CODE> to the <CODE>PdfArray</CODE>.
 * <P>
 * The newly added object will be the first element in the <CODE>ArrayList</CODE>.
 *
 * @param		object			<CODE>PdfObject</CODE> to add
 */

    public void addFirst(PdfObject object) {
        arrayList.add(0, object);
    }

/**
 * Checks if the <CODE>PdfArray</CODE> already contains a certain <CODE>PdfObject</CODE>.
 *
 * @param		object			<CODE>PdfObject</CODE> to check
 * @return		<CODE>true</CODE>
 */

    public boolean contains(PdfObject object) {
        return arrayList.contains(object);
    }

    public ListIterator listIterator() {
        return arrayList.listIterator();
    }

    public String toString() {
    	return arrayList.toString();
    }

    public PdfObject getPdfObject( int idx ) {
        return (PdfObject)arrayList.get(idx);
    }

    public PdfObject getDirectObject( int idx ) {
        return PdfReader.getPdfObject(getPdfObject(idx));
    }

    // more of the same like PdfDictionary. (MAS 2/17/06)
    public PdfDictionary getAsDict(int idx) {
        PdfDictionary dict = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isDictionary())
            dict = (PdfDictionary) orig;
        return dict;
    }

    public PdfArray getAsArray(int idx) {
        PdfArray array = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isArray())
            array = (PdfArray) orig;
        return array;
    }

    public PdfStream getAsStream(int idx) {
        PdfStream stream = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isStream())
            stream = (PdfStream) orig;
        return stream;
    }

    public PdfString getAsString(int idx) {
        PdfString string = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isString())
            string = (PdfString) orig;
        return string;
    }

    public PdfNumber getAsNumber(int idx) {
        PdfNumber number = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isNumber())
            number = (PdfNumber) orig;
        return number;
    }

    public PdfName getAsName(int idx) {
        PdfName name = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isName())
            name = (PdfName) orig;
        return name;
    }

    public PdfBoolean getAsBoolean(int idx) {
        PdfBoolean bool = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isBoolean())
            bool = (PdfBoolean) orig;
        return bool;
    }

    public PdfIndirectReference getAsIndirectObject(int idx) {
        PdfIndirectReference ref = null;
        PdfObject orig = getPdfObject(idx); // not getDirect this time.
        if (orig != null && orig.isIndirect())
            ref = (PdfIndirectReference) orig;
        return ref;
    }
}