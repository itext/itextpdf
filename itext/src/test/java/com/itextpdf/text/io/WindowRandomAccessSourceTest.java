package com.itextpdf.text.io;

import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WindowRandomAccessSourceTest {
	ArrayRandomAccessSource source;
	byte[] data;
	
	@Before
	public void setUp() throws Exception {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < 100; i++){
			baos.write((byte)i);
		}
		
		data = baos.toByteArray();
		source = new ArrayRandomAccessSource(data);
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testBasics() throws Exception {
		WindowRandomAccessSource window = new WindowRandomAccessSource(source, 7, 17);
		
		Assert.assertEquals(17, window.length());
		byte[] out = new byte[45];
		Assert.assertEquals(17, window.get(0, out, 0, 17));
		
		Assert.assertEquals(7, window.get(0));
		Assert.assertEquals(17, window.get(10));
		Assert.assertEquals(-1, window.get(17));
		
		Assert.assertEquals(17, window.get(0, out, 0, 45));
		for (int i = 0; i < 17; i++) {
			Assert.assertEquals(data[i+7], out[i]);
		}
	}

}
