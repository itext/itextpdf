/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.XMPUtils;
import com.itextpdf.xmp.options.PropertyOptions;

public class XmpBasicProperties {
    /** An unordered array specifying properties that were edited outside the authoring application. Each item should contain a single namespace and XPath separated by one ASCII space (U+0020). */
    public static final String ADVISORY = "Advisory";
    /** The base URL for relative URLs in the document content. If this document contains Internet links, and those links are relative, they are relative to this base URL. This property provides a standard way for embedded relative URLs to be interpreted by tools. Web authoring tools should set the value based on their notion of where URLs will be interpreted. */
    public static final String BASEURL = "BaseURL";
    /** The date and time the resource was originally created. */
    public static final String CREATEDATE = "CreateDate";
    /** The name of the first known tool used to create the resource. If history is present in the metadata, this value should be equivalent to that of xmpMM:History's softwareAgent property. */
    public static final String CREATORTOOL = "CreatorTool";
    /** An unordered array of text strings that unambiguously identify the resource within a given context. */
    public static final String IDENTIFIER = "Identifier";
    /** The date and time that any metadata for this resource was last changed. */
    public static final String METADATADATE = "MetadataDate";
    /** The date and time the resource was last modified. */
    public static final String MODIFYDATE = "ModifyDate";
    /** A short informal name for the resource. */
    public static final String NICKNAME = "Nickname";
    /** An alternative array of thumbnail images for a file, which can differ in characteristics such as size or image encoding. */
    public static final String THUMBNAILS = "Thumbnails";

    /**
     * Adds the creatortool.
     *
     * @param xmpMeta
     * @param creator
     */
    static public void setCreatorTool(XMPMeta xmpMeta, String creator) throws XMPException {
        xmpMeta.setProperty(XMPConst.NS_XMP, CREATORTOOL, creator);
    }

    /**
     * Adds the creation date.
     *
     * @param xmpMeta
     * @param date
     */
    static public void setCreateDate(XMPMeta xmpMeta, String date) throws XMPException {
        xmpMeta.setProperty(XMPConst.NS_XMP, CREATEDATE, date);
    }

    /**
     * Adds the modification date.
     *
     * @param xmpMeta
     * @param date
     */
    static public void setModDate(XMPMeta xmpMeta, String date) throws XMPException {
        xmpMeta.setProperty(XMPConst.NS_XMP, MODIFYDATE, date);
    }

    /**
     * Adds the meta data date.
     *
     * @param xmpMeta
     * @param date
     */
    static public void setMetaDataDate(XMPMeta xmpMeta, String date) throws XMPException {
        xmpMeta.setProperty(XMPConst.NS_XMP, METADATADATE, date);
    }

    /** Sets the identifier.
     *
     * @param xmpMeta
     * @param id
     */
    static public void setIdentifiers(XMPMeta xmpMeta, String[] id) throws XMPException {
        XMPUtils.removeProperties(xmpMeta, XMPConst.NS_DC, IDENTIFIER, true, true);
        for (int i = 0; i < id.length; i++) {
            xmpMeta.appendArrayItem(XMPConst.NS_DC, IDENTIFIER, new PropertyOptions(PropertyOptions.ARRAY), id[i], null);
        }
    }

    /** Adds the nickname.
     *
     * @param xmpMeta
     * @param name
     */
    static public void setNickname(XMPMeta xmpMeta, String name) throws XMPException {
        xmpMeta.setProperty(XMPConst.NS_XMP, NICKNAME, name);
    }
}
