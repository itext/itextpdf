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

public class DublinCoreProperties {
    /** External Contributors to the resource (other than the authors). */
    public static final String CONTRIBUTOR = "contributor";
    /** The extent or scope of the resource. */
    public static final String COVERAGE = "coverage";
    /** The authors of the resource (listed in order of precedence, if significant). */
    public static final String CREATOR = "creator";
    /** Date(s) that something interesting happened to the resource. */
    public static final String DATE = "date";
    /** A textual description of the content of the resource. Multiple values may be present for different languages. */
    public static final String DESCRIPTION = "description";
    /** The file format used when saving the resource. Tools and applications should set this property to the save format of the data. It may include appropriate qualifiers. */
    public static final String FORMAT = "format";
    /** Unique identifier of the resource. */
    public static final String IDENTIFIER = "identifier";
    /** An unordered array specifying the languages used in the	resource. */
    public static final String LANGUAGE = "language";
    /** Publishers. */
    public static final String PUBLISHER = "publisher";
    /** Relationships to other documents. */
    public static final String RELATION = "relation";
    /** Informal rights statement, selected by language. */
    public static final String RIGHTS = "rights";
    /** Unique identifier of the work from which this resource was derived. */
    public static final String SOURCE = "source";
    /** An unordered array of descriptive phrases or keywords that specify the topic of the content of the resource. */
    public static final String SUBJECT = "subject";
    /** The title of the document, or the name given to the resource. Typically, it will be a name by which the resource is formally known. */
    public static final String TITLE = "title";
    /** A document type; for example, novel, poem, or working paper. */
    public static final String TYPE = "type";

    /**
     * Adds a title.
     *
     * @param xmpMeta
     * @param title
     */
    static public void addTitle(XMPMeta xmpMeta, String title) throws XMPException {
        xmpMeta.appendArrayItem(XMPConst.NS_DC, TITLE, new PropertyOptions(PropertyOptions.ARRAY_ALTERNATE), title, null);
    }

    /**
     * Sets a title.
     *
     * @param xmpMeta
     * @param title
     * @param genericLang  The name of the generic language
     * @param specificLang The name of the specific language
     */
    static public void setTitle(XMPMeta xmpMeta, String title, String genericLang, String specificLang) throws XMPException {
        xmpMeta.setLocalizedText(XMPConst.NS_DC, TITLE, genericLang, specificLang, title);
    }

    /**
     * Adds a description.
     *
     * @param xmpMeta
     * @param desc
     */
    static public void addDescription(XMPMeta xmpMeta, String desc) throws XMPException {
        xmpMeta.appendArrayItem(XMPConst.NS_DC, DESCRIPTION, new PropertyOptions(PropertyOptions.ARRAY_ALTERNATE), desc, null);

    }

    /**
     * Sets a description.
     *
     * @param xmpMeta
     * @param desc
     * @param genericLang  The name of the generic language
     * @param specificLang The name of the specific language
     */
    static public void setDescription(XMPMeta xmpMeta, String desc, String genericLang, String specificLang) throws XMPException {
        xmpMeta.setLocalizedText(XMPConst.NS_DC, DESCRIPTION, genericLang, specificLang, desc);
    }

    /**
     * Adds a subject.
     *
     * @param xmpMeta
     * @param subject
     */
    static public void addSubject(XMPMeta xmpMeta, String subject) throws XMPException {
        xmpMeta.appendArrayItem(XMPConst.NS_DC, SUBJECT, new PropertyOptions(PropertyOptions.ARRAY), subject, null);
    }


    /**
     * Sets a subject.
     *
     * @param xmpMeta
     * @param subject array of subjects
     */
    static public void setSubject(XMPMeta xmpMeta, String[] subject) throws XMPException {
        XMPUtils.removeProperties(xmpMeta, XMPConst.NS_DC, SUBJECT, true, true);
        for (int i = 0; i < subject.length; i++) {
            xmpMeta.appendArrayItem(XMPConst.NS_DC, SUBJECT, new PropertyOptions(PropertyOptions.ARRAY), subject[i], null);
        }
    }

    /**
     * Adds a single author.
     *
     * @param xmpMeta
     * @param author
     */
    static public void addAuthor(XMPMeta xmpMeta, String author) throws XMPException {
        xmpMeta.appendArrayItem(XMPConst.NS_DC, CREATOR, new PropertyOptions(PropertyOptions.ARRAY_ORDERED), author, null);
    }

    /**
     * Sets an array of authors.
     *
     * @param xmpMeta
     * @param author
     */
    static public void setAuthor(XMPMeta xmpMeta, String[] author) throws XMPException {
        XMPUtils.removeProperties(xmpMeta, XMPConst.NS_DC, CREATOR, true, true);
        for (int i = 0; i < author.length; i++) {
            xmpMeta.appendArrayItem(XMPConst.NS_DC, CREATOR, new PropertyOptions(PropertyOptions.ARRAY_ORDERED), author[i], null);
        }
    }

    /**
     * Adds a single publisher.
     *
     * @param xmpMeta
     * @param publisher
     */
    static public void addPublisher(XMPMeta xmpMeta, String publisher) throws XMPException {
        xmpMeta.appendArrayItem(XMPConst.NS_DC, PUBLISHER, new PropertyOptions(PropertyOptions.ARRAY_ORDERED), publisher, null);
    }

    /**
     * Sets an array of publishers.
     *
     * @param xmpMeta
     * @param publisher
     */
    static public void setPublisher(XMPMeta xmpMeta, String[] publisher) throws XMPException {
        XMPUtils.removeProperties(xmpMeta, XMPConst.NS_DC, PUBLISHER, true, true);
        for (int i = 0; i < publisher.length; i++) {
            xmpMeta.appendArrayItem(XMPConst.NS_DC, PUBLISHER, new PropertyOptions(PropertyOptions.ARRAY_ORDERED), publisher[i], null);
        }
    }
}
