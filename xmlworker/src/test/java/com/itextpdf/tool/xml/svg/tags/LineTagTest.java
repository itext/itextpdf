package com.itextpdf.tool.xml.svg.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;


import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.svg.graphic.Line;
import com.itextpdf.tool.xml.svg.tags.LineTag;

public class LineTagTest {
	@Test
	public void testEndNoAttributes() {
		Map<String, String> attributes = new HashMap<String, String>();
		checkLineTagWithValues(attributes, 0, 0, 0, 0);
	}

	@Test
	public void testEndCorrectAttributes() {
		checkLineTagWithValues(getAttributes("8", "7", "6", "5"), 8, 7, 6, 5);
	}

	@Test
	public void testEndWrongCoordinates() {
		checkLineTagWithValues(getAttributes("a", "b", "c", "d"), 0, 0, 0, 0);
	}

	private Map<String, String> getAttributes(String x1, String y1, String x2,
			String y2) {
		Map<String, String> attributes = new HashMap<String, String>();
		if (x1 != null)
			attributes.put(LineTag.X1, x1);
		if (y1 != null)
			attributes.put(LineTag.Y1, y1);
		if (x2 != null)
			attributes.put(LineTag.X2, x2);
		if (y2 != null)
			attributes.put(LineTag.Y2, y2);
		return attributes;
	}

	private void checkLineTagWithValues(Map<String, String> attributes,
			float x1, float y1, float x2, float y2) {
		LineTag lineTag = new LineTag();
		Tag tag = new Tag("", attributes);
		List<Element> l = lineTag.end(null, tag, null);
		Assert.assertNotNull(l);
		Assert.assertEquals(1, l.size());
		Line line = (Line) l.get(0);
		Assert.assertEquals(x1, line.getX1());
		Assert.assertEquals(y1, line.getY1());
		Assert.assertEquals(x2, line.getX2());
		Assert.assertEquals(y2, line.getY2());
	}
}
