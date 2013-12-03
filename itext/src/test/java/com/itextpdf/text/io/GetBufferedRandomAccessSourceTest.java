package com.itextpdf.text.io;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetBufferedRandomAccessSourceTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSmallSizedFile() throws Exception { 
		// we had a problem if source was less than 4 characters in length - would result in array index out of bounds problems on get()
		byte[] data = new byte[]{42};
		ArrayRandomAccessSource arrayRAS = new ArrayRandomAccessSource(data);
		GetBufferedRandomAccessSource bufferedRAS = new GetBufferedRandomAccessSource(arrayRAS);
		Assert.assertEquals(42, bufferedRAS.get(0));
	}

}
