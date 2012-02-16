package com.itextpdf.tool.xml.svg.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;


import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.svg.graphic.Circle;
import com.itextpdf.tool.xml.svg.tags.CircleTag;

public class CircleTagTest {
	@Test
	public void testEndNoAttributes() {
		Map<String, String> attributes = new HashMap<String, String>();
		checkCircleTagNoResult(attributes);
	}

	@Test
	public void testEndCorrectAttributes() {
		checkCircleTagWithValues(getAttributes("8", "7", "6"), true, 8, 7, 6);
	}

	@Test
	public void testEndNoXY() {
		checkCircleTagWithValues(getAttributes(null, null, "6"), true, 0, 0, 6);
	}

	@Test
	public void testEndWrongXY() {
		checkCircleTagWithValues(getAttributes("wrong", "wrong", "6"), true, 0,
				0, 6);
	}

	@Test
	public void testEndWrongRadius() {
		checkCircleTagNoResult(getAttributes("1", "1", "wrong"));
	}

	@Test
	public void testEndZeroRadius() {
		checkCircleTagNoResult(getAttributes("1", "1", "0"));
	}

	@Test
	public void testEndNegativeRadius() {
		checkCircleTagNoResult(getAttributes("1", "1", "-10"));
	}

	private Map<String, String> getAttributes(String x, String y, String r) {
		Map<String, String> attributes = new HashMap<String, String>();
		if (x != null)
			attributes.put(CircleTag.CX, x);
		if (y != null)
			attributes.put(CircleTag.CY, y);
		if (r != null)
			attributes.put(CircleTag.RADIUS, r);
		return attributes;
	}

	private void checkCircleTagNoResult(Map<String, String> attributes) {
		checkCircleTagWithValues(attributes, false, 0f, 0f, 0f);
	}

	private void checkCircleTagWithValues(Map<String, String> attributes,
			boolean draw, float x, float y, float r) {
		CircleTag circleTag = new CircleTag();
		Tag tag = new Tag("circle", attributes);
		List<Element> l = circleTag.end(null, tag, null);
		Assert.assertNotNull(l);

		if (draw) {
			Assert.assertEquals(1, l.size());
			Circle circle = (Circle) l.get(0);
			Assert.assertEquals(x, circle.getX());
			Assert.assertEquals(y, circle.getY());
			Assert.assertEquals(r, circle.getRadius());
		} else {
			Assert.assertEquals(0, l.size());
		}
	}
}
