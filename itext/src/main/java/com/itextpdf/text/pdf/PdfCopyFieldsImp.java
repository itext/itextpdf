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

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
import com.itextpdf.text.pdf.AcroFields.Item;

/**
 *
 * @author  psoares
 */
@Deprecated
class PdfCopyFieldsImp extends PdfWriter {

    private static final PdfName iTextTag = new PdfName("_iTextTag_");
    private static final Integer zero = Integer.valueOf(0);
    ArrayList<PdfReader> readers = new ArrayList<PdfReader>();
    HashMap<PdfReader, IntHashtable> readers2intrefs = new HashMap<PdfReader, IntHashtable>();
    HashMap<PdfReader, IntHashtable> pages2intrefs = new HashMap<PdfReader, IntHashtable>();
    HashMap<PdfReader, IntHashtable> visited = new HashMap<PdfReader, IntHashtable>();
    ArrayList<AcroFields> fields = new ArrayList<AcroFields>();
    RandomAccessFileOrArray file;
    HashMap<String, Object> fieldTree = new HashMap<String, Object>();
    ArrayList<PdfIndirectReference> pageRefs = new ArrayList<PdfIndirectReference>();
    ArrayList<PdfDictionary> pageDics = new ArrayList<PdfDictionary>();
    PdfDictionary resources = new PdfDictionary();
    PdfDictionary form;
    boolean closing = false;
    Document nd;
    private HashMap<PdfArray, ArrayList<Integer>> tabOrder;
    private ArrayList<String> calculationOrder = new ArrayList<String>();
    private ArrayList<Object> calculationOrderRefs;
    private boolean hasSignature;
    private boolean needAppearances = false;
    private HashSet<Object> mergedRadioButtons = new HashSet<Object>();



    protected Counter COUNTER = CounterFactory.getCounter(PdfCopyFields.class);
    protected Counter getCounter() {
    	return COUNTER;
    }
    
    PdfCopyFieldsImp(OutputStream os) throws DocumentException {
        this(os, '\0');
    }

    PdfCopyFieldsImp(OutputStream os, char pdfVersion) throws DocumentException {
        super(new PdfDocument(), os);
        pdf.addWriter(this);
        if (pdfVersion != 0)
            super.setPdfVersion(pdfVersion);
        nd = new Document();
        nd.addDocListener(pdf);
    }

    void addDocument(PdfReader reader, List<Integer> pagesToKeep) throws DocumentException, IOException {
        if (!readers2intrefs.containsKey(reader) && reader.isTampered())
            throw new DocumentException(MessageLocalization.getComposedMessage("the.document.was.reused"));
        reader = new PdfReader(reader);
        reader.selectPages(pagesToKeep);
        if (reader.getNumberOfPages() == 0)
            return;
        reader.setTampered(false);
        addDocument(reader);
    }

    void addDocument(PdfReader reader) throws DocumentException, IOException {
        if (!reader.isOpenedWithFullPermissions())
            throw new BadPasswordException(MessageLocalization.getComposedMessage("pdfreader.not.opened.with.owner.password"));
        openDoc();
        if (readers2intrefs.containsKey(reader)) {
            reader = new PdfReader(reader);
        }
        else {
            if (reader.isTampered())
                throw new DocumentException(MessageLocalization.getComposedMessage("the.document.was.reused"));
            reader.consolidateNamedDestinations();
            reader.setTampered(true);
        }
        reader.shuffleSubsetNames();
        readers2intrefs.put(reader, new IntHashtable());
        readers.add(reader);
        int len = reader.getNumberOfPages();
        IntHashtable refs = new IntHashtable();
        for (int p = 1; p <= len; ++p) {
            refs.put(reader.getPageOrigRef(p).getNumber(), 1);
            reader.releasePage(p);
        }
        pages2intrefs.put(reader, refs);
        visited.put(reader, new IntHashtable());
        AcroFields acro = reader.getAcroFields();
        // when a document with NeedAppearances is encountered, the flag is set
        // in the resulting document.
        boolean needapp = !acro.isGenerateAppearances();
        if (needapp)
            needAppearances = true;
        fields.add(acro);
        updateCalculationOrder(reader);
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
            name = name.substring(0, name.length() - 1);
        return name;
    }

