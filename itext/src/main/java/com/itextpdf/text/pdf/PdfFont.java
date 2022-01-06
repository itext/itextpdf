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

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;

/**
 * <CODE>PdfFont</CODE> is the Pdf Font object.
 * <P>
 * Limitation: in this class only base 14 Type 1 fonts (courier, courier bold, courier oblique,
 * courier boldoblique, helvetica, helvetica bold, helvetica oblique, helvetica boldoblique,
 * symbol, times roman, times bold, times italic, times bolditalic, zapfdingbats) and their
 * standard encoding (standard, MacRoman, (MacExpert,) WinAnsi) are supported.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.7 (page 198-203).
 *
 * @see		PdfName
 * @see		PdfDictionary
 * @see		BadPdfFormatException
 */

class PdfFont implements Comparable<PdfFont> {


    /** the font metrics. */
    private BaseFont font;

    /** the size. */
    private float size;

    protected float hScale = 1;

    // constructors

    PdfFont(BaseFont bf, float size) {
        this.size = size;
        font = bf;
    }

    // methods

    /**
     * Compares this <CODE>PdfFont</CODE> with another
     *
     * @param	pdfFont	the other <CODE>PdfFont</CODE>
     * @return	a value
     */

    public int compareTo(PdfFont pdfFont) {
        if (pdfFont == null) {
            return -1;
        }
        try {
            if (font != pdfFont.font) {
                return 1;
            }
            if (this.size() != pdfFont.size()) {
                return 2;
            }
            return 0;
        }
        catch(ClassCastException cce) {
            return -2;
        }
    }

    /**
     * Returns the size of this font.
     *
     * @return		a size
     */

    float size() {
        return size;
    }

    /**
     * Returns the approximative width of 1 character of this font.
     *
     * @return		a width in Text Space
     */

    float width() {
        return width(' ');
    }

    /**
     * Returns the width of a certain character of this font.
     *
     * @param		character	a certain character
     * @return		a width in Text Space
     */

    float width(int character) {
        return font.getWidthPoint(character, size) * hScale;
    }

    float width(String s) {
        return font.getWidthPoint(s, size) * hScale;
    }

    BaseFont getFont() {
        return font;
    }

    static PdfFont getDefaultFont() {
        try {
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false);
            return new PdfFont(bf, 12);
        }
        catch (Exception ee) {
            throw new ExceptionConverter(ee);
        }
    }
    void setHorizontalScaling(float hScale) {
        this.hScale = hScale;
    }
    /**
     * Getter for the horizontal scaling.
     * @since iText 5.1.0
     */
    float getHorizontalScaling() {
    	return hScale;
    }
}
