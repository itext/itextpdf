/*
 * $Id: Utilities.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfEncodings;

/**
 * A collection of convenience methods that were present in many different iText
 * classes.
 */

public class Utilities {

	/**
	 * Gets the keys of a Hashtable.
	 * Marked as deprecated, not used anywhere anymore.
	 * @param <K> type for the key
	 * @param <V> type for the value
	 * @param table
	 *            a Hashtable
	 * @return the keyset of a Hashtable (or an empty set if table is null)
	 */
	@Deprecated
	public static <K, V>  Set<K> getKeySet(final Hashtable<K, V> table) {
		return table == null ? Collections.<K>emptySet() : table.keySet();
	}

	/**
	 * Utility method to extend an array.
	 *
	 * @param original
	 *            the original array or <CODE>null</CODE>
	 * @param item
	 *            the item to be added to the array
	 * @return a new array with the item appended
	 */
	public static Object[][] addToArray(Object original[][], final Object item[]) {
		if (original == null) {
			original = new Object[1][];
			original[0] = item;
			return original;
		} else {
			Object original2[][] = new Object[original.length + 1][];
			System.arraycopy(original, 0, original2, 0, original.length);
			original2[original.length] = item;
			return original2;
		}
	}

	/**
	 * Checks for a true/false value of a key in a Properties object.
	 * @param attributes
	 * @param key
	 * @return a true/false value of a key in a Properties object
	 */
	public static boolean checkTrueOrFalse(final Properties attributes, final String key) {
		return "true".equalsIgnoreCase(attributes.getProperty(key));
	}

	/**
	 * Unescapes an URL. All the "%xx" are replaced by the 'xx' hex char value.
	 * @param src the url to unescape
	 * @return the unescaped value
	 */
	public static String unEscapeURL(final String src) {
	    StringBuffer bf = new StringBuffer();
	    char[] s = src.toCharArray();
	    for (int k = 0; k < s.length; ++k) {
	        char c = s[k];
	        if (c == '%') {
	            if (k + 2 >= s.length) {
	                bf.append(c);
	                continue;
	            }
	            int a0 = PRTokeniser.getHex(s[k + 1]);
	            int a1 = PRTokeniser.getHex(s[k + 2]);
	            if (a0 < 0 || a1 < 0) {
	                bf.append(c);
	                continue;
	            }
	            bf.append((char)(a0 * 16 + a1));
	            k += 2;
	        }
	        else
	            bf.append(c);
	    }
	    return bf.toString();
	}

	/**
	 * This method makes a valid URL from a given filename.
	 * <P>
	 * This method makes the conversion of this library from the JAVA 2 platform
	 * to a JDK1.1.x-version easier.
	 *
	 * @param filename
	 *            a given filename
	 * @return a valid URL
	 * @throws MalformedURLException
	 */
	public static URL toURL(final String filename) throws MalformedURLException {
        try {
            return new URL(filename);
        }
        catch (Exception e) {
            return new File(filename).toURI().toURL();
        }
	}

	/**
	 * This method is an alternative for the <CODE>InputStream.skip()</CODE>
	 * -method that doesn't seem to work properly for big values of <CODE>size
	 * </CODE>.
	 *
	 * @param is
	 *            the <CODE>InputStream</CODE>
	 * @param size
	 *            the number of bytes to skip
	 * @throws IOException
	 */
	static public void skip(final InputStream is, int size) throws IOException {
	    long n;
		while (size > 0) {
	        n = is.skip(size);
	        if (n <= 0)
	            break;
			size -= n;
		}
	}

	/**
	 * Measurement conversion from millimeters to points.
	 * @param	value	a value in millimeters
	 * @return	a value in points
	 * @since	2.1.2
	 */
	public static final float millimetersToPoints(final float value) {
	    return inchesToPoints(millimetersToInches(value));
	}

	/**
	 * Measurement conversion from millimeters to inches.
	 * @param	value	a value in millimeters
	 * @return	a value in inches
	 * @since	2.1.2
	 */
	public static final float millimetersToInches(final float value) {
	    return value / 25.4f;
	}

	/**
	 * Measurement conversion from points to millimeters.
	 * @param	value	a value in points
	 * @return	a value in millimeters
	 * @since	2.1.2
	 */
	public static final float pointsToMillimeters(final float value) {
	    return inchesToMillimeters(pointsToInches(value));
	}

	/**
	 * Measurement conversion from points to inches.
	 * @param	value	a value in points
	 * @return	a value in inches
	 * @since	2.1.2
	 */
	public static final float pointsToInches(final float value) {
	    return value / 72f;
	}

	/**
	 * Measurement conversion from inches to millimeters.
	 * @param	value	a value in inches
	 * @return	a value in millimeters
	 * @since	2.1.2
	 */
	public static final float inchesToMillimeters(final float value) {
	    return value * 25.4f;
	}

	/**
	 * Measurement conversion from inches to points.
	 * @param	value	a value in inches
	 * @return	a value in points
	 * @since	2.1.2
	 */
	public static final float inchesToPoints(final float value) {
	    return value * 72f;
	}

