/**
 * 
 */
package com.itextpdf.text;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author redlab
 *
 */
public class ChunkTest {

	
	/**
	 * 
	 */
	private static final String _4SPACES = "    4spaces    ";
	private static final String _TAB = "\t4spaces    ";

	@Test
	public void prependingWhitspaces() {
		Chunk c = new Chunk(_4SPACES);
		Assert.assertEquals("difference in string", _4SPACES, c.getContent());
	}
	@Test
	public void prependingTab() {
		Chunk c = new Chunk(_TAB);
		Assert.assertEquals("difference in string", "4spaces    ", c.getContent());
	}
}
