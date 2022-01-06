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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Make copies of PDF documents. Documents can be edited after reading and
 * before writing them out.
 * @author Mark Thompson
 */

public class PdfCopy extends PdfWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfCopy.class);

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

        @Override
        public String toString() {
            String ext = "";
            if (hasCopied) ext += " Copied";
            return getRef() + ext;
        }
    }

    protected static Counter COUNTER = CounterFactory.getCounter(PdfCopy.class);
    protected Counter getCounter() {
        return COUNTER;
    }
    protected HashMap<RefKey, IndirectReferences> indirects;
    protected HashMap<PdfReader, HashMap<RefKey, IndirectReferences>> indirectMap;
    protected HashMap<PdfObject, PdfObject> parentObjects;
    protected HashSet<PdfObject> disableIndirects;
    protected PdfReader reader;
    protected int[] namePtr = {0};
    /** Holds value of property rotateContents. */
    private boolean rotateContents = true;
    protected PdfArray fieldArray;
    protected HashSet<PdfTemplate> fieldTemplates;
    private PdfStructTreeController structTreeController = null;
    private int currentStructArrayNumber = 0;
    //remember to avoid coping
    protected PRIndirectReference structTreeRootReference;
    //to remove unused objects
    protected LinkedHashMap<RefKey, PdfIndirectObject> indirectObjects;
    //PdfIndirectObjects, that generate PdfWriter.addToBody(PdfObject) method, already saved to PdfBody
    protected ArrayList<PdfIndirectObject> savedObjects;
    //imported pages from getImportedPage(PdfReader, int, boolean)
    protected ArrayList<ImportedPage> importedPages;
    //for correct update of kids in StructTreeRootController
    protected boolean updateRootKids = false;

    static private final PdfName annotId = new PdfName("iTextAnnotId");
    static private int annotIdCnt = 0;

    protected boolean mergeFields = false;
    private boolean needAppearances = false;
    private boolean hasSignature;
    private PdfIndirectReference acroForm;
    private HashMap<PdfArray, ArrayList<Integer>> tabOrder;
    private ArrayList<Object> calculationOrderRefs;
    private PdfDictionary resources;
    protected ArrayList<AcroFields> fields;
    private ArrayList<String> calculationOrder;
    private HashMap<String, Object> fieldTree;
    private HashMap<Integer, PdfIndirectObject> unmergedMap;
    private HashMap<RefKey, PdfIndirectObject> unmergedIndirectRefsMap;
    private HashMap<Integer, PdfIndirectObject> mergedMap;
    private HashSet<PdfIndirectObject> mergedSet;
    private boolean mergeFieldsInternalCall = false;
    private static final PdfName iTextTag = new PdfName("_iTextTag_");
    private static final Integer zero = Integer.valueOf(0);
    private HashSet<Object> mergedRadioButtons = new HashSet<Object>();
    private HashMap<Object, PdfString> mergedTextFields = new HashMap<Object, PdfString>();

    private HashSet<PdfReader> readersWithImportedStructureTreeRootKids = new HashSet<PdfReader>();

    protected static class ImportedPage {
        int pageNumber;
        PdfReader reader;
        PdfArray mergedFields;
        PdfIndirectReference annotsIndirectReference;

        ImportedPage(PdfReader reader, int pageNumber, boolean keepFields) {
            this.pageNumber = pageNumber;
            this.reader = reader;
            if (keepFields) {
                mergedFields = new PdfArray();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ImportedPage)) return false;
            ImportedPage other = (ImportedPage)o;
            return this.pageNumber == other.pageNumber && this.reader.equals(other.reader);
        }
        @Override
        public String toString() {
            return Integer.toString(pageNumber);
        }
    }

    /**
     * Constructor
     * @param document document
     * @param os outputstream
     */
    public PdfCopy(Document document, OutputStream os) throws DocumentException {
        super(new PdfDocument(), os);
        document.addDocListener(pdf);
        pdf.addWriter(this);
        indirectMap = new HashMap<PdfReader, HashMap<RefKey, IndirectReferences>>();
        parentObjects = new HashMap<PdfObject, PdfObject>();
        disableIndirects = new HashSet<PdfObject>();

        indirectObjects = new LinkedHashMap<RefKey, PdfIndirectObject>();
        savedObjects = new ArrayList<PdfIndirectObject>();
        importedPages = new ArrayList<ImportedPage>();
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

    public void setMergeFields() {
        this.mergeFields = true;
        resources = new PdfDictionary();
        fields = new ArrayList<AcroFields>();
        calculationOrder = new ArrayList<String>();
        fieldTree = new LinkedHashMap<String, Object>();
        unmergedMap = new HashMap<Integer, PdfIndirectObject>();
        unmergedIndirectRefsMap = new HashMap<RefKey, PdfIndirectObject>();
        mergedMap = new HashMap<Integer, PdfIndirectObject>();
        mergedSet = new HashSet<PdfIndirectObject>();
    }

    /**
     * Grabs a page from the input document
     * @param reader the reader of the document
     * @param pageNumber which page to get
     * @return the page
     */
    @Override
    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
        if (mergeFields && !mergeFieldsInternalCall) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", "getImportedPage"));
        }
        if (mergeFields) {
            ImportedPage newPage = new ImportedPage(reader, pageNumber, mergeFields);
            importedPages.add(newPage);
        }
        if (structTreeController != null)
            structTreeController.reader = null;
        disableIndirects.clear();
        parentObjects.clear();
        return getImportedPageImpl(reader, pageNumber);
    }

    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber, boolean keepTaggedPdfStructure) throws BadPdfFormatException {
        if (mergeFields && !mergeFieldsInternalCall) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", "getImportedPage"));
        }
        updateRootKids = false;
        if (!keepTaggedPdfStructure) {
            if (mergeFields) {
                ImportedPage newPage = new ImportedPage(reader, pageNumber, mergeFields);
                importedPages.add(newPage);
            }
            return getImportedPageImpl(reader, pageNumber);
        }
        if (structTreeController != null) {
            if (reader != structTreeController.reader)
                structTreeController.setReader(reader);
        } else {
            structTreeController = new PdfStructTreeController(reader, this);
        }
        ImportedPage newPage = new ImportedPage(reader, pageNumber, mergeFields);
        switch (checkStructureTreeRootKids(newPage)) {
            case -1: //-1 - clear , update
                clearIndirects(reader);
                updateRootKids = true;
                break;
            case 0: //0 - not clear, not update
                updateRootKids = false;
                break;
            case 1: //1 - not clear, update
                updateRootKids = true;
                break;
        }
        importedPages.add(newPage);

        disableIndirects.clear();
        parentObjects.clear();
        return getImportedPageImpl(reader, pageNumber);
    }

    private void clearIndirects(PdfReader reader) {
        HashMap<RefKey, IndirectReferences> currIndirects = indirectMap.get(reader);
        ArrayList<RefKey> forDelete = new ArrayList<RefKey>();
        for (Map.Entry<RefKey, IndirectReferences> entry: currIndirects.entrySet()) {
            PdfIndirectReference iRef = entry.getValue().theRef;
            RefKey key = new RefKey(iRef);
            PdfIndirectObject iobj = indirectObjects.get(key);
            if (iobj == null) {
                forDelete.add(entry.getKey());
            }
            else if (iobj.object.isArray() || iobj.object.isDictionary() || iobj.object.isStream()) {
                forDelete.add(entry.getKey());
            }
        }

        for (RefKey key: forDelete)
            currIndirects.remove(key);
    }

    //0 - not clear, not update
    //-1 - clear , update
    //1 - not clear, update
    private int checkStructureTreeRootKids(ImportedPage newPage) {
        //start of document;
        if (importedPages.size() == 0) return 1;
        boolean readerExist = false;
        for (ImportedPage page: importedPages) {
            if (page.reader.equals(newPage.reader)) {
                readerExist = true;
                break;
            }
        }

        //add new reader;
        if (!readerExist) return 1;

        ImportedPage lastPage = importedPages.get(importedPages.size() - 1);
        boolean equalReader = lastPage.reader.equals(newPage.reader);
        //reader exist, correct order;
        if (equalReader && newPage.pageNumber > lastPage.pageNumber) {
            if (readersWithImportedStructureTreeRootKids.contains(newPage.reader))
                return 0;
            else
                return 1;
        }
        //reader exist, incorrect order;
        return -1;
    }

    protected void structureTreeRootKidsForReaderImported(PdfReader reader) {
        readersWithImportedStructureTreeRootKids.add(reader);
    }

    protected void fixStructureTreeRoot(HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps) {
        HashMap<PdfName, PdfObject> newClassMap = new HashMap<PdfName, PdfObject>(activeClassMaps.size());
        for (PdfName key: activeClassMaps) {
            PdfObject cm = structureTreeRoot.classes.get(key);
            if (cm != null) newClassMap.put(key, cm);
        }

        structureTreeRoot.classes = newClassMap;

        PdfArray kids = structureTreeRoot.getAsArray(PdfName.K);
        if (kids != null) {
            for (int i = 0; i < kids.size(); ++i) {
                PdfIndirectReference iref = (PdfIndirectReference)kids.getPdfObject(i);
                RefKey key = new RefKey(iref);
                if (!activeKeys.contains(key)) kids.remove(i--);
            }
        }
    }

    protected PdfImportedPage getImportedPageImpl(PdfReader reader, int pageNumber) {
        if (currentPdfReaderInstance != null) {
            if (currentPdfReaderInstance.getReader() != reader) {

//  TODO: Removed - the user should be responsible for closing all PdfReaders.  But, this could cause a lot of memory leaks in code out there that hasn't been properly closing things - maybe add a finalizer to PdfReader that calls PdfReader#close() ??
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
            if (type != null) {
                if ((PdfName.PAGE.equals(type))) {
                    return theRef;
                }
                if ((PdfName.CATALOG.equals(type))) {
                    LOGGER.warn(MessageLocalization.getComposedMessage("make.copy.of.catalog.dictionary.is.forbidden"));
                    return null;
                }
            }
        }
        iRef.setCopied();
        if (obj != null) parentObjects.put(obj, in);
        PdfObject res = copyObject(obj, keepStructure, directRootKids);
        if (disableIndirects.contains(obj))
            iRef.setNotCopied();
        if (res != null)
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
        PdfDictionary out = new PdfDictionary(in.size());
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
        if (structTreeController != null && structTreeController.reader != null && (in.contains(PdfName.STRUCTPARENTS) || in.contains(PdfName.STRUCTPARENT))) {
            PdfName key = PdfName.STRUCTPARENT;
            if (in.contains(PdfName.STRUCTPARENTS)) {
                key = PdfName.STRUCTPARENTS;
            }
            PdfObject value = in.get(key);
            out.put(key, new PdfNumber(currentStructArrayNumber));
            structTreeController.copyStructTreeForPage((PdfNumber) value, currentStructArrayNumber++);
        }
        for (Object element : in.getKeys()) {
            PdfName key = (PdfName)element;
            PdfObject value = in.get(key);
            if (structTreeController != null && structTreeController.reader != null && (key.equals(PdfName.STRUCTPARENTS) || key.equals(PdfName.STRUCTPARENT))) {
                continue;
            }
            if (PdfName.PAGE.equals(type)) {
                if (!key.equals(PdfName.B) && !key.equals(PdfName.PARENT)) {
                    parentObjects.put(value, in);
                    PdfObject res = copyObject(value, keepStruct, directRootKids);
                    if (res != null)
                        out.put(key, res);
                }
            }
            else {
                PdfObject res;
                if (tagged && value.isIndirect() && isStructTreeRootReference((PRIndirectReference)value)) {
                    res = structureTreeRoot.getReference();
                } else {
                    res = copyObject(value, keepStruct, directRootKids);
                }
                if (res != null)
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
            if (res != null)
                out.put(key, res);
        }

        return out;
    }

    /**
     * Translate a PRArray to a PdfArray. Also translate all of the objects contained
     * in it
     */
    protected PdfArray copyArray(PdfArray in, boolean keepStruct, boolean directRootKids) throws IOException, BadPdfFormatException {
        PdfArray out = new PdfArray(in.size());

        for (Iterator<PdfObject> i = in.listIterator(); i.hasNext();) {
            PdfObject value = i.next();
            parentObjects.put(value, in);
            PdfObject res = copyObject(value, keepStruct, directRootKids);
            if (res != null)
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
    protected PdfObject copyObject(PdfObject in, boolean keepStruct, boolean directRootKids) throws IOException,BadPdfFormatException {
        if (in == null)
            return PdfNull.PDFNULL;
        switch (in.type) {
            case PdfObject.DICTIONARY:
                return copyDictionary((PdfDictionary)in, keepStruct, directRootKids);
            case PdfObject.INDIRECT:
                if (!keepStruct && !directRootKids)
                    // fix for PdfSmartCopy
                    return copyIndirect((PRIndirectReference)in);
                else
                    return copyIndirect((PRIndirectReference)in, keepStruct, directRootKids);
            case PdfObject.ARRAY:
                return copyArray((PdfArray)in, keepStruct, directRootKids);
            case PdfObject.NUMBER:
            case PdfObject.NAME:
            case PdfObject.STRING:
            case PdfObject.NULL:
            case PdfObject.BOOLEAN:
            case 0://PdfIndirectReference
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
        }
    }
    /**
     * Add an imported page to our output
     * @param iPage an imported page
     * @throws IOException, BadPdfFormatException
     */
    public void addPage(PdfImportedPage iPage) throws IOException, BadPdfFormatException {
        if (mergeFields && !mergeFieldsInternalCall) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", "addPage"));
        }

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
        if (tagged)
            structTreeRootReference = (PRIndirectReference)reader.getCatalog().get(PdfName.STRUCTTREEROOT);
        PdfDictionary newPage = copyDictionary(thePage);
        if (mergeFields) {
            ImportedPage importedPage = importedPages.get(importedPages.size() - 1);
            importedPage.annotsIndirectReference = body.getPdfIndirectReference();
            newPage.put(PdfName.ANNOTS, importedPage.annotsIndirectReference);
        }
        root.addPage(newPage);
        iPage.setCopied();
        ++currentPageNumber;
        pdf.setPageCount(currentPageNumber);
        structTreeRootReference = null;
    }

    /**
     * Adds a blank page.
     * @param	rect The page dimension
     * @param	rotation The rotation angle in degrees
     * @throws DocumentException
     * @since	2.1.5
     */
    public void addPage(Rectangle rect, int rotation) throws DocumentException {
        if (mergeFields && !mergeFieldsInternalCall) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.cannot.be.used.in.mergeFields.mode.please.use.addDocument", "addPage"));
        }
        PdfRectangle mediabox = new PdfRectangle(rect, rotation);
        PageResources resources = new PageResources();
        PdfPage page = new PdfPage(mediabox, new HashMap<String, PdfRectangle>(), resources.getResources(), 0);
        page.put(PdfName.TABS, getTabs());
        root.addPage(page);
        ++currentPageNumber;
        pdf.setPageCount(currentPageNumber);
    }

    public void addDocument(PdfReader reader, List<Integer> pagesToKeep) throws DocumentException, IOException {
        if (indirectMap.containsKey(reader)) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("document.1.has.already.been.added", reader.toString()));
        }
        reader.selectPages(pagesToKeep, false);
        addDocument(reader);
    }

    /**
     * Copy document fields to a destination document.
     * @param reader a document where fields are copied from.
     * @throws DocumentException
     * @throws IOException
     */
    public void copyDocumentFields(PdfReader reader) throws DocumentException, IOException {
        if (!document.isOpen()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("the.document.is.not.open.yet.you.can.only.add.meta.information"));
        }

        if (indirectMap.containsKey(reader)) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("document.1.has.already.been.added", reader.toString()));
        }

        if (!reader.isOpenedWithFullPermissions())
            throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password"));

        if (!mergeFields)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.method.can.be.only.used.in.mergeFields.mode.please.use.addDocument", "copyDocumentFields"));

        indirects = new HashMap<RefKey, IndirectReferences>();
        indirectMap.put(reader, indirects);

        reader.consolidateNamedDestinations();
        reader.shuffleSubsetNames();
        if (tagged && PdfStructTreeController.checkTagged(reader)) {
            structTreeRootReference = (PRIndirectReference)reader.getCatalog().get(PdfName.STRUCTTREEROOT);
            if (structTreeController != null) {
                if (reader != structTreeController.reader)
                    structTreeController.setReader(reader);
            } else {
                structTreeController = new PdfStructTreeController(reader, this);
            }
        }

        List<PdfObject> annotationsToBeCopied = new ArrayList<PdfObject>();

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            PdfDictionary page = reader.getPageNRelease(i);
            if (page != null && page.contains(PdfName.ANNOTS)) {
                PdfArray annots = page.getAsArray(PdfName.ANNOTS);
                if (annots != null && annots.size() > 0) {
                    if (importedPages.size() < i)
                        throw new DocumentException(MessageLocalization.getComposedMessage("there.are.not.enough.imported.pages.for.copied.fields"));
                    indirectMap.get(reader).put(new RefKey(reader.pageRefs.getPageOrigRef(i)), new IndirectReferences(pageReferences.get(i - 1)));
                    for (int j = 0; j < annots.size(); j++) {
                        PdfDictionary annot = annots.getAsDict(j);
                        if (annot != null) {
                            annot.put(annotId, new PdfNumber(++annotIdCnt));
                            annotationsToBeCopied.add(annots.getPdfObject(j));
                        }
                    }
                }
            }
        }

        for (PdfObject annot : annotationsToBeCopied) {
            copyObject(annot);
        }

        if (tagged && structTreeController != null)
            structTreeController.attachStructTreeRootKids(null);

        AcroFields acro = reader.getAcroFields();
        boolean needapp = !acro.isGenerateAppearances();
        if (needapp)
            needAppearances = true;
        fields.add(acro);
        updateCalculationOrder(reader);
        structTreeRootReference = null;
    }

    public void addDocument(PdfReader reader) throws DocumentException, IOException {
        if ( ! document.isOpen() ) {
            throw new DocumentException(MessageLocalization.getComposedMessage("the.document.is.not.open.yet.you.can.only.add.meta.information"));
        }
        if (indirectMap.containsKey(reader)) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("document.1.has.already.been.added", reader.toString()));
        }
        if (!reader.isOpenedWithFullPermissions())
            throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password"));
        if (mergeFields) {
            reader.consolidateNamedDestinations();
            reader.shuffleSubsetNames();
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                PdfDictionary page = reader.getPageNRelease(i);
                if (page != null && page.contains(PdfName.ANNOTS)) {
                    PdfArray annots = page.getAsArray(PdfName.ANNOTS);
                    if (annots != null) {
                        for (int j = 0; j < annots.size(); j++) {
                            PdfDictionary annot = annots.getAsDict(j);
                            if (annot != null)
                                annot.put(annotId, new PdfNumber(++annotIdCnt));
                        }
                    }
                }
            }
            AcroFields acro = reader.getAcroFields();
            // when a document with NeedAppearances is encountered, the flag is set
            // in the resulting document.
            boolean needapp = !acro.isGenerateAppearances();
            if (needapp)
                needAppearances = true;
            fields.add(acro);
            updateCalculationOrder(reader);
        }
        boolean tagged = this.tagged && PdfStructTreeController.checkTagged(reader);
        mergeFieldsInternalCall = true;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            addPage(getImportedPage(reader, i, tagged));
        }
        mergeFieldsInternalCall = false;
    }

    @Override
    public PdfIndirectObject addToBody(final PdfObject object, final PdfIndirectReference ref) throws IOException {
        return this.addToBody(object, ref, false);
    }

    @Override
    public PdfIndirectObject addToBody(final PdfObject object, final PdfIndirectReference ref, boolean formBranching) throws IOException {
        if (formBranching) {
            updateReferences(object);
        }
        PdfIndirectObject iobj;
        if ((tagged || mergeFields) && indirectObjects != null && (object.isArray() || object.isDictionary() || object.isStream() || object.isNull())) {
            RefKey key = new RefKey(ref);
            PdfIndirectObject obj = indirectObjects.get(key);
            if (obj == null) {
                obj = new PdfIndirectObject(ref, object, this);
                indirectObjects.put(key, obj);
            }
            iobj =  obj;
        } else {
            iobj = super.addToBody(object, ref);
        }
        if (mergeFields && object.isDictionary()) {
            PdfNumber annotId = ((PdfDictionary)object).getAsNumber(PdfCopy.annotId);
            if (annotId != null) {
                if (formBranching) {
                    mergedMap.put(annotId.intValue(), iobj);
                    mergedSet.add(iobj);
                } else {
                    unmergedMap.put(annotId.intValue(), iobj);
                    unmergedIndirectRefsMap.put(new RefKey(iobj.number, iobj.generation), iobj);
                }
            }
        }
        return iobj;
    }

    @Override
    protected void cacheObject(PdfIndirectObject iobj) {
        if ((tagged || mergeFields) && indirectObjects != null) {
            savedObjects.add(iobj);
            RefKey key = new RefKey(iobj.number, iobj.generation);
            if (!indirectObjects.containsKey(key)) indirectObjects.put(key, iobj);
        }
    }

    @Override
    protected void flushTaggedObjects() throws IOException {
        try {
            fixTaggedStructure();
        } catch (ClassCastException ex) {
        } finally {flushIndirectObjects();}
    }

    @Override
    protected void flushAcroFields() throws IOException, BadPdfFormatException {
        if (mergeFields) {
            try {
                //save annotations that appear just at page level (comments, popups)
                for (ImportedPage page : importedPages) {
                    PdfDictionary pageDict = page.reader.getPageN(page.pageNumber);
                    if (pageDict != null) {
                        PdfArray pageFields = pageDict.getAsArray(PdfName.ANNOTS);
                        if (pageFields == null || pageFields.size() == 0)
                            continue;
                        for (AcroFields.Item items: page.reader.getAcroFields().getFields().values()) {
                            for(PdfIndirectReference ref: items.widget_refs) {
                                pageFields.arrayList.remove(ref);
                            }
                        }
                        indirects = indirectMap.get(page.reader);
                        for (PdfObject ref: pageFields.arrayList)
                            page.mergedFields.add(copyObject(ref));
                    }
                }
                //ok, remove old fields and build create new one
                for (PdfReader reader : indirectMap.keySet()) {
                    reader.removeFields();
                }
                mergeFields();
                createAcroForms();

            } catch (ClassCastException ex) {
            } finally {
                if (!tagged)
                    flushIndirectObjects();
            }
        }
    }


    protected void fixTaggedStructure() throws IOException {
        HashMap<Integer, PdfIndirectReference> numTree = structureTreeRoot.getNumTree();
        HashSet<RefKey> activeKeys = new HashSet<RefKey>();
        ArrayList<PdfIndirectReference> actives = new ArrayList<PdfIndirectReference>();
        int pageRefIndex = 0;

        if (mergeFields && acroForm != null) {
            actives.add(acroForm);
            activeKeys.add(new RefKey(acroForm));
        }
        for (PdfIndirectReference page : pageReferences) {
            actives.add(page);
            activeKeys.add(new RefKey(page));
        }

        //from end, because some objects can appear on several pages because of MCR (out16.pdf)
        for (int i = numTree.size() - 1; i >= 0; --i) {
            PdfIndirectReference currNum = numTree.get(i);
            if (currNum == null) {
                continue;
            }
            RefKey numKey = new RefKey(currNum);
            PdfObject obj = indirectObjects.get(numKey).object;
            if (obj.isDictionary()) {
                boolean addActiveKeys = false;
                if (pageReferences.contains(((PdfDictionary)obj).get(PdfName.PG))) {
                    addActiveKeys = true;
                } else {
                    PdfDictionary k = PdfStructTreeController.getKDict((PdfDictionary)obj);
                    if (k != null && pageReferences.contains(k.get(PdfName.PG))) {
                        addActiveKeys = true;
                    }
                }
                if (addActiveKeys) {
                    activeKeys.add(numKey);
                    actives.add(currNum);
                } else {
                    numTree.remove(i);
                }
            } else if (obj.isArray()) {
                activeKeys.add(numKey);
                actives.add(currNum);
                PdfArray currNums = (PdfArray)obj;
                PdfIndirectReference currPage = pageReferences.get(pageRefIndex++);
                actives.add(currPage);
                activeKeys.add(new RefKey(currPage));
                PdfIndirectReference prevKid = null;
                for (int j = 0; j < currNums.size(); j++) {
                    PdfIndirectReference currKid = (PdfIndirectReference)currNums.getDirectObject(j);
                    if (currKid.equals(prevKid)) continue;
                    RefKey kidKey = new RefKey(currKid);
                    activeKeys.add(kidKey);
                    actives.add(currKid);

                    PdfIndirectObject iobj = indirectObjects.get(kidKey);
                    if (iobj.object.isDictionary()) {
                        PdfDictionary dict = (PdfDictionary)iobj.object;
                        PdfIndirectReference pg = (PdfIndirectReference)dict.get(PdfName.PG);
                        //if pg is real page - do nothing, else set correct pg and remove first MCID if exists
                        if (pg != null && !pageReferences.contains(pg) && !pg.equals(currPage)){
                            dict.put(PdfName.PG, currPage);
                            PdfArray kids = dict.getAsArray(PdfName.K);
                            if (kids != null) {
                                PdfObject firstKid = kids.getDirectObject(0);
                                if (firstKid.isNumber()) kids.remove(0);
                            }
                        }
                    }
                    prevKid = currKid;
                }
            }
        }

        HashSet<PdfName> activeClassMaps = new HashSet<PdfName>();
        //collect all active objects from current active set (include kids, classmap, attributes)
        findActives(actives, activeKeys, activeClassMaps);
        //find parents of active objects
        ArrayList<PdfIndirectReference> newRefs = findActiveParents(activeKeys);
        //find new objects with incorrect Pg; if find, set Pg from first correct kid. This correct kid must be.
        fixPgKey(newRefs, activeKeys);
        //remove unused kids of StructTreeRoot and remove unused objects from class map
        fixStructureTreeRoot(activeKeys, activeClassMaps);

        for(Map.Entry<RefKey, PdfIndirectObject> entry: indirectObjects.entrySet()) {
            if (!activeKeys.contains(entry.getKey())) {
                entry.setValue(null);
            }
            else {
                if (entry.getValue().object.isArray()) {
                    removeInactiveReferences((PdfArray)entry.getValue().object, activeKeys);
                } else if (entry.getValue().object.isDictionary()) {
                    PdfObject kids = ((PdfDictionary)entry.getValue().object).get(PdfName.K);
                    if (kids != null && kids.isArray())
                        removeInactiveReferences((PdfArray)kids, activeKeys);
                }
            }
        }
    }

    private void removeInactiveReferences(PdfArray array, HashSet<RefKey> activeKeys) {
        for (int i = 0; i < array.size(); ++i) {
            PdfObject obj = array.getPdfObject(i);
            if ((obj.type() == 0 && !activeKeys.contains(new RefKey((PdfIndirectReference)obj))) ||
                    (obj.isDictionary() && containsInactivePg((PdfDictionary)obj, activeKeys)))
                array.remove(i--);
        }
    }

    private boolean containsInactivePg(PdfDictionary dict, HashSet<RefKey> activeKeys) {
        PdfObject pg = dict.get(PdfName.PG);
        if (pg != null && !activeKeys.contains(new RefKey((PdfIndirectReference)pg)))
            return true;
        return false;
    }

    //return new found objects
    private ArrayList<PdfIndirectReference> findActiveParents(HashSet<RefKey> activeKeys){
        ArrayList<PdfIndirectReference> newRefs = new ArrayList<PdfIndirectReference>();
        ArrayList<RefKey> tmpActiveKeys = new ArrayList<RefKey>(activeKeys);
        for (int i = 0; i < tmpActiveKeys.size(); ++i) {
            PdfIndirectObject iobj = indirectObjects.get(tmpActiveKeys.get(i));
            if (iobj == null || !iobj.object.isDictionary()) continue;
            PdfObject parent = ((PdfDictionary)iobj.object).get(PdfName.P);
            if (parent != null && parent.type() == 0) {
                RefKey key = new RefKey((PdfIndirectReference)parent);
                if (!activeKeys.contains(key)) {
                    activeKeys.add(key);
                    tmpActiveKeys.add(key);
                    newRefs.add((PdfIndirectReference) parent);
                }
            }
        }
        return newRefs;
    }

    private void fixPgKey(ArrayList<PdfIndirectReference> newRefs, HashSet<RefKey> activeKeys){
        for (PdfIndirectReference iref: newRefs) {
            PdfIndirectObject iobj = indirectObjects.get(new RefKey(iref));
            if (iobj == null || !iobj.object.isDictionary()) continue;
            PdfDictionary dict = (PdfDictionary)iobj.object;
            PdfObject pg = dict.get(PdfName.PG);
            if (pg == null || activeKeys.contains(new RefKey((PdfIndirectReference)pg))) continue;
            PdfArray kids = dict.getAsArray(PdfName.K);
            if (kids == null) continue;
            for (int i = 0; i < kids.size(); ++i) {
                PdfObject obj = kids.getPdfObject(i);
                if (obj.type() != 0) {
                    kids.remove(i--);
                } else {
                    PdfIndirectObject kid = indirectObjects.get(new RefKey((PdfIndirectReference)obj));
                    if (kid != null && kid.object.isDictionary()) {
                        PdfObject kidPg = ((PdfDictionary)kid.object).get(PdfName.PG);
                        if (kidPg != null && activeKeys.contains(new RefKey((PdfIndirectReference)kidPg))) {
                            dict.put(PdfName.PG, kidPg);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void findActives(ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps){
        //collect all active objects from current active set (include kids, classmap, attributes)
        for (int i = 0; i < actives.size(); ++i) {
            RefKey key = new RefKey(actives.get(i));
            PdfIndirectObject iobj = indirectObjects.get(key);
            if (iobj == null || iobj.object == null) continue;
            switch (iobj.object.type()){
                case 0://PdfIndirectReference
                    findActivesFromReference((PdfIndirectReference)iobj.object, actives, activeKeys);
                    break;
                case PdfObject.ARRAY:
                    findActivesFromArray((PdfArray)iobj.object, actives, activeKeys, activeClassMaps);
                    break;
                case PdfObject.DICTIONARY:
                case PdfObject.STREAM:
                    findActivesFromDict((PdfDictionary)iobj.object, actives, activeKeys, activeClassMaps);
                    break;
            }
        }
    }

    private void findActivesFromReference(PdfIndirectReference iref, ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys) {
        RefKey key = new RefKey(iref);
        PdfIndirectObject iobj = indirectObjects.get(key);
        if (iobj != null && iobj.object.isDictionary() && containsInactivePg((PdfDictionary) iobj.object, activeKeys)) return;

        if(!activeKeys.contains(key)) {
            activeKeys.add(key);
            actives.add(iref);
        }
    }

    private void findActivesFromArray(PdfArray array, ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys, HashSet<PdfName> activeClassMaps) {
        for (PdfObject obj: array) {
            switch (obj.type()) {
                case 0://PdfIndirectReference
                    findActivesFromReference((PdfIndirectReference)obj, actives, activeKeys);
                    break;
                case PdfObject.ARRAY:
                    findActivesFromArray((PdfArray)obj, actives, activeKeys, activeClassMaps);
                    break;
                case PdfObject.DICTIONARY:
                case PdfObject.STREAM:
                    findActivesFromDict((PdfDictionary)obj, actives, activeKeys, activeClassMaps);
                    break;
            }
        }
    }

    private void findActivesFromDict(PdfDictionary dict, ArrayList<PdfIndirectReference> actives, HashSet<RefKey> activeKeys,  HashSet<PdfName> activeClassMaps) {
        if (containsInactivePg(dict, activeKeys)) return;
        for (PdfName key: dict.getKeys()) {
            PdfObject obj = dict.get(key);
            if (key.equals(PdfName.P)) continue;
            else if (key.equals(PdfName.C)) { //classmap
                if (obj.isArray()) {
                    for (PdfObject cm: (PdfArray)obj) {
                        if (cm.isName()) activeClassMaps.add((PdfName)cm);
                    }
                }
                else if (obj.isName()) activeClassMaps.add((PdfName)obj);
                continue;
            }
            switch (obj.type()) {
                case 0://PdfIndirectReference
                    findActivesFromReference((PdfIndirectReference)obj, actives, activeKeys);
                    break;
                case PdfObject.ARRAY:
                    findActivesFromArray((PdfArray)obj, actives, activeKeys, activeClassMaps);
                    break;
                case PdfObject.DICTIONARY:
                case PdfObject.STREAM:
                    findActivesFromDict((PdfDictionary)obj, actives, activeKeys, activeClassMaps);
                    break;
            }
        }
    }

    protected void flushIndirectObjects() throws IOException {
        for (PdfIndirectObject iobj: savedObjects)
            indirectObjects.remove(new RefKey(iobj.number, iobj.generation));
        HashSet<RefKey> inactives = new HashSet<RefKey>();
        for(Map.Entry<RefKey, PdfIndirectObject> entry: indirectObjects.entrySet()) {
            if (entry.getValue() != null)
                writeObjectToBody(entry.getValue());
            else
                inactives.add(entry.getKey());
        }
        ArrayList<PdfBody.PdfCrossReference> pdfCrossReferences = new ArrayList<PdfBody.PdfCrossReference>(body.xrefs);
        for (PdfBody.PdfCrossReference cr : pdfCrossReferences) {
            RefKey key = new RefKey(cr.getRefnum(), 0);
            if (inactives.contains(key))
                body.xrefs.remove(cr);
        }
        indirectObjects = null;
    }

    private void writeObjectToBody(PdfIndirectObject object) throws IOException {
        boolean skipWriting = false;
        if (mergeFields) {
            updateAnnotationReferences(object.object);
            if (object.object.isDictionary() || object.object.isStream()) {
                PdfDictionary dictionary = (PdfDictionary)object.object;
                if (unmergedIndirectRefsMap.containsKey(new RefKey(object.number, object.generation))) {
                    PdfNumber annotId = dictionary.getAsNumber(PdfCopy.annotId);
                    if (annotId != null && mergedMap.containsKey(annotId.intValue()))
                        skipWriting = true;
                }
                if (mergedSet.contains(object)) {
                    PdfNumber annotId = dictionary.getAsNumber(PdfCopy.annotId);
                    if (annotId != null) {
                        PdfIndirectObject unmerged = unmergedMap.get(annotId.intValue());
                        if (unmerged != null && unmerged.object.isDictionary()) {
                            PdfNumber structParent = ((PdfDictionary)unmerged.object).getAsNumber(PdfName.STRUCTPARENT);
                            if (structParent != null) {
                                dictionary.put(PdfName.STRUCTPARENT, structParent);
                            }
                        }
                    }
                }
            }
        }
        if (!skipWriting) {
            PdfDictionary dictionary = null;
            PdfNumber annotId = null;
            if (mergeFields && object.object.isDictionary()) {
                dictionary = (PdfDictionary)object.object;
                annotId = dictionary.getAsNumber(PdfCopy.annotId);
                if (annotId != null)
                    dictionary.remove(PdfCopy.annotId);
            }
            body.add(object.object, object.number, object.generation, true);
            if (annotId != null) {
                dictionary.put(PdfCopy.annotId, annotId);
            }
        }
    }

    private void updateAnnotationReferences(PdfObject obj) {
        if (obj.isArray()) {
            PdfArray array = (PdfArray)obj;
            for (int i = 0; i < array.size(); i++) {
                PdfObject o = array.getPdfObject(i);
                if (o != null && o.type() == 0) {
                    PdfIndirectObject entry = unmergedIndirectRefsMap.get(new RefKey((PdfIndirectReference)o));
                    if (entry != null) {
                        if (entry.object.isDictionary()) {
                            PdfNumber annotId = ((PdfDictionary) entry.object).getAsNumber(PdfCopy.annotId);
                            if (annotId != null) {
                                PdfIndirectObject merged = mergedMap.get(annotId.intValue());
                                if (merged != null) {
                                    array.set(i, merged.getIndirectReference());
                                }
                            }
                        }
                    }
                } else {
                    updateAnnotationReferences(o);
                }
            }
        } else if (obj.isDictionary() || obj.isStream()) {
            PdfDictionary dictionary = (PdfDictionary)obj;
            for (PdfName key : dictionary.getKeys()) {
                PdfObject o = dictionary.get(key);
                if (o != null && o.type() == 0) {
                    PdfIndirectObject entry = unmergedIndirectRefsMap.get(new RefKey((PdfIndirectReference)o));
                    if (entry != null) {
                        if (entry.object.isDictionary()) {
                            PdfNumber annotId = ((PdfDictionary) entry.object).getAsNumber(PdfCopy.annotId);
                            if (annotId != null) {
                                PdfIndirectObject merged = mergedMap.get(annotId.intValue());
                                if (merged != null) {
                                    dictionary.put(key, merged.getIndirectReference());
                                }
                            }
                        }
                    }
                } else {
                    updateAnnotationReferences(o);
                }
            }
        }
    }

    private void updateCalculationOrder(PdfReader reader) {
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary acro = catalog.getAsDict(PdfName.ACROFORM);
        if (acro == null)
            return;
        PdfArray co = acro.getAsArray(PdfName.CO);
        if (co == null || co.size() == 0)
            return;
        AcroFields af = reader.getAcroFields();
        for (int k = 0; k < co.size(); ++k) {
            PdfObject obj = co.getPdfObject(k);
            if (obj == null || !obj.isIndirect())
                continue;
            String name = getCOName(reader, (PRIndirectReference) obj);
            if (af.getFieldItem(name) == null)
                continue;
            name = "." + name;
            if (calculationOrder.contains(name))
                continue;
            calculationOrder.add(name);
        }
    }

    private static String getCOName(PdfReader reader, PRIndirectReference ref) {
        String name = "";
        while (ref != null) {
            PdfObject obj = PdfReader.getPdfObject(ref);
            if (obj == null || obj.type() != PdfObject.DICTIONARY)
                break;
            PdfDictionary dic = (PdfDictionary)obj;
            PdfString t = dic.getAsString(PdfName.T);
            if (t != null) {
                name = t.toUnicodeString()+ "." + name;
            }
            ref = (PRIndirectReference)dic.get(PdfName.PARENT);
        }
        if (name.endsWith("."))
            name = name.substring(0, name.length() - 2);
        return name;
    }

    private void mergeFields() {
        int pageOffset = 0;
        for (int k = 0; k < fields.size(); ++k) {
            AcroFields af = fields.get(k);
            Map<String, AcroFields.Item> fd = af.getFields();
            if (pageOffset < importedPages.size() && importedPages.get(pageOffset).reader == af.reader) {
                addPageOffsetToField(fd, pageOffset);
                pageOffset += af.reader.getNumberOfPages();
            }
            mergeWithMaster(fd);
        }
    }

    private void addPageOffsetToField(Map<String, AcroFields.Item> fd, int pageOffset) {
        if (pageOffset == 0)
            return;
        for (AcroFields.Item item: fd.values()) {
            for (int k = 0; k < item.size(); ++k) {
                int p = item.getPage(k).intValue();
                item.forcePage(k, p + pageOffset);
            }
        }
    }

    private void mergeWithMaster(Map<String, AcroFields.Item> fd) {
        for (Map.Entry<String, AcroFields.Item> entry: fd.entrySet()) {
            String name = entry.getKey();
            mergeField(name, entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    private void mergeField(String name, AcroFields.Item item) {
        HashMap<String, Object> map = fieldTree;
        StringTokenizer tk = new StringTokenizer(name, ".");
        if (!tk.hasMoreTokens())
            return;
        while (true) {
            String s = tk.nextToken();
            Object obj = map.get(s);
            if (tk.hasMoreTokens()) {
                if (obj == null) {
                    obj = new LinkedHashMap<String, Object>();
                    map.put(s, obj);
                    map = (HashMap<String, Object>)obj;
                    continue;
                }
                else if (obj instanceof HashMap)
                    map = (HashMap<String, Object>)obj;
                else
                    return;
            }
            else {
                if (obj instanceof HashMap)
                    return;
                PdfDictionary merged = item.getMerged(0);
                if (obj == null) {
                    PdfDictionary field = new PdfDictionary();
                    if (PdfName.SIG.equals(merged.get(PdfName.FT)))
                        hasSignature = true;
                    for (Object element : merged.getKeys()) {
                        PdfName key = (PdfName)element;
                        if (fieldKeys.contains(key))
                            field.put(key, merged.get(key));
                    }
                    ArrayList<Object> list = new ArrayList<Object>();
                    list.add(field);
                    createWidgets(list, item);
                    map.put(s, list);
                }
                else {
                    ArrayList<Object> list = (ArrayList<Object>)obj;
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

    private void createWidgets(ArrayList<Object> list, AcroFields.Item item) {
        for (int k = 0; k < item.size(); ++k) {
            list.add(item.getPage(k));
            PdfDictionary merged = item.getMerged(k);
            PdfObject dr = merged.get(PdfName.DR);
            if (dr != null)
                PdfFormField.mergeResources(resources, (PdfDictionary)PdfReader.getPdfObject(dr));
            PdfDictionary widget = new PdfDictionary();
            for (Object element : merged.getKeys()) {
                PdfName key = (PdfName)element;
                if (widgetKeys.contains(key))
                    widget.put(key, merged.get(key));
            }
            widget.put(iTextTag, new PdfNumber(item.getTabOrder(k).intValue() + 1));
            list.add(widget);
        }
    }

    private PdfObject propagate(PdfObject obj) throws IOException {
        if (obj == null) {
            return new PdfNull();
        } else if (obj.isArray()) {
            PdfArray a = (PdfArray)obj;
            for (int i = 0; i < a.size(); i++) {
                a.set(i, propagate(a.getPdfObject(i)));
            }
            return a;
        } else if (obj.isDictionary() || obj.isStream()) {
            PdfDictionary d = (PdfDictionary)obj;
            for (PdfName key : d.getKeys()) {
                d.put(key, propagate(d.get(key)));
            }
            return d;
        } else if (obj.isIndirect()) {
            obj = PdfReader.getPdfObject(obj);
            return addToBody(propagate(obj)).getIndirectReference();
        } else
            return obj;
    }

    private void createAcroForms() throws IOException, BadPdfFormatException {
        if (fieldTree.isEmpty()) {
            //write annotations that appear just at page level (comments, popups)
            for (ImportedPage importedPage : importedPages) {
                if (importedPage.mergedFields.size() > 0)
                    addToBody(importedPage.mergedFields, importedPage.annotsIndirectReference);
            }
            return;
        }
        PdfDictionary form = new PdfDictionary();
        form.put(PdfName.DR, propagate(resources));

        if (needAppearances) {
            form.put(PdfName.NEEDAPPEARANCES, PdfBoolean.PDFTRUE);
        }
        form.put(PdfName.DA, new PdfString("/Helv 0 Tf 0 g "));
        tabOrder = new HashMap<PdfArray, ArrayList<Integer>>();
        calculationOrderRefs = new ArrayList<Object>(calculationOrder);
        form.put(PdfName.FIELDS, branchForm(fieldTree, null, ""));
        if (hasSignature)
            form.put(PdfName.SIGFLAGS, new PdfNumber(3));
        PdfArray co = new PdfArray();
        for (int k = 0; k < calculationOrderRefs.size(); ++k) {
            Object obj = calculationOrderRefs.get(k);
            if (obj instanceof PdfIndirectReference)
                co.add((PdfIndirectReference)obj);
        }
        if (co.size() > 0)
            form.put(PdfName.CO, co);
        this.acroForm = addToBody(form).getIndirectReference();
        for (ImportedPage importedPage : importedPages) {
            addToBody(importedPage.mergedFields, importedPage.annotsIndirectReference);
        }
    }

    private void updateReferences(PdfObject obj) {
        if (obj.isDictionary() || obj.isStream()) {
            PdfDictionary dictionary = (PdfDictionary)obj;
            for (PdfName key : dictionary.getKeys()) {
                PdfObject o = dictionary.get(key);
                if (o.isIndirect()) {
                    PdfReader reader = ((PRIndirectReference)o).getReader();
                    HashMap<RefKey,IndirectReferences> indirects = indirectMap.get(reader);
                    IndirectReferences indRef = indirects.get(new RefKey((PRIndirectReference)o));
                    if (indRef != null) {
                        dictionary.put(key, indRef.getRef());
                    }
                } else {
                    updateReferences(o);
                }
            }
        } else if (obj.isArray()) {
            PdfArray array = (PdfArray)obj;
            for (int i = 0; i < array.size(); i++) {
                PdfObject o = array.getPdfObject(i);
                if (o.isIndirect()) {
                    PdfReader reader = ((PRIndirectReference)o).getReader();
                    HashMap<RefKey,IndirectReferences> indirects = indirectMap.get(reader);
                    IndirectReferences indRef = indirects.get(new RefKey((PRIndirectReference)o));
                    if (indRef != null) {
                        array.set(i, indRef.getRef());
                    }
                } else {
                    updateReferences(o);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private PdfArray branchForm(HashMap<String, Object> level, PdfIndirectReference parent, String fname) throws IOException, BadPdfFormatException {
        PdfArray arr = new PdfArray();
        for (Map.Entry<String, Object> entry: level.entrySet()) {
            String name = entry.getKey();
            Object obj = entry.getValue();
            PdfIndirectReference ind = getPdfIndirectReference();
            PdfDictionary dic = new PdfDictionary();
            if (parent != null)
                dic.put(PdfName.PARENT, parent);
            dic.put(PdfName.T, new PdfString(name, PdfObject.TEXT_UNICODE));
            String fname2 = fname + "." + name;
            int coidx = calculationOrder.indexOf(fname2);
            if (coidx >= 0)
                calculationOrderRefs.set(coidx, ind);
            if (obj instanceof HashMap) {
                dic.put(PdfName.KIDS, branchForm((HashMap<String, Object>) obj, ind, fname2));
                arr.add(ind);
                addToBody(dic, ind, true);
            }
            else {
                ArrayList<Object> list = (ArrayList<Object>)obj;
                dic.mergeDifferent((PdfDictionary) list.get(0));
                if (list.size() == 3) {
                    dic.mergeDifferent((PdfDictionary)list.get(2));
                    int page = ((Integer)list.get(1)).intValue();
                    PdfArray annots = importedPages.get(page - 1).mergedFields;
                    PdfNumber nn = (PdfNumber)dic.get(iTextTag);
                    dic.remove(iTextTag);
                    dic.put(PdfName.TYPE, PdfName.ANNOT);
                    adjustTabOrder(annots, ind, nn);
                } else {
                    PdfDictionary field = (PdfDictionary)list.get(0);
                    PdfArray kids = new PdfArray();
                    for (int k = 1; k < list.size(); k += 2) {
                        int page = ((Integer)list.get(k)).intValue();
                        PdfArray annots = importedPages.get(page - 1).mergedFields;
                        PdfDictionary widget = new PdfDictionary();
                        widget.merge((PdfDictionary)list.get(k + 1));
                        widget.put(PdfName.PARENT, ind);
                        PdfNumber nn = (PdfNumber)widget.get(iTextTag);
                        widget.remove(iTextTag);
                        if (PdfCopy.isTextField(field)) {
                            PdfString v = field.getAsString(PdfName.V);
                            PdfObject ap = widget.getDirectObject(PdfName.AP);
                            if (v != null && ap != null) {
                                if (!mergedTextFields.containsKey(list)) {
                                    mergedTextFields.put(list, v);
                                } else {
                                    try {
                                        TextField tx = new TextField(this, null, null);
                                        fields.get(0).decodeGenericDictionary(widget, tx);
                                        Rectangle box = PdfReader.getNormalizedRectangle(widget.getAsArray(PdfName.RECT));
                                        if (tx.getRotation() == 90 || tx.getRotation() == 270)
                                            box = box.rotate();
                                        tx.setBox(box);
                                        tx.setText(mergedTextFields.get(list).toUnicodeString());
                                        PdfAppearance app = tx.getAppearance();
                                        ((PdfDictionary)ap).put(PdfName.N, app.getIndirectReference());
                                    } catch (DocumentException e) {
                                        //do nothing
                                    }
                                }
                            }
                        } else if (PdfCopy.isCheckButton(field)) {
                            PdfName v = field.getAsName(PdfName.V);
                            PdfName as = widget.getAsName(PdfName.AS);
                            if (v != null && as != null)
                                widget.put(PdfName.AS, v);
                        } else if (PdfCopy.isRadioButton(field)) {
                            PdfName v = field.getAsName(PdfName.V);
                            PdfName as = widget.getAsName(PdfName.AS);
                            if (v != null && as != null && !as.equals(getOffStateName(widget))) {
                                if (!mergedRadioButtons.contains(list)) {
                                    mergedRadioButtons.add(list);
                                    widget.put(PdfName.AS, v);
                                } else {
                                    widget.put(PdfName.AS, getOffStateName(widget));
                                }
                            }
                        }
                        widget.put(PdfName.TYPE, PdfName.ANNOT);
                        PdfIndirectReference wref = addToBody(widget, getPdfIndirectReference(), true).getIndirectReference();
                        adjustTabOrder(annots, wref, nn);
                        kids.add(wref);
                    }
                    dic.put(PdfName.KIDS, kids);
                }
                arr.add(ind);
                addToBody(dic, ind, true);
            }
        }
        return arr;
    }

    private void adjustTabOrder(PdfArray annots, PdfIndirectReference ind, PdfNumber nn) {
        int v = nn.intValue();
        ArrayList<Integer> t = tabOrder.get(annots);
        if (t == null) {
            t = new ArrayList<Integer>();
            int size = annots.size() - 1;
            for (int k = 0; k < size; ++k) {
                t.add(zero);
            }
            t.add(Integer.valueOf(v));
            tabOrder.put(annots, t);
            annots.add(ind);
        }
        else {
            int size = t.size() - 1;
            for (int k = size; k >= 0; --k) {
                if (t.get(k).intValue() <= v) {
                    t.add(k + 1, Integer.valueOf(v));
                    annots.add(k + 1, ind);
                    size = -2;
                    break;
                }
            }
            if (size != -2) {
                t.add(0, Integer.valueOf(v));
                annots.add(0, ind);
            }
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
            if (fieldArray != null) {
                addFieldResources(theCat);
            } else if (mergeFields && acroForm != null) {
                theCat.put(PdfName.ACROFORM, acroForm);
            }
            return theCat;
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    protected boolean isStructTreeRootReference(PdfIndirectReference prRef) {
        if (prRef == null || structTreeRootReference == null)
            return false;
        return prRef.number == structTreeRootReference.number && prRef.generation == structTreeRootReference.generation;
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
            pdf.close();
            super.close();
//  Users are responsible for closing PdfReader
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
        if (mergeFields)
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("it.is.not.possible.to.free.reader.in.merge.fields.mode"));
        PdfArray array = reader.trailer.getAsArray(PdfName.ID);
        if (array != null)
            originalFileID = array.getAsString(0).getBytes();
        indirectMap.remove(reader);
//  TODO: Removed - the user should be responsible for closing all PdfReaders.  But, this could cause a lot of memory leaks in code out there that hasn't been properly closing things - maybe add a finalizer to PdfReader that calls PdfReader#close() ??
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

    protected PdfName getOffStateName(PdfDictionary widget) {
        return PdfName.Off;
    }

    protected static final HashSet<PdfName> widgetKeys = new HashSet<PdfName>();
    protected static final HashSet<PdfName> fieldKeys = new HashSet<PdfName>();
    static {
        widgetKeys.add(PdfName.SUBTYPE);
        widgetKeys.add(PdfName.CONTENTS);
        widgetKeys.add(PdfName.RECT);
        widgetKeys.add(PdfName.NM);
        widgetKeys.add(PdfName.M);
        widgetKeys.add(PdfName.F);
        widgetKeys.add(PdfName.BS);
        widgetKeys.add(PdfName.BORDER);
        widgetKeys.add(PdfName.AP);
        widgetKeys.add(PdfName.AS);
        widgetKeys.add(PdfName.C);
        widgetKeys.add(PdfName.A);
        widgetKeys.add(PdfName.STRUCTPARENT);
        widgetKeys.add(PdfName.OC);
        widgetKeys.add(PdfName.H);
        widgetKeys.add(PdfName.MK);
        widgetKeys.add(PdfName.DA);
        widgetKeys.add(PdfName.Q);
        widgetKeys.add(PdfName.P);
        widgetKeys.add(PdfName.TYPE);
        widgetKeys.add(annotId);
        fieldKeys.add(PdfName.AA);
        fieldKeys.add(PdfName.FT);
        fieldKeys.add(PdfName.TU);
        fieldKeys.add(PdfName.TM);
        fieldKeys.add(PdfName.FF);
        fieldKeys.add(PdfName.V);
        fieldKeys.add(PdfName.DV);
        fieldKeys.add(PdfName.DS);
        fieldKeys.add(PdfName.RV);
        fieldKeys.add(PdfName.OPT);
        fieldKeys.add(PdfName.MAXLEN);
        fieldKeys.add(PdfName.TI);
        fieldKeys.add(PdfName.I);
        fieldKeys.add(PdfName.LOCK);
        fieldKeys.add(PdfName.SV);
    }

    static Integer getFlags(PdfDictionary field) {
        PdfName type = field.getAsName(PdfName.FT);
        if (!PdfName.BTN.equals(type))
            return null;
        PdfNumber flags = field.getAsNumber(PdfName.FF);
        if (flags == null)
            return null;
        return flags.intValue();
    }

    static boolean isCheckButton(PdfDictionary field) {
        Integer flags = getFlags(field);
        return flags == null || ((flags.intValue() & PdfFormField.FF_PUSHBUTTON) == 0 && (flags.intValue() & PdfFormField.FF_RADIO) == 0);
    }

    static boolean isRadioButton(PdfDictionary field) {
        Integer flags = getFlags(field);
        return flags != null && (flags.intValue() & PdfFormField.FF_PUSHBUTTON) == 0 && (flags.intValue() & PdfFormField.FF_RADIO) != 0;
    }

    static boolean isTextField(PdfDictionary field) {
        PdfName type = field.getAsName(PdfName.FT);
        return PdfName.TX.equals(type);
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
        if (isTagged())
            throw new RuntimeException(MessageLocalization.getComposedMessage("creating.page.stamp.not.allowed.for.tagged.reader"));
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
