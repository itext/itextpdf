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
 * This module by Mark Thompson. Copyright (C) 2002 Mark Thompson
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
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;

/**
 * Make copies of PDF documents. Documents can be edited after reading and
 * before writing them out.
 * @author Mark Thompson
 */

public class PdfCopy extends PdfWriter {
    /**
     * This class holds information about indirect references, since they are
     * renumbered by iText.
     */
    static class IndirectReferences {
        PdfIndirectReference theRef;
        boolean hasCopied;
        IndirectReferences(PdfIndirectReference ref) {
            theRef = ref;
            hasCopied = false;
        }
        void setCopied() { hasCopied = true; }
        boolean getCopied() { return hasCopied; }
        PdfIndirectReference getRef() { return theRef; }
    };
    protected HashMap indirects;
    protected HashMap indirectMap;
    protected int currentObjectNum = 1;
    protected PdfReader reader;
    protected PdfIndirectReference acroForm;
    protected PdfIndirectReference topPageParent;
    protected ArrayList pageNumbersToRefs = new ArrayList();
    protected List newBookmarks;
    
    /**
     * A key to allow us to hash indirect references
     */
    protected static class RefKey {
        int num;
        int gen;
        RefKey(int num, int gen) {
            this.num = num;
            this.gen = gen;
        }
        RefKey(PdfIndirectReference ref) {
            num = ref.getNumber();
            gen = ref.getGeneration();
        }
        RefKey(PRIndirectReference ref) {
            num = ref.getNumber();
            gen = ref.getGeneration();
        }
        public int hashCode() {
            return (gen<<16)+num;
        }
        public boolean equals(Object o) {
            if (!(o instanceof RefKey)) return false;
            RefKey other = (RefKey)o;
            return this.gen == other.gen && this.num == other.num;
        }
        public String toString() {
            return "" + num + " " + gen;
        }
    }
    
    /**
     * Constructor
     * @param document
     * @param os outputstream
     */
    public PdfCopy(Document document, OutputStream os) throws DocumentException {
        super(new PdfDocument(), os);
        document.addDocListener(pdf);
        pdf.addWriter(this);
        indirectMap = new HashMap();
    }
    public void open() {
        super.open();
        topPageParent = getPdfIndirectReference();
        root.setLinearMode(topPageParent);
    }

