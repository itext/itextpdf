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
package com.itextpdf.tool.xml.parser;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.tool.xml.parser.state.InsideTagHTMLState;

/**
 * Wrapper class for different things that need to be kept track of between different states.
 *
 * @author redlab_b
 *
 */
public class XMLParserMemory {

	private String currentTag;
	private String currentAttr;
	private final StringBuilder currentEntity = new StringBuilder();
	private final StringBuilder comment = new StringBuilder();
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private final Map<String, String> attr;
	private String wsTag = "";
	private String currentNameSpace = "";

	/**
	 *
	 */
	public XMLParserMemory() {
		this.attr = new HashMap<String, String>();
	}

	/**
	 * Set the encountered tag
	 * @param content the tag
	 */
	public void currentTag(final String content) {
		this.currentTag = content;
		this.wsTag = content;
		this.attr.clear();
	}

	/**
	 * Sets the encountered attribute
	 * @param attr the attribute
	 */
	public void currentAttr(final String attr) {
		this.currentAttr = attr;
	}

	/**
	 * Sets the current attribute value
	 * @param content the current attributes value
	 */
	public void putCurrentAttrValue(final String content) {
		attr.put(this.currentAttr.toLowerCase(), content);
	}

	/**
	 * The current text buffer.
	 *
	 * @return current text buffer
	 */
	public ByteArrayOutputStream current() {
		return baos;
	}

	/**
	 * Returns the current tag
	 * @return the currentTag
	 */
	public String getCurrentTag() {
		return this.currentTag;
	}

	/**
	 * Returns a map of all attributes and their value found on the current tag
	 * @return the attributes of the current tag
	 */
	public Map<String, String> getAttributes() {
		return new HashMap<String, String>(this.attr);
	}

	/**
	 * Returns the current entity buffer
	 * @return a StringBuilder for the current entity
	 */
	public StringBuilder currentEntity() {
		return this.currentEntity;
	}

	/**
	 * Returns the xml comment buffer
	 *
	 * @return comment
	 */
	public StringBuilder comment() {
		return this.comment;
	}

	/**
	 * Returns last tag that needs to be taken into account for HTML Whitespace handling.<br />
	 * Used by {@link InsideTagHTMLState}, only for HTML processing.
	 * @return tag
	 */
	public String whitespaceTag() {
		return this.wsTag ;
	}

	/**
	 * Sets the last tag that needs to be taken into account for HTML Whitespace handling<br />
	 * Used by {@link InsideTagHTMLState}, only for HTML processing.
	 * @param tag the tag
	 */
	public void whitespaceTag(final String tag) {
		this.wsTag = tag;
	}

	/**
	 * Sets the current namespace
	 * @param ns the current namespace
	 */
	public void namespace(final String ns) {
		this.currentNameSpace = ns;
	}

	/**
	 * Flushes the namespace memory
	 */
	public void flushNameSpace() {
		this.currentNameSpace = "";
	}

	/**
	 * Get the current namespace
	 * @return the current namespace or empty String if no namespace
	 */
	public String getNameSpace() {
		return this.currentNameSpace;
	}

	/**
	 *
	 */
	public void resetBuffer() {
		this.baos = new ByteArrayOutputStream();
	}

}
