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

import java.io.IOException;
import java.util.HashMap;

/**
 * The structure tree root corresponds to the highest hierarchy level in a tagged PDF.
 * @author Paulo Soares
 */
public class PdfStructureTreeRoot extends PdfDictionary {

    private HashMap<Integer, PdfObject> parentTree = new HashMap<Integer, PdfObject>();
    private PdfIndirectReference reference;

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

    /**
     * Gets the writer.
     * @return the writer
     */
    public PdfWriter getWriter() {
        return this.writer;
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

    private void nodeProcess(PdfDictionary struc, PdfIndirectReference reference) throws IOException {
        PdfObject obj = struc.get(PdfName.K);
        if (obj != null && obj.isArray() && !(((PdfArray)obj).getPdfObject(0)).isNumber()) {
            PdfArray ar = (PdfArray)obj;
            for (int k = 0; k < ar.size(); ++k) {
                PdfStructureElement e = (PdfStructureElement)ar.getAsDict(k);
                ar.set(k, e.getReference());
                nodeProcess(e, e.getReference());
            }
        }
        if (reference != null)
            writer.addToBody(struc, reference);
    }

    void buildTree() throws IOException {
        HashMap<Integer, PdfIndirectReference> numTree = new HashMap<Integer, PdfIndirectReference>();
        for (Integer i: parentTree.keySet()) {
            PdfArray ar = (PdfArray)parentTree.get(i);
            numTree.put(i, writer.addToBody(ar).getIndirectReference());
        }
        PdfDictionary dicTree = PdfNumberTree.writeTree(numTree, writer);
        if (dicTree != null)
            put(PdfName.PARENTTREE, writer.addToBody(dicTree).getIndirectReference());

        nodeProcess(this, reference);
    }
}
