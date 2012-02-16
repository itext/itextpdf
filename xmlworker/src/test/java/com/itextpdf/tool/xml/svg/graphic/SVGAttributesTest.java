package com.itextpdf.tool.xml.svg.graphic;

import junit.framework.Assert;

import org.junit.Test;

import com.itextpdf.tool.xml.svg.graphic.SVGAttributes;


public class SVGAttributesTest {
	@Test
	public void isValidDashArrayTest() {
		checkValidDashArray("none", true);
		checkValidDashArray("NONE", true);
		checkValidDashArray("2,3,4", true);
		checkValidDashArray("2 3,4", true);
		checkValidDashArray("2   3", true);
		checkValidDashArray("0", true);
		
		checkValidDashArray("1,,2", false);
		checkValidDashArray("1,A,2", false);
		checkValidDashArray("1,-1,2", false);
		checkValidDashArray(",1,-1,2", false);
		checkValidDashArray("1,-1,", false);
	}
	
	private void checkValidDashArray(String list, boolean expected){
		Assert.assertEquals(expected, SVGAttributes.isValidDashArray(list));
	}
}