    /**
     * Grabs a page from the input document
     * @param reader the reader of the document
     * @param pageNumber which page to get
     * @return the page
     */
    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
        if (currentPdfReaderInstance != null) {
            if (currentPdfReaderInstance.getReader() != reader) {
                try {
                    currentPdfReaderInstance.getReader().close();
                    currentPdfReaderInstance.getReaderFile().close();
                }
                catch (IOException ioe) {
                    // empty on purpose
                }
                currentPdfReaderInstance = reader.getPdfReaderInstance(this);
            }
        }
        else {
            currentPdfReaderInstance = reader.getPdfReaderInstance(this);
        }
        return currentPdfReaderInstance.getImportedPage(pageNumber);            
    }
    
    
    /**
     * Translate a PRIndirectReference to a PdfIndirectReference
     * In addition, translates the object numbers, and copies the
     * referenced object to the output file.
     * NB: PRIndirectReferences (and PRIndirectObjects) really need to know what
     * file they came from, because each file has its own namespace. The translation
     * we do from their namespace to ours is *at best* heuristic, and guaranteed to
     * fail under some circumstances.
     */
    protected PdfIndirectReference copyIndirect(PRIndirectReference in) throws IOException, BadPdfFormatException {
        PdfIndirectReference theRef;
        RefKey key = new RefKey(in);
        IndirectReferences iRef = (IndirectReferences)indirects.get(key);
        if (iRef != null) {
            theRef = iRef.getRef();
            if (iRef.getCopied()) {
                //	        System.out.println(">>> Value is " + theRef.toString());
                return theRef;
            }
            //	    System.out.println(">>> Fill in " + theRef.toString());
        }
        else {
            theRef = body.getPdfIndirectReference();
            iRef = new IndirectReferences(theRef);
            indirects.put(key, iRef);
        }
        iRef.setCopied();
        PdfObject obj = copyObject(PdfReader.getPdfObjectRelease(in));
        addToBody(obj, theRef);
        return theRef;
    }
    
    /**
     * Translate a PRDictionary to a PdfDictionary. Also translate all of the
     * objects contained in it.
     */
    protected PdfDictionary copyDictionary(PdfDictionary in)
    throws IOException, BadPdfFormatException {
        PdfDictionary out = new PdfDictionary();
        PdfName type = (PdfName)in.get(PdfName.TYPE);
        
        for (Iterator it = in.getKeys().iterator(); it.hasNext();) {
            PdfName key = (PdfName)it.next();
            PdfObject value = in.get(key);
            //	    System.out.println("Copy " + key);
            if (type != null && PdfName.PAGE.equals(type)) {
                if (key.equals(PdfName.PARENT))
                    out.put(PdfName.PARENT, topPageParent);
                else if (!key.equals(PdfName.B))
                    out.put(key, copyObject(value));
            }
            else
                out.put(key, copyObject(value));
        }
        return out;
    }
    
    /**
     * Translate a PRStream to a PdfStream. The data part copies itself.
     */
    protected PdfStream copyStream(PRStream in) throws IOException, BadPdfFormatException {
        PRStream out = new PRStream(in, null);
        
        for (Iterator it = in.getKeys().iterator(); it.hasNext();) {
            PdfName key = (PdfName) it.next();
            PdfObject value = in.get(key);
            out.put(key, copyObject(value));
        }
        
        return out;
    }
    
    
    /**
     * Translate a PRArray to a PdfArray. Also translate all of the objects contained
     * in it
     */
    protected PdfArray copyArray(PdfArray in) throws IOException, BadPdfFormatException {
        PdfArray out = new PdfArray();
        
        for (Iterator i = in.getArrayList().iterator(); i.hasNext();) {
            PdfObject value = (PdfObject)i.next();
            out.add(copyObject(value));
        }
        return out;
    }
    
    /**
     * Translate a PR-object to a Pdf-object
     */
    protected PdfObject copyObject(PdfObject in) throws IOException,BadPdfFormatException {
        if (in == null)
            return PdfNull.PDFNULL;
        switch (in.type) {
            case PdfObject.DICTIONARY:
                //	        System.out.println("Dictionary: " + in.toString());
                return copyDictionary((PdfDictionary)in);
            case PdfObject.INDIRECT:
                return copyIndirect((PRIndirectReference)in);
            case PdfObject.ARRAY:
                return copyArray((PdfArray)in);
            case PdfObject.NUMBER:
            case PdfObject.NAME:
            case PdfObject.STRING:
            case PdfObject.NULL:
            case PdfObject.BOOLEAN:
                return in;
            case PdfObject.STREAM:
                return copyStream((PRStream)in);
                //                return in;
            default:
                if (in.type < 0) {
                    String lit = ((PdfLiteral)in).toString();
                    if (lit.equals("true") || lit.equals("false")) {
                        return new PdfBoolean(lit);
                    }
                    return new PdfLiteral(lit);
                }
                System.out.println("CANNOT COPY type " + in.type);
                return null;
        }
    }
    
    /**
     * convenience method. Given an importedpage, set our "globals"
     */
    protected int setFromIPage(PdfImportedPage iPage) {
        int pageNum = iPage.getPageNumber();
        PdfReaderInstance inst = currentPdfReaderInstance = iPage.getPdfReaderInstance();
        reader = inst.getReader();
        setFromReader(reader);
        return pageNum;
    }
    
    /**
     * convenience method. Given a reader, set our "globals"
     */
    protected void setFromReader(PdfReader reader) {
        this.reader = reader;
        indirects = (HashMap)indirectMap.get(reader);
        if (indirects == null) {
            indirects = new HashMap();
            indirectMap.put(reader,indirects);
            PdfDictionary catalog = reader.getCatalog();
            PRIndirectReference ref = (PRIndirectReference)catalog.get(PdfName.PAGES);
            indirects.put(new RefKey(ref), new IndirectReferences(topPageParent));
            ref = null;
            PdfObject o = catalog.get(PdfName.ACROFORM);
            if (o == null || o.type() != PdfObject.INDIRECT)
                return;
            ref = (PRIndirectReference)o;
            if (acroForm == null) acroForm = body.getPdfIndirectReference();
            indirects.put(new RefKey(ref), new IndirectReferences(acroForm));
        }
    }
    /**
     * Add an imported page to our output
     * @param iPage an imported page
     * @throws IOException, BadPdfFormatException
     */
    public void addPage(PdfImportedPage iPage) throws IOException, BadPdfFormatException {
        int pageNum = setFromIPage(iPage);
        
        PdfDictionary thePage = reader.getPageN(pageNum);
        PRIndirectReference origRef = reader.getPageOrigRef(pageNum);
        reader.releasePage(pageNum);
        RefKey key = new RefKey(origRef);
        PdfIndirectReference pageRef;
        IndirectReferences iRef = (IndirectReferences)indirects.get(key);
        // if we already have an iref for the page (we got here by another link)
        if (iRef != null) {
            pageRef = iRef.getRef();
        }
        else {
            pageRef = body.getPdfIndirectReference();
            iRef = new IndirectReferences(pageRef);
            indirects.put(key, iRef);
        }
        pageReferences.add(pageRef);
        ++currentPageNumber;
        if (! iRef.getCopied()) {
            iRef.setCopied();
            PdfDictionary newPage = copyDictionary(thePage);
            newPage.put(PdfName.PARENT, topPageParent);
            addToBody(newPage, pageRef);
        }
        root.addPage(pageRef);
        pageNumbersToRefs.add(pageRef);
    }
    
    public PdfIndirectReference getPageReference(int page) {
        if (page < 0 || page > pageNumbersToRefs.size())
            throw new IllegalArgumentException("Invalid page number " + page);
        return (PdfIndirectReference)pageNumbersToRefs.get(page - 1);
    }

    /**
     * Copy the acroform for an input document. Note that you can only have one,
     * we make no effort to merge them.
     * @param reader The reader of the input file that is being copied
     * @throws IOException, BadPdfFormatException
     */
    public void copyAcroForm(PdfReader reader) throws IOException, BadPdfFormatException {
        setFromReader(reader);
        
        PdfDictionary catalog = reader.getCatalog();
        PRIndirectReference hisRef = null;
        PdfObject o = catalog.get(PdfName.ACROFORM);
        if (o != null && o.type() == PdfObject.INDIRECT)
            hisRef = (PRIndirectReference)o;
        if (hisRef == null) return; // bugfix by John Englar
        RefKey key = new RefKey(hisRef);
        PdfIndirectReference myRef;
        IndirectReferences iRef = (IndirectReferences)indirects.get(key);
        if (iRef != null) {
            acroForm = myRef = iRef.getRef();
        }
        else {
            acroForm = myRef = body.getPdfIndirectReference();
            iRef = new IndirectReferences(myRef);
            indirects.put(key, iRef);
        }
        if (! iRef.getCopied()) {
            iRef.setCopied();
            PdfDictionary theForm = copyDictionary((PdfDictionary)PdfReader.getPdfObject(hisRef));
            addToBody(theForm, myRef);
        }
    }
    
    /*
     * the getCatalog method is part of PdfWriter.
     * we wrap this so that we can extend it
     */
    protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
        try {
            PdfDictionary theCat = ((PdfDocument)document).getCatalog(rootObj);
            if (acroForm != null) theCat.put(PdfName.ACROFORM, acroForm);
            if (newBookmarks == null || newBookmarks.isEmpty())
                return theCat;
            PdfDictionary top = new PdfDictionary();
            PdfIndirectReference topRef = getPdfIndirectReference();
            Object kids[] = SimpleBookmark.iterateOutlines(this, topRef, newBookmarks, false);
            top.put(PdfName.FIRST, (PdfIndirectReference)kids[0]);
            top.put(PdfName.LAST, (PdfIndirectReference)kids[1]);
            top.put(PdfName.COUNT, new PdfNumber(((Integer)kids[2]).intValue()));
            addToBody(top, topRef);
            theCat.put(PdfName.OUTLINES, topRef);
            return theCat;
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }
    
    /**
     * Sets the bookmarks. The list structure is defined in
     * <CODE>SimpleBookmark#</CODE>.
     * @param outlines the bookmarks or <CODE>null</CODE> to remove any
     */    
    public void setOutlines(List outlines) {
        newBookmarks = outlines;
    }

    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
     * <CODE>Elements</CODE> will be added.
     * <P>
     * The pages-tree is built and written to the outputstream.
     * A Catalog is constructed, as well as an Info-object,
     * the referencetable is composed and everything is written
     * to the outputstream embedded in a Trailer.
     */
    
    public synchronized void close() {
        if (open) {
            PdfReaderInstance ri = currentPdfReaderInstance;
            pdf.close();
            super.close();
            if (ri != null) {
                try {
                    ri.getReader().close();
                    ri.getReaderFile().close();
                }
                catch (IOException ioe) {
                    // empty on purpose
                }
            }
        }
    }
    PdfIndirectReference add(PdfImage pdfImage, PdfIndirectReference fixedRef) throws PdfException  { return null; }
    public PdfIndirectReference add(PdfOutline outline) { return null; }
    public void addAnnotation(PdfAnnotation annot) {  }
    PdfIndirectReference add(PdfPage page, PdfContents contents) throws PdfException { return null; }

    public void freeReader(PdfReader reader) throws IOException {
        indirectMap.remove(reader);
        if (currentPdfReaderInstance != null) {
            if (currentPdfReaderInstance.getReader() == reader) {
                try {
                    currentPdfReaderInstance.getReader().close();
                    currentPdfReaderInstance.getReaderFile().close();
                }
                catch (IOException ioe) {
                    // empty on purpose
                }
                currentPdfReaderInstance = null;
            }
        }
    }
}