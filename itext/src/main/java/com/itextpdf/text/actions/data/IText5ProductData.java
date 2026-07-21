/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2026 iText Group NV
 * Authors: Bruno Lowagie, et al.
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
package com.itextpdf.text.actions.data;

import com.itextpdf.commons.actions.data.ProductData;

/**
 * Stores an instance of {@link ProductData} related to iText 5.
 */
public class IText5ProductData {
    private static final String ITEXT5_PRODUCT_NAME = "itext5";
    private static final String ITEXT5_PUBLIC_PRODUCT_NAME = ITEXT5_PRODUCT_NAME;

    private static final String ITEXT5_VERSION = "5.5.13.6";
    private static final int ITEXT5_COPYRIGHT_SINCE = 1998;
    private static final int ITEXT5_COPYRIGHT_TO = 2026;

    private static final ProductData ITEXT5_PRODUCT_DATA = new ProductData(ITEXT5_PUBLIC_PRODUCT_NAME,
            ITEXT5_PRODUCT_NAME, ITEXT5_VERSION, ITEXT5_COPYRIGHT_SINCE, ITEXT5_COPYRIGHT_TO);

    private IText5ProductData() {
        //To do nothing.
    }

    /**
     * Getter for an instance of {@link ProductData} related to iText 5.
     *
     * @return iText 5 product description
     */
    public static ProductData getInstance() {
        return ITEXT5_PRODUCT_DATA;
    }
}
