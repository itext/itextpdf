/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
/**
 *
 */
package com.itextpdf.tool.xml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author itextpdf.com
 *
 */
public class TagChildrenTest {

	/**
	 *
	 */
	private static final String CHILDS_CHILD = "childsChild";
	/**
	 *
	 */
	private static final String CHILD2 = "child2";
	/**
	 *
	 */
	private static final String CHILD1 = "child1";
	/**
	 *
	 */
	private static final String ROOTSTR = "root";
	private Tag root;
	private Tag child2WithChild;
	private Tag child1NoChildren;
	private Tag childsChild;

	/**
	 * Init.
	 */
	@Before
	public void setUp() {
		root = new Tag(ROOTSTR);
		child1NoChildren = new Tag(CHILD1);
		root.addChild(child1NoChildren);
		child2WithChild = new Tag(CHILD2);
		childsChild = new Tag(CHILDS_CHILD);
		child2WithChild.addChild(childsChild);
		root.addChild(child2WithChild);
	}

	/**
	 * Test {@link Tag#getChild(String, String)}.
	 */
	@Test
	public void getChild() {
		Assert.assertEquals(child1NoChildren, root.getChild(CHILD1, ""));
	}
	/**
	 * Test {@link Tag#getChild(String, String, boolean)}.
	 */
	@Test
	public void getChildRecursive() {
		Assert.assertEquals(childsChild, root.getChild(CHILDS_CHILD, "", true));
	}
	/**
	 * Test {@link Tag#hasChild(String, String)}.
	 */
	@Test
	public void hasChild() {
		Assert.assertTrue(root.hasChild(CHILD1, ""));
	}
	/**
	 * Test {@link Tag#hasChild(String, String, boolean)}.
	 */
	@Test
	public void hasChildRecursive() {
		Assert.assertTrue(root.hasChild(CHILDS_CHILD, "", true));
	}
	/**
	 * Test {@link Tag#hasParent()}.
	 */
	@Test
	public void hasParent() {
		Assert.assertTrue(child1NoChildren.hasParent());
	}
	/**
	 * Test {@link Tag#hasChildren()}.
	 */
	@Test
	public void hasChildren() {
		Assert.assertTrue(child2WithChild.hasChildren());
	}
}
