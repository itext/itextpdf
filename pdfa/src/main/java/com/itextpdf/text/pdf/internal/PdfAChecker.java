/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Alexander Chingarev, Bruno Lowagie, et al.
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
package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.pdf.*;

import java.util.HashMap;
import java.util.HashSet;

abstract public class PdfAChecker {

    protected PdfAConformanceLevel conformanceLevel;
    protected HashMap<RefKey, PdfObject> cachedObjects = new HashMap<RefKey, PdfObject>();
    private HashSet<PdfName> keysForCheck = initKeysForCheck();
    private static byte[] emptyByteArray = new byte[]{};


    PdfAChecker(PdfAConformanceLevel conformanceLevel) {
        this.conformanceLevel = conformanceLevel;
    }

    abstract protected HashSet<PdfName> initKeysForCheck();

    public void cacheObject(PdfIndirectReference iref, PdfObject obj) {
        if (obj.type() == 0) {
            cachedObjects.put(new RefKey(iref), obj);
        } else if (obj instanceof PdfDictionary) {
            cachedObjects.put(new RefKey(iref), cleverPdfDictionaryClone((PdfDictionary)obj));
        } else if (obj.isArray()){
            cachedObjects.put(new RefKey(iref), cleverPdfArrayClone((PdfArray)obj));
        }
    }

    abstract public void close(PdfWriter writer);

    private PdfObject cleverPdfArrayClone(PdfArray array) {
        PdfArray newArray = new PdfArray();
        for (int i = 0; i < array.size(); i++) {
            PdfObject obj = array.getPdfObject(i);
            if (obj instanceof PdfDictionary)
                newArray.add(cleverPdfDictionaryClone((PdfDictionary)obj));
            else
                newArray.add(obj);
        }

        return newArray;
    }

    private PdfObject cleverPdfDictionaryClone(PdfDictionary dict) {
        PdfDictionary newDict;
        if (dict.isStream()) {
            newDict = new PdfStream(emptyByteArray);
            newDict.remove(PdfName.LENGTH);
        } else
            newDict = new PdfDictionary();

        for (PdfName key : dict.getKeys())
            if (keysForCheck.contains(key))
                newDict.put(key, dict.get(key));

        return newDict;
    }

    protected PdfObject getDirectObject(PdfObject obj) {
        if (obj == null)
            return null;
        // use counter to prevent indirect reference cycling
        int count = 0;
        // resolve references
        while (obj instanceof PdfIndirectReference) {
            PdfObject curr;
            if (obj.isIndirect())
                curr = PdfReader.getPdfObject(obj);
            else
                curr = cachedObjects.get(new RefKey((PdfIndirectReference)obj));
            if (curr == null) break;
            obj = curr;
            //10 - is max allowed reference chain
            if (count++ > 10)
                break;
        }
        return obj;
    }

    protected PdfDictionary getDirectDictionary(PdfObject obj) {
        obj = getDirectObject(obj);
        if (obj != null && obj instanceof PdfDictionary)
            return (PdfDictionary)obj;
        return null;
    }

    protected PdfStream getDirectStream(PdfObject obj) {
        obj = getDirectObject(obj);
        if (obj != null && obj.isStream())
            return (PdfStream)obj;
        return null;
    }

    protected PdfArray getDirectArray(PdfObject obj) {
        obj = getDirectObject(obj);
        if (obj != null && obj.isArray())
            return (PdfArray)obj;
        return null;
    }

    abstract protected void checkFont(PdfWriter writer, int key, Object obj1);

    abstract protected void checkImage(PdfWriter writer, int key, Object obj1);

    abstract protected void checkInlineImage(PdfWriter writer, int key, Object obj1);

    abstract protected void checkFormXObj(PdfWriter writer, int key, Object obj1);

    abstract protected void checkGState(PdfWriter writer, int key, Object obj1);

    abstract protected void checkLayer(PdfWriter writer, int key, Object obj1);

    abstract protected void checkTrailer(PdfWriter writer, int key, Object obj1);

    abstract protected void checkStream(PdfWriter writer, int key, Object obj1);

    abstract protected void checkFileSpec(PdfWriter writer, int key, Object obj1);

    abstract protected void checkPdfObject(PdfWriter writer, int key, Object obj1);

    abstract protected void checkCanvas(PdfWriter writer, int key, Object obj1);

    abstract protected void checkColor(PdfWriter writer, int key, Object obj1);

    abstract protected void checkAnnotation(PdfWriter writer, int key, Object obj1);

    abstract protected void checkAction(PdfWriter writer, int key, Object obj1);

    abstract protected void checkForm(PdfWriter writer, int key, Object obj1);

    abstract protected void checkStructElem(PdfWriter writer, int key, Object obj1);

    abstract protected void checkOutputIntent(PdfWriter writer, int key, Object obj1);

    void checkPdfAConformance(PdfWriter writer, int key, Object obj1) {
        if (writer == null || !writer.isPdfIso())
            return;
        switch (key) {
            case PdfIsoKeys.PDFISOKEY_FONT:
                checkFont(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_IMAGE:
                checkImage(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_GSTATE:
                checkGState(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_LAYER:
                checkLayer(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_TRAILER:
                checkTrailer(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_STREAM:
                checkStream(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_FILESPEC:
                checkFileSpec(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_OBJECT:
                checkPdfObject(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_CANVAS:
                checkCanvas(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_COLOR:
            case PdfIsoKeys.PDFISOKEY_CMYK:
            case PdfIsoKeys.PDFISOKEY_RGB:
                checkColor(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_ANNOTATION:
                checkAnnotation(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_ACTION:
                checkAction(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_FORM:
                checkForm(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_STRUCTELEM:
                if (checkStructure(conformanceLevel))
                    checkStructElem(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_INLINE_IMAGE:
                checkInlineImage(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_OUTPUTINTENT:
                checkOutputIntent(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_FORM_XOBJ:
                checkFormXObj(writer, key, obj1);
                break;
            default:
                break;
        }
    }

    public static boolean checkStructure(PdfAConformanceLevel conformanceLevel) {
        return conformanceLevel == PdfAConformanceLevel.PDF_A_1A
                || conformanceLevel == PdfAConformanceLevel.PDF_A_2A
                || conformanceLevel == PdfAConformanceLevel.PDF_A_3A;
    }

    protected static boolean checkFlag(int flags, int flag) {
        return (flags & flag) != 0;
    }
}
