/**
 *
 */
package com.itextpdf.tool.xml;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.tool.xml.exceptions.NoSiblingException;

/**
 * @author itextpdf.com
 *
 */
public class TagUtilsTest {


	private Tag sibling1;
	private Tag sibling2;
	private Tag parent;
	private Tag sibling3;

	@Before
	public void setup() {
		sibling1 = new Tag("sibling1");
		sibling2 = new Tag("sibling2");
		sibling3 = new Tag("sibling3");
		parent = new Tag("parent");

	}

	/**
	 * Validates that the first next sibling is found
	 * @throws NoSiblingException
	 */
	@Test
	public void testSiblingAvailable1() throws NoSiblingException {
		parent.addChild(sibling1);
		parent.addChild(sibling2);
		parent.addChild(sibling3);
		Assert.assertEquals(sibling3, TagUtils.getInstance().getSibling(sibling2, 1));
	}
	/**
	 * Validates that the second next sibling is found
	 * @throws NoSiblingException
	 */
	@Test
	public void testSiblingAvailable2() throws NoSiblingException {
		parent.addChild(sibling1);
		parent.addChild(sibling2);
		parent.addChild(sibling3);
		Assert.assertEquals(sibling3, TagUtils.getInstance().getSibling(sibling1, 2));
	}
	/**
	 * Validates that the previous sibling is found
	 * @throws NoSiblingException
	 */
	@Test
	public void testSiblingAvailableMinus1() throws NoSiblingException {
		parent.addChild(sibling1);
		parent.addChild(sibling2);
		parent.addChild(sibling3);
		Assert.assertEquals(sibling1, TagUtils.getInstance().getSibling(sibling2, -1));
	}
	/**
	 * Validates that NoSiblingException is thrown when none is found.
	 * @throws NoSiblingException
	 */
	@Test(expected=NoSiblingException.class)
	public void testNoSiblingAvailable() throws NoSiblingException {
		parent.addChild(sibling2);
		parent.addChild(sibling3);
		TagUtils.getInstance().getSibling(sibling2, -1);
	}
}
