package com.itextpdf.tool.xml.svg.tags;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.itextpdf.tool.xml.svg.tags.TagUtils;


public class TagUtilsTest {
	@Test
	public void splitValueList() {
		checksplitValue(" 2 , 3 , 4 ", new String[] { "2", "3", "4" });
		checksplitValue("2,,4", new String[] { "2", "", "4" });
		checksplitValue("2 3    4", new String[] { "2", "3", "4" });
		checksplitValue("2,3  4", new String[] { "2", "3", "4" });
		checksplitValue(",2, ", new String[] { "", "2", "" });
	}

	private void checksplitValue(String name, String[] result) {
		List<String> list = TagUtils.splitValueList(name);
		if (name == null) {
			Assert.assertNull(list);
		} else {
			Assert.assertNotNull(list);
		}

		Assert.assertEquals(result.length, list.size());
		for (int i = 0; i < result.length; i++) {
			Assert.assertEquals(result[i], list.get(i));
		}

	}
}
