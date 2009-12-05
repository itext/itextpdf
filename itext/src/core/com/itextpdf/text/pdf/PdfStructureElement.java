/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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

import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * This is a node in a document logical structure. It may contain a mark point or it may contain
 * other nodes.
 * @author Paulo Soares
 */
public class PdfStructureElement extends PdfDictionary {
    
    /**
     * Holds value of property kids.
     */
    private PdfStructureElement parent;
    private PdfStructureTreeRoot top;
    
    /**
     * Holds value of property reference.
     */
    private PdfIndirectReference reference;
    
    /**
     * Creates a new instance of PdfStructureElement.
     * @param parent the parent of this node
     * @param structureType the type of structure. It may be a standard type or a user type mapped by the role map
     */
    public PdfStructureElement(PdfStructureElement parent, PdfName structureType) {
        top = parent.top;
        init(parent, structureType);
        this.parent = parent;
        put(PdfName.P, parent.reference);
    }
    
    /**
     * Creates a new instance of PdfStructureElement.
     * @param parent the parent of this node
     * @param structureType the type of structure. It may be a standard type or a user type mapped by the role map
     */    
    public PdfStructureElement(PdfStructureTreeRoot parent, PdfName structureType) {
        top = parent;
        init(parent, structureType);
        put(PdfName.P, parent.getReference());
    }
    
    private void init(PdfDictionary parent, PdfName structureType) {
        PdfObject kido = parent.get(PdfName.K);
        PdfArray kids = null;
        if (kido != null && !kido.isArray())
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.parent.has.already.another.function"));
        if (kido == null) {
            kids = new PdfArray();
            parent.put(PdfName.K, kids);
        }
        else
            kids = (PdfArray)kido;
        kids.add(this);
        put(PdfName.S, structureType);
        reference = top.getWriter().getPdfIndirectReference();
    }
    
    /**
     * Gets the parent of this node.
     * @return the parent of this node
     */    
    public PdfDictionary getParent() {
        return parent;
    }
    
    void setPageMark(int page, int mark) {
        if (mark >= 0)
            put(PdfName.K, new PdfNumber(mark));
        top.setPageMark(page, reference);
    }
    
    /**
     * Gets the reference this object will be written to.
     * @return the reference this object will be written to
     * @since	2.1.6 method removed in 2.1.5, but restored in 2.1.6
     */    
    public PdfIndirectReference getReference() {
        return this.reference;
    }
}
