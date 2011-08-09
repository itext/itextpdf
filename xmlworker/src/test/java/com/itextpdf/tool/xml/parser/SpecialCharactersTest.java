/**
 *
 */
package com.itextpdf.tool.xml.parser;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.tool.xml.parser.state.InsideTagHTMLState;
import com.itextpdf.tool.xml.parser.state.SpecialCharState;

/**
 * @author itextpdf.com
 *
 */
public class SpecialCharactersTest {

	private String regHtml;
	private int reg;
	private SpecialCharState scState;
	private XMLParser parser;
	private String regStr;
	private InsideTagHTMLState itState;
	private int hex;
	private String e;
	private char accent;

	@Before
	public void setUp() {
		parser = new XMLParser();
		scState = new SpecialCharState(parser);
		itState = new InsideTagHTMLState(parser);
		reg = 174;
		regHtml = "&reg";
		regStr = "\u00ae";
		hex = 0x00ae;
		e = "Travailleur ou ch\u00f4meur, ouvrier, employ\u00e9 ou cadre, homme ou femme, jeune ou moins jeune,... au Syndicat lib\u00e9ral vous n'\u00eates pas un num\u00e9ro et vous pouvez compter sur l'aide de l'ensemble de nos collaborateurs.";
	}

	@Test
	public void testIntCode() throws UnsupportedEncodingException {
		itState.process((char) hex);
		String str = parser.memory().current().toString();
		System.out.println(str);
		Assert.assertEquals(hex, str.charAt(0));
	}

	@Test
	public void testHtmlChar() throws UnsupportedEncodingException {
		scState.process('r');
		scState.process('e');
		scState.process('g');
		scState.process(';');
		String str = parser.memory().current().toString();
		Assert.assertEquals(hex, str.charAt(0));
	}

	@Test
	public void testEacuteCcedilleEtc() throws UnsupportedEncodingException {
		for (int i = 0; i < e.length(); i++) {
			itState.process((char) e.codePointAt(i));
		}
		String str = parser.memory().current().toString();
		Assert.assertEquals(e, str);
	}
}
