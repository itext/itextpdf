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
