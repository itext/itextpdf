/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.html;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.itextpdf.text.BaseColor;


public class WebColorTest {

	/**
	 * Throw a bunch of equivalent colors at WebColors and ensure
	 * that the return values really are equivalent.
	 * @throws Exception
	 */
	@Test
	public void goodColorTests() throws Exception {
		String colors[] = {
				"#00FF00", "00FF00", "#0F0", "0F0", "lime", "rgb(0,255,0 )"
		};
		// TODO webColor creates colors with a zero alpha channel (save "transparent"), BaseColor's
		// 3-param constructor creates them with a 0xFF alpha channel.  Which is right?!
		BaseColor testCol = new BaseColor(0, 255, 0);
		for (String colStr : colors) {
			BaseColor curCol = WebColors.getRGBColor(colStr);
			assertTrue(dumpColor(testCol) + "!=" + dumpColor(curCol), testCol.equals(curCol));
		}
	}

	@Test
	public void moreColorTest() {
	    String colorStr = "#888";
	    String colorStrLong = "#888888";
	    assertEquals("Oh Nooo colors are different", WebColors.getRGBColor(colorStr), WebColors.getRGBColor(colorStrLong));
	}

	private String dumpColor(BaseColor col) {
		StringBuffer colBuf = new StringBuffer();
		colBuf.append("r:");
		colBuf.append(col.getRed());
		colBuf.append(" g:");
		colBuf.append(col.getGreen());
		colBuf.append(" b:");
		colBuf.append(col.getBlue());
		colBuf.append(" a:");
		colBuf.append(col.getAlpha());

		return colBuf.toString();
	}

	@Test
	public void badColorTests() throws Exception {
		String badColors[] = {
				"", null, "#xyz", "#12345", "notAColor"
		};

		for (String curStr : badColors) {
			try {
				// we can ignore the return value that'll never happen here
				WebColors.getRGBColor(curStr);

				assertTrue("getRGBColor should have thrown for: " + curStr, false);
			} catch (IllegalArgumentException e) {
				// Non-null bad colors will throw an illArgEx
				assertTrue( curStr != null );
				// good, it was supposed to throw
			} catch (NullPointerException e) {
				// the null color will NPE
				assertTrue( curStr == null);
			}
		}
	}


}
