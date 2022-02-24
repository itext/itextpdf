/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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

import java.util.Properties;
import java.util.StringTokenizer;
import java.util.HashMap;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;

/**
 * A class that contains some utilities to parse HTML attributes and content.
 * @since 5.0.6 (some of these methods used to be in the Markup class)
 * @deprecated since 5.5.2
 */
@Deprecated
public class HtmlUtilities {

	/**
	 * a default value for font-size 
     * @since 2.1.3
     */
	public static final float DEFAULT_FONT_SIZE = 12f;

    private static HashMap<String,Float> sizes = new HashMap<String,Float>();
    static {
        sizes.put("xx-small", new Float(4));
        sizes.put("x-small", new Float(6));
        sizes.put("small", new Float(8));
        sizes.put("medium", new Float(10));
        sizes.put("large", new Float(13));
        sizes.put("x-large", new Float(18));
        sizes.put("xx-large", new Float(26));
    }

	/**
	 * Parses a length.
	 * 
	 * @param string
	 *            a length in the form of an optional + or -, followed by a
	 *            number and a unit.
	 * @return a float
	 */

	public static float parseLength(String string) {
		return parseLength(string, DEFAULT_FONT_SIZE);
	}

	/**
	 * New method contributed by: Lubos Strapko
	 * 
	 * @since 2.1.3
	 */
	public static float parseLength(String string, float actualFontSize) {
		if (string == null)
			return 0f;
        Float fl = sizes.get(string.toLowerCase());
        if (fl != null)
            return fl.floatValue();
		int pos = 0;
		int length = string.length();
		boolean ok = true;
		while (ok && pos < length) {
			switch (string.charAt(pos)) {
			case '+':
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
				pos++;
				break;
			default:
				ok = false;
			}
		}
		if (pos == 0)
			return 0f;
		if (pos == length)
			return Float.parseFloat(string + "f");
		float f = Float.parseFloat(string.substring(0, pos) + "f");
		string = string.substring(pos);
		// inches
		if (string.startsWith("in")) {
			return f * 72f;
		}
		// centimeters
		if (string.startsWith("cm")) {
			return (f / 2.54f) * 72f;
		}
		// millimeters
		if (string.startsWith("mm")) {
			return (f / 25.4f) * 72f;
		}
		// picas
		if (string.startsWith("pc")) {
			return f * 12f;
		}
		// 1em is equal to the current font size
		if (string.startsWith("em")) {
			return f * actualFontSize;
		}
		// one ex is the x-height of a font (x-height is usually about half the
		// font-size)
		if (string.startsWith("ex")) {
			return f * actualFontSize / 2;
		}
		// default: we assume the length was measured in points
		return f;
	}

	/**
	 * Converts a <CODE>BaseColor</CODE> into a HTML representation of this <CODE>
	 * BaseColor</CODE>.
	 * 
	 * @param s
	 *            the <CODE>BaseColor</CODE> that has to be converted.
	 * @return the HTML representation of this <COLOR>BaseColor </COLOR>
	 */

	public static BaseColor decodeColor(String s) {
		if (s == null)
			return null;
		s = s.toLowerCase().trim();
		try {
			return WebColors.getRGBColor(s);
		}
		catch(IllegalArgumentException iae) {
			return null;
		}
	}

	/**
	 * This method parses a String with attributes and returns a Properties
	 * object.
	 * 
	 * @param string
	 *            a String of this form: 'key1="value1"; key2="value2";...
	 *            keyN="valueN" '
	 * @return a Properties object
	 */
	public static Properties parseAttributes(String string) {
		Properties result = new Properties();
		if (string == null)
			return result;
		StringTokenizer keyValuePairs = new StringTokenizer(string, ";");
		StringTokenizer keyValuePair;
		String key;
		String value;
		while (keyValuePairs.hasMoreTokens()) {
			keyValuePair = new StringTokenizer(keyValuePairs.nextToken(), ":");
			if (keyValuePair.hasMoreTokens())
				key = keyValuePair.nextToken().trim();
			else
				continue;
			if (keyValuePair.hasMoreTokens())
				value = keyValuePair.nextToken().trim();
			else
				continue;
			if (value.startsWith("\""))
				value = value.substring(1);
			if (value.endsWith("\""))
				value = value.substring(0, value.length() - 1);
			result.setProperty(key.toLowerCase(), value);
		}
		return result;
	}

