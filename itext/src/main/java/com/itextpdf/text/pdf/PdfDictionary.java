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

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.internal.PdfIsoKeys;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <CODE>PdfDictionary</CODE> is the Pdf dictionary object.
 * <P>
 * A dictionary is an associative table containing pairs of objects.
 * The first element of each pair is called the <I>key</I> and the second
 * element is called the <I>value</I>.
 * Unlike dictionaries in the PostScript language, a key must be a
 * <CODE>PdfName</CODE>.
 * A value can be any kind of <CODE>PdfObject</CODE>, including a dictionary.
 * A dictionary is generally used to collect and tie together the attributes
 * of a complex object, with each key-value pair specifying the name and value
 * of an attribute.<BR>
 * A dictionary is represented by two left angle brackets (<<), followed by a
 * sequence of key-value pairs, followed by two right angle brackets (>>).<BR>
 * This object is described in the 'Portable Document Format Reference Manual
 * version 1.7' section 3.2.6 (page 59-60).
 * <P>
 *
 * @see		PdfObject
 * @see		PdfName
 * @see		BadPdfFormatException
 */
public class PdfDictionary extends PdfObject {

    // CONSTANTS

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

    // CLASS VARIABLES

    /** This is the type of this dictionary */
    private PdfName dictionaryType = null;

    /** This is the hashmap that contains all the values and keys of the dictionary */
    protected LinkedHashMap<PdfName, PdfObject> hashMap;

    // CONSTRUCTORS

    /**
     * Constructs an empty <CODE>PdfDictionary</CODE>-object.
     */
    public PdfDictionary() {
        super(DICTIONARY);
        hashMap = new LinkedHashMap<PdfName, PdfObject>();
    }

    public PdfDictionary(int capacity) {
        super(DICTIONARY);
        hashMap = new LinkedHashMap<PdfName, PdfObject>(capacity);
    }

    /**
     * Constructs a <CODE>PdfDictionary</CODE>-object of a certain type.
     *
     * @param type a <CODE>PdfName</CODE>
     */
    public PdfDictionary(final PdfName type) {
        this();
        dictionaryType = type;
        put(PdfName.TYPE, dictionaryType);
    }

    // METHODS OVERRIDING SOME PDFOBJECT METHODS

    /**
     * Writes the PDF representation of this <CODE>PdfDictionary</CODE> as an
     * array of <CODE>byte</CODE> to the given <CODE>OutputStream</CODE>.
     *
     * @param writer for backwards compatibility
     * @param os the <CODE>OutputStream</CODE> to write the bytes to.
     * @throws IOException
     */
    @Override
    public void toPdf(final PdfWriter writer, final OutputStream os) throws IOException {
        PdfWriter.checkPdfIsoConformance(writer, PdfIsoKeys.PDFISOKEY_OBJECT, this);
        os.write('<');
        os.write('<');
        // loop over all the object-pairs in the HashMap
        PdfObject value;
        int type = 0;
        for (Entry<PdfName, PdfObject> e: hashMap.entrySet()) {
        	e.getKey().toPdf(writer, os);
        	value = e.getValue();
			type = value.type();
        	if (type != PdfObject.ARRAY && type != PdfObject.DICTIONARY && type != PdfObject.NAME && type != PdfObject.STRING)
                os.write(' ');
            value.toPdf(writer, os);
        }
        os.write('>');
        os.write('>');
    }

    /**
     * Returns a string representation of this <CODE>PdfDictionary</CODE>.
     *
     * The string doesn't contain any of the content of this dictionary.
     * Rather the string "dictionary" is returned, possibly followed by the
     * type of this <CODE>PdfDictionary</CODE>, if set.
     *
     * @return the string representation of this <CODE>PdfDictionary</CODE>
     * @see com.itextpdf.text.pdf.PdfObject#toString()
     */
    @Override
    public String toString() {
        if (get(PdfName.TYPE) == null)
            return "Dictionary";
        return "Dictionary of type: " + get(PdfName.TYPE);
    }

    // DICTIONARY CONTENT METHODS

