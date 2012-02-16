package com.itextpdf.tool.xml.svg.tags;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.itextpdf.tool.xml.svg.tags.PathTag;


public class PathTagTest {
	@Test
	public void testSplitPath() {
		String d = "M100,200L 150 175 \n\r\t A2.5,-30";

		ArrayList<String> expected = new ArrayList<String>();
		expected.add("M");
		expected.add("100");
		expected.add("200");
		expected.add("L");
		expected.add("150");
		expected.add("175");
		expected.add("A");
		expected.add("2.5");
		expected.add("-30");

		PathTag path = new PathTag();
		List<String> list = path.splitPath(d);
		Assert.assertNotNull(list);
		Assert.assertEquals(expected.size(), list.size());
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertEquals(expected.get(i), list.get(i));
		}
	}
}
