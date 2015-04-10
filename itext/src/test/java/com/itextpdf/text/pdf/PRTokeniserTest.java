package com.itextpdf.text.pdf;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PRTokeniser.TokenType;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

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