    /**
     * @since	2.1.5; before 2.1.5 the method was private
     */
    protected void updateCalculationOrder(PdfReader reader) {
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
            String name = getCOName(reader, (PRIndirectReference)obj);
            if (af.getFieldItem(name) == null)
                continue;
            name = "." + name;
            if (calculationOrder.contains(name))
                continue;
            calculationOrder.add(name);
        }
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
                for (PdfName key: dic.getKeys()) {
                    if (restricted && (key.equals(PdfName.PARENT) || key.equals(PdfName.KIDS)))
                        continue;
                    PdfObject ob = dic.get(key);
                    if (ob != null && ob.isIndirect()) {
                        PRIndirectReference ind = (PRIndirectReference)ob;
                        if (!setVisited(ind) && !isPage(ind)) {
                            PdfIndirectReference ref = getNewReference(ind);
                            propagate(PdfReader.getPdfObjectRelease(ind), ref, restricted);
                        }
                    }
                    else
                        propagate(ob, null, restricted);
                }
                break;
            }
            case PdfObject.ARRAY: {
                //PdfArray arr = new PdfArray();
                for (Iterator<PdfObject> it = ((PdfArray)obj).listIterator(); it.hasNext();) {
                    PdfObject ob = it.next();
                    if (ob != null && ob.isIndirect()) {
                        PRIndirectReference ind = (PRIndirectReference)ob;
                        if (!isVisited(ind) && !isPage(ind)) {
                            PdfIndirectReference ref = getNewReference(ind);
                            propagate(PdfReader.getPdfObjectRelease(ind), ref, restricted);
                        }
                    }
                    else
                        propagate(ob, null, restricted);
                }
                break;
            }
            case PdfObject.INDIRECT: {
                throw new RuntimeException(MessageLocalization.getComposedMessage("reference.pointing.to.reference"));
            }
        }
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

    @SuppressWarnings("unchecked")
    protected PdfArray branchForm(HashMap<String, Object> level, PdfIndirectReference parent, String fname) throws IOException {
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
                dic.put(PdfName.KIDS, branchForm((HashMap<String, Object>)obj, ind, fname2));
                arr.add(ind);
                addToBody(dic, ind);
            }
            else {
                ArrayList<Object> list = (ArrayList<Object>)obj;
                dic.mergeDifferent((PdfDictionary)list.get(0));
                if (list.size() == 3) {
                    dic.mergeDifferent((PdfDictionary)list.get(2));
                    int page = ((Integer)list.get(1)).intValue();
                    PdfDictionary pageDic = pageDics.get(page - 1);
                    PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
                    if (annots == null) {
                        annots = new PdfArray();
                        pageDic.put(PdfName.ANNOTS, annots);
                    }
                    PdfNumber nn = (PdfNumber)dic.get(iTextTag);
                    dic.remove(iTextTag);
                    adjustTabOrder(annots, ind, nn);
                }
                else {
                    PdfDictionary field = (PdfDictionary)list.get(0);
                    PdfName v = field.getAsName(PdfName.V);
                    PdfArray kids = new PdfArray();
                    for (int k = 1; k < list.size(); k += 2) {
                        int page = ((Integer)list.get(k)).intValue();
                        PdfDictionary pageDic = pageDics.get(page - 1);
                        PdfArray annots = pageDic.getAsArray(PdfName.ANNOTS);
                        if (annots == null) {
                            annots = new PdfArray();
                            pageDic.put(PdfName.ANNOTS, annots);
                        }
                        PdfDictionary widget = new PdfDictionary();
                        widget.merge((PdfDictionary)list.get(k + 1));
                        widget.put(PdfName.PARENT, ind);
                        PdfNumber nn = (PdfNumber)widget.get(iTextTag);
                        widget.remove(iTextTag);
                        if (PdfCopy.isCheckButton(field)) {
                            PdfName as = widget.getAsName(PdfName.AS);
                            if (v != null && as != null)
                                widget.put(PdfName.AS, v);
                        } else if (PdfCopy.isRadioButton(field)) {
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
                        PdfIndirectReference wref = addToBody(widget).getIndirectReference();
                        adjustTabOrder(annots, wref, nn);
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

    protected PdfName getOffStateName(PdfDictionary widget) {
        return PdfName.Off;
    }

    protected void createAcroForms() throws IOException {
        if (fieldTree.isEmpty())
            return;
        form = new PdfDictionary();
        form.put(PdfName.DR, resources);
        propagate(resources, null, false);
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
    }

    @Override
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

    /**
     * Creates the new PDF by merging the fields and forms.
     */
    protected void closeIt() throws IOException {
        for (int k = 0; k < readers.size(); ++k) {
            readers.get(k).removeFields();
        }
        for (int r = 0; r < readers.size(); ++r) {
            PdfReader reader = readers.get(r);
            for (int page = 1; page <= reader.getNumberOfPages(); ++page) {
                pageRefs.add(getNewReference(reader.getPageOrigRef(page)));
                pageDics.add(reader.getPageN(page));
            }
        }
        mergeFields();
        createAcroForms();
        for (int r = 0; r < readers.size(); ++r) {
                PdfReader reader = readers.get(r);
                for (int page = 1; page <= reader.getNumberOfPages(); ++page) {
                    PdfDictionary dic = reader.getPageN(page);
                    PdfIndirectReference pageRef = getNewReference(reader.getPageOrigRef(page));
                    PdfIndirectReference parent = root.addPageRef(pageRef);
                    dic.put(PdfName.PARENT, parent);
                    propagate(dic, pageRef, false);
                }
        }
        for (Map.Entry<PdfReader, IntHashtable>entry: readers2intrefs.entrySet()) {
            PdfReader reader = entry.getKey();
            try {
                file = reader.getSafeFile();
                file.reOpen();
                IntHashtable t = entry.getValue();
                int keys[] = t.toOrderedKeys();
                for (int k = 0; k < keys.length; ++k) {
                    PRIndirectReference ref = new PRIndirectReference(reader, keys[k]);
                    addToBody(PdfReader.getPdfObjectRelease(ref), t.get(keys[k]));
                }
            }
            finally {
                try {
                    file.close();
// TODO: Removed - the user should be responsible for closing all PdfReaders.  But, this could cause a lot of memory leaks in code out there that hasn't been properly closing things - maybe add a finalizer to PdfReader that calls PdfReader#close() ??            	
//                    reader.close();
                }
                catch (Exception e) {
                    // empty on purpose
                }
            }
        }
        pdf.close();
    }

    void addPageOffsetToField(Map<String, AcroFields.Item> fd, int pageOffset) {
        if (pageOffset == 0)
            return;
        for (AcroFields.Item item: fd.values()) {
            for (int k = 0; k < item.size(); ++k) {
                int p = item.getPage(k).intValue();
                item.forcePage(k, p + pageOffset);
            }
        }
    }

    void createWidgets(ArrayList<Object> list, AcroFields.Item item) {
        for (int k = 0; k < item.size(); ++k) {
            list.add(item.getPage(k));
            PdfDictionary merged = item.getMerged(k);
            PdfObject dr = merged.get(PdfName.DR);
            if (dr != null)
                PdfFormField.mergeResources(resources, (PdfDictionary)PdfReader.getPdfObject(dr));
            PdfDictionary widget = new PdfDictionary();
            for (Object element : merged.getKeys()) {
                PdfName key = (PdfName)element;
                if (widgetKeys.containsKey(key))
                    widget.put(key, merged.get(key));
            }
            widget.put(iTextTag, new PdfNumber(item.getTabOrder(k).intValue() + 1));
            list.add(widget);
        }
    }

    @SuppressWarnings("unchecked")
    void mergeField(String name, AcroFields.Item item) {
        HashMap<String, Object> map = fieldTree;
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
                        if (fieldKeys.containsKey(key))
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

    void mergeWithMaster(Map<String, Item> fd) {
        for (Map.Entry<String, Item> entry: fd.entrySet()) {
            String name = entry.getKey();
            mergeField(name, entry.getValue());
        }
    }

    void mergeFields() {
        int pageOffset = 0;
        for (int k = 0; k < fields.size(); ++k) {
            Map<String, Item> fd = fields.get(k).getFields();
            addPageOffsetToField(fd, pageOffset);
            mergeWithMaster(fd);
            pageOffset += readers.get(k).getNumberOfPages();
        }
    }

    @Override
    public PdfIndirectReference getPageReference(int page) {
        return pageRefs.get(page - 1);
    }

    @Override
    protected PdfDictionary getCatalog(PdfIndirectReference rootObj) {
        try {
            PdfDictionary cat = pdf.getCatalog(rootObj);
            if (form != null) {
                PdfIndirectReference ref = addToBody(form).getIndirectReference();
                cat.put(PdfName.ACROFORM, ref);
            }
            return cat;
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    protected PdfIndirectReference getNewReference(PRIndirectReference ref) {
        return new PdfIndirectReference(0, getNewObjectNumber(ref.getReader(), ref.getNumber(), 0));
    }

    @Override
    protected int getNewObjectNumber(PdfReader reader, int number, int generation) {
        IntHashtable refs = readers2intrefs.get(reader);
        int n = refs.get(number);
        if (n == 0) {
            n = getIndirectReferenceNumber();
            refs.put(number, n);
        }
        return n;
    }


    /**
     * Sets a reference to "visited" in the copy process.
     * @param	ref	the reference that needs to be set to "visited"
     * @return	true if the reference was set to visited
     */
    protected boolean setVisited(PRIndirectReference ref) {
        IntHashtable refs = visited.get(ref.getReader());
        if (refs != null)
        	return refs.put(ref.getNumber(), 1) != 0;
        else
        	return false;
    }

    /**
     * Checks if a reference has already been "visited" in the copy process.
     * @param	ref	the reference that needs to be checked
     * @return	true if the reference was already visited
     */
    protected boolean isVisited(PRIndirectReference ref) {
        IntHashtable refs = visited.get(ref.getReader());
        if (refs != null)
        	return refs.containsKey(ref.getNumber());
        else
        	return false;
    }

    protected boolean isVisited(PdfReader reader, int number, int generation) {
        IntHashtable refs = readers2intrefs.get(reader);
        return refs.containsKey(number);
    }

    /**
     * Checks if a reference refers to a page object.
     * @param	ref	the reference that needs to be checked
     * @return	true is the reference refers to a page object.
     */
    protected boolean isPage(PRIndirectReference ref) {
        IntHashtable refs = pages2intrefs.get(ref.getReader());
        if (refs != null)
        	return refs.containsKey(ref.getNumber());
        else
        	return false;
    }

    @Override
    RandomAccessFileOrArray getReaderFile(PdfReader reader) {
            return file;
    }

    public void openDoc() {
        if (!nd.isOpen())
            nd.open();
    }

    protected static final HashMap<PdfName, Integer> widgetKeys = new HashMap<PdfName, Integer>();
    protected static final HashMap<PdfName, Integer> fieldKeys = new HashMap<PdfName, Integer>();
    static {
        Integer one = Integer.valueOf(1);
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
        widgetKeys.put(PdfName.P, one);
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
