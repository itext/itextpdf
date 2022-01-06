/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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
