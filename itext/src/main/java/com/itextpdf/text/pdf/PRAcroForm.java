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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class captures an AcroForm on input. Basically, it extends Dictionary
 * by indexing the fields of an AcroForm
 * @author Mark Thompson
 */

public class PRAcroForm extends PdfDictionary {

    /**
     * This class holds the information for a single field
     */
    public static class FieldInformation {
    	String fieldName;
        PdfDictionary info;
        PRIndirectReference ref;

        FieldInformation(String fieldName, PdfDictionary info, PRIndirectReference ref) {
            this.fieldName = fieldName;
        	this.info = info;
            this.ref = ref;
        }
        
        /**
         * Returns the name of the widget annotation (the /NM entry).
         * @return	a String or null (if there's no /NM key)
         */
        public String getWidgetName() {
        	PdfObject name = info.get(PdfName.NM);
        	if (name != null)
        		return name.toString();
        	return null;
        }
        
        /**
         * Returns the full name of the field.
         * @return	a String or null
         */
        public String getName() {
        	return fieldName;
        }
        
        public PdfDictionary getInfo() {
        	return info;
        }
        
        public PRIndirectReference getRef() {
        	return ref;
        }
    };
    ArrayList<FieldInformation> fields;
    ArrayList<PdfDictionary> stack;
    HashMap<String, FieldInformation> fieldByName;
    PdfReader reader;

    /**
     * Constructor
     * @param reader reader of the input file
     */
    public PRAcroForm(PdfReader reader) {
        this.reader = reader;
        fields = new ArrayList<FieldInformation>();
        fieldByName = new HashMap<String, FieldInformation>();
        stack = new ArrayList<PdfDictionary>();
    }
    /**
     * Number of fields found
     * @return size
     */
    @Override
    public int size() {
        return fields.size();
    }

    public ArrayList<FieldInformation> getFields() {
        return fields;
    }

    public FieldInformation getField(String name) {
        return fieldByName.get(name);
    }

    /**
     * Given the title (/T) of a reference, return the associated reference
     * @param name a string containing the path
     * @return a reference to the field, or null
     */
    public PRIndirectReference getRefByName(String name) {
        FieldInformation fi = fieldByName.get(name);
        if (fi == null) return null;
        return fi.getRef();
    }
    /**
     * Read, and comprehend the acroform
     * @param root the document root
     */
    public void readAcroForm(PdfDictionary root) {
        if (root == null)
            return;
        hashMap = root.hashMap;
        pushAttrib(root);
        PdfArray fieldlist = (PdfArray)PdfReader.getPdfObjectRelease(root.get(PdfName.FIELDS));
        if (fieldlist != null) {
            iterateFields(fieldlist, null, null);
        }
    }

    /**
     * After reading, we index all of the fields. Recursive.
     * @param fieldlist An array of fields
     * @param fieldDict the last field dictionary we encountered (recursively)
     * @param parentPath the pathname of the field, up to this point or null
     */
    protected void iterateFields(PdfArray fieldlist, PRIndirectReference fieldDict, String parentPath) {
        for (Iterator<PdfObject> it = fieldlist.listIterator(); it.hasNext();) {
            PRIndirectReference ref = (PRIndirectReference)it.next();
            PdfDictionary dict = (PdfDictionary) PdfReader.getPdfObjectRelease(ref);

            // if we are not a field dictionary, pass our parent's values
            PRIndirectReference myFieldDict = fieldDict;
            String fullPath = parentPath;
            PdfString tField = (PdfString)dict.get(PdfName.T);
            boolean isFieldDict = tField != null;

            if (isFieldDict) {
                myFieldDict = ref;
                if (parentPath == null) {
                	fullPath = tField.toString();
                }
                else {
                	fullPath = parentPath + '.' + tField.toString();
                }
            }

            PdfArray kids = (PdfArray)dict.get(PdfName.KIDS);
            if (kids != null) {
                pushAttrib(dict);
                iterateFields(kids, myFieldDict, fullPath);
                stack.remove(stack.size() - 1);   // pop
            }
            else {          // leaf node
                if (myFieldDict != null) {
                    PdfDictionary mergedDict = stack.get(stack.size() - 1);
                    if (isFieldDict)
                        mergedDict = mergeAttrib(mergedDict, dict);

                    mergedDict.put(PdfName.T, new PdfString(fullPath));
                    FieldInformation fi = new FieldInformation(fullPath, mergedDict, myFieldDict);
                    fields.add(fi);
                    fieldByName.put(fullPath, fi);
                }
            }
        }
    }
    /**
     * merge field attributes from two dictionaries
     * @param parent one dictionary
     * @param child the other dictionary
     * @return a merged dictionary
     */
    protected PdfDictionary mergeAttrib(PdfDictionary parent, PdfDictionary child) {
        PdfDictionary targ = new PdfDictionary();
        if (parent != null) targ.putAll(parent);

        for (Object element : child.getKeys()) {
            PdfName key = (PdfName) element;
            if (key.equals(PdfName.DR) || key.equals(PdfName.DA) ||
            key.equals(PdfName.Q)  || key.equals(PdfName.FF) ||
            key.equals(PdfName.DV) || key.equals(PdfName.V)
            || key.equals(PdfName.FT) || key.equals(PdfName.NM)
            || key.equals(PdfName.F)) {
                targ.put(key,child.get(key));
            }
        }
        return targ;
    }
    /**
     * stack a level of dictionary. Merge in a dictionary from this level
     */
    protected void pushAttrib(PdfDictionary dict) {
        PdfDictionary dic = null;
        if (!stack.isEmpty()) {
            dic = stack.get(stack.size() - 1);
        }
        dic = mergeAttrib(dic, dict);
        stack.add(dic);
    }
}
