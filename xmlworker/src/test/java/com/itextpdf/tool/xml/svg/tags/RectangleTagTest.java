package com.itextpdf.tool.xml.svg.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;


import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.svg.graphic.Rectangle;
import com.itextpdf.tool.xml.svg.tags.RectangleTag;

public class RectangleTagTest {
	@Test
	public void testEndNoAttributes() {
		Map<String, String> attributes = new HashMap<String, String>();
		checkRectangleTagWithourResult(attributes);
	}

	@Test
	public void testEndNoWidth() {
		checkRectangleTagWithourResult(getAttributes("a", "b", null, "h", "rx",
				"ry"));
	}

	@Test
	public void testEndZeroWidth() {
		checkRectangleTagWithourResult(getAttributes("a", "b", "0", "100",
				"rx", "ry"));
	}

	@Test
	public void testEndWrongWidth() {
		checkRectangleTagWithourResult(getAttributes("a", "b", "-10", "100",
				"rx", "ry"));
	}

	@Test
	public void testEndNoHeight() {
		checkRectangleTagWithourResult(getAttributes("a", "b", "w", null, "rx",
				"ry"));
	}

	@Test
	public void testEndZeroHeight() {
		checkRectangleTagWithourResult(getAttributes("a", "b", "100", "0",
				"rx", "ry"));
	}

	@Test
	public void testEndWrongHeight() {
		checkRectangleTagWithourResult(getAttributes("a", "b", "100", "-10",
				"rx", "ry"));
	}

	@Test
	public void testEndAllWrong() {
		checkRectangleTagWithValues(
				getAttributes("a", "b", "100", "10", "rx", "ry"), true, 0, 0,
				100, 10, 0, 0);
	}

	@Test
	public void testEndAllMissing() {
		checkRectangleTagWithValues(
				getAttributes(null, null, "100", "10", null, null), true, 0, 0,
				100, 10, 0, 0);
	}

	@Test
	public void testEndAllNegative() {
		checkRectangleTagWithValues(
				getAttributes("-10", "-10", "100", "10", "-20", "-20"), true,
				-10, -10, 100, 10, 0, 0);
	}

	@Test
	public void testRxIsMissing() {
		checkRectangleTagWithValues(
				getAttributes("10", "10", "100", "10", null, "20"), true, 10,
				10, 100, 10, 20, 20);
	}

	@Test
	public void testRyIsMissing() {
		checkRectangleTagWithValues(
				getAttributes("10", "10", "100", "10", "20", null), true, 10,
				10, 100, 10, 20, 20);
	}

	@Test
	public void testRxIsMissingAndRyWrong() {
		checkRectangleTagWithValues(
				getAttributes("10", "10", "100", "10", null, "-20"), true, 10,
				10, 100, 10, 0, 0);
	}

	@Test
	public void testRyIsMissingAndRxWrong() {
		checkRectangleTagWithValues(
				getAttributes("10", "10", "100", "10", "a", null), true, 10,
				10, 100, 10, 0, 0);
	}

	@Test
	public void testRyWrong() {
		checkRectangleTagWithValues(
				getAttributes("10", "10", "100", "10", "10", "-20"), true, 10,
				10, 100, 10, 10, 10);
	}

	@Test
	public void testRxWrong() {
		checkRectangleTagWithValues(
				getAttributes("10", "10", "100", "10", "a", "5"), true, 10, 10,
				100, 10, 5, 5);
	}

	private Map<String, String> getAttributes(String x, String y, String w,
			String h, String rx, String ry) {
		Map<String, String> attributes = new HashMap<String, String>();
		if (x != null)
			attributes.put("x", x);
		if (y != null)
			attributes.put("y", y);
		if (w != null)
			attributes.put("width", w);
		if (h != null)
			attributes.put("height", h);
		if (rx != null)
			attributes.put("rx", rx);
		if (ry != null)
			attributes.put("ry", ry);
		return attributes;
	}

	private void checkRectangleTagWithourResult(Map<String, String> attributes) {
		checkRectangleTagWithValues(attributes, false, 0f, 0f, 0f, 0f, 0f, 0f);
	}

	private void checkRectangleTagWithValues(Map<String, String> attributes,
			boolean draw, float x, float y, float width, float height,
			float rx, float ry) {
		RectangleTag rectangleTag = new RectangleTag();
		Tag tag = new Tag("rect", attributes);
		List<Element> l = rectangleTag.end(null, tag, null);
		Assert.assertNotNull(l);

		if (draw) {
			Assert.assertEquals(1, l.size());
			Rectangle rectangle = (Rectangle) l.get(0);
			Assert.assertEquals(x, rectangle.getX());
			Assert.assertEquals(y, rectangle.getY());
			Assert.assertEquals(width, rectangle.getWidth());
			Assert.assertEquals(height, rectangle.getHeight());
			Assert.assertEquals(rx, rectangle.getRx());
			Assert.assertEquals(ry, rectangle.getRy());
		} else {
			Assert.assertEquals(0, l.size());
		}
	}
}
