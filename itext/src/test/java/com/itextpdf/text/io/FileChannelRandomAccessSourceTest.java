package com.itextpdf.text.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;

public class FileChannelRandomAccessSourceTest {
	byte[] data;
	File f;
	FileChannel channel;
	
	@Before
	public void setUp() throws Exception {
		TestResourceUtils.purgeTempFiles();

		Random r = new Random(42);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1];
		for (int i = 0; i < (1<<10); i++){
			r.nextBytes(buf);
			baos.write(buf[0]);
		}
		
		data = baos.toByteArray();
		f = TestResourceUtils.getBytesAsTempFile(data);

		FileInputStream is = new FileInputStream(f);
		channel = is.getChannel();
	}

	@After
	public void tearDown() throws Exception {
		if (channel != null)
			channel.close();
		
		TestResourceUtils.purgeTempFiles();
	}

	@Test
	public void testGet() throws Exception {
		FileChannelRandomAccessSource s = new FileChannelRandomAccessSource(channel);
		try{
			for(int i = 0; i < data.length; i++){
				int ch = s.get(i);
				Assert.assertFalse("EOF hit unexpectedly at " + i + " out of " + data.length, ch == -1);
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
		FileChannelRandomAccessSource s = new FileChannelRandomAccessSource(channel);
		try{
			int pos = 0;
			int count = s.get(pos, chunk, 0, chunk.length-1);
			while (count != -1){
				assertArrayEqual(data, pos, chunk, 0, count);
				pos += count;
				count = s.get(pos, chunk, 0, chunk.length-1);
			}
			
			Assert.assertEquals(-1, s.get(pos, chunk, 0, chunk.length));
		} finally {
			s.close();
		}
	}
	
	@Test
	public void testGetArrayPastEOF() throws Exception{
		FileChannelRandomAccessSource s = new FileChannelRandomAccessSource(channel);
		try{
			byte[] chunk = new byte[(int)channel.size() * 2];
			int len = s.get(0, chunk, 0, chunk.length);
			Assert.assertEquals((int)channel.size(), len);
		} finally {
			s.close();
		}
	}
}
