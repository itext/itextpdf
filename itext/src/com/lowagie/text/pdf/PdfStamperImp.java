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
import java.util.Iterator;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.ExceptionConverter;

class PdfStamperImp extends PdfWriter {
    RandomAccessFileOrArray file;
    PdfReader reader;
    int myXref[];
    /** Integer(page number) -> PageStamp */
    HashMap pagesToContent = new HashMap();
    boolean closed = false;
    /** Holds value of property rotateContents. */
    private boolean rotateContents = true;
    protected AcroFields acroFields;
    protected boolean flat = false;
    
    /** Creates new PdfStamperImp.
     * @param reader the read PDF
     * @param os the output destination
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @throws DocumentException on error
     */
    PdfStamperImp(PdfReader reader, OutputStream os, char pdfVersion) throws DocumentException, IOException {
        super(new PdfDocument(), os);
        if (reader.isTampered())
            throw new DocumentException("The original document was reused. Read it again from file.");
        reader.setTampered(true);
        this.reader = reader;
        file = reader.getSafeFile();
        if (pdfVersion == 0)
            super.setPdfVersion(reader.getPdfVersion());
        super.open();
    }
    
    void close(HashMap moreInfo) throws DocumentException, IOException {
        if (closed)
            return;
        if (flat)
            flatFields();
        closed = true;
        myXref = new int[reader.xrefObj.length];
        addSharedObjectsToBody();
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
            for (int k = 1; k < xb.length; ++k) {
                if (xb[k] != null && skip != k) {
                    addToBody(xb[k], getNewObjectNumber(reader, k, 0));
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
            PdfIndirectObject encryptionObject = body.add(crypto.getEncryptionDictionary());
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
        if (!newInfo.getKeys().isEmpty())
            info = addToBody(newInfo).getIndirectReference();
        // write the cross-reference table of the body
        body.writeCrossReferenceTable(os);
        PdfTrailer trailer = new PdfTrailer(body.size(),
        body.offset(),
        root,
        info,
        encryption,
        fileID);
        trailer.toPdf(this, os);
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
            ByteBuffer out = new ByteBuffer();
            if (ps.under != null) {
                out.append(PdfContents.SAVESTATE);
                applyRotation(pageNumber.intValue(), out);
                out.append(ps.under.getInternalBuffer());
                out.append(PdfContents.RESTORESTATE);
            }
            if (ps.over != null)
                out.append(PdfContents.SAVESTATE);
            out.append(reader.getPageContent(pageNumber.intValue(), file));
            if (ps.over != null) {
                out.append(' ');
                out.append(PdfContents.RESTORESTATE);
                out.append(PdfContents.SAVESTATE);
                applyRotation(pageNumber.intValue(), out);
                out.append(ps.over.getInternalBuffer());
                out.append(PdfContents.RESTORESTATE);
            }
            reader.setPageContent(pageNumber.intValue(), out.toByteArray());
            alterResources(ps);
        }
    }
    
    void alterResources(PageStamp ps) {
        PdfDictionary dic = reader.getPageN(ps.pageNumber);
        dic.put(PdfName.RESOURCES, ps.pageResources.getResources());
    }
    
    protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
        if (currentPdfReaderInstance == null) {
            if (myXref[number] == 0) {
                myXref[number] = getIndirectReferenceNumber();
            }
            return myXref[number];
        }
        else
            return currentPdfReaderInstance.getNewObjectNumber(number, generation);
    }
    
