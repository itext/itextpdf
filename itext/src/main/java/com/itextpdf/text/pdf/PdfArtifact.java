/*
 * $Id: PdfArtifact.java 6192 2014-01-29 14:37:53Z eugenemark $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Alexander Chingarev, Bruno Lowagie, et al.
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

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.HashMap;

public class PdfArtifact implements IAccessibleElement {

    protected PdfName role = PdfName.ARTIFACT;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    protected AccessibleElementId id = new AccessibleElementId();

    public PdfObject getAccessibleAttribute(final PdfName key) {
        if (accessibleAttributes != null)
            return accessibleAttributes.get(key);
        else
            return null;
    }

    public void setAccessibleAttribute(final PdfName key, final PdfObject value) {
        if (accessibleAttributes == null)
            accessibleAttributes = new HashMap<PdfName, PdfObject>();
        accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return accessibleAttributes;
    }

    public PdfName getRole() {
        return role;
    }

    public void setRole(final PdfName role) {
    }

    public AccessibleElementId getId() {
        return id;
    }

    public void setId(final AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return true;
    }

    public PdfString getType() {
        return accessibleAttributes == null ? null : (PdfString) accessibleAttributes.get(PdfName.TYPE);
    }

    public void setType(PdfString type) {
        setAccessibleAttribute(PdfName.TYPE, type);
    }

    public PdfArray getBBox() {
        return accessibleAttributes == null ? null : (PdfArray) accessibleAttributes.get(PdfName.BBOX);
    }

    public void setBBox(PdfArray bbox) {
        setAccessibleAttribute(PdfName.BBOX, bbox);
    }

    public PdfArray getAttached() {
        return accessibleAttributes == null ? null : (PdfArray) accessibleAttributes.get(PdfName.ATTACHED);
    }

    public void setAttached(PdfArray attached) {
        setAccessibleAttribute(PdfName.ATTACHED, attached);
    }


}
