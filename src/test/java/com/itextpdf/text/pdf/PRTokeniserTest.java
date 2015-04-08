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
