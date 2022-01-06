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

import java.util.HashMap;
import java.util.HashSet;

class PageResources {

    protected PdfDictionary fontDictionary = new PdfDictionary();
    protected PdfDictionary xObjectDictionary = new PdfDictionary();
    protected PdfDictionary colorDictionary = new PdfDictionary();
    protected PdfDictionary patternDictionary = new PdfDictionary();
    protected PdfDictionary shadingDictionary = new PdfDictionary();
    protected PdfDictionary extGStateDictionary = new PdfDictionary();
    protected PdfDictionary propertyDictionary = new PdfDictionary();
    protected HashSet<PdfName> forbiddenNames;
    protected PdfDictionary originalResources;
    protected int namePtr[] = {0};
    protected HashMap<PdfName, PdfName> usedNames;

    PageResources() {
    }

    void setOriginalResources(PdfDictionary resources, int newNamePtr[]) {
        if (newNamePtr != null)
            namePtr = newNamePtr;
        forbiddenNames = new HashSet<PdfName>();
        usedNames = new HashMap<PdfName, PdfName>();
        if (resources == null)
            return;
        originalResources = new PdfDictionary();
        originalResources.merge(resources);
        for (Object element : resources.getKeys()) {
            PdfName key = (PdfName)element;
            PdfObject sub = PdfReader.getPdfObject(resources.get(key));
            if (sub != null && sub.isDictionary()) {
                PdfDictionary dic = (PdfDictionary)sub;
                for (PdfName element2 : dic.getKeys()) {
                    forbiddenNames.add(element2);
                }
                PdfDictionary dic2 = new PdfDictionary();
                dic2.merge(dic);
                originalResources.put(key, dic2);
            }
        }
    }

    PdfName translateName(PdfName name) {
        PdfName translated = name;
        if (forbiddenNames != null) {
            translated = usedNames.get(name);
            if (translated == null) {
                while (true) {
                    translated = new PdfName("Xi" + namePtr[0]++);
                    if (!forbiddenNames.contains(translated))
                        break;
                }
                usedNames.put(name, translated);
            }
        }
        return translated;
    }

    PdfName addFont(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        fontDictionary.put(name, reference);
        return name;
    }

    PdfName addXObject(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        xObjectDictionary.put(name, reference);
        return name;
    }

    PdfName addColor(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        colorDictionary.put(name, reference);
        return name;
    }

    void addDefaultColor(PdfName name, PdfObject obj) {
        if (obj == null || obj.isNull())
            colorDictionary.remove(name);
        else
            colorDictionary.put(name, obj);
    }

    void addDefaultColor(PdfDictionary dic) {
        colorDictionary.merge(dic);
    }

    void addDefaultColorDiff(PdfDictionary dic) {
        colorDictionary.mergeDifferent(dic);
    }

    PdfName addShading(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        shadingDictionary.put(name, reference);
        return name;
    }

    PdfName addPattern(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        patternDictionary.put(name, reference);
        return name;
    }

    PdfName addExtGState(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        extGStateDictionary.put(name, reference);
        return name;
    }

    PdfName addProperty(PdfName name, PdfIndirectReference reference) {
        name = translateName(name);
        propertyDictionary.put(name, reference);
        return name;
    }

    PdfDictionary getResources() {
       PdfResources resources = new PdfResources();
        if (originalResources != null)
            resources.putAll(originalResources);
        resources.add(PdfName.FONT, fontDictionary);
        resources.add(PdfName.XOBJECT, xObjectDictionary);
        resources.add(PdfName.COLORSPACE, colorDictionary);
        resources.add(PdfName.PATTERN, patternDictionary);
        resources.add(PdfName.SHADING, shadingDictionary);
        resources.add(PdfName.EXTGSTATE, extGStateDictionary);
        resources.add(PdfName.PROPERTIES, propertyDictionary);
        return resources;
    }

    boolean hasResources() {
        return fontDictionary.size() > 0
            || xObjectDictionary.size() > 0
            || colorDictionary.size() > 0
            || patternDictionary.size() > 0
            || shadingDictionary.size() > 0
            || extGStateDictionary.size() > 0
            || propertyDictionary.size() > 0;
    }
}
