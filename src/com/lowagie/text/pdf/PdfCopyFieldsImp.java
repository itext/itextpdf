/*
 * Copyright 2004 by Paulo Soares.
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
import com.lowagie.text.DocumentException;
import com.lowagie.text.Document;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author  psoares
 */
class PdfCopyFieldsImp extends PdfWriter {
    
    ArrayList readers = new ArrayList();
    HashMap readers2intrefs = new HashMap();
    HashMap pages2intrefs = new HashMap();
    HashMap visited = new HashMap();
    ArrayList fields = new ArrayList();
    RandomAccessFileOrArray file;
    HashMap fieldTree = new HashMap();
    ArrayList pageRefs = new ArrayList();
    ArrayList pageDics = new ArrayList();
    PdfDictionary resources = new PdfDictionary();
    PdfDictionary form;
    protected List newBookmarks;
    boolean closing = false;
    Document nd;
    
    PdfCopyFieldsImp(OutputStream os) throws DocumentException, IOException {
        this(os, '\0');
    }
    
    PdfCopyFieldsImp(OutputStream os, char pdfVersion) throws DocumentException, IOException {
        super(new PdfDocument(), os);
        pdf.addWriter(this);
        if (pdfVersion != 0)
            super.setPdfVersion(pdfVersion);
        nd = new Document();
        nd.addDocListener(pdf);
    }
    
    void addDocument(PdfReader reader, List pagesToKeep) throws DocumentException {
        if (!readers2intrefs.containsKey(reader) && reader.isTampered())
            throw new DocumentException("The document was reused.");
        reader = new PdfReader(reader);        
        reader.selectPages(pagesToKeep);
        if (reader.getNumberOfPages() == 0)
            return;
        reader.setTampered(false);
        addDocument(reader);
    }
    
    void addDocument(PdfReader reader) throws DocumentException {
        openDoc();
        if (readers2intrefs.containsKey(reader)) {
            reader = new PdfReader(reader);
        }
        else {
            if (reader.isTampered())
                throw new DocumentException("The document was reused.");
            reader.consolidateNamedDestinations();
            reader.setTampered(true);
        }
        reader.shuffleSubsetNames();
        readers2intrefs.put(reader, new IntHashtable());
        readers.add(reader);
        int len = reader.getNumberOfPages();
        IntHashtable refs = new IntHashtable();
        for (int p = 1; p <= len; ++p)
            refs.put(reader.getPageOrigRef(p).getNumber(), 1);
        pages2intrefs.put(reader, refs);
        visited.put(reader, new IntHashtable());
        fields.add(reader.getAcroFields());
    }
    
    
    void propagate(PdfObject obj, PdfIndirectReference refo, boolean restricted) throws IOException {
        if (obj == null)
            return;
//        if (refo != null)
//            addToBody(obj, refo);
        if (obj instanceof PdfIndirectReference)
            return;
        switch (obj.type()) {
            case PdfObject.DICTIONARY:
            case PdfObject.STREAM: {
                PdfDictionary dic = (PdfDictionary)obj;
                for (Iterator it = dic.getKeys().iterator(); it.hasNext();) {
                    PdfName key = (PdfName)it.next();
                    if (restricted && (key.equals(PdfName.PARENT) || key.equals(PdfName.KIDS)))
                        continue;
                    PdfObject ob = dic.get(key);
                    if (ob != null && ob.isIndirect()) {
                        PRIndirectReference ind = (PRIndirectReference)ob;
                        if (!setVisited(ind) && !isPage(ind)) {
                            PdfIndirectReference ref = getNewReference(ind);
                            propagate(PdfReader.getPdfObject(ind), ref, restricted);
                        }
                    }
                    else
                        propagate(ob, null, restricted);
                }
                break;
            }
            case PdfObject.ARRAY: {
                ArrayList list = ((PdfArray)obj).getArrayList();
                PdfArray arr = new PdfArray();
                for (Iterator it = list.iterator(); it.hasNext();) {
                    PdfObject ob = (PdfObject)it.next();
                    if (ob != null && ob.isIndirect()) {
                        PRIndirectReference ind = (PRIndirectReference)ob;
                        if (!isVisited(ind) && !isPage(ind)) {
                            PdfIndirectReference ref = getNewReference(ind);
                            propagate(PdfReader.getPdfObject(ind), ref, restricted);
                        }
                    }
                    else
                        propagate(ob, null, restricted);
                }
                break;
            }
            case PdfObject.INDIRECT: {
                throw new RuntimeException("Reference pointing to reference.");
            }
        }
    }
    
