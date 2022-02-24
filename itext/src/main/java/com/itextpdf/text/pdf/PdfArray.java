/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
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
package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.internal.PdfIsoKeys;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <CODE>PdfArray</CODE> is the PDF Array object.
 * <P>
 * An array is a sequence of PDF objects. An array may contain a mixture of
 * object types.
 * An array is written as a left square bracket ([), followed by a sequence of
 * objects, followed by a right square bracket (]).<BR>
 * This object is described in the 'Portable Document Format Reference Manual
 * version 1.7' section 3.2.5 (page 58).
 *
 * @see		PdfObject
 */
public class PdfArray extends PdfObject implements Iterable<PdfObject> {

	// CLASS VARIABLES

	/** this is the actual array of PdfObjects */
    protected ArrayList<PdfObject> arrayList;

    // constructors

    /**
     * Constructs an empty <CODE>PdfArray</CODE>-object.
     */
    public PdfArray() {
        super(ARRAY);
        arrayList = new ArrayList<PdfObject>();
    }

    public PdfArray(int capacity) {
        super(ARRAY);
        arrayList = new ArrayList<PdfObject>(capacity);
    }

    /**
     * Constructs an <CODE>PdfArray</CODE>-object, containing 1
     * <CODE>PdfObject</CODE>.
     *
     * @param	object		a <CODE>PdfObject</CODE> that has to be added to the array
     */
    public PdfArray(final PdfObject object) {
        super(ARRAY);
        arrayList = new ArrayList<PdfObject>();
        arrayList.add(object);
    }

    /**
     * Constructs a <CODE>PdfArray</CODE>-object, containing all
     * <CODE>float</CODE> values in a specified array.
     *
     * The <CODE>float</CODE> values are internally converted to
     * <CODE>PdfNumber</CODE> objects.
     *
     * @param values    an array of <CODE>float</CODE> values to be added
     */
    public PdfArray(final float values[]) {
        super(ARRAY);
        arrayList = new ArrayList<PdfObject>();
        add(values);
    }

    /**
     * Constructs a <CODE>PdfArray</CODE>-object, containing all
     * <CODE>int</CODE> values in a specified array.
     *
     * The <CODE>int</CODE> values are internally converted to
     * <CODE>PdfNumber</CODE> objects.
     *
     * @param values    an array of <CODE>int</CODE> values to be added
     */
    public PdfArray(final int values[]) {
        super(ARRAY);
        arrayList = new ArrayList<PdfObject>();
        add(values);
    }

    /**
     * Constructs a <CODE>PdfArray</CODE>, containing all elements of a
     * specified <CODE>ArrayList</CODE>.
     *
     * @param l    an <CODE>ArrayList</CODE> with <CODE>PdfObject</CODE>s to be
     *   added to the array
     * @throws ClassCastException if the <CODE>ArrayList</CODE> contains
     *   something that isn't a <CODE>PdfObject</CODE>
     * @since 2.1.3
     */
    public PdfArray(final List<PdfObject> l) {
        this();
        for (PdfObject element : l)
            add(element);
    }

    /**
     * Constructs an <CODE>PdfArray</CODE>-object, containing all
     * <CODE>PdfObject</CODE>s in a specified <CODE>PdfArray</CODE>.
     *
     * @param array    a <CODE>PdfArray</CODE> to be added to the array
     */
    public PdfArray(final PdfArray array) {
        super(ARRAY);
        arrayList = new ArrayList<PdfObject>(array.arrayList);
    }

    // METHODS OVERRIDING SOME PDFOBJECT METHODS

    /**
     * Writes the PDF representation of this <CODE>PdfArray</CODE> as an array
     * of <CODE>byte</CODE> to the specified <CODE>OutputStream</CODE>.
     *
     * @param writer for backwards compatibility
     * @param os the <CODE>OutputStream</CODE> to write the bytes to.
     */
    @Override
    public void toPdf(final PdfWriter writer, final OutputStream os) throws IOException {
        PdfWriter.checkPdfIsoConformance(writer, PdfIsoKeys.PDFISOKEY_OBJECT, this);
        os.write('[');

        Iterator<PdfObject> i = arrayList.iterator();
        PdfObject object;
        int type = 0;
        if (i.hasNext()) {
            object = i.next();
            if (object == null)
                object = PdfNull.PDFNULL;
            object.toPdf(writer, os);
        }
        while (i.hasNext()) {
            object = i.next();
            if (object == null)
                object = PdfNull.PDFNULL;
            type = object.type();
            if (type != PdfObject.ARRAY && type != PdfObject.DICTIONARY && type != PdfObject.NAME && type != PdfObject.STRING)
                os.write(' ');
            object.toPdf(writer, os);
        }
        os.write(']');
    }