	/**
	 * Removes the comments sections of a String.
	 * 
	 * @param string
	 *            the original String
	 * @param startComment
	 *            the String that marks the start of a Comment section
	 * @param endComment
	 *            the String that marks the end of a Comment section.
	 * @return the String stripped of its comment section
	 */
	public static String removeComment(String string, String startComment,
			String endComment) {
		StringBuffer result = new StringBuffer();
		int pos = 0;
		int end = endComment.length();
		int start = string.indexOf(startComment, pos);
		while (start > -1) {
			result.append(string.substring(pos, start));
			pos = string.indexOf(endComment, start) + end;
			start = string.indexOf(startComment, pos);
		}
		result.append(string.substring(pos));
		return result.toString();
	}
	
	/**
	 * Helper class that reduces the white space in a String
	 * @param content content containing whitespace
	 * @return the content without all unnecessary whitespace
	 */
	public static String eliminateWhiteSpace(String content) {
		// multiple spaces are reduced to one,
		// newlines are treated as spaces,
		// tabs, carriage returns are ignored.
		StringBuffer buf = new StringBuffer();
		int len = content.length();
		char character;
		boolean newline = false;
		for (int i = 0; i < len; i++) {
			switch (character = content.charAt(i)) {
			case ' ':
				if (!newline) {
					buf.append(character);
				}
				break;
			case '\n':
				if (i > 0) {
					newline = true;
					buf.append(' ');
				}
				break;
			case '\r':
				break;
			case '\t':
				break;
			default:
				newline = false;
				buf.append(character);
			}
		}
		return buf.toString();
	}

	/**
	 * A series of predefined font sizes.
	 * @since 5.0.6 (renamed)
	 */
	public final static int FONTSIZES[] = { 8, 10, 12, 14, 18, 24, 36 };
	
	/**
	 * Picks a font size from a series of predefined font sizes.
	 * @param value		the new value of a font, expressed as an index
	 * @param previous	the previous value of the font size
	 * @return	a new font size.
	 */
	public static int getIndexedFontSize(String value, String previous) {
		// the font is expressed as an index in a series of predefined font sizes
		int sIndex = 0;
		// the font is defined as a relative size
		if (value.startsWith("+") || value.startsWith("-")) {
			// fetch the previous value
			if (previous == null)
				previous = "12";
			int c = (int)Float.parseFloat(previous);
			// look for the nearest font size in the predefined series
			for (int k = FONTSIZES.length - 1; k >= 0; --k) {
				if (c >= FONTSIZES[k]) {
					sIndex = k;
					break;
				}
			}
			// retrieve the difference
			int diff =
				Integer.parseInt(value.startsWith("+") ?
					value.substring(1) : value);
			// apply the difference
			sIndex += diff;
		}
		// the font is defined as an index
		else {
			try {
				sIndex = Integer.parseInt(value) - 1;
			} catch (NumberFormatException nfe) {
				sIndex = 0;
			}
		}
		if (sIndex < 0)
			sIndex = 0;
		else if (sIndex >= FONTSIZES.length)
			sIndex = FONTSIZES.length - 1;
		return FONTSIZES[sIndex];
	}

	/**
	 * Translates a String value to an alignment value.
	 * (written by Norman Richards, integrated into iText by Bruno)
	 * @param	alignment a String (one of the ALIGN_ constants of this class)
	 * @return	an alignment value (one of the ALIGN_ constants of the Element interface) 
	 */
	public static int alignmentValue(String alignment) {
		if (alignment == null) return Element.ALIGN_UNDEFINED;
	    if (HtmlTags.ALIGN_CENTER.equalsIgnoreCase(alignment)) {
	        return Element.ALIGN_CENTER;
	    }
	    if (HtmlTags.ALIGN_LEFT.equalsIgnoreCase(alignment)) {
	        return Element.ALIGN_LEFT;
	    }
	    if (HtmlTags.ALIGN_RIGHT.equalsIgnoreCase(alignment)) {
	        return Element.ALIGN_RIGHT;
	    }
	    if (HtmlTags.ALIGN_JUSTIFY.equalsIgnoreCase(alignment)) {
	        return Element.ALIGN_JUSTIFIED;
	    }
	    if (HtmlTags.ALIGN_JUSTIFIED_ALL.equalsIgnoreCase(alignment)) {
	        return Element.ALIGN_JUSTIFIED_ALL;
	    }
	    if (HtmlTags.ALIGN_TOP.equalsIgnoreCase(alignment)) {
	        return Element.ALIGN_TOP;
	    }
	    if (HtmlTags.ALIGN_MIDDLE.equalsIgnoreCase(alignment)) {
	        return Element.ALIGN_MIDDLE;
	    }
	    if (HtmlTags.ALIGN_BOTTOM.equalsIgnoreCase(alignment)) {
	        return Element.ALIGN_BOTTOM;
	    }
	    if (HtmlTags.ALIGN_BASELINE.equalsIgnoreCase(alignment)) {
	        return Element.ALIGN_BASELINE;
	    }
	    return Element.ALIGN_UNDEFINED;
	}
}
