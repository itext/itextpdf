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
