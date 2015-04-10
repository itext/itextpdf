/*
 * $Id: ColorDetails.java 6379 2014-05-16 10:12:59Z eugenemark $
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

/** Each colorSpace in the document will have an instance of this class
 *
 * @author Phillip Pan (phillip@formstar.com)
 */
class ColorDetails {

    /** The indirect reference to this color
     */
    PdfIndirectReference indirectReference;
    /** The color name that appears in the document body stream
     */
    PdfName colorSpaceName;
    /** The color
     */
    ICachedColorSpace colorSpace;

    /** Each spot color used in a document has an instance of this class.
     * @param colorName the color name
     * @param indirectReference the indirect reference to the font
     * @param scolor the <CODE>PDfSpotColor</CODE>
     */
    ColorDetails(PdfName colorName, PdfIndirectReference indirectReference, ICachedColorSpace scolor) {
        this.colorSpaceName = colorName;
        this.indirectReference = indirectReference;
        this.colorSpace = scolor;
    }

    /** Gets the indirect reference to this color.
     * @return the indirect reference to this color
     */
    public PdfIndirectReference getIndirectReference() {
        return indirectReference;
    }

    /** Gets the color name as it appears in the document body.
     * @return the color name
     */
    PdfName getColorSpaceName() {
        return colorSpaceName;
    }

    /** Gets the <CODE>SpotColor</CODE> object.
     * @return the <CODE>PdfSpotColor</CODE>
     */
    public PdfObject getPdfObject(PdfWriter writer) {
        return colorSpace.getPdfObject(writer);
    }
}
