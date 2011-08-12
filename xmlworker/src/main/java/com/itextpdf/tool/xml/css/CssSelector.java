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
package com.itextpdf.tool.xml.css;

import java.util.LinkedHashSet;
import java.util.Set;

import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.HTML;

/**
 * @author redlab_b
 *
 */
public class CssSelector {

	private final CssUtils utils;

	/**
	 *
	 */
	public CssSelector() {
		this.utils = CssUtils.getInstance();
	}
	/**
	 * Calls each method in this class and returns an aggregated list of selectors.
	 * @param t the tag
	 * @return set of selectors
	 */
	public Set<String> createAllSelectors(final Tag t) {
		Set<String> set = new LinkedHashSet<String>();
		set.addAll(createTagSelectors(t));
		set.addAll(createClassSelectors(t));
		set.addAll(createIdSelector(t));
		return set;
	}
	/**
	 * Creates selectors for a given tag.
	 * @param t the tag to create selectors for.
	 * @return all selectors for the given tag.
	 */
	public Set<String> createTagSelectors(final Tag t) {
		Set<String> selectors = new LinkedHashSet<String>();
		selectors.add(t.getName());
		if (null != t.getParent()) {
			Tag parent = t.getParent();
			StringBuilder b = new StringBuilder(t.getName());
			StringBuilder bStripped = new StringBuilder(t.getName());
			StringBuilder bElem = new StringBuilder(t.getName());
			StringBuilder bChild = new StringBuilder(t.getName());
			StringBuilder bChildSpaced = new StringBuilder(t.getName());
			Tag child = t;
			while (null != parent) {
				if (parent.getChildren().indexOf(child) == 0) {
					bChild.insert(0, '+').insert(0, parent.getName());
					bChildSpaced.insert(0, " + ").insert(0, parent.getName());
					selectors.add(bChild.toString());
					selectors.add(bChildSpaced.toString());
				}
				b.insert(0, " > ").insert(0, parent.getName());
				selectors.add(b.toString());
				bStripped.insert(0, ">").insert(0, parent.getName());
				selectors.add(bStripped.toString());
				bElem.insert(0, ' ').insert(0, parent.getName());
				selectors.add(bElem.toString());
				child = parent;
				parent = parent.getParent();
			}
		}
		return selectors;
	}

	/**
	 * Creates the class selectors, each class is prepended with a ".".
	 * @param t the tag
	 * @return set of Strings
	 */
	public Set<String> createClassSelectors(final Tag t) {
		String classes = t.getAttributes().get(HTML.Attribute.CLASS);
		Set<String> set = new LinkedHashSet<String>();
		if (null != classes) {
			String[] classSplit = this.utils.stripDoubleSpacesAndTrim(classes).split(" ");
			for (String klass : classSplit) {
				StringBuilder builder = new StringBuilder();
				builder.append('.').append(klass);
				set.add(builder.toString());
			}
		}
		return set;
	}

	/**
	 * Creates the selector for id, the id is prepended with '#'.
	 * @param t the tag
	 * @return set of Strings
	 */
	public Set<String> createIdSelector(final Tag t) {
		String id = t.getAttributes().get(HTML.Attribute.ID);
		Set<String> set = new LinkedHashSet<String>(1);
		if (null != id) {
			StringBuilder builder = new StringBuilder();
			builder.append('#').append(id);
			set.add(builder.toString());
		}
		return set;
	}

}
