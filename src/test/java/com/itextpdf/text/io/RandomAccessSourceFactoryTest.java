package com.itextpdf.text.io;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;

public class RandomAccessSourceFactoryTest {

	private File testFile;
	
	@Before
	public void setUp() throws Exception {
		TestResourceUtils.purgeTempFiles();
		testFile = TestResourceUtils.getBytesAsTempFile(new byte[]{0,1,1,0});
	}

	@After
	public void tearDown() throws Exception {
		TestResourceUtils.purgeTempFiles();
	}

//  Test note: We can't actually test this inside the same JVM (locking happens per-process)	
//	@Test
//	public void testExclusive() throws Exception {
//		RandomAccessSource first = new RandomAccessSourceFactory().setExclusivelyLockFile(true).createBestSource(testFile.getAbsolutePath());
//		try{
//			RandomAccessSource second = new RandomAccessSourceFactory().createBestSource(testFile.getAbsolutePath());
//			second.close();
//		} catch (IOException expected){
//			return;
//		} finally {
//			first.close();
//		}
//		
//		throw new AssertionError("Exclusive lock not obtained");
//	}
//
//	@Test
//	public void testNotExclusive() throws Exception {
//		RandomAccessSource first = new RandomAccessSourceFactory().createBestSource(testFile.getAbsolutePath());
//		try{
//			RandomAccessSource second = new RandomAccessSourceFactory().createBestSource(testFile.getAbsolutePath());
//			second.close();
//		} finally {
//			first.close();
//		}
//	}
	
}
