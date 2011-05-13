/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.tool.xml;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Configuration object for the XMLWorker.
 *
 * @author redlab_b
 *
 */
public interface XMLWorkerConfig {

	/**
	 * Reserved as key in the {@link XMLWorkerConfig#getMemory()} map.
	 */
	public static final String VERTICAL_POSITION = "verticalPosition";
	/**
	 * Reserved as key in the {@link XMLWorkerConfig#getMemory()} map.
	 */
	public static final String BOOKMARK_TREE = "BOOKMARK_TREE";
	/**
	 * Reserved as key in the {@link XMLWorkerConfig#getMemory()} map.
	 */
	public static final String LAST_MARGIN_BOTTOM = "lastMarginBottom";
	/**
	 * The TagProcessorFactory to use in the XMLWorker.
	 *
	 * @return the tag factory
	 */
	TagProcessorFactory getTagFactory();

	/**
	 * Whether or not the {@link XMLWorker} should accept unknown tags.
	 *
	 * @return true if unknown tags should be accepted
	 */
	boolean acceptUnknown();

	/**
	 * The Provider implementation to use in the XMLWorker.
	 *
	 * @return the provider
	 */
	Provider getProvider();

	/**
	 * The CSSResolver to use in the XMLWorker.
	 *
	 * @return the CSSResolver
	 */
	CSSResolver getCssResolver();

	/**
	 * Returns the page size that must be used.
	 *
	 * @return the page size rectangle
	 */
	Rectangle getPageSize();

	/**
	 * Returns the document everything will be added to if any (== may be null).
	 *
	 * @return the document
	 */
	Document getDocument();

	/**
	 * Returns the writer if any (== may be null).
	 *
	 * @return the PdfWriter
	 */
	PdfWriter getWriter();

	/**
	 * Used by the XMLWorkerImpl to convert all parsed tags to lower.
	 *
	 * @return true for lower-case, false to leave tags as they are.
	 */
	boolean isParsingHTML();

	/**
	 * @return the configuration items in memory
	 */
	public Map<String, Object> getMemory();

	/**
	 * Used by the XMLWorkerImpl to return a default root tag if no root tag is present in given snippet.
	 *
	 * @return a default root tag.
	 */
	Tag getDefaultRoot();

	/**
	 * Sets the character set to be used.
	 *
	 * @param charSet
	 */
	void charSet(Charset charSet);

	/**
	 * Returns the character set used. Defaults to {@link Charset#defaultCharset()}
	 *
	 * @return the character set used.
	 */
	Charset charSet();

	/**
	 * @return true if h1 to h6 tags should be auto-bookmarked
	 */
	boolean autoBookmark();

	/**
	 * Tags in this list are defined as roottags. In certain cases roottags behave different for calculating CSS to PDF values for e.g. leading.
	 * @return a list of tags that count as roottags
	 */
	List<String> getRootTags();

	/**
	 * @return
	 */
	List<TagListener> getTagListeners();

	/**
	 * @return
	 */
	boolean hasTagListener();


}
