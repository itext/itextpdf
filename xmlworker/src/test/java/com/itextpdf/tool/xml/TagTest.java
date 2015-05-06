/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with this program; if not, see http://www.gnu.org/licenses or
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a
 * covered work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.tool.xml;

import org.junit.Test;

import junit.framework.Assert;

/**
 * Validates initial settings of {@link Tag}
 *
 * @author itextpdf.com
 *
 */
public class TagTest {

	/**
	 * See that the attribute map is initialized
	 */
	@Test
	public void validateAttributesNotNull() {
		Tag t = new Tag("dummy");
		Assert.assertNotNull(t.getAttributes());
	}

	/**
	 * See that the css map is initialized
	 */
	@Test
	public void validateCssNotNull() {
		Tag t = new Tag("dummy");
		Assert.assertNotNull(t.getCSS());
	}

	/**
	 * See that the children is list
	 */
	@Test
	public void validateChildrenNotNull() {
		Tag t = new Tag("dummy");
		Assert.assertNotNull(t.getChildren());
	}

	/**
	 * Validates that the tags parent is set when adding it as a child
	 */
	@Test
	public void validateParentSetOnAdd() {
		Tag t = new Tag("pappie");
		Tag t2 = new Tag("baby");
		t.addChild(t2);
		Assert.assertEquals(t, t2.getParent());
	}

	/**
	 * Validates that the parent tag does not have the tag set as child when set parent is
	 * called as parent.
	 */
	@Test
	public void validateChildNotSetOnSetParent() {
		Tag t = new Tag("pappie");
		Tag t2 = new Tag("baby");
		t2.setParent(t);
		Assert.assertEquals(0, t.getChildren().size());
	}

	/**
	 * Compare equal tag names.
	 */
	@Test
	public void compareTrue() {
		Assert.assertTrue(new Tag("pappie").compareTag(new Tag("pappie")));
	}
	/**
	 * Compare notEqual tag names
	 */
	@Test
	public void compareFalse() {
		Assert.assertFalse(new Tag("pappie").compareTag(new Tag("lappie")));
	}
	/**
	 * Compare equal namespace (and tagname)
	 */
	@Test
	public void compareTrueNS() {
		Assert.assertTrue(new Tag("pappie", "ns").compareTag(new Tag("pappie", "ns")));
	}

	/**
	 * Compare different namespace (and equal tagname).
	 */
	@Test
	public void compareFalseNS() {
		Assert.assertFalse(new Tag("pappie", "ns").compareTag(new Tag("pappie", "xs")));
	}
	/**
	 * Compare different namespace and different tagname.
	 */
	@Test
	public void compareFalseTagAndNS() {
		Assert.assertFalse(new Tag("pappie", "ns").compareTag(new Tag("mammie", "xs")));
	}

}
