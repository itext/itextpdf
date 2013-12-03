/*
 * $Id: PdfPages.java 6077 2013-11-20 14:23:48Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
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
import java.util.ArrayList;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * <CODE>PdfPages</CODE> is the PDF Pages-object.
 * <P>
 * The Pages of a document are accessible through a tree of nodes known as the Pages tree.
 * This tree defines the ordering of the pages in the document.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 6.3 (page 71-73)
 *
 * @see		PdfPage
 */

public class PdfPages {

    private ArrayList<PdfIndirectReference> pages = new ArrayList<PdfIndirectReference>();
    private ArrayList<PdfIndirectReference> parents = new ArrayList<PdfIndirectReference>();
    private int leafSize = 10;
    private PdfWriter writer;
    private PdfIndirectReference topParent;

    // constructors

/**
 * Constructs a <CODE>PdfPages</CODE>-object.
 */

    PdfPages(PdfWriter writer) {
        this.writer = writer;
    }

    void addPage(PdfDictionary page) {
        try {
            if (pages.size() % leafSize == 0)
                parents.add(writer.getPdfIndirectReference());
            PdfIndirectReference parent = parents.get(parents.size() - 1);
            page.put(PdfName.PARENT, parent);
            PdfIndirectReference current = writer.getCurrentPage();
            writer.addToBody(page, current);
            pages.add(current);
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    PdfIndirectReference addPageRef(PdfIndirectReference pageRef) {
        try {
            if (pages.size() % leafSize == 0)
                parents.add(writer.getPdfIndirectReference());
            pages.add(pageRef);
            return parents.get(parents.size() - 1);
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    // returns the top parent to include in the catalog
    PdfIndirectReference writePageTree() throws IOException {
        if (pages.isEmpty())
            throw new IOException(MessageLocalization.getComposedMessage("the.document.has.no.pages"));
        int leaf = 1;
        ArrayList<PdfIndirectReference> tParents = parents;
        ArrayList<PdfIndirectReference> tPages = pages;
        ArrayList<PdfIndirectReference> nextParents = new ArrayList<PdfIndirectReference>();
        while (true) {
            leaf *= leafSize;
            int stdCount = leafSize;
            int rightCount = tPages.size() % leafSize;
            if (rightCount == 0)
                rightCount = leafSize;
            for (int p = 0; p < tParents.size(); ++p) {
                int count;
                int thisLeaf = leaf;
                if (p == tParents.size() - 1) {
                    count = rightCount;
                    thisLeaf = pages.size() % leaf;
                    if (thisLeaf == 0)
                        thisLeaf = leaf;
                }
                else
                    count = stdCount;
                PdfDictionary top = new PdfDictionary(PdfName.PAGES);
                top.put(PdfName.COUNT, new PdfNumber(thisLeaf));
                PdfArray kids = new PdfArray();
                ArrayList<PdfObject> internal = kids.getArrayList();
                internal.addAll(tPages.subList(p * stdCount, p * stdCount + count));
                top.put(PdfName.KIDS, kids);
                if (tParents.size() > 1) {
                    if (p % leafSize == 0)
                        nextParents.add(writer.getPdfIndirectReference());
                    top.put(PdfName.PARENT, nextParents.get(p / leafSize));
                }
                writer.addToBody(top, tParents.get(p));
            }
            if (tParents.size() == 1) {
                topParent = tParents.get(0);
                return topParent;
            }
            tPages = tParents;
            tParents = nextParents;
            nextParents = new ArrayList<PdfIndirectReference>();
        }
    }

    PdfIndirectReference getTopParent() {
        return topParent;
    }

    void setLinearMode(PdfIndirectReference topParent) {
        if (parents.size() > 1)
            throw new RuntimeException(MessageLocalization.getComposedMessage("linear.page.mode.can.only.be.called.with.a.single.parent"));
        if (topParent != null) {
            this.topParent = topParent;
            parents.clear();
            parents.add(topParent);
        }
        leafSize = 10000000;
    }

    void addPage(PdfIndirectReference page) {
        pages.add(page);
    }

    int reorderPages(int order[]) throws DocumentException {
        if (order == null)
            return pages.size();
        if (parents.size() > 1)
            throw new DocumentException(MessageLocalization.getComposedMessage("page.reordering.requires.a.single.parent.in.the.page.tree.call.pdfwriter.setlinearmode.after.open"));
        if (order.length != pages.size())
            throw new DocumentException(MessageLocalization.getComposedMessage("page.reordering.requires.an.array.with.the.same.size.as.the.number.of.pages"));
        int max = pages.size();
        boolean temp[] = new boolean[max];
        for (int k = 0; k < max; ++k) {
            int p = order[k];
            if (p < 1 || p > max)
                throw new DocumentException(MessageLocalization.getComposedMessage("page.reordering.requires.pages.between.1.and.1.found.2", String.valueOf(max), String.valueOf(p)));
            if (temp[p - 1])
                throw new DocumentException(MessageLocalization.getComposedMessage("page.reordering.requires.no.page.repetition.page.1.is.repeated", p));
            temp[p - 1] = true;
        }
        PdfIndirectReference copy[] = pages.toArray(new PdfIndirectReference[pages.size()]);
        for (int k = 0; k < max; ++k) {
            pages.set(k, copy[order[k] - 1]);
        }
        return max;
    }
}
