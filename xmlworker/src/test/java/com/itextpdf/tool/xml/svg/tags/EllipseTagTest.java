package com.itextpdf.tool.xml.svg.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;


import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.svg.graphic.Ellipse;
import com.itextpdf.tool.xml.svg.tags.EllipseTag;

public class EllipseTagTest {
	@Test
	public void testEndNoAttributes() {
		Map<String, String> attributes = new HashMap<String, String>();
		checkEllipseTagNoResult(attributes);
	}

	@Test
	public void testEndCorrectAttributes() {
		checkEllipseTagWithValues(getAttributes("8", "7", "6", "5"), true, 8,
				7, 6, 5);
	}

	@Test
	public void testEndNoXY() {
		checkEllipseTagWithValues(getAttributes(null, null, "6", "4"), true, 0,
				0, 6, 4);
	}

	@Test
	public void testEndWrongXY() {
		checkEllipseTagWithValues(getAttributes("wrong", "wrong", "6", "5"),
				true, 0, 0, 6, 5);
	}

	@Test
	public void testEndWrongRx() {
		checkEllipseTagNoResult(getAttributes("1", "1", "wrong", "5"));
	}

	@Test
	public void testEndWrongRy() {
		checkEllipseTagNoResult(getAttributes("1", "1", "4", "wrong"));
	}

	@Test
	public void testEndRxZero() {
		checkEllipseTagNoResult(getAttributes("1", "1", "0", "5"));
	}

	@Test
	public void testEndRyZero() {
		checkEllipseTagNoResult(getAttributes("1", "1", "4", "0"));
	}

	private Map<String, String> getAttributes(String x, String y, String rx,
			String ry) {
		Map<String, String> attributes = new HashMap<String, String>();
		if (x != null)
			attributes.put(EllipseTag.CX, x);
		if (y != null)
			attributes.put(EllipseTag.CY, y);
		if (rx != null)
			attributes.put(EllipseTag.RX, rx);
		if (ry != null)
			attributes.put(EllipseTag.RY, ry);
		return attributes;
	}

	private void checkEllipseTagNoResult(Map<String, String> attributes) {
		checkEllipseTagWithValues(attributes, false, 0f, 0f, 0f, 0f);
	}

	private void checkEllipseTagWithValues(Map<String, String> attributes,
			boolean draw, float x, float y, float rx, float ry) {
		EllipseTag ellipseTag = new EllipseTag();
		Tag tag = new Tag("", attributes);
		List<Element> l = ellipseTag.end(null, tag, null);
		Assert.assertNotNull(l);

		if (draw) {
			Assert.assertEquals(1, l.size());
			Ellipse ellipse = (Ellipse) l.get(0);
			Assert.assertEquals(x, ellipse.getX());
			Assert.assertEquals(y, ellipse.getY());
			Assert.assertEquals(rx, ellipse.getRx());
			Assert.assertEquals(ry, ellipse.getRy());
		} else {
			Assert.assertEquals(0, l.size());
		}
	}
}