    /**
     * Check if the value of a character belongs to a certain interval
     * that indicates it's the higher part of a surrogate pair.
     * @param c	the character
     * @return	true if the character belongs to the interval
     * @since	2.1.2
     */
    public static boolean isSurrogateHigh(final char c) {
        return c >= '\ud800' && c <= '\udbff';
    }

    /**
     * Check if the value of a character belongs to a certain interval
     * that indicates it's the lower part of a surrogate pair.
     * @param c	the character
     * @return	true if the character belongs to the interval
     * @since	2.1.2
     */
    public static boolean isSurrogateLow(final char c) {
        return c >= '\udc00' && c <= '\udfff';
    }

    /**
     * Checks if two subsequent characters in a String are
     * are the higher and the lower character in a surrogate
     * pair (and therefore eligible for conversion to a UTF 32 character).
     * @param text	the String with the high and low surrogate characters
     * @param idx	the index of the 'high' character in the pair
     * @return	true if the characters are surrogate pairs
     * @since	2.1.2
     */
    public static boolean isSurrogatePair(final String text, final int idx) {
        if (idx < 0 || idx > text.length() - 2)
            return false;
        return isSurrogateHigh(text.charAt(idx)) && isSurrogateLow(text.charAt(idx + 1));
    }

    /**
     * Checks if two subsequent characters in a character array are
     * are the higher and the lower character in a surrogate
     * pair (and therefore eligible for conversion to a UTF 32 character).
     * @param text	the character array with the high and low surrogate characters
     * @param idx	the index of the 'high' character in the pair
     * @return	true if the characters are surrogate pairs
     * @since	2.1.2
     */
    public static boolean isSurrogatePair(final char[] text, final int idx) {
        if (idx < 0 || idx > text.length - 2)
            return false;
        return isSurrogateHigh(text[idx]) && isSurrogateLow(text[idx + 1]);
    }

    /**
     * Returns the code point of a UTF32 character corresponding with
     * a high and a low surrogate value.
     * @param highSurrogate	the high surrogate value
     * @param lowSurrogate	the low surrogate value
     * @return	a code point value
     * @since	2.1.2
     */
    public static int convertToUtf32(final char highSurrogate, final char lowSurrogate) {
         return (highSurrogate - 0xd800) * 0x400 + lowSurrogate - 0xdc00 + 0x10000;
    }

    /**
     * Converts a unicode character in a character array to a UTF 32 code point value.
     * @param text	a character array that has the unicode character(s)
     * @param idx	the index of the 'high' character
     * @return	the code point value
     * @since	2.1.2
     */
    public static int convertToUtf32(final char[] text, final int idx) {
         return (text[idx] - 0xd800) * 0x400 + text[idx + 1] - 0xdc00 + 0x10000;
    }

    /**
     * Converts a unicode character in a String to a UTF32 code point value
     * @param text	a String that has the unicode character(s)
     * @param idx	the index of the 'high' character
     * @return	the codepoint value
     * @since	2.1.2
     */
    public static int convertToUtf32(final String text, final int idx) {
         return (text.charAt(idx) - 0xd800) * 0x400 + text.charAt(idx + 1) - 0xdc00 + 0x10000;
    }

    /**
     * Converts a UTF32 code point value to a String with the corresponding character(s).
     * @param codePoint	a Unicode value
     * @return	the corresponding characters in a String
     * @since	2.1.2
     */
    public static String convertFromUtf32(int codePoint) {
        if (codePoint < 0x10000)
            return Character.toString((char)codePoint);
        codePoint -= 0x10000;
        return new String(new char[]{(char)(codePoint / 0x400 + 0xd800), (char)(codePoint % 0x400 + 0xdc00)});
    }

    /**
     * Reads the contents of a file to a String.
     * @param	path	the path to the file
     * @return	a String with the contents of the file
     * @throws IOException
     * @since	iText 5.0.0
     */
	public static String readFileToString(final String path) throws IOException {
		return readFileToString(new File(path));
	}

    /**
     * Reads the contents of a file to a String.
     * @param	file	a file
     * @return	a String with the contents of the file
     * @throws IOException if file was not found or could not be read.
     * @since	iText 5.0.0
     */
	public static String readFileToString(final File file) throws IOException {
		byte[] jsBytes = new byte[(int) file.length()];
	    FileInputStream f = new FileInputStream(file);
	    f.read(jsBytes);
	    return new String(jsBytes);
	}

	/**
	 * Converts an array of bytes to a String of hexadecimal values
	 * @param bytes	a byte array
	 * @return	the same bytes expressed as hexadecimal values
	 */
	public static String convertToHex(byte[] bytes) {
	    ByteBuffer buf = new ByteBuffer();
	    for (byte b : bytes) {
	        buf.appendHex(b);
	    }
	    return PdfEncodings.convertToString(buf.toByteArray(), null).toUpperCase();
	}
}
