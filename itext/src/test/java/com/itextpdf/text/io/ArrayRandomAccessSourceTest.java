package com.itextpdf.text.io;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArrayRandomAccessSourceTest {
	byte[] data;
	
	@Before
	public void setUp() throws Exception {
		Random r = new Random(42);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1];
		for (int i = 0; i < (1<<10); i++){
			r.nextBytes(buf);
			baos.write(buf[0]);
		}
		
		data = baos.toByteArray();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGet() throws Exception {
		ArrayRandomAccessSource s = new ArrayRandomAccessSource(data);
		try{
			for(int i = 0; i < data.length; i++){
				int ch = s.get(i);
				Assert.assertFalse(ch == -1);
				Assert.assertEquals("Position " + i, data[i], (byte)ch);
			}
			Assert.assertEquals(-1, s.get(data.length));
		} finally {
			s.close();
		}
	}

	private void assertArrayEqual(byte[] a, int offa, byte[] b, int offb, int len){
		for(int i = 0; i < len; i++){
			if (a[i+offa] != b[i + offb]){
				throw new AssertionFailedError("Differ at index " + (i+offa) + " and " + (i + offb));
			}
			
		}
	}
	
	@Test
	public void testGetArray() throws Exception {
		byte[] chunk = new byte[257];
		ArrayRandomAccessSource s = new ArrayRandomAccessSource(data);
		try{
			int pos = 0;
			int count = s.get(pos, chunk, 0, chunk.length);
			while (count != -1){
				assertArrayEqual(data, pos, chunk, 0, count);
				pos += count;
				count = s.get(pos, chunk, 0, chunk.length);
			}
			
			Assert.assertEquals(-1, s.get(pos, chunk, 0, chunk.length));
		} finally {
			s.close();
		}
	}
}
