/*
 * Copyright 2003 by Paulo Soares.
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.ExceptionConverter;

class PdfStamperImp extends PdfWriter {
    HashMap readers2intrefs = new HashMap();
    HashMap readers2file = new HashMap();
    RandomAccessFileOrArray file;
    PdfReader reader;
    IntHashtable myXref = new IntHashtable();
    /** Integer(page number) -> PageStamp */
    HashMap pagesToContent = new HashMap();
    boolean closed = false;
    /** Holds value of property rotateContents. */
    private boolean rotateContents = true;
    protected AcroFields acroFields;
    protected boolean flat = false;
    protected int namePtr[] = {0};
    protected boolean namedAsNames;
    protected List newBookmarks;
    protected HashSet partialFlattening = new HashSet();
    protected boolean useVp = false;
    protected int vp = 0;
    protected HashMap fieldTemplates = new HashMap();
    protected boolean fieldsAdded = false;
    protected int sigFlags = 0;
    protected boolean append;
    protected IntHashtable marked;
    
    /** Creates new PdfStamperImp.
     * @param reader the read PDF
     * @param os the output destination
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @throws DocumentException on error
     */
    PdfStamperImp(PdfReader reader, OutputStream os, char pdfVersion, boolean append) throws DocumentException, IOException {
        super(new PdfDocument(), os);
        if (reader.isTampered())
            throw new DocumentException("The original document was reused. Read it again from file.");
        reader.setTampered(true);
        this.reader = reader;
        file = reader.getSafeFile();
        this.append = append;
        if (append) {
            if (reader.isRebuilt())
                throw new DocumentException("Append mode requires a document without errors even if recovery was possible.");
            if (reader.isEncrypted())
                throw new DocumentException("Append mode requires a document without encryption.");
            HEADER = getISOBytes("\n");
            file.reOpen();
            byte buf[] = new byte[8192];
            int n;
            while ((n = file.read(buf)) > 0)
                this.os.write(buf, 0, n);
            file.close();
            prevxref = reader.getLastXref();
            reader.setAppendable(true);
        }
        else {
            if (pdfVersion == 0)
                super.setPdfVersion(reader.getPdfVersion());
            else
                super.setPdfVersion(pdfVersion);
        }
        super.open();
        pdf.addWriter(this);
        if (append) {
            body.setRefnum(reader.getXrefSize());
            marked = new IntHashtable();
            if (reader.isNewXrefType())
                setFullCompression();
        }
    }
    
    void close(HashMap moreInfo) throws DocumentException, IOException {
        if (closed)
            return;
        if (useVp) {
            reader.setViewerPreferences(vp);
            markUsed(reader.getTrailer().get(PdfName.ROOT));
        }
        if (flat)
            flatFields();
        addFieldResources();
        if (sigFlags != 0) {
            PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(reader.getCatalog().get(PdfName.ACROFORM), reader.getCatalog());
            if (acroForm != null) {
                acroForm.put(PdfName.SIGFLAGS, new PdfNumber(sigFlags));
                markUsed(acroForm);
            }
        }
        closed = true;
        addSharedObjectsToBody();
        setOutlines();
        setJavaScript();
        PRIndirectReference iInfo = null;
        try {
            file.reOpen();
            alterContents();
            PdfObject xb[] = reader.xrefObj;
            int idx = 1;
            iInfo = (PRIndirectReference)reader.trailer.get(PdfName.INFO);
            int skip = -1;
            if (iInfo != null)
                skip = iInfo.getNumber();
            int rootN = ((PRIndirectReference)reader.trailer.get(PdfName.ROOT)).getNumber();
            if (append) {
                int keys[] = marked.getKeys();
                for (int k = 0; k < keys.length; ++k) {
                    int j = keys[k];
                    if (xb[j] != null && skip != j) {
                        addToBody(xb[j], j, j != rootN);
                    }
                }
            }
            else {
                for (int k = 1; k < xb.length; ++k) {
                    if (xb[k] != null && skip != k) {
                        addToBody(xb[k], getNewObjectNumber(reader, k, 0), k != rootN);
                    }
                }
            }
        }
        finally {
            try {
                file.close();
            }
            catch (Exception e) {
                // empty on purpose
            }
        }
        PdfIndirectReference encryption = null;
        PdfObject fileID = null;
        if (crypto != null) {
            PdfIndirectObject encryptionObject = addToBody(crypto.getEncryptionDictionary(), false);
            encryption = encryptionObject.getIndirectReference();
            fileID = crypto.getFileID();
        }
        PRIndirectReference iRoot = (PRIndirectReference)reader.trailer.get(PdfName.ROOT);
        PdfIndirectReference root = new PdfIndirectReference(0, getNewObjectNumber(reader, iRoot.getNumber(), 0));
        PdfIndirectReference info = null;
        PdfDictionary oldInfo = (PdfDictionary)PdfReader.getPdfObject(iInfo);
        PdfDictionary newInfo = new PdfDictionary();
        if (oldInfo != null) {
            for (Iterator i = oldInfo.getKeys().iterator(); i.hasNext();) {
                PdfName key = (PdfName)i.next();
                PdfObject value = PdfReader.getPdfObject(oldInfo.get(key));
                newInfo.put(key, value);
            }
        }
        if (moreInfo != null) {
            for (Iterator i = moreInfo.keySet().iterator(); i.hasNext();) {
                String key = (String)i.next();
                PdfName keyName = new PdfName(key);
                String value = (String)moreInfo.get(key);
                if (value == null)
                    newInfo.remove(keyName);
                else
                    newInfo.put(keyName, new PdfString(value, PdfObject.TEXT_UNICODE));
            }
        }
        if (append) {
            if (iInfo == null)
                info = addToBody(newInfo, false).getIndirectReference();
            else
                info = addToBody(newInfo, iInfo.getNumber(), false).getIndirectReference();
        }
        else {
            if (!newInfo.getKeys().isEmpty())
                info = addToBody(newInfo, false).getIndirectReference();
        }
        // write the cross-reference table of the body
        body.writeCrossReferenceTable(os, root, info, encryption, fileID, prevxref);
        if (fullCompression) {
            os.write(getISOBytes("startxref\n"));
            os.write(getISOBytes(String.valueOf(body.offset())));
            os.write(getISOBytes("\n%%EOF\n"));
        }
        else {
            PdfTrailer trailer = new PdfTrailer(body.size(),
            body.offset(),
            root,
            info,
            encryption,
            fileID, prevxref);
            trailer.toPdf(this, os);
        }
        os.flush();
        if (isCloseStream())
            os.close();
    }
    
    void applyRotation(int pageNumber, ByteBuffer out) {
        if (!rotateContents)
            return;
        Rectangle page = reader.getPageSizeWithRotation(pageNumber);
        int rotation = page.getRotation();
        switch (rotation) {
            case 90:
                out.append(PdfContents.ROTATE90);
                out.append(page.top());
                out.append(' ').append('0').append(PdfContents.ROTATEFINAL);
                break;
            case 180:
                out.append(PdfContents.ROTATE180);
                out.append(page.right());
                out.append(' ');
                out.append(page.top());
                out.append(PdfContents.ROTATEFINAL);
                break;
            case 270:
                out.append(PdfContents.ROTATE270);
                out.append('0').append(' ');
                out.append(page.right());
                out.append(PdfContents.ROTATEFINAL);
                break;
        }
    }
    
    void alterContents() throws IOException {
        for (Iterator i = pagesToContent.keySet().iterator(); i.hasNext();) {
            Integer pageNumber = (Integer)i.next();
            PageStamp ps = (PageStamp)pagesToContent.get(pageNumber);
            PdfDictionary pdic = reader.getPageN(pageNumber.intValue());
            markUsed(pdic);
            PdfArray ar = null;
            PdfObject content = PdfReader.getPdfObject(pdic.get(PdfName.CONTENTS), pdic);
            if (content == null) {
                ar = new PdfArray();
                pdic.put(PdfName.CONTENTS, ar);
            }
            else if (content.isArray()) {
                ar = (PdfArray)content;
                markUsed(ar);
            }
            else if (content.isStream()) {
                ar = new PdfArray();
                ar.add(pdic.get(PdfName.CONTENTS));
                pdic.put(PdfName.CONTENTS, ar);
            }
            else {
                ar = new PdfArray();
                pdic.put(PdfName.CONTENTS, ar);
            }
            ByteBuffer out = new ByteBuffer();
            if (ps.under != null) {
                out.append(PdfContents.SAVESTATE);
                applyRotation(pageNumber.intValue(), out);
                out.append(ps.under.getInternalBuffer());
                out.append(PdfContents.RESTORESTATE);
            }
            if (ps.over != null)
                out.append(PdfContents.SAVESTATE);
            PdfStream stream = new PdfStream(out.toByteArray());
            try{stream.flateCompress();}catch(Exception e){throw new ExceptionConverter(e);}
            ar.addFirst(addToBody(stream).getIndirectReference());
            out.reset();
            if (ps.over != null) {
                out.append(' ');
                out.append(PdfContents.RESTORESTATE);
                out.append(PdfContents.SAVESTATE);
                applyRotation(pageNumber.intValue(), out);
                out.append(ps.over.getInternalBuffer());
                out.append(PdfContents.RESTORESTATE);
                stream = new PdfStream(out.toByteArray());
                try{stream.flateCompress();}catch(Exception e){throw new ExceptionConverter(e);}
                ar.add(addToBody(stream).getIndirectReference());
            }
            alterResources(ps);
        }
    }

    void alterResources(PageStamp ps) {
        PdfDictionary dic = reader.getPageN(ps.pageNumber);
        dic.put(PdfName.RESOURCES, ps.pageResources.getResources());
    }
    
    protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
        IntHashtable ref = (IntHashtable)readers2intrefs.get(reader);
        if (ref != null) {
            int n = ref.get(number);
            if (n == 0) {
                n = getIndirectReferenceNumber();
                ref.put(number, n);
            }
            return n;
        }
        if (currentPdfReaderInstance == null) {
            if (append)
                return number;
            int n = myXref.get(number);
            if (n == 0) {
                n = getIndirectReferenceNumber();
                myXref.put(number, n);
            }
            return n;
        }
        else
            return currentPdfReaderInstance.getNewObjectNumber(number, generation);
    }
    
    RandomAccessFileOrArray getReaderFile(PdfReader reader) {
        if (readers2intrefs.containsKey(reader)) {
            RandomAccessFileOrArray raf = (RandomAccessFileOrArray)readers2file.get(reader);
            if (raf != null)
                return raf;
            return reader.getSafeFile();
        }
        if (currentPdfReaderInstance == null)
            return file;
        else
            return currentPdfReaderInstance.getReaderFile();
    }
    
    public void registerReader(PdfReader reader, boolean openFile) throws IOException {
        if (readers2intrefs.containsKey(reader))
            return;
        readers2intrefs.put(reader, new IntHashtable());
        if (openFile) {
            RandomAccessFileOrArray raf = reader.getSafeFile();
            readers2file.put(reader, raf);
            raf.reOpen();
        }
    }
    
    public void unRegisterReader(PdfReader reader) {
        if (!readers2intrefs.containsKey(reader))
            return;
        readers2intrefs.remove(reader);
        RandomAccessFileOrArray raf = (RandomAccessFileOrArray)readers2file.get(reader);
        if (raf == null)
            return;
        readers2file.remove(reader);
        try{raf.close();}catch(Exception e){}
    }

    static void findAllObjects(PdfReader reader, PdfObject obj, IntHashtable hits) {
        if (obj == null)
            return;
        switch (obj.type()) {
            case PdfObject.INDIRECT:
                PRIndirectReference iref = (PRIndirectReference)obj;
                if (reader != iref.getReader())
                    return;
                if (hits.containsKey(iref.getNumber()))
                    return;
                hits.put(iref.getNumber(), 1);
                findAllObjects(reader, PdfReader.getPdfObject(obj), hits);
                return;
            case PdfObject.ARRAY:
                ArrayList lst = ((PdfArray)obj).getArrayList();
                for (int k = 0; k < lst.size(); ++k) {
                    findAllObjects(reader, (PdfObject)lst.get(k), hits);
                }
                return;
            case PdfObject.DICTIONARY:
                PdfDictionary dic = (PdfDictionary)obj;
                for (Iterator it = dic.getKeys().iterator(); it.hasNext();) {
                    PdfName name = (PdfName)it.next();
                    findAllObjects(reader, dic.get(name), hits);
                }
                return;
        }
    }
    
    public void addComments(FdfReader fdf) throws IOException{
        if (readers2intrefs.containsKey(fdf))
            return;
        PdfDictionary catalog = fdf.getCatalog();
        catalog = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.FDF));
        if (catalog == null)
            return;
        PdfArray annots = (PdfArray)PdfReader.getPdfObject(catalog.get(PdfName.ANNOTS));
        if (annots == null || annots.size() == 0)
            return;
        registerReader(fdf, false);
        IntHashtable hits = new IntHashtable();
        ArrayList an = new ArrayList();
        ArrayList ar = annots.getArrayList();
        for (int k = 0; k < ar.size(); ++k) {
            PdfObject obj = (PdfObject)ar.get(k);
            PdfDictionary annot = (PdfDictionary)PdfReader.getPdfObject(obj);
            PdfNumber page = (PdfNumber)PdfReader.getPdfObject(annot.get(PdfName.PAGE));
            if (page == null || page.intValue() >= reader.getNumberOfPages())
                continue;
            findAllObjects(fdf, obj, hits);
            an.add(obj);
        }
        int arhits[] = hits.getKeys();
        for (int k = 0; k < arhits.length; ++k) {
            int n = arhits[k];
            addToBody(fdf.xrefObj[n], getNewObjectNumber(fdf, n, 0));
        }
        for (int k = 0; k < an.size(); ++k) {
            PdfObject obj = (PdfObject)an.get(k);
            PdfDictionary annot = (PdfDictionary)PdfReader.getPdfObject(obj);
            PdfNumber page = (PdfNumber)PdfReader.getPdfObject(annot.get(PdfName.PAGE));
            PdfDictionary dic = reader.getPageN(page.intValue() + 1);
            PdfArray annotsp = (PdfArray)PdfReader.getPdfObject(dic.get(PdfName.ANNOTS), dic);
            if (annotsp == null) {
                annotsp = new PdfArray();
                dic.put(PdfName.ANNOTS, annotsp);
                markUsed(dic);
            }
            markUsed(annotsp);
            annotsp.add(obj);
        }
    }
    
    PageStamp getPageStamp(int pageNum) {
        Integer page = new Integer(pageNum);
        PageStamp ps = (PageStamp)pagesToContent.get(page);
        if (ps == null) {
            ps = new PageStamp(this, reader, pageNum);
            pagesToContent.put(page, ps);
        }
        return ps;
    }
    
    PdfContentByte getUnderContent(int pageNum) {
        if (pageNum < 1 || pageNum > reader.getNumberOfPages())
            return null;
        PageStamp ps = getPageStamp(pageNum);
        if (ps.under == null)
            ps.under = new StampContent(this, pageNum);
        return ps.under;
    }
    
    PdfContentByte getOverContent(int pageNum) {
        if (pageNum < 1 || pageNum > reader.getNumberOfPages())
            return null;
        PageStamp ps = getPageStamp(pageNum);
        if (ps.over == null)
            ps.over = new StampContent(this, pageNum);
        return ps.over;
    }
    
    /** Getter for property rotateContents.
     * @return Value of property rotateContents.
     *
     */
    boolean isRotateContents() {
        return this.rotateContents;
    }
    
    /** Setter for property rotateContents.
     * @param rotateContents New value of property rotateContents.
     *
     */
    void setRotateContents(boolean rotateContents) {
        this.rotateContents = rotateContents;
    }
    
    boolean isContentWritten() {
        return body.size() > 1;
    }
    
    AcroFields getAcroFields() {
        if (acroFields == null)
            acroFields = new AcroFields(reader, this);
        return acroFields;
    }

    void setFormFlattening(boolean flat) {
        this.flat = flat;
    }
    
    boolean partialFormFlattening(String name) {
        getAcroFields();
        if (!acroFields.getFields().containsKey(name))
            return false;
        partialFlattening.add(name);
        return true;
    }
    
    void flatFields() {
        if (append)
            throw new IllegalArgumentException("Field flattening is not supported in append mode.");
        getAcroFields();
        HashMap fields = acroFields.getFields();
        if (fieldsAdded && partialFlattening.isEmpty()) {
            for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
                partialFlattening.add(i.next());
            }
        }
        PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(reader.getCatalog().get(PdfName.ACROFORM));
        ArrayList acroFds = null;
        if (acroForm != null) {
            PdfArray array = (PdfArray)PdfReader.getPdfObject(acroForm.get(PdfName.FIELDS), acroForm);
            if (array != null)
                acroFds = array.getArrayList();
        }
        for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
            String name = (String)i.next();
            if (!partialFlattening.isEmpty() && !partialFlattening.contains(name))
                continue;
            AcroFields.Item item = (AcroFields.Item)fields.get(name);
            for (int k = 0; k < item.merged.size(); ++k) {
                PdfDictionary merged = (PdfDictionary)item.merged.get(k);
                PdfNumber ff = (PdfNumber)PdfReader.getPdfObject(merged.get(PdfName.F));
                int flags = 0;
                if (ff != null)
                    flags = ff.intValue();
                int page = ((Integer)item.page.get(k)).intValue();
                PdfDictionary appDic = (PdfDictionary)PdfReader.getPdfObject(merged.get(PdfName.AP));
                if (appDic != null && (flags & PdfFormField.FLAGS_PRINT) != 0 && (flags & PdfFormField.FLAGS_HIDDEN) == 0) {
                    PdfObject obj = appDic.get(PdfName.N);
                    PdfAppearance app = null;
                    PdfObject objReal = PdfReader.getPdfObject(obj);
                    if (obj instanceof PdfIndirectReference && !obj.isIndirect())
                        app = new PdfAppearance((PdfIndirectReference)obj);
                    else if (objReal instanceof PdfStream) {
                        ((PdfDictionary)objReal).put(PdfName.SUBTYPE, PdfName.FORM);
                        app = new PdfAppearance((PdfIndirectReference)obj);
                    }
                    else {
                        if (objReal.isDictionary()) {
                            PdfName as = (PdfName)PdfReader.getPdfObject(merged.get(PdfName.AS));
                            if (as != null) {
                                PdfIndirectReference iref = (PdfIndirectReference)((PdfDictionary)objReal).get(as);
                                if (iref != null) {
                                    app = new PdfAppearance(iref);
                                    if (iref.isIndirect()) {
                                        objReal = PdfReader.getPdfObject(iref);
                                        ((PdfDictionary)objReal).put(PdfName.SUBTYPE, PdfName.FORM);
                                    }
                                }
                            }
                        }
                    }
                    if (app != null) {
                        Rectangle box = PdfReader.getNormalizedRectangle((PdfArray)PdfReader.getPdfObject(merged.get(PdfName.RECT)));
                        PdfContentByte cb = getOverContent(page);
                        cb.setLiteral("Q ");
                        cb.addTemplate(app, box.left(), box.bottom());
                        cb.setLiteral("q ");
                    }
                }
                if (partialFlattening.isEmpty())
                    continue;
                PdfDictionary pageDic = reader.getPageN(page);
                PdfArray annots = (PdfArray)PdfReader.getPdfObject(pageDic.get(PdfName.ANNOTS));
                if (annots == null)
                    continue;
                ArrayList ar = annots.getArrayList();
                for (int idx = 0; idx < ar.size(); ++idx) {
                    PdfObject ran = (PdfObject)ar.get(idx);
                    if (!ran.isIndirect())
                        continue;
                    PdfObject ran2 = (PdfObject)item.widget_refs.get(k);
                    if (!ran2.isIndirect())
                        continue;
                    if (((PRIndirectReference)ran).getNumber() == ((PRIndirectReference)ran2).getNumber()) {
                        ar.remove(idx--);
                        PRIndirectReference wdref = (PRIndirectReference)ran2;
                        while (true) {
                            PdfDictionary wd = (PdfDictionary)PdfReader.getPdfObject(wdref);
                            PRIndirectReference parentRef = (PRIndirectReference)wd.get(PdfName.PARENT);
                            PdfReader.killIndirect(wdref);
                            if (parentRef == null) { // reached AcroForm
                                for (int fr = 0; fr < acroFds.size(); ++fr) {
                                    PdfObject h = (PdfObject)acroFds.get(fr);
                                    if (h.isIndirect() && ((PRIndirectReference)h).getNumber() == wdref.getNumber()) {
                                        acroFds.remove(fr);
                                        --fr;
                                    }
                                }
                                break;
                            }
                            PdfDictionary parent = (PdfDictionary)PdfReader.getPdfObject(parentRef);
                            PdfArray kids = (PdfArray)PdfReader.getPdfObject(parent.get(PdfName.KIDS));
                            ArrayList kar = kids.getArrayList();
                            for (int fr = 0; fr < kar.size(); ++fr) {
                                PdfObject h = (PdfObject)kar.get(fr);
                                if (h.isIndirect() && ((PRIndirectReference)h).getNumber() == wdref.getNumber()) {
                                    kar.remove(fr);
                                    --fr;
                                }
                            }
                            if (!kar.isEmpty())
                                break;
                            wdref = parentRef;
                        }
                    }
                }
                if (ar.size() == 0) {
                    PdfReader.killIndirect(pageDic.get(PdfName.ANNOTS));
                    pageDic.remove(PdfName.ANNOTS);
                }
            }
        }
        if (!fieldsAdded && partialFlattening.isEmpty()) {
            for (int page = 1; page <= reader.getNumberOfPages(); ++page) {
                PdfDictionary pageDic = reader.getPageN(page);
                PdfArray annots = (PdfArray)PdfReader.getPdfObject(pageDic.get(PdfName.ANNOTS));
                if (annots == null)
                    continue;
                ArrayList ar = annots.getArrayList();
                for (int idx = 0; idx < ar.size(); ++idx) {
                    PdfObject annoto = PdfReader.getPdfObject((PdfObject)ar.get(idx));
                        if ((annoto instanceof PdfIndirectReference) && !annoto.isIndirect())
                            continue;
                    PdfDictionary annot = (PdfDictionary)annoto;
                    if (PdfName.WIDGET.equals(annot.get(PdfName.SUBTYPE))) {
                        ar.remove(idx);
                        --idx;
                    }
                }
                if (ar.size() == 0) {
                    PdfReader.killIndirect(pageDic.get(PdfName.ANNOTS));
                    pageDic.remove(PdfName.ANNOTS);
                }
            }
            eliminateAcroformObjects();
        }
    }

    void eliminateAcroformObjects() {
        PdfObject acro = reader.getCatalog().get(PdfName.ACROFORM);
        if (acro == null)
            return;
        PdfDictionary acrodic = (PdfDictionary)PdfReader.getPdfObject(acro);
        PdfObject iFields = acrodic.get(PdfName.FIELDS);
        if (iFields != null) {
            PdfDictionary kids = new PdfDictionary();
            kids.put(PdfName.KIDS, iFields);
            sweepKids(kids);
        }
//        PdfReader.killIndirect(acro);
//        reader.getCatalog().remove(PdfName.ACROFORM);
    }
    
    void sweepKids(PdfObject obj) {
        PdfObject oo = PdfReader.killIndirect(obj);
        if (!oo.isDictionary())
            return;
        PdfDictionary dic = (PdfDictionary)oo;
        PdfArray kids = (PdfArray)PdfReader.killIndirect(dic.get(PdfName.KIDS));
        if (kids == null)
            return;
        ArrayList ar = kids.getArrayList();
        for (int k = 0; k < ar.size(); ++k) {
            sweepKids((PdfObject)ar.get(k));
        }
    }
    
    public PdfIndirectReference getPageReference(int page) {
        PdfIndirectReference ref = reader.getPageOrigRef(page);
        if (ref == null)
            throw new IllegalArgumentException("Invalid page number " + page);
        return ref;
    }
    
    public void addAnnotation(PdfAnnotation annot) {
        throw new RuntimeException("Unsupported in this context. Use PdfStamper.addAnnotation()");
    }
    
    void addDocumentField(PdfIndirectReference ref) {
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
        if (acroForm == null) {
            acroForm = new PdfDictionary();
            catalog.put(PdfName.ACROFORM, acroForm);
            markUsed(catalog);
        }
        PdfArray fields = (PdfArray)PdfReader.getPdfObject(acroForm.get(PdfName.FIELDS), acroForm);
        if (fields == null) {
            fields = new PdfArray();
            acroForm.put(PdfName.FIELDS, fields);
            markUsed(acroForm);
        }
        fields.add(ref);
        markUsed(fields);
    }
    
    void addFieldResources() {
        if (fieldTemplates.size() == 0)
            return;
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
        if (acroForm == null) {
            acroForm = new PdfDictionary();
            catalog.put(PdfName.ACROFORM, acroForm);
            markUsed(catalog);
        }
        PdfDictionary dr = (PdfDictionary)PdfReader.getPdfObject(acroForm.get(PdfName.DR), acroForm);
        if (dr == null) {
            dr = new PdfDictionary();
            acroForm.put(PdfName.DR, dr);
            markUsed(acroForm);
        }
        markUsed(dr);
        for (Iterator it = fieldTemplates.keySet().iterator(); it.hasNext();) {
            PdfTemplate template = (PdfTemplate)it.next();
            PdfFormField.mergeResources(dr, (PdfDictionary)template.getResources(), this);
        }
        PdfDictionary fonts = (PdfDictionary)PdfReader.getPdfObject(dr.get(PdfName.FONT));
        if (fonts != null && acroForm.get(PdfName.DA) == null) {
            acroForm.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
            markUsed(acroForm);
        }
    }
    
    void expandFields(PdfFormField field, ArrayList allAnnots) {
        allAnnots.add(field);
        ArrayList kids = field.getKids();
        if (kids != null) {
            for (int k = 0; k < kids.size(); ++k)
                expandFields((PdfFormField)kids.get(k), allAnnots);
        }
    }

    void addAnnotation(PdfAnnotation annot, int page) {
        try {
            ArrayList allAnnots = new ArrayList();
            if (annot.isForm()) {
                fieldsAdded = true;
                getAcroFields();
                PdfFormField field = (PdfFormField)annot;
                if (field.getParent() != null)
                    return;
                expandFields(field, allAnnots);
            }
            else
                allAnnots.add(annot);
            for (int k = 0; k < allAnnots.size(); ++k) {
                annot = (PdfAnnotation)allAnnots.get(k);
                int realPage = page;
                if (annot.getPlaceInPage() > 0)
                    realPage = annot.getPlaceInPage();
                if (annot.isForm()) {
                    if (!annot.isUsed()) {
                        HashMap templates = annot.getTemplates();
                        if (templates != null)
                            fieldTemplates.putAll(templates);
                    }
                    PdfFormField field = (PdfFormField)annot;
                    if (field.getParent() == null)
                        addDocumentField(field.getIndirectReference());
                }
                if (annot.isAnnotation()) {
                    PdfDictionary dic = reader.getPageN(realPage);
                    PdfArray annots = (PdfArray)PdfReader.getPdfObject(dic.get(PdfName.ANNOTS), dic);
                    if (annots == null) {
                        annots = new PdfArray();
                        dic.put(PdfName.ANNOTS, annots);
                        markUsed(dic);
                    }
                    annots.add(annot.getIndirectReference());
                    markUsed(annots);
                    if (!annot.isUsed()) {
                        PdfRectangle rect = (PdfRectangle)annot.get(PdfName.RECT);
                        if (rect.left() != 0 || rect.right() != 0 || rect.top() != 0 || rect.bottom() != 0) {
                            int rotation = reader.getPageRotation(realPage);
                            Rectangle pageSize = reader.getPageSizeWithRotation(realPage);
                            switch (rotation) {
                                case 90:
                                    annot.put(PdfName.RECT, new PdfRectangle(
                                    pageSize.top() - rect.bottom(),
                                    rect.left(),
                                    pageSize.top() - rect.top(),
                                    rect.right()));
                                    break;
                                case 180:
                                    annot.put(PdfName.RECT, new PdfRectangle(
                                    pageSize.right() - rect.left(),
                                    pageSize.top() - rect.bottom(),
                                    pageSize.right() - rect.right(),
                                    pageSize.top() - rect.top()));
                                    break;
                                case 270:
                                    annot.put(PdfName.RECT, new PdfRectangle(
                                    rect.bottom(),
                                    pageSize.right() - rect.left(),
                                    rect.top(),
                                    pageSize.right() - rect.right()));
                                    break;
                            }
                        }
                    }
                }
                if (!annot.isUsed()) {
                    annot.setUsed();
                    addToBody(annot, annot.getIndirectReference());
                }
            }
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    private void outlineTravel(PRIndirectReference outline) {
        while (outline != null) {
            PdfDictionary outlineR = (PdfDictionary)PdfReader.getPdfObject(outline);
            PRIndirectReference first = (PRIndirectReference)outlineR.get(PdfName.FIRST);
            if (first != null) {
                outlineTravel(first);
            }
            PdfReader.killIndirect(outlineR.get(PdfName.DEST));
            PdfReader.killIndirect(outlineR.get(PdfName.A));
            PdfReader.killIndirect(outline);
            outline = (PRIndirectReference)outlineR.get(PdfName.NEXT);
        }
    }

    void deleteOutlines() {
        PdfDictionary catalog = reader.getCatalog();
        PRIndirectReference outlines = (PRIndirectReference)catalog.get(PdfName.OUTLINES);
        if (outlines == null)
            return;
        outlineTravel(outlines);
        PdfReader.killIndirect(outlines);
        catalog.remove(PdfName.OUTLINES);
        markUsed(catalog);
    }
    
    void setJavaScript() throws IOException {
        ArrayList djs = pdf.getDocumentJavaScript();
        if (djs.size() == 0)
            return;
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary names = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.NAMES), catalog);
        if (names == null) {
            names = new PdfDictionary();
            catalog.put(PdfName.NAMES, names);
            markUsed(catalog);
        }
        markUsed(names);
        String s = String.valueOf(djs.size() - 1);
        int n = s.length();
        String pad = "000000000000000";
        HashMap maptree = new HashMap();
        for (int k = 0; k < djs.size(); ++k) {
            s = String.valueOf(k);
            s = pad.substring(0, n - s.length()) + s;
            maptree.put(s, djs.get(k));
        }
        PdfDictionary tree = PdfNameTree.writeTree(maptree, this);
        names.put(PdfName.JAVASCRIPT, addToBody(tree).getIndirectReference());
    }
        
    void setOutlines() throws IOException {
        if (newBookmarks == null)
            return;
        deleteOutlines();
        if (newBookmarks.size() == 0)
            return;
        namedAsNames = (reader.getCatalog().get(PdfName.DESTS) != null);
        PdfDictionary top = new PdfDictionary();
        PdfIndirectReference topRef = getPdfIndirectReference();
        Object kids[] = SimpleBookmark.iterateOutlines(this, topRef, newBookmarks, namedAsNames);
        top.put(PdfName.FIRST, (PdfIndirectReference)kids[0]);
        top.put(PdfName.LAST, (PdfIndirectReference)kids[1]);
        top.put(PdfName.COUNT, new PdfNumber(((Integer)kids[2]).intValue()));
        addToBody(top, topRef);
        reader.getCatalog().put(PdfName.OUTLINES, topRef);
        markUsed(reader.getCatalog());
    }
    
    void setOutlines(List outlines) {
        newBookmarks = outlines;
    }
    
    /**
     * Sets the viewer preferences.
     * @param preferences the viewer preferences
     * @see PdfWriter#setViewerPreferences(int)
     */
    public void setViewerPreferences(int preferences) {
        useVp = true;
        vp |= preferences;
    }
    
    /**
     * Set the signature flags.
     * @param f the flags. This flags are ORed with current ones
     */
    public void setSigFlags(int f) {
        sigFlags |= f;
    }
    
    /** Always throws an <code>UnsupportedOperationException</code>.
     * @param actionType ignore
     * @param action ignore
     * @throws PdfException ignore
     * @see PdfStamper#setPageAction(PdfName, PdfAction, int)
     */    
    public void setPageAction(PdfName actionType, PdfAction action) throws PdfException {
        throw new UnsupportedOperationException("Use setPageAction(PdfName actionType, PdfAction action, int page)");
    }

    /**
     * Sets the open and close page additional action.
     * @param actionType the action type. It can be <CODE>PdfWriter.PAGE_OPEN</CODE>
     * or <CODE>PdfWriter.PAGE_CLOSE</CODE>
     * @param action the action to perform
     * @param page the page where the action will be applied. The first page is 1
     * @throws PdfException if the action type is invalid
     */    
    void setPageAction(PdfName actionType, PdfAction action, int page) throws PdfException {
        if (!actionType.equals(PAGE_OPEN) && !actionType.equals(PAGE_CLOSE))
            throw new PdfException("Invalid page additional action type: " + actionType.toString());
        PdfDictionary pg = reader.getPageN(page);
        PdfDictionary aa = (PdfDictionary)PdfReader.getPdfObject(pg.get(PdfName.AA), pg);
        if (aa == null) {
            aa = new PdfDictionary();
            pg.put(PdfName.AA, aa);
            markUsed(pg);
        }
        aa.put(actionType, action);
        markUsed(aa);
    }

    /**
     * Always throws an <code>UnsupportedOperationException</code>.
     * @param seconds ignore
     */
    public void setDuration(int seconds) {
        throw new UnsupportedOperationException("Use setPageAction(PdfName actionType, PdfAction action, int page)");
    }
    
    /**
     * Always throws an <code>UnsupportedOperationException</code>.
     * @param transition ignore
     */
    public void setTransition(PdfTransition transition) {
        throw new UnsupportedOperationException("Use setPageAction(PdfName actionType, PdfAction action, int page)");
    }

    /**
     * Sets the display duration for the page (for presentations)
     * @param seconds   the number of seconds to display the page. A negative value removes the entry
     * @param page the page where the duration will be applied. The first page is 1
     */
    void setDuration(int seconds, int page) {
        PdfDictionary pg = reader.getPageN(page);
        if (seconds < 0)
            pg.remove(PdfName.DUR);
        else
            pg.put(PdfName.DUR, new PdfNumber(seconds));
        markUsed(pg);
    }
    
    /**
     * Sets the transition for the page
     * @param transition   the transition object. A <code>null</code> removes the transition
     * @param page the page where the transition will be applied. The first page is 1
     */
    void setTransition(PdfTransition transition, int page) {
        PdfDictionary pg = reader.getPageN(page);
        if (transition == null)
            pg.remove(PdfName.TRANS);
        else
            pg.put(PdfName.TRANS, transition.getTransitionDictionary());
        markUsed(pg);
    }

    protected void markUsed(PdfObject obj) {
        if (append && obj != null) {
            PRIndirectReference ref = null;
            if (obj.type() == PdfObject.INDIRECT)
                ref = (PRIndirectReference)obj;
            else
                ref = obj.getIndRef();
            if (ref != null)
                marked.put(ref.getNumber(), 1);
        }
    }
    
    protected void markUsed(int num) {
        if (append)
            marked.put(num, 1);
    }
    
    /**
     * Getter for property append.
     * @return Value of property append.
     */
    boolean isAppend() {
        return append;
    }
        
    static class PageStamp {
        
        int pageNumber;
        StampContent under;
        StampContent over;
        PageResources pageResources;
        
        PageStamp(PdfStamperImp stamper, PdfReader reader, int pageNumber) {
            this.pageNumber = pageNumber;
            pageResources = new PageResources();
            PdfDictionary dic = reader.getPageN(pageNumber);
            PdfDictionary resources = (PdfDictionary)PdfReader.getPdfObject(dic.get(PdfName.RESOURCES));
            pageResources.setOriginalResources(resources, stamper.namePtr);
        }
    }
}