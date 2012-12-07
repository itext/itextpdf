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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import com.itextpdf.text.pdf.interfaces.IPdfStructureElement;

/**
 * This is a node in a document logical structure. It may contain a mark point or it may contain
 * other nodes.
 * @author Paulo Soares
 */
public class PdfStructureElement extends PdfDictionary implements IPdfStructureElement{
    
    /**
     * Holds value of property kids.
     */
    private PdfStructureElement parent;
    private PdfStructureTreeRoot top;

    /**
     * Holds value of property reference.
     */
    private PdfIndirectReference reference;

    private int pageMark;

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
        put(PdfName.TYPE, PdfName.STRUCTELEM);
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
        put(PdfName.TYPE, PdfName.STRUCTELEM);
    }

    protected PdfStructureElement(PdfDictionary parent, PdfName structureType) {
        if (parent instanceof PdfStructureElement) {
            top = ((PdfStructureElement)parent).top;
            init(parent, structureType);
            this.parent = (PdfStructureElement)parent;
            put(PdfName.P, ((PdfStructureElement)parent).reference);
            put(PdfName.TYPE, PdfName.STRUCTELEM);
        } else if (parent instanceof PdfStructureTreeRoot) {
            top = (PdfStructureTreeRoot)parent;
            init(parent, structureType);
            put(PdfName.P, ((PdfStructureTreeRoot)parent).getReference());
            put(PdfName.TYPE, PdfName.STRUCTELEM);
        } else {

        }
    }

    private void init(PdfDictionary parent, PdfName structureType) {
        PdfObject kido = parent.get(PdfName.K);
        PdfArray kids = null;
        if (kido == null) {
            kids = new PdfArray();
            parent.put(PdfName.K, kids);
        } else if (kido instanceof PdfArray) {
            kids = (PdfArray)kido;
        } else {
            kids = new PdfArray();
            kids.add(kido);
            parent.put(PdfName.K, kids);
        }
        if (kids.size() > 0) {
            if (kids.getAsNumber(0) != null)
                kids.remove(0);
            if (kids.size() > 0) {
                PdfDictionary mcr = kids.getAsDict(0);
                if (mcr != null && PdfName.MCR.equals(mcr.getAsName(PdfName.TYPE))) {
                    kids.remove(0);
                }
            }
        }
        kids.add(this);
        put(PdfName.S, structureType);
        reference = top.getWriter().getPdfIndirectReference();
    }

    /**
     * Gets the parent of this node.
     * @return the parent of this node
     */
    public PdfDictionary getParent() {
        return getParent(false);
    }

    public PdfDictionary getParent(boolean includeStructTreeRoot) {
        if (parent == null && includeStructTreeRoot)
            return top;
        else
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
        PdfDictionary parent = getParent();
        if (parent instanceof PdfStructureElement)
            return ((PdfStructureElement) parent).getAttribute(name);
        if (parent instanceof PdfStructureTreeRoot)
            return  ((PdfStructureTreeRoot) parent).getAttribute(name);

        return new PdfNull();
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

    public void writeAttributes(final IAccessibleElement element) {
        if (element instanceof Paragraph) {
            writeAttributes((Paragraph) element);
        } else if (element instanceof Chunk) {
            writeAttributes((Chunk)element);
        } else if (element instanceof Image) {
            writeAttributes((Image)element);
        } else if (element instanceof List) {
            writeAttributes((List)element);
        } else if (element instanceof ListItem) {
            writeAttributes((ListItem)element);
        } else if (element instanceof ListLabel) {
            writeAttributes((ListLabel)element);
        } else if (element instanceof ListBody) {
            writeAttributes((ListBody)element);
        } else if (element instanceof PdfPTable) {
            writeAttributes((PdfPTable)element);
        } else if (element instanceof PdfPRow) {
            writeAttributes((PdfPRow)element);
        } else if (element instanceof PdfPCell) {
            writeAttributes((PdfPCell)element);
        } else if (element instanceof PdfPTableHeader) {
            writeAttributes((PdfPTableHeader)element);
        } else if (element instanceof PdfPTableFooter) {
            writeAttributes((PdfPTableFooter)element);
        } else if (element instanceof PdfPTableBody) {
            writeAttributes((PdfPTableBody)element);
        }
        if (element.getAccessibleAttributes() != null) {
            for (PdfName key : element.getAccessibleAttributes().keySet()) {
                if (key.equals(PdfName.LANG) || key.equals(PdfName.ALT) || key.equals(PdfName.ACTUALTEXT) || key.equals(PdfName.E)) {
                    put(key, element.getAccessibleAttribute(key));
                } else {
                    setAttribute(key, element.getAccessibleAttribute(key));
                }
            }
        }
    }

    private void writeAttributes(final Chunk chunk) {
        if (chunk != null) {
            if (chunk.getImage() != null) {
                writeAttributes(chunk.getImage());
            } else {

            }
        }
    }

    private void writeAttributes(final Image image) {
        if (image != null) {

        }
    }

    private void writeAttributes(final Paragraph paragraph) {
        if (paragraph != null) {
            // Setting non-inheritable attributes
            if ((paragraph.getFont() != null) && (paragraph.getFont().getColor() != null)) {
                BaseColor c = paragraph.getFont().getColor();
                float[] colors = new float[]{(float)c.getRed() / 255f, (float)c.getGreen() / 255f, (float)c.getBlue() / 255f};
                this.setAttribute(PdfName.COLOR, new PdfArray(colors));
            }
            if (Float.compare(paragraph.getSpacingBefore(), 0f) != 0)
                this.setAttribute(PdfName.SPACEBEFORE, new PdfNumber(paragraph.getSpacingBefore()));
            if (Float.compare(paragraph.getSpacingAfter(), 0f) != 0)
                this.setAttribute(PdfName.SPACEAFTER, new PdfNumber(paragraph.getSpacingAfter()));
            if (Float.compare(paragraph.getFirstLineIndent(), 0f) != 0)
                this.setAttribute(PdfName.TEXTINDENT, new PdfNumber(paragraph.getFirstLineIndent()));

            // Setting inheritable attributes
            IPdfStructureElement parent = (IPdfStructureElement) this.getParent(true);
            PdfObject obj = parent.getAttribute(PdfName.STARTINDENT);
            if (obj instanceof PdfNumber) {
                float startIndent = ((PdfNumber) obj).floatValue();
                if (Float.compare(startIndent, paragraph.getIndentationLeft()) != 0)
                    this.setAttribute(PdfName.STARTINDENT, new PdfNumber(paragraph.getIndentationLeft()));
            } else {
                if (Math.abs(paragraph.getIndentationLeft()) > Float.MIN_VALUE)
                    this.setAttribute(PdfName.STARTINDENT, new PdfNumber(paragraph.getIndentationLeft()));
            }

            obj = parent.getAttribute(PdfName.ENDINDENT);
            if (obj instanceof PdfNumber) {
                float endIndent = ((PdfNumber) obj).floatValue();
                if (Float.compare(endIndent, paragraph.getIndentationRight()) != 0)
                    this.setAttribute(PdfName.ENDINDENT, new PdfNumber(paragraph.getIndentationRight()));
            } else {
                if (Float.compare(paragraph.getIndentationRight(), 0) != 0)
                    this.setAttribute(PdfName.ENDINDENT, new PdfNumber(paragraph.getIndentationRight()));
            }

            PdfName align = null;
            switch (paragraph.getAlignment()) {
                case Element.ALIGN_LEFT:
                    align = PdfName.START;
                    break;
                case Element.ALIGN_CENTER:
                    align = PdfName.CENTER;
                    break;
                case Element.ALIGN_RIGHT:
                    align = PdfName.END;
                    break;
                case Element.ALIGN_JUSTIFIED:
                    align = PdfName.JUSTIFY;
                    break;
            }
            obj = parent.getAttribute(PdfName.TEXTALIGN);
            if (obj instanceof PdfName) {
                PdfName textAlign = ((PdfName) obj);
                if (align != null && !textAlign.equals(align))
                    this.setAttribute(PdfName.TEXTALIGN, align);
            } else {
                if (align != null && !PdfName.START.equals(align))
                    this.setAttribute(PdfName.TEXTALIGN, align);
            }
        }
    }

    private void writeAttributes(final List list) {
        if (list != null) {

        }
    }

    private void writeAttributes(final ListItem listItem) {
        if (listItem != null) {

        }
    }

    private void writeAttributes(final ListBody listBody) {
        if (listBody != null) {

        }
    }

    private void writeAttributes(final ListLabel listLabel) {
        if (listLabel != null) {

        }
    }

    private void writeAttributes(final PdfPTable table) {
        if (table != null) {

        }
    }

    private void writeAttributes(final PdfPRow row) {
        if (row != null) {

        }
    }

    private void writeAttributes(final PdfPCell cell) {
        if (cell != null) {

        }
    }

    private void writeAttributes(final PdfPTableHeader header) {
        if (header != null) {

        }
    }

    private void writeAttributes(final PdfPTableBody body) {
        if (body != null) {

        }
    }

    private void writeAttributes(final PdfPTableFooter footer) {
        if (footer != null) {

        }
    }


}
