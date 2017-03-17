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
package com.itextpdf.text.pdf;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.itextpdf.text.pdf.PRTokeniser.TokenType;
import java.io.IOException;

public class PRTokeniserTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private void checkTokenTypes(String data, TokenType... expectedTypes) throws Exception {
		PRTokeniser tok = new PRTokeniser(new RandomAccessFileOrArray(data.getBytes()));
		
		for(int i = 0; i < expectedTypes.length; i++){
			tok.nextValidToken();
			//System.out.println(tok.getTokenType() + " -> " + tok.getStringValue());
			Assert.assertEquals("Position " + i, expectedTypes[i], tok.getTokenType());
		}
	}

	private void checkNumberValue(String data, String expectedValue) throws IOException {
		PRTokeniser tok = new PRTokeniser(new RandomAccessFileOrArray(data.getBytes()));

		tok.nextValidToken();
		Assert.assertEquals("Wrong type", TokenType.NUMBER, tok.getTokenType());
		Assert.assertEquals("Wrong multiple minus signs number handling", expectedValue, tok.getStringValue());
	}
	
	@Test
	public void testOneNumber() throws Exception {
		checkTokenTypes(
				"/Name1 70",
				TokenType.NAME,
				TokenType.NUMBER,
				TokenType.ENDOFFILE
		);
	}

	@Test
	public void testTwoNumbers() throws Exception {
		checkTokenTypes(
				"/Name1 70/Name 2",
				TokenType.NAME, 
				TokenType.NUMBER,
				TokenType.NAME,
				TokenType.NUMBER,
				TokenType.ENDOFFILE
		);
	}

	@Test
	public void testMultipleMinusSignsRealNumber() throws Exception {
		checkNumberValue("----40.25", "-40.25");
	}

	@Test
	public void testMultipleMinusSignsIntegerNumber() throws Exception {
		checkNumberValue("--9", "0");
	}

	@Test
	public void test() throws Exception {
		checkTokenTypes(
				"<</Size 70/Root 46 0 R/Info 44 0 R/ID[<8C2547D58D4BD2C6F3D32B830BE3259D><8F69587888569A458EB681A4285D5879>]/Prev 116 >>",
				TokenType.START_DIC, 
				TokenType.NAME, 
				TokenType.NUMBER,
				TokenType.NAME,
				TokenType.REF,
				TokenType.NAME,
				TokenType.REF,
				TokenType.NAME,
				TokenType.START_ARRAY,
				TokenType.STRING,
				TokenType.STRING,
				TokenType.END_ARRAY,
				TokenType.NAME,
				TokenType.NUMBER,
				TokenType.END_DIC,
				TokenType.ENDOFFILE
				
		);
		
	}

}
