/*
 * $Id: PdfBorderDictionary.java 6134 2013-12-23 13:15:14Z blowagie $
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

import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * A <CODE>PdfBorderDictionary</CODE> define the appearance of a Border (Annotations).
 *
 * @see		PdfDictionary
 */

public class PdfBorderDictionary extends PdfDictionary {
    
    public static final int STYLE_SOLID = 0;
    public static final int STYLE_DASHED = 1;
    public static final int STYLE_BEVELED = 2;
    public static final int STYLE_INSET = 3;
    public static final int STYLE_UNDERLINE = 4;
    // constructors
    
/**
 * Constructs a <CODE>PdfBorderDictionary</CODE>.
 */
    
    public PdfBorderDictionary(float borderWidth, int borderStyle, PdfDashPattern dashes) {
        put(PdfName.W, new PdfNumber(borderWidth));
        switch (borderStyle) {
            case STYLE_SOLID:
                put(PdfName.S, PdfName.S);
                break;
            case STYLE_DASHED:
                if (dashes != null)
                    put(PdfName.D, dashes);
                put(PdfName.S, PdfName.D);
                break;
            case STYLE_BEVELED:
                put(PdfName.S, PdfName.B);
                break;
            case STYLE_INSET:
                put(PdfName.S, PdfName.I);
                break;
            case STYLE_UNDERLINE:
                put(PdfName.S, PdfName.U);
                break;
            default:
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.border.style"));
        }
    }
    
    public PdfBorderDictionary(float borderWidth, int borderStyle) {
        this(borderWidth, borderStyle, null);
    }
}
