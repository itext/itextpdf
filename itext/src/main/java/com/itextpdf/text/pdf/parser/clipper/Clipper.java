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

import com.itextpdf.text.pdf.parser.clipper.Point.LongPoint;

public interface Clipper {
    public enum ClipType {
        INTERSECTION, UNION, DIFFERENCE, XOR
    }

    enum Direction {
        RIGHT_TO_LEFT, LEFT_TO_RIGHT
    };

    public enum EndType {
        CLOSED_POLYGON, CLOSED_LINE, OPEN_BUTT, OPEN_SQUARE, OPEN_ROUND
    };

    public enum JoinType {
        BEVEL, ROUND, MITER
    };

    public enum PolyFillType {
        EVEN_ODD, NON_ZERO, POSITIVE, NEGATIVE
    };

    public enum PolyType {
        SUBJECT, CLIP
    };

    public interface ZFillCallback {
        void zFill( LongPoint bot1, LongPoint top1, LongPoint bot2, LongPoint top2, LongPoint pt );
    };

    //InitOptions that can be passed to the constructor ...
    public final static int REVERSE_SOLUTION = 1;

    public final static int STRICTLY_SIMPLE = 2;

    public final static int PRESERVE_COLINEAR = 4;

    boolean addPath( Path pg, PolyType polyType, boolean Closed );

    boolean addPaths( Paths ppg, PolyType polyType, boolean closed );

    void clear();

    boolean execute( ClipType clipType, Paths solution );

    boolean execute( ClipType clipType, Paths solution, PolyFillType subjFillType, PolyFillType clipFillType );

    boolean execute( ClipType clipType, PolyTree polytree );

    public boolean execute( ClipType clipType, PolyTree polytree, PolyFillType subjFillType, PolyFillType clipFillType );
}
