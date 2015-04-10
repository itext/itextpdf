/*
 * $Id: IAccessibleElement.java 6192 2014-01-29 14:37:53Z eugenemark $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Alexander Chingarev, et al.
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
package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

import java.util.HashMap;

/**
 * Describes accessible element.
 */
public interface IAccessibleElement {

    /**
     * Get the attribute of accessible element (everything in <code>A</code> dictionary + <code>Lang</code>, <code>Alt</code>, <code>ActualText</code>, <code>E</code>).
     * @param key
     * @return
     */
    PdfObject getAccessibleAttribute(final PdfName key);

    /**
     * Set the attribute of accessible element (everything in <code>A</code> dictionary + <code>Lang</code>, <code>Alt</code>, <code>ActualText</code>, <code>E</code>).
     * @param key
     * @param value
     */
    void setAccessibleAttribute(final PdfName key, final PdfObject value);

    /**
     * Gets all the properties of accessible element.
     * @return
     */
    HashMap<PdfName, PdfObject> getAccessibleAttributes();

    /**
     * Gets the role of the accessible element.
     * @return
     */
    PdfName getRole();

    /**
     * Sets the role of the accessiblee element.
     * Set role to <code>null</code> if you don't want to tag this element.
     * Note that all child elements won't also be tagged.
     * @param role
     */
    void setRole(final PdfName role);

    AccessibleElementId getId();

    void setId(final AccessibleElementId id);

    boolean isInline();
}