    protected PdfArray branchForm(HashMap level, PdfIndirectReference parent) throws IOException {
        PdfArray arr = new PdfArray();
        for (Iterator it = level.keySet().iterator(); it.hasNext();) {
            String name = (String)it.next();
            Object obj = level.get(name);
            PdfIndirectReference ind = getPdfIndirectReference();
            PdfDictionary dic = new PdfDictionary();
            if (parent != null)
                dic.put(PdfName.PARENT, parent);
            dic.put(PdfName.T, new PdfString(name, PdfObject.TEXT_UNICODE));
            if (obj instanceof HashMap) {
                dic.put(PdfName.KIDS, branchForm((HashMap)obj, ind));
                arr.add(ind);
                addToBody(dic, ind);
            }
            else {
                ArrayList list = (ArrayList)obj;
                dic.mergeDifferent((PdfDictionary)list.get(0));
                if (list.size() == 3) {
                    dic.mergeDifferent((PdfDictionary)list.get(2));
                    int page = ((Integer)list.get(1)).intValue();
                    PdfDictionary pageDic = (PdfDictionary)pageDics.get(page - 1);
                    PdfArray annots = (PdfArray)PdfReader.getPdfObject(pageDic.get(PdfName.ANNOTS));
                    if (annots == null) {
                        annots = new PdfArray();
                        pageDic.put(PdfName.ANNOTS, annots);
                    }
                    annots.add(ind);
                }
                else {
                    PdfArray kids = new PdfArray();
                    for (int k = 1; k < list.size(); k += 2) {
                        int page = ((Integer)list.get(k)).intValue();
                        PdfDictionary pageDic = (PdfDictionary)pageDics.get(page - 1);
                        PdfArray annots = (PdfArray)PdfReader.getPdfObject(pageDic.get(PdfName.ANNOTS));
                        if (annots == null) {
                            annots = new PdfArray();
                            pageDic.put(PdfName.ANNOTS, annots);
                        }
                        PdfDictionary widget = new PdfDictionary();
                        widget.merge((PdfDictionary)list.get(k + 1));
                        widget.put(PdfName.PARENT, ind);
                        PdfIndirectReference wref = addToBody(widget).getIndirectReference();
                        annots.add(wref);
                        kids.add(wref);
                        propagate(widget, null, false);
                    }
                    dic.put(PdfName.KIDS, kids);
                }
                arr.add(ind);
                addToBody(dic, ind);
                propagate(dic, null, false);
            }
        }
        return arr;
    }
    
    protected void createAcroForms() throws IOException {
        if (fieldTree.size() == 0)
            return;
        form = new PdfDictionary();
        form.put(PdfName.DR, resources);
        form.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
        form.put(PdfName.FIELDS, branchForm(fieldTree, null));
    }
    
