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
package com.itextpdf.text;

import java.util.List;
import java.util.ArrayList;

/**
 * This is an <CODE>Element</CODE> that contains
 * some meta information about the document.
 * <P>
 * An object of type <CODE>Meta</CODE> can not be constructed by the user.
 * User defined meta information should be placed in a <CODE>Header</CODE>-object.
 * <CODE>Meta</CODE> is reserved for: Subject, Keywords, Author, Title, Producer
 * and Creationdate information.
 *
 * @see		Element
 * @see		Header
 */

public class Meta implements Element {

    // membervariables

	/** This is the type of Meta-information this object contains. */
    private final int type;

    /** This is the content of the Meta-information. */
    private final StringBuffer content;

	/**
	 * The possible value of an alignment attribute.
	 * @since 5.0.6 (moved from ElementTags)
	 */
	public static final String UNKNOWN = "unknown";

	/**
	 * The possible value of an alignment attribute.
	 * @since 5.0.6 (moved from ElementTags)
	 */
	public static final String PRODUCER = "producer";

	/**
	 * The possible value of an alignment attribute.
	 * @since 5.0.6 (moved from ElementTags)
	 */
	public static final String CREATIONDATE = "creationdate";

	/**
	 * The possible value of an alignment attribute.
	 * @since 5.0.6 (moved from ElementTags)
	 */
	public static final String AUTHOR = "author";

	/**
	 * The possible value of an alignment attribute.
	 * @since 5.0.6 (moved from ElementTags)
	 */
	public static final String KEYWORDS = "keywords";

	/**
	 * The possible value of an alignment attribute.
	 * @since 5.0.6 (moved from ElementTags)
	 */
	public static final String SUBJECT = "subject";

	/**
	 * The possible value of an alignment attribute.
	 * @since 5.0.6 (moved from ElementTags)
	 */
	public static final String TITLE = "title";

    // constructors

    /**
     * Constructs a <CODE>Meta</CODE>.
     *
     * @param	type		the type of meta-information
     * @param	content		the content
     */
    Meta(final int type, final String content) {
        this.type = type;
        this.content = new StringBuffer(content);
    }

    /**
     * Constructs a <CODE>Meta</CODE>.
     *
     * @param	tag		    the tagname of the meta-information
     * @param	content		the content
     */
    public Meta(final String tag, final String content) {
        this.type = Meta.getType(tag);
        this.content = new StringBuffer(content);
    }

    // implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to a
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener		the <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(final ElementListener listener) {
        try {
            return listener.add(this);
        }
        catch(DocumentException de) {
            return false;
        }
    }

    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */
    public int type() {
        return type;
    }

    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    public List<Chunk> getChunks() {
        return new ArrayList<Chunk>();
    }

	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return false;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return false;
	}

    // methods

    /**
     * appends some text to this <CODE>Meta</CODE>.
     *
     * @param	string      a <CODE>String</CODE>
     * @return	a <CODE>StringBuffer</CODE>
     */
    public StringBuffer append(final String string) {
        return content.append(string);
    }

    // methods to retrieve information

	/**
     * Returns the content of the meta information.
     *
     * @return	a <CODE>String</CODE>
     */
    public String getContent() {
        return content.toString();
    }

	/**
     * Returns the name of the meta information.
     *
     * @return	a <CODE>String</CODE>
     */

    public String getName() {
        switch (type) {
            case Element.SUBJECT:
                return Meta.SUBJECT;
            case Element.KEYWORDS:
                return Meta.KEYWORDS;
            case Element.AUTHOR:
                return Meta.AUTHOR;
            case Element.TITLE:
                return Meta.TITLE;
            case Element.PRODUCER:
                return Meta.PRODUCER;
            case Element.CREATIONDATE:
                return Meta.CREATIONDATE;
                default:
                    return Meta.UNKNOWN;
        }
    }

    /**
     * Returns the name of the meta information.
     *
     * @param tag iText tag for meta information
     * @return	the Element value corresponding with the given tag
     */
    public static int getType(final String tag) {
        if (Meta.SUBJECT.equals(tag)) {
            return Element.SUBJECT;
        }
        if (Meta.KEYWORDS.equals(tag)) {
            return Element.KEYWORDS;
        }
        if (Meta.AUTHOR.equals(tag)) {
            return Element.AUTHOR;
        }
        if (Meta.TITLE.equals(tag)) {
            return Element.TITLE;
        }
        if (Meta.PRODUCER.equals(tag)) {
            return Element.PRODUCER;
        }
        if (Meta.CREATIONDATE.equals(tag)) {
            return Element.CREATIONDATE;
        }
        return Element.HEADER;
    }

}