    RandomAccessFileOrArray getReaderFile(PdfReader reader) {
        if (currentPdfReaderInstance == null)
            return file;
        else
            return currentPdfReaderInstance.getReaderFile();
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
    
    void flatFields() {
        getAcroFields();
        HashMap fields = acroFields.getFields();
        for (Iterator i = fields.values().iterator(); i.hasNext();) {
            AcroFields.Item item = (AcroFields.Item)i.next();
            for (int k = 0; k < item.merged.size(); ++k) {
                PdfDictionary merged = (PdfDictionary)item.merged.get(k);
                PdfNumber ff = (PdfNumber)PdfReader.getPdfObject(merged.get(PdfName.F));
                int flags = 0;
                if (ff != null)
                    flags = ff.intValue();
                if ((flags & PdfFormField.FLAGS_PRINT) == 0 || (flags & PdfFormField.FLAGS_HIDDEN) != 0)
                    continue;
                PdfDictionary appDic = (PdfDictionary)PdfReader.getPdfObject(merged.get(PdfName.AP));
                if (appDic == null)
                    continue;
                PdfObject obj = appDic.get(PdfName.N);
                PdfAppearance app = null;
                PdfObject objReal = PdfReader.getPdfObject(obj);
                if (obj instanceof PdfIndirectReference && obj.type() != PdfObject.INDIRECT)
                    app = new PdfAppearance((PdfIndirectReference)obj);
                else if (objReal instanceof PdfStream) {
                    ((PdfDictionary)objReal).put(PdfName.SUBTYPE, PdfName.FORM);
                    app = new PdfAppearance((PdfIndirectReference)obj);
                }
                else {
                    if (objReal.type() == PdfObject.DICTIONARY) {
                        PdfName as = (PdfName)PdfReader.getPdfObject(merged.get(PdfName.AS));
                        if (as != null) {
                            PdfIndirectReference iref = (PdfIndirectReference)((PdfDictionary)objReal).get(as);
                            if (iref != null) {
                                app = new PdfAppearance(iref);
                                if (iref.type() == PdfObject.INDIRECT) {
                                    objReal = PdfReader.getPdfObject(iref);
                                    ((PdfDictionary)objReal).put(PdfName.SUBTYPE, PdfName.FORM);
                                }
                            }
                        }
                    }
                }
                if (app == null)
                    continue;
                Rectangle box = PdfReader.getNormalizedRectangle((PdfArray)PdfReader.getPdfObject(merged.get(PdfName.RECT)));
                int page = ((Integer)item.page.get(k)).intValue();
                PdfContentByte cb = getOverContent(page);
                cb.setLiteral("Q ");
                cb.addTemplate(app, box.left(), box.bottom());
                cb.setLiteral("q ");
            }
        }
        for (int page = 1; page <= reader.getNumberOfPages(); ++page) {
            PdfDictionary pageDic = reader.getPageN(page);
            PdfArray annots = (PdfArray)PdfReader.getPdfObject(pageDic.get(PdfName.ANNOTS));
            if (annots == null)
                continue;
            ArrayList ar = annots.getArrayList();
            for (int idx = 0; idx < ar.size(); ++idx) {
                PdfDictionary annot = (PdfDictionary)PdfReader.getPdfObject((PdfObject)ar.get(idx));
                if (PdfName.WIDGET.equals(annot.get(PdfName.SUBTYPE))) {
                    ar.remove(idx);
                    --idx;
                }
            }
            if (ar.size() == 0) {
                PdfObject obj = pageDic.get(PdfName.ANNOTS);
                if (obj.type() == PdfObject.INDIRECT)
                    reader.xrefObj[((PRIndirectReference)obj).getNumber()] = null;
                pageDic.remove(PdfName.ANNOTS);
            }
        }
        eliminateAcroformObjects();
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
        PdfReader.killIndirect(acro);
        reader.getCatalog().remove(PdfName.ACROFORM);
    }
    
    void sweepKids(PdfObject obj) {
        PdfDictionary dic = (PdfDictionary)PdfReader.killIndirect(obj);        
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

    void addAnnotation(PdfAnnotation annot, int page) {
        try {
            if (annot.isForm())
                throw new RuntimeException("Form fields not yet supported.");
            PdfRectangle rect = (PdfRectangle)annot.get(PdfName.RECT);
            int rotation = reader.getPageRotation(page);
            Rectangle pageSize = reader.getPageSizeWithRotation(page);
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
            PdfDictionary dic = reader.getPageN(page);
            PdfArray annots = (PdfArray)reader.getPdfObject(dic.get(PdfName.ANNOTS));
            if (annots == null) {
                annots = new PdfArray();
                dic.put(PdfName.ANNOTS, annots);
            }
            PdfIndirectReference ref = addToBody(annot).getIndirectReference();
            annots.add(ref);
            annot.put(PdfName.RECT, rect);
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    class PageStamp {
        
        int pageNumber;
        StampContent under;
        StampContent over;
        PageResources pageResources;
        
        PageStamp(PdfStamperImp stamper, PdfReader reader, int pageNumber) {
            this.pageNumber = pageNumber;
            pageResources = new PageResources();
            PdfDictionary dic = reader.getPageN(pageNumber);
            PdfDictionary resources = (PdfDictionary)PdfReader.getPdfObject(dic.get(PdfName.RESOURCES));
            pageResources.setOriginalResources(resources, reader);
        }
    }
}