    public void close() {
        if (closing) {
            super.close();
            return;
        }
        closing = true;
        try {
            closeIt();
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    protected void closeIt() throws DocumentException, IOException {
        for (int k = 0; k < readers.size(); ++k) {
            ((PdfReader)readers.get(k)).removeFields();
        }
        for (int r = 0; r < readers.size(); ++r) {
            PdfReader reader = (PdfReader)readers.get(r);
            for (int page = 1; page <= reader.getNumberOfPages(); ++page) {
                pageRefs.add(getNewReference(reader.getPageOrigRef(page)));
                pageDics.add(reader.getPageN(page));
            }
        }
        mergeFields();
        createAcroForms();
        for (int r = 0; r < readers.size(); ++r) {
                PdfReader reader = (PdfReader)readers.get(r);
                for (int page = 1; page <= reader.getNumberOfPages(); ++page) {
                    PdfDictionary dic = reader.getPageN(page);
                    PdfIndirectReference pageRef = getNewReference(reader.getPageOrigRef(page));
                    PdfIndirectReference parent = root.addPageRef(pageRef);
                    dic.put(PdfName.PARENT, parent);
                    propagate(dic, pageRef, false);
                }
        }
        for (Iterator it = readers2intrefs.keySet().iterator(); it.hasNext();) {
            try {
                PdfReader reader = (PdfReader)it.next();
                file = reader.getSafeFile();
                file.reOpen();
                IntHashtable t = (IntHashtable)readers2intrefs.get(reader);
                int keys[] = t.toOrderedKeys();
                for (int k = 0; k < keys.length; ++k) {
                    PRIndirectReference ref = new PRIndirectReference(reader, keys[k]);
                    addToBody(PdfReader.getPdfObject(ref), t.get(keys[k]));
                }
                file.close();
            }
            finally {
                try {
                    file.close();
                }
                catch (Exception e) {
                    // empty on purpose
                }
            }
        }
            
        pdf.close();
    }
    
    void addPageOffsetToField(HashMap fd, int pageOffset) {
        if (pageOffset == 0)
            return;
        for (Iterator it = fd.values().iterator(); it.hasNext();) {
            ArrayList page = ((AcroFields.Item)it.next()).page;
            for (int k = 0; k < page.size(); ++k)
                page.set(k, new Integer(((Integer)page.get(k)).intValue() + pageOffset));
        }
    }

    void createWidgets(ArrayList list, AcroFields.Item item) {
        for (int k = 0; k < item.merged.size(); ++k) {
            list.add(item.page.get(k));
            PdfDictionary merged = (PdfDictionary)item.merged.get(k);
            PdfObject dr = merged.get(PdfName.DR);
            if (dr != null)
                PdfFormField.mergeResources(resources, (PdfDictionary)PdfReader.getPdfObject(dr));
            PdfDictionary widget = new PdfDictionary();
            for (Iterator it = merged.getKeys().iterator(); it.hasNext();) {
                PdfName key = (PdfName)it.next();
                if (widgetKeys.containsKey(key))
                    widget.put(key, merged.get(key));
            }
            list.add(widget);
        }
    }
    
    void mergeField(String name, AcroFields.Item item) {
        HashMap map = fieldTree;
        StringTokenizer tk = new StringTokenizer(name, ".");
        if (!tk.hasMoreTokens())
            return;
        while (true) {
            String s = tk.nextToken();
            Object obj = map.get(s);
            if (tk.hasMoreTokens()) {
                if (obj == null) {
                    obj = new HashMap();
                    map.put(s, obj);
                    map = (HashMap)obj;
                    continue;
                }
                else if (obj instanceof HashMap)
                    map = (HashMap)obj;
                else
                    return;
            }
            else {
                if (obj instanceof HashMap)
                    return;
                PdfDictionary merged = (PdfDictionary)item.merged.get(0);
                if (obj == null) {
                    PdfDictionary field = new PdfDictionary();
                    for (Iterator it = merged.getKeys().iterator(); it.hasNext();) {
                        PdfName key = (PdfName)it.next();
                        if (fieldKeys.containsKey(key))
                            field.put(key, merged.get(key));
                    }
                    ArrayList list = new ArrayList();
                    list.add(field);
                    createWidgets(list, item);
                    map.put(s, list);
                }
                else {
                    ArrayList list = (ArrayList)obj;
                    PdfDictionary field = (PdfDictionary)list.get(0);
                    PdfName type1 = (PdfName)field.get(PdfName.FT);
                    PdfName type2 = (PdfName)merged.get(PdfName.FT);
                    if (type1 == null || !type1.equals(type2))
                        return;
                    int flag1 = 0;
                    PdfObject f1 = field.get(PdfName.FF);
                    if (f1 != null && f1.isNumber())
                        flag1 = ((PdfNumber)f1).intValue();
                    int flag2 = 0;
                    PdfObject f2 = merged.get(PdfName.FF);
                    if (f2 != null && f2.isNumber())
                        flag2 = ((PdfNumber)f2).intValue();
                    if (type1.equals(PdfName.BTN)) {
                        if (((flag1 ^ flag2) & PdfFormField.FF_PUSHBUTTON) != 0)
                            return;
                        if ((flag1 & PdfFormField.FF_PUSHBUTTON) == 0 && ((flag1 ^ flag2) & PdfFormField.FF_RADIO) != 0)
                            return;
                    }
                    else if (type1.equals(PdfName.CH)) {
                        if (((flag1 ^ flag2) & PdfFormField.FF_COMBO) != 0)
                            return;
                    }
                    createWidgets(list, item);
                }
                return;
            }
        }
    }
    
    void mergeWithMaster(HashMap fd) {
        for (Iterator it = fd.keySet().iterator(); it.hasNext();) {
            String name = (String)it.next();
            mergeField(name, (AcroFields.Item)fd.get(name));
        }
    }
    
    void mergeFields() {
        int pageOffset = 0;
        for (int k = 0; k < fields.size(); ++k) {
            HashMap fd = ((AcroFields)fields.get(k)).getFields();
            addPageOffsetToField(fd, pageOffset);
            mergeWithMaster(fd);
            pageOffset += ((PdfReader)readers.get(k)).getNumberOfPages();
        }
    }

    public PdfIndirectReference getPageReference(int page) {
        return (PdfIndirectReference)pageRefs.get(page - 1);
    }
    
    protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
        try {
            PdfDictionary cat = ((PdfDocument)document).getCatalog(rootObj);
            if (form != null) {
                PdfIndirectReference ref = addToBody(form).getIndirectReference();
                cat.put(PdfName.ACROFORM, ref);
            }
            if (newBookmarks == null || newBookmarks.size() == 0)
                return cat;
            PdfDictionary top = new PdfDictionary();
            PdfIndirectReference topRef = getPdfIndirectReference();
            Object kids[] = SimpleBookmark.iterateOutlines(this, topRef, newBookmarks, false);
            top.put(PdfName.FIRST, (PdfIndirectReference)kids[0]);
            top.put(PdfName.LAST, (PdfIndirectReference)kids[1]);
            top.put(PdfName.COUNT, new PdfNumber(((Integer)kids[2]).intValue()));
            addToBody(top, topRef);
            cat.put(PdfName.OUTLINES, topRef);            
            return cat;
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    protected PdfIndirectReference getNewReference(PRIndirectReference ref) {
        return new PdfIndirectReference(0, getNewObjectNumber(ref.getReader(), ref.getNumber(), 0));
    }
    
    protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
        IntHashtable refs = (IntHashtable)readers2intrefs.get(reader);
        int n = refs.get(number);
        if (n == 0) {
            n = getIndirectReferenceNumber();
            refs.put(number, n);
        }
        return n;
    }
    
    protected boolean isVisited(PdfReader reader, int number, int generation) {
        IntHashtable refs = (IntHashtable)readers2intrefs.get(reader);
        return refs.containsKey(number);
    }
    
    protected boolean isVisited(PRIndirectReference ref) {
        IntHashtable refs = (IntHashtable)visited.get(ref.getReader());
        return refs.containsKey(ref.getNumber());
    }
    
    protected boolean setVisited(PRIndirectReference ref) {
        IntHashtable refs = (IntHashtable)visited.get(ref.getReader());
        return (refs.put(ref.getNumber(), 1) != 0);
    }
    
    protected boolean isPage(PRIndirectReference ref) {
        IntHashtable refs = (IntHashtable)pages2intrefs.get(ref.getReader());
        return refs.containsKey(ref.getNumber());
    }

    RandomAccessFileOrArray getReaderFile(PdfReader reader) {
            return file;
    }

    /**
     * Sets the bookmarks. The list structure is defined in
     * <CODE>SimpleBookmark#</CODE>.
     * @param outlines the bookmarks or <CODE>null</CODE> to remove any
     */    
    public void setOutlines(List outlines) {
        newBookmarks = outlines;
    }

    public void openDoc() {
        if (!nd.isOpen())
            nd.open();
    }    
    
    protected static final HashMap widgetKeys = new HashMap();
    protected static final HashMap fieldKeys = new HashMap();
    static {
        Integer one = new Integer(1);
        widgetKeys.put(PdfName.SUBTYPE, one);
        widgetKeys.put(PdfName.CONTENTS, one);
        widgetKeys.put(PdfName.RECT, one);
        widgetKeys.put(PdfName.NM, one);
        widgetKeys.put(PdfName.M, one);
        widgetKeys.put(PdfName.F, one);
        widgetKeys.put(PdfName.BS, one);
        widgetKeys.put(PdfName.BORDER, one);
        widgetKeys.put(PdfName.AP, one);
        widgetKeys.put(PdfName.AS, one);
        widgetKeys.put(PdfName.C, one);
        widgetKeys.put(PdfName.A, one);
        widgetKeys.put(PdfName.STRUCTPARENT, one);
        widgetKeys.put(PdfName.OC, one);
        widgetKeys.put(PdfName.H, one);
        widgetKeys.put(PdfName.MK, one);
        widgetKeys.put(PdfName.DA, one);
        widgetKeys.put(PdfName.Q, one);
        fieldKeys.put(PdfName.AA, one);
        fieldKeys.put(PdfName.FT, one);
        fieldKeys.put(PdfName.TU, one);
        fieldKeys.put(PdfName.TM, one);
        fieldKeys.put(PdfName.FF, one);
        fieldKeys.put(PdfName.V, one);
        fieldKeys.put(PdfName.DV, one);
        fieldKeys.put(PdfName.DS, one);
        fieldKeys.put(PdfName.RV, one);
        fieldKeys.put(PdfName.OPT, one);
        fieldKeys.put(PdfName.MAXLEN, one);
        fieldKeys.put(PdfName.TI, one);
        fieldKeys.put(PdfName.I, one);
        fieldKeys.put(PdfName.LOCK, one);
        fieldKeys.put(PdfName.SV, one);
    }
}
