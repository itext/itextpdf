/*
 * $Id: PdfStructureTreeRoot.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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

import com.itextpdf.text.pdf.interfaces.IPdfStructureElement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The structure tree root corresponds to the highest hierarchy level in a tagged PDF.
 * @author Paulo Soares
 */
public class PdfStructureTreeRoot extends PdfDictionary implements IPdfStructureElement {

    private HashMap<Integer, PdfObject> parentTree = new HashMap<Integer, PdfObject>();
    private PdfIndirectReference reference;
    private PdfDictionary classMap = null;
    protected HashMap<PdfName,PdfObject> classes = null;
    private HashMap<Integer, PdfIndirectReference> numTree = null;

    /**
     * Holds value of property writer.
     */
    private PdfWriter writer;

    /** Creates a new instance of PdfStructureTreeRoot */
    PdfStructureTreeRoot(PdfWriter writer) {
        super(PdfName.STRUCTTREEROOT);
        this.writer = writer;
        reference = writer.getPdfIndirectReference();
    }

    private void createNumTree() throws IOException {
        if (numTree != null) return;
        numTree = new HashMap<Integer, PdfIndirectReference>();
        for (Integer i: parentTree.keySet()) {
            PdfObject obj = parentTree.get(i);
            if (obj.isArray()) {
                PdfArray ar = (PdfArray)obj;
                numTree.put(i, writer.addToBody(ar).getIndirectReference());
            } else if (obj instanceof PdfIndirectReference) {
                numTree.put(i, (PdfIndirectReference)obj);
            }
        }
    }

    /**
     * Maps the user tags to the standard tags. The mapping will allow a standard application to make some sense of the tagged
     * document whatever the user tags may be.
     * @param used the user tag
     * @param standard the standard tag
     */
    public void mapRole(PdfName used, PdfName standard) {
        PdfDictionary rm = (PdfDictionary)get(PdfName.ROLEMAP);
        if (rm == null) {
            rm = new PdfDictionary();
            put(PdfName.ROLEMAP, rm);
        }
        rm.put(used, standard);
    }

    public void mapClass(PdfName name, PdfObject object) {
        if (classMap == null) {
            classMap = new PdfDictionary();
            classes = new HashMap<PdfName, PdfObject>();
        }
        classes.put(name,object);
    }

    public PdfObject getMappedClass(PdfName name) {
        if (classes == null)
            return null;
        return classes.get(name);
    }

    /**
     * Gets the writer.
     * @return the writer
     */
    public PdfWriter getWriter() {
        return this.writer;
    }

    public HashMap<Integer, PdfIndirectReference> getNumTree() throws IOException {
        if (numTree == null) createNumTree();
        return numTree;
    }

    /**
     * Gets the reference this object will be written to.
     * @return the reference this object will be written to
     * @since	2.1.6 method removed in 2.1.5, but restored in 2.1.6
     */
    public PdfIndirectReference getReference() {
        return this.reference;
    }

    void setPageMark(int page, PdfIndirectReference struc) {
        Integer i = Integer.valueOf(page);
        PdfArray ar = (PdfArray)parentTree.get(i);
        if (ar == null) {
            ar = new PdfArray();
            parentTree.put(i, ar);
        }
        ar.add(struc);
    }

    void setAnnotationMark(int structParentIndex, PdfIndirectReference struc) {
        parentTree.put(Integer.valueOf(structParentIndex), struc);
    }

    private void nodeProcess(PdfDictionary struc, PdfIndirectReference reference) throws IOException {
        PdfObject obj = struc.get(PdfName.K);
        if (obj != null && obj.isArray()) {
            PdfArray ar = (PdfArray)obj;
            for (int k = 0; k < ar.size(); ++k) {
                PdfDictionary dictionary = ar.getAsDict(k);
                if (dictionary == null)
                    continue;
                if (!PdfName.STRUCTELEM.equals(dictionary.get(PdfName.TYPE)))
                    continue;
                if (ar.getPdfObject(k) instanceof PdfStructureElement) {
                    PdfStructureElement e = (PdfStructureElement) dictionary;
                    ar.set(k, e.getReference());
                    nodeProcess(e, e.getReference());
                }
            }
        }
        if (reference != null)
            writer.addToBody(struc, reference);
    }

    void buildTree() throws IOException {
        createNumTree();
        PdfDictionary dicTree = PdfNumberTree.writeTree(numTree, writer);
        if (dicTree != null)
            put(PdfName.PARENTTREE, writer.addToBody(dicTree).getIndirectReference());
        if (classMap != null && !classes.isEmpty()) {
            for (Map.Entry<PdfName,PdfObject> entry : classes.entrySet()) {
                PdfObject value = entry.getValue();
                if (value.isDictionary())
                    classMap.put(entry.getKey(), writer.addToBody(value).getIndirectReference());
                else if (value.isArray()) {
                    PdfArray newArray = new PdfArray();
                    PdfArray array = (PdfArray)value;
                    for (int i = 0; i < array.size(); ++i) {
                        if (array.getPdfObject(i).isDictionary())
                            newArray.add(writer.addToBody(array.getAsDict(i)).getIndirectReference());
                    }
                    classMap.put(entry.getKey(),newArray);
                }
            }
            put(PdfName.CLASSMAP, writer.addToBody(classMap).getIndirectReference());
        }
        nodeProcess(this, reference);
    }

    /**
     * Gets the first entarance of attribute.
     * @returns PdfObject
     * @since 5.3.4
     */
    public PdfObject getAttribute(PdfName name){
        PdfDictionary attr = getAsDict(PdfName.A);
        if (attr != null){
            if (attr.contains(name))
                return attr.get(name);
        }
        return null;
    }

    /**
     * Sets the attribute value.
     * @since 5.3.4
     */
    public void setAttribute(PdfName name, PdfObject obj){
        PdfDictionary attr = getAsDict(PdfName.A);
        if (attr == null){
            attr = new PdfDictionary();
            put(PdfName.A, attr);
        }
        attr.put(name, obj);
    }
}
