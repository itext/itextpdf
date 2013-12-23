/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml;

import java.util.*;

/**
 * Represents an encountered tag.
 *
 * @author redlab_b
 *
 */
public class Tag implements Iterable<Tag> {

	private Tag parent;
	private final String tag;
	private final Map<String, String> attributes;
	private Map<String, String> css;
	private final List<Tag> children;
	private final String ns;
    private Object lastMarginBottom = null;


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
		this.children = new LinkedList<Tag>();
		if (ns == null) {
			throw new NullPointerException("NS cannot be null");
		}
		this.ns = ns;
	}

	/**
	 * Create a new tag object.
	 * @param tag the tag name
	 * @param attr the attributes
	 * @param ns the namespace
	 */
	public Tag(final String tag, final Map<String, String> attr, final String ns) {
		this(tag, attr,new HashMap<String, String>(0),ns );
	}

	/**
	 * Create a new tag object.
	 * @param tag the name of the tag
	 * @param ns the namespace of the tag (do not set null, set an empty String)
	 */
	public Tag(final String tag, final String ns) {
		 this(tag, new HashMap<String, String>(0), new HashMap<String, String>(0), ns);
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
	 * @deprecated marked as deprecated in favor for getName, we won't remove it
	 *             yet.
	 * @return the tag name
	 */
	@Deprecated
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
	 * Returns all children of this tag with the given name.
	 * @param name the name of the tags to look for
	 *
	 * @return the children tags of this tag with the given name.
	 */
	public List<Tag> getChildren(final String name) {
		List<Tag> named = new LinkedList<Tag>();
		for(Tag child: this.children) {
			if(child.getName().equals(name)) {
				named.add(child);
			}
		}
		return named;
	}

	/**
	 * @return the ns
	 */
	public String getNameSpace() {
		return ns;
	}

	/**
	 * Print the tag
	 */
	@Override
	public String toString() {
		if ("".equalsIgnoreCase(ns)) {
			return String.format("%s", this.tag);
		}
		return  String.format("%s:%s", this.ns, this.tag);

	}

	/**
	 * Compare this tag with t for namespace and name equality.
	 * @param t the tag to compare with
	 * @return true if the namespace and tag are the same.
	 */
	public boolean compareTag(final Tag t) {
		if (this == t) {
			return true;
		}
		if (t == null) {
			return false;
		}
		Tag other = t;
		if (ns == null) {
			if (other.ns != null) {
				return false;
			}
		} else if (!ns.equals(other.ns)) {
			return false;
		}
		if (tag == null) {
			if (other.tag != null) {
				return false;
			}
		} else if (!tag.equals(other.tag)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the child iterator.
	 */
	public Iterator<Tag> iterator() {
		return children.iterator();
	}

	/**
	 * Finds the first child that matches the given name and namespace.
	 * @param name the name of the tag
	 * @param ns the namespace
	 * @return the child
	 */
	public Tag getChild(final String name, final String ns) {
        return getChild(name, ns, false);
	}

	/**
	 * Finds the first child that matches the given name and ns. Optionally look in the whole tree (in children of children of children ...)
	 * @param name name of the tag
	 * @param ns the namespace
	 * @param recursive true if the tree should be fully inwards inspected.
	 * @return the child if found
	 */
	public Tag getChild(final String name, final String ns, final boolean recursive) {
        return recursiveGetChild(this, name, ns, recursive);
	}

	/**
	 * Whether or not this tag has children.
	 *
	 * @return true if there are children
	 */
	public boolean hasChildren() {
		return getChildren().size() != 0;
	}

	/**
	 * Whether or not this tag has a parent.
	 *
	 * @return true if parent is not <code>null</code>
	 */
	public boolean hasParent() {
		return getParent() != null;
	}

	/**
	 * Check if this tag has a child with the given name and namespace.
	 * @param name the name of the tag to look for
	 * @param ns the namespace (if no namespace, set an empty String)
	 * @return true if a child with given name and ns is found
	 */
	public boolean hasChild(final String name, final String ns) {
		return hasChild(name, ns, false);
	}

	/**
	 * Check if this tag has a child with the given name and namespace.
	 * @param name the name of the tag to look for
	 * @param ns the namespace (if no namespace, set an empty String)
	 * @param recursive true if children's children children children ... should be inspected too.
	 * @return true if a child with the given name and ns is found.
	 */
	public boolean hasChild(final String name, final String ns, final boolean recursive) {
        if (recursive) {
			return recursiveHasChild(this, name, ns, true);
		} else {
			return recursiveHasChild(this, name, ns, false);
		}
	}

	/**
	 * @param tag
	 * @param name
	 * @param ns
	 * @param recursive
	 * @return true if the child is found in the child tree
	 */
	private boolean recursiveHasChild(final Tag tag, final String name, final String ns, final boolean recursive) {
		for (Tag t : tag) {
			if (t.tag.equals(name) && t.ns.equals(ns)) {
				return true;
			} else if (recursive) {

				if (recursiveHasChild(t, name, ns, recursive)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param tag
	 * @param name
	 * @param ns
	 * @param recursive
	 * @return the child tag
	 */
	private Tag recursiveGetChild(final Tag tag, final String name, final String ns, final boolean recursive) {
		for (Tag t : tag) {
			if (t.tag.equals(name) && t.ns.equals(ns)) {
				return t;
			} else if (recursive) {
				Tag rT = null;
				if (null != (rT = recursiveGetChild(t, name, ns, recursive))) {
					return rT;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the name of the tag.<br />(Actually the same as getTag method, but
	 * after using XMLWorker for a while we caught ourself always trying to call
	 * Tag#getName() instead of Tag#getTag())
	 *
	 * @return the name of the tag.
	 */
	public String getName() {
		return this.tag;
	}

    public Object getLastMarginBottom() {
        return lastMarginBottom;
    }

    public void setLastMarginBottom(Object lastMarginBottom) {
        this.lastMarginBottom = lastMarginBottom;
    }
}
