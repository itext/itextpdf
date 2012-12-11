/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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
        void setNotCopied() { hasCopied = false; }
        boolean getCopied() { return hasCopied; }
        PdfIndirectReference getRef() { return theRef; }
    }
    protected HashMap<RefKey, IndirectReferences> indirects;
    protected HashMap<PdfReader, HashMap<RefKey, IndirectReferences>> indirectMap;
    protected HashMap<PdfObject, PdfObject> parentObjects;
    protected HashSet<PdfObject> disableIndirects;
    protected PdfReader reader;
    protected PdfIndirectReference acroForm;
    protected int[] namePtr = {0};
    /** Holds value of property rotateContents. */
    private boolean rotateContents = true;
    protected PdfArray fieldArray;
    protected HashSet<PdfTemplate> fieldTemplates;
    private PdfStructTreeController structTreeController = null;
    private int currentStructArrayNumber = 0;

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
        @Override
        public int hashCode() {
            return (gen<<16)+num;
        }
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof RefKey)) return false;
            RefKey other = (RefKey)o;
            return this.gen == other.gen && this.num == other.num;
        }
        @Override
        public String toString() {
            return Integer.toString(num) + ' ' + gen;
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
        indirectMap = new HashMap<PdfReader, HashMap<RefKey, IndirectReferences>>();
        parentObjects = new HashMap<PdfObject, PdfObject>();
        disableIndirects = new HashSet<PdfObject>();
    }

    /**
     * Setting page events isn't possible with Pdf(Smart)Copy.
     * Use the PageStamp class if you want to add content to copied pages.
	 * @see com.itextpdf.text.pdf.PdfWriter#setPageEvent(com.itextpdf.text.pdf.PdfPageEvent)
	 */
	@Override
	public void setPageEvent(PdfPageEvent event) {
		throw new UnsupportedOperationException();
	}

	/** Getter for property rotateContents.
     * @return Value of property rotateContents.
     *
     */
    public boolean isRotateContents() {
        return this.rotateContents;
    }

    /** Setter for property rotateContents.
     * @param rotateContents New value of property rotateContents.
     *
     */
    public void setRotateContents(boolean rotateContents) {
        this.rotateContents = rotateContents;
    }

    /**
     * Grabs a page from the input document
     * @param reader the reader of the document
     * @param pageNumber which page to get
     * @return the page
     */
    @Override
    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
        if (structTreeController != null)
            structTreeController.reader = null;
        return getImportedPageImpl(reader, pageNumber);
    }

    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber, boolean keepTaggedPdfStructure) throws BadPdfFormatException {
        if (!keepTaggedPdfStructure)
            return getImportedPage(reader, pageNumber);
        else {
            if (structTreeController != null) {
                if (reader != structTreeController.reader)
                    structTreeController.setReader(reader);
            } else {
                structTreeController = new PdfStructTreeController(reader, this);
            }
            disableIndirects.clear();
            parentObjects.clear();
            return getImportedPageImpl(reader, pageNumber);
        }
    }

    protected PdfImportedPage getImportedPageImpl(PdfReader reader, int pageNumber) {
        if (currentPdfReaderInstance != null) {
            if (currentPdfReaderInstance.getReader() != reader) {

// TODO: Removed - the user should be responsible for closing all PdfReaders.  But, this could cause a lot of memory leaks in code out there that hasn't been properly closing things - maybe add a finalizer to PdfReader that calls PdfReader#close() ??            	
//             	  try {
//                    currentPdfReaderInstance.getReader().close();
//                    currentPdfReaderInstance.getReaderFile().close();
//                }
//                catch (IOException ioe) {
//                    // empty on purpose
//                }
                currentPdfReaderInstance = super.getPdfReaderInstance(reader);
            }
        }
        else {
            currentPdfReaderInstance = super.getPdfReaderInstance(reader);
        }
        //currentPdfReaderInstance.setOutputToPdf(false);
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
    protected PdfIndirectReference copyIndirect(PRIndirectReference in, boolean keepStructure, boolean directRootKids) throws IOException, BadPdfFormatException {
        PdfIndirectReference theRef;
        RefKey key = new RefKey(in);
        IndirectReferences iRef = indirects.get(key);
        PdfObject obj = PdfReader.getPdfObjectRelease(in);
        if ((keepStructure) && (directRootKids))
            if (obj instanceof PdfDictionary) {
                PdfDictionary dict = (PdfDictionary) obj;
                if (dict.contains(PdfName.PG))
                    return null;
            }

        if (iRef != null) {
            theRef = iRef.getRef();
            if (iRef.getCopied()) {
                return theRef;
            }
        }
        else {
            theRef = body.getPdfIndirectReference();
            iRef = new IndirectReferences(theRef);
            indirects.put(key, iRef);
        }

        if (obj != null && obj.isDictionary()) {
            PdfObject type = PdfReader.getPdfObjectRelease(((PdfDictionary)obj).get(PdfName.TYPE));
            if (type != null && PdfName.PAGE.equals(type)) {
                return theRef;
            }
        }
        iRef.setCopied();
        parentObjects.put(obj, in);
        PdfObject res = copyObject(obj, keepStructure, directRootKids);
        if (disableIndirects.contains(obj))
            iRef.setNotCopied();
        if ((res != null) && !(res instanceof PdfNull))
        {
            addToBody(res, theRef);
            return theRef;
        }
        else {
            indirects.remove(key);
            return null;
        }

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
        return copyIndirect(in, false, false);
    }

    /**
     * Translate a PRDictionary to a PdfDictionary. Also translate all of the
     * objects contained in it.
     */
    protected PdfDictionary copyDictionary(PdfDictionary in, boolean keepStruct, boolean directRootKids)
            throws IOException, BadPdfFormatException {
        PdfDictionary out = new PdfDictionary();
        PdfObject type = PdfReader.getPdfObjectRelease(in.get(PdfName.TYPE));
        
        if (keepStruct)
        {
            if ((directRootKids) && (in.contains(PdfName.PG)))
            {
                PdfObject curr = in;
                disableIndirects.add(curr);
                while (parentObjects.containsKey(curr) && !(disableIndirects.contains(curr))) {
                    curr = parentObjects.get(curr);
                    disableIndirects.add(curr);
                }
                return null;
            }
                
            PdfName structType = in.getAsName(PdfName.S);
            structTreeController.addRole(structType);
            structTreeController.addClass(in);
        }
        
        for (Object element : in.getKeys()) {
            PdfName key = (PdfName)element;
            PdfObject value = in.get(key);
            if (structTreeController != null && structTreeController.reader != null && (key.equals(PdfName.STRUCTPARENT) || key.equals(PdfName.STRUCTPARENTS))) {
                out.put(key,new PdfNumber(currentStructArrayNumber));
                structTreeController.copyStructTreeForPage((PdfNumber)value, currentStructArrayNumber++);
                continue;
            }
            if (type != null && PdfName.PAGE.equals(type)) {
                if (!key.equals(PdfName.B) && !key.equals(PdfName.PARENT)) {
                    parentObjects.put(value, in);
                    PdfObject res = copyObject(value, keepStruct, directRootKids);
                    if ((res != null) && !(res instanceof PdfNull))
                        out.put(key, res);
                }
            }
            else {
                PdfObject res = copyObject(value, keepStruct, directRootKids);
                if ((res != null) && !(res instanceof PdfNull))
                    out.put(key, res);
            }
        }

        return out;
    }

    /**
     * Translate a PRDictionary to a PdfDictionary. Also translate all of the
     * objects contained in it.
     */
    protected PdfDictionary copyDictionary(PdfDictionary in)
    throws IOException, BadPdfFormatException {
        return copyDictionary(in, false, false);
    }

    /**
     * Translate a PRStream to a PdfStream. The data part copies itself.
     */
    protected PdfStream copyStream(PRStream in) throws IOException, BadPdfFormatException {
        PRStream out = new PRStream(in, null);

        for (Object element : in.getKeys()) {
            PdfName key = (PdfName) element;
            PdfObject value = in.get(key);
            parentObjects.put(value, in);
            PdfObject res = copyObject(value);
            if ((res != null) && !(res instanceof PdfNull))
                out.put(key, res);
        }

        return out;
    }

    /**
     * Translate a PRArray to a PdfArray. Also translate all of the objects contained
     * in it
     */
    protected PdfArray copyArray(PdfArray in, boolean keepStruct, boolean directRootKids) throws IOException, BadPdfFormatException {
        PdfArray out = new PdfArray();

        for (Iterator<PdfObject> i = in.listIterator(); i.hasNext();) {
            PdfObject value = i.next();
            parentObjects.put(value, in);
            PdfObject res = copyObject(value, keepStruct, directRootKids);
            if ((res != null) && !(res instanceof PdfNull))
                out.add(res);
        }
        return out;
    }

    /**
     * Translate a PRArray to a PdfArray. Also translate all of the objects contained
     * in it
     */
    protected PdfArray copyArray(PdfArray in) throws IOException, BadPdfFormatException {
        return copyArray(in, false, false);
    }

    /**
     * Translate a PR-object to a Pdf-object
     */
    protected PdfObject copyObject(PdfObject in, boolean keepStruct, boolean directRootKidds) throws IOException,BadPdfFormatException {
        if (in == null)
            return PdfNull.PDFNULL;
        switch (in.type) {
            case PdfObject.DICTIONARY:
                return copyDictionary((PdfDictionary)in, keepStruct, directRootKidds);
            case PdfObject.INDIRECT:
                if (!keepStruct && !directRootKidds)
                    // fix for PdfSmartCopy
                    return copyIndirect((PRIndirectReference)in);
                else
                    return copyIndirect((PRIndirectReference)in, keepStruct, directRootKidds);
            case PdfObject.ARRAY:
                return copyArray((PdfArray)in, keepStruct, directRootKidds);
            case PdfObject.NUMBER:
            case PdfObject.NAME:
            case PdfObject.STRING:
            case PdfObject.NULL:
            case PdfObject.BOOLEAN:
            case 0:
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
     * Translate a PR-object to a Pdf-object
     */
    protected PdfObject copyObject(PdfObject in) throws IOException,BadPdfFormatException {
        return copyObject(in, false, false);
    }

    /**
     * convenience method. Given an imported page, set our "globals"
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
        indirects = indirectMap.get(reader);
        if (indirects == null) {
            indirects = new HashMap<RefKey, IndirectReferences>();
            indirectMap.put(reader,indirects);
            PdfDictionary catalog = reader.getCatalog();
            PRIndirectReference ref = null;
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
        IndirectReferences iRef = indirects.get(key);
        if (iRef != null && !iRef.getCopied()) {
            pageReferences.add(iRef.getRef());
            iRef.setCopied();
        }
        pageRef = getCurrentPage();
        if (iRef == null) {
            iRef = new IndirectReferences(pageRef);
            indirects.put(key, iRef);
        }
        iRef.setCopied();
        PdfDictionary newPage = copyDictionary(thePage);
        root.addPage(newPage);
        iPage.setCopied();
        ++currentPageNumber;
    }

    /**
     * Adds a blank page.
     * @param	rect The page dimension
     * @param	rotation The rotation angle in degrees
     * @throws DocumentException 
     * @since	2.1.5
     */
    public void addPage(Rectangle rect, int rotation) throws DocumentException {
    	PdfRectangle mediabox = new PdfRectangle(rect, rotation);
    	PageResources resources = new PageResources();
    	PdfPage page = new PdfPage(mediabox, new HashMap<String, PdfRectangle>(), resources.getResources(), 0);
    	page.put(PdfName.TABS, getTabs());
    	root.addPage(page);
    	++currentPageNumber;
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
        IndirectReferences iRef = indirects.get(key);
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
    @Override
    protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
        try {
            PdfDictionary theCat = pdf.getCatalog(rootObj);
            buildStructTreeRootForTagged(theCat);
            if (fieldArray == null) {
                if (acroForm != null) theCat.put(PdfName.ACROFORM, acroForm);
            }
            else
                addFieldResources(theCat);
            return theCat;
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    private void addFieldResources(PdfDictionary catalog) throws IOException {
        if (fieldArray == null)
            return;
        PdfDictionary acroForm = new PdfDictionary();
        catalog.put(PdfName.ACROFORM, acroForm);
        acroForm.put(PdfName.FIELDS, fieldArray);
        acroForm.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
        if (fieldTemplates.isEmpty())
            return;
        PdfDictionary dr = new PdfDictionary();
        acroForm.put(PdfName.DR, dr);
        for (PdfTemplate template: fieldTemplates) {
            PdfFormField.mergeResources(dr, (PdfDictionary)template.getResources());
        }
        // if (dr.get(PdfName.ENCODING) == null) dr.put(PdfName.ENCODING, PdfName.WIN_ANSI_ENCODING);
        PdfDictionary fonts = dr.getAsDict(PdfName.FONT);
        if (fonts == null) {
            fonts = new PdfDictionary();
            dr.put(PdfName.FONT, fonts);
        }
        if (!fonts.contains(PdfName.HELV)) {
            PdfDictionary dic = new PdfDictionary(PdfName.FONT);
            dic.put(PdfName.BASEFONT, PdfName.HELVETICA);
            dic.put(PdfName.ENCODING, PdfName.WIN_ANSI_ENCODING);
            dic.put(PdfName.NAME, PdfName.HELV);
            dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
            fonts.put(PdfName.HELV, addToBody(dic).getIndirectReference());
        }
        if (!fonts.contains(PdfName.ZADB)) {
            PdfDictionary dic = new PdfDictionary(PdfName.FONT);
            dic.put(PdfName.BASEFONT, PdfName.ZAPFDINGBATS);
            dic.put(PdfName.NAME, PdfName.ZADB);
            dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
            fonts.put(PdfName.ZADB, addToBody(dic).getIndirectReference());
        }
    }

    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
     * <CODE>Elements</CODE> will be added.
     * <P>
     * The pages-tree is built and written to the outputstream.
     * A Catalog is constructed, as well as an Info-object,
     * the reference table is composed and everything is written
     * to the outputstream embedded in a Trailer.
     */

    @Override
    public void close() {
        if (open) {
            PdfReaderInstance ri = currentPdfReaderInstance;
            pdf.close();
            super.close();
// Users are responsible for closing PdfReaderw            
//            if (ri != null) {
//                try {
//                    ri.getReader().close();
//                    ri.getReaderFile().close();
//                }
//                catch (IOException ioe) {
//                    // empty on purpose
//                }
//            }
        }
    }
    public PdfIndirectReference add(PdfOutline outline) { return null; }
    @Override
    public void addAnnotation(PdfAnnotation annot) {  }
    @Override
    PdfIndirectReference add(PdfPage page, PdfContents contents) throws PdfException { return null; }

    @Override
    public void freeReader(PdfReader reader) throws IOException {
        indirectMap.remove(reader);
// TODO: Removed - the user should be responsible for closing all PdfReaders.  But, this could cause a lot of memory leaks in code out there that hasn't been properly closing things - maybe add a finalizer to PdfReader that calls PdfReader#close() ??            	
//        if (currentPdfReaderInstance != null) {
//            if (currentPdfReaderInstance.getReader() == reader) {
//                try {
//                    currentPdfReaderInstance.getReader().close();
//                    currentPdfReaderInstance.getReaderFile().close();
//                }
//                catch (IOException ioe) {
//                    // empty on purpose
//                }
                currentPdfReaderInstance = null;
//            }
//        }
        super.freeReader(reader);
    }

    /**
     * Create a page stamp. New content and annotations, including new fields, are allowed.
     * The fields added cannot have parents in another pages. This method modifies the PdfReader instance.<p>
     * The general usage to stamp something in a page is:
     * <p>
     * <pre>
     * PdfImportedPage page = copy.getImportedPage(reader, 1);
     * PdfCopy.PageStamp ps = copy.createPageStamp(page);
     * ps.addAnnotation(PdfAnnotation.createText(copy, new Rectangle(50, 180, 70, 200), "Hello", "No Thanks", true, "Comment"));
     * PdfContentByte under = ps.getUnderContent();
     * under.addImage(img);
     * PdfContentByte over = ps.getOverContent();
     * over.beginText();
     * over.setFontAndSize(bf, 18);
     * over.setTextMatrix(30, 30);
     * over.showText("total page " + totalPage);
     * over.endText();
     * ps.alterContents();
     * copy.addPage(page);
     * </pre>
     * @param iPage an imported page
     * @return the <CODE>PageStamp</CODE>
     */
    public PageStamp createPageStamp(PdfImportedPage iPage) {
        int pageNum = iPage.getPageNumber();
        PdfReader reader = iPage.getPdfReaderInstance().getReader();
        PdfDictionary pageN = reader.getPageN(pageNum);
        return new PageStamp(reader, pageN, this);
    }

    public static class PageStamp {

        PdfDictionary pageN;
        PdfCopy.StampContent under;
        PdfCopy.StampContent over;
        PageResources pageResources;
        PdfReader reader;
        PdfCopy cstp;

        PageStamp(PdfReader reader, PdfDictionary pageN, PdfCopy cstp) {
            this.pageN = pageN;
            this.reader = reader;
            this.cstp = cstp;
        }

        public PdfContentByte getUnderContent(){
            if (under == null) {
                if (pageResources == null) {
                    pageResources = new PageResources();
                    PdfDictionary resources = pageN.getAsDict(PdfName.RESOURCES);
                    pageResources.setOriginalResources(resources, cstp.namePtr);
                }
                under = new PdfCopy.StampContent(cstp, pageResources);
            }
            return under;
        }

        public PdfContentByte getOverContent(){
            if (over == null) {
                if (pageResources == null) {
                    pageResources = new PageResources();
                    PdfDictionary resources = pageN.getAsDict(PdfName.RESOURCES);
                    pageResources.setOriginalResources(resources, cstp.namePtr);
                }
                over = new PdfCopy.StampContent(cstp, pageResources);
            }
            return over;
        }

        public void alterContents() throws IOException {
            if (over == null && under == null)
                return;
            PdfArray ar = null;
            PdfObject content = PdfReader.getPdfObject(pageN.get(PdfName.CONTENTS), pageN);
            if (content == null) {
                ar = new PdfArray();
                pageN.put(PdfName.CONTENTS, ar);
            } else if (content.isArray()) {
                ar = (PdfArray)content;
            } else if (content.isStream()) {
                ar = new PdfArray();
                ar.add(pageN.get(PdfName.CONTENTS));
                pageN.put(PdfName.CONTENTS, ar);
            } else {
                ar = new PdfArray();
                pageN.put(PdfName.CONTENTS, ar);
            }
            ByteBuffer out = new ByteBuffer();
            if (under != null) {
                out.append(PdfContents.SAVESTATE);
                applyRotation(pageN, out);
                out.append(under.getInternalBuffer());
                out.append(PdfContents.RESTORESTATE);
            }
            if (over != null)
                out.append(PdfContents.SAVESTATE);
            PdfStream stream = new PdfStream(out.toByteArray());
            stream.flateCompress(cstp.getCompressionLevel());
            PdfIndirectReference ref1 = cstp.addToBody(stream).getIndirectReference();
            ar.addFirst(ref1);
            out.reset();
            if (over != null) {
                out.append(' ');
                out.append(PdfContents.RESTORESTATE);
                out.append(PdfContents.SAVESTATE);
                applyRotation(pageN, out);
                out.append(over.getInternalBuffer());
                out.append(PdfContents.RESTORESTATE);
                stream = new PdfStream(out.toByteArray());
                stream.flateCompress(cstp.getCompressionLevel());
                ar.add(cstp.addToBody(stream).getIndirectReference());
            }
            pageN.put(PdfName.RESOURCES, pageResources.getResources());
        }

        void applyRotation(PdfDictionary pageN, ByteBuffer out) {
            if (!cstp.rotateContents)
                return;
            Rectangle page = reader.getPageSizeWithRotation(pageN);
            int rotation = page.getRotation();
            switch (rotation) {
                case 90:
                    out.append(PdfContents.ROTATE90);
                    out.append(page.getTop());
                    out.append(' ').append('0').append(PdfContents.ROTATEFINAL);
                    break;
                case 180:
                    out.append(PdfContents.ROTATE180);
                    out.append(page.getRight());
                    out.append(' ');
                    out.append(page.getTop());
                    out.append(PdfContents.ROTATEFINAL);
                    break;
                case 270:
                    out.append(PdfContents.ROTATE270);
                    out.append('0').append(' ');
                    out.append(page.getRight());
                    out.append(PdfContents.ROTATEFINAL);
                    break;
            }
        }

        private void addDocumentField(PdfIndirectReference ref) {
            if (cstp.fieldArray == null)
                cstp.fieldArray = new PdfArray();
            cstp.fieldArray.add(ref);
        }

        private void expandFields(PdfFormField field, ArrayList<PdfAnnotation> allAnnots) {
            allAnnots.add(field);
            ArrayList<PdfFormField> kids = field.getKids();
            if (kids != null) {
                for (PdfFormField f : kids)
                    expandFields(f, allAnnots);
            }
        }

        public void addAnnotation(PdfAnnotation annot) {
            try {
                ArrayList<PdfAnnotation> allAnnots = new ArrayList<PdfAnnotation>();
                if (annot.isForm()) {
                    PdfFormField field = (PdfFormField)annot;
                    if (field.getParent() != null)
                        return;
                    expandFields(field, allAnnots);
                    if (cstp.fieldTemplates == null)
                        cstp.fieldTemplates = new HashSet<PdfTemplate>();
                }
                else
                    allAnnots.add(annot);
                for (int k = 0; k < allAnnots.size(); ++k) {
                    annot = allAnnots.get(k);
                    if (annot.isForm()) {
                        if (!annot.isUsed()) {
                            HashSet<PdfTemplate> templates = annot.getTemplates();
                            if (templates != null)
                                cstp.fieldTemplates.addAll(templates);
                        }
                        PdfFormField field = (PdfFormField)annot;
                        if (field.getParent() == null)
                            addDocumentField(field.getIndirectReference());
                    }
                    if (annot.isAnnotation()) {
                        PdfObject pdfobj = PdfReader.getPdfObject(pageN.get(PdfName.ANNOTS), pageN);
                        PdfArray annots = null;
                        if (pdfobj == null || !pdfobj.isArray()) {
                            annots = new PdfArray();
                            pageN.put(PdfName.ANNOTS, annots);
                        }
                        else
                            annots = (PdfArray)pdfobj;
                        annots.add(annot.getIndirectReference());
                        if (!annot.isUsed()) {
                            PdfRectangle rect = (PdfRectangle)annot.get(PdfName.RECT);
                            if (rect != null && (rect.left() != 0 || rect.right() != 0 || rect.top() != 0 || rect.bottom() != 0)) {
                                int rotation = reader.getPageRotation(pageN);
                                Rectangle pageSize = reader.getPageSizeWithRotation(pageN);
                                switch (rotation) {
                                    case 90:
                                        annot.put(PdfName.RECT, new PdfRectangle(
                                        pageSize.getTop() - rect.bottom(),
                                        rect.left(),
                                        pageSize.getTop() - rect.top(),
                                        rect.right()));
                                        break;
                                    case 180:
                                        annot.put(PdfName.RECT, new PdfRectangle(
                                        pageSize.getRight() - rect.left(),
                                        pageSize.getTop() - rect.bottom(),
                                        pageSize.getRight() - rect.right(),
                                        pageSize.getTop() - rect.top()));
                                        break;
                                    case 270:
                                        annot.put(PdfName.RECT, new PdfRectangle(
                                        rect.bottom(),
                                        pageSize.getRight() - rect.left(),
                                        rect.top(),
                                        pageSize.getRight() - rect.right()));
                                        break;
                                }
                            }
                        }
                    }
                    if (!annot.isUsed()) {
                        annot.setUsed();
                        cstp.addToBody(annot, annot.getIndirectReference());
                    }
                }
            }
            catch (IOException e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    public static class StampContent extends PdfContentByte {
        PageResources pageResources;

        /** Creates a new instance of StampContent */
        StampContent(PdfWriter writer, PageResources pageResources) {
            super(writer);
            this.pageResources = pageResources;
        }

        /**
         * Gets a duplicate of this <CODE>PdfContentByte</CODE>. All
         * the members are copied by reference but the buffer stays different.
         *
         * @return a copy of this <CODE>PdfContentByte</CODE>
         */
        @Override
        public PdfContentByte getDuplicate() {
            return new PdfCopy.StampContent(writer, pageResources);
        }

        @Override
        PageResources getPageResources() {
            return pageResources;
        }
    }
}