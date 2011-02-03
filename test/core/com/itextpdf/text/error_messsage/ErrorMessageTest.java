package com.itextpdf.text.error_messsage;

import junit.framework.Assert;

import org.junit.Test;

import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * @author redlab_b
 * 
 */
public class ErrorMessageTest {

	/*
	 * using 1.unsupported.jpeg.marker.2={1}: unsupported JPEG marker: {2}
	 */
	@Test
	public void verifyParamReplacementNoParam() {
		Assert.assertEquals("{1}: unsupported JPEG marker: {2}",
				MessageLocalization
						.getComposedMessage("1.unsupported.jpeg.marker.2"));
	}

	@Test
	public void verifyParamReplacement1Param() {
		Assert.assertEquals("one: unsupported JPEG marker: {2}",
				MessageLocalization.getComposedMessage(
						"1.unsupported.jpeg.marker.2", "one"));
	}

	@Test
	public void verifyParamReplacement2Param() {
		Assert.assertEquals("one: unsupported JPEG marker: two",
				MessageLocalization.getComposedMessage(
						"1.unsupported.jpeg.marker.2", "one", "two"));
	}

	@Test
	public void verifyParamReplacementXParam() {
		Assert.assertEquals("one: unsupported JPEG marker: two",
				MessageLocalization.getComposedMessage(
						"1.unsupported.jpeg.marker.2", "one", "two", "three",
						"four", "five"));
	}
}
