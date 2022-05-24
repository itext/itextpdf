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
import java.util.Properties;

/**
 * Wrapper that allows to add properties to 'basic building block' objects.
 * Before iText 1.5 every 'basic building block' implemented the MarkupAttributes interface.
 * By setting attributes, you could add markup to the corresponding XML and/or HTML tag.
 * This functionality was hardly used by anyone, so it was removed, and replaced by
 * the MarkedObject functionality.
 *
 * @deprecated since 5.5.9. This class is no longer used.
 */
@Deprecated
public class MarkedObject implements Element {

	/** The element that is wrapped in a MarkedObject. */
	protected Element element;

	/** Contains extra markupAttributes */
	protected Properties markupAttributes = new Properties();

	/**
	 * This constructor is for internal use only.
	 */
	protected MarkedObject() {
		element = null;
	}

	/**
	 * Creates a MarkedObject.
	 * @param element the element
	 */
	public MarkedObject(final Element element) {
		this.element = element;
	}

    /**
     * Gets all the chunks in this element.
     *
     * @return  an <CODE>ArrayList</CODE>
     */
	public List<Chunk> getChunks() {
		return element.getChunks();
	}

    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param       listener        an <CODE>ElementListener</CODE>
     * @return <CODE>true</CODE> if the element was processed successfully
     */
	public boolean process(final ElementListener listener) {
        try {
            return listener.add(element);
        }
        catch(DocumentException de) {
            return false;
        }
	}

    /**
     * Gets the type of the text element.
     *
     * @return  a type
     */
	public int type() {
		return MARKED;
	}

	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return true;
	}

	/**
	 * Getter for the markup attributes.
	 * @return the markupAttributes
	 */
	public Properties getMarkupAttributes() {
		return markupAttributes;
	}

	/**
	 * Adds one markup attribute.
	 * @param key markup key
	 * @param value markup value
	 */
	public void setMarkupAttribute(final String key, final String value) {
		markupAttributes.setProperty(key, value);
	}

}
