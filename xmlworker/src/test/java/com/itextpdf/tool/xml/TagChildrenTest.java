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
