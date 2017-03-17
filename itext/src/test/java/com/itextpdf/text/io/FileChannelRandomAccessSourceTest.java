/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
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
