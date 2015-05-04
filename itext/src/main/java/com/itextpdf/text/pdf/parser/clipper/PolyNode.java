/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
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
package com.itextpdf.text.pdf.parser.clipper;

import java.util.ArrayList;
import java.util.List;

public class PolyNode {
    private PolyNode parent;
    protected List<IntPoint> polygon = new ArrayList<IntPoint>();
    private int index;
    private JoinType jointype;
    private EndType endtype;
    private List<PolyNode> childs = new ArrayList<PolyNode>();
    private boolean isOpen;
    private boolean isHole;

    private boolean isHoleNode() {
        boolean result = true;
        PolyNode node = parent;
        while (node != null) {
            result = !result;
            node = node.parent;
        }
        return result;
    }

    public int getChildCount() {
        return childs.size();
    }

    public JoinType getJointype() {
        return jointype;
    }

    public void setJointype(JoinType jointype) {
        this.jointype = jointype;
    }

    public EndType getEndtype() {
        return endtype;
    }

    public void setEndtype(EndType endtype) {
        this.endtype = endtype;
    }

    public List<IntPoint> getContour() {
        return polygon;
    }

    protected void addChild(PolyNode child) {
        int cnt = childs.size();
        childs.add(child);
        child.setParent(this);
        child.setIndex(cnt);
    }

    public PolyNode GetNext() {
        if (childs.size() > 0)
            return childs.get(0);
        else
            return GetNextSiblingUp();
    }

    private PolyNode GetNextSiblingUp() {
        if (parent == null)
            return null;
        else if (index == parent.childs.size() - 1)
            return parent.GetNextSiblingUp();
        else
            return parent.childs.get(index + 1);
    }

    public List<PolyNode> getChilds() {
        return childs;
    }

    public PolyNode getParent() {
        return parent;
    }

    public void setParent(PolyNode polyNode) {
        this.parent = polyNode;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean IsHole() {
        return isHoleNode();
    }

    public boolean isHole() {
        return isHole;
    }

    public void setHole(boolean isHole) {
        this.isHole = isHole;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