    /**
     * Associates the specified <CODE>PdfObject</CODE> as <VAR>value</VAR> with
     * the specified <CODE>PdfName</CODE> as <VAR>key</VAR> in this map.
     *
     * If the map previously contained a mapping for this <VAR>key</VAR>, the
     * old <VAR>value</VAR> is replaced. If the <VAR>value</VAR> is
     * <CODE>null</CODE> or <CODE>PdfNull</CODE> the key is deleted.
     *
     * @param key a <CODE>PdfName</CODE>
     * @param object the <CODE>PdfObject</CODE> to be associated with the
     *   <VAR>key</VAR>
     */
    public void put(final PdfName key, final PdfObject object) {
        if (key == null)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("key.is.null"));
        if (object == null || object.isNull())
            hashMap.remove(key);
        else
            hashMap.put(key, object);
    }

    /**
     * Associates the specified <CODE>PdfObject</CODE> as value to the
     * specified <CODE>PdfName</CODE> as key in this map.
     *
     * If the <VAR>value</VAR> is a <CODE>PdfNull</CODE>, it is treated just as
     * any other <CODE>PdfObject</CODE>. If the <VAR>value</VAR> is
     * <CODE>null</CODE> however nothing is done.
     *
     * @param key a <CODE>PdfName</CODE>
     * @param value the <CODE>PdfObject</CODE> to be associated to the
     * <VAR>key</VAR>
     */
    public void putEx(final PdfName key, final PdfObject value) {
        if (key == null)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("key.is.null"));
        if (value == null)
            return;
        put(key, value);
    }

    /**
     * Copies all of the mappings from the specified <CODE>PdfDictionary</CODE>
     * to this <CODE>PdfDictionary</CODE>.
     *
     * These mappings will replace any mappings previously contained in this
     * <CODE>PdfDictionary</CODE>.
     *
     * @param dic The <CODE>PdfDictionary</CODE> with the mappings to be
     *   copied over
     */
    public void putAll(final PdfDictionary dic) {
        hashMap.putAll(dic.hashMap);
    }

    /**
     * Removes a <CODE>PdfObject</CODE> and its <VAR>key</VAR> from the
     * <CODE>PdfDictionary</CODE>.
     *
     * @param key a <CODE>PdfName</CODE>
     */
    public void remove(final PdfName key) {
        hashMap.remove(key);
    }

    /**
     * Removes all the <CODE>PdfObject</CODE>s and its <VAR>key</VAR>s from the
     * <CODE>PdfDictionary</CODE>.
     * @since 5.0.2
     */
    public void clear() {
        hashMap.clear();
    }

    /**
     * Returns the <CODE>PdfObject</CODE> associated to the specified
     * <VAR>key</VAR>.
     *
     * @param key a <CODE>PdfName</CODE>
     * @return the </CODE>PdfObject</CODE> previously associated to the
     *   <VAR>key</VAR>
     */
    public PdfObject get(final PdfName key) {
        return hashMap.get(key);
    }

    /**
     * Returns the <CODE>PdfObject</CODE> associated to the specified
     * <VAR>key</VAR>, resolving a possible indirect reference to a direct
     * object.
     *
     * This method will never return a <CODE>PdfIndirectReference</CODE>
     * object.
     *
     * @param key A key for the <CODE>PdfObject</CODE> to be returned
     * @return A direct <CODE>PdfObject</CODE> or <CODE>null</CODE>
     */
    public PdfObject getDirectObject(final PdfName key) {
        return PdfReader.getPdfObject(get(key));
    }

    /**
     * Get all keys that are set.
     *
     * @return <CODE>true</CODE> if it is, otherwise <CODE>false</CODE>.
     */
    public Set<PdfName> getKeys() {
        return hashMap.keySet();
    }

    /**
     * Returns the number of <VAR>key</VAR>-<VAR>value</VAR> mappings in this
     * <CODE>PdfDictionary</CODE>.
     *
     * @return the number of <VAR>key</VAR>-<VAR>value</VAR> mappings in this
     *   <CODE>PdfDictionary</CODE>.
     */
    public int size() {
        return hashMap.size();
    }

    /**
     * Returns <CODE>true</CODE> if this <CODE>PdfDictionary</CODE> contains a
     * mapping for the specified <VAR>key</VAR>.
     *
     * @return <CODE>true</CODE> if the key is set, otherwise <CODE>false</CODE>.
     */
    public boolean contains(final PdfName key) {
        return hashMap.containsKey(key);
    }

    // DICTIONARY TYPE METHODS

    /**
     * Checks if a <CODE>Dictionary</CODE> is of the type FONT.
     *
     * @return <CODE>true</CODE> if it is, otherwise <CODE>false</CODE>.
     */
    public boolean isFont() {
        return checkType(FONT);
    }

    /**
     * Checks if a <CODE>Dictionary</CODE> is of the type PAGE.
     *
     * @return <CODE>true</CODE> if it is, otherwise <CODE>false</CODE>.
     */
    public boolean isPage() {
        return checkType(PAGE);
    }

    /**
     * Checks if a <CODE>Dictionary</CODE> is of the type PAGES.
     *
     * @return <CODE>true</CODE> if it is, otherwise <CODE>false</CODE>.
     */
    public boolean isPages() {
        return checkType(PAGES);
    }

    /**
     * Checks if a <CODE>Dictionary</CODE> is of the type CATALOG.
     *
     * @return <CODE>true</CODE> if it is, otherwise <CODE>false</CODE>.
     */
    public boolean isCatalog() {
        return checkType(CATALOG);
    }

    /**
     * Checks if a <CODE>Dictionary</CODE> is of the type OUTLINES.
     *
     * @return <CODE>true</CODE> if it is, otherwise <CODE>false</CODE>.
     */
    public boolean isOutlineTree() {
        return checkType(OUTLINES);
    }
    
    /**
     * Checks the type of the dictionary.
     * @param type the type you're looking for
     * @return true if the type of the dictionary corresponds with the type you're looking for
     */
    public boolean checkType(PdfName type) {
    	if (type == null)
    		return false;
    	if (dictionaryType == null)
    		dictionaryType = getAsName(PdfName.TYPE);
    	return type.equals(dictionaryType);
    }

    // OTHER METHODS

    public void merge(final PdfDictionary other) {
        hashMap.putAll(other.hashMap);
    }

    public void mergeDifferent(final PdfDictionary other) {
        for (PdfName key : other.hashMap.keySet()) {
            if (!hashMap.containsKey(key))
                hashMap.put(key, other.hashMap.get(key));
        }
    }

     // DOWNCASTING GETTERS
     // @author Mark A Storer (2/17/06)

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfDictionary</CODE>,
     * resolving indirect references.
     *
     * The object associated with the <CODE>PdfName</CODE> given is retrieved
     * and resolved to a direct object.
     * If it is a <CODE>PdfDictionary</CODE>, it is cast down and returned as
     * such. Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfDictionary</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfDictionary getAsDict(final PdfName key) {
        PdfDictionary dict = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isDictionary())
            dict = (PdfDictionary) orig;
        return dict;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfArray</CODE>,
     * resolving indirect references.
     *
     * The object associated with the <CODE>PdfName</CODE> given is retrieved
     * and resolved to a direct object.
     * If it is a <CODE>PdfArray</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfArray</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfArray getAsArray(final PdfName key) {
        PdfArray array = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isArray())
            array = (PdfArray) orig;
        return array;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfStream</CODE>,
     * resolving indirect references.
     *
     * The object associated with the <CODE>PdfName</CODE> given is retrieved
     * and resolved to a direct object.
     * If it is a <CODE>PdfStream</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfStream</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfStream getAsStream(final PdfName key) {
        PdfStream stream = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isStream())
            stream = (PdfStream) orig;
        return stream;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfString</CODE>,
     * resolving indirect references.
     *
     * The object associated with the <CODE>PdfName</CODE> given is retrieved
     * and resolved to a direct object.
     * If it is a <CODE>PdfString</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfString</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfString getAsString(final PdfName key) {
        PdfString string = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isString())
            string = (PdfString) orig;
        return string;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfNumber</CODE>,
     * resolving indirect references.
     *
     * The object associated with the <CODE>PdfName</CODE> given is retrieved
     * and resolved to a direct object.
     * If it is a <CODE>PdfNumber</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfNumber</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfNumber getAsNumber(final PdfName key) {
        PdfNumber number = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isNumber())
            number = (PdfNumber) orig;
        return number;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfName</CODE>,
     * resolving indirect references.
     *
     * The object associated with the <CODE>PdfName</CODE> given is retrieved
     * and resolved to a direct object.
     * If it is a <CODE>PdfName</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfName</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfName getAsName(final PdfName key) {
        PdfName name = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isName())
            name = (PdfName) orig;
        return name;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfBoolean</CODE>,
     * resolving indirect references.
     *
     * The object associated with the <CODE>PdfName</CODE> given is retrieved
     * and resolved to a direct object.
     * If it is a <CODE>PdfBoolean</CODE>, it is cast down and returned as such.
     * Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfBoolean</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfBoolean getAsBoolean(final PdfName key) {
        PdfBoolean bool = null;
        PdfObject orig = getDirectObject(key);
        if (orig != null && orig.isBoolean())
            bool = (PdfBoolean)orig;
        return bool;
    }

    /**
     * Returns a <CODE>PdfObject</CODE> as a <CODE>PdfIndirectReference</CODE>.
     *
     * The object associated with the <CODE>PdfName</CODE> given is retrieved
     * If it is a <CODE>PdfIndirectReference</CODE>, it is cast down and returned
     * as such. Otherwise <CODE>null</CODE> is returned.
     *
     * @param key A <CODE>PdfName</CODE>
     * @return the associated <CODE>PdfIndirectReference</CODE> object,
     *   or <CODE>null</CODE>
     */
    public PdfIndirectReference getAsIndirectObject(final PdfName key) {
        PdfIndirectReference ref = null;
        PdfObject orig = get(key); // not getDirect this time.
        if (orig != null && orig.isIndirect())
            ref = (PdfIndirectReference) orig;
        return ref;
    }
}
