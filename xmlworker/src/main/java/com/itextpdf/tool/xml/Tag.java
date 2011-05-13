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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an encountered tag.
 *
 * @author redlab_b
 *
 */
public class Tag {

	private Tag parent;
	private final String tag;
	private final Map<String, String> attributes;
	private Map<String, String> css;
	private final List<Tag> children;
	private final String ns;

	/**
	 * Construct a tag.
	 *
	 * @param tag the tag name
	 * @param attr the attributes in the tag
	 */
	public Tag(final String tag, final Map<String, String> attr) {
		this(tag, attr, new HashMap<String, String>(0), "");
	}

	/**
	 * @param tag the tag name
	 */
	public Tag(final String tag) {
		this(tag, new HashMap<String, String>(0), new HashMap<String, String>(0), "");
	}

	/**
	 *
	 * @param tag the tag name
	 * @param attr the attributes
	 * @param css a map with CSS
	 * @param ns the namespace
	 */
	public Tag(final String tag, final Map<String, String> attr, final Map<String, String> css, final String ns ) {
		this.tag = tag;
		this.attributes = attr;
		this.css = css;
		this.children = new ArrayList<Tag>(0);
		this.ns = ns;
	}

	/**
	 *
	 * @param tag the tag name
	 * @param attr the attributes
	 * @param ns the namespace
	 */
	public Tag(final String tag, final Map<String, String> attr, final String ns) {
		this(tag, attr,new HashMap<String, String>(0),ns );
	}

	/**
	 * Set the tags parent tag.
	 *
	 * @param parent the parent tag of this tag
	 */
	public void setParent(final Tag parent) {
		this.parent = parent;

	}

	/**
	 * Returns the parent tag for this tag.
	 *
	 * @return the parent tag or null if none
	 */
	public Tag getParent() {
		return this.parent;
	}

	/**
	 * The tags name.
	 *
	 * @return the tag name
	 */
	public String getTag() {
		return this.tag;
	}

	/**
	 * Returns a Map of css property, value.
	 *
	 * @return the css, never null but can be an empty map.
	 */
	public Map<String, String> getCSS() {
		return this.css;
	}

	/**
	 * Set the css map. If <code>null</code> is given the css is cleared.
	 *
	 * @param css set css properties
	 */
	public void setCSS(final Map<String, String> css) {
		if (null != css) {
			this.css = css;
		} else {
			this.css.clear();
		}
	}

	/**
	 * @return the attributes of the tag
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * Add a child tag to this tag. The given tags parent is set to this tag.
	 *
	 * @param t the tag
	 */
	public void addChild(final Tag t) {
		t.setParent(this);
		this.children.add(t);

	}

	/**
	 * Returns all children of this tag.
	 *
	 * @return the children tags of this tag.
	 */
	public List<Tag> getChildren() {
		return this.children;
	}

	/**
	 * @return the ns
	 */
	public String getNameSpace() {
		return ns;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s", this.tag);
	}
}