    /**
     * Returns a string representation of this <CODE>PdfArray</CODE>.
     *
     * The string representation consists of a list of all
     * <CODE>PdfObject</CODE>s contained in this <CODE>PdfArray</CODE>,
     * enclosed in square brackets ("[]"). Adjacent elements are separated
     * by the characters ", " (comma and space).
     *
     * @return the string representation of this <CODE>PdfArray</CODE>
     */
    @Override
    public String toString() {
    	return arrayList.toString();
    }

    // ARRAY CONTENT METHODS

    /**
     * Overwrites a specified location of the array, returning the previous
     * value
     *
     * @param idx The index of the element to be overwritten
     * @param obj new value for the specified index
     * @throws IndexOutOfBoundsException if the specified position doesn't exist
     * @return the previous value
     * @since 2.1.5
     */
    public PdfObject set(final int idx, final PdfObject obj) {
        return arrayList.set(idx, obj);
    }

    /**
     * Remove the element at the specified position from the array.
     *
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param idx The index of the element to be removed.
     * @throws IndexOutOfBoundsException the specified position doesn't exist
     * @since 2.1.5
     */
    public PdfObject remove(final int idx) {
        return arrayList.remove(idx);
    }

    /**
     * Get the internal arrayList for this PdfArray.  Not Recommended.
     *
     * @deprecated
     * @return the internal ArrayList.  Naughty Naughty.
     */
    @Deprecated
    public ArrayList<PdfObject> getArrayList() {
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
     * Returns <CODE>true</CODE> if the array is empty.
     *
     * @return <CODE>true</CODE> if the array is empty
     * @since 2.1.5
     */
    public boolean isEmpty() {
        return arrayList.isEmpty();
    }

    /**
     * Adds a <CODE>PdfObject</CODE> to the end of the <CODE>PdfArray</CODE>.
     *
     * The <CODE>PdfObject</CODE> will be the last element.
     *
     * @param object <CODE>PdfObject</CODE> to add
     * @return always <CODE>true</CODE>
     */
    public boolean add(final PdfObject object) {
        return arrayList.add(object);
    }

    /**
     * Adds an array of <CODE>float</CODE> values to end of the
     * <CODE>PdfArray</CODE>.
     *
     * The values will be the last elements.
     * The <CODE>float</CODE> values are internally converted to
     * <CODE>PdfNumber</CODE> objects.
     *
     * @param values An array of <CODE>float</CODE> values to add
     * @return always <CODE>true</CODE>
     */
    public boolean add(final float values[]) {
        for (int k = 0; k < values.length; ++k)
            arrayList.add(new PdfNumber(values[k]));
        return true;
    }

    /**
     * Adds an array of <CODE>int</CODE> values to end of the <CODE>PdfArray</CODE>.
     *
     * The values will be the last elements.
     * The <CODE>int</CODE> values are internally converted to
     * <CODE>PdfNumber</CODE> objects.
     *
     * @param values An array of <CODE>int</CODE> values to add
     * @return always <CODE>true</CODE>
     */
    public boolean add(final int values[]) {
        for (int k = 0; k < values.length; ++k)
            arrayList.add(new PdfNumber(values[k]));
        return true;
    }

    /**
     * Inserts the specified element at the specified position.
     *
     * Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index The index at which the specified element is to be inserted
     * @param element The element to be inserted
     * @throws IndexOutOfBoundsException if the specified index is larger than the
     *   last position currently set, plus 1.
     * @since 2.1.5
     */
    public void add(final int index, final PdfObject element) {
        arrayList.add(index, element);
    }

    /**
     * Inserts a <CODE>PdfObject</CODE> at the beginning of the
     * <CODE>PdfArray</CODE>.
     *
     * The <CODE>PdfObject</CODE> will be the first element, any other elements
     * will be shifted to the right (adds one to their indices).
     *
     * @param object The <CODE>PdfObject</CODE> to add
     */
    public void addFirst(final PdfObject object) {
        arrayList.add(0, object);
    }

    /**
     * Checks if the <CODE>PdfArray</CODE> already contains a certain
     * <CODE>PdfObject</CODE>.
     *
     * @param object The <CODE>PdfObject</CODE> to check
     * @return <CODE>true</CODE>
     */
    public boolean contains(final PdfObject object) {
        return arrayList.contains(object);
    }

    /**
     * Returns the list iterator for the array.
     *
     * @return a ListIterator
     */
    public ListIterator<PdfObject> listIterator() {
        return arrayList.listIterator();
    }

    /**
     * Returns the <CODE>PdfObject</CODE> with the specified index.
     *
     * A possible indirect references is not resolved, so the returned
     * <CODE>PdfObject</CODE> may be either a direct object or an indirect
     * reference, depending on how the object is stored in the
     * <CODE>PdfArray</CODE>.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return A <CODE>PdfObject</CODE>
     */
    public PdfObject getPdfObject(final int idx) {
        return arrayList.get(idx);
    }

    /**
     * Returns the <CODE>PdfObject</CODE> with the specified index, resolving
     * a possible indirect reference to a direct object.
     *
     * Thus this method will never return a <CODE>PdfIndirectReference</CODE>
     * object.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return A direct <CODE>PdfObject</CODE> or <CODE>null</CODE>
     */
    public PdfObject getDirectObject(final int idx) {
        return PdfReader.getPdfObject(getPdfObject(idx));
    }

    // DOWNCASTING GETTERS
    // @author Mark A Storer (2/17/06)

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfDictionary</CODE>,
     * resolving indirect references.
     *
     * The object corresponding to the specified index is retrieved and
     * resolvedto a direct object.
     * If it is a <CODE>PdfDictionary</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return the corresponding <CODE>PdfDictionary</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfDictionary getAsDict(final int idx) {
        PdfDictionary dict = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isDictionary())
            dict = (PdfDictionary) orig;
        return dict;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfArray</CODE>,
     * resolving indirect references.
     *
     * The object corresponding to the specified index is retrieved and
     * resolved to a direct object.
     * If it is a <CODE>PdfArray</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return the corresponding <CODE>PdfArray</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfArray getAsArray(final int idx) {
        PdfArray array = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isArray())
            array = (PdfArray) orig;
        return array;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfStream</CODE>,
     * resolving indirect references.
     *
     * The object corresponding to the specified index is retrieved and
     * resolved to a direct object.
     * If it is a <CODE>PdfStream</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return the corresponding <CODE>PdfStream</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfStream getAsStream(final int idx) {
        PdfStream stream = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isStream())
            stream = (PdfStream) orig;
        return stream;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfString</CODE>,
     * resolving indirect references.
     *
     * The object corresponding to the specified index is retrieved and
     * resolved to a direct object.
     * If it is a <CODE>PdfString</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return the corresponding <CODE>PdfString</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfString getAsString(final int idx) {
        PdfString string = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isString())
            string = (PdfString) orig;
        return string;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfNumber</CODE>,
     * resolving indirect references.
     *
     * The object corresponding to the specified index is retrieved and
     * resolved to a direct object.
     * If it is a <CODE>PdfNumber</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return the corresponding <CODE>PdfNumber</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfNumber getAsNumber(final int idx) {
        PdfNumber number = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isNumber())
            number = (PdfNumber) orig;
        return number;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfName</CODE>,
     * resolving indirect references.
     *
     * The object corresponding to the specified index is retrieved and
     * resolved to a direct object.
     * If it is a <CODE>PdfName</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return the corresponding <CODE>PdfName</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfName getAsName(final int idx) {
        PdfName name = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isName())
            name = (PdfName) orig;
        return name;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfBoolean</CODE>,
     * resolving indirect references.
     *
     * The object corresponding to the specified index is retrieved and
     * resolved to a direct object.
     * If it is a <CODE>PdfBoolean</CODE>, it is cast down and returned as
     * such. Otherwise <CODE>null</CODE> is returned.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return the corresponding <CODE>PdfBoolean</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfBoolean getAsBoolean(final int idx) {
        PdfBoolean bool = null;
        PdfObject orig = getDirectObject(idx);
        if (orig != null && orig.isBoolean())
            bool = (PdfBoolean) orig;
        return bool;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfIndirectReference</CODE>.
     *
     * The object corresponding to the specified index is retrieved.
     * If it is a <CODE>PdfIndirectReference</CODE>, it is cast down and
     * returned as such. Otherwise <CODE>null</CODE> is returned.
     *
     * @param idx The index of the <CODE>PdfObject</CODE> to be returned
     * @return the corresponding <CODE>PdfIndirectReference</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfIndirectReference getAsIndirectObject(final int idx) {
        PdfIndirectReference ref = null;
        PdfObject orig = getPdfObject(idx); // not getDirect this time.
        if (orig instanceof PdfIndirectReference)
            ref = (PdfIndirectReference) orig;
        return ref;
    }

	/**
	 * @return an iterator that iterates over the {@link PdfObject}s in this PdfArray.
	 */
	public Iterator<PdfObject> iterator() {
		return arrayList.iterator();
	}
	
	/**
	 * 
	 * @return this PdfArray's values as a long[] 
	 * @since 5.3.5
	 */
	public long[] asLongArray(){
		long[] rslt = new long[size()];
        for (int k = 0; k < rslt.length; ++k) {
            rslt[k] = getAsNumber(k).longValue();
        }
        return rslt;
	}

    /**
     *
     * @return this PdfArray's values as a double[]
     * @since 5.5.6
     */
    public double[] asDoubleArray() {
        double[] rslt = new double[size()];
        for (int k = 0; k < rslt.length; ++k) {
            rslt[k] = getAsNumber(k).doubleValue();
        }
        return rslt;
    }
}
