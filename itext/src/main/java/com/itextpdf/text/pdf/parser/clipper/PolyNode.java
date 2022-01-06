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
package com.itextpdf.text.pdf.parser.clipper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.itextpdf.text.pdf.parser.clipper.Clipper.EndType;
import com.itextpdf.text.pdf.parser.clipper.Clipper.JoinType;
import com.itextpdf.text.pdf.parser.clipper.Point.LongPoint;

public class PolyNode {
    enum NodeType {
        ANY, OPEN, CLOSED
    }

    private PolyNode parent;
    private final Path polygon = new Path();
    private int index;
    private JoinType joinType;
    private EndType endType;
    protected final List<PolyNode> childs = new ArrayList<PolyNode>();
    private boolean isOpen;

    public void addChild( PolyNode child ) {
        final int cnt = childs.size();
        childs.add( child );
        child.parent = this;
        child.index = cnt;
    }

    public int getChildCount() {
        return childs.size();
    }

    public List<PolyNode> getChilds() {
        return Collections.unmodifiableList( childs );
    }

    public List<LongPoint> getContour() {
        return polygon;
    }

    public EndType getEndType() {
        return endType;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public PolyNode getNext() {
        if (!childs.isEmpty()) {
            return childs.get( 0 );
        }
        else {
            return getNextSiblingUp();
        }
    }

    private PolyNode getNextSiblingUp() {
        if (parent == null) {
            return null;
        }
        else if (index == parent.childs.size() - 1) {
            return parent.getNextSiblingUp();
        }
        else {
            return parent.childs.get( index + 1 );
        }
    }

    public PolyNode getParent() {
        return parent;
    }

    public Path getPolygon() {
        return polygon;
    }

    public boolean isHole() {
        return isHoleNode();
    }

    private boolean isHoleNode() {
        boolean result = true;
        PolyNode node = parent;
        while (node != null) {
            result = !result;
            node = node.parent;
        }
        return result;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setEndType( EndType value ) {
        endType = value;
    }

    public void setJoinType( JoinType value ) {
        joinType = value;
    }

    public void setOpen( boolean isOpen ) {
        this.isOpen = isOpen;
    }

    public void setParent( PolyNode n ) {
        parent = n;

    }

}
