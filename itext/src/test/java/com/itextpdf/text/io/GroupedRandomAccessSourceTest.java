package com.itextpdf.text.io;

import java.io.ByteArrayOutputStream;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GroupedRandomAccessSourceTest {
	byte[] data;
	
	@Before
	public void setUp() throws Exception {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < 100; i++){
			baos.write((byte)i);
		}
		
		data = baos.toByteArray();
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testGet() throws Exception {
		ArrayRandomAccessSource source1 = new ArrayRandomAccessSource(data);
		ArrayRandomAccessSource source2 = new ArrayRandomAccessSource(data);
		ArrayRandomAccessSource source3 = new ArrayRandomAccessSource(data);
		
		RandomAccessSource[] inputs = new RandomAccessSource[]{
				source1, source2, source3
		};
		
		GroupedRandomAccessSource grouped = new GroupedRandomAccessSource(inputs);
		
		Assert.assertEquals(source1.length() + source2.length() + source3.length(), grouped.length());

		Assert.assertEquals(source1.get(99),  grouped.get(99));
		Assert.assertEquals(source2.get(0),  grouped.get(100));
		Assert.assertEquals(source2.get(1),  grouped.get(101));
		Assert.assertEquals(source1.get(99),  grouped.get(99));
		Assert.assertEquals(source3.get(99),  grouped.get(299));

		Assert.assertEquals(-1, grouped.get(300));
	}

	private byte[] rangeArray(int start, int count){
		byte[] rslt = new byte[count];
		for(int i = 0; i < count; i++){
			rslt[i] = (byte)(i + start);
		}
		return rslt;
	}
	
	private void assertArrayEqual(byte[] a, int offa, byte[] b, int offb, int len){
		for(int i = 0; i < len; i++){
			if (a[i+offa] != b[i + offb]){
				throw new AssertionFailedError("Differ at index " + (i+offa) + " and " + (i + offb) + " -> " + a[i+offa] + " != " + b[i + offb]);
			}
			
		}
	}
	
	@Test
	public void testGetArray() throws Exception {
		ArrayRandomAccessSource source1 = new ArrayRandomAccessSource(data); // 0 - 99
		ArrayRandomAccessSource source2 = new ArrayRandomAccessSource(data); // 100 - 199
		ArrayRandomAccessSource source3 = new ArrayRandomAccessSource(data); // 200 - 299
		
		RandomAccessSource[] inputs = new RandomAccessSource[]{
				source1, source2, source3
		};
		
		GroupedRandomAccessSource grouped = new GroupedRandomAccessSource(inputs);

		byte[] out = new byte[500];

		Assert.assertEquals(300, grouped.get(0, out, 0, 300));
		assertArrayEqual(rangeArray(0, 100), 0, out, 0, 100);
		assertArrayEqual(rangeArray(0, 100), 0, out, 100, 100);
		assertArrayEqual(rangeArray(0, 100), 0, out, 200, 100);
		
		Assert.assertEquals(300, grouped.get(0, out, 0, 301));
		assertArrayEqual(rangeArray(0, 100), 0, out, 0, 100);
		assertArrayEqual(rangeArray(0, 100), 0, out, 100, 100);
		assertArrayEqual(rangeArray(0, 100), 0, out, 200, 100);
		
		Assert.assertEquals(100, grouped.get(150, out, 0, 100));
		assertArrayEqual(rangeArray(50, 50), 0, out, 0, 50);
		assertArrayEqual(rangeArray(0, 50), 0, out, 50, 50);
	}
	
	@Test
	public void testRelease() throws Exception{
		
		ArrayRandomAccessSource source1 = new ArrayRandomAccessSource(data); // 0 - 99
		ArrayRandomAccessSource source2 = new ArrayRandomAccessSource(data); // 100 - 199
		ArrayRandomAccessSource source3 = new ArrayRandomAccessSource(data); // 200 - 299
		
		RandomAccessSource[] sources = new RandomAccessSource[]{
				source1, source2, source3
		};
		
		final RandomAccessSource[] current = new RandomAccessSource[]{null};
		final int[] openCount = new int[]{0};
		GroupedRandomAccessSource grouped = new GroupedRandomAccessSource(sources){
			protected void sourceReleased(RandomAccessSource source) throws java.io.IOException {
				openCount[0]--;
				if (current[0] != source)
					throw new AssertionFailedError("Released source isn't the current source");
				current[0] = null;
			}
			
			protected void sourceInUse(RandomAccessSource source) throws java.io.IOException {
				if (current[0] != null)
					throw new AssertionFailedError("Current source wasn't released properly");
				openCount[0]++;
				current[0] = source;
			}
		};

		grouped.get(250);
		grouped.get(251);
		Assert.assertEquals(1, openCount[0]);
		grouped.get(150);
		grouped.get(151);
		Assert.assertEquals(1, openCount[0]);
		grouped.get(50);
		grouped.get(51);
		Assert.assertEquals(1, openCount[0]);
		grouped.get(150);
		grouped.get(151);
		Assert.assertEquals(1, openCount[0]);
		grouped.get(250);
		grouped.get(251);
		Assert.assertEquals(1, openCount[0]);

		grouped.close();
	}
}
