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

import com.lowagie.text.ExceptionConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <CODE>PdfArray</CODE> is the PDF Array object.
 * <P>
 * An array is a sequence of PDF objects. An array may contain a mixture of object types.
 * An array is written as a left square bracket ([), followed by a sequence of objects,
 * followed by a right square bracket (]).<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.6 (page 40).
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
 * Constructs an <CODE>PdfArray</CODE>-object, containing all the <CODE>PdfObject</CODE>s in a given <CODE>PdfArray</CODE>.
 *
 * @param	array		a <CODE>PdfArray</CODE> that has to be added to the array
 */
    
    public PdfArray(PdfArray array) {
        super(ARRAY);
        arrayList = new ArrayList(array.getArrayList());
    }
    
    // methods overriding some methods in PdfObject
    
/**
 * Returns the PDF representation of this <CODE>PdfArray</CODE>.
 *
 * @return		an array of <CODE>byte</CODE>s
 */
    
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        os.write('[');

        Iterator i = arrayList.iterator();
        PdfObject object;
        if (i.hasNext()) {
            object = (PdfObject) i.next();
            object.toPdf(writer, os);
        }
        while (i.hasNext()) {
            object = (PdfObject) i.next();
            os.write(' ');
            object.toPdf(writer, os);
        }
        os.write(']');
    }
    
    // methods concerning the ArrayList-membervalue
    
/**
 * Returns an ArrayList containing <CODE>PdfObject</CODE>s.
 *
 * @return		an ArrayList
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
}