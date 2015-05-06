package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

public class RandomAccessFileOrArrayTest {
	byte[] data;
	RandomAccessFileOrArray rafoa;
	
	@Before
	public void setUp() throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		for (int i = 0; i < 10000; i++){
			os.write(i);
		}
		data = os.toByteArray();
		rafoa = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(data));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPushback_byteByByte() throws Exception {
		
		Assert.assertEquals(data[0], (byte)rafoa.read());
		Assert.assertEquals(data[1], (byte)rafoa.read());
		byte pushBackVal = (byte)(data[1] + 42);
		rafoa.pushBack(pushBackVal);
		Assert.assertEquals(pushBackVal, (byte)rafoa.read());
		Assert.assertEquals(data[2], (byte)rafoa.read());
		Assert.assertEquals(data[3], (byte)rafoa.read());
		
	}

	@Test
	public void testSimple() throws Exception {
		for(int i = 0; i < data.length; i++){
			Assert.assertEquals(data[i], (byte)rafoa.read());
		}
	}

	@Test
	public void testSeek() throws Exception {
		RandomAccessFileOrArray rafoa = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(data));
		rafoa.seek(72);
		for(int i = 72; i < data.length; i++){
			Assert.assertEquals(data[i], (byte)rafoa.read());
		}
	}
	
	@Test
	public void testFilePositionWithPushback() throws Exception {
		RandomAccessFileOrArray rafoa = new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(data));
		long offset = 72;
		rafoa.seek(offset);
		Assert.assertEquals(offset, rafoa.getFilePointer());
		byte pushbackVal = 42;
		rafoa.pushBack(pushbackVal);
		Assert.assertEquals(offset-1, rafoa.getFilePointer());
	}
}
