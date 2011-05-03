/*
 * $Id: AbstractTagProcessor.java 349 2011-05-02 18:53:54Z balder $
 *
 * This file is part of the iText (R) project. Copyright (c) 1998-2011 1T3XT BVBA Authors: Balder Van Camp, Emiel
 * Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License version 3 as published by the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * 1T3XT, 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public License along with this program; if not,
 * see http://www.gnu.org/licenses or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL: http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions of this program must display Appropriate
 * Legal Notices, as required under Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a covered work must retain the producer
 * line in every PDF that is created or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a commercial license. Buying such a license is
 * mandatory as soon as you develop commercial activities involving the iText software without disclosing the source
 * code of your own applications. These activities include: offering paid services to customers as an ASP, serving PDFs
 * on the fly in a web application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.FontSizeTranslator;

/**
 * Abstract TagProcessor that allows setting the configuration object to a protected member variable.<br />
 * Adds empty implementations for TagProcessor methods.
 *
 * @author Balder Van Camp
 *
 */
public abstract class AbstractTagProcessor implements TagProcessor {

	/**
	 * The configuration object of the XMLWorker.
	 */
	protected XMLWorkerConfig configuration;
	private final FontSizeTranslator fontsizeTrans;

	/**
	 *
	 */
	public AbstractTagProcessor() {
		fontsizeTrans = FontSizeTranslator.getInstance();
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.TagProcessor#setConfiguration(com.itextpdf.tool.xml.XMLWorkerConfig)
	 */
	public void setConfiguration(final XMLWorkerConfig config) {
		this.configuration = config;
	}

	/**
	 * Calculates any found font size to pt values and set it in the CSS before calling
	 * {@link AbstractTagProcessor#start(Tag)}
	 *
	 * @see com.itextpdf.tool.xml.TagProcessor#startElement(com.itextpdf.tool.xml.Tag)
	 */
	public final List<Element> startElement(final Tag tag) {
		float fontSize = fontsizeTrans.translateFontSize(tag);
		tag.getCSS().put(CSS.Property.FONT_SIZE, fontSize + "pt");
		return start(tag);
	}

	/**
	 * Classes extending AbstractTagProcessor should override the start element for actions that should be done in
	 * {@link TagProcessor#startElement(Tag)}. The {@link AbstractTagProcessor#startElement(Tag)} calls this method
	 * after or before doing certain stuff, (see it's description).
	 *
	 * @param tag
	 * @return an element to be added to current content, may be null
	 */
	public List<Element> start(final Tag tag){ return new ArrayList<Element>(0); };

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.TagProcessor#content(com.itextpdf.tool.xml.Tag, java.lang.String)
	 */
	public List<Element> content(final Tag tag, final String content) {
		return new ArrayList<Element>(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.TagProcessor#endElement(com.itextpdf.tool.xml.Tag, java.util.List)
	 */
	public List<Element> endElement(final Tag tag, final List<Element> currentContent) {
		return new ArrayList<Element>(0);
	}

	/**
	 * Defaults to false.
	 *
	 * @see com.itextpdf.tool.xml.TagProcessor#isStackOwner()
	 */
	public boolean isStackOwner() {
		return false;
	}
}
