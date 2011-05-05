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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Balder Van Camp
 *
 */
public class XMLWorkerConfigurationImpl implements XMLWorkerConfig {

	private TagProcessorFactory factory;
	private CSSResolver resolver;
	private boolean acceptUnknown;
	private boolean isHTML;
	private Provider provider = new DefaultProvider();
	private Rectangle pageSize = PageSize.A4;
	private PdfWriter writer;
	private Document doc;
	private final Map<String, Object> memory;
	private Charset charSet = Charset.defaultCharset();
	private boolean isAutoBookMark = true;
	private final List<String> roottags = Arrays.asList(new String[] { "defaultRoot", "body", "div" });

	/**
	 *
	 */
	public XMLWorkerConfigurationImpl(){
		memory = new HashMap<String, Object>();
	}
	/**
	 * @param doc
	 * @param writer
	 */
	public XMLWorkerConfigurationImpl(final Document doc, final PdfWriter writer){
		this();
		this.doc = doc;
		this.pageSize = doc.getPageSize();
		this.writer = writer;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.XMLWorkerConfig#getTagFactory()
	 */
	public TagProcessorFactory getTagFactory() {
		return factory;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.XMLWorkerConfig#acceptUnknown()
	 */
	public boolean acceptUnknown() {
		return acceptUnknown;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.XMLWorkerConfig#getProvider()
	 */
	public Provider getProvider() {
		return provider;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.XMLWorkerConfig#getCssResolver()
	 */
	public CSSResolver getCssResolver() {
		return resolver;
	}

	/**
	 * Sets the CSS.
	 *
	 * @param cssResolver
	 * @return XMLWorkerConfigurationImpl object
	 */
	public XMLWorkerConfigurationImpl cssResolver(final CSSResolver cssResolver) {
		this.resolver = cssResolver;
		return this;
	}

	/**
	 * Sets the TagProcessorFactory used by the {@link XMLWorker}.
	 *
	 * @param factory the TagProcessorFactory
	 * @return XMLWorkerConfigurationImpl object
	 */
	public XMLWorkerConfigurationImpl tagProcessorFactory(final TagProcessorFactory factory) {
		this.factory = factory;
		return this;

	}

	/**
	 * @param acceptUnknown
	 * @return XMLWorkerConfigurationImpl object
	 */
	public XMLWorkerConfigurationImpl acceptUnknown(final boolean acceptUnknown) {
		this.acceptUnknown = acceptUnknown;
		return this;
	}

	/**
	 *
	 * @param provider
	 * @return XMLWorkerConfigurationImpl object
	 */
	public XMLWorkerConfigurationImpl provider(final Provider provider) {
		this.provider = provider;
		return this;
	}

	/**
	 * The page size for the document. If not set the default {@link PageSize#A4_LANDSCAPE} is returned.
	 *
	 * @return the page size for the document
	 *
	 */
	public Rectangle getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the page size.
	 *
	 * @param pageSize the page size that should be used.
	 * @return XMLWorkerConfigurationImpl object
	 */
	public XMLWorkerConfigurationImpl pageSize(final Rectangle pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	/**
	 * Sets the PdfWriter.
	 *
	 * @param pdfWriter the writer
	 * @return XMLWorkerConfigurationImpl object
	 */
	public XMLWorkerConfigurationImpl pdfWriter(final PdfWriter pdfWriter) {
		this.writer = pdfWriter;
		return this;
	}

	/**
	 * Sets the document.
	 *
	 * @param document the Document
	 * @return XMLWorkerConfigurationImpl object
	 */
	public XMLWorkerConfigurationImpl document(final Document document) {
		this.doc = document;
		this.pageSize = doc.getPageSize();
		return this;
	}

	/**
	 * Sets whether or not the worker is parsing HTML.
	 * case.
	 *
	 * @param isHtml boolean value, true if worker should convert tags to lower case
	 * @return XMLWorkerConfigurationImpl object
	 */
	public XMLWorkerConfigurationImpl isParsingHTML(final boolean isHtml) {
		this.isHTML = isHtml;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.XMLWorkerConfig#getDocument()
	 */
	public Document getDocument() {
		return doc;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.XMLWorkerConfig#getWriter()
	 */
	public PdfWriter getWriter() {
		return writer;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.XMLWorkerConfig#parsingHTML()
	 */
	public boolean isParsingHTML() {
		return isHTML;
	}

	public Map<String, Object> getMemory() {
		return memory;
	}
	public Tag getDefaultRoot() {
		return new Tag("defaultRoot");
	}
	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.XMLWorkerConfig#charSet(java.nio.charset.Charset)
	 */
	public void charSet(Charset charSet) {
		this.charSet  = charSet;
		
	}
	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.XMLWorkerConfig#charSet()
	 */
	public Charset charSet() {
		return charSet;
	}
	/**
	 * Defaults to true
	 * @see XMLWorkerConfig#autoBookmark()
	 */
	public boolean autoBookmark() {
		return this.isAutoBookMark;
	}
	
	/**
	 * 
	 * @param bookmark true to enable auto bookmarking of headers, false otherwise.
	 */
	public void autoBookMark(boolean bookmark) {
		this.isAutoBookMark = bookmark;
	}
	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.XMLWorkerConfig#getRootTags()
	 */
	public List<String> getRootTags() {
		return new ArrayList<String>(roottags);
	}
	
	public void addRootTag(String tag) {
		roottags.add(tag);
	}
	
	public void removeRootTag(String tag) {
		roottags.remove(tag);
	}
	
}
